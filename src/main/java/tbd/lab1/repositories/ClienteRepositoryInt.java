package tbd.lab1.repositories;
import tbd.lab1.entities.ClienteEntity;
import java.util.List;

public interface ClienteRepositoryInt {
    ClienteEntity saveCliente(ClienteEntity cliente);

    List<ClienteEntity> getClientes();

    boolean deleteCliente(Integer id);

    ClienteEntity getClienteById(Integer id);

    boolean updateCliente(ClienteEntity cliente);
}
