package tbd.lab1.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import tbd.lab1.models.ClienteMultiplesCompras;
import tbd.lab1.models.UsuarioActivo;
import tbd.lab1.services.AuditService;

import java.util.List;

@RestController
@RequestMapping("/api/auditoria")
public class AuditController {

    @Autowired
    private AuditService auditService;

    @GetMapping("/usuariosMasActivos")
    public ResponseEntity<List<UsuarioActivo>> obtenerUsuariosMasActivos() {
        List<UsuarioActivo> usuarios = auditService.obtenerUsuariosMasActivos();
        return ResponseEntity.ok(usuarios);
    }

    @GetMapping("/clientesMultiplesCompras")
    public ResponseEntity<ClienteMultiplesCompras> obtenerClientesMultiplesCompras() {

        ClienteMultiplesCompras reporte = auditService.obtenerClientesMultiplesCompras();
        return ResponseEntity.ok(reporte);
    }

}
