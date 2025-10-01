package ru.yandex.practicum.filmorate.sevice;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.repository.JdbcGenreRepository;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class GenreService {
    private final JdbcGenreRepository repository;

    public List<Genre> getAll() {
        return repository.getAll();
    }

    public Genre getById(Long genreId) {
        if (genreId == null) {
            log.error("не передан id для получения жанра");
            throw new IllegalArgumentException("id является обязательным параметром");
        }

        return repository.getById(genreId)
                .orElseThrow(() -> {
                    log.error("Жанр с id {} не найден", genreId);
                    return new NotFoundException("Жанр с id " + genreId + " не найден");
                });
    }
}
