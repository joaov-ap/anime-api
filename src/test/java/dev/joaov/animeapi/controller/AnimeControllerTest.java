package dev.joaov.animeapi.controller;

import dev.joaov.animeapi.mapper.AnimeMapperImpl;
import dev.joaov.animeapi.model.Anime;
import dev.joaov.animeapi.model.AnimeStatus;
import dev.joaov.animeapi.repository.AnimeRepository;
import dev.joaov.animeapi.service.AnimeService;
import org.junit.jupiter.api.*;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

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

        mockMvc.perform(MockMvcRequestBuilders.get("/v1/animes"))
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

        mockMvc.perform(MockMvcRequestBuilders.get("/v1/animes/{id}", id))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }

    @Test
    @DisplayName("GET v1/animes/99 throws ResponseStatusException when anime is not found")
    @Order(3)
    void findById_ThrowsResponseStatusException_WhenAnimeIsNotFound() throws Exception {
        var id = 99L;
        BDDMockito.when(service.findById(id)).thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "Anime with id {%d} not found".formatted(id)));

        mockMvc.perform(MockMvcRequestBuilders.get("/v1/animes/{id}", id))
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
                        .post("/v1/animes")
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
                        .put("/v1/animes/{id}", id)
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

        mockMvc.perform(MockMvcRequestBuilders.delete("/v1/animes/{id}", id))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    private String readResourceFile(String fileName) throws IOException {
        var file = resourceLoader.getResource("classpath:%s".formatted(fileName)).getFile();

        return new String(Files.readAllBytes(file.toPath()));
    }
}