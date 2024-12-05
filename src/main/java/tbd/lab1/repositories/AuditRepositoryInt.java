package tbd.lab1.repositories;

import tbd.lab1.models.ClienteMultiplesCompras;
import tbd.lab1.models.UsuarioActivo;
import java.util.List;

public interface AuditRepositoryInt {
    List<UsuarioActivo> reporteUsuariosMasActivos();
    ClienteMultiplesCompras clientesMultiplesCompras();
}
