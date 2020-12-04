package pluto.models;

import pluto.database.Database;
import pluto.exceptions.ValidationException;
import pluto.models.helpers.UserType;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/***
 * Representation of a student in the system.
 * They can manage their courses that they've taken and see subjects of those.
 */
public class StudentModel extends UserModel {
    /***
     * Helper list of the json objects listed pluto codes of courses taken
     */
    private final List<String> coursePlutoCodes;

    public StudentModel(String em, String na, String pw, String d, String addr) throws ValidationException, NoSuchAlgorithmException {
        super(em, na, pw, d, addr);
        coursePlutoCodes = new LinkedList<>();
    }

    public StudentModel(String em, String na, String pw, String d, String addr, String[] coursePlutoCodes) throws ValidationException, NoSuchAlgorithmException {
        super(em, na, pw, d, addr);
        this.coursePlutoCodes = Arrays.asList(coursePlutoCodes);
    }

    public StudentModel(JsonObject json) throws ValidationException {
        super(json);
        this.coursePlutoCodes = json.getJsonArray("courses").stream()
                .map(item -> {
                    String value = item.toString();
                    return value.substring(1, value.length() - 1);
                })
                .collect(Collectors.toList());
    }

    @Override
    public UserType getType() {
        return UserType.STUDENT;
    }

    /***
     * Sets the courses taken and subjects of those
     */
    @Override
    public void initCoursesAndSubjects() {
        myCourses = Database.getCoursesWherePlutoCodeIn(coursePlutoCodes);
        mySubjects = myCourses.stream()
                .map(CourseModel::getSubject)
                .distinct()
                .collect(Collectors.toList());
    }

    /***
     * Method removes the student from the student list of the courses
     */
    @Override
    public void manageSubjectsAndCoursesBeforeDelete() {
        for (CourseModel c : myCourses) {
            c.removeStudent(this);
        }
    }

    @Override
    public JsonObject jsonify() {
        JsonObject userObject = super.jsonify();

        JsonArrayBuilder takenCoursesBuilder = Json.createArrayBuilder();
        for (CourseModel c : myCourses) {
            takenCoursesBuilder.add(c.getPlutoCode());
        }

        return Json.createObjectBuilder()
                .add("type", "Student")
                .add("courses", takenCoursesBuilder.build())
                .add("details", userObject)
                .build();
    }
}
