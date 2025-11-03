package br.com.milhas.gerenciador.service;

import br.com.milhas.gerenciador.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AutenticacaoService implements UserDetailsService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    // Este método é chamado pelo Spring Security quando o usuário tenta fazer login
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Nossa lógica para buscar o usuário pelo e-mail
        return usuarioRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado com o e-mail: " + username));
    }
}