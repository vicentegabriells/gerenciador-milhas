package br.com.milhas.gerenciador.repository;

import br.com.milhas.gerenciador.dto.PontosPorCartaoDTO; // 1. IMPORTAR O NOVO DTO
import br.com.milhas.gerenciador.model.Cartao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query; // 2. IMPORTAR O @Query
import org.springframework.data.repository.query.Param; // 3. IMPORTAR O @Param
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartaoRepository extends JpaRepository<Cartao, Long> {

    // Método que já existia:
    List<Cartao> findByUsuarioId(Long usuarioId);

    // --- NOVO MÉTODO ADICIONADO ---
    /**
     * Busca de forma otimizada os dados para o relatório de pontos por cartão,
     * filtrando pelo e-mail do usuário e já retornando o DTO.
     * @param emailUsuario O e-mail do usuário logado.
     * @return Uma lista de PontosPorCartaoDTO.
     */
    @Query("SELECT new br.com.milhas.gerenciador.dto.PontosPorCartaoDTO(c.nome, c.saldoDePontos) " +
           "FROM Cartao c " +
           "WHERE c.usuario.email = :emailUsuario")
    List<PontosPorCartaoDTO> findPontosPorCartaoByUsuarioEmail(@Param("emailUsuario") String emailUsuario);
}
