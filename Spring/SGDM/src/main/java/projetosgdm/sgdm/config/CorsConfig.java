package projetosgdm.sgdm.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class CorsConfig {
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/usuarios/**").permitAll() // Libera TUDO de usuarios
                        .anyRequest().permitAll() // Liberar todas as outras rotas (para DEV) — pode ser .authenticated() depois
                )
                .formLogin(login -> login.disable()); // não mostra tela login padrão
        return http.build();
    }
}
