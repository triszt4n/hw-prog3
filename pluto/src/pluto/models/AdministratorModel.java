package pluto.models;

import pluto.models.exceptions.ValidationException;

public class AdministratorModel extends UserModel {
    public AdministratorModel(String em, String na, String pw, String d, String addr) throws ValidationException {
        super(em, na, pw, d, addr);
    }

    @Override
    public String getTitle() {
        return "Administrator";
    }
}
