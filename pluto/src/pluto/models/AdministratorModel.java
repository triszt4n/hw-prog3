package pluto.models;

import pluto.database.Database;
import pluto.exceptions.ValidationException;

import java.security.NoSuchAlgorithmException;
import java.util.LinkedList;
import java.util.List;

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

    @Override
    public void initMyCoursesAndSubjects(List<String> plutoCodes) {
        myCourses = new LinkedList<>();
        mySubjects = Database.getSubjectsWhereCreatorUser(this);
    }
}
