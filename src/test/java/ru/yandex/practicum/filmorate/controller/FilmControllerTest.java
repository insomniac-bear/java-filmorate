package ru.yandex.practicum.filmorate.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class FilmControllerTest {
    @Autowired private MockMvc mvc;
    @Autowired private ObjectMapper objectMapper;

    @Test
    @DisplayName("Проверка исключения на null название фильма")
    void nameIsNullExceptionTest() throws Exception {
        Film film =
                Film.builder()
                        .name(null)
                        .description("Описание")
                        .duration(1)
                        .releaseDate(LocalDate.now())
                        .build();

        mvc.perform(
                post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(film)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Проверка исключения на название фильма состоящее только из пробелов")
    void nameIsBlankExceptionTest() throws Exception {
        Film film =
                Film.builder()
                        .name(" ")
                        .description("Описание")
                        .duration(1)
                        .releaseDate(LocalDate.now())
                        .build();

        mvc.perform(
                post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(film)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Проверка исключения на пустое название фильма")
    void nameIsEmptyExceptionTest() throws Exception {
        Film film =
                Film.builder()
                        .name("")
                        .description("Описание")
                        .duration(1)
                        .releaseDate(LocalDate.now())
                        .build();

        mvc.perform(
                post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(film)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Проверка исключения на null описание фильма")
    void descriptionIsNullExceptionTest() throws Exception {
        Film film =
                Film.builder()
                        .name("Имя")
                        .description(null)
                        .duration(1)
                        .releaseDate(LocalDate.now())
                        .build();

        mvc.perform(
                post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(film)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Проверка исключения на описание фильма состоящее только из пробелов")
    void descriptionIsBlankExceptionTest() throws Exception {
        Film film =
                Film.builder()
                        .name("Имя")
                        .description(" ")
                        .duration(1)
                        .releaseDate(LocalDate.now())
                        .build();

        mvc.perform(
                        post("/films")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(film)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Проверка исключения на пустое описание фильма")
    void descriptionIsEmptyExceptionTest() throws Exception {
        Film film =
                Film.builder()
                        .name("Имя")
                        .description("")
                        .duration(1)
                        .releaseDate(LocalDate.now())
                        .build();

        mvc.perform(
                        post("/films")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(film)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Проверка исключения на длинное описание фильма")
    void descriptionIsTooLongExceptionTest() throws Exception {
        Film film =
                Film.builder()
                        .name("Имя")
                        .description("a".repeat(201))
                        .duration(1)
                        .releaseDate(LocalDate.now())
                        .build();

        mvc.perform(
                        post("/films")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(film)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Проверка исключений на дату релиза фильма")
    void releaseDateExceptionTest() throws Exception {
        Film film =
                Film.builder()
                        .name("Имя")
                        .description("Описание")
                        .duration(1)
                        .releaseDate(LocalDate.of(1800, 1, 1))
                        .build();

        mvc.perform(
                        post("/films")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(film)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Проверка исключений на нулевую продолжительность фильма")
    void durationIsZeroExceptionTest() throws Exception {
        Film film =
                Film.builder()
                        .name("Имя")
                        .description("Описание")
                        .duration(0)
                        .releaseDate(LocalDate.now())
                        .build();

        mvc.perform(
                        post("/films")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(film)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Проверка исключений на отрицательную продолжительность фильма")
    void durationIsNegativeExceptionTest() throws Exception {
        Film film =
                Film.builder()
                        .name("Имя")
                        .description("Описание")
                        .duration(-1)
                        .releaseDate(LocalDate.now())
                        .build();

        mvc.perform(
                        post("/films")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(film)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Проверка на успешное создание фильма")
    void durationIsCreatedTest() throws Exception {
        Film film =
                Film.builder()
                        .name("Имя")
                        .description("Описание")
                        .duration(1)
                        .releaseDate(LocalDate.now())
                        .build();

        mvc.perform(
                        post("/films")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(film)))
                .andExpect(status().isOk());
    }
}