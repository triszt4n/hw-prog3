package pluto.models;

import pluto.database.Database;
import pluto.exceptions.AuthorizationException;
import pluto.exceptions.EntityNotFoundException;
import pluto.exceptions.ValidationException;
import pluto.models.helpers.UserType;

import javax.json.Json;
import javax.json.JsonObject;
import java.security.NoSuchAlgorithmException;
import java.util.Collections;
import java.util.LinkedList;

/**
 * Representation of an instructor in the application system.
 * They coordinate subjects (create), and instruct courses.
 */
public class InstructorModel extends UserModel {
    /**
     * if the instructor is accepted by an admin into the application
     */
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

    public InstructorModel(JsonObject json) throws ValidationException {
        super(json);
        isAccepted = json.getBoolean("isAccepted");
    }

    /***
     * @see UserModel
     * @param pw raw password to be authorized with
     * @throws AuthorizationException
     * @throws ValidationException
     * @throws NoSuchAlgorithmException
     */
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
    public UserType getType() {
        return UserType.INSTRUCTOR;
    }

    @Override
    public String getStatus() {
        return isAccepted? "Accepted instructor" : "Non-accepted instructor";
    }

    /***
     * Sets all the subjects that they created/coordinate
     */
    @Override
    public void initCoursesAndSubjects() {
        mySubjects = Database.getSubjectsWhereCreatorUser(this);
    }

    /***
     * With the help of shallow copies, the method deletes all the courses where instructor was instructor, and
     * deletes all the subjects where they were coordinator
     * @throws EntityNotFoundException
     */
    @Override
    public void manageSubjectsAndCoursesBeforeDelete() throws EntityNotFoundException {
        LinkedList<CourseModel> shallowCopyCourses = new LinkedList<>(myCourses);
        LinkedList<SubjectModel> shallowCopySubjects = new LinkedList<>(mySubjects);
        Collections.copy(shallowCopyCourses, myCourses);
        Collections.copy(shallowCopySubjects, mySubjects);

        for (CourseModel c : shallowCopyCourses) {
            CourseModel.delete(c.getPlutoCode());
        }

        for (SubjectModel s : shallowCopySubjects) {
            SubjectModel.delete(s.getPlutoCode());
        }
    }

    @Override
    public JsonObject jsonify() {
        JsonObject userObject = super.jsonify();
        return Json.createObjectBuilder()
                .add("type", "Instructor")
                .add("isAccepted", isAccepted)
                .add("details", userObject)
                .build();
    }
}
