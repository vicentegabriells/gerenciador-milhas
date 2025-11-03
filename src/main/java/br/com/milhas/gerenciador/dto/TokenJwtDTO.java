package br.com.milhas.gerenciador.dto;

// Este record representa o JSON que nossa API vai devolver após o login bem-sucedido
public record TokenJwtDTO(String token) {
}