package br.com.fiap.m8music.config;

import br.com.fiap.m8music.service.SpotifyTokenService;
import feign.RequestInterceptor;
import org.springframework.context.annotation.Bean;

public class SpotifyFeignConfig {

    @Bean
    public RequestInterceptor spotifyAuthInterceptor(SpotifyTokenService tokenService) {
        return template -> template.header("Authorization", "Bearer " + tokenService.getAccessToken());
    }
}
