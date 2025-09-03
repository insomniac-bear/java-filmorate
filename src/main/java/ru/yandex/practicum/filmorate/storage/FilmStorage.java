package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Map;

public interface FilmStorage {
    Map<Long, Film> getFilms();

    Film createFilm(Film film);

    Film updateFilm(Film film);

    Film getFilmById(Long filmId);
}
