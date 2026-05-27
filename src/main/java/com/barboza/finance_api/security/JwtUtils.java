package com.barboza.finance_api.security;

import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtils {
    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private long expiration;

    // Genera un token JWT a partir del email del usuario. Se llama cuando usuario hace login exitoso
    public String generateToken(String email) {
        return Jwts.builder()
            .subject(email) // subject es la identidad principal del token
            .issuedAt(new Date()) // issuedAt es la fecha/hora en que se creó el token
            // expiration es la fecha/hora en que vence el token
            // System.currentTimeMillis() es el momento actual en milisegundos
            // se le suma el valor de jwt.expiration (ej: 86400000 = 24 horas)
            .expiration(new Date(System.currentTimeMillis() + expiration))
            // signWith firma el token con nuestra SecretKey
            // esto garantiza que nadie pueda modificar el token sin invalidarlo
            .signWith(getSigningKey())
            .compact(); // construye y retorna el token como String
    }

    // Extrae el email guardado dentro de un token JWT. Usado para saber qué usuario hace un request
    public String extractEmail(String token) {
        return Jwts.parser()
            // verifyWith indica con qué llave debe verificar la firma del token
            // si el token fue alterado, la verificación falla automáticamente
            .verifyWith(getSigningKey())
            .build() // build() construye el parser con la configuración indicada
            // parseSignedClaims decodifica el token y verifica su firma
            // si el token es inválido o está vencido lanza excepción
            .parseSignedClaims(token)
            // getPayload() obtiene el contenido del token (los "claims")
            // claims son los datos guardados dentro del token
            .getPayload()
            // getSubject() retorna el valor que pusimos en .subject()
            // en este caso, el email del usuario
            .getSubject();
    }

    // Verifica si un token es válido (no vencido, no alterado, bien formado)
    public boolean isTokenValid(String token) {
        try {
            // descripcion del pipe arriba
            Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token);
            return true;
        } catch (JwtException e) {
            // JwtException es la clase padre de todos los errores JWT:
            // - ExpiredJwtException: el token ya venció
            // - MalformedJwtException: el token fue alterado o tiene mal formato
            // - SignatureException: la firma no coincide con nuestra SecretKey
            return false;
        } catch (IllegalArgumentException e) {
            // IllegalArgumentException se lanza si el token es null o está vacío
            return false;
        }
    }

    // Método privado auxiliar que convierte el secret (String Base64)
    // en una SecretKey que JJWT usa para firmar y verificar tokens
    private SecretKey getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secret); // Convierte el String Base64 en un array de bytes

        // construye la SecretKey a partir de esos bytes HMAC-SHA es el algoritmo de firma
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
