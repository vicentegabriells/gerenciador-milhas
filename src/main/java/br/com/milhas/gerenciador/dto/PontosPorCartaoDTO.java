package br.com.milhas.gerenciador.dto;

import java.math.BigDecimal;

/**
 * DTO (Data Transfer Object) específico para o relatório de
 * "Total de Pontos por Cartão".
 */

public record PontosPorCartaoDTO(
        String nomeCartao,
        BigDecimal totalPontos
) {
}