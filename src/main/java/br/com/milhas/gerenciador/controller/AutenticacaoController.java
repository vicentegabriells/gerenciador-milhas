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
@RequestMapping("/api/login")
public class AutenticacaoController {

    @Autowired
    private AuthenticationManager manager; // Componente do Spring Security para autenticar

    @Autowired
    private TokenService tokenService; // Nosso serviço de token

    @PostMapping
    public ResponseEntity<TokenJwtDTO> efetuarLogin(@RequestBody DadosAutenticacaoDTO dados) {
        // 1. Cria um objeto de autenticação com os dados recebidos
        var authenticationToken = new UsernamePasswordAuthenticationToken(dados.email(), dados.senha());

        // 2. Chama o AuthenticationManager para validar as credenciais
        Authentication authentication = manager.authenticate(authenticationToken);

        // 3. Se as credenciais forem válidas, gera o token JWT
        var usuarioAutenticado = (Usuario) authentication.getPrincipal();
        String tokenJWT = tokenService.gerarToken(usuarioAutenticado);

        // 4. Retorna o token para o cliente
        return ResponseEntity.ok(new TokenJwtDTO(tokenJWT));
    }
}