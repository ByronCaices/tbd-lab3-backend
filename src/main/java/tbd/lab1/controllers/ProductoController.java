package tbd.lab1.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tbd.lab1.entities.ProductoEntity;
import tbd.lab1.services.ProductoService;

import java.util.List;

@RestController
@RequestMapping("/api/productos")
@CrossOrigin("*")
public class ProductoController {
    @Autowired
    ProductoService productoService;

    @PostMapping("/")
    public ResponseEntity<ProductoEntity> saveProducto(@RequestBody ProductoEntity producto) {
        ProductoEntity NewProducto = productoService.saveProducto(producto);
        return ResponseEntity.ok(NewProducto);
    }

    @GetMapping("/id-producto/{id}")
    public ResponseEntity<ProductoEntity> getProductoById(@PathVariable Integer id) {
        ProductoEntity producto = productoService.getProductoById(id);
        return ResponseEntity.ok(producto);
    }

    @GetMapping("/")
    public ResponseEntity<List<ProductoEntity>> listProducto() {
        try {
            List<ProductoEntity> productos = productoService.getAllProductos();
            return ResponseEntity.ok(productos);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    // actualiza producto
    @PutMapping("/")
    public ResponseEntity<ProductoEntity> updateProducto(@RequestBody ProductoEntity producto) {
        boolean isUpdated = productoService.updateProducto(producto);
        if (isUpdated) {
            return ResponseEntity.ok(producto);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // borra un solo producto
    @DeleteMapping("/delete-producto/{id}")
    public ResponseEntity<Boolean> deleteProductoById(@PathVariable Integer id) throws Exception {
        productoService.deleteProducto(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/desactivarProductos")
    public ResponseEntity<String> desactivarProductosSinStock() {
        productoService.desactivarProductosSinStock();
        return ResponseEntity.ok("Productos sin stock desactivados correctamente.");
    }

}
