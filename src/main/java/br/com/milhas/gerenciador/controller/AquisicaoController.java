package br.com.milhas.gerenciador.controller;

import br.com.milhas.gerenciador.dto.AquisicaoCadastroDTO;
import br.com.milhas.gerenciador.dto.AquisicaoResponseDTO;
import br.com.milhas.gerenciador.service.AquisicaoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule; // Para converter datas (LocalDate)
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/aquisicoes")
public class AquisicaoController {

    @Autowired
    private AquisicaoService aquisicaoService;

    /**
     * Endpoint para registrar uma nova aquisição (compra com comprovante).
     * Consome "multipart/form-data".
     */
    @PostMapping(consumes = { "multipart/form-data" })
    public ResponseEntity<AquisicaoResponseDTO> registrarAquisicao(
            @RequestPart("aquisicao") String aquisicaoDtoJson, // 1. O JSON como String
            @RequestPart("comprovante") MultipartFile comprovante, // 2. O arquivo [cite: 20]
            Authentication authentication
    ) {
        try {
            // 3. Precisamos converter o JSON (String) para o DTO (Objeto) manualmente
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule()); // Habilita suporte para LocalDate
            AquisicaoCadastroDTO dto = objectMapper.readValue(aquisicaoDtoJson, AquisicaoCadastroDTO.class);

            // 4. Pega o e-mail do usuário logado (do token)
            String emailUsuarioLogado = authentication.getName();

            // 5. Chama o serviço com o DTO, o arquivo e o e-mail
            AquisicaoResponseDTO resposta = aquisicaoService.registrarAquisicao(dto, comprovante, emailUsuarioLogado);
            
            return ResponseEntity.status(201).body(resposta);
        } catch (Exception e) {
            e.printStackTrace(); // É bom ver o erro no console
            return ResponseEntity.badRequest().body(null);
        }
    }

    /**
     * Endpoint para listar todas as aquisições do usuário logado.
     */
    @GetMapping
    public ResponseEntity<List<AquisicaoResponseDTO>> listarAquisicoes(Authentication authentication) {
        String emailUsuarioLogado = authentication.getName();
        List<AquisicaoResponseDTO> lista = aquisicaoService.listarPorUsuario(emailUsuarioLogado);
        return ResponseEntity.ok(lista);
    }
}