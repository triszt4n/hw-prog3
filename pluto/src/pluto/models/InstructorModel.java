package pluto.models;

import pluto.database.Database;
import pluto.exceptions.AuthorizationException;
import pluto.exceptions.EntityNotFoundException;
import pluto.exceptions.ValidationException;

import javax.json.Json;
import javax.json.JsonObject;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.List;

public class InstructorModel extends UserModel {
    private boolean isAccepted;

    public boolean isAccepted() {
        return isAccepted;
    }

    public void setAccepted(boolean isAccepted) {
        this.isAccepted = isAccepted;
    }

    public InstructorModel(String em, String na, String pw, String d, String addr, boolean isAcc) throws ValidationException, NoSuchAlgorithmException {
        super(em, na, pw, d, addr);
        isAccepted = isAcc;
    }

    public InstructorModel(String email, String name, String dob, String addr, boolean isAcc, String pluto, String encryptedPw, String salt) throws ValidationException {
        super(email, name, dob, addr, pluto, encryptedPw, salt);
        isAccepted = isAcc;
    }

    @Override
    public void authorize(String pw) throws AuthorizationException, ValidationException, NoSuchAlgorithmException {
        if (!isAccepted) {
            throw new AuthorizationException("Your request to be an Instructor is not accepted yet");
        }
        else {
            super.authorize(pw);
        }
    }

    @Override
    public String getTitle() {
        return "Instructor";
    }

    @Override
    public String getStatus() {
        return isAccepted? "Accepted instructor" : "Non-accepted instructor";
    }

    @Override
    public void initCoursesAndSubjects(List<String> plutoCodes) {
        mySubjects = Database.getSubjectsWhereCreatorUser(this);
    }

    @Override
    public void manageSubjectsAndCoursesBeforeDelete() throws EntityNotFoundException {
        for (CourseModel c : myCourses) {
            CourseModel.delete(c.getPlutoCode());
        }
        for (SubjectModel s : mySubjects) {
            SubjectModel.delete(s.getPlutoCode());
        }
    }

    @Override
    public JsonObject jsonify() {
        return Json.createObjectBuilder()
                .add("type", "Instructor")
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
