package br.com.fiap.m8music.getaway.client;

import br.com.fiap.m8music.getaway.client.dto.TokenResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "spotify-token", url = "https://accounts.spotify.com")
public interface TokenSpotifyClient {

    @PostMapping(value = "/api/token", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    TokenResponseDTO getToken(
            @RequestHeader("Authorization") String basicAuth,
            @RequestBody MultiValueMap<String, String> formParams
    );
}
