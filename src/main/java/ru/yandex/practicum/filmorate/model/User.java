package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import lombok.Data;

import java.time.LocalDate;

/**
* User
*/
@Data
public class User {
    private Integer id;
    @Email
    private String email;
    @NotNull
    @NotBlank
    private String login;
    private String name;
    @Past
    private LocalDate birthday;
}
