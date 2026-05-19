package br.com.fiap.m8music.getaway.controller;

import br.com.fiap.m8music.getaway.client.dto.SpotifySearchResponseDTO;
import br.com.fiap.m8music.service.SpotifyService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/spotify")
@RequiredArgsConstructor
public class SpotifyController {

    private final SpotifyService spotifyService;

    @GetMapping("/search")
    public SpotifySearchResponseDTO search(
            @RequestParam String q,
            @RequestParam(defaultValue = "track") String type,
            @RequestParam(defaultValue = "10") Integer limit,
            @RequestParam(defaultValue = "0") Integer offset,
            @RequestParam(required = false) String market
    ) {
        return spotifyService.search(q, type, limit, offset, market);
    }
}
