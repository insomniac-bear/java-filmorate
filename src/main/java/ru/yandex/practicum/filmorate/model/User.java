package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

/**
* User
*/
@Data
@Builder
public class User {
    private Integer id;
    @Email
    private String email;
    @NotNull
    @NotBlank
    @Pattern(regexp = "^\\S+$", message = "Логин не может содержать пробелы")
    private String login;
    private String name;
    @Past
    private LocalDate birthday;
}
