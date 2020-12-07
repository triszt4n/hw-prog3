package pluto.exceptions;

/***
 * Database management exception for letting the outer world know if the database files couldn't be properly loaded.
 */
public class DatabaseDamagedException extends Exception {
    public DatabaseDamagedException(String msg) {
        super(msg);
    }
}
