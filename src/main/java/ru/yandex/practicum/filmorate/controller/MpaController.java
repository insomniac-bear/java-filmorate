package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.sevice.MpaService;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/mpa")
public class MpaController {
    private final MpaService service;

    @GetMapping
    public List<Mpa> getAll() {
        log.info("GET /mpa - получение списка всех mpa");
        return service.getAll();
    }

    @GetMapping("/{mpaId}")
    @ResponseStatus(HttpStatus.OK)
    public Mpa getById(@PathVariable Long mpaId) {
        log.info("GET /mpa/{} - получение mpa", mpaId);
        return service.getById(mpaId);
    }
}
