package pluto.models.database;

import pluto.models.CourseModel;
import pluto.models.SubjectModel;
import pluto.models.UserModel;

import java.util.LinkedList;
import java.util.List;

public class Database {
    private static List<UserModel> users = new LinkedList<>();
    private static List<SubjectModel> subjects = new LinkedList<>();
    private static List<CourseModel> courses = new LinkedList<>();
    private static boolean isInitialized = false;

    public class DatabaseDamagedException extends Exception {
        public DatabaseDamagedException(String msg) {
            super(msg);
        }
    }

    public class DatabaseNotInitializedException extends Exception {
        public DatabaseNotInitializedException(String msg) {
            super(msg);
        }
    }

    public class DatabaseNotFound extends Exception {
        public DatabaseNotFound(String msg) {
            super(msg);
        }
    }

    public static void loadAll() throws DatabaseDamagedException, DatabaseNotFound {

        isInitialized = true;
    }
}
