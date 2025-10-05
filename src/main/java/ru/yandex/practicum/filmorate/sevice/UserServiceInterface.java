package ru.yandex.practicum.filmorate.sevice;

import ru.yandex.practicum.filmorate.dto.CreateUserRequest;
import ru.yandex.practicum.filmorate.dto.UpdateUserRequest;
import ru.yandex.practicum.filmorate.dto.UserResponse;

import java.util.List;

public interface UserServiceInterface {
    List<UserResponse> getAll();

    UserResponse getById(Long userId);

    UserResponse create(CreateUserRequest request);

    UserResponse update(UpdateUserRequest request);

    UserResponse addFriend(Long userId, Long friendId);

    void removeFriend(Long userId, Long friendId);

    List<UserResponse> getUserFriends(Long userId);

    List<UserResponse> getCommonFriends(Long userId, Long friendId);
}
