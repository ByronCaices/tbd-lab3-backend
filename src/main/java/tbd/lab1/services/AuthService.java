package tbd.lab1.services;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Cookie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import tbd.lab1.dtos.TokenResponseDTO;
import tbd.lab1.entities.UsuarioEntity;

@Service
public class AuthService {
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtUtil;
    private final UsuarioService usuarioService;


    @Autowired
    public AuthService(AuthenticationManager authenticationManager, JwtService jwtUtil, UsuarioService usuarioService) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.usuarioService = usuarioService;
    }

    public UsuarioEntity createUsuario(UsuarioEntity usuario) {
        return usuarioService.createUsuario(usuario);
    }

    public TokenResponseDTO authenticate(String username, String password, HttpServletResponse response) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        username,
                        password
                )
        );

        Integer idUsuario = usuarioService.getUsuarioByEmail(username).getId_usuario();
        String jwtToken = jwtUtil.generateToken(username);
        String jwtRefreshToken = jwtUtil.generateRefreshToken(username);

        // Crear la cookie para el Refresh Token
        Cookie refreshTokenCookie = new Cookie("refreshToken", jwtRefreshToken);
        refreshTokenCookie.setHttpOnly(true); // Protege contra XSS
        refreshTokenCookie.setSecure(false);  // Solo para HTTPS
        refreshTokenCookie.setPath("/"); // El endpoint que manejará la rotación
        refreshTokenCookie.setMaxAge(15 * 24 * 60 * 60); // 15 días en segundos
        refreshTokenCookie.setAttribute("SameSite", "Strict"); // O "Lax" si necesitas que funcione en enlaces externos

        // Agregar la cookie a la respuesta
        response.addCookie(refreshTokenCookie);
        //response.addHeader(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString());

        return new TokenResponseDTO(jwtToken, idUsuario);

    }

    public TokenResponseDTO refresh(String refreshToken) {
        if (refreshToken == null) {
            throw new RuntimeException("Refresh token is missing");
        }

        // Extraer el nombre de usuario del token de refresco
        String username = jwtUtil.getUsername(refreshToken);

        // Buscar el usuario en la base de datos y verificar si existe
        UsuarioEntity usuario = usuarioService.getUsuarioByEmail(username);

        if (usuario == null) {
            throw new RuntimeException("User not found");
        }

        // Verificar si el token de refresco es válido
        if (!jwtUtil.validateRefreshToken(refreshToken, usuario.getEmail())) {
            throw new RuntimeException("Invalid refresh token");
        }

        String jwtToken = jwtUtil.generateToken(usuario.getEmail());

        return new TokenResponseDTO(jwtToken, usuario.getId_usuario());
    }
}