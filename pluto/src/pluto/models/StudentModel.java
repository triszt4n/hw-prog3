package pluto.models;

import pluto.database.Database;
import pluto.exceptions.ValidationException;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class StudentModel extends UserModel {
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
    public String getTitle() {
        return "Student";
    }

    @Override
    public void initCoursesAndSubjects() {
        myCourses = Database.getCoursesWherePlutoCodeIn(coursePlutoCodes);
        mySubjects = myCourses.stream()
                .map(CourseModel::getSubject)
                .distinct()
                .collect(Collectors.toList());
    }

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
