package br.com.milhas.gerenciador.controller;

import br.com.milhas.gerenciador.model.ProgramaDePontos;
import br.com.milhas.gerenciador.service.ProgramaDePontosService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/programas") // Define o URL base para "/api/programas"
public class ProgramaDePontosController {

    @Autowired
    private ProgramaDePontosService programaService; // Injeta o serviço

    /**
     * Endpoint para cadastrar um novo programa de pontos.
     * Acessível via: POST /api/programas
     * Requer autenticação (Token JWT).
     */
    @PostMapping
    public ResponseEntity<ProgramaDePontos> cadastrarPrograma(@RequestBody ProgramaDePontos programa) {
        try {
            ProgramaDePontos novoPrograma = programaService.cadastrar(programa);
            // Retorna 201 Created com o programa criado no corpo
            return ResponseEntity.status(201).body(novoPrograma);
        } catch (RuntimeException e) {
            // Se o serviço lançar uma exceção (ex: nome duplicado)
            return ResponseEntity.badRequest().body(null);
        }
    }

    /**
     * Endpoint para listar todos os programas de pontos cadastrados.
     * Acessível via: GET /api/programas
     * Requer autenticação (Token JWT).
     */
    @GetMapping
    public ResponseEntity<List<ProgramaDePontos>> listarProgramas() {
        List<ProgramaDePontos> programas = programaService.listarTodos();
        // Retorna 200 OK com a lista de programas no corpo
        return ResponseEntity.ok(programas);
    }
}
