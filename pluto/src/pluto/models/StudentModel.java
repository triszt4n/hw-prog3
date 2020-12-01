package pluto.models;

import pluto.database.Database;
import pluto.exceptions.ValidationException;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonValue;
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
                .map(JsonValue::toString)
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
        myCourses.forEach(c -> c.removeStudent(this));
    }

    @Override
    public JsonObject jsonify() {
        JsonArrayBuilder takenCoursesBuilder = Json.createArrayBuilder();
        for (CourseModel c : myCourses) {
            takenCoursesBuilder.add(c.getPlutoCode());
        }

        return Json.createObjectBuilder()
                .add("type", "Student")
                .add("pluto", plutoCode)
                .add("email", email)
                .add("name", name)
                .add("dob", unparsedDob)
                .add("address", address)
                .add("credentials", Json.createObjectBuilder()
                        .add("password", Arrays.toString(encryptedPassword))
                        .add("salt", Arrays.toString(salt))
                        .build()
                )
                .add("courses", takenCoursesBuilder.build())
                .build();
    }
}
