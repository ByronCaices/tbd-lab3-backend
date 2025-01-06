-- Limpiar tablas en orden inverso para evitar violaciones de claves foráneas
TRUNCATE TABLE detalle_orden CASCADE;
TRUNCATE TABLE orden CASCADE;
TRUNCATE TABLE cliente CASCADE;


-- =============================
-- Modificar estructura de la tabla cliente
-- =============================
DROP TABLE IF EXISTS cliente CASCADE;
CREATE TABLE cliente (
                         id_cliente SERIAL PRIMARY KEY,
                         nombre VARCHAR(100) NOT NULL,
                         direccion TEXT,
                         email VARCHAR(100),
                         telefono VARCHAR(15),
                         posicion VARCHAR(50),
                         latitud NUMERIC(9, 5),
                         longitud NUMERIC(9, 5)
);

TRUNCATE TABLE producto CASCADE;
TRUNCATE TABLE categoria CASCADE;

-- =============================
-- Poblar tabla categoria
-- =============================
INSERT INTO categoria (id_categoria, nombre) VALUES
                                                 (1, 'Electrónica'),
                                                 (2, 'Hogar'),
                                                 (3, 'Ropa'),
                                                 (4, 'Juguetes'),
                                                 (5, 'Deportes'),
                                                 (6, 'Muebles');

-- =============================
-- Poblar tabla producto
-- =============================
INSERT INTO producto (id_producto, nombre, descripcion, precio, stock, estado, id_categoria) VALUES
                                                                                                 (1, 'Smart TV 4K de 55 pulgadas', 'Televisor de gran calidad y sonido', 7000000.00, 20, 'Disponible', 1),
                                                                                                 (2, 'Playstation 5 Pro', 'Consola moderna de gran potencia', 8000000.00, 6, 'Disponible', 1),
                                                                                                 (3, 'Nintendo Switch', 'Consola portátil y de sobremesa', 500000.00, 2, 'Disponible', 1),
                                                                                                 (4, 'Xbox Series S', 'Consola de última generación', 600000.00, 4, 'Disponible', 1),
                                                                                                 (5, 'Smart TV de 50 pulgadas', 'Televisor de gran calidad y sonido', 7000000.00, 20, 'Disponible', 1),
                                                                                                 (6, 'Refrigerador de 400L', 'Refrigerador de bajo consumo', 450000.00, 10, 'Disponible', 2),
                                                                                                 (7, 'Licuadora de 2L', 'Licuadora de alta potencia y con buen motor', 60000.00, 15, 'Disponible', 2),
                                                                                                 (8, 'Hervidor eléctrico de 3L', 'Hervidor de acero inoxidable y de buena calidad', 25000.00, 30, 'Disponible', 2),
                                                                                                 (9, 'Microondas', 'Microondas de alta potencia y permite descongelar productos', 80000.00, 20, 'Disponible', 2),
                                                                                                 (10, 'Robot aspiradora', 'Producto inteligente que limpia el hogar', 120000.00, 8, 'Disponible', 2),
                                                                                                 (11, 'Polera', 'Polera básica talla M de color blanco', 12000.00, 50, 'Disponible', 3),
                                                                                                 (12, 'Jeans mezclilla', 'Jeans tiro bajo y muy ancho', 25000.00, 40, 'Disponible', 3),
                                                                                                 (13, 'Chaqueta deportiva', 'Chaqueta perfecta para el ejercicio y talla L', 45000.00, 15, 'Disponible', 3),
                                                                                                 (14, 'Zapatillas de running', 'Zapatillas diseñadas con tecnología especial para correr, talla 42', 60000.00, 20, 'Disponible', 3),
                                                                                                 (15, 'Vestido ligero', 'Vestido perfecto para épocas de altas temperaturas', 30000.00, 10, 'Disponible', 3),
                                                                                                 (16, 'Lego', 'Set de construcción de lego', 45000.00, 25, 'Disponible', 4),
                                                                                                 (17, 'Muñeca', 'Muñeca con sonidos y luces', 30000.00, 18, 'Disponible', 4),
                                                                                                 (18, 'Auto a control remoto', 'Coche eléctrico con control de alta duración', 40000.00, 10, 'Disponible', 4),
                                                                                                 (19, 'Puzzle', 'Rompecabezas de 1000 piezas y hecho con materiales de alta calidad', 15000.00, 50, 'Disponible', 4),
                                                                                                 (20, 'Drone', 'Drone con cámara básica e intuitivo para menores de edad', 60000.00, 12, 'Disponible', 4),
                                                                                                 (21, 'Pelota de fútbol', 'Pelota de fútbol de la Euro 2024', 25000.00, 30, 'Disponible', 5),
                                                                                                 (22, 'Bicicleta', 'Bicicleta con suspensión completa, ideal para las montañas', 350000.00, 5, 'Disponible', 5),
                                                                                                 (23, 'Pesas', 'Set de pesas con discos intercambiables hasta 20 kilos', 80000.00, 10, 'Disponible', 5),
                                                                                                 (24, 'Malla para voleibol', 'Malla de nylon resistente', 30000.00, 8, 'Disponible', 5),
                                                                                                 (25, 'Skateboard', 'Tabla de skate profesional con buen agarre', 70000.00, 12, 'Disponible', 5),
                                                                                                 (26, 'Sofá 3 plazas', 'Sofá cómodo y elegante', 250000.00, 3, 'Disponible', 6),
                                                                                                 (27, 'Mesa de comedor', 'Mesa de madera para 6 personas, no incluye las sillas', 180000.00, 5, 'Disponible', 6),
                                                                                                 (28, 'Cama King Size', 'Cama amplia y confortable', 350000.00, 2, 'Disponible', 6),
                                                                                                 (29, 'Silla ergonómica', 'Silla ideal para oficina', 70000.00, 15, 'Disponible', 6),
                                                                                                 (30, 'Estantería de madera', 'Estantería armable y espaciosa', 90000.00, 10, 'Disponible', 6);

-- =============================
-- Poblar tabla cliente
-- =============================
INSERT INTO cliente (id_cliente, nombre, direccion, email, telefono, posicion, latitud, longitud) VALUES
                                                                                                      (1, 'Byron Brito', 'Calle 123', 'byron.brito@gmail.com', '123456789', 'Posición A', '10.12345', '-75.12345'),
                                                                                                      (2, 'Thomas Oyanedel', 'Avenida los palotes 3', 'thomas.oyanedel@gmail.com', '987654321', 'Posición B', '10.12400', '-75.12400'),
                                                                                                      (3, 'Isidora Riffo', 'Pasaje feliz 1', 'isidora.riffo@gmail.com', '456789123', 'Posición C', '10.12500', '-75.12500'),
                                                                                                      (4, 'Benjamin Cassone', 'Calle eao 231', 'benjamin.cassone@gmail.com', '789123456', 'Posición D', '10.12600', '-75.12600'),
                                                                                                      (5, 'Andre Bustamante', 'Condominio buena vida 2', 'andre.bustamante@gmail.com', '321654987', 'Posición E', '10.12700', '-75.12700'),
                                                                                                      (6, 'Camila Valenzuela', 'Calle 456', 'camila.valenzuela@gmail.com', '654321789', 'Posición F', '10.12800', '-75.12800'),
                                                                                                      (7, 'Diego Torres', 'Avenida Siempre Viva 742', 'diego.torres@gmail.com', '147258369', 'Posición G', '10.12900', '-75.12900'),
                                                                                                      (8, 'Sofía Martínez', 'Boulevard de los Sueños 1000', 'sofia.martinez@gmail.com', '258369147', 'Posición H', '10.13000', '-75.13000'),
                                                                                                      (9, 'Juan Pérez', 'Ruta 66 Km 45', 'juan.perez@gmail.com', '369147258', 'Posición I', '10.13100', '-75.13100'),
                                                                                                      (10, 'María González', 'Callejón del Beso 5', 'maria.gonzalez@gmail.com', '741852963', 'Posición J', '10.13200', '-75.13200'),
                                                                                                      (11, 'Carlos Sánchez', 'Plaza de la Constitución 10', 'carlos.sanchez@gmail.com', '852963741', 'Posición K', '10.13300', '-75.13300'),
                                                                                                      (12, 'Laura Ramírez', 'Camino Real 200', 'laura.ramirez@gmail.com', '963741852', 'Posición L', '10.13400', '-75.13400'),
                                                                                                      (13, 'Luis Fernández', 'Paseo de la Reforma 300', 'luis.fernandez@gmail.com', '159753486', 'Posición M', '10.13500', '-75.13500'),
                                                                                                      (14, 'Ana López', 'Avenida Central 50', 'ana.lopez@gmail.com', '357159486', 'Posición N', '10.13600', '-75.13600'),
                                                                                                      (15, 'Pedro Díaz', 'Calle del Sol 77', 'pedro.diaz@gmail.com', '753159486', 'Posición O', '10.13700', '-75.13700'),
                                                                                                      (16, 'Lucía Herrera', 'Avenida de la Paz 88', 'lucia.herrera@gmail.com', '951357486', 'Posición P', '10.13800', '-75.13800'),
                                                                                                      (17, 'Jorge Silva', 'Calle Luna 12', 'jorge.silva@gmail.com', '357951486', 'Posición Q', '10.13900', '-75.13900'),
                                                                                                      (18, 'Valentina Cruz', 'Plaza Mayor 9', 'valentina.cruz@gmail.com', '654789123', 'Posición R', '10.14000', '-75.14000'),
                                                                                                      (19, 'Andrés Morales', 'Ruta del Vino 33', 'andres.morales@gmail.com', '789123654', 'Posición S', '10.14100', '-75.14100'),
                                                                                                      (20, 'Natalia Reyes', 'Avenida Libertad 44', 'natalia.reyes@gmail.com', '123789456', 'Posición T', '10.14200', '-75.14200'),
                                                                                                      (21, 'Ricardo Soto', 'Calle Mar 7', 'ricardo.soto@gmail.com', '456123789', 'Posición U', '10.14300', '-75.14300'),
                                                                                                      (22, 'Gabriela Torres', 'Boulevard de la Aurora 21', 'gabriela.torres@gmail.com', '789456123', 'Posición V', '10.14400', '-75.14400'),
                                                                                                      (23, 'Fernando Rojas', 'Calle del Río 14', 'fernando.rojas@gmail.com', '321789654', 'Posición W', '10.14500', '-75.14500'),
                                                                                                      (24, 'Paula Mendoza', 'Avenida de los Pinos 55', 'paula.mendoza@gmail.com', '654321987', 'Posición X', '10.14600', '-75.14600'),
                                                                                                      (25, 'Sebastián Vega', 'Plaza de las Flores 6', 'sebastian.vega@gmail.com', '987654123', 'Posición Y', '10.14700', '-75.14700'),
                                                                                                      (26, 'Daniela Castro', 'Calle Oro 19', 'daniela.castro@gmail.com', '159357486', 'Posición Z', '10.14800', '-75.14800'),
                                                                                                      (27, 'Mateo Navarro', 'Avenida del Mar 23', 'mateo.navarro@gmail.com', '753951486', 'Posición AA', '10.14900', '-75.14900'),
                                                                                                      (28, 'Elena Paredes', 'Calle del Sol 8', 'elena.paredes@gmail.com', '852741963', 'Posición AB', '10.15000', '-75.15000'),
                                                                                                      (29, 'Diego Morales', 'Ruta Nacional 12', 'diego.morales@gmail.com', '147852369', 'Posición AC', '10.15100', '-75.15100'),
                                                                                                      (30, 'Carla Reyes', 'Calle Estrella 4', 'carla.reyes@gmail.com', '369258147', 'Posición AD', '10.15200', '-75.15200');

-- =============================
-- Tabla de Almacen
-- =============================

INSERT INTO almacen (id_almacen, nombre, posicion, latitud, longitud) VALUES
                                                                          (1, 'Almacén Central', 'Posición AE', '10.15300', '-75.15300'),
                                                                          (2, 'Almacén Norte', 'Posición AF', '10.15400', '-75.15400'),
                                                                          (3, 'Almacén Sur', 'Posición AG', '10.15500', '-75.15500'),
                                                                          (4, 'Almacén Este', 'Posición AH', '10.15600', '-75.15600'),
                                                                          (5, 'Almacén Oeste', 'Posición AI', '10.15700', '-75.15700'),
                                                                          (6, 'Almacén Noroeste', 'Posición AJ', '10.15800', '-75.15800'),
                                                                          (7, 'Almacén Suroeste', 'Posición AK', '10.15900', '-75.15900'),
                                                                          (8, 'Almacén Noreste', 'Posición AL', '10.16000', '-75.16000'),
                                                                          (9, 'Almacén Sureste', 'Posición AM', '10.16100', '-75.16100'),
                                                                          (10, 'Almacén Principal', 'Posición AN', '10.16200', '-75.16200'),
                                                                          (11, 'Almacén Auxiliar', 'Posición AO', '10.16300', '-75.16300'),
                                                                          (12, 'Almacén Temporal', 'Posición AP', '10.16400', '-75.16400'),
                                                                          (13, 'Almacén Permanente', 'Posición AQ', '10.16500', '-75.16500'),
                                                                          (14, 'Almacén Industrial', 'Posición AR', '10.16600', '-75.16600'),
                                                                          (15, 'Almacén Comercial', 'Posición AS', '10.16700', '-75.16700'),
                                                                          (16, 'Almacén de Emergencia', 'Posición AT', '10.16800', '-75.16800'),
                                                                          (17, 'Almacén de Respaldo', 'Posición AU', '10.16900', '-75.16900'),
                                                                          (18, 'Almacén de Insumos', 'Posición AV', '10.17000', '-75.17000'),
                                                                          (19, 'Almacén de Repuestos', 'Posición AW', '10.17100', '-75.17100'),
                                                                          (20, 'Almacén de Materias Primas', 'Posición AX', '10.17200', '-75.17200'),
                                                                          (21, 'Almacén de Productos Terminados', 'Posición AY', '10.17300', '-75.17300'),
                                                                          (22, 'Almacén de Distribución', 'Posición AZ', '10.17400', '-75.17400'),
                                                                          (23, 'Almacén de Logística', 'Posición BA', '10.17500', '-75.17500'),
                                                                          (24, 'Almacén de Expedición', 'Posición BB', '10.17600', '-75.17600'),
                                                                          (25, 'Almacén de Recepción', 'Posición BC', '10.17700', '-75.17700'),
                                                                          (26, 'Almacén de Tecnología', 'Posición BD', '10.17800', '-75.17800'),
                                                                          (27, 'Almacén de Alimentos', 'Posición BE', '10.17900', '-75.17900'),
                                                                          (28, 'Almacén de Bebidas', 'Posición BF', '10.18000', '-75.18000'),
                                                                          (29, 'Almacén de Muebles', 'Posición BG', '10.18100', '-75.18100'),
                                                                          (30, 'Almacén de Equipos', 'Posición BH', '10.18200', '-75.18200');


-- =============================
-- Poblar tabla orden
-- =============================
INSERT INTO orden (id_orden, fecha_orden, estado, id_cliente, id_almacen, total) VALUES
                                                                                     (1, '2024-10-15 10:00:00', 'Pagada', 1, 1, 500000.00),
                                                                                     (2, '2024-10-15 15:30:00', 'Enviada', 2, 2, 400000.00),
                                                                                     (3, '2024-10-15 18:45:00', 'Pendiente', 1, 3, 50000.00),
                                                                                     (4, '2024-10-15 12:20:00', 'Enviada', 5, 4 , 100000.00),
                                                                                     (5, '2024-10-15 17:30:00', 'Enviada', 5, 5, 300000.00),
                                                                                     (6, '2024-10-15 18:00:00', 'Pagada', 1, 6, 80000.00),

                                                                                     -- Órdenes adicionales
                                                                                     (7, '2024-11-21 09:15:00', 'Pagada', 6, 7, 150000.00),
                                                                                     (8, '2024-11-22 11:45:00', 'Enviada', 7, 8, 200000.00),
                                                                                     (9, '2024-11-23 14:30:00', 'Pendiente', 8, 9, 75000.00),
                                                                                     (10, '2024-11-24 16:00:00', 'Enviada', 9, 10, 125000.00),
                                                                                     (11, '2024-11-25 10:30:00', 'Pagada', 10, 11, 50000.00),
                                                                                     (12, '2024-11-26 13:20:00', 'Enviada', 11, 12, 220000.00),
                                                                                     (13, '2024-11-27 15:50:00', 'Pendiente', 12, 13, 98000.00),
                                                                                     (14, '2024-11-28 17:10:00', 'Enviada', 13, 14, 300000.00),
                                                                                     (15, '2024-11-29 19:25:00', 'Pagada', 14, 15, 45000.00),
                                                                                     (16, '2024-11-30 08:40:00', 'Enviada', 15, 16, 60000.00),
                                                                                     (17, '2024-12-01 10:05:00', 'Pagada', 16, 17, 120000.00),
                                                                                     (18, '2024-12-02 12:30:00', 'Enviada', 17, 18, 80000.00),
                                                                                     (19, '2024-12-03 14:55:00', 'Pendiente', 18, 19, 50000.00),
                                                                                     (20, '2024-12-04 16:20:00', 'Enviada', 19, 20, 90000.00),
                                                                                     (21, '2024-12-05 09:35:00', 'Pagada', 20, 21, 130000.00),
                                                                                     (22, '2024-12-06 11:50:00', 'Enviada', 21, 22, 70000.00),
                                                                                     (23, '2024-12-07 14:10:00', 'Pendiente', 22, 23, 60000.00),
                                                                                     (24, '2024-12-08 16:25:00', 'Enviada', 23, 24, 110000.00),
                                                                                     (25, '2024-12-09 18:40:00', 'Pagada', 24, 25, 85000.00),
                                                                                     (26, '2024-12-10 20:55:00', 'Enviada', 25, 26, 95000.00),
                                                                                     (27, '2024-12-11 08:15:00', 'Pendiente', 26, 27, 40000.00),
                                                                                     (28, '2024-12-12 10:30:00', 'Enviada', 27, 28, 160000.00),
                                                                                     (29, '2024-12-13 12:45:00', 'Pagada', 28, 29, 70000.00),
                                                                                     (30, '2024-12-14 15:00:00', 'Enviada', 29, 30, 50000.00),
                                                                                     (31, '2024-12-15 17:15:00', 'Pendiente', 30, 1, 30000.00),
                                                                                     (32, '2024-12-16 09:30:00', 'Enviada', 1, 2, 200000.00),
                                                                                     (33, '2024-12-17 11:45:00', 'Pagada', 2, 3, 250000.00),
                                                                                     (34, '2024-12-18 14:00:00', 'Enviada', 3, 4, 180000.00),
                                                                                     (35, '2024-12-19 16:15:00', 'Pendiente', 4, 5, 95000.00),
                                                                                     (36, '2024-12-20 18:30:00', 'Enviada', 5, 6, 130000.00),
                                                                                     (37, '2024-12-21 20:45:00', 'Pagada', 6, 7, 40000.00),
                                                                                     (38, '2024-12-22 08:00:00', 'Enviada', 7, 8, 210000.00),
                                                                                     (39, '2024-12-23 10:15:00', 'Pendiente', 8, 9, 160000.00),
                                                                                     (40, '2024-12-24 12:30:00', 'Enviada', 9, 10, 70000.00),
                                                                                     (41, '2024-12-25 14:45:00', 'Pagada', 10, 11, 90000.00),
                                                                                     (42, '2024-12-26 17:00:00', 'Enviada', 11, 12, 300000.00),
                                                                                     (43, '2024-12-27 19:15:00', 'Pendiente', 12, 13, 120000.00),
                                                                                     (44, '2024-12-28 21:30:00', 'Enviada', 13, 14, 85000.00),
                                                                                     (45, '2024-12-29 08:45:00', 'Pagada', 14, 15, 50000.00),
                                                                                     (46, '2024-12-30 11:00:00', 'Enviada', 15, 16, 75000.00),
                                                                                     (47, '2024-12-31 13:15:00', 'Pendiente', 16, 17, 140000.00),
                                                                                     (48, '2025-01-01 15:30:00', 'Enviada', 17, 18, 60000.00),
                                                                                     (49, '2025-01-02 17:45:00', 'Pagada', 18, 19, 95000.00),
                                                                                     (50, '2025-01-03 20:00:00', 'Enviada', 19, 20, 110000.00);

-- =============================
-- Poblar tabla detalle_orden
-- =============================
INSERT INTO detalle_orden (id_detalle, id_orden, id_producto, cantidad, precio_unitario) VALUES
                                                                                             (1, 1, 1, 2, 7000000.00),
                                                                                             (2, 2, 2, 1, 8000000.00),
                                                                                             (3, 2, 3, 1, 500000.00),
                                                                                             (4, 3, 4, 1, 600000.00),
                                                                                             (5, 4, 21, 4, 25000.00),
                                                                                             (6, 5, 22, 1, 350000.00),
                                                                                             (7, 6, 5, 2, 7000000.00),
                                                                                             -- Detalles adicionales
                                                                                             (8, 7, 6, 1, 450000.00),
                                                                                             (9, 7, 7, 2, 60000.00),
                                                                                             (10, 8, 8, 3, 25000.00),
                                                                                             (11, 8, 9, 1, 80000.00),
                                                                                             (12, 9, 10, 1, 120000.00),
                                                                                             (13, 10, 11, 5, 12000.00),
                                                                                             (14, 11, 12, 2, 25000.00),
                                                                                             (15, 12, 13, 1, 45000.00),
                                                                                             (16, 13, 14, 3, 60000.00),
                                                                                             (17, 14, 15, 2, 30000.00),
                                                                                             (18, 15, 16, 4, 45000.00),
                                                                                             (19, 16, 17, 2, 30000.00),
                                                                                             (20, 17, 18, 1, 40000.00),
                                                                                             (21, 18, 19, 2, 15000.00),
                                                                                             (22, 19, 20, 1, 60000.00),
                                                                                             (23, 20, 21, 3, 25000.00),
                                                                                             (24, 21, 22, 1, 350000.00),
                                                                                             (25, 22, 23, 2, 80000.00),
                                                                                             (26, 23, 24, 1, 30000.00),
                                                                                             (27, 24, 25, 2, 70000.00),
                                                                                             (28, 25, 26, 1, 250000.00),
                                                                                             (29, 26, 27, 1, 180000.00),
                                                                                             (30, 27, 28, 1, 350000.00),
                                                                                             (31, 28, 29, 2, 70000.00),
                                                                                             (32, 29, 30, 1, 90000.00),
                                                                                             (33, 30, 1, 1, 7000000.00),
                                                                                             (34, 31, 2, 1, 8000000.00),
                                                                                             (35, 32, 3, 2, 500000.00),
                                                                                             (36, 33, 4, 1, 600000.00),
                                                                                             (37, 34, 5, 1, 7000000.00),
                                                                                             (38, 35, 6, 2, 450000.00),
                                                                                             (39, 36, 7, 3, 60000.00),
                                                                                             (40, 37, 8, 1, 25000.00),
                                                                                             (41, 38, 9, 2, 80000.00),
                                                                                             (42, 39, 10, 1, 120000.00),
                                                                                             (43, 40, 11, 4, 12000.00),
                                                                                             (44, 41, 12, 3, 25000.00),
                                                                                             (45, 42, 13, 2, 45000.00),
                                                                                             (46, 43, 14, 1, 60000.00),
                                                                                             (47, 44, 15, 2, 30000.00),
                                                                                             (48, 45, 16, 3, 45000.00),
                                                                                             (49, 46, 17, 2, 30000.00),
                                                                                             (50, 47, 18, 1, 40000.00),
                                                                                             (51, 48, 19, 2, 15000.00),
                                                                                             (52, 49, 20, 1, 60000.00),
                                                                                             (53, 50, 21, 3, 25000.00),
                                                                                             (54, 1, 2, 1, 8000000.00),
                                                                                             (55, 1, 3, 1, 500000.00);

-- =============================
-- Actualizar secuencias de IDs
-- =============================

-- Actualizar secuencia de categoria
SELECT setval(
               pg_get_serial_sequence('categoria', 'id_categoria'),
               (SELECT MAX(id_categoria) FROM categoria)
       );

SELECT setval(
               pg_get_serial_sequence('almacen', 'id_almacen'),
               (SELECT MAX(id_almacen) FROM almacen)
       );

-- Actualizar secuencia de producto
SELECT setval(
               pg_get_serial_sequence('producto', 'id_producto'),
               (SELECT MAX(id_producto) FROM producto)
       );

-- Actualizar secuencia de cliente
SELECT setval(
               pg_get_serial_sequence('cliente', 'id_cliente'),
               (SELECT MAX(id_cliente) FROM cliente)
       );

-- Actualizar secuencia de orden
SELECT setval(
               pg_get_serial_sequence('orden', 'id_orden'),
               (SELECT MAX(id_orden) FROM orden)
       );

-- Actualizar secuencia de detalle_orden
SELECT setval(
               pg_get_serial_sequence('detalle_orden', 'id_detalle'),
               (SELECT MAX(id_detalle) FROM detalle_orden)
       );