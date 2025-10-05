package ru.yandex.practicum.filmorate.sevice;

import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;

public interface MpaServiceInterface {
    List<ru.yandex.practicum.filmorate.model.Mpa> getAll();

    Mpa getById(Long mpaId);
}
