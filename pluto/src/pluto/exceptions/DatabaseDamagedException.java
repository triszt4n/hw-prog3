package pluto.exceptions;

public class DatabaseDamagedException extends Exception {
    public DatabaseDamagedException(String msg) {
        super(msg);
    }
}
