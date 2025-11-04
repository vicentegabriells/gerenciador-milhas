package br.com.milhas.gerenciador.security;

import br.com.milhas.gerenciador.repository.UsuarioRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component // 1. Marca esta classe como um componente gerenciado pelo Spring
public class SecurityFilter extends OncePerRequestFilter { // 2. Garante que o filtro rode 1x por requisição

    @Autowired
    private TokenService tokenService; // Nosso serviço de token para validar

    @Autowired
    private UsuarioRepository usuarioRepository; // Para buscar o usuário no banco

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        
        // 3. Recupera o token do cabeçalho da requisição
        var tokenJWT = recuperarToken(request);

        if (tokenJWT != null) {
            // 4. Valida o token e pega o subject (email)
            var subject = tokenService.getSubject(tokenJWT);

            if (subject != null) {
                // 5. Se o token for válido, busca o usuário no banco
                UserDetails usuario = usuarioRepository.findByEmail(subject)
                        .orElseThrow(() -> new RuntimeException("Usuário não encontrado no filtro"));

                // 6. Cria um objeto de autenticação para o Spring Security
                var authentication = new UsernamePasswordAuthenticationToken(usuario, null, usuario.getAuthorities());

                // 7. Define o usuário como autenticado no contexto de segurança
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }

        // 8. Continua a cadeia de filtros (permite a requisição prosseguir)
        filterChain.doFilter(request, response);
    }

    // Método auxiliar para extrair o token do cabeçalho "Authorization"
    private String recuperarToken(HttpServletRequest request) {
        var authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader != null) {
            // O token vem no formato "Bearer <token>", então removemos o prefixo "Bearer "
            return authorizationHeader.replace("Bearer ", "").trim();
        }
        return null; // Retorna nulo se não encontrar o cabeçalho
    }
}
