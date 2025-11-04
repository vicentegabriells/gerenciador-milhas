package br.com.milhas.gerenciador.repository;

import br.com.milhas.gerenciador.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    // Método customizado: O Spring cria a consulta para buscar um usuário pelo e-mail
    Optional<Usuario> findByEmail(String email);
}