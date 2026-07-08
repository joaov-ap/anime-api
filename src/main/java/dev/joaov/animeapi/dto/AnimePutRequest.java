package dev.joaov.animeapi.dto;

import dev.joaov.animeapi.model.AnimeStatus;

public record AnimePutRequest(String name, int episodes, String genre, AnimeStatus status) {
}
