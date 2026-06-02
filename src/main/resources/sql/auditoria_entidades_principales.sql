-- ========================================================
-- AUDITORIA DE ENTIDADES PRINCIPALES PARA PEERLINK
-- PostgreSQL
--
-- Objetivo:
-- Registrar movimientos de las entidades principales del sistema
-- directamente desde la base de datos mediante triggers.
--
-- Entidades cubiertas:
-- - usuarios
-- - materias
-- - reservas
--
-- Cada tabla de auditoria guarda:
-- - id de la auditoria
-- - id de la entidad afectada
-- - movimiento realizado: INSERT, UPDATE o DELETE
-- - tabla origen que disparo el evento
-- - fecha y hora
-- - datos anteriores y datos nuevos en JSONB
-- ========================================================

BEGIN;

-- ========================================================
-- 1. AUDITORIA DE USUARIOS
-- Incluye la tabla principal usuarios y sus tablas satelite 6NF.
-- ========================================================

CREATE TABLE IF NOT EXISTS auditoria_usuarios (
    id SERIAL PRIMARY KEY,
    usuario_id INTEGER,
    movimiento VARCHAR(20) NOT NULL,
    tabla_origen VARCHAR(80) NOT NULL,
    fecha_hora TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    datos_anteriores JSONB,
    datos_nuevos JSONB
);

CREATE INDEX IF NOT EXISTS idx_auditoria_usuarios_usuario_id
    ON auditoria_usuarios (usuario_id);

CREATE INDEX IF NOT EXISTS idx_auditoria_usuarios_fecha_hora
    ON auditoria_usuarios (fecha_hora DESC);

CREATE OR REPLACE FUNCTION fn_auditoria_usuarios()
RETURNS TRIGGER AS $$
DECLARE
    entidad_id INTEGER;
BEGIN
    entidad_id := COALESCE(NEW.id, OLD.id);

    INSERT INTO auditoria_usuarios (
        usuario_id,
        movimiento,
        tabla_origen,
        datos_anteriores,
        datos_nuevos
    )
    VALUES (
        entidad_id,
        TG_OP,
        TG_TABLE_NAME,
        CASE WHEN TG_OP IN ('UPDATE', 'DELETE') THEN to_jsonb(OLD) ELSE NULL END,
        CASE WHEN TG_OP IN ('INSERT', 'UPDATE') THEN to_jsonb(NEW) ELSE NULL END
    );

    RETURN COALESCE(NEW, OLD);
END;
$$ LANGUAGE plpgsql;

DROP TRIGGER IF EXISTS trg_auditoria_usuarios ON usuarios;
CREATE TRIGGER trg_auditoria_usuarios
AFTER INSERT OR UPDATE OR DELETE ON usuarios
FOR EACH ROW EXECUTE FUNCTION fn_auditoria_usuarios();

DROP TRIGGER IF EXISTS trg_auditoria_usuarios_nombre ON usuarios_nombre;
CREATE TRIGGER trg_auditoria_usuarios_nombre
AFTER INSERT OR UPDATE OR DELETE ON usuarios_nombre
FOR EACH ROW EXECUTE FUNCTION fn_auditoria_usuarios();

DROP TRIGGER IF EXISTS trg_auditoria_usuarios_correo ON usuarios_correo;
CREATE TRIGGER trg_auditoria_usuarios_correo
AFTER INSERT OR UPDATE OR DELETE ON usuarios_correo
FOR EACH ROW EXECUTE FUNCTION fn_auditoria_usuarios();

DROP TRIGGER IF EXISTS trg_auditoria_usuarios_password ON usuarios_password;
CREATE TRIGGER trg_auditoria_usuarios_password
AFTER INSERT OR UPDATE OR DELETE ON usuarios_password
FOR EACH ROW EXECUTE FUNCTION fn_auditoria_usuarios();

DROP TRIGGER IF EXISTS trg_auditoria_usuarios_rol ON usuarios_rol;
CREATE TRIGGER trg_auditoria_usuarios_rol
AFTER INSERT OR UPDATE OR DELETE ON usuarios_rol
FOR EACH ROW EXECUTE FUNCTION fn_auditoria_usuarios();

-- ========================================================
-- 2. AUDITORIA DE MATERIAS
-- Incluye materias y sus tablas satelite idioma/facultad.
-- ========================================================

CREATE TABLE IF NOT EXISTS auditoria_materias (
    id SERIAL PRIMARY KEY,
    materia_id INTEGER,
    movimiento VARCHAR(20) NOT NULL,
    tabla_origen VARCHAR(80) NOT NULL,
    fecha_hora TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    datos_anteriores JSONB,
    datos_nuevos JSONB
);

CREATE INDEX IF NOT EXISTS idx_auditoria_materias_materia_id
    ON auditoria_materias (materia_id);

CREATE INDEX IF NOT EXISTS idx_auditoria_materias_fecha_hora
    ON auditoria_materias (fecha_hora DESC);

CREATE OR REPLACE FUNCTION fn_auditoria_materias()
RETURNS TRIGGER AS $$
DECLARE
    entidad_id INTEGER;
BEGIN
    entidad_id := COALESCE(NEW.id, OLD.id);

    INSERT INTO auditoria_materias (
        materia_id,
        movimiento,
        tabla_origen,
        datos_anteriores,
        datos_nuevos
    )
    VALUES (
        entidad_id,
        TG_OP,
        TG_TABLE_NAME,
        CASE WHEN TG_OP IN ('UPDATE', 'DELETE') THEN to_jsonb(OLD) ELSE NULL END,
        CASE WHEN TG_OP IN ('INSERT', 'UPDATE') THEN to_jsonb(NEW) ELSE NULL END
    );

    RETURN COALESCE(NEW, OLD);
END;
$$ LANGUAGE plpgsql;

DROP TRIGGER IF EXISTS trg_auditoria_materias ON materias;
CREATE TRIGGER trg_auditoria_materias
AFTER INSERT OR UPDATE OR DELETE ON materias
FOR EACH ROW EXECUTE FUNCTION fn_auditoria_materias();

DROP TRIGGER IF EXISTS trg_auditoria_materias_idioma ON materias_idioma;
CREATE TRIGGER trg_auditoria_materias_idioma
AFTER INSERT OR UPDATE OR DELETE ON materias_idioma
FOR EACH ROW EXECUTE FUNCTION fn_auditoria_materias();

DROP TRIGGER IF EXISTS trg_auditoria_materias_facultad ON materias_facultad;
CREATE TRIGGER trg_auditoria_materias_facultad
AFTER INSERT OR UPDATE OR DELETE ON materias_facultad
FOR EACH ROW EXECUTE FUNCTION fn_auditoria_materias();

-- ========================================================
-- 3. AUDITORIA DE RESERVAS
-- Incluye reservas y sus tablas satelite 6NF.
-- ========================================================

CREATE TABLE IF NOT EXISTS auditoria_reservas (
    id SERIAL PRIMARY KEY,
    reserva_id INTEGER,
    movimiento VARCHAR(20) NOT NULL,
    tabla_origen VARCHAR(80) NOT NULL,
    fecha_hora TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    datos_anteriores JSONB,
    datos_nuevos JSONB
);

CREATE INDEX IF NOT EXISTS idx_auditoria_reservas_reserva_id
    ON auditoria_reservas (reserva_id);

CREATE INDEX IF NOT EXISTS idx_auditoria_reservas_fecha_hora
    ON auditoria_reservas (fecha_hora DESC);

CREATE OR REPLACE FUNCTION fn_auditoria_reservas()
RETURNS TRIGGER AS $$
DECLARE
    entidad_id INTEGER;
BEGIN
    entidad_id := COALESCE(NEW.id, OLD.id);

    INSERT INTO auditoria_reservas (
        reserva_id,
        movimiento,
        tabla_origen,
        datos_anteriores,
        datos_nuevos
    )
    VALUES (
        entidad_id,
        TG_OP,
        TG_TABLE_NAME,
        CASE WHEN TG_OP IN ('UPDATE', 'DELETE') THEN to_jsonb(OLD) ELSE NULL END,
        CASE WHEN TG_OP IN ('INSERT', 'UPDATE') THEN to_jsonb(NEW) ELSE NULL END
    );

    RETURN COALESCE(NEW, OLD);
END;
$$ LANGUAGE plpgsql;

DROP TRIGGER IF EXISTS trg_auditoria_reservas ON reservas;
CREATE TRIGGER trg_auditoria_reservas
AFTER INSERT OR UPDATE OR DELETE ON reservas
FOR EACH ROW EXECUTE FUNCTION fn_auditoria_reservas();

DROP TRIGGER IF EXISTS trg_auditoria_reservas_estudiante ON reservas_estudiante;
CREATE TRIGGER trg_auditoria_reservas_estudiante
AFTER INSERT OR UPDATE OR DELETE ON reservas_estudiante
FOR EACH ROW EXECUTE FUNCTION fn_auditoria_reservas();

DROP TRIGGER IF EXISTS trg_auditoria_reservas_tutor_materia ON reservas_tutor_materia;
CREATE TRIGGER trg_auditoria_reservas_tutor_materia
AFTER INSERT OR UPDATE OR DELETE ON reservas_tutor_materia
FOR EACH ROW EXECUTE FUNCTION fn_auditoria_reservas();

DROP TRIGGER IF EXISTS trg_auditoria_reservas_fecha ON reservas_fecha;
CREATE TRIGGER trg_auditoria_reservas_fecha
AFTER INSERT OR UPDATE OR DELETE ON reservas_fecha
FOR EACH ROW EXECUTE FUNCTION fn_auditoria_reservas();

DROP TRIGGER IF EXISTS trg_auditoria_reservas_estado ON reservas_estado;
CREATE TRIGGER trg_auditoria_reservas_estado
AFTER INSERT OR UPDATE OR DELETE ON reservas_estado
FOR EACH ROW EXECUTE FUNCTION fn_auditoria_reservas();

DROP TRIGGER IF EXISTS trg_auditoria_reservas_idioma ON reservas_idioma;
CREATE TRIGGER trg_auditoria_reservas_idioma
AFTER INSERT OR UPDATE OR DELETE ON reservas_idioma
FOR EACH ROW EXECUTE FUNCTION fn_auditoria_reservas();

DROP TRIGGER IF EXISTS trg_auditoria_reservas_facultad ON reservas_facultad;
CREATE TRIGGER trg_auditoria_reservas_facultad
AFTER INSERT OR UPDATE OR DELETE ON reservas_facultad
FOR EACH ROW EXECUTE FUNCTION fn_auditoria_reservas();

COMMIT;

-- ========================================================
-- CONSULTAS DE VERIFICACION PARA LA SUSTENTACION
-- ========================================================

-- SELECT * FROM auditoria_usuarios ORDER BY fecha_hora DESC;
-- SELECT * FROM auditoria_materias ORDER BY fecha_hora DESC;
-- SELECT * FROM auditoria_reservas ORDER BY fecha_hora DESC;
