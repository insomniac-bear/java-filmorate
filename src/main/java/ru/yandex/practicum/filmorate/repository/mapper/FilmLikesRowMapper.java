package ru.yandex.practicum.filmorate.repository.mapper;

import org.springframework.jdbc.core.RowMapper;
import ru.yandex.practicum.filmorate.model.FilmLikes;

import java.sql.ResultSet;
import java.sql.SQLException;

public class FilmLikesRowMapper implements RowMapper<FilmLikes> {
    @Override
    public FilmLikes mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        FilmLikes filmLikes = new FilmLikes();
        filmLikes.setFilmId(resultSet.getLong("film_id"));
        filmLikes.setLikesCount(resultSet.getInt("likes_count"));

        return filmLikes;
    }
}
