package br.com.milhas.gerenciador.repository;

import br.com.milhas.gerenciador.model.Cartao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartaoRepository extends JpaRepository<Cartao, Long> {

    // Método customizado: Busca todos os cartões de um usuário específico
    List<Cartao> findByUsuarioId(Long usuarioId);
}