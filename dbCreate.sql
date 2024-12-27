-- 1. Drop database si ya existe (opcional)
DROP DATABASE IF EXISTS ecommercedb;

CREATE EXTENSION postgis;


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

CREATE EXTENSION postgis;

-- Creación de la base de datos y tablas

CREATE TABLE IF NOT EXISTS usuario (
                                       id_usuario SERIAL PRIMARY KEY,
                                       nombre VARCHAR(255) NOT NULL,
                                       email VARCHAR(255) NOT NULL UNIQUE,
                                       contrasena VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS pos_usuario (
                                           id_cliente VARCHAR(20) NOT NULL PRIMARY KEY,
                                           latitud DOUBLE PRECISION,
                                           longitud DOUBLE PRECISION,
                                           geom GEOMETRY(Point, 4326)
);

CREATE TABLE if NOT EXISTS almacen (
                                       id_almacen SERIAL PRIMARY KEY,
                                       nombre VARCHAR(255) NOT NULL,
                                       posicion text,
                                       longitud text,
                                       latitud text
);

CREATE TABLE IF NOT EXISTS pos_almacen (
                                           id_almacen INTEGER NOT NULL PRIMARY KEY,
                                           latitud DOUBLE PRECISION,
                                           longitud DOUBLE PRECISION,
                                           geom GEOMETRY(Point, 4326)
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
                         telefono VARCHAR(20),
                         posicion text NOT NULL,
                         latitud text NOT NULL,
                         longitud text NOT NULL
);

-- Tabla: orden
CREATE TABLE IF NOT EXISTS orden (
                                     id_orden SERIAL PRIMARY KEY,
                                     fecha_orden TIMESTAMP NOT NULL,
                                     estado VARCHAR(50) NOT NULL,
                                     id_cliente INTEGER NOT NULL,
                                     id_almacen INTEGER NOT NULL,
                                     total NUMERIC(10, 2) NOT NULL,
                                     FOREIGN KEY (id_cliente) REFERENCES cliente(id_cliente),
                                     FOREIGN KEY (id_almacen) REFERENCES almacen(id_almacen)
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


---------------------------------- #################################
-- COSAS DEL LABORATORIO 2


-- Creamos un trigger que almacene la posicion del usuario segun su geometría
-- longitud y latitud.
CREATE OR REPLACE FUNCTION insertar_pos_usuario() RETURNS TRIGGER
    LANGUAGE plpgsql
AS
$$
BEGIN
    INSERT INTO pos_usuario (id_cliente, latitud, longitud, geom)
    VALUES (
               NEW.id_cliente,
               CAST(NEW.latitud AS DOUBLE PRECISION),
               CAST(NEW.longitud AS DOUBLE PRECISION),
               ST_SetSRID(ST_MakePoint(CAST(NEW.longitud AS DOUBLE PRECISION), CAST(NEW.latitud AS DOUBLE PRECISION)), 4326)
           )
    ON CONFLICT (id_cliente) DO UPDATE
        SET
            latitud = EXCLUDED.latitud,
            longitud = EXCLUDED.longitud,
            geom = EXCLUDED.geom;

    RETURN NEW;
END;
$$;

ALTER FUNCTION insertar_pos_usuario() OWNER TO postgres;


-- Cada vez que se haga un post o un update, se activa el metodo
CREATE TRIGGER trg_insertar_pos_usuario
    AFTER INSERT OR UPDATE
    ON cliente
    FOR EACH ROW
EXECUTE FUNCTION insertar_pos_usuario();

-- Creamos el trigger de almacen
CREATE OR REPLACE FUNCTION insertar_pos_almacen() RETURNS TRIGGER
    LANGUAGE plpgsql
AS $$
BEGIN
    INSERT INTO pos_almacen (id_almacen, latitud, longitud, geom)
    VALUES (
               NEW.id_almacen,
               CAST(NEW.latitud AS DOUBLE PRECISION),
               CAST(NEW.longitud AS DOUBLE PRECISION),
               ST_SetSRID(ST_MakePoint(CAST(NEW.longitud AS DOUBLE PRECISION), CAST(NEW.latitud AS DOUBLE PRECISION)), 4326)
           )
    ON CONFLICT (id_almacen) DO UPDATE
        SET
            latitud = EXCLUDED.latitud,
            longitud = EXCLUDED.longitud,
            geom = EXCLUDED.geom;

    RETURN NEW;
END;
$$;

-- Para que se active
CREATE TRIGGER trg_insertar_pos_almacen
    AFTER INSERT OR UPDATE ON almacen
    FOR EACH ROW
EXECUTE FUNCTION insertar_pos_almacen();


-- REQUERIMIENTO 14
CREATE OR REPLACE FUNCTION obtener_ordenes_cercanas(id_almacen_input INTEGER, radio_km DOUBLE PRECISION DEFAULT 10.0)
    RETURNS TABLE (
                      id_orden INTEGER,
                      fecha_orden TIMESTAMP,
                      estado VARCHAR,
                      id_cliente INTEGER,
                      id_almacen INTEGER,
                      total NUMERIC
                  ) AS
$$
DECLARE
    geom_almacen GEOGRAPHY;
BEGIN
    -- Obtener la geometría del almacén específico
    SELECT pa.geom::GEOGRAPHY INTO geom_almacen
    FROM almacen a
             JOIN pos_almacen pa ON a.id_almacen = pa.id_almacen
    WHERE a.id_almacen = id_almacen_input;

-- Verificar si se encontró el almacén
    IF geom_almacen IS NULL THEN
        RAISE EXCEPTION 'Almacén no encontrado con el ID: %', id_almacen_input;
    END IF;

    -- Devolver las órdenes cercanas al almacén
    RETURN QUERY
        SELECT
            o.id_orden,
            o.fecha_orden,
            o.estado,
            o.id_cliente,  -- Asegúrate de que estás usando el ID correcto aquí
            o.id_almacen,
            o.total
        FROM
            orden o
                JOIN cliente c ON o.id_cliente = c.id_cliente  -- Asegúrate de que estás usando el ID correcto aquí
                JOIN pos_usuario pu ON c.id_cliente::VARCHAR = pu.id_cliente  -- Asegúrate de que los tipos coincidan
        WHERE
            ST_DWithin(geom_almacen, pu.geom::GEOGRAPHY, radio_km * 1000); -- Asegurar que la unidad es metros

END;
$$
    LANGUAGE plpgsql;







-- REQUERIMIENTO 15
CREATE OR REPLACE FUNCTION obtener_almacen_mas_cercano(id_cliente_input INTEGER)
    RETURNS TABLE (
                      id_almacen INTEGER,
                      nombre VARCHAR,
                      posicion TEXT,
                      latitud DOUBLE PRECISION,
                      longitud DOUBLE PRECISION
                  ) AS
$$
DECLARE
    geom_cliente GEOGRAPHY;
BEGIN
    -- Obtener la geometría del cliente específico
    SELECT pu.geom::GEOGRAPHY INTO geom_cliente
    FROM cliente c
             JOIN pos_usuario pu ON c.id_cliente::VARCHAR = pu.id_cliente
    WHERE c.id_cliente = id_cliente_input;

-- Verificar si se encontró el cliente
    IF geom_cliente IS NULL THEN
        RAISE EXCEPTION 'Cliente no encontrado con el ID: %', id_cliente_input;
    END IF;

    -- Devolver el almacén más cercano al cliente
    RETURN QUERY
        SELECT
            a.id_almacen,
            a.nombre,
            a.posicion,
            pa.latitud,
            pa.longitud
        FROM
            almacen a
                JOIN
            pos_almacen pa ON a.id_almacen = pa.id_almacen
        ORDER BY
            ST_Distance(geom_cliente, pa.geom::GEOGRAPHY) -- Ordenar por distancia ascendente
        LIMIT 1;
END;
$$
    LANGUAGE plpgsql;




-- REQUERIMIENTO 21
CREATE OR REPLACE FUNCTION obtener_distancia_cliente_almacen(id_cliente_input INTEGER, id_almacen_input INTEGER)
    RETURNS DOUBLE PRECISION AS
$$
DECLARE
    geom_cliente GEOGRAPHY;
    geom_almacen GEOGRAPHY;
BEGIN
    -- Obtener la geometría del cliente
    SELECT pu.geom::GEOGRAPHY INTO geom_cliente
    FROM cliente c  -- Cambiar a la tabla cliente
             JOIN pos_usuario pu ON c.id_cliente::VARCHAR = pu.id_cliente  -- Asegúrate de usar el id_cliente aquí
    WHERE c.id_cliente = id_cliente_input;

-- Verificar si se encontró el cliente
    IF geom_cliente IS NULL THEN
        RAISE EXCEPTION 'Cliente no encontrado con el ID: %', id_cliente_input;
    END IF;

    -- Obtener la geometría del almacén
    SELECT pa.geom::GEOGRAPHY INTO geom_almacen
    FROM almacen a
             JOIN pos_almacen pa ON a.id_almacen = pa.id_almacen
    WHERE a.id_almacen = id_almacen_input;

-- Verificar si se encontró el almacén
    IF geom_almacen IS NULL THEN
        RAISE EXCEPTION 'Almacén no encontrado con el ID: %', id_almacen_input;
    END IF;

    -- Calcular la distancia en kilómetros
    RETURN ST_Distance(geom_cliente, geom_almacen) / 1000; -- Devuelve la distancia en km
END;
$$
    LANGUAGE plpgsql;