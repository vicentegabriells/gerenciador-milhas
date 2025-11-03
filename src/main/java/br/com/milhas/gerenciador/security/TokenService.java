package br.com.milhas.gerenciador.security;

import br.com.milhas.gerenciador.model.Usuario;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;

@Service
public class TokenService {

    @Value("${api.security.token.secret}")
    private String secret;

    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    // Método responsável por gerar o Token JWT
    public String gerarToken(Usuario usuario) {
        try {
            Instant expirationTime = gerarTempoDeExpiracao();

            return Jwts.builder()
                    .issuer("API Gerenciador de Milhas")         // CORREÇÃO: 'setIssuer' virou 'issuer'
                    .subject(usuario.getEmail())                // CORREÇÃO: 'setSubject' virou 'subject'
                    .issuedAt(new Date())                       // CORREÇÃO: 'setIssuedAt' virou 'issuedAt'
                    .expiration(Date.from(expirationTime))      // CORREÇÃO: 'setExpiration' virou 'expiration'
                    .signWith(getSigningKey())
                    .compact();
        } catch (Exception e) {
            throw new RuntimeException("Erro ao gerar token JWT", e);
        }
    }

    // Método responsável por validar o token e extrair o Subject (email)
    public String getSubject(String tokenJWT) {
        if (tokenJWT == null || tokenJWT.isBlank()) {
            return null;
        }

        try {
            // CORREÇÃO: A forma de fazer o "parse" do token mudou
            Claims claims = Jwts.parser() // Começa com .parser()
                    .verifyWith(getSigningKey()) // Passa a chave de verificação
                    .build()
                    .parseSignedClaims(tokenJWT) // Faz o parse do token
                    .getPayload(); // Pega o conteúdo (claims)

            return claims.getSubject();
        } catch (Exception e) {
            // Token inválido (expirado, assinatura incorreta, etc.)
            return null;
        }
    }

    // Define o tempo de expiração do token (ex: 2 horas a partir de agora)
    private Instant gerarTempoDeExpiracao() {
        return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-03:00"));
    }
}