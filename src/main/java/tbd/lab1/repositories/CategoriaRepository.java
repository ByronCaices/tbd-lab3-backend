package tbd.lab1.repositories;

import org.sql2o.Connection;
import org.sql2o.Sql2o;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import tbd.lab1.entities.CategoriaEntity;

import java.util.List;

@Repository
public class CategoriaRepository implements CategoriaRepositoryInt {

    @Autowired
    private Sql2o sql2o;

    public CategoriaEntity saveCategoria(CategoriaEntity categoria) {
        String sql = "INSERT INTO categoria (nombre) VALUES (:nombre)";
        try (Connection con = sql2o.open()) {
            // Cambiar Long a Integer
            Integer id = (Integer) con.createQuery(sql, true)
                    .addParameter("nombre", categoria.getNombre())
                    .executeUpdate()
                    .getKey();

            // Establecer el id en la entidad, ahora el id es de tipo int
            categoria.setId_categoria(id); // idCategoria ahora es un int en la entidad
            return categoria;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<CategoriaEntity> getCategorias() {
        String sql = "SELECT * FROM categoria";
        try (Connection con = sql2o.open()) {
            return con.createQuery(sql)
                    .executeAndFetch(CategoriaEntity.class);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    public CategoriaEntity findByIdCategoria(Integer id) { // Cambiar Long a int
        String sql = "SELECT * FROM categoria WHERE id_categoria = :id";
        try (Connection con = sql2o.open()) {
            return con.createQuery(sql)
                    .addParameter("id", id)
                    .executeAndFetchFirst(CategoriaEntity.class);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean deleteCategoria(Integer id) { // Cambiar Long a int
        String sql = "DELETE FROM categoria WHERE id_categoria = :id";
        try (Connection con = sql2o.open()) {
            int affectedRows = con.createQuery(sql)
                    .addParameter("id", id)
                    .executeUpdate()
                    .getResult();
            return affectedRows > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateCategoria(CategoriaEntity categoria) {
        String sql = "UPDATE categoria SET nombre = :nombre WHERE id_categoria = :id";
        try (Connection con = sql2o.open()) {
            int affectedRows = con.createQuery(sql)
                    .addParameter("nombre", categoria.getNombre())
                    .addParameter("id", categoria.getId_categoria()) // idCategoria ahora es un int en la entidad
                    .executeUpdate()
                    .getResult();
            return affectedRows > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}