package tbd.lab1.repositories;


import org.sql2o.Connection;
import org.sql2o.Sql2o;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import tbd.lab1.entities.OrdenEntity;
import java.util.ArrayList;
import java.util.List;

@Repository
public class OrdenRepository implements OrdenRepositoryInt {

    @Autowired
    private Sql2o sql2o;

    public OrdenEntity saveOrden(OrdenEntity orden) {
        // Validar que todos los campos necesarios estén presentes
        if (orden.getFecha_orden() == null ||
                orden.getEstado() == null ||
                orden.getTotal() == null ||
                orden.getId_cliente() == null) {

            throw new IllegalArgumentException("Todos los campos deben estar completos: fecha_orden, estado, id_cliente y total.");
        }

        String sql = "INSERT INTO orden (fecha_orden, estado, id_cliente, total) VALUES (:fecha_orden, :estado, :id_cliente, :total) RETURNING id_orden";

        try (Connection con = sql2o.open()) {
            Integer idOrden = con.createQuery(sql, true)
                    .addParameter("fecha_orden", orden.getFecha_orden())
                    .addParameter("estado", orden.getEstado())
                    .addParameter("id_cliente", orden.getId_cliente())
                    .addParameter("total", orden.getTotal())
                    .executeUpdate()
                    .getKey(Integer.class); // Obtener el ID generado

            orden.setId_orden(idOrden);
            return orden; // Devolver la orden guardada
        } catch (Exception e) {
            e.printStackTrace();
            return null; // En caso de error, devolver null
        }
    }

    public OrdenEntity getOrdenById(Integer id) {
        String sql = "SELECT o.id_orden, o.fecha_orden, o.estado, " +
                "o.id_cliente, o.total " +
                "FROM orden o " +
                "JOIN cliente c ON o.id_cliente = c.id_cliente " +
                "WHERE o.id_orden = :id";

        try (Connection con = sql2o.open()) {
            OrdenEntity orden = con.createQuery(sql)
                    .addParameter("id", id)
                    .executeAndFetchFirst(OrdenEntity.class);

            return orden;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }



    public List<OrdenEntity> getOrdenes() {
        String sql = "SELECT o.id_orden AS id_orden, o.fecha_orden AS fecha_orden, o.estado AS estado, " +
                "o.id_cliente AS cliente, o.total AS total " +
                "FROM orden o " +
                "JOIN cliente c ON o.id_cliente = c.id_cliente";

        try (Connection con = sql2o.open()) {
            return con.createQuery(sql)
                    .executeAndFetch(OrdenEntity.class);
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
    public List<OrdenEntity> getAllOrdenes(){
        String sql = "SELECT * FROM orden";
        try (Connection con = sql2o.open()) {
            return con.createQuery(sql)
                    .executeAndFetch(OrdenEntity.class);
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }



    public boolean updateOrden(OrdenEntity orden) {
        String sql = "UPDATE orden SET fecha_orden = :fecha_orden, estado = :estado, total = :total, id_cliente = :id_cliente WHERE id_orden = :id_orden";

        try (Connection con = sql2o.open()) {
            int affectedRows = con.createQuery(sql)
                    .addParameter("fecha_orden", orden.getFecha_orden())
                    .addParameter("estado", orden.getEstado())
                    .addParameter("total", orden.getTotal())
                    .addParameter("id_cliente", orden.getId_cliente()) // Obtener directamente el ID del cliente
                    .addParameter("id_orden", orden.getId_orden())
                    .executeUpdate()
                    .getResult(); // Obtener el número de filas afectadas

            return affectedRows > 0; // Devuelve true si se actualizó al menos una fila
        } catch (Exception e) {
            e.printStackTrace();
            return false; // Devuelve false si ocurre un error
        }
    }

    public boolean deleteOrden(Integer id) {
        String sql = "DELETE FROM orden WHERE id_orden = :id";
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
}