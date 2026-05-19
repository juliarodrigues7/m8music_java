package br.com.fiap.m8music.getaway.client.dto;

import java.util.List;

public record SpotifyTrackPageDTO(
        String href,
        Integer limit,
        String next,
        Integer offset,
        String previous,
        Integer total,
        List<SpotifyTrackDTO> items
) {
}
