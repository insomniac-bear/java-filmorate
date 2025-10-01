package ru.yandex.practicum.filmorate.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.repository.mapper.GenreRowMapper;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class JdbcGenreRepository implements GenreRepository {
    private final NamedParameterJdbcOperations jdbc;
    private final GenreRowMapper genreMapper = new GenreRowMapper();

    public List<Genre> getAll() {
        String query = "SELECT * FROM genres";
        return jdbc.query(query, genreMapper);
    }

    public Optional<Genre> getById(Long genreId) {
        String query = "SELECT * FROM genres WHERE id = :genreId";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("genreId", genreId);
        try {
            Genre result = jdbc.queryForObject(query, params, genreMapper);
            return Optional.ofNullable(result);
        } catch (EmptyResultDataAccessException ignored) {
            return Optional.empty();
        }
    }

}
