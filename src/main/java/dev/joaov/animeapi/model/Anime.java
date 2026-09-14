package dev.joaov.animeapi.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class Anime {
    private Long id;
    private String name;
    private int episodes;
    private String genre;
    private AnimeStatus status;
}
