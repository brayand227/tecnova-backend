package com.tienda.backend.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtUtils {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private Long expiration;

    /**
     * Genera una clave segura para firmar los tokens JWT
     * Si la clave proporcionada es muy corta (< 32 bytes), genera una clave aleatoria
     * (esto es útil para desarrollo, pero en producción siempre usa una clave segura)
     */
    private SecretKey getSigningKey() {
        // Convertir la clave secreta a bytes
        byte[] keyBytes = secret.getBytes();
        
        // Para HS256, la clave debe tener al menos 32 bytes (256 bits)
        if (keyBytes.length < 32) {
            // En desarrollo, generar una clave segura aleatoria
            // ADVERTENCIA: En producción, siempre debes proporcionar una clave segura en application.properties
            System.out.println("WARNING: La clave secreta es muy corta. Generando clave aleatoria para desarrollo.");
            return Keys.secretKeyFor(SignatureAlgorithm.HS256);
        }
        
        // Si la clave tiene la longitud adecuada, usarla
        return Keys.hmacShaKeyFor(keyBytes);
    }

    /**
     * Genera un token JWT para el usuario
     */
    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("roles", userDetails.getAuthorities());
        return createToken(claims, userDetails.getUsername());
    }

    /**
     * Crea el token JWT con los claims y el subject especificados
     */
    private String createToken(Map<String, Object> claims, String subject) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expiration);

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * Valida si el token es válido para el usuario proporcionado
     */
    public Boolean validateToken(String token, UserDetails userDetails) {
        try {
            final String username = extractUsername(token);
            return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
        } catch (JwtException | IllegalArgumentException e) {
            // Si hay cualquier error con el token, considerarlo inválido
            return false;
        }
    }

    /**
     * Extrae el nombre de usuario del token
     */
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * Extrae un claim específico del token
     */
    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    /**
     * Extrae todos los claims del token
     */
    private Claims extractAllClaims(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException e) {
            throw e; // Re-lanzar para manejar específicamente tokens expirados
        } catch (JwtException e) {
            throw new JwtException("Token inválido: " + e.getMessage());
        }
    }

    /**
     * Verifica si el token ha expirado
     */
    private Boolean isTokenExpired(String token) {
        try {
            final Date expiration = extractExpiration(token);
            return expiration.before(new Date());
        } catch (ExpiredJwtException e) {
            return true; // Si el token ya expiró, retornar true
        }
    }

    /**
     * Extrae la fecha de expiración del token
     */
    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    /**
     * Método adicional para obtener la información del token sin validar (útil para debugging)
     */
    public String getTokenInfo(String token) {
        try {
            Claims claims = extractAllClaims(token);
            return String.format("Token válido - Usuario: %s, Emitido: %s, Expira: %s",
                    claims.getSubject(),
                    claims.getIssuedAt(),
                    claims.getExpiration());
        } catch (Exception e) {
            return "Token inválido: " + e.getMessage();
        }
    }
}