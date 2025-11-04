package br.com.milhas.gerenciador.repository;

import br.com.milhas.gerenciador.model.Bandeira;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional; // 1. Importar o Optional

@Repository
public interface BandeiraRepository extends JpaRepository<Bandeira, Long> {

    // 2. MÉTODO ADICIONADO
    // Método customizado: O Spring cria a consulta para buscar uma bandeira pelo nome
    Optional<Bandeira> findByNome(String nome);
}