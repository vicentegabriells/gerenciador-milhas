package br.com.milhas.gerenciador.repository;

import br.com.milhas.gerenciador.model.Bandeira;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BandeiraRepository extends JpaRepository<Bandeira, Long> {
}