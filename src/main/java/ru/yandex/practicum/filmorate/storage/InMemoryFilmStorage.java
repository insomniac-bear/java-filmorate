package ru.yandex.practicum.filmorate.storage;

import jakarta.validation.Valid;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.filmorate.exception.DuplicatedDataException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.HashMap;
import java.util.Map;

@Component
public class InMemoryFilmStorage implements FilmStorage {
    private Long lastId = 1L;
    private final Map<Long, Film> films = new HashMap<>();

    @Override
    public Map<Long, Film> getFilms() {
        return films;
    }

    @Override
    public Film createFilm(Film film) {
        if (findFilmByName(film.getName()) != null) {
            throw new DuplicatedDataException("Такой фильм уже существует");
        }

        film.setId(lastId++);
        films.put(film.getId(), film);
        return film;
    }

    @Override
    public Film updateFilm(@Valid @RequestBody Film film) {
        if (film.getId() == null) {
            throw new ValidationException("Id должен быть указан");
        }

        if (!films.containsKey(film.getId())) {
            throw new NotFoundException("Фильм с id = " + film.getId() + " не найден");
        }

        films.put(film.getId(), film);
        return film;
    }

    @Override
    public Film getFilmById(Long filmId) {
        if (filmId == null) {
            throw new ValidationException("filmId не передан");
        }

        if (!films.containsKey(filmId)) {
            throw new NotFoundException("Фильм с id " + filmId + " не найден");
        }

        return films.get(filmId);
    }

    private Film findFilmByName(String name) {
        return films.values()
                .stream()
                .filter(existFilm -> name.equals(existFilm.getName()))
                .findFirst()
                .orElse(null);
    }
}
