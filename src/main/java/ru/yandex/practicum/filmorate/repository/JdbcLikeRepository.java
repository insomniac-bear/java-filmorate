package ru.yandex.practicum.filmorate.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.stereotype.Repository;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Repository
@RequiredArgsConstructor
public class JdbcLikeRepository implements LikeRepository {
    private final NamedParameterJdbcOperations jdbc;

    @Override
    public void addLike(Long filmId, Long userId) {
        String query = "INSERT INTO likes (film_id, user_id) VALUES (:filmId, :userId)";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("filmId", filmId);
        params.addValue("userId", userId);
        jdbc.update(query, params);
    }

    @Override
    public void removeLike(Long filmId, Long userId) {
        String query = "DELETE FROM likes WHERE film_id = :filmId AND user_id = :userId";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("filmId", filmId);
        params.addValue("userId", userId);
        jdbc.update(query, params);
    }

    public Set<Long> getFilmLikes(Long filmId) {
        String query = "SELECT user_id FROM likes WHERE film_id = :filmId";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("filmId", filmId);
        List<Long> userIds = jdbc.queryForList(query, params, Long.class);
        return new HashSet<>(userIds);
    }

    public List<Long> getPopularFilmIds(int limit) {
        String query = "SELECT film_id FROM likes GROUP BY film_id ORDER BY COUNT(user_id) DESC LIMIT :limit";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("limit", limit);

        return jdbc.queryForList(query, params, Long.class);
    }
}
