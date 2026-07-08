package dev.joaov.animeapi.dto;

import dev.joaov.animeapi.model.AnimeStatus;

public record AnimeGetResponse(Long id, String name, int episodes, String genre, AnimeStatus status) {
}
