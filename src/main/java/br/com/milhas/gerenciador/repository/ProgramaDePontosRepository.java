package br.com.milhas.gerenciador.repository;

import br.com.milhas.gerenciador.model.ProgramaDePontos;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProgramaDePontosRepository extends JpaRepository<ProgramaDePontos, Long> {
    
}