package ru.yandex.practicum.filmorate.storage;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.filmorate.exception.DuplicatedDataException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class InMemoryFilmStorage implements FilmStorage {
    private Long lastId = 1L;
    private final Map<Long, Film> films = new HashMap<>();

    @Override
    public Collection<Film> getFilms() {
        return Map.copyOf(films).values();
    }

    @Override
    public Film createFilm(Film film) {
        if (findFilmByName(film.getName()) != null) {
            log.error("Попытка создать существующий фильм");
            throw new DuplicatedDataException("Такой фильм уже существует");
        }

        film.setId(lastId++);
        films.put(film.getId(), film);
        return film;
    }

    @Override
    public Film updateFilm(@Valid @RequestBody Film film) {
        if (!films.containsKey(film.getId())) {
            log.error("Фильм с id {} не найден", film.getId());
            throw new NotFoundException("Фильм с id = " + film.getId() + " не найден");
        }

        films.put(film.getId(), film);
        return film;
    }

    @Override
    public Film getFilmById(Long filmId) {
        if (!films.containsKey(filmId)) {
            log.error("Фильм с id {} не найден", filmId);
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
