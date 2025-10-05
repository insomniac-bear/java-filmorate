package ru.yandex.practicum.filmorate.sevice;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dto.CreateFilmRequest;
import ru.yandex.practicum.filmorate.dto.UpdateFilmRequest;
import ru.yandex.practicum.filmorate.dto.FilmResponse;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.mapper.FilmMapper;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.repository.JdbcFilmRepository;
import ru.yandex.practicum.filmorate.repository.JdbcGenreRepository;
import ru.yandex.practicum.filmorate.repository.JdbcLikeRepository;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class FilmService {
    private final JdbcFilmRepository filmRepository;
    private final JdbcLikeRepository likeRepository;
    private final JdbcGenreRepository genreRepository;

    public List<FilmResponse> getAll() {
        return filmRepository.findAll();
    }

    public FilmResponse getById(Long filmId) {
        if (filmId == null) {
            log.error("Не передан id фильма");
            throw new ValidationException("filmId не передан");
        }

        FilmResponse film = filmRepository.findById(filmId)
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

    public FilmResponse create(CreateFilmRequest newFilm) {
        return filmRepository.create(newFilm);
    }

    public FilmResponse update(@Valid UpdateFilmRequest request) {
        if (!request.hasId()) {
            log.error("Не передан id фильма");
            throw new ValidationException("Id должен быть указан");
        }

        FilmResponse film = filmRepository.findById(request.getId())
                .map(oldFilm -> FilmMapper.updateFilmFields(oldFilm, request))
                .orElseThrow(() -> {
                    log.error("Фильм с id {} не найден", request.getId());
                    return new NotFoundException("Фильм с id " + request.getId() + " не найден");
                });
        return filmRepository.update(film);
    }

    public List<FilmResponse> getPopularFilms(int count) {
        if (count <= 0) {
            log.warn("Параметр count меньше или равен 0");
            throw new ValidationException("Параметр count должен быть положительным числом");
        }

        List<Long> popularFilmIds = likeRepository.getPopularFilmIds(count);
        List<FilmResponse> films = filmRepository.findFilmsByIds(popularFilmIds);
        Map<Long, FilmResponse> filmMap = films.stream()
                .collect(Collectors.toMap(FilmResponse::getId, Function.identity()));

        return popularFilmIds.stream()
                .map(filmMap::get)
                .filter((Objects::nonNull))
                .toList();
    }
}
