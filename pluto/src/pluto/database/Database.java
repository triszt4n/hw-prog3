package pluto.database;

import io.github.cdimascio.dotenv.Dotenv;
import io.github.cdimascio.dotenv.DotenvException;
import pluto.app.PlutoConsole;
import pluto.exceptions.*;
import pluto.models.*;
import pluto.models.helpers.CourseType;
import pluto.models.helpers.UserType;

import javax.json.*;
import java.io.*;
import java.security.NoSuchAlgorithmException;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/***
 * Representative singleton class of a database in real life. Instead of a query language it uses methods to get the important data.
 * Utilizes lists for storing the data that can be used by the application.
 */
public class Database {
    private static final List<UserModel> users = new LinkedList<>();
    private static final List<SubjectModel> subjects = new LinkedList<>();
    private static final List<CourseModel> courses = new LinkedList<>();

    public static List<UserModel> getAllUsers() {
        return users;
    }

    public static List<InstructorModel> getAllInstructors() {
        return users.stream()
                .filter(u -> u.getType().equals(UserType.INSTRUCTOR))
                .map(u -> (InstructorModel)u)
                .collect(Collectors.toList());
    }

    public static List<SubjectModel> getAllSubjects() {
        return subjects;
    }

    public static List<CourseModel> getAllCourses() {
        return courses;
    }

    /***
     * Helper method for the other WherePlutoCode type search methods
     * @param pluto pluto code of the entity searched
     * @param list list of entities to be searched
     * @return the entity that has the pluto code
     */
    private static AbstractModel getEntityWherePlutoCode(String pluto, List<? extends AbstractModel> list) {
        return list.stream()
                .filter(e -> e.getPlutoCode().equals(pluto))
                .findFirst()
                .orElse(null);
    }

    public static UserModel getUserWherePlutoCode(String pluto) {
        UserModel result = (UserModel) getEntityWherePlutoCode(pluto, users);
        if (result == null) {
            PlutoConsole.log("getUserWherePlutoCode couldn't find User!", pluto);
        }
        return result;
    }

    public static SubjectModel getSubjectWherePlutoCode(String pluto) {
        SubjectModel result = (SubjectModel) getEntityWherePlutoCode(pluto, subjects);
        if (result == null) {
            PlutoConsole.log("getSubjectWherePlutoCode couldn't find Subject!", pluto);
        }
        return result;
    }

    public static CourseModel getCourseWherePlutoCode(String pluto) {
        CourseModel result = (CourseModel) getEntityWherePlutoCode(pluto, courses);
        if (result == null) {
            PlutoConsole.log("getCourseWherePlutoCode couldn't find Course!", pluto);
        }
        return result;
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

    public static List<StudentModel> getStudentsWhereMyCoursesHas(CourseModel course) {
        return users.stream()
                .filter(u -> u.getType().equals(UserType.STUDENT) && u.getMyCourses().contains(course))
                .map(u -> (StudentModel) u)
                .collect(Collectors.toList());
    }


    /***
     * Interprets the given json object and creates user entity
     * @param object json object to be read
     * @throws ValidationException if there is problem with json object
     */
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

    /***
     * Interprets the given json object and creates subject entity
     * @param object json object to be read
     * @throws ValidationException if there is problem with json object
     * @throws EntityNotFoundException if instructor read from json is not in the db
     */
    private static void manageSubjectJson(JsonObject object) throws ValidationException, EntityNotFoundException {
        new SubjectModel(object);
    }

    /***
     * Interprets the given json object and creates course entity
     * @param object json object to be read
     * @throws ValidationException if there is problem with json object
     * @throws EntityNotFoundException if instructor read from json is not in the db
     */
    private static void manageCourseJson(JsonObject object) throws ValidationException, EntityNotFoundException {
        new CourseModel(object);
    }

    /***
     * Realizes a json file reading
     * @param fileName json file to be read
     * @param managerFunction functional interface to apply it on the json object (for saving entity)
     * @throws DatabaseNotFound if no file is found
     * @throws DatabaseDamagedException if json is not okay
     */
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

    /***
     * Realizes the import from json files and makes the initialization of the associations in user and course entities
     * @throws DatabaseNotFound if no file is found
     * @throws DatabaseDamagedException if json is not okay
     */
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

    /***
     * Get an admin entity from the environment
     * @throws ValidationException if pluto code in non-conform
     * @throws NoSuchAlgorithmException if password hashing algorithm is badly set
     * @throws DotenvException if environment variable query fails
     */
    public static void loadAdmin() throws ValidationException, NoSuchAlgorithmException, DotenvException {
        Dotenv dotenv = Dotenv.load();
        String pluto = dotenv.get("PLUTO_ADMIN_CODE");
        try {
            UserModel.get(pluto);
            PlutoConsole.log("Administrator found in database!");
        } catch (EntityNotFoundException e) {
            PlutoConsole.log("Administrator not found in database, will fetch from environment...");
            String name = dotenv.get("PLUTO_ADMIN_NAME");
            String email = dotenv.get("PLUTO_ADMIN_EMAIL");
            String password = dotenv.get("PLUTO_ADMIN_PASSWORD");
            new AdministratorModel(pluto, email, name, password, "1970-01-01", "");
            PlutoConsole.log("Administrator created from environment variables! :-)");
        }
    }

    /***
     * Realizes a json file creation/modification
     * @param fileName json file to be read
     * @param list the entity list to be saved
     * @throws DatabasePersistenceException if there's something wrong going on with the IO
     */
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

    /***
     * Saves database
     * @throws DatabasePersistenceException if there's something wrong going on with the IO
     */
    public static void saveToJsonFiles() throws DatabasePersistenceException {
        saveToJsonFile("users", users);
        saveToJsonFile("subjects", subjects);
        saveToJsonFile("courses", courses);
        PlutoConsole.log("Database successfully saved to files! :-)");
    }

    /***
     * Seeds the database with consistent data to test (not real data)
     * @throws ValidationException if entity creation fails with validation
     * @throws NoSuchAlgorithmException if password hashing algorithm is badly set
     */
    public static void seed() throws ValidationException, NoSuchAlgorithmException {
        AdministratorModel user2 = new AdministratorModel("admin@pluto.com", "Adam Ministrator", "123456", "1989-08-20", "");
        InstructorModel user3 = new InstructorModel("macdonell@math.pluto.edu", "Thomas Tasnadius", "123456", "1972-03-14", "1 University Road, MS, Boston", true);
        InstructorModel user4 = new InstructorModel("gabriels@db.pluto.edu", "Sandor Gandolf", "123456", "1961-10-10", "4041 Ersel Street, MS, Levington", true);
        InstructorModel user5 = new InstructorModel("joska@pluto.edu", "Jozsef Kovacs", "123456", "1972-12-20", "", true);
        InstructorModel user6 = new InstructorModel("bazsika@pluto.com", "Balazs Takacs", "123456", "1989-09-11", "82931 Paris Str., TN, Crawford", true);
        new InstructorModel("harper@pluto.com", "Alan Harper", "123456", "1979-01-01", "Green Houses Main Str., CA, Los Angeles", false);

        SubjectModel subj1 = new SubjectModel("Databases", String.valueOf(5), "2/1/1/v", String.valueOf(3), user4, true);

        CourseModel c1 = new CourseModel("1", CourseType.LECTURE, String.valueOf(20), "This is the main lecture", subj1, user4);
        new CourseModel("2-RUS", CourseType.LECTURE, String.valueOf(20), "Taught in Russian", subj1, user4);
        CourseModel c2 = new CourseModel("P-1", CourseType.PRACTICE, String.valueOf(10), "", subj1, user3);
        new CourseModel("P-2", CourseType.PRACTICE, String.valueOf(10), "", subj1, user5);
        new CourseModel("P-3", CourseType.PRACTICE, String.valueOf(10), "", subj1, user4);
        CourseModel c3 = new CourseModel("L-1", CourseType.LABORATORY, String.valueOf(10), "Only for students with signature", subj1, user6);
        new CourseModel("L-2", CourseType.LABORATORY, String.valueOf(10), "Free to choose", subj1, user6);
        new CourseModel("L-3", CourseType.LABORATORY, String.valueOf(15), "Free to choose", subj1, user6);

        SubjectModel subj2 = new SubjectModel("Calculus 101", String.valueOf(6), "4/2/0/v", String.valueOf(1), user3, true);

        new CourseModel("L1", CourseType.LECTURE, String.valueOf(20), "Only for students of talent groups", subj2, user3);
        new CourseModel("L2", CourseType.LECTURE, String.valueOf(20), "Only for students of normal groups", subj2, user4);
        new CourseModel("PRAC", CourseType.PRACTICE, String.valueOf(40), "Only for students of normal groups", subj2, user4);

        SubjectModel subj3 = new SubjectModel("Calculus 201", String.valueOf(6), "4/2/0/f", String.valueOf(3), user3, true);

        CourseModel c5 = new CourseModel("L1", CourseType.LECTURE, String.valueOf(30), "", subj3, user3);
        new CourseModel("L2", CourseType.LECTURE, String.valueOf(10), "", subj3, user6);
        CourseModel c6 = new CourseModel("P1", CourseType.PRACTICE, String.valueOf(15), "", subj3, user3);
        new CourseModel("P2", CourseType.PRACTICE, String.valueOf(25), "", subj3, user6);

        SubjectModel subj4 = new SubjectModel("Computer Networks", String.valueOf(4), "2/0/2/s", String.valueOf(5), user5, false);

        CourseModel c8 = new CourseModel("LEC", CourseType.LECTURE, String.valueOf(22), "", subj4, user5);
        new CourseModel("L-HW", CourseType.LABORATORY, String.valueOf(3), "For students with signature", subj4, user5);
        CourseModel c9 = new CourseModel("L-NORM", CourseType.LABORATORY, String.valueOf(19), "", subj4, user5);

        SubjectModel subj5 = new SubjectModel("Information Theory", String.valueOf(4), "3/0/0/v", String.valueOf(3), user4, false);

        CourseModel c7 = new CourseModel("COURSE", CourseType.LECTURE, String.valueOf(12), "", subj5, user5);

        SubjectModel subj6 = new SubjectModel("Intro to Physics", String.valueOf(3), "0/3/0/v", String.valueOf(3), user2, false);

        new CourseModel("SEM", CourseType.SEMINAR, String.valueOf(6), "", subj6, user5);


        String[] rawCourses = new String[]{c1.getPlutoCode(), c2.getPlutoCode(), c3.getPlutoCode(), c8.getPlutoCode(), c9.getPlutoCode()};
        String[] rawCourses2 = new String[]{c1.getPlutoCode(), c2.getPlutoCode(), c3.getPlutoCode(), c8.getPlutoCode(), c9.getPlutoCode(), c5.getPlutoCode(), c6.getPlutoCode(), c7.getPlutoCode()};
        new StudentModel("154031@oxford.edu", "Ulrich Martens", "123456", "1998-01-01", "2399 Sundown Lane, Chatfield, TX", rawCourses);
        new StudentModel("111111@oxford.edu", "Jeremy Ingvar", "123456", "1997-10-30", "", rawCourses);
        new StudentModel("130411@oxford.edu", "Ursula Alois", "123456", "1997-10-30", "", rawCourses);
        new StudentModel("151151@oxford.edu", "Jozefina Bellany", "123456", "1997-10-30", "", rawCourses);
        StudentModel user1 = new StudentModel("191772@oxford.edu", "Tristan Mason", "123456", "1999-08-30", "21 University Str., NY, Albany", rawCourses2);

        for (UserModel user : users) {
            user.initCoursesAndSubjects();
        }

        for (CourseModel course : courses) {
            course.initStudents();
        }

        PlutoConsole.log("Database seed has successfully run! :-)", "Here's some users to start testing with:");
        PlutoConsole.taglessLog(user1.toString());
        PlutoConsole.taglessLog(user4.toString());
    }

    /***
     * Resets the db, but leaves admins alone.
     */
    public static void reset() {
        List<UserModel> nonAdmins = new LinkedList<>();
        users.forEach(u -> {
            if (!u.getType().equals(UserType.ADMINISTRATOR)) {
                nonAdmins.add(u);
            }
        });
        users.removeAll(nonAdmins);
        subjects.clear();
        courses.clear();
        PlutoConsole.log("Successfully deleted database");
    }
}
