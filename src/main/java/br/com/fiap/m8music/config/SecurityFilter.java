package br.com.fiap.m8music.config;

import br.com.fiap.m8music.getaway.repository.CantorRepository;
import br.com.fiap.m8music.getaway.repository.ClienteRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class SecurityFilter extends OncePerRequestFilter {
    private final TokenService tokenService;
    private final CantorRepository cantorRepository;
    private final ClienteRepository clienteRepository;

    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        var tokenJWT = recoverToken(request);
        if (tokenJWT != null) {
            var subject = tokenService.validateToken(tokenJWT);
            if (subject != null) {
                UserDetails user = cantorRepository.findByEmail(subject);
                if (user == null) {
                    try {
                        user = clienteRepository.findById(Long.parseLong(subject)).orElse(null);
                    } catch (NumberFormatException ignored) {}
                }
                if (user != null) {
                    var authentication = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }
        }
        chain.doFilter(request, response);
    }

    private String recoverToken(HttpServletRequest request){
        var authHeader = request.getHeader("Authorization");
        if(authHeader == null) return null;
        return authHeader.replace("Bearer ", "");
    }
}
