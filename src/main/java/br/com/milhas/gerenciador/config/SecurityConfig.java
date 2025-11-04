package br.com.milhas.gerenciador.config;

import br.com.milhas.gerenciador.security.SecurityFilter; // 1. Importa nosso filtro customizado
import org.springframework.beans.factory.annotation.Autowired; // 2. Importa o Autowired
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter; // 3. Importa o filtro padrão

@Configuration // Indica que é uma classe de configuração
@EnableWebSecurity // Habilita a configuração de segurança web
public class SecurityConfig {

    // 4. Injeta o nosso filtro de segurança (SecurityFilter)
    @Autowired
    private SecurityFilter securityFilter;

    // Define a cadeia de filtros de segurança (as regras de acesso)
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                // Desabilita o CSRF, pois nossa API é stateless e não usa sessões
                .csrf(csrf -> csrf.disable())
                
                // Configura a política de sessão para STATELESS (sem estado)
                // O servidor não guarda informações de sessão; cada requisição é independente
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                
                // Configura as regras de autorização para as requisições HTTP
                .authorizeHttpRequests(authorize -> authorize
                        // Permite acesso PÚBLICO ao endpoint de cadastro (POST)
                        .requestMatchers(HttpMethod.POST, "/api/usuarios").permitAll()
                        
                        // Permite acesso PÚBLICO ao endpoint de login (POST)
                        .requestMatchers(HttpMethod.POST, "/api/login").permitAll()
                        
                        // Exige autenticação para qualquer outra requisição
                        .anyRequest().authenticated()
                )
                
                // 5. Adiciona nosso filtro (securityFilter) ANTES do filtro padrão do Spring
                // Isso garante que nosso filtro de token JWT rode primeiro
                .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class)
                
                .build();
    }

    // Expõe o AuthenticationManager (usado no AutenticacaoController para o login)
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    // Expõe o codificador de senhas (BCrypt) para ser usado em toda a aplicação
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}