package pluto.models.database;

import pluto.app.PlutoConsole;
import pluto.models.*;
import pluto.models.exceptions.ValidationException;

import javax.json.Json;
import javax.json.JsonReader;
import java.io.*;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

public class Database {
    private static List<UserModel> users = new LinkedList<>();
    private static List<SubjectModel> subjects = new LinkedList<>();
    private static List<CourseModel> courses = new LinkedList<>();
    private static boolean isInitialized = false;

    public static class DatabaseDamagedException extends Exception {
        public DatabaseDamagedException(String msg) {
            super(msg);
        }
    }

    public class DatabaseNotInitializedException extends Exception {
        public DatabaseNotInitializedException(String msg) {
            super(msg);
        }
    }

    public static class DatabaseNotFound extends Exception {
        public DatabaseNotFound(String msg) {
            super(msg);
        }
    }

    public static UserModel getUserWherePlutoCode(String pluto) {
        UserModel user = users.stream()
                .filter(u -> { return u.getPlutoCode().equals(pluto); })
                .findFirst()
                .orElse(null);
        if (user == null) {
            PlutoConsole.log("getUserWherePlutoCode couldn't find User!");
        }
        return user;
    }

    public static void addUser(UserModel user) {
        users.add(user);
    }

    public static void loadAll() throws DatabaseDamagedException, DatabaseNotFound {
        JsonReader readerUsers = null;
        JsonReader readerSubjects = null;
        JsonReader readerCourses = null;
        try {
            readerUsers = Json.createReader(new FileReader(new File("users.json")));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            throw new DatabaseNotFound("Database connection failed, files not found!");
        }



        readerUsers.close();

        try {
            readerSubjects = Json.createReader(new FileReader(new File("subjects.json")));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            throw new DatabaseNotFound("Database connection failed, files not found!");
        }



        readerSubjects.close();

        try {
            readerCourses = Json.createReader(new FileReader(new File("courses.json")));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            throw new DatabaseNotFound("Database connection failed, files not found!");
        }



        readerCourses.close();

        isInitialized = true;
    }

    public static void saveAll() {

    }

    public static void seed() {
        try {
            StudentModel user1 = new StudentModel("trisz@gmail.com", "Piller Trisztán", "123456", "1999-08-30", "Veszprém, Damjanich János utca 4/A");
            AdministratorModel user2 = new AdministratorModel("admin@gmail.com", "Mr. Admin", "123456", "1989-08-20", "");
            InstructorModel user3 = new InstructorModel("tasnadi@gmail.com", "Tasnadi Tamas", "123456", "1972-03-14", "", false);
        } catch (ValidationException e) {
            e.printStackTrace();
        }
        PlutoConsole.log("number of users in db: " + users.size());
        for (UserModel user : users) {
            PlutoConsole.taglessLog(user.toString());
        }
    }
}
