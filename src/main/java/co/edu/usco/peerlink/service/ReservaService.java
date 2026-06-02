package co.edu.usco.peerlink.service;

import co.edu.usco.peerlink.dto.ReservaDetalleDTO;
import co.edu.usco.peerlink.dto.ReservaDTO;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Contrato de negocio para reservas y solicitudes de tutorias.
 *
 * <p>Expone operaciones para crear reservas, consultar horarios por usuario,
 * aplicar filtros y permitir que el tutor actualice estados como confirmada
 * o cancelada.</p>
 */
public interface ReservaService {
    /**
     * Crea una reserva solicitada por un estudiante.
     *
     * @param dto datos de tutor, materia, idioma, facultad y fecha/hora
     * @return reserva creada en estado inicial
     */
    ReservaDTO crearReserva(ReservaDTO dto);

    /**
     * Obtiene reservas resumidas del usuario autenticado segun su rol.
     *
     * @return reservas del estudiante o tutor actual
     */
    List<ReservaDTO> obtenerReservasDelUsuario();

    /**
     * Consulta reservas detalladas del estudiante autenticado con filtros.
     *
     * @param idioma filtro opcional de idioma tecnico
     * @param facultad filtro opcional de facultad tecnica
     * @param desde fecha/hora inicial opcional
     * @param hasta fecha/hora final opcional
     * @return reservas detalladas del estudiante
     */
    List<ReservaDetalleDTO> obtenerMisReservas(String idioma, String facultad, LocalDateTime desde, LocalDateTime hasta);

    /**
     * Consulta tutorias detalladas del tutor autenticado con filtros.
     *
     * @param idioma filtro opcional de idioma tecnico
     * @param facultad filtro opcional de facultad tecnica
     * @param desde fecha/hora inicial opcional
     * @param hasta fecha/hora final opcional
     * @return tutorias detalladas del tutor
     */
    List<ReservaDetalleDTO> obtenerMisTutorias(String idioma, String facultad, LocalDateTime desde, LocalDateTime hasta);

    /**
     * Actualiza el estado de una reserva perteneciente al tutor autenticado.
     *
     * @param reservaId identificador de la reserva
     * @param nuevoEstado estado solicitado
     * @return reserva actualizada
     */
    ReservaDTO actualizarEstado(Integer reservaId, String nuevoEstado);
}
