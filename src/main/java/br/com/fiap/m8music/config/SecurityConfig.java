package br.com.fiap.m8music.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final SecurityFilter securityFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.POST, "/auth/login").permitAll()
                        .requestMatchers(HttpMethod.POST, "/auth/register-cantor").permitAll()
                        .requestMatchers(HttpMethod.POST, "/auth/register-cliente").permitAll()
                        .requestMatchers(
                                "/v3/api-docs/**",
                                "/swagger-ui/**",
                                "/swagger-ui.html"
                        ).permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/cantores/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/cantores/**").hasRole("CANTOR")
                        .requestMatchers(HttpMethod.PUT, "/api/cantores/**").hasRole("CANTOR")
                        .requestMatchers(HttpMethod.DELETE, "/api/cantores/**").hasRole("CANTOR")
                        .requestMatchers(HttpMethod.GET, "/api/musica/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/musica/**").hasRole("CANTOR")
                        .requestMatchers(HttpMethod.PUT, "/api/musica/**").hasRole("CANTOR")
                        .requestMatchers(HttpMethod.DELETE, "/api/musica/**").hasRole("CANTOR")
                        .requestMatchers(HttpMethod.POST, "/api/pedidos/**").hasRole("CLIENTE")
                        .requestMatchers("/api/pedidos/**").authenticated()
                        .requestMatchers("/api/clientes/**").authenticated()
                        .requestMatchers("/api/spotify/**").authenticated()

                        .anyRequest().authenticated()
                )
                .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
