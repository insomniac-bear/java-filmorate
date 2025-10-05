package ru.yandex.practicum.filmorate.repository.mapper;

import org.springframework.jdbc.core.RowMapper;
import ru.yandex.practicum.filmorate.dto.FriendResponse;
import ru.yandex.practicum.filmorate.model.Friend;
import ru.yandex.practicum.filmorate.util.FriendStatusValues;

import java.sql.ResultSet;
import java.sql.SQLException;

public class FriendRowMapper implements RowMapper<FriendResponse> {
    @Override
    public FriendResponse mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        return FriendResponse.builder()
                .id(resultSet.getLong("user_id"))
                .userId(resultSet.getLong("user_id"))
                .friendId(resultSet.getLong("friend_id"))
                .status(FriendStatusValues.valueOf(resultSet.getString("status")))
                .build();
    }
}
