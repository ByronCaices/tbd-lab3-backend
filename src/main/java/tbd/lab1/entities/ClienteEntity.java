package tbd.lab1.entities;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClienteEntity {

    private Integer id_cliente;

    private String nombre;

    private String direccion;

    private String email;

    private String telefono;
}
