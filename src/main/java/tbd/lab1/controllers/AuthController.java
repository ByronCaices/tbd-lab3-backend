package tbd.lab1.controllers;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.util.WebUtils;
import tbd.lab1.dtos.TokenResponseDTO;
import tbd.lab1.entities.UsuarioEntity;
import tbd.lab1.services.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<TokenResponseDTO> login(@RequestBody Map<String, Object> payload, HttpServletResponse response){
        TokenResponseDTO tokenResponseDTO = authService.authenticate(
                (String) payload.get("username"),
                (String) payload.get("password"),
                response
        );
        return ResponseEntity.ok(tokenResponseDTO);
    }

    @PostMapping("/refresh")
    public ResponseEntity<TokenResponseDTO> refresh(HttpServletRequest request, HttpServletResponse response){
        try {
            String refreshToken = WebUtils.getCookie(request, "refreshToken").getValue();
            TokenResponseDTO tokenResponseDTO = authService.refresh(refreshToken);
            if (tokenResponseDTO == null){
                return ResponseEntity.badRequest().build();
            }
            return ResponseEntity.ok(tokenResponseDTO);
        } catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
    }

    // crea un usuario
    @PostMapping("/register")
    public ResponseEntity<UsuarioEntity> saveUsuario(@RequestBody UsuarioEntity usuario) {
        try {
            UsuarioEntity NewUsuario = authService.createUsuario(usuario);
            if (NewUsuario != null) {
                return ResponseEntity.ok(NewUsuario);
            } else {
                return ResponseEntity.badRequest().build();
            }
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }

    }
}