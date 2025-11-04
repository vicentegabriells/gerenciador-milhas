package br.com.milhas.gerenciador.controller;

import br.com.milhas.gerenciador.model.Bandeira;
import br.com.milhas.gerenciador.service.BandeiraService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bandeiras") // Define o URL base para "/api/bandeiras"
public class BandeiraController {

    @Autowired
    private BandeiraService bandeiraService; // Injeta o serviço de bandeiras

    /**
     * Endpoint para cadastrar uma nova bandeira.
     * Acessível via: POST /api/bandeiras
     * Requer autenticação (Token JWT).
     */
    @PostMapping
    public ResponseEntity<Bandeira> cadastrarBandeira(@RequestBody Bandeira bandeira) {
        try {
            Bandeira novaBandeira = bandeiraService.cadastrar(bandeira);
            // Retorna 201 Created com a bandeira criada no corpo
            return ResponseEntity.status(201).body(novaBandeira);
        } catch (RuntimeException e) {
            // Se o serviço lançar uma exceção (ex: nome duplicado), retorna 400 Bad Request
            return ResponseEntity.badRequest().body(null); // Pode ser melhorado para retornar a msg de erro
        }
    }

    /**
     * Endpoint para listar todas as bandeiras cadastradas.
     * Acessível via: GET /api/bandeiras
     * Requer autenticação (Token JWT).
     */
    @GetMapping
    public ResponseEntity<List<Bandeira>> listarBandeiras() {
        List<Bandeira> bandeiras = bandeiraService.listarTodas();
        // Retorna 200 OK com a lista de bandeiras no corpo
        return ResponseEntity.ok(bandeiras);
    }
}
