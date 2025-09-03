package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.sevice.UserService;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;

import java.util.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {
    private final UserService userService;
    private final InMemoryUserStorage userStorage;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Collection<User> getUsers() {
        return userStorage.getUsers().values();
    }

    @GetMapping("/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public User getUserById(@PathVariable Long userId) {
        return userStorage.getUserById(userId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public User createUser(@Valid @RequestBody User user) {
        return userStorage.createUser(user);
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public User updateUser(@Valid @RequestBody User user) {
        return userStorage.updateUser(user);
    }

    @PutMapping("/{id}/friends/{friendId}")
    @ResponseStatus(HttpStatus.OK)
    public User addFriend(@PathVariable Map<String, String> pathVarsMap) {
        Long userId = Long.parseLong(pathVarsMap.get("id"));
        Long friendId = Long.parseLong(pathVarsMap.get("friendId"));
        return userService.addFriend(userId, friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    @ResponseStatus(HttpStatus.OK)
    public User removeFriend(@PathVariable Map<String, String> pathVarsMap) {
        Long userId = Long.parseLong(pathVarsMap.get("id"));
        Long friendId = Long.parseLong(pathVarsMap.get("friendId"));
        return userService.removeFriend(userStorage.getUserById(userId), userStorage.getUserById(friendId));
    }
    
    @GetMapping("/{id}/friends")
    @ResponseStatus(HttpStatus.OK)
    public Set<Long>getFriends(@PathVariable String id) {
        Long userId = Long.parseLong(id);
        User user = userStorage.getUserById(userId);
        return user.getFriends();
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    @ResponseStatus(HttpStatus.OK)
    public List<User>getCommonFriends(@PathVariable Map<String, String> pathVarMaps) {
        Long userId = Long.parseLong(pathVarMaps.get("id"));
        Long friendId = Long.parseLong(pathVarMaps.get("otherId"));
        return userService.getCommonFriends(userStorage.getUserById(userId), userStorage.getUserById(friendId));
    }
}
