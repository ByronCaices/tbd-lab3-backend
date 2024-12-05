package tbd.lab1.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tbd.lab1.models.ClienteMultiplesCompras;
import tbd.lab1.models.UsuarioActivo;
import tbd.lab1.repositories.AuditRepository;

import java.util.List;

@Service
public class AuditService {

    @Autowired
    private AuditRepository auditRepository;

    public List<UsuarioActivo> obtenerUsuariosMasActivos() {
        return auditRepository.reporteUsuariosMasActivos();
    }

    public ClienteMultiplesCompras obtenerClientesMultiplesCompras() {
        // Aquí puedes agregar validaciones adicionales (por ejemplo, validar formato de fechas)
        return auditRepository.clientesMultiplesCompras();
    }

}

