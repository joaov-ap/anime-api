package dev.joaov.animeapi.dto;

import dev.joaov.animeapi.model.AnimeStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record AnimePostRequest(
        @NotBlank(message = "The field 'name' is required")
        String name,
        @Positive(message = "The field 'episodes' need to be positive")
        int episodes,
        @NotBlank(message = "The field 'genre' is required")
        String genre,
        @NotNull(message = "The field 'animeStatus' is required")
        AnimeStatus status) {
}
