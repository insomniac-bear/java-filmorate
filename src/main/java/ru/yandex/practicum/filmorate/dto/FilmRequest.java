package ru.yandex.practicum.filmorate.dto;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Slf4j
@Data
public class FilmRequest {
    private Long id;
    private String name;
    private String description;
    private LocalDate releaseDate;
    private Integer duration;
    private Mpa mpa;
    private Set<Genre> genres = new HashSet<>();

    public boolean hasId() {
        return id != null;
    }

    public boolean hasName() {
        return !(name == null || name.isBlank());
    }

    public boolean hasDescription() {
        if (description == null || description.isBlank()) {
            return false;
        }

        if (description.length() > 200) {
            log.warn("Передано описание более 200 символов");
            throw new ValidationException("Описание фильма не может быть меньше 200 символов");
        }

        return  true;
    }

    public boolean hasReleaseDate() {
        if (releaseDate == null) {
            return false;
        }

        if (releaseDate.isBefore(LocalDate.of(1895, 12, 28))) {
            log.error("Переданный releaseDate раньше 28.12.1895");
            throw new ValidationException("Фильм не может быть раньше 28.12.1895");
        }
        return true;
    }

    public boolean hasDuration() {
        if (duration == null) {
            return false;
        }

        if (duration < 0) {
            log.error("Передан duration меньше 0");
            throw new ValidationException("Продолжительность фильма не может быть отрицательной");
        }
        return true;
    }

    public boolean hasMpa() {
        return mpa != null;
    }

    public boolean hasGenres() {
        return !genres.isEmpty();
    }
}
