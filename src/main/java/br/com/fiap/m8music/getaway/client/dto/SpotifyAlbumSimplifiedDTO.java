package br.com.fiap.m8music.getaway.client.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Map;

public record SpotifyAlbumSimplifiedDTO(
        String id,
        String name,
        @JsonProperty("album_type") String albumType,
        @JsonProperty("total_tracks") Integer totalTracks,
        @JsonProperty("release_date") String releaseDate,
        @JsonProperty("release_date_precision") String releaseDatePrecision,
        String href,
        String uri,
        @JsonProperty("external_urls") Map<String, String> externalUrls,
        List<SpotifyImageDTO> images,
        List<SpotifyArtistSimplifiedDTO> artists
) {
}
