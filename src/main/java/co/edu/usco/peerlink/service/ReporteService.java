package co.edu.usco.peerlink.service;

import java.time.LocalDateTime;

public interface ReporteService {
    byte[] generarReporteMisReservas(String idioma, String facultad, LocalDateTime desde, LocalDateTime hasta);
    byte[] generarReporteMisTutorias(String idioma, String facultad, LocalDateTime desde, LocalDateTime hasta);
    byte[] generarReporteAdminResumen();
    byte[] generarReporteAdminTutores();
    byte[] generarReporteAdminUsuarios();
}
