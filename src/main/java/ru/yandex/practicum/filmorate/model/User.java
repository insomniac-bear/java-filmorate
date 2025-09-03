package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

/**
* User
*/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User {
    private Long id;
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
