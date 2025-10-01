package ru.yandex.practicum.filmorate.repository;

import java.util.List;
import java.util.Set;

public interface LikeRepository {
    void addLike(Long filmId, Long userId);

    void removeLike(Long filmId, Long userId);

    Set<Long> getFilmLikes(Long filmId);

    List<Long> getPopularFilmIds(int limit);
}
