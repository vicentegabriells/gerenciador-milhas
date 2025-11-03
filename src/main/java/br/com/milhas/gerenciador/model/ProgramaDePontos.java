package br.com.milhas.gerenciador.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "programas_de_pontos")
@Getter
@Setter
public class ProgramaDePontos {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String nome; // Ex: "Smiles", "TudoAzul", "Latam Pass"

    // O saldo de pontos será controlado no relacionamento com o cartão ou usuário,
    // mas o requisito menciona "Controlar o saldo atual de pontos de cada programa".
    // Uma forma simples é ter uma tabela de associação. Vamos adicionar o saldo na entidade Cartao por enquanto.
}