package tbd.lab1.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "usuario")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class UsuarioEntity {

    @Id
    private String id;

    private String nombre;

    private String email;

    private String contrasena;
}
