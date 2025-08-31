package ru.yandex.practicum.filmorate.exception;

public class DuplicatedDataException extends IllegalArgumentException {
    public DuplicatedDataException(String message) {
        super(message);
    }
}
