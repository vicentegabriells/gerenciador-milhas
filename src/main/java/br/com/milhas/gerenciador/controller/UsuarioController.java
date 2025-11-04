package br.com.milhas.gerenciador.controller;

import br.com.milhas.gerenciador.model.Usuario;
import br.com.milhas.gerenciador.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController // 1. Define que esta classe é um Controller REST (responde JSON)
@RequestMapping("/api/usuarios") // 2. Define o prefixo do URL para todos os métodos
public class UsuarioController {

    @Autowired // 3. Injeta nosso serviço de usuário
    private UsuarioService usuarioService;

    // 4. Mapeia este método para requisições HTTP POST em "/api/usuarios"
    @PostMapping
    public ResponseEntity<Usuario> cadastrarUsuario(@RequestBody Usuario usuario) {
        // 5. @RequestBody converte o JSON do corpo da requisição para um objeto Usuario

        try {
            Usuario novoUsuario = usuarioService.cadastrar(usuario);
            // 6. Retorna o usuário criado com o status HTTP 201 Created
            return ResponseEntity.status(201).body(novoUsuario);
        } catch (RuntimeException e) {
            // 7. Em caso de erro (ex: e-mail duplicado), retorna um erro HTTP 400 Bad Request
            return ResponseEntity.badRequest().body(null);
        }
    }
    @GetMapping("/teste-auth")
        public ResponseEntity<String> testeAutenticacao() {
    return ResponseEntity.ok("Você está autenticado!");
}
}