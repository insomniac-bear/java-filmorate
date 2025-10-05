package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.dto.CreateFilmRequest;
import ru.yandex.practicum.filmorate.dto.UpdateFilmRequest;
import ru.yandex.practicum.filmorate.dto.FilmResponse;
import ru.yandex.practicum.filmorate.sevice.FilmService;

import java.util.Collection;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/films")
public class FilmController {
    private final FilmService filmService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Collection<FilmResponse> getFilms() {
        log.info("GET /films - получение всех фильмов");
        return filmService.getAll();
    }

    @GetMapping("/{filmId}")
    @ResponseStatus(HttpStatus.OK)
    public FilmResponse getFilmById(@PathVariable Long filmId) {
        log.info("GET /films/{} - получение фильма", filmId);
        return filmService.getById(filmId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public FilmResponse createFilm(@Valid @RequestBody CreateFilmRequest film) {
        log.info("POST /films - создание фильма {}", film);
        return filmService.create(film);
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public FilmResponse updateFilm(@RequestBody UpdateFilmRequest request) {
        log.info("PUT /films - обновление фильма {}", request);
        return filmService.update(request);
    }

    @GetMapping("/popular")
    @ResponseStatus(HttpStatus.OK)
    public Collection<FilmResponse> getPopular(@RequestParam(defaultValue = "10") int count) {
        log.info("GET /films/popular - получение популярных фильмов");
        return filmService.getPopularFilms(count);
    }
}
