package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class FilmControllerTest {
    private final String descriptionTemplate = "a".repeat(200);
    private final LocalDate minDateRelease = LocalDate.of(1895, 12, 28);
    FilmController filmController = new FilmController();

    @Test
    @DisplayName("Проверка исключения на название фильма")
    void nameExceptionTest() {
        final ValidationException exception = assertThrows(ValidationException.class,
                () -> filmController.createFilm(filmGenerator(0, null, descriptionTemplate, minDateRelease, 1)));
        assertEquals("Название фильма не может быть пустым", exception.getMessage());
    }

    @Test
    @DisplayName("Проверка исключений на описание фильма")
    void descriptionExceptionTest() {
        final ValidationException emptyException = assertThrows(ValidationException.class,
                () -> filmController.createFilm(filmGenerator(0, "a", null, minDateRelease,
                        1)));
        assertEquals("Описание фильма не заполнено", emptyException.getMessage());

        final ValidationException overflowException = assertThrows(ValidationException.class,
                () -> filmController.createFilm(filmGenerator(0, "a", descriptionTemplate + "a", minDateRelease,
                        1)));
        assertEquals("Максимальная длинна описания 200 символов", overflowException.getMessage());
    }

    @Test
    @DisplayName("Проверка исключений на дату релиза фильма")
    void releaseDateExceptionTest() {
        final ValidationException emptyException = assertThrows(ValidationException.class,
                () -> filmController.createFilm(filmGenerator(0, "a", descriptionTemplate, null,
                        1)));
        assertEquals("Дата релиза не заполнена", emptyException.getMessage());

        final ValidationException overflowException = assertThrows(ValidationException.class,
                () -> filmController.createFilm(filmGenerator(0, "a", descriptionTemplate,
                        minDateRelease.minusDays(1),1)));
        assertEquals("Дата релиза не может быть ранее, чем 28 декабря 1895", overflowException.getMessage());
    }

    @Test
    @DisplayName("Проверка исключений на продолжительность фильма")
    void durationExceptionTest() {
        final ValidationException emptyException = assertThrows(ValidationException.class,
                () -> filmController.createFilm(filmGenerator(0, "a", descriptionTemplate, minDateRelease,
                        null)));
        assertEquals("Продолжительность фильма не заполнена", emptyException.getMessage());

        final ValidationException overflowException = assertThrows(ValidationException.class,
                () -> filmController.createFilm(filmGenerator(0, "a", descriptionTemplate,
                        minDateRelease,-1)));
        assertEquals("Продолжительность фильма не может быть отрицательной", overflowException.getMessage());
    }

    private Film filmGenerator(Integer id, String name, String description, LocalDate releaseDate, Integer duration) {
        Film film = new Film();
        film.setId(id);
        film.setName(name);
        film.setDescription(description);
        film.setReleaseDate(releaseDate);
        film.setDuration(duration);
        return film;
    }
}