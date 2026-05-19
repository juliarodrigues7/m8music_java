package br.com.fiap.m8music.getaway.client;

import br.com.fiap.m8music.config.SpotifyFeignConfig;
import br.com.fiap.m8music.getaway.client.dto.SpotifySearchResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "spotify", url = "https://api.spotify.com/v1", configuration = SpotifyFeignConfig.class)
public interface SpotifyClient {

    @GetMapping("/search")
    SpotifySearchResponseDTO search(
            @RequestParam("q") String query,
            @RequestParam("type") String type,
            @RequestParam("limit") Integer limit,
            @RequestParam("offset") Integer offset,
            @RequestParam(value = "market", required = false) String market
    );
}
