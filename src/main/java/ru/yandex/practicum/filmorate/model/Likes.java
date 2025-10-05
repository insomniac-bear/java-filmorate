package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class Likes {
    private Long id;
    private Long filmId;
    private int likesCount;
}
