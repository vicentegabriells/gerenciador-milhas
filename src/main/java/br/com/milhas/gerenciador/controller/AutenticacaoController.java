package br.com.milhas.gerenciador.controller;

import br.com.milhas.gerenciador.dto.DadosAutenticacaoDTO;
import br.com.milhas.gerenciador.dto.TokenJwtDTO;
import br.com.milhas.gerenciador.model.Usuario;
import br.com.milhas.gerenciador.security.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/login") // Endpoint de login
public class AutenticacaoController {

    // Componente do Spring Security que gerencia a autenticação
    @Autowired
    private AuthenticationManager manager;

    // Nosso serviço de token
    @Autowired
    private TokenService tokenService;

    // Mapeia para requisições POST em "/api/login"
    @PostMapping
    public ResponseEntity<TokenJwtDTO> efetuarLogin(@RequestBody DadosAutenticacaoDTO dados) {
        // 1. Cria um token de autenticação (ainda não validado)
        var authenticationToken = new UsernamePasswordAuthenticationToken(dados.email(), dados.senha());

        // 2. Efetivamente autentica o usuário (valida e-mail e senha)
        // O Spring chama nosso AutenticacaoService e PasswordEncoder aqui por baixo dos panos
        Authentication authentication = manager.authenticate(authenticationToken);

        // 3. Se a autenticação deu certo, pega o objeto Usuario
        var usuarioAutenticado = (Usuario) authentication.getPrincipal();

        // 4. Gera o Token JWT para este usuário
        String tokenJWT = tokenService.gerarToken(usuarioAutenticado);

        // 5. Retorna o token para o cliente com status 200 OK
        return ResponseEntity.ok(new TokenJwtDTO(tokenJWT));
    }
}