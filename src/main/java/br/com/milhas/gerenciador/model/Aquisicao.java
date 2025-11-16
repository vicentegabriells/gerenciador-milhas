package br.com.milhas.gerenciador.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "aquisicoes")
@Getter
@Setter

public class Aquisicao {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String descricao; // Ex: "Compra na Amazon"

    @Column(nullable = false)
    private BigDecimal valorGasto; // Valor da compra

    @Column(nullable = false)
    private BigDecimal pontosCalculados; // Pontos a serem recebidos [cite: 21]

    @Column(nullable = false)
    private LocalDate dataCompra;

    @Column(nullable = false)
    private LocalDate dataPrevistaCredito; // Para "quanto tempo falta" [cite: 23]

    @Enumerated(EnumType.STRING) // Salva o nome do enum (ex: "PENDENTE") no banco
    @Column(nullable = false)
    private StatusCredito status;

    private String caminhoComprovante; // Caminho/nome do arquivo de upload 

    // Relacionamento: Muitas aquisições para Um cartão
    @ManyToOne
    @JoinColumn(name = "cartao_id", nullable = false)
    private Cartao cartao;

}