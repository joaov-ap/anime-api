package dev.joaov.animeapi.repository;

import dev.joaov.animeapi.model.Anime;
import dev.joaov.animeapi.model.AnimeStatus;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

class AnimeRepositoryTest {
    private AnimeRepository repository;
    private List<Anime> animeList;
    private Anime hunterxhunter;

    @BeforeEach()
    void init() {
        repository = new AnimeRepository();
        animeList = repository.findAll();
        hunterxhunter = new Anime(null, "Hunter X Hunter", 143, "Shounen", AnimeStatus.COMPLETED);
    }

    @Test
    @DisplayName("findAll return empty list when there are no animes")
    void findAll_ReturnsEmptyList_WhenThereAreNoAnimes() {
        Assertions.assertThat(animeList).isNotNull().isEmpty();
    }

    @Test
    @DisplayName("findById find an existent anime")
    void findById_ReturnsAnAnime_WhenAnimeIsFound() {
        Anime animeSaved = repository.save(hunterxhunter);
        var animeFound = repository.findById(animeSaved.getId());

        Assertions.assertThat(animeFound).isPresent().contains(animeSaved);
    }

    @Test
    @DisplayName("findById return Optional.empty() when anime doesnt exist")
    void findById_ReturnsEmpty_WhenAnimeIsNotFound() {
        var id = 2L;
        var animeFound = repository.findById(id);

        Assertions.assertThat(animeFound).isEqualTo(Optional.empty());
    }

    @Test
    void save_SavesAnAnime_WhenSuccessful() {
        Anime animeSaved = repository.save(hunterxhunter);

        Assertions.assertThat(animeList).isNotNull().hasSize(1);
        Assertions.assertThat(animeSaved).isIn(animeList);
        Assertions.assertThat(animeSaved.getId()).isNotNull();
    }

    @Test
    @DisplayName("update replaces an anime correctly")
    void update_ReplacesAnAnime_WhenSuccessful() {
        var animeSaved = repository.save(hunterxhunter);

        var animeToUpdate = new Anime(null, "Hunter X Hunter", 143, "Shounen", AnimeStatus.WATCHING);
        var animeUpdated = repository.update(animeSaved, animeToUpdate);

        Assertions.assertThat(animeList).hasSize(1);
        Assertions.assertThat(animeUpdated).isIn(animeList);
    }

    @Test
    @DisplayName("delete removes an anime from the list")
    void delete_RemovesAnAnime_WhenSuccessful() {
        var animeSaved = repository.save(hunterxhunter);
        repository.delete(animeSaved);

        Assertions.assertThat(animeList).hasSize(0);
        Assertions.assertThat(animeSaved).isNotIn(animeList);
    }
}