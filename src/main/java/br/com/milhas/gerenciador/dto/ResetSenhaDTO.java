package br.com.milhas.gerenciador.dto;

public record ResetSenhaDTO(
        String token,
        String novaSenha,
        String confirmacaoSenha
) {
}