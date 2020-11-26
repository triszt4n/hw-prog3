package pluto.models;

import pluto.controllers.AbstractController;
import pluto.models.database.Database;

public abstract class AbstractModel {
    private long id;
    private static final Database db = new Database();
    private AbstractController myController;

    // used for constructor to throw exception if there's conformity problems
    public void validate() {

    }

    public static AbstractModel get(long id) {
        return null;
    }

    public static void delete(long id) {

    }
}
