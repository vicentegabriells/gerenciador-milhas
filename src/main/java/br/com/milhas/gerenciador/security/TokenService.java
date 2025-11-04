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

    // 1. Injeta o valor da propriedade 'api.security.token.secret' do application.properties
    @Value("${api.security.token.secret}")
    private String secret;

    // Converte a string secreta em uma chave criptográfica segura
    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    // Método responsável por gerar o Token JWT
    public String gerarToken(Usuario usuario) {
        try {
            Instant expirationTime = gerarTempoDeExpiracao(); // Pega o tempo de expiração

            // Constrói o token (Builder pattern)
            return Jwts.builder()
                    .issuer("API Gerenciador de Milhas")         // Quem emitiu o token
                    .subject(usuario.getEmail())                // "Dono" do token (identificador único)
                    .issuedAt(new Date())                       // Data da geração
                    .expiration(Date.from(expirationTime))      // Data da expiração
                    .signWith(getSigningKey())                  // Assina com nossa chave secreta
                    .compact();                                 // Finaliza e retorna a string do token
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
            // Faz o "parse" do token usando a chave secreta para verificar a assinatura
            Claims claims = Jwts.parser()
                    .verifyWith(getSigningKey()) // Verifica se a assinatura é válida
                    .build()
                    .parseSignedClaims(tokenJWT) // Decodifica o token
                    .getPayload(); // Pega o conteúdo (claims/dados)

            return claims.getSubject(); // Retorna o e-mail (subject)
        } catch (Exception e) {
            // Se o token estiver expirado, inválido ou a assinatura errada, retorna nulo
            return null;
        }
    }

    // Método privado que define a expiração do token (2 horas a partir de agora)
    private Instant gerarTempoDeExpiracao() {
        return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-03:00"));
    }
}