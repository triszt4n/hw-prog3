package pluto.exceptions;

/***
 * Exception class for letting the controller know if authorization failed
 */
public class AuthorizationException extends Exception {
    public AuthorizationException(String msg) {
        super(msg);
    }
}
