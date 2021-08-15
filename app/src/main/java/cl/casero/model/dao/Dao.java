package cl.casero.model.dao;

import java.util.List;

public interface Dao<T> {
    void create(T object);

    List<T> read();

    void update(T object);

    void delete(Number id);

    T readById(Number id);

    List<T> readBy(String filter);
}
