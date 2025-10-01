package ru.yandex.practicum.filmorate.repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.InternalServerException;
import ru.yandex.practicum.filmorate.model.Friend;
import ru.yandex.practicum.filmorate.repository.mapper.FriendRowMapper;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Repository
@RequiredArgsConstructor
public class JdbcFriendRepository implements FriendRepository {
    private final NamedParameterJdbcOperations jdbc;
    private final FriendRowMapper friendMapper = new FriendRowMapper();

    @Override
    public Set<Long> getAllFriends(Long userId) {
        String query = "SELECT f.user_id, f.friend_id, fs.status FROM friends AS f "
                + "LEFT JOIN friend_statuses AS fs ON f.status_id = fs.id "
                + "WHERE f.user_id = :userId OR (f.friend_id = :userId AND fs.status = 'ACCEPTED')";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("userId", userId);

        List<Friend> friends = jdbc.query(query, params, friendMapper);
        return friends.stream()
                .map(friend -> friend.getFriendId())
                .collect(Collectors.toSet());
    }

    @Override
    public Optional<Friend> getFriendById(Long userId, Long friendId) {
        String query = "SELECT f.user_id, f.friend_id, fs.status FROM friends AS f "
                + "LEFT JOIN friend_statuses AS fs ON f.status_id = fs.id "
                + "WHERE (f.user_id = :userId AND f.friend_id = :friendId) "
                + "OR (f.user_id = :friendId AND f.friend_id = :userId AND fs.status = 'ACCEPTED')";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("userId", userId);
        params.addValue("friendId", friendId);
        try {
            Friend result = jdbc.queryForObject(query, params, friendMapper);
            return Optional.ofNullable(result);
        } catch (EmptyResultDataAccessException ignored) {
            return Optional.empty();
        }
    }

    @Override
    public void addFriend(Long userId, Long friendId) {
        String query = "INSERT INTO friends (user_id, friend_id, status_id) VALUES (:userId, :friendId, SELECT id FROM friend_statuses WHERE status = 'PENDING')";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("userId", userId);
        params.addValue("friendId", friendId);
        int rowUpdated = jdbc.update(query, params);

        if (rowUpdated == 0) {
            log.error("Ошибка при добовлении пользователя с id {} в друзья к пользователю с id {}", friendId, userId);
            throw new InternalServerException("Не удалось добавить друга");
        }
    }

    @Override
    public void acceptFriend(Long userId, Long friendId) {
        String query = "UPDATE friends SET status_id = SELECT id FROM friend_statuses WHERE status = 'ACCEPTED' WHERE user_id = :userId AND friend_id = :friendId";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("userId", userId);
        params.addValue("friendId", friendId);
        int rowUpdated = jdbc.update(query, params);

        if (rowUpdated == 0) {
            log.error("Ошибка при подтверждении дружбы пользователя с id {} с пользователем с id {}", friendId, userId);
            throw new InternalServerException("Не удалось подтвердить дружбу");
        }
    }

    @Override
    public void declineFriend(Long userId, Long friendId) {
        String query = "UPDATE friends SET user_id = :friendId, friend_id = :userId, status_id = SELECT id FROM friend_statuses WHERE status = 'PENDING' WHERE user_id = :userId AND friend_id = :friendId";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("userId", userId);
        params.addValue("friendId", friendId);
        int rowUpdated = jdbc.update(query, params);

        if (rowUpdated == 0) {
            log.error("Ошибка при подтверждении дружбы пользователя с id {} с пользователем с id {}", friendId, userId);
            throw new InternalServerException("Не удалось подтвердить дружбу");
        }
    }

    @Override
    public void removeFriend(Long userId, Long friendId) {
        String query = "DELETE FROM friends WHERE user_id = :userId AND friend_id = :friendId";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("userId", userId);
        params.addValue("friendId", friendId);

        jdbc.update(query, params);
    }
}
