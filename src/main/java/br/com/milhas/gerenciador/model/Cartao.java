package br.com.milhas.gerenciador.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;

@Entity
@Table(name = "cartoes")
@Getter
@Setter
public class Cartao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome; // Ex: "Meu Visa Infinite"

    @Column(nullable = false)
    private BigDecimal saldoDePontos; // "Controlar o saldo atual de pontos"

    // --- RELACIONAMENTOS ---

    // Muitos cartões para Um usuário
    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    // Muitos cartões para Uma bandeira
    @ManyToOne
    @JoinColumn(name = "bandeira_id", nullable = false)
    private Bandeira bandeira;

    // Muitos cartões para Um programa de pontos
    @ManyToOne
    @JoinColumn(name = "programa_id", nullable = false)
    private ProgramaDePontos programaDePontos;
}