package co.edu.usco.peerlink.service;

import co.edu.usco.peerlink.dto.MateriaDTO;
import co.edu.usco.peerlink.dto.ReservaDetalleDTO;
import co.edu.usco.peerlink.dto.TutorMateriaDetalleDTO;
import co.edu.usco.peerlink.dto.UsuarioDTO;
import co.edu.usco.peerlink.exception.BusinessException;
import co.edu.usco.peerlink.security.AuthenticatedUser;
import co.edu.usco.peerlink.security.SecurityUtils;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRMapCollectionDataSource;
import net.sf.jasperreports.engine.design.JRDesignBand;
import net.sf.jasperreports.engine.design.JRDesignExpression;
import net.sf.jasperreports.engine.design.JRDesignField;
import net.sf.jasperreports.engine.design.JRDesignParameter;
import net.sf.jasperreports.engine.design.JRDesignStaticText;
import net.sf.jasperreports.engine.design.JRDesignStyle;
import net.sf.jasperreports.engine.design.JRDesignTextField;
import net.sf.jasperreports.engine.design.JRDesignSection;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.type.HorizontalTextAlignEnum;
import net.sf.jasperreports.engine.type.ModeEnum;
import net.sf.jasperreports.engine.type.OrientationEnum;
import net.sf.jasperreports.engine.type.VerticalTextAlignEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.awt.Color;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Genera reportes PDF mediante JasperReports para estudiantes, tutores y administradores.
 */
@Service
public class ReporteServiceImpl implements ReporteService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ReporteServiceImpl.class);
    private static final int PAGE_WIDTH = 842;
    private static final int PAGE_HEIGHT = 595;
    private static final int MARGIN = 24;
    private static final int CONTENT_WIDTH = PAGE_WIDTH - (MARGIN * 2);
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm", Locale.ROOT);

    private final ReservaService reservaService;
    private final MateriaService materiaService;
    private final UsuarioService usuarioService;

    /**
     * Inyecta los servicios de datos usados para construir reportes.
     *
     * @param reservaService servicio de reservas
     * @param materiaService servicio de materias y asignaciones
     * @param usuarioService servicio de usuarios
     */
    public ReporteServiceImpl(ReservaService reservaService,
                              MateriaService materiaService,
                              UsuarioService usuarioService) {
        this.reservaService = reservaService;
        this.materiaService = materiaService;
        this.usuarioService = usuarioService;
    }

    @Override
    /**
     * Genera el PDF de reservas del estudiante autenticado.
     *
     * @param idioma filtro por idioma
     * @param facultad filtro por facultad
     * @param desde fecha inicial
     * @param hasta fecha final
     * @return bytes del PDF generado
     */
    public byte[] generarReporteMisReservas(String idioma, String facultad, LocalDateTime desde, LocalDateTime hasta) {
        AuthenticatedUser usuario = currentUser();
        List<ReservaDetalleDTO> reservas = reservaService.obtenerMisReservas(idioma, facultad, desde, hasta);
        List<ReportColumn> columns = List.of(
                new ReportColumn("materia", "Materia", 150),
                new ReportColumn("tutor", "Tutor", 150),
                new ReportColumn("fechaHora", "Fecha y hora", 105),
                new ReportColumn("idioma", "Idioma", 55),
                new ReportColumn("facultad", "Facultad", 190),
                new ReportColumn("estado", "Estado", 90)
        );
        List<Map<String, ?>> rows = new ArrayList<>();
        reservas.forEach(item -> rows.add(row(
                        "materia", item.getMateriaNombre(),
                        "tutor", item.getTutorNombre(),
                        "fechaHora", formatDateTime(item.getFechaHora()),
                        "idioma", item.getIdioma(),
                        "facultad", item.getFacultad(),
                        "estado", item.getEstado()
                )));
        return buildPdf("Reporte de mis reservas", subtitle(usuario), columns, rows);
    }

    @Override
    /**
     * Genera el PDF de tutorias del tutor autenticado.
     *
     * @param idioma filtro por idioma
     * @param facultad filtro por facultad
     * @param desde fecha inicial
     * @param hasta fecha final
     * @return bytes del PDF generado
     */
    public byte[] generarReporteMisTutorias(String idioma, String facultad, LocalDateTime desde, LocalDateTime hasta) {
        AuthenticatedUser usuario = currentUser();
        List<ReservaDetalleDTO> reservas = reservaService.obtenerMisTutorias(idioma, facultad, desde, hasta);
        List<ReportColumn> columns = List.of(
                new ReportColumn("estudiante", "Estudiante", 150),
                new ReportColumn("materia", "Materia", 150),
                new ReportColumn("fechaHora", "Fecha y hora", 105),
                new ReportColumn("idioma", "Idioma", 55),
                new ReportColumn("facultad", "Facultad", 190),
                new ReportColumn("estado", "Estado", 90)
        );
        List<Map<String, ?>> rows = new ArrayList<>();
        reservas.forEach(item -> rows.add(row(
                        "estudiante", item.getEstudianteNombre(),
                        "materia", item.getMateriaNombre(),
                        "fechaHora", formatDateTime(item.getFechaHora()),
                        "idioma", item.getIdioma(),
                        "facultad", item.getFacultad(),
                        "estado", item.getEstado()
                )));
        return buildPdf("Reporte de mis tutorias", subtitle(usuario), columns, rows);
    }

    @Override
    /**
     * Genera un reporte administrativo resumido de materias.
     *
     * @return bytes del PDF generado
     */
    public byte[] generarReporteAdminResumen() {
        List<UsuarioDTO> usuarios = usuarioService.obtenerTodos();
        List<MateriaDTO> materias = materiaService.obtenerTodas();
        List<TutorMateriaDetalleDTO> asignaciones = materiaService.listarAsignacionesTutorMateria();
        List<ReportColumn> columns = List.of(
                new ReportColumn("tipo", "Tipo", 160),
                new ReportColumn("detalle", "Detalle", 420),
                new ReportColumn("total", "Total", 160)
        );
        List<Map<String, ?>> rows = List.of(
                row("tipo", "Usuarios", "detalle", "Usuarios registrados en PeerLink", "total", String.valueOf(usuarios.size())),
                row("tipo", "Materias", "detalle", "Materias academicas creadas", "total", String.valueOf(materias.size())),
                row("tipo", "Asignaciones", "detalle", "Relaciones tutor-materia", "total", String.valueOf(asignaciones.size()))
        );
        return buildPdf("Reporte administrativo", "Resumen general de PeerLink", columns, rows);
    }

    @Override
    /**
     * Genera un reporte administrativo de asignaciones tutor-materia.
     *
     * @return bytes del PDF generado
     */
    public byte[] generarReporteAdminTutores() {
        List<UsuarioDTO> tutores = usuarioService.obtenerTodos().stream()
                .filter(usuario -> "TUTOR".equalsIgnoreCase(usuario.getRol()))
                .toList();
        List<TutorMateriaDetalleDTO> asignaciones = materiaService.listarAsignacionesTutorMateria();
        List<ReportColumn> columns = List.of(
                new ReportColumn("tutor", "Tutor", 160),
                new ReportColumn("correo", "Correo", 165),
                new ReportColumn("materia", "Materia", 150),
                new ReportColumn("idioma", "Idioma", 55),
                new ReportColumn("facultad", "Facultad", 190),
                new ReportColumn("fechaHora", "Fecha y hora", 70)
        );
        List<Map<String, ?>> rows = new ArrayList<>();

        tutores.forEach(tutor -> {
            List<TutorMateriaDetalleDTO> tutorAsignaciones = asignaciones.stream()
                    .filter(item -> tutor.getId().equals(item.getTutorId()))
                    .toList();
            if (tutorAsignaciones.isEmpty()) {
                rows.add(row(
                        "tutor", tutor.getNombreCompleto(),
                        "correo", tutor.getCorreo(),
                        "materia", "Sin materias asignadas",
                        "idioma", "",
                        "facultad", "",
                        "fechaHora", ""
                ));
                return;
            }
            tutorAsignaciones.forEach(item -> rows.add(row(
                    "tutor", item.getTutorNombre(),
                    "correo", item.getTutorCorreo(),
                    "materia", item.getMateriaNombre(),
                    "idioma", item.getIdioma(),
                    "facultad", item.getFacultad(),
                    "fechaHora", formatDateTime(item.getFechaHora())
            )));
        });

        return buildPdf("Reporte de tutores", "Tutores registrados y materias asociadas", columns, rows);
    }

    @Override
    /**
     * Genera un reporte administrativo de usuarios.
     *
     * @return bytes del PDF generado
     */
    public byte[] generarReporteAdminUsuarios() {
        List<UsuarioDTO> usuarios = usuarioService.obtenerTodos();
        List<ReportColumn> columns = List.of(
                new ReportColumn("id", "ID", 70),
                new ReportColumn("nombre", "Nombre", 250),
                new ReportColumn("correo", "Correo", 300),
                new ReportColumn("rol", "Rol", 170)
        );
        List<Map<String, ?>> rows = new ArrayList<>();
        usuarios.forEach(usuario -> rows.add(row(
                "id", String.valueOf(usuario.getId()),
                "nombre", usuario.getNombreCompleto(),
                "correo", usuario.getCorreo(),
                "rol", usuario.getRol()
        )));

        return buildPdf("Reporte de usuarios", "Usuarios registrados en PeerLink", columns, rows);
    }

    private byte[] buildPdf(String title, String subtitle, List<ReportColumn> columns, List<Map<String, ?>> rows) {
        try {
            JasperDesign design = createDesign(title, subtitle, columns);
            JasperReport report = JasperCompileManager.compileReport(design);
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("REPORT_TITLE", title);
            parameters.put("REPORT_SUBTITLE", subtitle);
            parameters.put("GENERATED_AT", "Generado: " + formatDateTime(LocalDateTime.now()));
            JasperPrint print = JasperFillManager.fillReport(report, parameters, new JRMapCollectionDataSource(rows));
            return JasperExportManager.exportReportToPdf(print);
        } catch (JRException | RuntimeException exception) {
            LOGGER.error("No fue posible generar el reporte PDF: {}", title, exception);
            throw new BusinessException("error.reporte.generacion", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private JasperDesign createDesign(String title, String subtitle, List<ReportColumn> columns) throws JRException {
        JasperDesign design = new JasperDesign();
        design.setName(safeReportName(title));
        design.setPageWidth(PAGE_WIDTH);
        design.setPageHeight(PAGE_HEIGHT);
        design.setOrientation(OrientationEnum.LANDSCAPE);
        design.setLeftMargin(MARGIN);
        design.setRightMargin(MARGIN);
        design.setTopMargin(MARGIN);
        design.setBottomMargin(MARGIN);
        design.setColumnWidth(CONTENT_WIDTH);
        design.addStyle(baseStyle());
        addParameters(design);
        addFields(design, columns);
        design.setTitle(titleBand());
        design.setPageHeader(headerBand(columns));
        ((JRDesignSection) design.getDetailSection()).addBand(detailBand(columns));
        return design;
    }

    private JRDesignStyle baseStyle() {
        JRDesignStyle style = new JRDesignStyle();
        style.setName("PeerLinkBase");
        style.setDefault(true);
        style.setFontName("SansSerif");
        style.setFontSize(9f);
        style.setPdfFontName("Helvetica");
        style.setPdfEncoding("Cp1252");
        style.setPdfEmbedded(false);
        return style;
    }

    private void addParameters(JasperDesign design) throws JRException {
        for (String name : List.of("REPORT_TITLE", "REPORT_SUBTITLE", "GENERATED_AT")) {
            JRDesignParameter parameter = new JRDesignParameter();
            parameter.setName(name);
            parameter.setValueClass(String.class);
            design.addParameter(parameter);
        }
    }

    private void addFields(JasperDesign design, List<ReportColumn> columns) throws JRException {
        for (ReportColumn column : columns) {
            JRDesignField field = new JRDesignField();
            field.setName(column.key());
            field.setValueClass(String.class);
            design.addField(field);
        }
    }

    private JRDesignBand titleBand() {
        JRDesignBand band = new JRDesignBand();
        band.setHeight(72);
        band.addElement(textField("$P{REPORT_TITLE}", 0, 0, CONTENT_WIDTH, 28, 18f, true));
        band.addElement(textField("$P{REPORT_SUBTITLE}", 0, 30, CONTENT_WIDTH, 18, 10f, false));
        band.addElement(textField("$P{GENERATED_AT}", 0, 50, CONTENT_WIDTH, 16, 8f, false));
        return band;
    }

    private JRDesignBand headerBand(List<ReportColumn> columns) {
        JRDesignBand band = new JRDesignBand();
        band.setHeight(24);
        int x = 0;
        for (ReportColumn column : columns) {
            JRDesignStaticText text = staticText(column.label(), x, 0, column.width(), 24, true);
            text.setBackcolor(new Color(31, 143, 93));
            text.setForecolor(Color.WHITE);
            text.setMode(ModeEnum.OPAQUE);
            band.addElement(text);
            x += column.width();
        }
        return band;
    }

    private JRDesignBand detailBand(List<ReportColumn> columns) {
        JRDesignBand band = new JRDesignBand();
        band.setHeight(24);
        int x = 0;
        for (ReportColumn column : columns) {
            band.addElement(textField("$F{" + column.key() + "}", x, 0, column.width(), 24, 8.5f, false));
            x += column.width();
        }
        return band;
    }

    private JRDesignStaticText staticText(String value, int x, int y, int width, int height, boolean bold) {
        JRDesignStaticText text = new JRDesignStaticText();
        text.setX(x);
        text.setY(y);
        text.setWidth(width);
        text.setHeight(height);
        text.setText(value);
        text.setFontSize(8.5f);
        text.setBold(bold);
        text.setHorizontalTextAlign(HorizontalTextAlignEnum.LEFT);
        text.setVerticalTextAlign(VerticalTextAlignEnum.MIDDLE);
        return text;
    }

    private JRDesignTextField textField(String expressionText, int x, int y, int width, int height, float size, boolean bold) {
        JRDesignTextField text = new JRDesignTextField();
        text.setX(x);
        text.setY(y);
        text.setWidth(width);
        text.setHeight(height);
        text.setFontSize(size);
        text.setBold(bold);
        text.setBlankWhenNull(true);
        text.setHorizontalTextAlign(HorizontalTextAlignEnum.LEFT);
        text.setVerticalTextAlign(VerticalTextAlignEnum.MIDDLE);
        JRDesignExpression expression = new JRDesignExpression();
        expression.setText(expressionText);
        text.setExpression(expression);
        return text;
    }

    private String subtitle(AuthenticatedUser usuario) {
        return usuario.getNombreCompleto() + " - " + usuario.getUsername();
    }

    private AuthenticatedUser currentUser() {
        return SecurityUtils.currentUser();
    }

    private String formatDateTime(LocalDateTime value) {
        return value == null ? "" : value.format(DATE_TIME_FORMATTER);
    }

    private String safeReportName(String title) {
        return title.toLowerCase(Locale.ROOT).replaceAll("[^a-z0-9]+", "_");
    }

    private Map<String, ?> row(String key1, String value1, String key2, String value2, String key3, String value3) {
        Map<String, Object> row = new HashMap<>();
        row.put(key1, valueOrEmpty(value1));
        row.put(key2, valueOrEmpty(value2));
        row.put(key3, valueOrEmpty(value3));
        return row;
    }

    private Map<String, ?> row(String key1, String value1, String key2, String value2, String key3, String value3,
                               String key4, String value4) {
        Map<String, Object> row = new HashMap<>();
        row.put(key1, valueOrEmpty(value1));
        row.put(key2, valueOrEmpty(value2));
        row.put(key3, valueOrEmpty(value3));
        row.put(key4, valueOrEmpty(value4));
        return row;
    }

    private Map<String, ?> row(String key1, String value1, String key2, String value2, String key3, String value3,
                               String key4, String value4, String key5, String value5, String key6, String value6) {
        Map<String, Object> row = new HashMap<>();
        row.put(key1, valueOrEmpty(value1));
        row.put(key2, valueOrEmpty(value2));
        row.put(key3, valueOrEmpty(value3));
        row.put(key4, valueOrEmpty(value4));
        row.put(key5, valueOrEmpty(value5));
        row.put(key6, valueOrEmpty(value6));
        return row;
    }

    private String valueOrEmpty(String value) {
        return value == null ? "" : value;
    }

    private record ReportColumn(String key, String label, int width) {
    }
}
