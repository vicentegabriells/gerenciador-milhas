package br.com.milhas.gerenciador.dto;

import br.com.milhas.gerenciador.model.Cartao;

import java.math.BigDecimal;

public record CartaoResponseDTO(
        Long id,
        String nome,
        BigDecimal saldoDePontos,
        String nomeBandeira,
        String nomePrograma
) {
    
    // Construtor auxiliar para facilitar a conversão da Entidade Cartao para este DTO
    public CartaoResponseDTO(Cartao cartao) {
        this(
                cartao.getId(),
                cartao.getNome(),
                cartao.getSaldoDePontos(),
                cartao.getBandeira().getNome(), // Pega o nome da bandeira
                cartao.getProgramaDePontos().getNome() // Pega o nome do programa
        );
    }
}
