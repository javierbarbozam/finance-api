package com.barboza.finance_api.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity //activa el módulo de seguridad de Spring
public class SecurityConfig {
    
    private final JwtFilter jwtFilter;

    SecurityConfig(JwtFilter jwtFilter) {
        this.jwtFilter = jwtFilter;
    }

    @Bean
    // SecurityFilterChain define todas las reglas de seguridad HTTP
    public SecurityFilterChain securityFilterChain(HttpSecurity http)  throws Exception {
        http
            // CSRF es una protección para apps con sesiones/cookies, no aplica porque se usan tokens JWT
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/auth/register", "/auth/login").permitAll() // rutas públicas
                .anyRequest().authenticated() // rutas que requieren token válido
            )
            // Le decimos a Spring que NO use sesiones HTTP
            // Cada request debe ser autenticada, el servidor no recuerda al usuario entre requests
            .sessionManagement(session ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )
            // Registrar JwtFilter en la cadena de filtros de Spring
            // addFilterBefore significa que nuestro filtro se ejecuta ANTES
            // del filtro de autenticación por usuario/contraseña de Spring
            .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        // construir y retornar configuración
        return http.build();
    }
    
        // @Bean para el encriptador de contraseñas
        // BCrypt es el algoritmo estándar para hashear contraseñas
        // Spring lo usa automáticamente cuando guarda o verifica contraseñas
        @Bean
        public PasswordEncoder passwordEncoder() {
            return new BCryptPasswordEncoder();
        }
}
