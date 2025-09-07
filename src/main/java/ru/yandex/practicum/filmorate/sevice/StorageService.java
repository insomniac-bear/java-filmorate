package ru.yandex.practicum.filmorate.sevice;

import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public abstract class StorageService<E> {

    public abstract Collection<E> getAll();

    public abstract E getById(Long id);

    public abstract E create(E entity);

    public abstract E update(E entity);
}
