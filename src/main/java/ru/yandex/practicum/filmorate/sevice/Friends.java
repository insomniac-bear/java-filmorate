package ru.yandex.practicum.filmorate.sevice;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface Friends {

    User addFriend(Long userId, Long friendId);

    User removeFriend(Long userId, Long friendId);

    List<User> getUserFriends(Long userId);

    List<User> getCommonFriends(Long userId, Long friendId);
}
