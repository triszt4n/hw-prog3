package pluto.models.database;

import pluto.helpers.PlutoConsole;
import pluto.models.CourseModel;
import pluto.models.SubjectModel;
import pluto.models.UserModel;

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
        UserModel user = null;
        try {
            user = users.get(
                    Collections.binarySearch(users, new UserModel(pluto), new Comparator<UserModel>() {
                        @Override
                        public int compare(UserModel o1, UserModel o2) {
                            return o1.getPlutoCode().compareTo(o2.getPlutoCode());
                        }
                    })
            );
        } catch (IndexOutOfBoundsException e) {
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
}
