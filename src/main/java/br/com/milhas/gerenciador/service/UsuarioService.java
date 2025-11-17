package br.com.milhas.gerenciador.service;

import br.com.milhas.gerenciador.dto.ResetSenhaDTO; // 1. IMPORTAR
import br.com.milhas.gerenciador.dto.SolicitacaoSenhaDTO; // 2. IMPORTAR
import br.com.milhas.gerenciador.dto.UsuarioAtualizacaoDTO;
import br.com.milhas.gerenciador.model.Usuario;
import br.com.milhas.gerenciador.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime; // 3. IMPORTAR
import java.util.UUID; // 4. IMPORTAR

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // --- Método que já existia ---
    public Usuario cadastrar(Usuario usuario) {
        // ... (código existente sem alterações)
        if (usuarioRepository.findByEmail(usuario.getEmail()).isPresent()) {
            throw new RuntimeException("E-mail já cadastrado.");
        }
        String senhaCriptografada = passwordEncoder.encode(usuario.getSenha());
        usuario.setSenha(senhaCriptografada);
        return usuarioRepository.save(usuario);
    }

    // --- Método que já existia ---
    @Transactional
    public Usuario atualizarPerfil(String emailUsuarioLogado, UsuarioAtualizacaoDTO dto) {
        // ... (código existente sem alterações)
        Usuario usuario = usuarioRepository.findByEmail(emailUsuarioLogado)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
        if (dto.nome() != null && !dto.nome().isBlank()) {
            usuario.setNome(dto.nome());
        }
        return usuario;
    }

    // --- 5. NOVO MÉTODO ADICIONADO (Endpoint 1) ---
    /**
     * Gera um token de recuperação de senha para um usuário.
     * @param dto Contém o e-mail do usuário.
     * @return O token gerado (UUID).
     */
    @Transactional
    public String solicitarResetSenha(SolicitacaoSenhaDTO dto) {
        // 1. Busca o usuário pelo e-mail
        Usuario usuario = usuarioRepository.findByEmail(dto.email())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado com este e-mail."));

        // 2. Gera um token aleatório (UUID)
        String token = UUID.randomUUID().toString();

        // 3. Define a validade do token (ex: 1 hora a partir de agora)
        LocalDateTime expiryDate = LocalDateTime.now().plusHours(1);

        // 4. Salva o token e a data de expiração no usuário
        usuario.setResetToken(token);
        usuario.setResetTokenExpiry(expiryDate);
        usuarioRepository.save(usuario);

        // 5. Retorna o token (para simular o envio de e-mail)
        return token;
    }

    // --- 6. NOVO MÉTODO ADICIONADO (Endpoint 2) ---
    /**
     * Efetiva a troca de senha usando um token válido.
     * @param dto Contém o token, a nova senha e a confirmação.
     */
    @Transactional
    public void resetarSenha(ResetSenhaDTO dto) {
        // 1. Valida se as senhas coincidem
        if (dto.novaSenha() == null || !dto.novaSenha().equals(dto.confirmacaoSenha())) {
            throw new RuntimeException("As senhas não coincidem.");
        }

        // 2. Busca o usuário pelo token
        Usuario usuario = usuarioRepository.findByResetToken(dto.token())
                .orElseThrow(() -> new RuntimeException("Token inválido ou não encontrado."));

        // 3. Valida se o token expirou
        if (usuario.getResetTokenExpiry().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Token expirado. Solicite uma nova recuperação.");
        }

        // 4. Se tudo estiver OK, criptografa e salva a nova senha
        String senhaCriptografada = passwordEncoder.encode(dto.novaSenha());
        usuario.setSenha(senhaCriptografada);

        // 5. Invalida o token para que não possa ser usado novamente
        usuario.setResetToken(null);
        usuario.setResetTokenExpiry(null);
        usuarioRepository.save(usuario);
    }
}