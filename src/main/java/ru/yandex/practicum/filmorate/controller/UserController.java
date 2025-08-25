package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.*;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {
    private final Map<Integer, User> users = new HashMap<>();
    private int lastId = 1;

    @GetMapping
    public Collection<User> getUsers() {
        return users.values();
    }

    @PostMapping
    public User createUser(@RequestBody User user) {
        validateUser(user);
        user.setId(lastId++);

        if (user.getName() == null || user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }

        users.put(user.getId(), user);
        return user;
    }

    @PutMapping
    public User updateUser(@RequestBody User user) {
        if (user.getId() == null) {
            log.warn("не указан id");
            throw new ValidationException("Id пользователя должен быть указан");
        }

        if (!users.containsKey(user.getId())) {
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

        validateUser(oldUser);
        return oldUser;

    }

    private void validateUser(User user) {
        if (user.getEmail() == null || user.getEmail().isEmpty()) {
            log.warn("email пользователя не заполнен");
            throw new ValidationException("Email не заполнен");
        }

        if (user.getLogin() == null || user.getLogin().isEmpty()) {
            log.warn("login пользователя не заполнен");
            throw new ValidationException("Логин пользователя не заполнен");
        }

        if (user.getBirthday() == null) {
            log.warn("birthday пользователя не заполнена");
            throw new ValidationException("Дата рождения пользователя не заполнена");
        }

        if (!user.getEmail().contains("@")) {
            log.warn("email не содержит символ @");
            throw new ValidationException("Некорректный email");
        }

        if (user.getLogin().contains(" ")) {
            log.warn("login содержит пробелы");
            throw new ValidationException("Логин пользователя не должен содержать пробелы");
        }

        if (user.getBirthday().isAfter(LocalDate.now())) {
            log.warn("birthday указано в будущем");
            throw new ValidationException("Некорректно указана дата рождения");
        }

    }
}
