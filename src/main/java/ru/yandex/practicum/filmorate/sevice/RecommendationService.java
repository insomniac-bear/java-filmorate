package ru.yandex.practicum.filmorate.sevice;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.repository.JdbcFilmRepository;
import ru.yandex.practicum.filmorate.repository.JdbcLikeRepository;
import ru.yandex.practicum.filmorate.repository.JdbcUserRepository;

@Slf4j
@Service
@AllArgsConstructor
public class RecommendationService implements Recommendation {
    private final JdbcLikeRepository likeRepository;
    private final JdbcFilmRepository filmRepository;
    private final JdbcUserRepository userRepository;

    @Override
    public void addLike(Long filmId, Long userId) {
        Film film = filmRepository.findById(filmId)
                .orElseThrow(() -> {
                    log.error("Фильм с id {} не найден", filmId);
                    return new NotFoundException("Фильм с id: " + filmId + " не найден");
                });

        User user = userRepository.findById(filmId)
                .orElseThrow(() -> {
                    log.error("Пользователь с id {} не найден", userId);
                    return new NotFoundException("Пользователь с id: " + userId + " не найден");
                });

        likeRepository.addLike(film.getId(), user.getId());
    }

    @Override
    public void removeLike(Long filmId, Long userId) {
        Film film = filmRepository.findById(filmId)
                .orElseThrow(() -> {
                    log.error("Фильм с id {} не найден", filmId);
                    return new NotFoundException("Фильм с id: " + filmId + " не найден");
                });

        User user = userRepository.findById(filmId)
                .orElseThrow(() -> {
                    log.error("Пользователь с id {} не найден", userId);
                    return new NotFoundException("Пользователь с id: " + userId + " не найден");
                });

        likeRepository.removeLike(film.getId(), user.getId());
    }
}
