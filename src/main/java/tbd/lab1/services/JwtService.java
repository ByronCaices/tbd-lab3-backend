package tbd.lab1.services;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.concurrent.TimeUnit;

@Service
public class JwtService {

    // Este es el código secreto que está en la fase de firma del JWT
    // En un ambiente de producción, este valor debe ser guardado en un lugar seguro
    private static String SECRET = "yo";
    private static Algorithm ALGORITHM = Algorithm.HMAC256(SECRET);
    private static Long JWT_EXPIRATION = TimeUnit.MINUTES.toMillis(5); // Modifica este valor para cambiar la duración del token
    private static Long JWT_REFRESH_EXPIRATION = TimeUnit.DAYS.toMillis(15); // Modifica este valor para cambiar la duración del token


    public String generateToken(String username) {
        return create(username, JWT_EXPIRATION);
    }

    public String generateRefreshToken(String username) {
        return create(username, JWT_REFRESH_EXPIRATION);
    }

    // Este método crea un JWT con el nombre de usuario
    public String create(String username, Long expiration) {
        return JWT.create()
                .withIssuer(username)
                .withClaim("username", username)
                .withSubject(username)
                .withIssuedAt(new Date(System.currentTimeMillis()))
                .withExpiresAt(new Date(System.currentTimeMillis() + expiration)) // Modifica este valor para cambiar la duración del token
                .sign(ALGORITHM);
    }

    // Este método verifica si un JWT es válido
    public boolean isValid(String jwt){
        try {
            JWT.require(ALGORITHM)
                    .build()
                    .verify(jwt);
            return true;
        } catch (JWTVerificationException e) {
            return false;
        }
    }

    public boolean validateRefreshToken(String token, String username) {
        String usernameToken = getUsername(token);
        return (usernameToken.equals(username) && !isTokenExpired(token));
    }

    private boolean isTokenExpired(String token){
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token){
        return JWT.require(ALGORITHM)
                .build()
                .verify(token)
                .getExpiresAt();
    }

    // Este método extrae el nombre de usuario de un JWT
    public String getUsername(String jwt){
        return JWT.require(ALGORITHM)
                .build()
                .verify(jwt)
                .getSubject();
    }
}
