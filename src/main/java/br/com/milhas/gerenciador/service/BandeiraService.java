package br.com.milhas.gerenciador.service;

import br.com.milhas.gerenciador.model.Bandeira;
import br.com.milhas.gerenciador.repository.BandeiraRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BandeiraService {

    // 1. Injeta o repositório de bandeiras
    @Autowired
    private BandeiraRepository bandeiraRepository;

    /**
     * Cadastra uma nova bandeira no banco de dados.
     * @param bandeira O objeto Bandeira a ser salvo.
     * @return A Bandeira salva (com o ID).
     * @throws RuntimeException se uma bandeira com o mesmo nome já existir.
     */
    public Bandeira cadastrar(Bandeira bandeira) {
        // REGRA DE NEGÓCIO: Não permitir nomes duplicados
        if (bandeiraRepository.findByNome(bandeira.getNome()).isPresent()) {
            throw new RuntimeException("Bandeira com o nome '" + bandeira.getNome() + "' já existe.");
        }

        // Salva a nova bandeira no banco
        return bandeiraRepository.save(bandeira);
    }

    /**
     * Lista todas as bandeiras cadastradas no banco de dados.
     * @return Uma lista de todas as Bandeiras.
     */
    public List<Bandeira> listarTodas() {
        return bandeiraRepository.findAll();
    }

    // Futuramente, podemos adicionar métodos como:
    // - buscarPorId(Long id)
    // - atualizar(Long id, Bandeira bandeira)
    // - deletar(Long id)
}
