package br.com.milhas.gerenciador.controller;

import br.com.milhas.gerenciador.dto.UsuarioAtualizacaoDTO; // 1. IMPORTAR
import br.com.milhas.gerenciador.model.Usuario;
import br.com.milhas.gerenciador.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication; // 2. IMPORTAR
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    // --- Método que já existia (Cadastro) ---
    @PostMapping
    public ResponseEntity<Usuario> cadastrarUsuario(@RequestBody Usuario usuario) {
        try {
            Usuario novoUsuario = usuarioService.cadastrar(usuario);
            return ResponseEntity.status(201).body(novoUsuario);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }
    
    // --- Método que já existia (Teste de Auth) ---
    @GetMapping("/teste-auth")
    public ResponseEntity<String> testeAutenticacao() {
        return ResponseEntity.ok("Você está autenticado!");
    }

    // --- 3. NOVO MÉTODO ADICIONADO ---
    /**
     * Endpoint para o usuário logado atualizar seu perfil (apenas o nome).
     * Acessível via: PUT /api/usuarios/perfil
     * Requer autenticação (Token JWT).
     */
    @PutMapping("/perfil")
    public ResponseEntity<Usuario> atualizarPerfil(
            @RequestBody UsuarioAtualizacaoDTO dto, 
            Authentication authentication
    ) {
        try {
            // 1. Pega o e-mail do usuário logado (do token)
            String emailUsuarioLogado = authentication.getName();

            // 2. Chama o serviço para realizar a atualização
            Usuario usuarioAtualizado = usuarioService.atualizarPerfil(emailUsuarioLogado, dto);

            // 3. Retorna 200 OK com o usuário atualizado
            return ResponseEntity.ok(usuarioAtualizado);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }
}