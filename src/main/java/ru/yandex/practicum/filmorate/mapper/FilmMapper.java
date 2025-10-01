package ru.yandex.practicum.filmorate.mapper;

import lombok.NoArgsConstructor;
import ru.yandex.practicum.filmorate.dto.FilmRequest;
import ru.yandex.practicum.filmorate.model.Film;

@NoArgsConstructor
public class FilmMapper {
    public static Film updateFilmFields(Film film, FilmRequest request) {
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

        if (request.hasGenres()) {
            film.setGenres(request.getGenres());
        }

        if (request.hasMpa()) {
            film.setMpa(request.getMpa());
        }

        return film;
    }
}
