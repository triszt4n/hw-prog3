package pluto.models;

import pluto.models.database.Database;
import pluto.models.exceptions.ValidationException;

public abstract class AbstractModel {
    protected long id;
    protected static final Database db = new Database();

    protected void validate() throws ValidationException { }
    protected void save() throws ValidationException { }

    public static void delete(long id) { }
}
