package br.com.milhas.gerenciador.repository;

import br.com.milhas.gerenciador.model.Aquisicao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AquisicaoRepository extends JpaRepository<Aquisicao, Long> {

    // Método para buscar todas as aquisições de um usuário específico
    // O Spring JPA entende "findBy" + "Cartao" + "Usuario" + "Email"
    List<Aquisicao> findByCartaoUsuarioEmail(String email);
}