package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;

@Slf4j
@Component
public class InMemoryUserStorage implements UserStorage {
    private final Map<Long, User> users = new HashMap<>();
    private Long lastId = 1L;

    @Override
    public Map<Long, User> getUsers() {
        return users;
    }

    @Override
    public User createUser(User user) {
        user.setId(lastId++);

        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }

        users.put(user.getId(), user);
        log.trace("Создан пользователь {}", user);
        return user;
    }

    @Override
    public User updateUser(User user) {
        if (user.getId() == null) {
            log.warn("Не указан id пользователя");
            throw new ValidationException("Id пользователя должен быть указан");
        }

        if (!users.containsKey(user.getId())) {
            log.warn("Пользователь с id {} не найден", user.getId());
            throw new NotFoundException("Пользователь с id " + user.getId() + " не найден");
        }

        User oldUser = users.get(user.getId());

        if (user.getName() != null && !user.getName().isEmpty()) {
            oldUser.setName(user.getName());
        }

        if (user.getLogin() != null) {
            oldUser.setLogin(user.getLogin());
        }

        if (user.getBirthday() != null) {
            oldUser.setBirthday(user.getBirthday());
        }

        if (user.getEmail() != null) {
            oldUser.setEmail(user.getEmail());
        }
        log.trace("Пользователь с id {} обновлен", user.getId());
        return oldUser;
    }

    @Override
    public User getUserById(Long userId) {
        if (userId == null) {
            log.warn("userId равен null");
            throw new ValidationException("userId не передан");
        }

        if (!users.containsKey(userId)) {
            throw new NotFoundException("Пользователь с id " + userId + " не найден");
        }

        return users.get(userId);
    }
}
