package br.com.milhas.gerenciador.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import br.com.milhas.gerenciador.model.Usuario;
import br.com.milhas.gerenciador.repository.UsuarioRepository;

@Service // 1. Anotação que define esta classe como um componente de Serviço
public class UsuarioService {

    // 2. Injeção de dependência: O Spring vai nos dar uma instância do Repository
    @Autowired
    private UsuarioRepository usuarioRepository;

    // Injeta o codificador de senhas que configuramos na classe SecurityConfig
    @Autowired
    private PasswordEncoder passwordEncoder;

    // 3. Método com a lógica de negócio para cadastrar um novo usuário
    public Usuario cadastrar(Usuario usuario) {
        // REGRA DE NEGÓCIO 1: Verificar se o e-mail já está em uso
        if (usuarioRepository.findByEmail(usuario.getEmail()).isPresent()) {
            // Se o Optional retornado não for vazio, significa que já existe um usuário
            throw new RuntimeException("E-mail já cadastrado.");
        }

        // REGRA DE NEGÓCIO 2: Criptografar a senha antes de salvar no banco
        // Pega a senha que veio do formulário ("123") e a transforma em um hash seguro
        String senhaCriptografada = passwordEncoder.encode(usuario.getSenha());
        usuario.setSenha(senhaCriptografada);

        // 4. Salva o novo usuário (agora com a senha criptografada) no banco
        return usuarioRepository.save(usuario);
    }

    // Aqui podemos adicionar outros métodos no futuro, como:
    // - buscarUsuarioPorId(Long id)
    // - atualizarPerfil(Usuario usuario)
    // - deletarUsuario(Long id)
}