package br.com.milhas.gerenciador.controller;

import br.com.milhas.gerenciador.model.Usuario;
import br.com.milhas.gerenciador.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController // 1. Define que esta classe é um Controller REST
@RequestMapping("/api/usuarios") // 2. Define o prefixo do URL
public class UsuarioController {

    @Autowired // 3. Injeta nosso serviço de usuário
    private UsuarioService usuarioService;

    // 4. Mapeia para HTTP POST em "/api/usuarios" (Endpoint PÚBLICO)
    @PostMapping
    public ResponseEntity<Usuario> cadastrarUsuario(@RequestBody Usuario usuario) {
        try {
            Usuario novoUsuario = usuarioService.cadastrar(usuario);
            // 6. Retorna o usuário criado com o status HTTP 201 Created
            return ResponseEntity.status(201).body(novoUsuario);
        } catch (RuntimeException e) {
            // 7. Em caso de erro (ex: e-mail duplicado)
            return ResponseEntity.badRequest().body(null);
        }
    }

    // --- NOSSO NOVO MÉTODO DE TESTE ---
    // Mapeia para HTTP GET em "/api/usuarios/teste-auth" (Endpoint PROTEGIDO)
    @GetMapping("/teste-auth")
    public ResponseEntity<String> testeAutenticacao() {
        // Este endpoint só será acessível se o usuário enviar um token JWT válido
        return ResponseEntity.ok("Você está autenticado!");
    }
}