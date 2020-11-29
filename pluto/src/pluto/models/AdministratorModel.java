package pluto.models;

import pluto.exceptions.ValidationException;

import java.security.NoSuchAlgorithmException;

public class AdministratorModel extends UserModel {
    public AdministratorModel(String em, String na, String pw, String d, String addr) throws ValidationException, NoSuchAlgorithmException {
        super(em, na, pw, d, addr);
    }

    @Override
    public String getTitle() {
        return "Administrator";
    }

    @Override
    public String getStatus() {
        return getTitle();
    }
}
