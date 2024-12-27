package tbd.lab1.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tbd.lab1.entities.AlmacenEntity;
import tbd.lab1.entities.OrdenEntity;
import tbd.lab1.services.AlmacenService;
import java.util.List;

@RestController
@RequestMapping("/api/almacen")
@CrossOrigin("*")
public class AlmacenController {

    @Autowired
    AlmacenService almacenService;

    @PostMapping("/")
    public ResponseEntity<AlmacenEntity> saveAlmacen(@RequestBody AlmacenEntity almacen) {
        AlmacenEntity NewAlmacen = almacenService.saveAlmacen(almacen);
        return ResponseEntity.ok(NewAlmacen);
    }

    @GetMapping("/id-almacen/{id}")
    public ResponseEntity<AlmacenEntity> getAlmacenById(@PathVariable Integer id) {
        AlmacenEntity almacen = almacenService.getAlmacenById(id);
        return ResponseEntity.ok(almacen);
    }

    @GetMapping("/")
    public ResponseEntity<List<AlmacenEntity>> listAlmacen() {
        try {
            List<AlmacenEntity> almacenes = almacenService.getAllAlmacenes();
            return ResponseEntity.ok(almacenes);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/")
    public ResponseEntity<AlmacenEntity> updateAlmacen(@RequestBody AlmacenEntity almacen) {
        boolean isUpdated = almacenService.updateAlmacen(almacen);
        if (isUpdated) {
            return ResponseEntity.ok(almacen);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/delete-almacen/{id}")
    public ResponseEntity<Boolean> deleteAlmacenById(@PathVariable Integer id) throws Exception {
        almacenService.deleteAlmacen(id);
        return ResponseEntity.noContent().build();
    }




    //RF 14: Ordenes más cercanas
    @GetMapping("/ordenes-cercanas/{idAlmacen}/{radioKm}")
    public ResponseEntity<List<OrdenEntity>> obtenerOrdenesCercanas(@PathVariable int idAlmacen, @PathVariable double radioKm) {
        try {
            List<OrdenEntity> ordenesCercanas = almacenService.obtenerOrdenesCercanas(idAlmacen, radioKm);
            return ResponseEntity.ok(ordenesCercanas);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }



}