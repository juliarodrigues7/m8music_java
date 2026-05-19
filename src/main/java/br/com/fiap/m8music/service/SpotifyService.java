package br.com.fiap.m8music.service;

import br.com.fiap.m8music.exception.BusinessException;
import br.com.fiap.m8music.getaway.client.SpotifyClient;
import br.com.fiap.m8music.getaway.client.dto.SpotifySearchResponseDTO;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SpotifyService {

    private static final Logger log = LoggerFactory.getLogger(SpotifyService.class);

    private final SpotifyClient spotifyClient;

    @Retry(name = "spotify", fallbackMethod = "searchFallback")
    public SpotifySearchResponseDTO search(String q, String type, Integer limit, Integer offset, String market) {
        return spotifyClient.search(q, type, limit, offset, market);
    }

    public SpotifySearchResponseDTO searchFallback(String q, String type, Integer limit, Integer offset, String market, Exception e) {
        log.warn("Spotify indisponível após retentativas. Query='{}', erro: {}", q, e.getMessage());
        throw new BusinessException("Spotify indisponível no momento. Tente novamente em instantes.");
    }
}
