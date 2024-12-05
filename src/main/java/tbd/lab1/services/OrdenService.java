package tbd.lab1.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tbd.lab1.entities.OrdenEntity;
import tbd.lab1.repositories.OrdenRepository;

import java.util.ArrayList;

@Service
public class OrdenService {

    private static final Logger logger = LoggerFactory.getLogger(OrdenService.class);

    @Autowired
    OrdenRepository ordenRepository;

    public OrdenEntity saveOrden(OrdenEntity orden) {
        logger.info("Recibido objeto Orden: {}", orden);
        return ordenRepository.saveOrden(orden);
    }

    public OrdenEntity getOrdenById(Integer id) {
        return ordenRepository.getOrdenById(id);
    }

    public ArrayList<OrdenEntity> getOrdenes() {
        return (ArrayList<OrdenEntity>) ordenRepository.getOrdenes();
    }
    public ArrayList<OrdenEntity> getAllOrdenes() {
        return (ArrayList<OrdenEntity>) ordenRepository.getAllOrdenes();
    }
    public boolean updateOrden(OrdenEntity orden) {
        System.out.println("Recibido objeto Orden: " + orden);
        System.out.println("Recibido objeto Orden: " + orden.getId_orden());
        // vemos si el cliente existe en la base de datos
        if (ordenRepository.getOrdenById(orden.getId_orden()) != null) {
            // actualizamos el cliente usando el método del repositorio
            return ordenRepository.updateOrden(orden);
        }
        return false;
    }

    public boolean deleteOrden(Integer id) throws Exception {
        try {
            ordenRepository.deleteOrden(id);
            return true;
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }
}
