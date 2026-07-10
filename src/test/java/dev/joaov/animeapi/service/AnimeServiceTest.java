package dev.joaov.animeapi.service;

import dev.joaov.animeapi.model.Anime;
import dev.joaov.animeapi.model.AnimeStatus;
import dev.joaov.animeapi.repository.AnimeRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class AnimeServiceTest {
    @InjectMocks
    private AnimeService service;

    @Mock
    private AnimeRepository repository;
    private List<Anime> animeList;

    @BeforeEach
    void init() {
        var hunterxhunter = new Anime(1L, "Hunter X Hunter", 143, "Shounen", AnimeStatus.COMPLETED);
        var yuyuhakusho = new Anime(2L, "Yu Yu Hakusho", 100, "Shounen", AnimeStatus.COMPLETED);
        var naruto = new Anime(3L, "Naruto", 500, "Shounen", AnimeStatus.COMPLETED);
        animeList = new ArrayList<>(List.of(hunterxhunter, yuyuhakusho, naruto));
    }

    @Test
    @DisplayName("findAll returns all animes when successful")
    @Order(1)
    void findAll_ReturnsAnime_WhenSuccessful() {
        BDDMockito.when(repository.findAll()).thenReturn(animeList);

        var animes = service.findAll();
        Assertions.assertThat(animes).isNotNull().hasSameElementsAs(animeList);
    }

    @Test
    @DisplayName("findAll returns empty list when there are no animes")
    @Order(2)
    void findAll_ReturnsEmptyList_WhenThereAreNoAnimes() {
        var animes = service.findAll();
        Assertions.assertThat(animes).isNotNull().isEmpty();
    }

    @Test
    @DisplayName("findById return an anime when exist")
    @Order(3)
    void findById_ReturnsAnAnime_WhenAnimeIsFound() {
        var expectedAnime = animeList.getFirst();
        BDDMockito.when(repository.findById(expectedAnime.getId())).thenReturn(Optional.of(expectedAnime));

        var animeFound = service.findById(expectedAnime.getId());
        Assertions.assertThat(animeFound).hasNoNullFieldsOrProperties().isEqualTo(expectedAnime);
    }

    @Test
    @DisplayName("findById throws ResponseStatusException when anime is not found")
    @Order(4)
    void findById_ThrowsResponseStatusException_WhenAnimeIsNotFound() {
        var expectedAnime = animeList.getFirst();
        BDDMockito.when(repository.findById(expectedAnime.getId())).thenReturn(Optional.empty());

        Assertions.assertThatException()
                .isThrownBy(() -> service.findById(expectedAnime.getId()))
                .isInstanceOf(ResponseStatusException.class);
    }

    @Test
    @DisplayName("save Saves an anime to the list")
    @Order(5)
    void save_SavesAnAnime_WhenSuccessful() {
        var animeToSave = new Anime(99L, "One Piece", 1082, "Shounen", AnimeStatus.WATCHING);
        BDDMockito.when(repository.save(animeToSave)).thenReturn(animeToSave);

        var animeSaved = service.save(animeToSave);
        Assertions.assertThat(animeSaved).isEqualTo(animeToSave).hasNoNullFieldsOrProperties();
        Assertions.assertThat(animeSaved.getId()).isEqualTo(animeToSave.getId());
    }

    @Test
    @DisplayName("update replaces an anime correctly")
    @Order(6)
    void update_ReplacesAnAnime_WhenSuccessful() {
        var animeToUpdate = animeList.getFirst();
        animeToUpdate.setStatus(AnimeStatus.WATCHING);

        BDDMockito.when(repository.findById(animeToUpdate.getId())).thenReturn(Optional.of(animeToUpdate));
        BDDMockito.when(repository.update(animeToUpdate, animeToUpdate)).thenReturn(animeToUpdate);

        var updatedAnime = service.update(animeToUpdate.getId(), animeToUpdate);
        Assertions.assertThat(animeList).contains(updatedAnime);
    }

    @Test
    @DisplayName("update throws ResponseStatusException when an anime is not found")
    @Order(7)
    void update_ThrowsResponseStatusException_WhenAnimeIsNotFound() {
        var animeToUpdate = animeList.getFirst();
        BDDMockito.when(repository.findById(animeToUpdate.getId())).thenReturn(Optional.empty());

        Assertions.assertThatException()
                .isThrownBy(() -> service.update(animeToUpdate.getId(), animeToUpdate))
                .isInstanceOf(ResponseStatusException.class);
    }

    @Test
    @DisplayName("delete removes an anime from the list")
    @Order(8)
    void delete_RemovesAnAnime_WhenSuccessful() {
        var animeToDelete = animeList.getFirst();
        BDDMockito.when(repository.findById(animeToDelete.getId())).thenReturn(Optional.of(animeToDelete));
        BDDMockito.doNothing().when(repository).delete(animeToDelete);

        Assertions.assertThatNoException().isThrownBy(() -> service.delete(animeToDelete.getId()));
    }

    @Test
    @DisplayName("delete throws ResponseStatusException when an anime is not found")
    @Order(9)
    void delete_ThrowsResponseStatusException_WhenAnimeIsNotFound() {
        var animeToDelete = animeList.getFirst();
        BDDMockito.when(repository.findById(animeToDelete.getId())).thenReturn(Optional.empty());

        Assertions.assertThatException()
                .isThrownBy(() -> service.delete(animeToDelete.getId()))
                .isInstanceOf(ResponseStatusException.class);
    }
}