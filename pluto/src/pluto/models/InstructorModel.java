package pluto.models;

import pluto.models.exceptions.ValidationException;

public class InstructorModel extends UserModel {
    private boolean isAccepted;

    public InstructorModel(String em, String na, String pw, String d, String addr, boolean isAcc) throws ValidationException {
        super(em, na, pw, d, addr);
        isAccepted = isAcc;
    }

    @Override
    public void authorize(String pw) throws AuthorizationException {
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
}
