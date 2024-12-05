package tbd.lab1.repositories;

import tbd.lab1.entities.ProductoEntity;

import java.util.List;

public interface ProductoRepositoryInt {

    ProductoEntity saveProducto(ProductoEntity producto);
    List<ProductoEntity> getProductos();
    ProductoEntity getProductoById(Integer id);
    boolean updateProducto(ProductoEntity producto);
    boolean deleteProducto(Integer id);
    void desactivarProductosSinStock();
}
