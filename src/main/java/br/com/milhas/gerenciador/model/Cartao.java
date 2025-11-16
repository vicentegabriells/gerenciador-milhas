package br.com.milhas.gerenciador.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;
import java.util.List; // 1. IMPORTAR A LISTA

@Entity
@Table(name = "cartoes")
@Getter
@Setter
public class Cartao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false)
    private BigDecimal saldoDePontos;

    @Column(nullable = false)
    private BigDecimal fatorConversao;

    // --- RELACIONAMENTOS ---

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "bandeira_id", nullable = false)
    private Bandeira bandeira;

    @ManyToOne
    @JoinColumn(name = "programa_id", nullable = false)
    private ProgramaDePontos programaDePontos;

    // --- NOVO RELACIONAMENTO ADICIONADO ---
    // Um Cartão pode ter muitas Aquisições
    @OneToMany(mappedBy = "cartao", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Aquisicao> aquisicoes;
}