package pluto.models;

import pluto.app.PlutoConsole;
import pluto.database.Database;
import pluto.exceptions.EntityNotFoundException;
import pluto.exceptions.ValidationException;

import java.util.List;

public class SubjectModel extends AbstractModel {
    private String name;
    private int credit;
    private String requirements;
    private int semester;
    private UserModel coordinator;
    private boolean isOpened;

    public String getPlutoCode() {
        return plutoCode;
    }

    public void setPlutoCode(String plutoCode) {
        this.plutoCode = plutoCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCredit() {
        return credit;
    }

    public void setCredit(int credit) {
        this.credit = credit;
    }

    public String getRequirements() {
        return requirements;
    }

    public void setRequirements(String requirements) {
        this.requirements = requirements;
    }

    public int getSemester() {
        return semester;
    }

    public void setSemester(int semester) {
        this.semester = semester;
    }

    public UserModel getCoordinator() {
        return coordinator;
    }

    public void setCoordinator(UserModel coordinator) {
        this.coordinator = coordinator;
    }

    public boolean isOpened() {
        return isOpened;
    }

    public void setOpened(boolean opened, UserModel user) throws ValidationException {
        if (user != coordinator) {
            throw new ValidationException("You are not the coordinator of this subject!");
        }
        else {
            isOpened = opened;
        }
    }

    @Override
    public int getIndex() {
        return Database.getCurrentIndexOfSubject(this);
    }

    @Override
    protected void save() {

    }

    public static SubjectModel get(String pluto) throws EntityNotFoundException, ValidationException {
        SubjectModel result = Database.getSubjectWherePlutoCode(pluto);
        if (result == null) {
            throw new EntityNotFoundException("No subject found with Pluto code");
        }
        return result;
    }

    public static List<SubjectModel> all() {
        return Database.getSubjects();
    }

    @Override
    public String toString() {
        return PlutoConsole.createLog(
                "[SUBJECT] " + plutoCode,
                name,
                String.valueOf(credit),
                requirements,
                String.valueOf(semester),
                coordinator.getName(),
                String.valueOf(isOpened)
        );
    }
}
