package br.com.milhas.gerenciador.dto;

import br.com.milhas.gerenciador.model.Cartao;

import java.math.BigDecimal;

public record CartaoResponseDTO(
        Long id,
        String nome,
        BigDecimal saldoDePontos,
        BigDecimal fatorConversao, // --- NOVO CAMPO ADICIONADO ---
        String nomeBandeira,
        String nomePrograma
) {
    
    // Construtor auxiliar para facilitar a conversão
    public CartaoResponseDTO(Cartao cartao) {
        this(
                cartao.getId(),
                cartao.getNome(),
                cartao.getSaldoDePontos(),
                cartao.getFatorConversao(), // --- MAPEANDO O NOVO CAMPO ---
                cartao.getBandeira().getNome(),
                cartao.getProgramaDePontos().getNome()
        );
    }
}