package ru.yandex.practicum.filmorate.sevice;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;

public interface Recommendation {

    Film addLike(Long filmId, Long userId);

    void removeLike(Long filmId, Long userId);

    Collection<Film> getPopularFilms(int count);
}
