package ru.yandex.practicum.filmorate.sevice;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final InMemoryUserStorage userStorage;

    public User addFriend(Long userId, Long friendId) {
        User user = userStorage.getUserById(userId);
        User friend = userStorage.getUserById(friendId);
        user.getFriends().add(friend.getId());
        friend.getFriends().add(user.getId());
        log.trace("Пользователь {} добавлен в друзья к пользователю {}", friend, user);
        return user;
    }

    public User removeFriend(User user, User friend) {
        Set<Long> friends = user.getFriends();
        friends.remove(friend.getId());
        Set<Long> friendFriends = friend.getFriends();
        friendFriends.remove(user.getId());
        log.trace("Пользователь {} удален из друзей у пользователя {}", friend, user);
        return user;
    }

    public List<User> getCommonFriends(User user, User friend) {
        Set<Long> commonFriendIds = new HashSet<>(user.getFriends());
        commonFriendIds.retainAll(friend.getFriends());
        Map<Long, User> allUsers = userStorage.getUsers();

        return commonFriendIds.stream()
                .map(allUsers::get)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }
}
