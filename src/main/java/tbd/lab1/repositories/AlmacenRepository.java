package tbd.lab1.repositories;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.sql2o.Connection;
import org.sql2o.Sql2o;
import tbd.lab1.entities.AlmacenEntity;
import tbd.lab1.entities.OrdenEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;


@Repository
public class AlmacenRepository implements AlmacenRepositoryInt{

    @Autowired
    private Sql2o sql2o;

    public AlmacenEntity saveAlmacen(AlmacenEntity almacen) {
        // Validar que los campos necesarios no estén nulos
        if (almacen.getNombre() == null || almacen.getNombre().isEmpty()) {
            throw new IllegalArgumentException("El nombre del almacén es obligatorio.");
        }
        if (almacen.getPosicion() == null || almacen.getPosicion().isEmpty()) {
            throw new IllegalArgumentException("La posición del almacén es obligatoria.");
        }
        if (almacen.getLongitud() == null || almacen.getLongitud().isEmpty()) {
            throw new IllegalArgumentException("La longitud del almacén es obligatoria.");
        }
        if (almacen.getLatitud() == null || almacen.getLatitud().isEmpty()) {
            throw new IllegalArgumentException("La latitud del almacén es obligatoria.");
        }

        String sql = "INSERT INTO almacen (nombre, posicion, longitud, latitud) " +
                "VALUES (:nombre, :posicion, :longitud, :latitud) " +
                "RETURNING id_almacen";

        try (Connection con = sql2o.open()) {
            Integer newAlmacenId = con.createQuery(sql, true)
                    .addParameter("nombre", almacen.getNombre())
                    .addParameter("posicion", almacen.getPosicion())
                    .addParameter("longitud", almacen.getLongitud())
                    .addParameter("latitud", almacen.getLatitud())
                    .executeUpdate()
                    .getKey(Integer.class);

            almacen.setId_almacen(newAlmacenId);
            return almacen;

        } catch (Exception e) {
            e.printStackTrace();
            return null; // En caso de error, devolver null
        }
    }

    public AlmacenEntity getAlmacenById(Integer id) {
        String sql = "SELECT id_almacen, nombre, posicion, longitud, latitud " +
                "FROM almacen WHERE id_almacen = :id";

        try (Connection con = sql2o.open()) {
            return con.createQuery(sql)
                    .addParameter("id", id)
                    .executeAndFetchFirst(AlmacenEntity.class);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<AlmacenEntity> getAlmacenes() {
        String sql = "SELECT * FROM almacen ORDER BY id_almacen";

        try (Connection con = sql2o.open()) {
            return con.createQuery(sql)
                    .executeAndFetch(AlmacenEntity.class);

        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public boolean updateAlmacen(AlmacenEntity almacen) {
        String sql = "UPDATE almacen SET nombre = :nombre, posicion = :posicion, " +
                "longitud = :longitud, latitud = :latitud " +
                "WHERE id_almacen = :idAlmacen";

        try (Connection con = sql2o.open()) {
            int affectedRows = con.createQuery(sql)
                    .addParameter("nombre", almacen.getNombre())
                    .addParameter("posicion", almacen.getPosicion())
                    .addParameter("longitud", almacen.getLongitud())
                    .addParameter("latitud", almacen.getLatitud())
                    .addParameter("idAlmacen", almacen.getId_almacen())
                    .executeUpdate()
                    .getResult();

            return affectedRows > 0; // Devuelve true si se actualizó al menos una fila

        } catch (Exception e) {
            e.printStackTrace();
            return false; // Devuelve false si ocurre un error
        }
    }

    public boolean deleteAlmacen(Integer id) {
        String sql = "DELETE FROM almacen WHERE id_almacen = :id";

        try (Connection con = sql2o.open()) {
            int affectedRows = con.createQuery(sql)
                    .addParameter("id", id)
                    .executeUpdate()
                    .getResult();

            return affectedRows > 0; // Devuelve true si se eliminó al menos una fila

        } catch (Exception e) {
            e.printStackTrace();
            return false; // Devuelve false si ocurre un error
        }
    }

    public List<OrdenEntity> obtenerOrdenesCercanas(int idAlmacen, double radioKm) {
        String sql = "SELECT * FROM obtener_ordenes_cercanas(:idAlmacen, :radioKm)";
        try (Connection con = sql2o.open()) {
            return con.createQuery(sql)
                    .addParameter("idAlmacen", idAlmacen)
                    .addParameter("radioKm", radioKm)
                    .executeAndFetch(OrdenEntity.class);
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

}