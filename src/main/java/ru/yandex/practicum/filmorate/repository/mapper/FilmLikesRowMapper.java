package ru.yandex.practicum.filmorate.repository.mapper;

import org.springframework.jdbc.core.RowMapper;
import ru.yandex.practicum.filmorate.model.Likes;

import java.sql.ResultSet;
import java.sql.SQLException;

public class FilmLikesRowMapper implements RowMapper<Likes> {
    @Override
    public Likes mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        Likes likes = new Likes();
        likes.setFilmId(resultSet.getLong("film_id"));
        likes.setLikesCount(resultSet.getInt("likes_count"));

        return likes;
    }
}
