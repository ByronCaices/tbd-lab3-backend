package tbd.lab1.services;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import tbd.lab1.entities.UsuarioEntity;
import tbd.lab1.repositories.UsuarioRepository;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UsuarioService {
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioEntity createUsuario(UsuarioEntity usuario) {
        String encodedPassword = passwordEncoder.encode(usuario.getContrasena());
        usuario.setContrasena(encodedPassword);
        return usuarioRepository.saveUsuario(usuario);
    }

    public List<UsuarioEntity> getAllUsuarios() {
        return usuarioRepository.getUsuarios();
    }

    public UsuarioEntity getUsuarioById(Integer id) {
        UsuarioEntity usuario = usuarioRepository.getUsuarioById(id);
        return usuario;
    }

    public boolean deleteUsuario(Integer id) throws Exception {
        try {
            Boolean response = usuarioRepository.deleteUsuario(id);
            if (response) {
                return true;
            } else {
                throw new Exception("No se pudo eliminar el usuario");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public boolean updateUsuario(UsuarioEntity usuario) {
        // vemos si el cliente existe en la base de datos
        if (usuarioRepository.getUsuarioById(usuario.getId_usuario()) != null) {
            // actualizamos el cliente usando el método del repositorio
            return usuarioRepository.updateUsuario(usuario);
        }
        return false;
    }

    public UsuarioEntity getUsuarioByEmail(String email) {
        return usuarioRepository.getUsuarioEmail(email);
    }

}
