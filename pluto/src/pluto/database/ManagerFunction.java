package pluto.database;

import pluto.exceptions.EntityNotFoundException;
import pluto.exceptions.ValidationException;

/***
 * A "function storage" used in the database for json interpretation
 * @param <T> generally JsonObject
 */
@FunctionalInterface
public interface ManagerFunction<T> {
    void apply(T t) throws ValidationException, EntityNotFoundException;
}
