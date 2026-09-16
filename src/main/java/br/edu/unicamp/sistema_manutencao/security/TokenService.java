package br.edu.unicamp.sistema_manutencao.security;

import br.edu.unicamp.sistema_manutencao.entities.Usuario;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Service
public class TokenService {

    // O Spring vai buscar essa senha secreta no arquivo application.properties
    @Value("${api.security.token.secret}")
    private String secret;

    public String generateToken(Usuario usuario) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            return JWT.create()
                    .withIssuer("gmao-unicamp")
                    .withSubject(usuario.getEmail()) // Guardamos o email dentro do token
                    .withClaim("role", usuario.getRole().name()) // Guardamos se ele é ADMIN ou USER
                    .withExpiresAt(genExpirationDate()) // Define o vencimento
                    .sign(algorithm);
        } catch (JWTCreationException exception) {
            throw new RuntimeException("Erro ao gerar token JWT", exception);
        }
    }

    public String validateToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            return JWT.require(algorithm)
                    .withIssuer("gmao-unicamp")
                    .build()
                    .verify(token)
                    .getSubject(); // Devolve o email do usuário se o token for válido
        } catch (JWTVerificationException exception) {
            return ""; // Retorna vazio se o token for falso, alterado ou expirado
        }
    }

    private Instant genExpirationDate() {
        // Expira exatamente 2 horas a partir do momento atual em UTC
        return Instant.now().plus(2, ChronoUnit.HOURS);
    }
}