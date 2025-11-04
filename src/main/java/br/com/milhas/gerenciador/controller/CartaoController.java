package br.com.milhas.gerenciador.controller;

import br.com.milhas.gerenciador.dto.CartaoCadastroDTO;
import br.com.milhas.gerenciador.dto.CartaoResponseDTO;
import br.com.milhas.gerenciador.model.Usuario;
import br.com.milhas.gerenciador.service.CartaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cartoes") // URL base: /api/cartoes
public class CartaoController {

    @Autowired
    private CartaoService cartaoService;

    /**
     * Endpoint para cadastrar um novo cartão.
     * Acessível via: POST /api/cartoes
     * Requer autenticação (Token JWT).
     * @param dto Os dados do cartão vindos do corpo da requisição.
     * @param authentication Objeto injetado pelo Spring Security com os dados do usuário logado.
     */
    @PostMapping
    public ResponseEntity<CartaoResponseDTO> cadastrarCartao(@RequestBody CartaoCadastroDTO dto, Authentication authentication) {
        try {
            // 1. Pega o e-mail do usuário (o "username") de dentro do token
            String emailUsuarioLogado = authentication.getName();

            // 2. Chama o serviço passando os dados do DTO e o e-mail do usuário
            CartaoResponseDTO cartaoSalvo = cartaoService.cadastrar(dto, emailUsuarioLogado);

            return ResponseEntity.status(201).body(cartaoSalvo);
        } catch (RuntimeException e) {
            // Retorna 400 em caso de erro (ex: ID da bandeira não encontrado)
            return ResponseEntity.badRequest().body(null);
        }
    }

    /**
     * Endpoint para listar todos os cartões do usuário logado.
     * Acessível via: GET /api/cartoes
     * Requer autenticação (Token JWT).
     * @param authentication Objeto com os dados do usuário logado.
     */
    @GetMapping
    public ResponseEntity<List<CartaoResponseDTO>> listarCartoesDoUsuario(Authentication authentication) {
        // 1. Pega o e-mail do usuário logado
        String emailUsuarioLogado = authentication.getName();

        // 2. Chama o serviço para buscar a lista
        List<CartaoResponseDTO> listaDeCartoes = cartaoService.listarPorUsuario(emailUsuarioLogado);

        return ResponseEntity.ok(listaDeCartoes);
    }
}

