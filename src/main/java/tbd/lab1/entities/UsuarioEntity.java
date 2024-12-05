package tbd.lab1.entities;

import lombok.Data;

@Data
public class UsuarioEntity {

    private Integer id_usuario;

    private String nombre;

    private String email;

    private String contrasena;
}
