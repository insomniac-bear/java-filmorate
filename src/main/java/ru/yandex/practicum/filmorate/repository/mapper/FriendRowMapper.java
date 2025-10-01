package ru.yandex.practicum.filmorate.repository.mapper;

import org.springframework.jdbc.core.RowMapper;
import ru.yandex.practicum.filmorate.model.Friend;
import ru.yandex.practicum.filmorate.util.FriendStatus;

import java.sql.ResultSet;
import java.sql.SQLException;

public class FriendRowMapper implements RowMapper<Friend> {
    @Override
    public Friend mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        Friend friend = new Friend();
        friend.setUserId(resultSet.getLong("user_id"));
        friend.setFriendId(resultSet.getLong("friend_id"));
        FriendStatus status = FriendStatus.valueOf(resultSet.getString("status"));
        friend.setStatus(status);
        return friend;
    }
}
