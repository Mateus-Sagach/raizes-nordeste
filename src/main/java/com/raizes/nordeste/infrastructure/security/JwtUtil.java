package com.raizes.nordeste.infrastructure.security;


import com.raizes.nordeste.domain.model.Usuario;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private Long expiration;

    //Converte a string do jwt.secret em um objeto SecretKey que o JJWT usa para assinar e verificar tokens. O HMAC-SHA é o algoritmo criptográfico
    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(
                secret.getBytes(StandardCharsets.UTF_8));
    }

    public String gerarToken(Usuario usuario) {
        return Jwts.builder()
                .subject(usuario.getEmail())
                .claim("perfil", usuario.getPerfil().name())
                .claim("userId", usuario.getId())
                .issuedAt(new Date())
                .expiration(new Date(
                        System.currentTimeMillis() + expiration * 1000))
                .signWith(getSigningKey())
                .compact();
    }

    public String extrairEmail(String token) {
        return getClaims(token).getSubject();
    }

    public boolean isTokenValido(String token) {
        try {
            getClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    private Claims getClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}