package ru.yandex.practicum.filmorate.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.yandex.practicum.filmorate.dto.UpdateUserRequest;
import ru.yandex.practicum.filmorate.model.User;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserMapper {
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
}
