package ru.yandex.practicum.filmorate.repository;

import ru.yandex.practicum.filmorate.model.Friend;

import java.util.Optional;
import java.util.Set;

public interface FriendRepository {
    Set<Long> getAllFriends(Long userId);

    Optional<Friend> getFriendById(Long userId, Long friendId);

    void addFriend(Long userId, Long friendId);

    void acceptFriend(Long userId, Long friendId);

    void declineFriend(Long userId, Long friendId);

    void removeFriend(Long userId, Long friendId);
}
