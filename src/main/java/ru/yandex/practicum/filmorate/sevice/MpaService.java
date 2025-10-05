package ru.yandex.practicum.filmorate.sevice;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.repository.JdbcMpaRepository;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class MpaService implements MpaServiceInterface {
    private final JdbcMpaRepository repository;

    @Override
    public List<Mpa> getAll() {
        return repository.getAll();
    }

    @Override
    public Mpa getById(Long mpaId) {
        if (mpaId == null) {
            log.error("Не передан id для получения mpa");
            throw new IllegalArgumentException("id является обязательным параметром");
        }

        return repository.getById(mpaId)
                .orElseThrow(() -> {
                    log.error("Рейтинг с id {} не найден", mpaId);
                    return new NotFoundException("Рейтинг с id " + mpaId + " не найдег");
                });
    }
}
