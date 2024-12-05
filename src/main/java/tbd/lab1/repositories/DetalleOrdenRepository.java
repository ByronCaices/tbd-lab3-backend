package tbd.lab1.repositories;

import org.sql2o.Connection;
import org.sql2o.Sql2o;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import tbd.lab1.entities.DetalleOrdenEntity;

import java.util.ArrayList;
import java.util.List;

@Repository
public class DetalleOrdenRepository implements DetalleOrdenRepositoryInt{

    @Autowired
    private Sql2o sql2o;

    public DetalleOrdenEntity saveDetalleOrden(DetalleOrdenEntity detalleOrden) {
        String sql = "INSERT INTO detalle_orden (id_orden, id_producto, cantidad, precio_unitario) VALUES (:idOrden, :idProducto, :cantidad, :precioUnitario) RETURNING id_detalle";

        try (Connection con = sql2o.open()) {
            // Ejecutar la consulta de inserción y obtener el id_detalle generado
            Integer idDetalle = con.createQuery(sql)
                    .addParameter("idOrden", detalleOrden.getId_orden())
                    .addParameter("idProducto", detalleOrden.getId_producto())
                    .addParameter("cantidad", detalleOrden.getCantidad())
                    .addParameter("precioUnitario", detalleOrden.getPrecio_unitario())
                    .executeUpdate()
                    .getKey(Integer.class);

            if (idDetalle == null) {
                throw new RuntimeException("Failed to insert DetalleOrden. No ID returned.");
            }

            detalleOrden.setId_detalle(idDetalle);
            return detalleOrden;

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Error occurred while saving DetalleOrden: " + e.getMessage());
            return null; // En caso de error, devolver null
        }
    }

    public DetalleOrdenEntity getDetalleOrdenById(Integer id) {
        String sql = "SELECT id_detalle, id_orden, id_producto, cantidad, precio_unitario FROM detalle_orden WHERE id_detalle = :id";

        try (Connection con = sql2o.open()) {
            DetalleOrdenEntity detalleOrden = con.createQuery(sql)
                    .addParameter("id", id)
                    .executeAndFetchFirst(DetalleOrdenEntity.class);

            return detalleOrden;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<DetalleOrdenEntity> getDetalleOrdenes() {
        String sql = "SELECT id_detalle, id_orden, id_producto, cantidad, precio_unitario FROM detalle_orden";

        try (Connection con = sql2o.open()) {
            return con.createQuery(sql)
                    .executeAndFetch(DetalleOrdenEntity.class);
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public boolean updateDetalleOrden(DetalleOrdenEntity detalleOrden) {
        String sql = "UPDATE detalle_orden SET id_orden = :idOrden, id_producto = :idProducto, cantidad = :cantidad, precio_unitario = :precioUnitario WHERE id_detalle = :idDetalle";

        try (Connection con = sql2o.open()) {
            int affectedRows = con.createQuery(sql)
                    .addParameter("idOrden", detalleOrden.getId_orden())
                    .addParameter("idProducto", detalleOrden.getId_producto())
                    .addParameter("cantidad", detalleOrden.getCantidad())
                    .addParameter("precioUnitario", detalleOrden.getPrecio_unitario())
                    .addParameter("idDetalle", detalleOrden.getId_detalle())
                    .executeUpdate()
                    .getResult(); // Obtener el número de filas afectadas

            return affectedRows > 0; // Devuelve true si se actualizó al menos una fila
        } catch (Exception e) {
            e.printStackTrace();
            return false; // Devuelve false si ocurre un error
        }
    }

    public boolean deleteDetalleOrden(Integer id) {
        String sql = "DELETE FROM detalle_orden WHERE id_detalle = :id";
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

    public void gestionarDevolucion(int id_orden, int id_producto, int cantidad) {
        String sql = "CALL gestionar_devolucion_proc(:id_orden, :id_producto, :cantidad)";
        try (Connection connection = sql2o.open()) {
            connection.createQuery(sql)
                    .addParameter("id_orden", id_orden)
                    .addParameter("id_producto", id_producto)
                    .addParameter("cantidad", cantidad)
                    .executeUpdate();
        }
    }

    public List<DetalleOrdenEntity> getDetallesByIdOrden(int id_orden) {
        String sql = "SELECT id_detalle, id_orden, id_producto, cantidad, precio_unitario FROM detalle_orden WHERE id_orden = :id_orden order by id_detalle";

        try (Connection con = sql2o.open()) {
            return con.createQuery(sql)
                    .addParameter("id_orden", id_orden)
                    .executeAndFetch(DetalleOrdenEntity.class);
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
}
