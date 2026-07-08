package dev.joaov.animeapi.controller;

import dev.joaov.animeapi.model.Anime;
import dev.joaov.animeapi.service.AnimeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/animes")
@RequiredArgsConstructor
public class AnimeController {
    private final AnimeService animeService;

    @GetMapping
    public ResponseEntity<List<Anime>> findAll() {
        var animes = animeService.findAll();
        return ResponseEntity.ok(animes);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Anime> findById(@PathVariable Long id) {
        Anime animeFound = animeService.findById(id);
        return ResponseEntity.ok(animeFound);
    }

    @PostMapping
    public ResponseEntity<Anime> save(@RequestBody Anime anime) {
        Anime animeCreated = animeService.save(anime);
        return ResponseEntity.status(HttpStatus.CREATED).body(animeCreated);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Anime> update(@PathVariable Long id, @RequestBody Anime anime) {
        Anime animeUpdated = animeService.update(id, anime);
        return ResponseEntity.ok(animeUpdated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        animeService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
