package br.com.milhas.gerenciador.controller;

import br.com.milhas.gerenciador.dto.*; // 1. IMPORTAR (agora importa todos os DTOs)
import br.com.milhas.gerenciador.model.Usuario;
import br.com.milhas.gerenciador.security.TokenService;
import br.com.milhas.gerenciador.service.UsuarioService; // 2. IMPORTAR
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
@RequestMapping("/api") // 3. MUDANÇA: URL base agora é /api
public class AutenticacaoController {

    @Autowired
    private AuthenticationManager manager;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private UsuarioService usuarioService; // 4. INJETAR

    // --- Endpoint que já existia ---
    // Mapeado para POST /api/login
    @PostMapping("/login")
    public ResponseEntity<TokenJwtDTO> efetuarLogin(@RequestBody DadosAutenticacaoDTO dados) {
        var authenticationToken = new UsernamePasswordAuthenticationToken(dados.email(), dados.senha());
        Authentication authentication = manager.authenticate(authenticationToken);
        var usuarioAutenticado = (Usuario) authentication.getPrincipal();
        String tokenJWT = tokenService.gerarToken(usuarioAutenticado);
        return ResponseEntity.ok(new TokenJwtDTO(tokenJWT));
    }

    // --- 5. NOVO MÉTODO ADICIONADO (Solicitação) ---
    /**
     * Endpoint público para solicitar a recuperação de senha.
     * Acessível via: POST /api/forgot-password
     */
    @PostMapping("/forgot-password")
    public ResponseEntity<RespostaTokenDTO> solicitarResetSenha(@RequestBody SolicitacaoSenhaDTO dto) {
        try {
            String token = usuarioService.solicitarResetSenha(dto);
            // Retorna o token para o usuário (simulando envio de e-mail)
            return ResponseEntity.ok(new RespostaTokenDTO(token));
        } catch (RuntimeException e) {
            // Não retornamos "Usuário não encontrado" por segurança (para não vazar e-mails)
            // Mas para o teste, vamos retornar o erro:
            return ResponseEntity.badRequest().build();
        }
    }

    // --- 6. NOVO MÉTODO ADICIONADO (Reset) ---
    /**
     * Endpoint público para efetivar a troca de senha.
     * Acessível via: POST /api/reset-password
     */
    @PostMapping("/reset-password")
    public ResponseEntity<Void> resetarSenha(@RequestBody ResetSenhaDTO dto) {
        try {
            usuarioService.resetarSenha(dto);
            // Retorna 200 OK (Vazio) se a senha foi trocada
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            // Retorna 400 Bad Request se o token for inválido, expirado ou senhas não baterem
            return ResponseEntity.badRequest().build();
        }
    }
}