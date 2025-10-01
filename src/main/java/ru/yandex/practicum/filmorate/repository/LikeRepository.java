package ru.yandex.practicum.filmorate.repository;

import java.util.List;

public interface LikeRepository {
    void addLike(Long filmId, Long userId);

    void removeLike(Long filmId, Long userId);

    List<Long> getPopularFilmIds(int limit);
}
