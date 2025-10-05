package ru.yandex.practicum.filmorate.sevice;

import jakarta.validation.Valid;
import ru.yandex.practicum.filmorate.dto.CreateFilmRequest;
import ru.yandex.practicum.filmorate.dto.FilmResponse;
import ru.yandex.practicum.filmorate.dto.UpdateFilmRequest;

import java.util.List;

public interface FilmServiceInterface {
    List<FilmResponse> getAll();

    FilmResponse getById(Long filmId);

    FilmResponse create(CreateFilmRequest newFilm);

    FilmResponse update(@Valid UpdateFilmRequest request);

    List<FilmResponse> getPopularFilms(int count);
}
