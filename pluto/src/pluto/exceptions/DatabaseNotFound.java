package pluto.exceptions;

/***
 * Database management exception for letting the outer world know if the database files are non-existent.
 */
public class DatabaseNotFound extends Exception {
    public DatabaseNotFound(String msg) {
        super(msg);
    }
}
