package ru.yandex.practicum.filmorate.sevice;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class UserService extends StorageService<User> implements Friends {
    private final UserStorage userStorage;

    @Override
    public Collection<User> getAll() {
        return userStorage.getUsers();
    }

    @Override
    public User getById(Long userId) {
        return userStorage.getUserById(userId);
    }

    @Override
    public User create(User user) {
        return userStorage.createUser(user);
    }

    @Override
    public User update(User user) {
        return userStorage.updateUser(user);
    }

    public User addFriend(Long userId, Long friendId) {
        User user = userStorage.getUserById(userId);
        User friend = userStorage.getUserById(friendId);
        user.getFriends().add(friend.getId());
        friend.getFriends().add(user.getId());
        log.info("Пользователь {} добавлен в друзья к пользователю {}", friend, user);
        return user;
    }

    public User removeFriend(Long userId, Long friendId) {
        User user = userStorage.getUserById(userId);
        User friend = userStorage.getUserById(friendId);
        Set<Long> friends = user.getFriends();
        friends.remove(friend.getId());
        Set<Long> friendFriends = friend.getFriends();
        friendFriends.remove(user.getId());
        log.info("Пользователь {} удален из друзей у пользователя {}", friend, user);
        return user;
    }

    public List<User> getUserFriends(Long userId) {
        User user = userStorage.getUserById(userId);
        Set<Long> friendIds = user.getFriends();
        Collection<User> allUsers = userStorage.getUsers();
        return allUsers.stream()
                .filter(friend -> friendIds.contains(friend.getId()))
                .collect(Collectors.toList());
    }

    public List<User> getCommonFriends(Long userId, Long friendId) {
        User user = userStorage.getUserById(userId);
        User friend = userStorage.getUserById(friendId);
        Set<Long> commonFriendIds = new HashSet<>(user.getFriends());
        commonFriendIds.retainAll(friend.getFriends());
        Collection<User> allUsers = userStorage.getUsers();

        return allUsers.stream()
                .filter(anyUser -> commonFriendIds.contains(anyUser.getId()))
                .collect(Collectors.toList());
    }
}
