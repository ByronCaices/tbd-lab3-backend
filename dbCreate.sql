-- 1. Drop database si ya existe (opcional)
DROP DATABASE IF EXISTS ecommercedb;


-- 2. Crear la base de datos si no existe
CREATE DATABASE ecommercedb
    WITH
    OWNER = postgres
    ENCODING = 'UTF8'
    TABLESPACE = pg_default
    CONNECTION LIMIT = -1
    IS_TEMPLATE = False;


-- Usar la base de datos creada
\c ecommercedb;

-- Creación de la base de datos y tablas

CREATE TABLE IF NOT EXISTS usuario (
                                       id_usuario SERIAL PRIMARY KEY,
                                       nombre VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    contrasena VARCHAR(255) NOT NULL
    );


-- Tabla: categoria
CREATE TABLE categoria (
                           id_categoria SERIAL PRIMARY KEY,
                           nombre VARCHAR(100) NOT NULL
);

-- Tabla: producto
CREATE TABLE producto (
                          id_producto SERIAL PRIMARY KEY,
                          nombre VARCHAR(255) NOT NULL,
                          descripcion TEXT,
                          precio DECIMAL(10, 2) NOT NULL,
                          stock INT NOT NULL,
                          estado VARCHAR(50) NOT NULL,
                          id_categoria INTEGER NOT NULL,
                          CONSTRAINT fk_categoria FOREIGN KEY (id_categoria) REFERENCES categoria (id_categoria)
);

-- Tabla: cliente
CREATE TABLE cliente (
                         id_cliente SERIAL PRIMARY KEY,
                         nombre VARCHAR(255) NOT NULL,
                         direccion VARCHAR(255),
                         email VARCHAR(100),
                         telefono VARCHAR(20)
);

-- Tabla: orden
CREATE TABLE orden (
                       id_orden SERIAL PRIMARY KEY,
                       fecha_orden TIMESTAMP NOT NULL,
                       estado VARCHAR(50) NOT NULL,
                       id_cliente INTEGER NOT NULL,
                       total DECIMAL(10, 2) NOT NULL,
                       CONSTRAINT fk_cliente FOREIGN KEY (id_cliente) REFERENCES cliente (id_cliente)
);

-- Tabla: detalle_orden
CREATE TABLE detalle_orden (
                               id_detalle SERIAL PRIMARY KEY,
                               id_orden INTEGER NOT NULL,
                               id_producto INTEGER NOT NULL,
                               cantidad INT NOT NULL,
                               precio_unitario DECIMAL(10, 2) NOT NULL,
                               CONSTRAINT fk_orden FOREIGN KEY (id_orden) REFERENCES orden (id_orden) ON DELETE CASCADE,
                               CONSTRAINT fk_producto FOREIGN KEY (id_producto) REFERENCES producto (id_producto)
);

CREATE TABLE auditoria (
                           id_auditoria SERIAL PRIMARY KEY, -- Identificador único para cada registro
                           accion VARCHAR(50) NOT NULL,    -- Tipo de acción: INSERT, UPDATE, DELETE
                           nombre_tabla VARCHAR(100) NOT NULL, -- Nombre de la tabla afectada
                           id_registro INTEGER NOT NULL,   -- ID del registro afectado
                           usuario VARCHAR(255) NOT NULL,  -- Usuario que ejecutó la acción
                           descripcion TEXT,               -- Descripción de los datos (formato JSON)
                           fecha TIMESTAMP DEFAULT CURRENT_TIMESTAMP -- Fecha y hora de la acción
);


-- Funcion: auditar_accion
-- Description: Esta función registra las acciones de insert, update y delete en la tabla cliente
-- CALL auditar_accion();

CREATE OR REPLACE FUNCTION auditar_accion()
RETURNS TRIGGER AS $$
BEGIN
    IF TG_OP = 'INSERT' THEN
        INSERT INTO auditoria (accion, nombre_tabla, id_registro, usuario, descripcion)
        VALUES ('INSERT', TG_TABLE_NAME, NEW.id_cliente, current_user, row_to_json(NEW)::text);
RETURN NEW;
ELSIF TG_OP = 'UPDATE' THEN
        INSERT INTO auditoria (accion, nombre_tabla, id_registro, usuario, descripcion)
        VALUES ('UPDATE', TG_TABLE_NAME, NEW.id_cliente, current_user, row_to_json(NEW)::text);
RETURN NEW;
ELSIF TG_OP = 'DELETE' THEN
        INSERT INTO auditoria (accion, nombre_tabla, id_registro, usuario, descripcion)
        VALUES ('DELETE', TG_TABLE_NAME, OLD.id_cliente, current_user, row_to_json(OLD)::text);
RETURN OLD;
END IF;
END;
$$ LANGUAGE plpgsql;

-- Requerimiento 16
-- Trigger de auditoria, registra las querys de de insert, update y delete en la tabla cliente
CREATE TRIGGER trigger_auditoria
    AFTER INSERT OR UPDATE OR DELETE ON cliente
    FOR EACH ROW EXECUTE FUNCTION auditar_accion();


-- Requerimiento 17
-- Descripción: Este procedimiento consulta la tabla auditoria, agrupa las acciones (INSERT, UPDATE, DELETE) por usuario y devuelve los usuarios con mayor cantidad de acciones ejecutadas.
-- Ej: SELECT * FROM reporte_usuarios_mas_activos();
DROP FUNCTION IF EXISTS reporte_usuarios_mas_activos();

CREATE OR REPLACE FUNCTION reporte_usuarios_mas_activos()
    RETURNS TABLE(id_registro INTEGER, total_queries BIGINT) AS $$
BEGIN
    RETURN QUERY
        SELECT a.id_registro, COUNT(*) AS total_queries
        FROM auditoria a
        GROUP BY a.id_registro
        ORDER BY total_queries DESC;
END;
$$ LANGUAGE plpgsql;

--Requerimiento 24
-- Descripción: Este procedimiento actualiza el stock de los productos devueltos y, si todos los productos de una orden han sido devueltos, cambia el estado de la orden a 'devuelto'.
-- ej: CALL gestionar_devolucion_proc(1, 2, 3); id_orden=1, id_producto=2, cantidad=3

CREATE OR REPLACE PROCEDURE gestionar_devolucion_proc(
    IN p_id_orden INTEGER,
    IN p_id_producto INTEGER,
    IN p_cantidad INTEGER
)
LANGUAGE plpgsql AS $$
DECLARE
    nuevo_total DECIMAL(10, 2);
    precio_unit DECIMAL(10, 2);
BEGIN
    -- Actualizar el stock del producto
    UPDATE producto
    SET stock = stock + p_cantidad
    WHERE id_producto = p_id_producto;

    -- Obtener el precio unitario del producto en el detalle de la orden
    SELECT precio_unitario INTO precio_unit
    FROM detalle_orden
    WHERE id_orden = p_id_orden AND id_producto = p_id_producto;

    -- Calcular el nuevo total
    SELECT total - (precio_unit * p_cantidad) INTO nuevo_total
    FROM orden
    WHERE id_orden = p_id_orden;

    -- Actualizar el total de la orden
    UPDATE orden
    SET total = nuevo_total
    WHERE id_orden = p_id_orden;



    -- Verificar si la orden debe actualizar su estado
    IF (SELECT COUNT(*) FROM detalle_orden WHERE id_orden = p_id_orden AND cantidad > 0) = 0 THEN
        UPDATE orden
        SET estado = 'devuelto'
        WHERE id_orden = p_id_orden;
    END IF;
END;
$$;



-- Requerimiento 25
-- Descripción: Este procedimiento recorre la tabla producto y actualiza el estado de los productos cuyo stock sea menor o igual a cero, marcándolos como "no disponible".
-- Ej: CALL desactivar_productos_sin_stock();
CREATE OR REPLACE PROCEDURE desactivar_productos_sin_stock()
LANGUAGE plpgsql AS $$
BEGIN
    UPDATE producto
    SET estado = 'Agotado'
    WHERE stock <= 0;
END;
$$;



-- requerimiento 51
-- Este procedimiento recibe un rango de fechas (p_fecha_inicio y p_fecha_fin), identifica los clientes que realizaron más de una compra en un día y devuelve la lista de productos comprados.
-- Ej: CALL clientes_multiples_compras_y_productos('2024-10-01', '2024-10-31');
CREATE OR REPLACE PROCEDURE clientes_multiples_compras_y_productos(
    IN p_fecha_inicio DATE,
    IN p_fecha_fin DATE
)
LANGUAGE plpgsql AS $$
BEGIN
    SELECT c.nombre AS cliente, o.fecha_orden, p.nombre AS producto, COUNT(o.id_orden) AS num_compras
    FROM cliente c
    JOIN orden o ON c.id_cliente = o.id_cliente
    JOIN detalle_orden d ON o.id_orden = d.id_orden
    JOIN producto p ON d.id_producto = p.id_producto
    WHERE o.fecha_orden BETWEEN p_fecha_inicio AND p_fecha_fin
    GROUP BY c.nombre, o.fecha_orden, p.nombre
    HAVING COUNT(o.id_orden) > 1
    ORDER BY c.nombre, o.fecha_orden;
END;
$$;

