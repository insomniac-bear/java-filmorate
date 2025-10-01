package ru.yandex.practicum.filmorate.sevice;

import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

@Service
public abstract class StorageService<E> {

    public abstract List<E> getAll();

    public abstract E getById(Long id);

    public abstract E create(E entity);

    public abstract E update(E entity);
}
