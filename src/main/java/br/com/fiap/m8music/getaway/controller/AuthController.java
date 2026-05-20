package br.com.fiap.m8music.getaway.controller;

import br.com.fiap.m8music.getaway.dto.auth.LoginRequestDTO;
import br.com.fiap.m8music.getaway.dto.auth.LoginResponseDTO;
import br.com.fiap.m8music.getaway.dto.cantor.CantorCreateDTO;
import br.com.fiap.m8music.getaway.dto.cantor.CantorDTO;
import br.com.fiap.m8music.getaway.dto.cliente.ClienteCreateDTO;
import br.com.fiap.m8music.service.AuthService;
import br.com.fiap.m8music.service.CantorService;
import br.com.fiap.m8music.service.ClienteService;
import br.com.fiap.m8music.config.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final CantorService cantorService;
    private final ClienteService clienteService;
    private final AuthenticationConfiguration authenticationConfiguration;
    private final TokenService tokenService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginRequestDTO body) throws Exception {
        LoginResponseDTO response = authService.login(body);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/register-cantor")
    public ResponseEntity<CantorDTO> register(@RequestBody CantorCreateDTO body) {
        CantorDTO cantor = cantorService.create(body);

        return ResponseEntity.status(HttpStatus.CREATED).body(cantor);
    }

    @PostMapping("/register-cliente")
    public ResponseEntity<LoginResponseDTO> registerGuest(@RequestBody ClienteCreateDTO body) {
        LoginResponseDTO response = clienteService.createGuest(body);
        return ResponseEntity.ok(response);
    }


    @GetMapping("/token")
    public ResponseEntity<String> getToken(Authentication authentication) throws Exception {
        authService.getToken(authentication);
        return ResponseEntity.ok("Hello World");
    }
}
