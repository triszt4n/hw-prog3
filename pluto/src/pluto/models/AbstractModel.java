package pluto.models;

import pluto.models.database.Database;

public abstract class AbstractModel {
    protected long id;
    protected static final Database db = new Database();

    // used for constructor to throw exception if there's conformity problems
    private void validate() { }

    // used for saving into db if validation okay
    private void save() { }

    public static AbstractModel get(long id) {
        return null;
    }

    public static void delete(long id) { }
}
