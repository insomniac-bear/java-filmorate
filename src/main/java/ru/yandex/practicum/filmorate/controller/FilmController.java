package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.DuplicatedDataException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {
    private final LocalDate minDateRelease = LocalDate.of(1895, 12, 28);

    private final Map<Integer, Film> films = new HashMap<>();

    @GetMapping
    public Collection<Film> getFilms() {
        return films.values();
    }

    @PostMapping
    public Film createFilm(@RequestBody Film film) {
        validateFilm(film);

        if (findFilmByName(film.getName()) != null) {
            throw new DuplicatedDataException("Такой фильм уже существует");
        }

        film.setId(getNextId());
        films.put(film.getId(), film);
        log.info("Добавлен фильм {}", film.getName());
        return film;
    }

    @PutMapping
    public Film updateFilm(@RequestBody Film film) {
        if (film.getId() == null) {
            log.warn("Не указан id фильма");
            throw new ValidationException("Id должен быть указан");
        }

        if (films.containsKey(film.getId())) {
            Film oldFilm = films.get(film.getId());

            if (film.getName() != null) {
                oldFilm.setName(film.getName());
            }

            if (film.getDescription() != null) {
                oldFilm.setDescription(film.getDescription());
            }

            if (film.getReleaseDate() != null) {
                oldFilm.setReleaseDate(film.getReleaseDate());
            }

            if (film.getDuration() != null) {
                oldFilm.setDuration(film.getDuration());
            }

            validateFilm(oldFilm);
            log.info("Обновлен фильм {}", oldFilm.getName());
            return oldFilm;
        }

        throw new NotFoundException("Фильм с id = " + film.getId() + " не найден");
    }

    private void validateFilm(Film film) {
        if (film.getDescription() == null || film.getDescription().isEmpty()) {
            log.warn("Описание фильма не заполнено");
            throw new ValidationException("Описание фильма не заполнено");
        }

        if (film.getReleaseDate() == null) {
            log.warn("Дата релиза не заполнена");
            throw new ValidationException("Дата релиза не заполнена");
        }

        if (film.getDuration() == null) {
            log.warn("Продолжительность фильма не заполнена");
            throw new ValidationException("Продолжительность фильма не заполнена");
        }

        if (film.getName() == null || film.getName().isEmpty()) {
            log.warn("Название фильма не заполнено");
            throw new ValidationException("Название фильма не может быть пустым");
        }

        if (film.getDescription().length() > 200) {
            log.warn("Превышена длинна описания");
            throw new ValidationException("Максимальная длинна описания 200 символов");
        }

        if (film.getReleaseDate().isBefore(minDateRelease)) {
            log.warn("Дата релиза меньше 28 декабря 1895");
            throw new ValidationException("Дата релиза не может быть ранее, чем 28 декабря 1895");
        }

        if (film.getDuration() < 0) {
            log.warn("Продолжительность фильма меньше 0");
            throw new ValidationException("Продолжительность фильма не может быть отрицательной");
        }
    }

    private Film findFilmByName(String name) {
        return films.values()
                .stream()
                .filter(existFilm -> name.equals(existFilm.getName()))
                .findFirst()
                .orElse(null);
    }

    private int getNextId() {
        int currentMaxId = films.keySet()
                .stream()
                .mapToInt(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }
}
