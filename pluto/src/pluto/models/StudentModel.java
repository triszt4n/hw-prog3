package pluto.models;

import pluto.exceptions.ValidationException;

import java.security.NoSuchAlgorithmException;

public class StudentModel extends UserModel {
    public StudentModel(String em, String na, String pw, String d, String addr) throws ValidationException, NoSuchAlgorithmException {
        super(em, na, pw, d, addr);
    }

    @Override
    public String getTitle() {
        return "Student";
    }

    @Override
    protected void initMySubjects() {
        super.initMySubjects();
    }

    @Override
    protected void initMyCourses() {
        super.initMyCourses();
    }
}
