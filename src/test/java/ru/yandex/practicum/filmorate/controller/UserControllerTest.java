package ru.yandex.practicum.filmorate.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {
    @Autowired private MockMvc mvc;
    @Autowired private ObjectMapper objectMapper;

    @Test
    @DisplayName("Проверка Email на null значение")
    void emailIsNullExceptionTest() throws Exception {
        User user = User.builder()
                .email(null)
                .name("Имя")
                .birthday(LocalDate.now())
                .build();

        mvc.perform(
                post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Проверка Email на пустое значение")
    void emailIsBlankExceptionTest() throws Exception {
        User user = User.builder()
                .email(" ")
                .name("Имя")
                .birthday(LocalDate.now())
                .build();

        mvc.perform(
                post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Проверка Email на не корректное значение")
    void emailIsInvalidExceptionTest() throws Exception {
        User user = User.builder()
                .email("invalid.mail")
                .name("Имя")
                .birthday(LocalDate.now())
                .build();

        mvc.perform(
                post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Проверка Login на null значение")
    void loginNullExceptionTest() throws Exception {
        User user = User.builder()
                .email("valid@mail.com")
                .login(null)
                .birthday(LocalDate.now())
                .build();

        mvc.perform(
                post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Проверка Login на пробелы")
    void loginNoValidExceptionTest() throws Exception {
        User user = User.builder()
                .email("valid@mail.com")
                .login("Не корректный логин")
                .birthday(LocalDate.now())
                .build();

        mvc.perform(
                post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Проверка Birthday на то, что оно не в будущем")
    void birthDayExceptionTest() throws Exception {
        User user = User.builder()
                .email("valid@mail.com")
                .login("Не корректный логин")
                .birthday(LocalDate.now().plusDays(1))
                .build();

        mvc.perform(
                post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Проверка корректного создания пользователя")
    void addUserTest() throws Exception {
        User user = User.builder()
                .email("valid@mail.com")
                .login("Логин")
                .birthday(LocalDate.now().minusDays(1))
                .build();

        mvc.perform(
                post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isCreated());
    }
}