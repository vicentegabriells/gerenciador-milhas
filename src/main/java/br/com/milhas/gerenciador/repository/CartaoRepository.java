package br.com.milhas.gerenciador.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.milhas.gerenciador.model.Cartao;

@Repository
public interface CartaoRepository extends JpaRepository<Cartao, Long> {

    // Método para buscar todos os cartões de um usuário específico
    List<Cartao> findByUsuarioId(Long usuarioId);
}