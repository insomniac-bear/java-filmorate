package ru.yandex.practicum.filmorate.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.yandex.practicum.filmorate.dto.CreateUserRequest;
import ru.yandex.practicum.filmorate.dto.UpdateUserRequest;
import ru.yandex.practicum.filmorate.dto.UserResponse;
import ru.yandex.practicum.filmorate.model.User;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserMapper {
    public static User createUserRequestToUser(CreateUserRequest request) {
        return User.builder()
                .login(request.getLogin())
                .email(request.getEmail())
                .name(request.getName() != null ? request.getName() : request.getLogin())
                .birthday(request.getBirthday())
                .build();
    }

    public static User updateUserFields(User user, UpdateUserRequest request) {
        if (request.hasEmail()) {
            user.setEmail(request.getEmail());
        }

        if (request.hasLogin()) {
            if (!request.hasName()) {
                user.setName(request.getLogin());
            }

            user.setLogin(request.getLogin());
        }

        if (request.hasName()) {
            user.setName(request.getName());
        }

        if (request.hasBirthday()) {
            user.setBirthday(request.getBirthday());
        }

        return user;
    }

    public static UserResponse userToUserResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .login(user.getLogin())
                .email(user.getEmail())
                .name(user.getName())
                .birthday(user.getBirthday())
                .build();
    }
}
