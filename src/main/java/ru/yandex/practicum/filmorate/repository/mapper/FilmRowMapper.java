package ru.yandex.practicum.filmorate.repository.mapper;

import ru.yandex.practicum.filmorate.model.Film;

import org.springframework.jdbc.core.RowMapper;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

public class FilmRowMapper implements RowMapper<Film> {
    @Override
    public Film mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        Film film = new Film();
        film.setId(resultSet.getLong("id"));
        film.setName(resultSet.getString("name"));
        film.setDescription(resultSet.getString("description"));
        film.setDuration(resultSet.getInt("duration"));
        LocalDateTime rawReleaseDate = resultSet.getTimestamp("release_date").toLocalDateTime();
        film.setReleaseDate(rawReleaseDate.toLocalDate());

        Mpa mpa = new Mpa();
        mpa.setId(resultSet.getLong("mpa_id"));
        mpa.setName(resultSet.getString("mpa_name"));
        film.setMpa(mpa);

        return film;
    }
}
