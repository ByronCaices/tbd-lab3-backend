package tbd.lab1.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClienteMultiplesCompras {
    private Integer num_clientes; // Número de clientes
    private String productos_comprados; // Productos comprados
}
