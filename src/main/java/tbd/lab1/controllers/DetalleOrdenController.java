package tbd.lab1.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tbd.lab1.entities.DetalleOrdenEntity;
import tbd.lab1.services.DetalleOrdenService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/detalleordenes")
@CrossOrigin("*")
public class DetalleOrdenController {
    @Autowired
    DetalleOrdenService detalleOrdenService;

    @PostMapping("/")
    public ResponseEntity<DetalleOrdenEntity> saveDetalle(@RequestBody DetalleOrdenEntity detalle) {
        DetalleOrdenEntity NewDetalle = detalleOrdenService.saveDetalle(detalle);
        return ResponseEntity.ok(NewDetalle);
    }

    @GetMapping("/id-detalle/{id}")
    public ResponseEntity<DetalleOrdenEntity> getDetalleById(@PathVariable Integer id) {
        DetalleOrdenEntity detalle = detalleOrdenService.getDetalleById(id);
        return ResponseEntity.ok(detalle);
    }

    @GetMapping("/")
    public ResponseEntity<List<DetalleOrdenEntity>> listDetalle() {
        List<DetalleOrdenEntity> detalle = detalleOrdenService.getDetalle();
        return ResponseEntity.ok(detalle);
    }

    @PutMapping("/")
    public ResponseEntity<DetalleOrdenEntity> updateDetalle(@RequestBody DetalleOrdenEntity detalle) {
        boolean isUpdated = detalleOrdenService.updateDetalle(detalle);
        if (isUpdated) {
            return ResponseEntity.ok(detalle);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/delete-detalle/{id}")
    public ResponseEntity<Boolean> deleteDetalleById(@PathVariable Integer id) throws Exception {
        detalleOrdenService.deleteDetalle(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/gestionarDevolucion")
    public ResponseEntity<String> gestionarDevolucion(
            @RequestBody Map<String, Object> body) {

        detalleOrdenService.gestionarDevolucion(
                (Integer) body.get("idOrden"),
                (Integer) body.get("idProducto"),
                (Integer) body.get("cantidad")
        );
        return ResponseEntity.ok("Devolución gestionada correctamente.");
    }

    @GetMapping("/id-orden/{id_orden}")
    public ResponseEntity<List<DetalleOrdenEntity>> getDetallesByIdOrden(@PathVariable Integer id_orden) {
        List<DetalleOrdenEntity> detalles = detalleOrdenService.getDetallesByIdOrden(id_orden);
        return ResponseEntity.ok(detalles);
    }


}
