package dev.joaov.animeapi.service;

import dev.joaov.animeapi.mapper.AnimeMapper;
import dev.joaov.animeapi.model.Anime;
import dev.joaov.animeapi.repository.AnimeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AnimeService {
    private final AnimeRepository animeRepository;
    private final AnimeMapper MAPPER;

    public List<Anime> findAll() {
        return animeRepository.findAll();
    }

    public Anime findById(Long id) {
        return animeRepository.findById(id).orElseThrow(() -> new RuntimeException("Anime with id {%d} not found".formatted(id)));
    }

    public Anime save(Anime anime) {
        return animeRepository.save(anime);
    }

    public Anime update(Long id, Anime anime) {
        var animeFound = findById(id);

        var animeToUpdate = MAPPER.toAnime(anime);
        animeToUpdate.setId(id);
        return animeRepository.update(animeFound, animeToUpdate);
    }

    public void delete(Long id) {
        var animeToDelete = findById(id);
        animeRepository.delete(animeToDelete);
    }
}
