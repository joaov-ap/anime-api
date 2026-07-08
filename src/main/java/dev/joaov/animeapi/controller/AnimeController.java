package dev.joaov.animeapi.controller;

import dev.joaov.animeapi.dto.AnimeGetResponse;
import dev.joaov.animeapi.dto.AnimePostRequest;
import dev.joaov.animeapi.dto.AnimePutRequest;
import dev.joaov.animeapi.mapper.AnimeMapper;
import dev.joaov.animeapi.service.AnimeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/animes")
@RequiredArgsConstructor
public class AnimeController {
    private final AnimeMapper MAPPER;
    private final AnimeService animeService;

    @GetMapping
    public ResponseEntity<List<AnimeGetResponse>> findAll() {
        var animes = MAPPER.toListAnimeGetResponse(animeService.findAll());
        return ResponseEntity.ok(animes);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AnimeGetResponse> findById(@PathVariable Long id) {
        var animeFound = MAPPER.toAnimeGetResponse(animeService.findById(id));
        return ResponseEntity.ok(animeFound);
    }

    @PostMapping
    public ResponseEntity<AnimeGetResponse> save(@RequestBody AnimePostRequest postRequest) {
        var anime = MAPPER.toAnime(postRequest);
        var animeSaved = animeService.save(anime);

        var response = MAPPER.toAnimeGetResponse(animeSaved);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AnimeGetResponse> update(@PathVariable Long id, @RequestBody AnimePutRequest putRequest) {
        var anime = MAPPER.toAnime(putRequest);
        var animeUpdated = animeService.update(id, anime);

        var response = MAPPER.toAnimeGetResponse(animeUpdated);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        animeService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
