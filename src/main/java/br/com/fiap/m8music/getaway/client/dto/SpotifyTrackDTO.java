package br.com.fiap.m8music.getaway.client.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Map;

public record SpotifyTrackDTO(
        String id,
        String name,
        String href,
        String uri,
        @JsonProperty("duration_ms") Integer durationMs,
        @JsonProperty("track_number") Integer trackNumber,
        @JsonProperty("disc_number") Integer discNumber,
        Boolean explicit,
        @JsonProperty("is_local") Boolean isLocal,
        @JsonProperty("is_playable") Boolean isPlayable,
        @JsonProperty("external_urls") Map<String, String> externalUrls,
        List<SpotifyArtistSimplifiedDTO> artists,
        SpotifyAlbumSimplifiedDTO album
) {
}
