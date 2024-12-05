-- Crear tabla categoria
CREATE TABLE categoria (
                           id_categoria SERIAL PRIMARY KEY,
                           nombre VARCHAR(100) NOT NULL
);

-- Crear tabla producto
CREATE TABLE producto (
                          id_producto SERIAL PRIMARY KEY,
                          nombre VARCHAR(255) NOT NULL,
                          descripcion TEXT,
                          precio DECIMAL(10, 2) NOT NULL,
                          stock INT NOT NULL,
                          estado VARCHAR(50) CHECK (estado IN ('disponible', 'agotado')),
                          id_categoria INTEGER REFERENCES categoria(id_categoria)
);

-- Crear tabla cliente
CREATE TABLE cliente (
                         id_cliente SERIAL PRIMARY KEY,
                         nombre VARCHAR(255) NOT NULL,
                         direccion VARCHAR(255),
                         email VARCHAR(100),
                         telefono VARCHAR(20)
);

-- Crear tabla orden
CREATE TABLE orden (
                       id_orden SERIAL PRIMARY KEY,
                       fecha_orden TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                       estado VARCHAR(50) CHECK (estado IN ('pendiente', 'pagada', 'enviada', 'devuelto')),
                       id_cliente INTEGER REFERENCES cliente(id_cliente),
                       total DECIMAL(10, 2) NOT NULL
);

-- Crear tabla detalle_orden
CREATE TABLE detalle_orden (
                               id_detalle SERIAL PRIMARY KEY,
                               id_orden INTEGER REFERENCES orden(id_orden),
                               id_producto INTEGER REFERENCES producto(id_producto),
                               cantidad INT NOT NULL,
                               precio_unitario DECIMAL(10, 2) NOT NULL
);