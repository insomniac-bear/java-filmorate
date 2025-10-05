package ru.yandex.practicum.filmorate.sevice;

import ru.yandex.practicum.filmorate.dto.GenreResponse;

import java.util.List;

public interface GenreServiceInterface {
    List<GenreResponse> getAll();

    GenreResponse getById(Long genreId);
}
