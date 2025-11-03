package br.com.milhas.gerenciador.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.milhas.gerenciador.model.Bandeira;

@Repository
public interface BandeiraRepository extends JpaRepository<Bandeira, Long> {
}