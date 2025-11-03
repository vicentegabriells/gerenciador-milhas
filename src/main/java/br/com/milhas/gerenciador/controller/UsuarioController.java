package br.com.milhas.gerenciador.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.milhas.gerenciador.model.Usuario;
import br.com.milhas.gerenciador.service.UsuarioService;

@RestController // 1. Define que esta classe é um Controller REST
@RequestMapping("/api/usuarios") // 2. Define o prefixo do URL para todos os métodos deste controller
public class UsuarioController {

    @Autowired // 3. Injeta o nosso serviço de usuário
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
            return ResponseEntity.badRequest().body(null); // Simplificado, depois melhoramos a msg de erro
        }
    }
}