package pluto.models;

public class StudentModel extends UserModel {
    public StudentModel(String em, String na, String pw, String d, String addr) throws ValidationException {
        super(em, na, pw, d, addr);
    }
}
