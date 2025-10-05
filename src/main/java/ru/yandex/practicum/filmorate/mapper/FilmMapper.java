package ru.yandex.practicum.filmorate.mapper;

import lombok.NoArgsConstructor;
import ru.yandex.practicum.filmorate.dto.CreateFilmRequest;
import ru.yandex.practicum.filmorate.dto.UpdateFilmRequest;
import ru.yandex.practicum.filmorate.dto.FilmResponse;
import ru.yandex.practicum.filmorate.model.Film;

@NoArgsConstructor
public class FilmMapper {
    public static FilmResponse filmToFilmResponse(Film film) {
        return FilmResponse.builder()
                .id(film.getId())
                .name(film.getName())
                .description(film.getDescription())
                .duration(film.getDuration())
                .releaseDate(film.getReleaseDate())
                .build();
    }

    public static Film createFilmRequestToFilm(CreateFilmRequest filmRequest) {
        return Film.builder()
                .name(filmRequest.getName())
                .description(filmRequest.getDescription())
                .releaseDate(filmRequest.getReleaseDate())
                .duration(filmRequest.getDuration())
                .build();
    }

    public static FilmResponse updateFilmFields(FilmResponse film, UpdateFilmRequest request) {
        if (request.hasName()) {
            film.setName(request.getName());
        }

        if (request.hasDescription()) {
            film.setDescription(request.getDescription());
        }

        if (request.hasReleaseDate()) {
            film.setReleaseDate(request.getReleaseDate());
        }

        if (request.hasDuration()) {
            film.setDuration(request.getDuration());
        }

        return film;
    }
}
