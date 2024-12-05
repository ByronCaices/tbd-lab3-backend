package tbd.lab1.repositories;

import tbd.lab1.entities.UsuarioEntity;

import java.util.List;

public interface UsuarioRepositoryInt {
    UsuarioEntity saveUsuario(UsuarioEntity usuario);

    List<UsuarioEntity> getUsuarios();

    boolean deleteUsuario(Integer id);

    UsuarioEntity getUsuarioById(Integer id);

    boolean updateUsuario(UsuarioEntity usuario);
}
