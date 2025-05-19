package com.highload.architect.soc.network.dao;

import java.util.UUID;

public interface AbstractDao<T> {
    T getById(UUID id);

    void save(T entity);
}
