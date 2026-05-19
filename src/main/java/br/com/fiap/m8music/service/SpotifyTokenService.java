package br.com.fiap.m8music.service;

import br.com.fiap.m8music.getaway.client.TokenSpotifyClient;
import br.com.fiap.m8music.getaway.client.dto.TokenResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.time.Instant;
import java.util.Base64;

@Service
@RequiredArgsConstructor
public class SpotifyTokenService {

    private final TokenSpotifyClient tokenClient;

    @Value("${spotify.client-id}")
    private String clientId;

    @Value("${spotify.client-secret}")
    private String clientSecret;

    private String cachedToken;
    private Instant tokenExpiry = Instant.MIN;

    public synchronized String getAccessToken() {
        if (cachedToken == null || Instant.now().isAfter(tokenExpiry)) {
            refresh();
        }
        return cachedToken;
    }

    private void refresh() {
        String credentials = Base64.getEncoder()
                .encodeToString((clientId + ":" + clientSecret).getBytes());

        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
        form.add("grant_type", "client_credentials");

        TokenResponseDTO response = tokenClient.getToken("Basic " + credentials, form);
        cachedToken = response.accessToken();
        // 60s de margem antes de expirar
        tokenExpiry = Instant.now().plusSeconds(response.expiresIn() - 60);
    }
}
