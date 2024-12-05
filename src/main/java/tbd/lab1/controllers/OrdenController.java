package tbd.lab1.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tbd.lab1.entities.OrdenEntity;
import tbd.lab1.services.OrdenService;

import java.util.List;

@RestController
@RequestMapping("/api/ordenes")
@CrossOrigin("*")
public class OrdenController {

    private static final Logger logger = LoggerFactory.getLogger(OrdenController.class);

    @Autowired
    OrdenService ordenService;

    @PostMapping("/")
    public ResponseEntity<OrdenEntity> saveProducto(@RequestBody OrdenEntity orden) {
        logger.info("Recibido objeto Orden: {}", orden);
        OrdenEntity NewOrden = ordenService.saveOrden(orden);
        return ResponseEntity.ok(NewOrden);
    }

    @GetMapping("/id-orden/{id}")
    public ResponseEntity<OrdenEntity> getOrdenById(@PathVariable Integer id) {
        OrdenEntity orden = ordenService.getOrdenById(id);
        return ResponseEntity.ok(orden);
    }

    @GetMapping("/")
    public ResponseEntity<List<OrdenEntity>> listOrden() {
        List<OrdenEntity> ordenes = ordenService.getAllOrdenes();
        return ResponseEntity.ok(ordenes);
    }

    @PutMapping("/")
    public ResponseEntity<OrdenEntity> updateOrden(@RequestBody OrdenEntity orden) {
        boolean isUpdated = ordenService.updateOrden(orden);
        if (isUpdated) {
            return ResponseEntity.ok(orden);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/delete-orden/{id}")
    public ResponseEntity<Boolean> deleteOrdenById(@PathVariable Integer id) throws Exception {
        ordenService.deleteOrden(id);
        return ResponseEntity.noContent().build();
    }

}
