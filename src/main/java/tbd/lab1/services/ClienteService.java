package tbd.lab1.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tbd.lab1.entities.ClienteEntity;
import tbd.lab1.repositories.ClienteRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

    public List<ClienteEntity> getAllClientes() {
        return clienteRepository.getClientes();
    }

    public ClienteEntity getClienteById(Integer id) {
        ClienteEntity cliente = clienteRepository.getClienteById(id);
        return cliente;
    }

    public ClienteEntity createCliente(ClienteEntity clienteEntity) {
        return clienteRepository.saveCliente(clienteEntity);
    }

    public ClienteEntity saveCliente(ClienteEntity cliente) {
        return clienteRepository.saveCliente(cliente);
    }

    public ArrayList<ClienteEntity> getClientes() {
        return (ArrayList<ClienteEntity>) clienteRepository.getClientes();
    }

    public boolean deleteCliente(Integer id) throws Exception {
        try {
            clienteRepository.deleteCliente(id);
            return true;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public boolean updateCliente(ClienteEntity cliente) {
        // vemos si el cliente existe en la base de datos
        if (clienteRepository.getClienteById(cliente.getId_cliente()) != null) {
            // actualizamos el cliente usando el método del repositorio
            return clienteRepository.updateCliente(cliente);
        }
        return false;
    }

}
