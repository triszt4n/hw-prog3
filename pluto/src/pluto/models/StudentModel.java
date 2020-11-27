package pluto.models;

import pluto.models.exceptions.ValidationException;

public class StudentModel extends UserModel {


    public StudentModel(String em, String na, String pw, String d, String addr) throws ValidationException {
        super(em, na, pw, d, addr);
    }

    @Override
    public String getTitle() {
        return "Student";
    }
}
