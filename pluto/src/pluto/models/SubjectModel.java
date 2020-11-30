package pluto.models;

import pluto.app.PlutoConsole;
import pluto.database.Database;
import pluto.exceptions.EntityNotFoundException;
import pluto.exceptions.ValidationException;
import pluto.models.validators.StringValidator;

import java.util.List;

public class SubjectModel extends AbstractModel {
    private String name;
    private int credit;
    private String requirements;
    private int semester;
    private UserModel coordinator;
    private boolean isOpened;

    private List<CourseModel> courses;

    public String getName() {
        return name;
    }
    public int getCredit() {
        return credit;
    }
    public String getRequirements() {
        return requirements;
    }
    public int getSemester() {
        return semester;
    }
    public UserModel getCoordinator() {
        return coordinator;
    }
    public boolean isOpened() {
        return isOpened;
    }

    public void setName(String name) throws ValidationException {
        StringValidator sv = new StringValidator();
        sv.validate(name, "Name")
                .checkLength(3, 200);
        this.name = name;
    }

    public void setCredit(String credit) throws ValidationException {
        int cr;
        try {
            cr = Integer.parseInt(credit);
        }
        catch (NumberFormatException e) {
            throw new ValidationException("Credit: " + e.getMessage());
        }

        if (cr > 100) {
            throw new ValidationException("Are you sure about the number of credits?");
        }
        this.credit = cr;
    }

    public void setRequirements(String requirements) throws ValidationException {
        StringValidator sv = new StringValidator();
        sv.validate(requirements, "Requirements")
                .checkRegex("[0-9]/[0-9]/[0-9]/[fsv]$");
        this.requirements = requirements;
    }

    public void setSemester(String semester) throws ValidationException {
        int sem;
        try {
            sem = Integer.parseInt(semester);
        }
        catch (NumberFormatException e) {
            throw new ValidationException("Semester: " + e.getMessage());
        }

        if (sem > 14) {
            throw new ValidationException("Are you sure about the recommended semester number?");
        }
        this.semester = sem;
    }

    public void setCoordinator(UserModel coordinator) {
        this.coordinator = coordinator;
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
    protected void save() {
        if (plutoCode == null) {
            generatePlutoCode();
        }

        Database.addSubject(this);
    }

    public SubjectModel(String name, String credit, String requirements, String semester, UserModel coordinator, boolean isOpened) throws ValidationException {
        plutoCode = null;
        setName(name);
        setCredit(credit);
        setRequirements(requirements);
        setSemester(semester);
        setCoordinator(coordinator);
        setOpened(isOpened, coordinator);
        save();
    }

    public void initCourses() {
        courses = Database.getCoursesWhereSubject(this);
    }

    public static SubjectModel get(String pluto) throws EntityNotFoundException {
        SubjectModel result = Database.getSubjectWherePlutoCode(pluto);
        if (result == null) {
            throw new EntityNotFoundException("No subject found with Pluto code");
        }
        return result;
    }

    public static void delete(String pluto) throws EntityNotFoundException {
        SubjectModel result = get(pluto);
        Database.removeSubject(result);
    }

    public static List<SubjectModel> all() {
        return Database.getAllSubjects();
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
