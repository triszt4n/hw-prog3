package pluto.database;

import io.github.cdimascio.dotenv.Dotenv;
import io.github.cdimascio.dotenv.DotenvException;
import pluto.app.PlutoConsole;
import pluto.exceptions.*;
import pluto.models.*;
import pluto.models.helpers.CourseType;

import javax.json.*;
import java.io.*;
import java.security.NoSuchAlgorithmException;
import java.util.LinkedList;
import java.util.List;
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

    private static void manageUserJson(JsonObject object) throws ValidationException {
        if (object.getString("type").equals("Student")) {
            new StudentModel(object);
        }
        else if (object.getString("type").equals("Instructor")) {
            new InstructorModel(object);
        }
        else {
            new AdministratorModel(object);
        }
    }

    private static void manageSubjectJson(JsonObject object) throws ValidationException, EntityNotFoundException {
        new SubjectModel(object);
    }

    private static void manageCourseJson(JsonObject object) throws ValidationException, EntityNotFoundException {
        new CourseModel(object);
    }

    private static void loadFromJsonFile(String fileName, ManagerFunction<JsonObject> managerFunction) throws DatabaseNotFound, DatabaseDamagedException {
        JsonReader reader = null;
        try {
            File source = new File("data" + File.separator + fileName + ".json");
            reader = Json.createReader(new FileReader(source));

            JsonArray resultArray = reader.readArray();
            for (JsonValue value : resultArray) {
                managerFunction.apply(value.asJsonObject());
            }
        } catch (FileNotFoundException fileNotFoundException) {
            fileNotFoundException.printStackTrace();
            throw new DatabaseNotFound("Database connection failed, files not found!");
        } catch (ValidationException | EntityNotFoundException exception) {
            exception.printStackTrace();
            throw new DatabaseDamagedException("Json object is corrupted, database damaged, couldn't be read!");
        }
        finally {
            if (reader != null) reader.close();
        }
    }

    public static void loadFromJsonFiles() throws DatabaseDamagedException, DatabaseNotFound {
        loadFromJsonFile("users", Database::manageUserJson);
        loadFromJsonFile("subjects", Database::manageSubjectJson);
        loadFromJsonFile("courses", Database::manageCourseJson);
        for (UserModel user : users) {
            user.initCoursesAndSubjects();
        }
        for (CourseModel course : courses) {
            course.initStudents();
        }
        PlutoConsole.log("Database successfully loaded from files! :-)");
    }

    public static void loadFromDotEnv() throws ValidationException, NoSuchAlgorithmException, DotenvException {
        Dotenv dotenv = Dotenv.load();
        String pluto = dotenv.get("PLUTO_ADMIN_CODE");
        String name = dotenv.get("PLUTO_ADMIN_NAME");
        String email = dotenv.get("PLUTO_ADMIN_EMAIL");
        String password = dotenv.get("PLUTO_ADMIN_PASSWORD");
        new AdministratorModel(pluto, email, name, password, "1970-01-01", "");
        PlutoConsole.log("Administrator successfully extracted from environment variables! :-)");
    }

    private static void saveToJsonFile(String fileName, List<? extends AbstractModel> list) throws DatabasePersistenceException {
        JsonWriter writer = null;
        try {
            File target = new File("data" + File.separator + fileName + ".json");
            boolean newFileCreated = target.createNewFile();
            PlutoConsole.log(newFileCreated? "New file created: " + fileName + ".json" : "File already exists: " + fileName + ".json");

            writer = Json.createWriter(new FileWriter(target));
            JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
            list.forEach(item -> arrayBuilder.add(item.jsonify()));
            writer.writeArray(arrayBuilder.build());
        } catch (IOException e) {
            e.printStackTrace();
            throw new DatabasePersistenceException("Database couldn't save data! Persistence lost!");
        } finally {
            if (writer != null) writer.close();
        }
    }

    public static void saveToJsonFiles() throws DatabasePersistenceException {
        saveToJsonFile("users", users);
        saveToJsonFile("subjects", subjects);
        saveToJsonFile("courses", courses);
        PlutoConsole.log("Database successfully saved to files! :-)");
    }

    public static void seed() throws ValidationException, NoSuchAlgorithmException {
        AdministratorModel user2 = new AdministratorModel("admin@pluto.com", "Adam Ministrator", "123456", "1989-08-20", "");
        InstructorModel user3 = new InstructorModel("tasnadi@math.bme.hu", "Tasnádi Tamás", "123456", "1972-03-14", "", false);
        InstructorModel user4 = new InstructorModel("gajdos@db.bme.hu", "Gajdos Sándor", "123456", "1961-10-10", "", true);
        InstructorModel user5 = new InstructorModel("youknowwho@slytherin.edu", "Lord Voldemort", "123456", "1972-12-20", "", true);
        InstructorModel user6 = new InstructorModel("gyakvez@best.com", "Kovács Gyak Vezető", "123456", "1989-09-11", "Budapest 1111, Vezér utca 1.", true);

        SubjectModel subj1 = new SubjectModel("Adatbázisok", String.valueOf(5), "2/1/1/v", String.valueOf(3), user4, true);
        SubjectModel subj2 = new SubjectModel("Analízis 1 informatikusoknak", String.valueOf(6), "4/2/0/v", String.valueOf(1), user3, true);
        SubjectModel subj3 = new SubjectModel("Analízis 2 keresztfélév", String.valueOf(6), "4/2/0/f", String.valueOf(3), user3, true);
        SubjectModel subj4 = new SubjectModel("Sötét bűbájok", String.valueOf(4), "2/0/2/s", String.valueOf(5), user5, false);

        CourseModel c1 = new CourseModel("E1", CourseType.LECTURE, String.valueOf(200), "This is the main lecture", subj1, user4);
        CourseModel c2 = new CourseModel("G1", CourseType.PRACTICE, String.valueOf(32), "", subj1, user6);
        CourseModel c3 = new CourseModel("L1", CourseType.LABORATORY, String.valueOf(16), "Only for students with signature", subj1, user6);
        CourseModel c4 = new CourseModel("E1", CourseType.LECTURE, String.valueOf(120), "Only for students of BME VIK", subj3, user3);

        String[] rawCourses = new String[]{c1.getPlutoCode(), c3.getPlutoCode()};
        StudentModel user1 = new StudentModel("piller.trisztan@simonyi.bme.hu", "Piller Trisztán", "123456", "1999-08-30", "Veszprém, Damjanich János utca 4/A", rawCourses);

        for (UserModel user : users) {
            user.initCoursesAndSubjects();
        }

        for (CourseModel course : courses) {
            course.initStudents();
        }

        PlutoConsole.log("Database seed has successfully run! :-)", "Here's one student to start testing with:");
        PlutoConsole.taglessLog(user1.toString());
    }

    public static void reset() {
        List<UserModel> nonAdmins = new LinkedList<>();
        users.forEach(u -> {
            if (!u.getTitle().equals("Administrator")) {
                nonAdmins.add(u);
            }
        });
        users.removeAll(nonAdmins);
        subjects.clear();
        courses.clear();
        PlutoConsole.log("Successfully deleted database");
    }
}
