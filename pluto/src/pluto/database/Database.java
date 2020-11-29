package pluto.database;

import pluto.app.PlutoConsole;
import pluto.exceptions.DatabaseDamagedException;
import pluto.exceptions.DatabaseNotFound;
import pluto.models.*;
import pluto.exceptions.ValidationException;

import javax.json.Json;
import javax.json.JsonReader;
import java.io.*;
import java.security.NoSuchAlgorithmException;
import java.util.*;

public class Database {
    private static final List<UserModel> users = new LinkedList<>();
    private static final List<SubjectModel> subjects = new LinkedList<>();
    private static final List<CourseModel> courses = new LinkedList<>();

    public static List<UserModel> getUsers() {
        return users;
    }
    public static List<SubjectModel> getSubjects() {
        return subjects;
    }
    public static List<CourseModel> getCourses() {
        return courses;
    }

    private static AbstractModel getEntityWherePlutoCode(String pluto, List<? extends AbstractModel> list) {
        AbstractModel result = list.stream()
                .filter(e -> e.getPlutoCode().equals(pluto))
                .findFirst()
                .orElse(null);
        if (result == null) {
            PlutoConsole.log("getEntityWherePlutoCode couldn't find Entity!");
        }
        return result;
    }

    public static UserModel getUserWherePlutoCode(String pluto) {
        return (UserModel) getEntityWherePlutoCode(pluto, users);
    }

    public static SubjectModel getSubjectWherePlutoCode(String pluto) {
        return (SubjectModel) getEntityWherePlutoCode(pluto, subjects);
    }

    public static CourseModel getCourseWherePlutoCode(String pluto) {
        return (CourseModel) getEntityWherePlutoCode(pluto, courses);
    }

    private static AbstractModel getEntityWhereIndex(int index, List<? extends AbstractModel> list) {
        AbstractModel result;
        try {
            result = list.get(index);
        } catch (IndexOutOfBoundsException e) {
            PlutoConsole.log("getEntityWhereIndex: " + e.getMessage());
            result = null;
        }
        if (result == null) {
            PlutoConsole.log("getEntityWhereIndex couldn't find Entity!");
        }
        return result;
    }

    public static UserModel getUserWhereIndex(int index) {
        return (UserModel) getEntityWhereIndex(index, users);
    }

    public static SubjectModel getSubjectWhereIndex(int index) {
        return (SubjectModel) getEntityWhereIndex(index, subjects);
    }

    public static CourseModel getCourseWhereIndex(int index) {
        return (CourseModel) getEntityWhereIndex(index, courses);
    }

    public static int getCurrentIndexOfUser(UserModel user) {
        return users.indexOf(user);
    }

    public static int getCurrentIndexOfSubject(SubjectModel subject) {
        return subjects.indexOf(subject);
    }

    public static int getCurrentIndexOfCourse(CourseModel course) {
        return courses.indexOf(course);
    }

    private static boolean deleteEntityWhereIndex(int index, List<? extends AbstractModel> list) {
        try {
            list.remove(index);
        } catch (IndexOutOfBoundsException e) {
            PlutoConsole.log("deleteEntityWhereIndex: " + e.getMessage());
            return false;
        }
        return true;
    }

    public static boolean deleteUserWhereIndex(int index) {
        return deleteEntityWhereIndex(index, users);
    }

    public static boolean deleteSubjectWhereIndex(int index) {
        return deleteEntityWhereIndex(index, subjects);
    }

    public static boolean deleteCourseWhereIndex(int index) {
        return deleteEntityWhereIndex(index, courses);
    }

    public static void addUser(UserModel user) {
        users.add(user);
    }

    public static void addSubject(SubjectModel subject) {
        subjects.add(subject);
    }

    public static void addCourse(CourseModel course) {
        courses.add(course);
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
    }

    public static void saveAll() {

    }

    public static void seed() throws ValidationException, NoSuchAlgorithmException {
        StudentModel user1 = new StudentModel("trisz@gmail.com", "Piller Trisztán", "123456", "1999-08-30", "Veszprém, Damjanich János utca 4/A");
        AdministratorModel user2 = new AdministratorModel("admin@gmail.com", "Mr. Admin", "123456", "1989-08-20", "");
        InstructorModel user3 = new InstructorModel("tasnadi@gmail.com", "Tasnadi Tamas", "123456", "1972-03-14", "", false);

        PlutoConsole.log("number of users in db: " + users.size());
        for (UserModel user : users) {
            PlutoConsole.taglessLog(user.toString());
        }

        PlutoConsole.log("Database seed has successfully run");
    }

    public static void reset() {
        List<UserModel> nonAdmins = new LinkedList<>();
        users.forEach(u -> {
            if (u.getTitle() != "Administrator") {
                nonAdmins.add(u);
            }
        });
        users.removeAll(nonAdmins);
        subjects.clear();
        courses.clear();
        PlutoConsole.log("Successfully deleted database");
    }
}
