package ru.yandex.practicum.filmorate.sevice;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dto.FilmRequest;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.mapper.FilmMapper;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.repository.GenreRepository;
import ru.yandex.practicum.filmorate.repository.JdbcFilmRepository;
import ru.yandex.practicum.filmorate.repository.LikeRepository;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class FilmService {
    private final JdbcFilmRepository filmRepository;
    private final LikeRepository likeRepository;
    private final GenreRepository genreRepository;

    public List<Film> getAll() {
        return filmRepository.findAll();
    }

    public Film getById(Long filmId) {
        if (filmId == null) {
            log.error("Не передан id фильма");
            throw new ValidationException("filmId не передан");
        }

        Film film = filmRepository.findById(filmId)
                .orElseThrow(() -> {
                    log.error("Фильм с id {} не найден", filmId);
                    return new NotFoundException("Фильм с id " + filmId + " не найдег");
                });

        List<Long> filmGenreIds = filmRepository.getFilmGenres(film.getId()).stream().sorted().toList();
        Map<Long, Genre> genres = genreRepository.getAll().stream()
                .collect(Collectors.toMap(
                        Genre::getId,
                        Function.identity()
                ));
        Set<Genre> filmGenres = new HashSet<>();

        for (Long genreId: filmGenreIds) {
            filmGenres.add(genres.get(genreId));
        }

        film.setGenres(filmGenres);

        return film;
    }

    public Film create(Film film) {
        return filmRepository.create(film);
    }

    public Film update(@Valid FilmRequest request) {
        if (!request.hasId()) {
            log.error("Не передан id фильма");
            throw new ValidationException("Id должен быть указан");
        }

        Film film = filmRepository.findById(request.getId())
                .map(oldFilm -> FilmMapper.updateFilmFields(oldFilm, request))
                .orElseThrow(() -> {
                    log.error("Фильм с id {} не найден", request.getId());
                    return new NotFoundException("Фильм с id " + request.getId() + " не найден");
                });

        return filmRepository.update(film);
    }

    public List<Film> getPopularFilms(int count) {
        if (count <= 0) {
            log.warn("Параметр count меньше или равен 0");
            throw new ValidationException("Параметр count должен быть положительным числом");
        }

        List<Long> popularFilmIds = likeRepository.getPopularFilmIds(count);
        List<Film> films = filmRepository.findFilmsByIds(popularFilmIds);
        Map<Long, Film> filmMap = films.stream()
                .collect(Collectors.toMap(Film::getId, Function.identity()));

        return popularFilmIds.stream()
                .map(filmMap::get)
                .filter((Objects::nonNull))
                .toList();
    }
}
