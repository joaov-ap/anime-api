package dev.joaov.animeapi.controller;

import dev.joaov.animeapi.exception.NotFoundException;
import dev.joaov.animeapi.mapper.AnimeMapperImpl;
import dev.joaov.animeapi.model.Anime;
import dev.joaov.animeapi.model.AnimeStatus;
import dev.joaov.animeapi.service.AnimeService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

@WebMvcTest(controllers = AnimeController.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Import(AnimeMapperImpl.class)
class AnimeControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockitoBean
    private AnimeService service;
    private List<Anime> animeList;
    @Autowired
    private ResourceLoader resourceLoader;
    private final String URL = "/v1/animes";

    @BeforeEach
    void init() {
        var hunterxhunter = new Anime(1L, "Hunter X Hunter", 143, "Shounen", AnimeStatus.COMPLETED);
        var yuyuhakusho = new Anime(2L, "Yu Yu Hakusho", 100, "Shounen", AnimeStatus.COMPLETED);
        var naruto = new Anime(3L, "Naruto", 500, "Shounen", AnimeStatus.COMPLETED);
        animeList = new ArrayList<>(List.of(hunterxhunter, yuyuhakusho, naruto));
    }

    @Test
    @DisplayName("GET v1/animes returns all animes when successful")
    @Order(1)
    void findAll_ReturnsAnimeList_WhenSuccessful() throws Exception {
        BDDMockito.when(service.findAll()).thenReturn(animeList);
        var response = readResourceFile("get-anime-findall-200.json");

        mockMvc.perform(MockMvcRequestBuilders.get(URL))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }

    @Test
    @DisplayName("GET v1/animes/1 return an anime when exist")
    @Order(2)
    void findById_ReturnsAnAnime_WhenAnimeIsFound() throws Exception {
        var id = 1L;
        BDDMockito.when(service.findById(id)).thenReturn(animeList.getFirst());
        var response = readResourceFile("get-anime-by-id-200.json");

        mockMvc.perform(MockMvcRequestBuilders.get(URL + "/{id}", id))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }

    @Test
    @DisplayName("GET v1/animes/99 throws NotFound when anime is not found")
    @Order(3)
    void findById_ThrowsNotFound_WhenAnimeIsNotFound() throws Exception {
        var id = 99L;
        BDDMockito.when(service.findById(id)).thenThrow(new NotFoundException("Anime with id {%d} not found".formatted(id)));

        mockMvc.perform(MockMvcRequestBuilders.get(URL + "/{id}", id))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    @DisplayName("POST v1/animes Saves an anime to the list")
    @Order(4)
    void save_SavesAnAnime_WhenSuccessful() throws Exception {
        var request = readResourceFile("post-request-anime-200.json");
        var response = readResourceFile("post-response-anime-201.json");
        var animeToSave = new Anime(99L, "One Piece", 1085, "Shounen", AnimeStatus.DROPPED);
        BDDMockito.when(service.save(ArgumentMatchers.any())).thenReturn(animeToSave);

        mockMvc.perform(MockMvcRequestBuilders
                        .post(URL)
                        .content(request)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }

    @Test
    @DisplayName("PUT v1/animes/1 replaces an anime correctly")
    @Order(5)
    void update_ReplacesAnAnime_WhenSuccessful() throws Exception {
        var request = readResourceFile("put-request-anime-200.json");
        var response = readResourceFile("put-response-anime-200.json");
        var animeToUpdate = new Anime(1L, "Hunter X Hunter", 158, "Shounen", AnimeStatus.COMPLETED);
        var id = animeToUpdate.getId();
        BDDMockito.when(service.findById(id)).thenReturn(animeToUpdate);
        BDDMockito.when(service.update(ArgumentMatchers.any(), ArgumentMatchers.any())).thenReturn(animeToUpdate);

        mockMvc.perform(MockMvcRequestBuilders
                        .put(URL + "/{id}", id)
                        .content(request)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }

    @Test
    @DisplayName("DELETE v1/animes/1 removes an anime from the list")
    @Order(6)
    void delete_RemovesAnAnime_WhenSuccessful() throws Exception {
        var id = animeList.getFirst().getId();
        BDDMockito.when(service.findById(id)).thenReturn(animeList.getFirst());

        mockMvc.perform(MockMvcRequestBuilders.delete(URL + "/{id}", id))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @ParameterizedTest
    @MethodSource("postAnimeBadRequestSource")
    @DisplayName("POST v1/animes returns bad request when fields are empty")
    @Order(7)
    void save_ReturnsBadRequest_WhenFieldsAreEmpty(String fileName, List<String> errors) throws Exception {
        var request = readResourceFile("%s".formatted(fileName));

        var mvcResult = mockMvc.perform(MockMvcRequestBuilders
                        .post(URL)
                        .content(request)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andReturn();

        var resolvedException = mvcResult.getResolvedException();

        Assertions.assertThat(resolvedException).isNotNull();
        Assertions.assertThat(resolvedException.getMessage()).contains(errors);
    }

    private static Stream<Arguments> postAnimeBadRequestSource(){
        var nameError = "The field 'name' is required";
        var positiveEpisodeError = "The field 'episodes' need to be positive";
        var genreError = "The field 'genre' is required";
        var animeStatusError = "The field 'animeStatus' is required";

        var allErrors = List.of(nameError, positiveEpisodeError, genreError, animeStatusError);

        return Stream.of(
                Arguments.of("post-request-anime-empty-fields-400.json", allErrors),
                Arguments.of("post-request-anime-blank-fields-400.json", allErrors)
        );
    }

    private String readResourceFile(String fileName) throws IOException {
        var file = resourceLoader.getResource("classpath:%s".formatted(fileName)).getFile();

        return new String(Files.readAllBytes(file.toPath()));
    }
}