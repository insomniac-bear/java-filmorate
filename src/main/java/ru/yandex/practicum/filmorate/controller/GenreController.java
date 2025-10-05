package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.dto.GenreResponse;
import ru.yandex.practicum.filmorate.sevice.GenreService;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/genres")
public class GenreController {
    private final GenreService service;

    @GetMapping
    public List<GenreResponse> getAll() {
        log.info("GET /genres - получение списка всех жанров");
        return service.getAll();
    }

    @GetMapping("/{genreId}")
    @ResponseStatus(HttpStatus.OK)
    public GenreResponse getById(@PathVariable Long genreId) {
        log.info("GET /genres/{} - получение жанра", genreId);
        return service.getById(genreId);
    }
}
