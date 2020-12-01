package pluto.models;

import pluto.database.Database;
import pluto.exceptions.EntityNotFoundException;
import pluto.exceptions.ValidationException;

import javax.json.Json;
import javax.json.JsonObject;
import java.security.NoSuchAlgorithmException;
import java.util.Collections;
import java.util.LinkedList;

public class AdministratorModel extends UserModel {
    public AdministratorModel(String pluto, String em, String na, String pw, String d, String addr) throws ValidationException, NoSuchAlgorithmException {
        super(pluto, em, na, pw, d, addr);
    }

    public AdministratorModel(String em, String na, String pw, String d, String addr) throws ValidationException, NoSuchAlgorithmException {
        super(em, na, pw, d, addr);
    }

    public AdministratorModel(JsonObject json) throws ValidationException {
        super(json);
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
    public void initCoursesAndSubjects() {
        mySubjects = Database.getSubjectsWhereCreatorUser(this);
    }

    @Override
    public void manageSubjectsAndCoursesBeforeDelete() throws EntityNotFoundException {
        LinkedList<SubjectModel> shallowCopySubjects = new LinkedList<>(mySubjects);
        Collections.copy(shallowCopySubjects, mySubjects);

        for (SubjectModel s : shallowCopySubjects) {
            SubjectModel.delete(s.getPlutoCode());
        }
    }

    @Override
    public JsonObject jsonify() {
        JsonObject userObject = super.jsonify();
        return Json.createObjectBuilder()
                .add("type", "Administrator")
                .add("details", userObject)
                .build();
    }
}
