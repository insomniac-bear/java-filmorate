package ru.yandex.practicum.filmorate.dto;

import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@Builder
public class CreateUserRequest {
    @NotEmpty
    @Email
    private String email;
    @NotBlank
    @Pattern(regexp = "^\\S+$", message = "Логин не может содержать пробелы")
    private String login;
    private String name;
    @PastOrPresent
    private LocalDate birthday;
    @Builder.Default
    private Set<Long> friends = new HashSet<>();
}
