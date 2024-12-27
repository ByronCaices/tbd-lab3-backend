package tbd.lab1.services;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tbd.lab1.entities.AlmacenEntity;
import tbd.lab1.entities.OrdenEntity;
import tbd.lab1.repositories.AlmacenRepository;


import java.util.List;
@Service
@RequiredArgsConstructor
public class AlmacenService {

    @Autowired
    AlmacenRepository almacenRepository;

    public AlmacenEntity saveAlmacen(AlmacenEntity almacen){
        return almacenRepository.saveAlmacen(almacen);
    }

    public List<AlmacenEntity> getAllAlmacenes() {
        return almacenRepository.getAlmacenes();
    }

    public AlmacenEntity getAlmacenById(Integer id) {
        AlmacenEntity almacen = almacenRepository.getAlmacenById(id);
        return almacen;
    }

    public boolean deleteAlmacen(Integer id) throws Exception {
        try {
            Boolean response = almacenRepository.deleteAlmacen(id);
            if (response) {
                return true;
            } else {
                throw new Exception("No se pudo eliminar el almacen");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public boolean updateAlmacen(AlmacenEntity almacen) {
        // vemos si el cliente existe en la base de datos
        if (almacenRepository.getAlmacenById(almacen.getId_almacen()) != null) {
            // actualizamos el cliente usando el método del repositorio
            return almacenRepository.updateAlmacen(almacen);
        }
        return false;
    }

    public List<OrdenEntity> obtenerOrdenesCercanas(int idAlmacen, double radioKm) {
        return almacenRepository.obtenerOrdenesCercanas(idAlmacen, radioKm);
    }

}