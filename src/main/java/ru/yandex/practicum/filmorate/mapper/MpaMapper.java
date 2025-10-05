package ru.yandex.practicum.filmorate.mapper;

import ru.yandex.practicum.filmorate.dto.MpaResponse;
import ru.yandex.practicum.filmorate.model.Mpa;

public class MpaMapper {
    public static MpaResponse mpaToMpaResponse(Mpa mpa) {
        return MpaResponse.builder()
                .id(mpa.getId())
                .name(mpa.getName())
                .build();
    }
}
