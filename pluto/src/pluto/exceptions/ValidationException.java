package pluto.exceptions;

/***
 * Exception used if entity validation fails.
 */
public class ValidationException extends Exception {
    public ValidationException(String msg) {
        super(msg);
    }
}
