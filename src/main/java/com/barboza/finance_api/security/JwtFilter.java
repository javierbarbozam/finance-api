package com.barboza.finance_api.security;

import java.io.IOException;
import java.util.List;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtFilter extends OncePerRequestFilter {

    final JwtUtils jwtUtils;

    JwtFilter(JwtUtils jwtUtils) {
        this.jwtUtils = jwtUtils;
    }

    @Override
    // Metodo principal usado por spring para cualquier request
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // Obtener el header "Authorization" de la request. Si usuario no mandó token, este valor será null
        String authHeader = request.getHeader("Authorization");
        
        // Si no hay header o no empieza con "Bearer", pasa request sin autenticar
        // Esto permite que rutas públicas funcionen sin token
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            // filterChain.doFilter pasa request al siguiente filtro
            filterChain.doFilter(request, response);
            return;
        }

        // Extraer prefijo "Bearer xxxxx.yyyyy.zzzzz" para obtener token "xxxxx.yyyyy.zzzzz"
        String token = authHeader.substring(7);

        // Extraer email del token
        String email = jwtUtils.extractEmail(token);

        // Verifica:
        // 1. Que el email no sea null
        // 2. Que no haya ya una autenticación activa en el contexto de seguridad
        //    (para no sobreescribir una autenticación que ya existe)
        if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            
            if(jwtUtils.isTokenValid(token)) {
                // Creamos el objeto de autenticación que Spring Security entiende
                // Parámetros: (identidad, credenciales, permisos)
                // - email es la identidad del usuario
                // - null porque no necesita contraseña (el token es suficiente)
                // - Lista vacía porque por ahora no hay roles/permisos
                UsernamePasswordAuthenticationToken authToken =
                    new UsernamePasswordAuthenticationToken(email, null, List.of());

                // Agregamos detalles de request al objeto de autenticación
                // (como la IP del cliente, útil para logs y auditoría)
                authToken.setDetails(
                    new WebAuthenticationDetailsSource().buildDetails(request)
                );

                // Guardar autenticación en el SecurityContext
                // A partir de aquí Spring sabe quién es el usuario en esta request
                SecurityContextHolder.getContext().setAuthentication(authToken);
 
            }
        }

        // Pasar request al siguiente filtro. Se ejecuta siempre, con o sin token válido
        filterChain.doFilter(request, response);
    }   
}
