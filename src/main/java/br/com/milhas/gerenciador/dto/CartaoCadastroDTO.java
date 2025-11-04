package br.com.milhas.gerenciador.dto;

import java.math.BigDecimal;

public record CartaoCadastroDTO(
        String nome,
        BigDecimal saldoDePontos,
        BigDecimal fatorConversao, // --- NOVO CAMPO ADICIONADO ---
        Long bandeiraId,
        Long programaId
) {
}