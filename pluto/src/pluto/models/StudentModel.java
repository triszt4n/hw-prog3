package pluto.models;

import pluto.database.Database;
import pluto.exceptions.ValidationException;

import java.security.NoSuchAlgorithmException;
import java.util.List;

public class StudentModel extends UserModel {
    public StudentModel(String em, String na, String pw, String d, String addr) throws ValidationException, NoSuchAlgorithmException {
        super(em, na, pw, d, addr);
    }

    @Override
    public String getTitle() {
        return "Student";
    }

    @Override
    public void initMyCoursesAndSubjects(List<String> plutoCodes) {
        myCourses = Database.getCoursesWherePlutoCodeIn(plutoCodes);
    }
}
