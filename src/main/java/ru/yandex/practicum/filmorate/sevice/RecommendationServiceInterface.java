package ru.yandex.practicum.filmorate.sevice;

public interface RecommendationServiceInterface {

    void addLike(Long filmId, Long userId);

    void removeLike(Long filmId, Long userId);
}
