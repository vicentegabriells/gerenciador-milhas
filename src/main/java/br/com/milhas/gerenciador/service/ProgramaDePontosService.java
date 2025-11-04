package br.com.milhas.gerenciador.service;

import br.com.milhas.gerenciador.model.ProgramaDePontos;
import br.com.milhas.gerenciador.repository.ProgramaDePontosRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProgramaDePontosService {

    // 1. Injeta o repositório de programas de pontos
    @Autowired
    private ProgramaDePontosRepository programaRepository;

    /**
     * Cadastra um novo programa de pontos no banco de dados.
     * @param programa O objeto ProgramaDePontos a ser salvo.
     * @return O ProgramaDePontos salvo (com o ID).
     * @throws RuntimeException se um programa com o mesmo nome já existir.
     */
    public ProgramaDePontos cadastrar(ProgramaDePontos programa) {
        // REGRA DE NEGÓCIO: Não permitir nomes duplicados
        if (programaRepository.findByNome(programa.getNome()).isPresent()) {
            throw new RuntimeException("Programa de pontos com o nome '" + programa.getNome() + "' já existe.");
        }

        // Salva o novo programa no banco
        return programaRepository.save(programa);
    }

    /**
     * Lista todos os programas de pontos cadastrados no banco de dados.
     * @return Uma lista de todos os ProgramasDePontos.
     */
    public List<ProgramaDePontos> listarTodos() {
        return programaRepository.findAll();
    }
}