package br.com.milhas.gerenciador.dto;

import java.math.BigDecimal;

// Usamos os IDs da bandeira e do programa, que o frontend terá
// obtido ao listar as bandeiras e programas.
public record CartaoCadastroDTO(
        String nome,
        BigDecimal saldoDePontos,
        Long bandeiraId,
        Long programaId
) {
}