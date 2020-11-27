package pluto.models;

public class InstructorModel extends UserModel {
    private boolean isAllowed;

    public InstructorModel(String em, String na, String pw, String d, String addr) throws ValidationException {
        super(em, na, pw, d, addr);
    }
}
