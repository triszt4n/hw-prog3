package pluto.models;

public class AdministratorModel extends UserModel {
    public AdministratorModel(String em, String na, String pw, String d, String addr) throws ValidationException {
        super(em, na, pw, d, addr);
    }
}
