package br.com.milhas.gerenciador.config;

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

@Configuration // Indica que é uma classe de configuração
@EnableWebSecurity // Habilita a configuração de segurança web
public class SecurityConfig {

    // Define a cadeia de filtros de segurança
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(csrf -> csrf.disable()) // Desabilita o CSRF (vulnerabilidade de formulários web)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // Define a política de sessão como stateless (essencial para API REST/JWT)
                .authorizeHttpRequests(authorize -> authorize
                        // Permite acesso PÚBLICO ao endpoint de cadastro (POST)
                        .requestMatchers(HttpMethod.POST, "/api/usuarios").permitAll()
                        // Permite acesso PÚBLICO ao endpoint de login (POST)
                        .requestMatchers(HttpMethod.POST, "/api/login").permitAll()
                        // Exige autenticação para qualquer outra requisição
                        .anyRequest().authenticated()
                )
                .build();
    }

    // Expõe o AuthenticationManager (usado no AutenticacaoController)
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    // Expõe o codificador de senhas (usado no UsuarioService e pelo Spring Security)
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}