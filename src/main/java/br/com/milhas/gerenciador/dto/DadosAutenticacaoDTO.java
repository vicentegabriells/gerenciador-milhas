package br.com.milhas.gerenciador.dto;

// record é uma forma moderna (Java 16+) de criar classes imutáveis para dados.
// Representa o JSON que o usuário envia no login: {"email": "...", "senha": "..."}
public record DadosAutenticacaoDTO(String email, String senha) {
}