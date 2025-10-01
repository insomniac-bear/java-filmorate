package ru.yandex.practicum.filmorate.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.repository.mapper.UserRowMapper;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class JdbcUserRepository implements UserRepository {
    private final NamedParameterJdbcOperations jdbc;
    private final UserRowMapper userMapper = new UserRowMapper();

    @Override
    public List<User> findAll() {
        String query = "SELECT * FROM users";
        return jdbc.query(query, userMapper);
    }

    @Override
    public Optional<User> findById(Long userId) {
        String query = "SELECT * FROM users WHERE id = :userId";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("userId", userId);
        try {
            User result = jdbc.queryForObject(query, params, userMapper);
            return Optional.ofNullable(result);
        } catch (EmptyResultDataAccessException ignored) {
            return Optional.empty();
        }
    }

    @Override
    public User create(User user) {
        String query = "INSERT INTO users(email, login, name, birthday) VALUES(:email, :login, :name, :birthday)";
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("email", user.getEmail());
        params.addValue("login", user.getLogin());
        params.addValue("name", user.getName());
        params.addValue("birthday", user.getBirthday());
        jdbc.update(query, params, keyHolder);

        user.setId(keyHolder.getKeyAs(Long.class));

        return user;
    }

    @Override
    public User update(User user) {
        String query = "UPDATE users SET email = :email, login = :login, name = :name, birthday = :birthday WHERE id = :userId";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("email", user.getEmail());
        params.addValue("login", user.getLogin());
        params.addValue("name", user.getName());
        params.addValue("birthday", user.getBirthday());
        params.addValue("userId", user.getId());

        jdbc.update(query, params);

        return user;
    }
}
