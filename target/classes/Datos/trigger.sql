CREATE TABLE auditoria (
       id_auditoria SERIAL PRIMARY KEY,
       accion VARCHAR(10),
       nombre_tabla VARCHAR(50),
       id_registro BIGINT,
       usuario VARCHAR(50),
       fecha TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
       descripcion TEXT
);

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

CREATE TRIGGER trigger_auditoria
    AFTER INSERT OR UPDATE OR DELETE ON cliente
    FOR EACH ROW EXECUTE FUNCTION auditar_accion();


CREATE TABLE devoluciones (
                              id_devolucion SERIAL PRIMARY KEY,
                              id_orden INTEGER NOT NULL,
                              id_producto INTEGER NOT NULL,
                              cantidad INTEGER NOT NULL,
                              fecha_devolucion TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE OR REPLACE FUNCTION gestionar_devolucion()
RETURNS TRIGGER AS $$
DECLARE
nueva_cantidad INTEGER;
BEGIN
    -- Actualizar el stock del producto
UPDATE producto
SET stock = stock + NEW.cantidad
WHERE id_producto = NEW.id_producto;

-- Verificar si la orden debe actualizar su estado
IF (SELECT COUNT(*) FROM detalle_orden WHERE id_orden = NEW.id_orden AND cantidad > 0) = 0 THEN
UPDATE orden
SET estado = 'devuelto'
WHERE id_orden = NEW.id_orden;
END IF;

RETURN NEW;
END;
$$ LANGUAGE plpgsql;


CREATE TRIGGER trigger_gestionar_devolucion
    AFTER INSERT ON devoluciones
    FOR EACH ROW
    EXECUTE FUNCTION gestionar_devolucion();


INSERT INTO devoluciones (id_orden, id_producto, cantidad)
VALUES (1, 2, 3);


---------------------------------- #################################----------------
-- COSAS DEL LABORATORIO 2


-- Creamos un trigger que almacene la posición del cliente según su geometría
-- longitud y latitud.
CREATE OR REPLACE FUNCTION insertar_pos_usuario() RETURNS TRIGGER
LANGUAGE plpgsql
AS
$$
BEGIN

-- Vamos añadir en pos_usuario su id, latitud, logitud y posición geometrica.
INSERT INTO pos_usuario (id_cliente, latitud, longitud, geom)
VALUES (
           NEW.id_cliente,
           CAST(NEW.latitud AS DOUBLE PRECISION),
           CAST(NEW.longitud AS DOUBLE PRECISION),
           ST_SetSRID(ST_MakePoint(CAST(NEW.longitud AS DOUBLE PRECISION), CAST(NEW.latitud AS DOUBLE PRECISION)), 4326)
       )

    -- Cada vez que se haga un UPDATE
    ON CONFLICT (id_cliente) DO UPDATE
                                     SET
                                     latitud = EXCLUDED.latitud,
                                     longitud = EXCLUDED.longitud,
                                     geom = EXCLUDED.geom;
RETURN NEW;
END;
$$;

ALTER FUNCTION insertar_pos_usuario() OWNER TO postgres;


-- Cada vez que se haga un post o un update, se activa el metodo.
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

-- Vamos a insertar dentro de pos_almacen su id, latitud, longitud y geometría.
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

-- Para que se ejecute el trigger
CREATE TRIGGER trg_insertar_pos_almacen

    -- Cada vez que hace un post o un update, este trigger se activa.
    AFTER INSERT OR UPDATE ON almacen
    FOR EACH ROW
    EXECUTE FUNCTION insertar_pos_almacen();


-- REQUERIMIENTO 14
CREATE OR REPLACE FUNCTION obtener_ordenes_cercanas(
    id_almacen_input INTEGER,
    radio_km DOUBLE PRECISION DEFAULT 10.0
)
RETURNS TABLE (
    id_orden INTEGER,
    fecha_orden TIMESTAMP,
    estado VARCHAR,
    id_cliente INTEGER,
    id_almacen INTEGER,
    total NUMERIC
) AS $$
DECLARE
geom_almacen GEOGRAPHY;
    longitud_almacen DOUBLE PRECISION;
    latitud_almacen DOUBLE PRECISION;
BEGIN
    -- Obtenemos las coordenadas del almacén
SELECT
    CAST(pos_almacen.longitud AS DOUBLE PRECISION),
    CAST(pos_almacen.latitud AS DOUBLE PRECISION)
INTO
    longitud_almacen,
    latitud_almacen
FROM pos_almacen
WHERE pos_almacen.id_almacen = id_almacen_input;

-- Se crea la geometría del almacén
geom_almacen := ST_MakePoint(longitud_almacen, latitud_almacen)::GEOGRAPHY;

    -- Verificamos la existencia del almacén
    IF geom_almacen IS NULL THEN
        RAISE EXCEPTION 'Almacén no encontrado con el ID: %', id_almacen_input;
END IF;

    -- Vamos a retornar las ordenes del almacén específico
RETURN QUERY
SELECT
    orden.id_orden,
    orden.fecha_orden,
    orden.estado,
    orden.id_cliente,
    orden.id_almacen,
    orden.total
FROM
    orden
        JOIN
    cliente ON orden.id_cliente = cliente.id_cliente
WHERE
  -- Filtramos por un almacén en específico
    orden.id_almacen = id_almacen_input
  AND
  -- Se filtra por la por distancia
    ST_DWithin(
            ST_MakePoint(
                    CAST(cliente.longitud AS DOUBLE PRECISION),
                    CAST(cliente.latitud AS DOUBLE PRECISION)
            )::GEOGRAPHY,
            geom_almacen,
            radio_km * 1000
    );
END;
$$ LANGUAGE plpgsql;


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
    -- Se obtiene la geometría del cliente que se ingreso
SELECT pu.geom::GEOGRAPHY INTO geom_cliente
FROM cliente c
         JOIN pos_usuario pu ON c.id_cliente::VARCHAR = pu.id_cliente
WHERE c.id_cliente = id_cliente_input;

-- Se verifica que el cliente exista
IF geom_cliente IS NULL THEN
        RAISE EXCEPTION 'Cliente no encontrado con el ID: %', id_cliente_input;
END IF;

    -- Se devuelve el almacén más cercano al cliente
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
    -- Se ordena por distancia ascendente
    ST_Distance(geom_cliente, pa.geom::GEOGRAPHY)
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
    -- Se obtiene la geometría del cliente del mapa
SELECT pu.geom::GEOGRAPHY INTO geom_cliente
-- Utilizamos la tabla del cliente
FROM cliente c
         JOIN pos_usuario pu ON c.id_cliente::VARCHAR = pu.id_cliente
WHERE c.id_cliente = id_cliente_input;

-- Se verifica si se encontró el cliente
IF geom_cliente IS NULL THEN
        RAISE EXCEPTION 'Cliente no encontrado con el ID: %', id_cliente_input;
END IF;

    -- Se obtiene la geometría del almacén en el mapa. (Lo mismo que con cliente)
SELECT pa.geom::GEOGRAPHY INTO geom_almacen
FROM almacen a
         JOIN pos_almacen pa ON a.id_almacen = pa.id_almacen
WHERE a.id_almacen = id_almacen_input;

-- Se verifica si se encontro el almacén correctamente
IF geom_almacen IS NULL THEN
        RAISE EXCEPTION 'Almacén no encontrado con el ID: %', id_almacen_input;
END IF;

-- Se calcula la distancia que hay entre los puntos
RETURN ST_Distance(geom_cliente, geom_almacen) / 1000; -- Devuelve la distancia en km
END;
$$
LANGUAGE plpgsql;
