package br.com.milhas.gerenciador.dto;

/**
 * DTO para receber os dados de atualização de perfil do usuário.
 * Apenas o nome pode ser alterado por este DTO.
 */
public record UsuarioAtualizacaoDTO(
        String nome
) {
}
