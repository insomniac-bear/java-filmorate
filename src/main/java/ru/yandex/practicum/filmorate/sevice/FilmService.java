package ru.yandex.practicum.filmorate.sevice;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;

import java.util.Collection;
import java.util.Comparator;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class FilmService {
    private final InMemoryFilmStorage filmStorage;
    private final InMemoryUserStorage userStorage;

    public Film addLike(Long filmId, Long userId) {
        Film film = filmStorage.getFilmById(filmId);
        Map<Long, User> users = userStorage.getUsers();

        if (!users.containsKey(userId)) {
            log.error("Пользователь с id {} не найден", userId);
            throw new NotFoundException("Пользователь с id " + userId +  " не найден");
        }

        Set<Long> filmLikes = film.getLikes();
        filmLikes.add(userId);
        log.trace("Добавлен лайк от пользователя с id {} для фильма с id {}", userId, filmId);

        return film;
    }

    public void removeLike(Long filmId, Long userId) {
        Film film = filmStorage.getFilmById(filmId);
        Map<Long, User> users = userStorage.getUsers();

        if (!users.containsKey(userId)) {
            log.error("Пользователь с id {} не найден", userId);
            throw new NotFoundException("Пользователь с id " + userId +  " не найден");
        }

        Set<Long> filmLikes = film.getLikes();
        filmLikes.remove(userId);
        log.trace("Удален лайк от пользователя с id {} для фильма с id {}", userId, filmId);
    }

    public Collection<Film> getPopularFilms(int count) {
        if (count <= 0) {
            log.warn("Параметр count меньше или равен 0");
            throw new ValidationException("Параметр count должен быть положительным числом");
        }

        return filmStorage.getFilms().values().stream()
                .sorted(Comparator.comparingInt(f -> -f.getLikes().size())
                )
                .limit(count)
                .collect(Collectors.toList());
    }
}
