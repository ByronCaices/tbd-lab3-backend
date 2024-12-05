// 1. Seleccionar o crear la base de datos
use ecommercedb;

// 2. Crear la colección 'usuario' y establecer un índice único en 'email'
db.createCollection('usuario');
db.usuario.createIndex({ email: 1 }, { unique: true });

// 3. Crear la colección 'categoria'
db.createCollection('categoria');

// 4. Crear la colección 'producto' y establecer un índice en 'id_categoria' si es necesario
db.createCollection('producto');
db.producto.createIndex({ id_categoria: 1 });

// 5. Crear la colección 'cliente'
db.createCollection('cliente');

// 6. Crear la colección 'orden' y establecer un índice en 'id_cliente' si es necesario
db.createCollection('orden');
db.orden.createIndex({ id_cliente: 1 });

// 7. Crear la colección 'auditoria' y establecer un índice en 'fecha' si es necesario
db.createCollection('auditoria');
db.auditoria.createIndex({ fecha: 1 });

// **Inserción de datos iniciales (opcional):**

// Insertar categorías
db.categoria.insertMany([
  { nombre: "Electrónica" },
  { nombre: "Ropa" },
  { nombre: "Hogar y Cocina" }
]);

// Obtener los IDs de las categorías para referencias
var categorias = db.categoria.find().toArray();

// Insertar productos referenciando 'id_categoria'
db.producto.insertMany([
  {
    nombre: "Smartphone",
    descripcion: "Teléfono inteligente de última generación",
    precio: 699.99,
    stock: 50,
    estado: "Disponible",
    id_categoria: categorias.find(c => c.nombre === "Electrónica")._id
  },
  {
    nombre: "Camiseta",
    descripcion: "Camiseta 100% algodón",
    precio: 19.99,
    stock: 150,
    estado: "Disponible",
    id_categoria: categorias.find(c => c.nombre === "Ropa")._id
  }
]);

// Insertar un cliente
db.cliente.insertOne({
  nombre: "María López",
  direccion: "Av. Siempre Viva 742",
  email: "maria.lopez@example.com",
  telefono: "555-6789"
});

// Obtener el ID del cliente
var cliente = db.cliente.findOne({ email: "maria.lopez@example.com" });

// Insertar una orden con detalles embebidos
var producto = db.producto.findOne({ nombre: "Smartphone" });

db.orden.insertOne({
  fecha_orden: new Date(),
  estado: "Pendiente",
  id_cliente: cliente._id,
  total: producto.precio * 1, // cantidad * precio_unitario
  detalles: [
    {
      id_producto: producto._id,
      cantidad: 1,
      precio_unitario: producto.precio
    }
  ]
});

// Insertar un registro de auditoría
db.auditoria.insertOne({
  accion: "INSERT",
  nombre_coleccion: "orden",
  id_registro: db.orden.findOne({ id_cliente: cliente._id })._id,
  usuario: "admin",
  descripcion: "Creación de nueva orden",
  fecha: new Date()
});
