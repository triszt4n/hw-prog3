package pluto.models;

import pluto.database.Database;
import pluto.exceptions.ValidationException;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class StudentModel extends UserModel {
    public StudentModel(String em, String na, String pw, String d, String addr) throws ValidationException, NoSuchAlgorithmException {
        super(em, na, pw, d, addr);
    }

    public StudentModel(String email, String name, String dob, String addr, String pluto, String encryptedPw, String salt) throws ValidationException {
        super(email, name, dob, addr, pluto, encryptedPw, salt);
    }

    @Override
    public String getTitle() {
        return "Student";
    }

    @Override
    public void initCoursesAndSubjects(List<String> plutoCodes) {
        myCourses = Database.getCoursesWherePlutoCodeIn(plutoCodes);
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
                )
                .add("courses", takenCoursesBuilder.build())
                .build();
    }
}
