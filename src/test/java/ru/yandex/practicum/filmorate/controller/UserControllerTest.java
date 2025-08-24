package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class UserControllerTest {
    UserController userController = new UserController();

    @Test
    void emailNullExceptionTest() {
        final ValidationException exception = assertThrows(
                ValidationException.class,
                () -> userController.createUser(userGenerator(1, null, "login", "name", LocalDate.of(2020, 12, 1))));
        assertEquals("Email не заполнен", exception.getMessage());
    }

    @Test
    void emailNoValidExceptionTest() {
        final ValidationException exception = assertThrows(
                ValidationException.class,
                () -> userController.createUser(userGenerator(1, "email", "login", "name", LocalDate.of(2020, 12, 1))));
        assertEquals("Некорректный email", exception.getMessage());
    }

    @Test
    void loginNullExceptionTest() {
        final ValidationException exception = assertThrows(
                ValidationException.class,
                () -> userController.createUser(userGenerator(1, "email@email.ru", null, "name", LocalDate.of(2020, 12,
                        1))));
        assertEquals("Логин пользователя не заполнен", exception.getMessage());
    }

    @Test
    void loginNoValidExceptionTest() {
        final ValidationException exception = assertThrows(
                ValidationException.class,
                () -> userController.createUser(userGenerator(1, "email@email.ru", "my login", "name", LocalDate.of(2020, 12,
                        1))));
        assertEquals("Логин пользователя не должен содержать пробелы", exception.getMessage());
    }

    @Test
    void birthDayExceptionTest() {
        final ValidationException exception = assertThrows(
                ValidationException.class,
                () -> userController.createUser(userGenerator(1, "email@email.ru", "login", "name",
                        LocalDate.now().plusDays(1))));
        assertEquals("Некорректно указана дата рождения", exception.getMessage());
    }

    @Test
    void addUserTest() {
        User user = userGenerator(1, "emailemail.ru@", "login", "", LocalDate.now());
        userController.createUser(user);
        assertEquals(user.getName(), user.getLogin());
        assertEquals(user.getId(), 1);
    }

    private User userGenerator(int id, String email, String login, String name, LocalDate birthday) {
        User user = new User();
        user.setId(id);
        user.setEmail(email);
        user.setLogin(login);
        user.setName(name);
        user.setBirthday(birthday);
        return user;
    }
}