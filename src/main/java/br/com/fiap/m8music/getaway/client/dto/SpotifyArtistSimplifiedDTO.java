package br.com.fiap.m8music.getaway.client.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

public record SpotifyArtistSimplifiedDTO(
        String id,
        String name,
        String href,
        String uri,
        @JsonProperty("external_urls") Map<String, String> externalUrls
) {
}
