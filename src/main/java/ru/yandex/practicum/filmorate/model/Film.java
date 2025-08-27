package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

/**
 * Film.
 */
@Data
@Builder
public class Film {
    private Integer id;
    @NotNull(message = "Название фильма не может быть пустым")
    @NotBlank(message = "Название фильма не может быть пустым")
    private String name;
    @NotNull(message = "Описание фильма не заполнено")
    @NotBlank(message = "Описание фильма не может быть пустым")
    @Size(min = 1, max = 200, message = "Длинна описания не может быть пустой или более 200 символов")
    private String description;
    private LocalDate releaseDate;
    @Positive(message = "Продолжительность фильма не может быть отрицательной")
    private Integer duration;

    @AssertTrue(message = "Фильм не может быть раньше 28.12.1895")
    public boolean isValidReleaseDate() {
        return releaseDate == null || !releaseDate.isBefore(LocalDate.of(1895, 12, 28));
    }
}
