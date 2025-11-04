package br.com.milhas.gerenciador.dto;

// Representa o JSON que a API devolve após um login bem-sucedido: {"token": "..."}
public record TokenJwtDTO(String token) {
}