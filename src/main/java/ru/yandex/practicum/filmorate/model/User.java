package ru.yandex.practicum.filmorate.model;

import lombok.*;

import java.time.LocalDate;

/**
* User
*/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User {
    private Long id;
    private String email;
    private String login;
    private String name;
    private LocalDate birthday;
    private Long ratingId;
}
