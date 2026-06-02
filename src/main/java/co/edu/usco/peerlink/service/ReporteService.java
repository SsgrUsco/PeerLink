package co.edu.usco.peerlink.service;

import java.time.LocalDateTime;

/**
 * Contrato para generacion de reportes PDF del sistema.
 *
 * <p>Las implementaciones usan JasperReports Library para producir arreglos
 * de bytes con contenido PDF descargable desde los endpoints REST.</p>
 */
public interface ReporteService {
    /**
     * Genera el reporte de reservas del estudiante autenticado.
     *
     * @param idioma filtro opcional de idioma tecnico
     * @param facultad filtro opcional de facultad tecnica
     * @param desde fecha/hora inicial opcional
     * @param hasta fecha/hora final opcional
     * @return contenido binario del PDF
     */
    byte[] generarReporteMisReservas(String idioma, String facultad, LocalDateTime desde, LocalDateTime hasta);

    /**
     * Genera el reporte de tutorias del tutor autenticado.
     *
     * @param idioma filtro opcional de idioma tecnico
     * @param facultad filtro opcional de facultad tecnica
     * @param desde fecha/hora inicial opcional
     * @param hasta fecha/hora final opcional
     * @return contenido binario del PDF
     */
    byte[] generarReporteMisTutorias(String idioma, String facultad, LocalDateTime desde, LocalDateTime hasta);

    /**
     * Genera el resumen administrativo global.
     *
     * @return contenido binario del PDF
     */
    byte[] generarReporteAdminResumen();

    /**
     * Genera el reporte administrativo de tutores.
     *
     * @return contenido binario del PDF
     */
    byte[] generarReporteAdminTutores();

    /**
     * Genera el reporte administrativo de usuarios.
     *
     * @return contenido binario del PDF
     */
    byte[] generarReporteAdminUsuarios();
}
