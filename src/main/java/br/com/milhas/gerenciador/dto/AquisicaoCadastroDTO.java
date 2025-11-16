package br.com.milhas.gerenciador.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

// DTO que o frontend enviará (como JSON)
public record AquisicaoCadastroDTO(
        String descricao,
        BigDecimal valorGasto,
        LocalDate dataCompra,
        LocalDate dataPrevistaCredito,
        Long cartaoId
) {
}
