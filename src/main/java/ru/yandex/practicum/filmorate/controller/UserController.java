package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.dto.CreateUserRequest;
import ru.yandex.practicum.filmorate.dto.UpdateUserRequest;
import ru.yandex.practicum.filmorate.dto.UserResponse;
import ru.yandex.practicum.filmorate.sevice.UserService;

import java.util.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<UserResponse> getUsers() {
        log.info("GET /users - получение всех пользователей");
        return userService.getAll();
    }

    @GetMapping("/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public UserResponse getUserById(@PathVariable Long userId) {
        log.info("GET /users/{} - получение пользователя", userId);
        return userService.getById(userId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponse createUser(@Valid @RequestBody CreateUserRequest request) {
        log.info("POST /users - создание пользователя {}", request);
        return userService.create(request);
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public UserResponse updateUser(@RequestBody UpdateUserRequest request) {
        log.info("PUT /users - обновление пользователя {}", request);
        return userService.update(request);
    }

    @PutMapping("/{id}/friends/{friendId}")
    @ResponseStatus(HttpStatus.OK)
    public UserResponse addFriend(@PathVariable Map<String, String> pathVarsMap) {
        Long userId = Long.parseLong(pathVarsMap.get("id"));
        Long friendId = Long.parseLong(pathVarsMap.get("friendId"));
        log.info("PUT /users/{}/friends/{} - добавление в друзья", userId, friendId);
        return userService.addFriend(userId, friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    @ResponseStatus(HttpStatus.OK)
    public void removeFriend(@PathVariable Map<String, String> pathVarsMap) {
        Long userId = Long.parseLong(pathVarsMap.get("id"));
        Long friendId = Long.parseLong(pathVarsMap.get("friendId"));
        log.info("DELETE /users/{}/friends/{} - удаление из друзей", userId, friendId);
        userService.removeFriend(userId, friendId);
    }

    @GetMapping("/{id}/friends")
    @ResponseStatus(HttpStatus.OK)
    public List<UserResponse> getFriends(@PathVariable Long id) {
        log.info("GET /users/{}/friends - получение всех друзей", id);
        return userService.getUserFriends(id);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    @ResponseStatus(HttpStatus.OK)
    public List<UserResponse> getCommonFriends(@PathVariable Map<String, String> pathVarMaps) {
        Long userId = Long.parseLong(pathVarMaps.get("id"));
        Long friendId = Long.parseLong(pathVarMaps.get("otherId"));
        log.info("GET /users/{}/friends/{} - получение общих друзей", userId, friendId);
        return userService.getCommonFriends(userId, friendId);
    }
}
