package tbd.lab1.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tbd.lab1.entities.ProductoEntity;
import tbd.lab1.repositories.ProductoRepository;

import java.util.ArrayList;

@Service
public class ProductoService {
    @Autowired
    ProductoRepository productoRepository;

    public ProductoEntity saveProducto(ProductoEntity producto){
        return productoRepository.saveProducto(producto);
    }

    //obtiene producto segun su id
    public ProductoEntity getProductoById(Integer id){
        return productoRepository.getProductoById(id);
    }

    //obtiene todos los productos
    public ArrayList<ProductoEntity> getAllProductos(){
        return (ArrayList<ProductoEntity>) productoRepository.getProductos();
    }

    public ArrayList<ProductoEntity> getProductos(){
        return (ArrayList<ProductoEntity>) productoRepository.getProductos();
    }

    public boolean updateProducto(ProductoEntity producto) {
        // vemos si el producto existe en la base de datos
        if (productoRepository.getProductoById(producto.getId_producto()) != null) {
            // actualizamos el product usando el método del repositorio
            return productoRepository.updateProducto(producto);
        }
        return false;
    }

    public boolean deleteProducto(Integer id) throws Exception {
        try{
            productoRepository.deleteProducto(id);
            return true;
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    public void desactivarProductosSinStock() {
        productoRepository.desactivarProductosSinStock();
    }

}
