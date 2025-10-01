package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.sevice.MpaService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/mpa")
public class MpaController {
    private final MpaService service;

    @GetMapping
    public List<Mpa> getAll() {
        return service.getAll();
    }

    @GetMapping("/{mpaId}")
    @ResponseStatus(HttpStatus.OK)
    public Mpa getById(@PathVariable Long mpaId) {
        return service.getById(mpaId);
    }
}
