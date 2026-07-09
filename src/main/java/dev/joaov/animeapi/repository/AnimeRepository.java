package dev.joaov.animeapi.repository;

import dev.joaov.animeapi.model.Anime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class AnimeRepository {
    private Long id = 1L;
    private final List<Anime> animeList = new ArrayList<>();

    public List<Anime> findAll() {
        return animeList;
    }

    public Optional<Anime> findById(Long id) {
        return animeList.stream().filter(a -> a.getId().equals(id)).findFirst();
    }

    public Anime save(Anime anime) {
        anime.setId(this.id);
        animeList.add(anime);

        this.id += 1;
        return anime;
    }

    public Anime update(Anime anime, Anime animeToUpdate) {
        animeList.set(animeList.indexOf(anime), animeToUpdate);
        return animeToUpdate;
    }

    public void delete(Anime animeToDelete) {
        animeList.remove(animeToDelete);
    }
}
