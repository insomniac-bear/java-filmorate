package ru.yandex.practicum.filmorate.sevice;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.Collection;
import java.util.Comparator;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class FilmService extends StorageService<Film> implements Recommendation {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    @Override
    public Collection<Film> getAll() {
        return filmStorage.getFilms();
    }

    @Override
    public Film getById(Long filmId) {
        if (filmId == null) {
            log.error("Не передан id фильма");
            throw new ValidationException("filmId не передан");
        }

        return filmStorage.getFilmById(filmId);
    }

    @Override
    public Film create(Film film) {
        return filmStorage.createFilm(film);
    }

    @Override
    public Film update(Film film) {
        if (film.getId() == null) {
            log.error("Не передан id фильма");
            throw new ValidationException("Id должен быть указан");
        }

        return filmStorage.updateFilm(film);
    }

    public Film addLike(Long filmId, Long userId) {
        Film film = filmStorage.getFilmById(filmId);
        User user = userStorage.getUserById(userId);

        Set<Long> filmLikes = film.getLikes();
        filmLikes.add(user.getId());
        log.info("Добавлен лайк от пользователя с id {} для фильма с id {}", userId, filmId);

        return film;
    }

    public void removeLike(Long filmId, Long userId) {
        Film film = filmStorage.getFilmById(filmId);
        User user = userStorage.getUserById(userId);

        Set<Long> filmLikes = film.getLikes();
        filmLikes.remove(user.getId());
        log.info("Удален лайк от пользователя с id {} для фильма с id {}", userId, filmId);
    }

    public Collection<Film> getPopularFilms(int count) {
        if (count <= 0) {
            log.warn("Параметр count меньше или равен 0");
            throw new ValidationException("Параметр count должен быть положительным числом");
        }

        return filmStorage.getFilms().stream().sorted(Comparator.comparingInt(f -> -f.getLikes().size()))
                .limit(count)
                .collect(Collectors.toList());
    }
}
