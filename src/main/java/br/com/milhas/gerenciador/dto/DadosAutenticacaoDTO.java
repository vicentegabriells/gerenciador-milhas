package br.com.milhas.gerenciador.dto;

// Este record representa o JSON que o frontend vai enviar no corpo da requisição de login
public record DadosAutenticacaoDTO(String email, String senha) {
}