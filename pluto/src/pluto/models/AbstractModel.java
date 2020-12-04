package pluto.models;

import javax.json.JsonObject;
import java.security.SecureRandom;

/***
 * Abstract model class in the MVC design pattern.
 * Makes it possible for the the application to reach the database and manage the entities with the help of this class.
 */
public abstract class AbstractModel {
    /***
     * Unique identifier for an entity in the pluto system
     */
    protected String plutoCode;

    protected static final int PLUTO_CODE_LENGTH = 12;

    protected static final String PLUTO_CODE_CHARSET = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";

    /***
     * Simple getter
     * @return the unique pluto code
     */
    public String getPlutoCode() {
        return plutoCode;
    }

    /***
     * De facto unique identifier generator (only uses SecureRandom).
     * Creates a unique pluto code for the entity.
     */
    protected void generatePlutoCode() {
        SecureRandom rnd = new SecureRandom();

        StringBuilder sb = new StringBuilder(PLUTO_CODE_LENGTH);
        for (int i = 0; i < PLUTO_CODE_LENGTH; i++) {
            sb.append(PLUTO_CODE_CHARSET.charAt(rnd.nextInt(PLUTO_CODE_CHARSET.length())));
        }
        plutoCode = sb.toString();
    }

    /***
     * Saver method, that puts the entity into the database
     */
    protected abstract void save();

    /***
     * Compiles the entity into a json object for saving to json file.
     * @return formed json object of the entity
     */
    public abstract JsonObject jsonify();
}
