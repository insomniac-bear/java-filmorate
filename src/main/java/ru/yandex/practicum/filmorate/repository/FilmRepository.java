package ru.yandex.practicum.filmorate.repository;

import ru.yandex.practicum.filmorate.dto.FilmRequest;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;
import java.util.Optional;

public interface FilmRepository {
    List<Film> findAll();

    List<Film> findFilmsByIds(List<Long> filmIds);

    Film create(Film film);

    Film update(Film film);

    List<Long> getFilmGenres(Long filmId);

    Optional<Film> findById(Long filmId);
}
