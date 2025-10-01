package ru.yandex.practicum.filmorate.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.repository.mapper.MpaRowMapper;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class JdbcMpaRepository implements MpaRepository {
    private final NamedParameterJdbcOperations jdbc;
    private final MpaRowMapper mpaMapper = new MpaRowMapper();

    public List<Mpa> getAll() {
        String query = "SELECT * FROM mpa";
        return jdbc.query(query, mpaMapper);
    }

    public Optional<Mpa> getById(Long mpaId) {
        String query = "SELECT * FROM mpa WHERE id = :mpaId";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("mpaId", mpaId);
        try {
            Mpa result = jdbc.queryForObject(query, params, mpaMapper);
            return Optional.ofNullable(result);
        } catch (EmptyResultDataAccessException ignored) {
            return Optional.empty();
        }
    }
}
