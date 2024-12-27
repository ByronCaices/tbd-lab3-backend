package tbd.lab1.repositories;

import org.sql2o.Connection;
import org.sql2o.Sql2o;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import tbd.lab1.entities.AlmacenEntity;
import tbd.lab1.entities.ClienteEntity;

import java.util.List;

@Repository
public class ClienteRepository implements ClienteRepositoryInt {

    @Autowired
    private Sql2o sql2o;

    // Guarda un cliente usando sql2o
    public ClienteEntity saveCliente(ClienteEntity cliente) {
        String sql = "INSERT INTO cliente (nombre, direccion, email, telefono, posicion, longitud, latitud) VALUES (:nombre, :direccion, :email, :telefono, :posicion, :longitud, :latitud)";
        try (Connection con = sql2o.open()) {
            // Insertar el cliente en la base de datos
            Integer id = (Integer) con.createQuery(sql, true)  // true indica que se quiere obtener el ID generado
                    .addParameter("nombre", cliente.getNombre())
                    .addParameter("direccion", cliente.getDireccion())
                    .addParameter("email", cliente.getEmail())
                    .addParameter("telefono", cliente.getTelefono())
                    .addParameter("posicion", cliente.getPosicion())
                    .addParameter("longitud", cliente.getLongitud())
                    .addParameter("latitud", cliente.getLatitud())
                    .executeUpdate()
                    .getKey(); // Obtener el ID generado

            // Establecer el ID generado al cliente
            cliente.setId_cliente(id);
            System.out.println("###############\nCliente guardado con ID: " + id + "\n##################");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return cliente;
    }

    // Obtiene todos los clientes ingresados en la base de datos
    public List<ClienteEntity> getClientes() {
        String sql = "SELECT * FROM cliente order by id_cliente";
        try (Connection con = sql2o.open()) {
            return con.createQuery(sql)
                    .executeAndFetch(ClienteEntity.class);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // Elimina un cliente por ID
    public boolean deleteCliente(Integer id) {
        String sql = "DELETE FROM cliente WHERE id_cliente = :id";
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
    public ClienteEntity getClienteById(Integer id) {
        String sql = "SELECT * FROM cliente WHERE id_cliente = :id";
        try (Connection con = sql2o.open()) {
            return con.createQuery(sql)
                    .addParameter("id", id)
                    .executeAndFetchFirst(ClienteEntity.class); // Obtiene el primer resultado
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // Actualiza un cliente existente
    public boolean updateCliente(ClienteEntity cliente) {
        String sql = "UPDATE cliente SET nombre = :nombre, direccion = :direccion, email = :email, telefono = :telefono, telefono = :telefono, posicion = :posicion, longitud = :longitud, latitud = :latitud WHERE id_cliente = :id";
        try (Connection con = sql2o.open()) {
            int affectedRows = con.createQuery(sql)
                    .addParameter("nombre", cliente.getNombre())
                    .addParameter("direccion", cliente.getDireccion())
                    .addParameter("email", cliente.getEmail())
                    .addParameter("telefono", cliente.getTelefono())
                    .addParameter("posicion", cliente.getPosicion())
                    .addParameter("longitud", cliente.getLongitud())
                    .addParameter("latitud", cliente.getLatitud())
                    .addParameter("id", cliente.getId_cliente())
                    .executeUpdate()
                    .getResult(); // Obtener el número de filas afectadas

            return affectedRows > 0; // Devuelve true si se actualizó al menos una fila
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<AlmacenEntity> getAlmacenMasCercano(Integer idCliente) {
        String sql = "SELECT id_almacen, nombre, posicion, latitud, longitud FROM obtener_almacen_mas_cercano(:id_cliente_input)";
        try (Connection con = sql2o.open()) {
            return con.createQuery(sql)
                    .addParameter("id_cliente_input", idCliente)
                    .executeAndFetch(AlmacenEntity.class);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public Double obtenerDistanciaClienteAlmacen(Integer idCliente, Integer idAlmacen) {
        String sql = "SELECT obtener_distancia_cliente_almacen(:id_cliente_input, :id_almacen_input)";
        try (Connection con = sql2o.open()) {
            return con.createQuery(sql)
                    .addParameter("id_cliente_input", idCliente)
                    .addParameter("id_almacen_input", idAlmacen)
                    .executeScalar(Double.class);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}