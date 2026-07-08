package dev.joaov.animeapi.model;

import lombok.*;

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
