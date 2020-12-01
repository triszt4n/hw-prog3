package pluto.database;

import pluto.exceptions.EntityNotFoundException;
import pluto.exceptions.ValidationException;

@FunctionalInterface
public interface ManagerFunction<T> {
    void apply(T t) throws ValidationException, EntityNotFoundException;
}
