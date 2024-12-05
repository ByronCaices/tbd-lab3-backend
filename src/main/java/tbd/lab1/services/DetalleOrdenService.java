package tbd.lab1.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tbd.lab1.entities.DetalleOrdenEntity;
import tbd.lab1.entities.ProductoEntity;

import tbd.lab1.repositories.DetalleOrdenRepository;
import tbd.lab1.repositories.ProductoRepository;

import java.util.ArrayList;

@Service
public class DetalleOrdenService {
    @Autowired
    DetalleOrdenRepository detalleOrdenRepository;
    @Autowired
    ProductoRepository productoRepository;

    public DetalleOrdenEntity saveDetalle(DetalleOrdenEntity detalle) {
        int producto_id = detalle.getId_producto();
        ProductoEntity producto = productoRepository.getProductoById(producto_id);
        producto.setStock(producto.getStock() - detalle.getCantidad());
        productoRepository.updateProducto(producto);
        return detalleOrdenRepository.saveDetalleOrden(detalle);
    }

    public DetalleOrdenEntity getDetalleById(Integer id) {
        return detalleOrdenRepository.getDetalleOrdenById(id);
    }

    public ArrayList<DetalleOrdenEntity> getDetalle() {
        return (ArrayList<DetalleOrdenEntity>) detalleOrdenRepository.getDetalleOrdenes();
    }

    public boolean updateDetalle(DetalleOrdenEntity detalle) {
        // vemos si el cliente existe en la base de datos
        if (detalleOrdenRepository.getDetalleOrdenById(detalle.getId_detalle()) != null) {
            // actualizamos el cliente usando el método del repositorio
            return detalleOrdenRepository.updateDetalleOrden(detalle);
        }
        return false;
    }

    public boolean deleteDetalle(Integer id) throws Exception {
        try {
            detalleOrdenRepository.deleteDetalleOrden(id);
            return true;
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    public void gestionarDevolucion(int idOrden, int idProducto, int cantidad) {
        // Validaciones adicionales si es necesario
        detalleOrdenRepository.gestionarDevolucion(idOrden, idProducto, cantidad);
    }

    public ArrayList<DetalleOrdenEntity> getDetallesByIdOrden(int id_orden) {
        return (ArrayList<DetalleOrdenEntity>) detalleOrdenRepository.getDetallesByIdOrden(id_orden);
    }

}
