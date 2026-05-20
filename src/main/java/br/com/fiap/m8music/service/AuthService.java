package br.com.fiap.m8music.service;

import br.com.fiap.m8music.config.TokenService;
import br.com.fiap.m8music.domain.Cantor;
import br.com.fiap.m8music.domain.Cliente;
import br.com.fiap.m8music.getaway.dto.auth.LoginRequestDTO;
import br.com.fiap.m8music.getaway.dto.auth.LoginResponseDTO;
import br.com.fiap.m8music.getaway.repository.CantorRepository;
import br.com.fiap.m8music.getaway.repository.ClienteRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService implements UserDetailsService {

    private final AuthenticationConfiguration authenticationConfiguration;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;
    private final CantorRepository cantorRepository;
    private final ClienteRepository clienteRepository;

    @Override
    public UserDetails loadUserByUsername(String identifier) throws UsernameNotFoundException {
        UserDetails cantor = cantorRepository.findByEmail(identifier);
        if (cantor != null) return cantor;

        try {
            Long clienteId = Long.parseLong(identifier);
            return clienteRepository.findById(clienteId)
                    .orElseThrow(() -> new UsernameNotFoundException("Não encontrado: " + identifier));
        } catch (NumberFormatException e) {
            throw new UsernameNotFoundException("Não encontrado: " + identifier);
        }
    }

    public LoginResponseDTO login(@Valid LoginRequestDTO body) throws Exception {
        var authenticationManager = authenticationConfiguration.getAuthenticationManager();
        var authToken = new UsernamePasswordAuthenticationToken(
                body.email(),
                body.senha()
        );
        var auth = authenticationManager.authenticate(authToken);
        var cantor = (Cantor) auth.getPrincipal();
        var token = tokenService.generateToken(cantor);
        return LoginResponseDTO.builder()
                .token(token)
                .build();
    }

    public LoginResponseDTO createGuestSession(String nome) {
        var cliente = Cliente.builder()
                .nome(nome)
                .build();

        var savedClient = clienteRepository.save(cliente);
        var token = tokenService.generateGuestToken(savedClient);

        return LoginResponseDTO.builder().token(token).build();
    }

    public String getToken(Authentication authentication) {
        return tokenService.generateToken(authentication);
    }
}