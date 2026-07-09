package dev.joaov.animeapi.mapper;

import dev.joaov.animeapi.dto.AnimeGetResponse;
import dev.joaov.animeapi.dto.AnimePostRequest;
import dev.joaov.animeapi.dto.AnimePutRequest;
import dev.joaov.animeapi.model.Anime;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface AnimeMapper {
    Anime toAnime(AnimePostRequest animePostRequest);
    Anime toAnime(AnimePutRequest animePutRequest);
    AnimeGetResponse toAnimeGetResponse(Anime anime);
    List<AnimeGetResponse> toListAnimeGetResponse(List<Anime> animes);
}
