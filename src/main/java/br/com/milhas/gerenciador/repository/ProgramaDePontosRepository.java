package br.com.milhas.gerenciador.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.milhas.gerenciador.model.ProgramaDePontos;

@Repository
public interface ProgramaDePontosRepository extends JpaRepository<ProgramaDePontos, Long> {
}