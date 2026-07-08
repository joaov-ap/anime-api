package dev.joaov.animeapi.mapper;

import dev.joaov.animeapi.dto.AnimeGetResponse;
import dev.joaov.animeapi.dto.AnimePostRequest;
import dev.joaov.animeapi.dto.AnimePutRequest;
import dev.joaov.animeapi.model.Anime;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface AnimeMapper {
    Anime toAnime(Anime anime);
    @Mapping(target = "anime.id", ignore = true)
    Anime toAnime(AnimePostRequest animePostRequest);
    @Mapping(target = "anime.id", ignore = true)
    Anime toAnime(AnimePutRequest animePutRequest);
    AnimeGetResponse toAnimeGetResponse(Anime anime);
    List<AnimeGetResponse> toListAnimeGetResponse(List<Anime> animes);
}
