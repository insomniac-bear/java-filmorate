package ru.yandex.practicum.filmorate.repository.mapper;

import ru.yandex.practicum.filmorate.dto.FilmResponse;
import ru.yandex.practicum.filmorate.model.Film;

import org.springframework.jdbc.core.RowMapper;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.ResultSet;
import java.sql.SQLException;

public class FilmRowMapper implements RowMapper<FilmResponse> {
    @Override
    public FilmResponse mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        Film film = Film.builder()
                .id(resultSet.getLong("id"))
                .name(resultSet.getString("name"))
                .description(resultSet.getString("description"))
                .releaseDate(resultSet.getDate("release_date").toLocalDate())
                .duration(resultSet.getInt("duration"))
                .mpaId(resultSet.getLong("mpa_id"))
                .build();
        Mpa mpa = Mpa.builder()
                .id(resultSet.getLong("mpa_id"))
                .name(resultSet.getString("mpa_name"))
                .build();

        FilmResponse filmData = FilmResponse.builder()
                .id(film.getId())
                .mpa(mpa)
                .build();
        filmData.setFilmData(film);

        return filmData;
    }
}
