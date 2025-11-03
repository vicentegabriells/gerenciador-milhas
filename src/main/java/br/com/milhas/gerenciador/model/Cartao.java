package br.com.milhas.gerenciador.model;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

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
    private BigDecimal saldoDePontos; // Conforme requisito: "Controlar o saldo atual de pontos"

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
}