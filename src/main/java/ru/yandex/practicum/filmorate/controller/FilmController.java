package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.DuplicatedDataException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {
    private int lastId = 1;
    private final Map<Integer, Film> films = new HashMap<>();

    @GetMapping
    public Collection<Film> getFilms() {
        return films.values();
    }

    @PostMapping
    public Film createFilm(@Valid @RequestBody Film film) {
        if (findFilmByName(film.getName()) != null) {
            throw new DuplicatedDataException("Такой фильм уже существует");
        }

        film.setId(lastId++);
        films.put(film.getId(), film);
        log.info("Добавлен фильм {}", film.getName());
        return film;
    }

    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film film) {
        if (film.getId() == null) {
            log.warn("Не указан id фильма");
            throw new ValidationException("Id должен быть указан");
        }

        if (!films.containsKey(film.getId())) {
            throw new NotFoundException("Фильм с id = " + film.getId() + " не найден");
        }

        films.put(film.getId(), film);
        return film;
    }

    private Film findFilmByName(String name) {
        return films.values()
                .stream()
                .filter(existFilm -> name.equals(existFilm.getName()))
                .findFirst()
                .orElse(null);
    }
}
