package pluto.models;

import pluto.database.Database;
import pluto.exceptions.EntityNotFoundException;
import pluto.exceptions.ValidationException;

import javax.json.Json;
import javax.json.JsonObject;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.List;

public class AdministratorModel extends UserModel {
    public AdministratorModel(String em, String na, String pw, String d, String addr) throws ValidationException, NoSuchAlgorithmException {
        super(em, na, pw, d, addr);
    }

    public AdministratorModel(String email, String name, String dob, String addr, String pluto, String encryptedPw, String salt) throws ValidationException {
        super(email, name, dob, addr, pluto, encryptedPw, salt);
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

    @Override
    public JsonObject jsonify() {
        return Json.createObjectBuilder()
                .add("type", "Administrator")
                .add("pluto", plutoCode)
                .add("email", email)
                .add("name", name)
                .add("dob", unparsedDob)
                .add("address", address)
                .add("credentials", Json.createObjectBuilder()
                        .add("password", Arrays.toString(encryptedPassword))
                        .add("salt", Arrays.toString(salt))
                )
                .build();
    }
}
