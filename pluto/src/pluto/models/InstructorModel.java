package pluto.models;

import pluto.database.Database;
import pluto.exceptions.AuthorizationException;
import pluto.exceptions.ValidationException;

import java.security.NoSuchAlgorithmException;
import java.util.LinkedList;
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
    public void initMyCoursesAndSubjects(List<String> plutoCodes) {
        myCourses = new LinkedList<>();
        mySubjects = Database.getSubjectsWhereCreatorUser(this);
    }
}
