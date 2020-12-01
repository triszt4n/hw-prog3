package pluto.database;

import pluto.app.PlutoConsole;
import pluto.exceptions.DatabaseDamagedException;
import pluto.exceptions.DatabaseNotFound;
import pluto.models.*;
import pluto.exceptions.ValidationException;
import pluto.models.helpers.CourseType;

import javax.json.Json;
import javax.json.JsonReader;
import java.io.*;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.stream.Collectors;

public class Database {
    private static final List<UserModel> users = new LinkedList<>();
    private static final List<SubjectModel> subjects = new LinkedList<>();
    private static final List<CourseModel> courses = new LinkedList<>();

    public static List<UserModel> getAllUsers() {
        return users;
    }

    public static List<InstructorModel> getAllInstructors() {
        return users.stream()
                .filter(u -> u.getTitle().equals("Instructor"))
                .map(u -> (InstructorModel)u)
                .collect(Collectors.toList());
    }

    public static List<SubjectModel> getAllSubjects() {
        return subjects;
    }

    public static List<CourseModel> getAllCourses() {
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

    public static void addUser(UserModel user) {
        users.add(user);
    }

    public static void addSubject(SubjectModel subject) {
        subjects.add(subject);
    }

    public static void addCourse(CourseModel course) {
        courses.add(course);
    }

    public static void removeUser(UserModel user) {
        users.remove(user);
    }

    public static void removeSubject(SubjectModel subject) {
        subjects.remove(subject);
    }

    public static void removeCourse(CourseModel course) {
        courses.remove(course);
    }

    public static List<CourseModel> getCoursesWherePlutoCodeIn(List<String> plutoCodes) {
        return courses.stream()
                .filter(c -> plutoCodes.contains(c.getPlutoCode()))
                .collect(Collectors.toList());
    }

    public static List<SubjectModel> getSubjectsWhereCreatorUser(UserModel user) {
        return subjects.stream()
                .filter(s -> s.getCoordinator() == user)
                .collect(Collectors.toList());
    }

    public static List<CourseModel> getCoursesWhereInstructor(InstructorModel instructor) {
        return courses.stream()
                .filter(c -> c.getInstructor() == instructor)
                .collect(Collectors.toList());
    }

    public static List<CourseModel> getCoursesWhereSubject(SubjectModel subject) {
        return courses.stream()
                .filter(c -> c.getSubject() == subject)
                .collect(Collectors.toList());
    }

    public static List<StudentModel> getStudentsWhereMyCoursesHas(CourseModel course) {
        return users.stream()
                .filter(u -> u.getTitle().equals("Student") && u.getMyCourses().contains(course))
                .map(u -> (StudentModel) u)
                .collect(Collectors.toList());
    }

    public static void removeCoursesWhereSubject(SubjectModel subject) {
        List<CourseModel> deletables = new LinkedList<>();
        courses.forEach(c -> {
            if (c.getSubject() == subject) {
                deletables.add(c);
            }
        });
        courses.removeAll(deletables);
    }

    public static void removeSubjectsAndTheirCoursesWhereCoordinator(UserModel user) {
        List<SubjectModel> deletables = new LinkedList<>();
        subjects.forEach(s -> {
            if (s.getCoordinator() == user) {
                deletables.add(s);
                removeCoursesWhereSubject(s);
            }
        });
        subjects.removeAll(deletables);
    }

    public static void loadFromJsonFiles() throws DatabaseDamagedException, DatabaseNotFound {
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

    public static void saveToJsonFiles() {

    }

    public static void seed() throws ValidationException, NoSuchAlgorithmException {
        StudentModel user1 = new StudentModel("piller.trisztan@simonyi.bme.hu", "Piller Trisztán", "123456", "1999-08-30", "Veszprém, Damjanich János utca 4/A");
        AdministratorModel user2 = new AdministratorModel("admin@pluto.com", "Adam Ministrator", "123456", "1989-08-20", "");
        InstructorModel user3 = new InstructorModel("tasnadi@math.bme.hu", "Tasnádi Tamás", "123456", "1972-03-14", "", false);
        InstructorModel user4 = new InstructorModel("gajdos@db.bme.hu", "Gajdos Sándor", "123456", "1961-10-10", "", true);
        InstructorModel user5 = new InstructorModel("youknowwho@slytherin.edu", "Lord Voldemort", "123456", "1972-12-20", "", true);
        InstructorModel user6 = new InstructorModel("gyakvez@best.com", "Kovács Gyak Vezető", "123456", "1989-09-11", "Budapest 1111, Vezér utca 1.", true);

        PlutoConsole.log("number of users in db: " + users.size());
        for (UserModel user : users) {
            PlutoConsole.taglessLog(user.toString());
        }

        SubjectModel subj1 = new SubjectModel("Adatbázisok", String.valueOf(5), "2/1/1/v", String.valueOf(3), user4, true);
        SubjectModel subj2 = new SubjectModel("Analízis 1 informatikusoknak", String.valueOf(6), "4/2/0/v", String.valueOf(1), user3, true);
        SubjectModel subj3 = new SubjectModel("Analízis 2 keresztfélév", String.valueOf(6), "4/2/0/f", String.valueOf(3), user3, true);
        SubjectModel subj4 = new SubjectModel("Sötét bűbájok", String.valueOf(4), "2/0/2/s", String.valueOf(5), user5, false);

        PlutoConsole.log("number of subjects in db: " + subjects.size());
        for (SubjectModel subject : subjects) {
            PlutoConsole.taglessLog(subject.toString());
        }

        CourseModel c1 = new CourseModel("E1", CourseType.LECTURE, String.valueOf(200), "This is the main lecture", subj1, user4);
        CourseModel c2 = new CourseModel("G1", CourseType.PRACTICE, String.valueOf(32), "", subj1, user6);
        CourseModel c3 = new CourseModel("L1", CourseType.LABORATORY, String.valueOf(16), "Only for students with signature", subj1, user6);
        CourseModel c4 = new CourseModel("E1", CourseType.LECTURE, String.valueOf(120), "Only for students of BME VIK", subj3, user3);

        List<String> myCourses = new LinkedList<>();
        myCourses.add(c1.getPlutoCode());
        myCourses.add(c3.getPlutoCode());

        for (UserModel user : users) {
            user.initCoursesAndSubjects(myCourses);
            PlutoConsole.log("init courses or subjects of user: " + user.getName());
            user.getMyCourses().forEach(c -> PlutoConsole.log("[MYCOURSE] " + c.getShortCode()));
        }

        PlutoConsole.log("number of courses in db: " + courses.size() + "\n");
        for (CourseModel course : courses) {
            PlutoConsole.taglessLog(course.toString());
            course.initStudents();
            PlutoConsole.log("init students of course: ", course.getShortCode());
            course.getStudents().forEach(s -> PlutoConsole.log("[ATTENDEE] " + s.getName()));
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
