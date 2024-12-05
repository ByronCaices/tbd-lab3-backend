package tbd.lab1.repositories;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;
import org.sql2o.Connection;
import org.sql2o.Sql2o;
import tbd.lab1.entities.UsuarioEntity;

import java.util.List;

@Repository
public class UsuarioRepository implements UsuarioRepositoryInt {

    @Autowired
    private Sql2o sql2o;

    // Guarda un cliente usando sql2o
    public UsuarioEntity saveUsuario(UsuarioEntity usuario) {
        String sql = "INSERT INTO usuario (nombre, email, contrasena) VALUES (:nombre, :email, :contrasena)";
        try (Connection con = sql2o.open()) {
            // Insertar el cliente en la base de datos
            Integer id = (Integer) con.createQuery(sql, true) // true indica que se quiere obtener el ID generado
                    .addParameter("nombre", usuario.getNombre())
                    .addParameter("email", usuario.getEmail())
                    .addParameter("contrasena", usuario.getContrasena())
                    .executeUpdate()
                    .getKey(); // Obtener el ID generado

            // Establecer el ID generado al cliente
            usuario.setId_usuario(id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return usuario;
    }

    // Obtiene todos los clientes ingresados en la base de datos
    public List<UsuarioEntity> getUsuarios() {
        String sql = "SELECT * FROM usuario";
        try (Connection con = sql2o.open()) {
            return con.createQuery(sql)
                    .executeAndFetch(UsuarioEntity.class);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // Elimina un cliente por ID
    public boolean deleteUsuario(Integer id) {
        String sql = "DELETE FROM usuario WHERE id_usuario = :id";
        try (Connection con = sql2o.open()) {
            int affectedRows = con.createQuery(sql)
                    .addParameter("id", id)
                    .executeUpdate()
                    .getResult(); // Obtener el número de filas afectadas

            return affectedRows > 0; // Devuelve true si se eliminó al menos una fila
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // Obtiene un cliente por ID
    public UsuarioEntity getUsuarioById(Integer id) {
        String sql = "SELECT * FROM usuario WHERE id_usuario = :id";
        try (Connection con = sql2o.open()) {
            return con.createQuery(sql)
                    .addParameter("id", id)
                    .executeAndFetchFirst(UsuarioEntity.class); // Obtiene el primer resultado
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // Actualiza un cliente existente
    public boolean updateUsuario(UsuarioEntity usuario) {
        String sql = "UPDATE usuario SET nombre = :nombre, email = :email WHERE id_usuario = :id";
        try (Connection con = sql2o.open()) {
            int affectedRows = con.createQuery(sql)
                    .addParameter("nombre", usuario.getNombre())
                    .addParameter("email", usuario.getEmail())
                    .addParameter("id", usuario.getId_usuario())
                    .executeUpdate()
                    .getResult(); // Obtener el número de filas afectadas

            return affectedRows > 0; // Devuelve true si se actualizó al menos una fila
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public UsuarioEntity getUsuarioId(String username) {
        try (org.sql2o.Connection con = sql2o.open()) {
            return con.createQuery("SELECT * FROM usuario WHERE username = :username")
                    .addParameter("username", username)
                    .executeAndFetchFirst(UsuarioEntity.class);
        }
    }

    public UsuarioEntity getUsuarioEmail(String username) {
        try (org.sql2o.Connection con = sql2o.open()) {
            return con.createQuery("SELECT * FROM usuario WHERE email = :email")
                    .addParameter("email", username)
                    .executeAndFetchFirst(UsuarioEntity.class);
        }
    }
}
