package ru.yandex.practicum.filmorate.sevice;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dto.GenreResponse;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.mapper.GenreMapper;
import ru.yandex.practicum.filmorate.repository.JdbcGenreRepository;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class GenreService implements GenreServiceInterface {
    private final JdbcGenreRepository repository;

    @Override
    public List<GenreResponse> getAll() {
        return repository.getAll().stream()
                .map(GenreMapper::genreToGenreResponse)
                .toList();
    }

    @Override
    public GenreResponse getById(Long genreId) {
        if (genreId == null) {
            log.error("не передан id для получения жанра");
            throw new IllegalArgumentException("id является обязательным параметром");
        }

        return repository.getById(genreId)
                .map(GenreMapper::genreToGenreResponse)
                .orElseThrow(() -> {
                    log.error("Жанр с id {} не найден", genreId);
                    return new NotFoundException("Жанр с id " + genreId + " не найден");
                });
    }
}
