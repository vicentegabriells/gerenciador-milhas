package br.com.milhas.gerenciador.model;

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
}