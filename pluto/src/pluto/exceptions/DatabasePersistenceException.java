package pluto.exceptions;

/***
 * Database management exception for letting the outer world know if the saving of the database ran into IO problems.
 */
public class DatabasePersistenceException extends Exception {
    public DatabasePersistenceException(String msg) {
        super(msg);
    }
}
