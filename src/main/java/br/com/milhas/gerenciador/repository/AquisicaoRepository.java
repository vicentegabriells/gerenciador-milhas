package br.com.milhas.gerenciador.repository;

import br.com.milhas.gerenciador.model.Aquisicao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query; // 1. IMPORTAR
import org.springframework.data.repository.query.Param; // 2. IMPORTAR
import org.springframework.stereotype.Repository;

import java.math.BigDecimal; // 3. IMPORTAR
import java.util.List;

@Repository
public interface AquisicaoRepository extends JpaRepository<Aquisicao, Long> {

    // --- Método que já existia ---
    List<Aquisicao> findByCartaoUsuarioEmail(String email);

    // --- 4. NOVO MÉTODO ADICIONADO ---
    /**
     * Calcula a média de dias entre a data da compra e a data prevista de crédito
     * para todas as aquisições de um usuário específico.
     * Esta é uma consulta SQL nativa (PostgreSQL).
     *
     * @param emailUsuario O e-mail do usuário logado.
     * @return A média de dias (em BigDecimal) ou null se não houver dados.
     */
    @Query(value = "SELECT AVG(a.data_prevista_credito - a.data_compra) " +
                   "FROM aquisicoes a " +
                   "JOIN cartoes c ON a.cartao_id = c.id " +
                   "JOIN usuarios u ON c.usuario_id = u.id " +
                   "WHERE u.email = :emailUsuario",
           nativeQuery = true) // <-- Indica que é SQL nativo
    BigDecimal findPrazoMedioRecebimentoPorUsuario(@Param("emailUsuario") String emailUsuario);
}