package br.com.milhas.gerenciador.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.milhas.gerenciador.model.Usuario;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    // Spring Data JPA vai criar a consulta automaticamente baseada no nome do método
    Optional<Usuario> findByEmail(String email);
}