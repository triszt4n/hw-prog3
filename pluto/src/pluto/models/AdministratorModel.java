package pluto.models;

import pluto.database.Database;
import pluto.exceptions.EntityNotFoundException;
import pluto.exceptions.ValidationException;

import java.security.NoSuchAlgorithmException;
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
    public void initCoursesAndSubjects(List<String> plutoCodes) {
        mySubjects = Database.getSubjectsWhereCreatorUser(this);
    }

    @Override
    public void manageSubjectsAndCoursesBeforeDelete() throws EntityNotFoundException {
        for (SubjectModel s : mySubjects) {
            SubjectModel.delete(s.getPlutoCode());
        }
    }
}
