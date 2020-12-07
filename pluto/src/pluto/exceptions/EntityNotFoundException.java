package pluto.exceptions;

/***
 * Exception used for letting the controller know if the queried entity is existent in database
 */
public class EntityNotFoundException extends Exception {
    public EntityNotFoundException(String msg) {
        super(msg);
    }
}
