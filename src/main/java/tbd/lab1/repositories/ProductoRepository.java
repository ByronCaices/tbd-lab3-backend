package tbd.lab1.repositories;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.sql2o.Sql2o;
import tbd.lab1.entities.ProductoEntity;
import org.sql2o.Connection;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Repository
public class ProductoRepository implements ProductoRepositoryInt {

    @Autowired
    private Sql2o sql2o;

    public ProductoEntity saveProducto(ProductoEntity producto) {
        // Validar que los campos necesarios no estén nulos
        if (producto.getNombre() == null || producto.getNombre().isEmpty()) {
            throw new IllegalArgumentException("El nombre del producto es obligatorio.");
        }
        if (producto.getDescripcion() == null || producto.getDescripcion().isEmpty()) {
            throw new IllegalArgumentException("La descripción del producto es obligatoria.");
        }
        if (producto.getPrecio() == null || producto.getPrecio().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("El precio del producto debe ser mayor a cero.");
        }
        if (producto.getStock() == null || producto.getStock() <= 0) {
            throw new IllegalArgumentException("El stock del producto debe ser mayor a cero.");
        }
        if (producto.getEstado() == null ||
                (!producto.getEstado().equals("Disponible") && !producto.getEstado().equals("Agotado"))) {
            throw new IllegalArgumentException("El estado debe ser 'Disponible' o 'Agotado'.");
        }
        if (producto.getId_categoria() == null) {
            throw new IllegalArgumentException("La categoría es obligatoria.");
        }

        String sql = "INSERT INTO producto (nombre, descripcion, precio, stock, estado, id_categoria) " +
                "VALUES (:nombre, :descripcion, :precio, :stock, :estado, :id_categoria) " +
                "RETURNING id_producto";

        try (Connection con = sql2o.open()) {
            // Ejecutar la consulta e intentar obtener el producto insertado
            Integer newProducto = con.createQuery(sql, true)
                    .addParameter("nombre", producto.getNombre())
                    .addParameter("descripcion", producto.getDescripcion())
                    .addParameter("precio", producto.getPrecio())
                    .addParameter("stock", producto.getStock())
                    .addParameter("estado", producto.getEstado())
                    .addParameter("id_categoria", producto.getId_categoria())
                    .executeUpdate()
                    .getKey(Integer.class);

            producto.setId_producto(newProducto);
            return producto;

        } catch (Exception e) {
            e.printStackTrace();
            return null; // En caso de error, devolver null
        }
    }

    public ProductoEntity getProductoById(Integer id) {
        String sql = "SELECT id_producto, nombre, descripcion, precio, stock, estado, id_categoria " +
                "FROM producto WHERE id_producto = :id";

        try (Connection con = sql2o.open()) {
            ProductoEntity producto = con.createQuery(sql)
                    .addParameter("id", id)
                    .executeAndFetchFirst(ProductoEntity.class);

            return producto;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<ProductoEntity> getProductos() {
        String sql = "SELECT * FROM producto ORDER BY id_producto";

        try (Connection con = sql2o.open()) {
            return con.createQuery(sql)
                    .executeAndFetch(ProductoEntity.class);
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public boolean updateProducto(ProductoEntity producto) {
        String sql = "UPDATE producto SET nombre = :nombre, descripcion = :descripcion, precio = :precio, " +
                "stock = :stock, estado = :estado, id_categoria = :id_categoria " +
                "WHERE id_producto = :idProducto";

        try (Connection con = sql2o.open()) {
            int affectedRows = con.createQuery(sql)
                    .addParameter("nombre", producto.getNombre())
                    .addParameter("descripcion", producto.getDescripcion())
                    .addParameter("precio", producto.getPrecio())
                    .addParameter("stock", producto.getStock())
                    .addParameter("estado", producto.getEstado())
                    .addParameter("id_categoria", producto.getId_categoria())
                    .addParameter("idProducto", producto.getId_producto())
                    .executeUpdate()
                    .getResult(); // Obtener el número de filas afectadas

            return affectedRows > 0; // Devuelve true si se actualizó al menos una fila
        } catch (Exception e) {
            e.printStackTrace();
            return false; // Devuelve false si ocurre un error
        }
    }

    public boolean deleteProducto(Integer id) {
        String sql = "DELETE FROM producto WHERE id_producto = :id";
        try (Connection con = sql2o.open()) {
            int affectedRows = con.createQuery(sql)
                    .addParameter("id", id)
                    .executeUpdate()
                    .getResult(); // Obtener el número de filas afectadas

            return affectedRows > 0; // Devuelve true si se eliminó al menos una fila
        } catch (Exception e) {
            e.printStackTrace();
            return false; // Devuelve false si ocurre un error
        }
    }

    public void desactivarProductosSinStock() {
        String sql = "CALL desactivar_productos_sin_stock()";
        try (Connection connection = sql2o.open()) {
            connection.createQuery(sql)
                    .executeUpdate();
        }
    }

}
