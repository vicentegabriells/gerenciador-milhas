package br.com.milhas.gerenciador.repository;

import br.com.milhas.gerenciador.model.ProgramaDePontos;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional; // 1. Importar o Optional

@Repository
public interface ProgramaDePontosRepository extends JpaRepository<ProgramaDePontos, Long> {

    // 2. MÉTODO ADICIONADO
    // Método customizado: O Spring cria a consulta para buscar um programa pelo nome
    Optional<ProgramaDePontos> findByNome(String nome);
}