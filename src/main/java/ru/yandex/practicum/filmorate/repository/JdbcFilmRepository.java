package ru.yandex.practicum.filmorate.repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSourceUtils;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.validation.annotation.Validated;
import ru.yandex.practicum.filmorate.exception.InternalServerException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.repository.mapper.FilmRowMapper;

import java.util.*;

@Slf4j
@Repository
@RequiredArgsConstructor
public class JdbcFilmRepository implements FilmRepository {
    private final NamedParameterJdbcOperations jdbc;
    private final FilmRowMapper filmMapper = new FilmRowMapper();
    private final JdbcMpaRepository mpaRepository;
    private final JdbcGenreRepository genreRepository;

    @Override
    public List<Film> findAll() {
        String query = """
            SELECT f.*, m.id AS mpa_id, m.name AS mpa_name
            FROM films AS f
            LEFT JOIN mpa AS m ON f.mpa_id = m.id
            """;
        return jdbc.query(query, filmMapper);
    }

    @Override
    public List<Film> findFilmsByIds(List<Long> filmIds) {
        String query = """
            SELECT f.*, m.id AS mpa_id, m.name AS mpa_name
            FROM films AS f
            LEFT JOIN mpa AS m ON f.mpa_id = m.id
            WHERE f.id IN (:filmIds)
            """;
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("filmIds", filmIds);
        return jdbc.query(query, params, filmMapper);
    }

    @Override
    public Optional<Film> findById(Long filmId) {
        String query = """
            SELECT  f.*, m.id AS mpa_id, m.name AS mpa_name
            FROM films as f
            LEFT JOIN mpa AS m ON f.mpa_id = m.id
            WHERE f.id = :filmId
            """;
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("filmId", filmId);
        try {
            Film result = jdbc.queryForObject(query, params, filmMapper);
            return Optional.ofNullable(result);
        } catch (EmptyResultDataAccessException ignored) {
            return Optional.empty();
        }
    }

    @Override
    public Film create(Film film) {
        String query = """
            INSERT INTO films(name, description, release_date, duration, mpa_id)
            VALUES(:name, :description, :releaseDate, :duration, :mpaId)
            """;
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("name", film.getName());
        params.addValue("description", film.getDescription());
        params.addValue("releaseDate", film.getReleaseDate());
        params.addValue("duration", film.getDuration());
        if (film.getMpa() == null) {
            params.addValue("mpaId", null);
        } else {
            Long mpaId = film.getMpa().getId();
            Optional<Mpa> mpa = mpaRepository.getById(mpaId);
            if (mpa.isEmpty()) {
                log.error("Передан некорректный id mpa: {}", mpaId);
                throw new NotFoundException("Рейтинг с id " + mpaId + " не найден");
            }
            params.addValue("mpaId", mpaId);
        }
        jdbc.update(query, params, keyHolder);
        film.setId(keyHolder.getKeyAs(Long.class));
        if (film.getGenres() != null && !film.getGenres().isEmpty()) {
            batchCreateFilmsGenres(film.getId(), film.getGenres());
        }
        return film;
    }

    @Override
    public Film update(@Validated Film film) {
        Long filmId = film.getId();
        findById(filmId).orElseThrow(() -> {
            log.error("Фильм с id {} не найден", filmId);
            return new NotFoundException("Фильм с id " + filmId + " не найден");
        });

        String query = """
            UPDATE films SET name = :name, description = :description, release_date = :releaseDate, duration = :duration, mpa_id = :mpaId
            WHERE id = :filmId
            """;
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("filmId", filmId);
        params.addValue("name", film.getName());
        params.addValue("description", film.getDescription());
        params.addValue("releaseDate", film.getReleaseDate());
        params.addValue("duration", film.getDuration());
        if (film.getMpa() == null) {
            params.addValue("mpaId", null);
        } else {
            Long mpaId = film.getMpa().getId();
            Optional<Mpa> mpa = mpaRepository.getById(mpaId);
            if (mpa.isEmpty()) {
                log.error("Передан некорректный id mpa: {}", mpaId);
                throw new NotFoundException("Рейтинг с id " + mpaId + " не найден");
            }
            params.addValue("mpaId", mpaId);
        }
        int rowsUpdated = jdbc.update(query, params);
        if (rowsUpdated == 0) {
            log.error("Не удалось обновить фильм с id {}", filmId);
            throw new InternalServerException("Не удалось обновить данные");
        }

        if (film.getGenres() != null && !film.getGenres().isEmpty()) {
            jdbc.update("DELETE FROM films_genres WHERE film_id = :filmId", params);
            batchCreateFilmsGenres(filmId, film.getGenres());
        }

        return film;
    }

    @Override
    public List<Long> getFilmGenres(Long filmId) {
        String query = "SELECT genre_id FROM films_genres WHERE film_id = :filmId";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("filmId", filmId);

        return jdbc.queryForList(query, params, Long.class);
    }

    private void batchCreateFilmsGenres(Long filmId, Set<Genre> genres) {
        String query = "INSERT INTO films_genres (film_id, genre_id) VALUES (:filmId, :genreId)";
        Object[] batchParams = genres.stream()
                .map(genre -> {
                    Long genreId = genre.getId();
                    Optional<Genre> savedGenre = genreRepository.getById(genreId);
                    if (savedGenre.isEmpty()) {
                        log.error("Передан некорректный id genre: {}", genreId);
                        throw new NotFoundException("Жанр с id " + genreId + " не найден");
                    }
                    Map<String, Object> map = new HashMap<>();
                    map.put("filmId", filmId);
                    map.put("genreId", genreId);
                    return map;
                })
                .toArray();
        SqlParameterSource[] batch = SqlParameterSourceUtils.createBatch(batchParams);
        jdbc.batchUpdate(query, batch);
    }
}
