package pluto.models;

import pluto.app.PlutoConsole;
import pluto.database.Database;
import pluto.exceptions.EntityNotFoundException;
import pluto.exceptions.ValidationException;
import pluto.models.helpers.StringValidator;

import javax.json.Json;
import javax.json.JsonObject;
import java.util.LinkedList;
import java.util.List;

public class SubjectModel extends AbstractModel {
    private String name;
    private int credit;
    private String requirements;
    private int semester;
    private UserModel coordinator;
    private boolean isOpened;

    private final List<CourseModel> courses;

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

    public List<CourseModel> getCourses() {
        return courses;
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
        if (!(user == coordinator || user.getTitle().equals("Administrator"))) {
            throw new ValidationException("You have no permission to change this subject!");
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
        coordinator.addSubject(this);
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

        courses = new LinkedList<>();
    }

    public SubjectModel(String name, String credit, String requirements, String semester, String coordinatorPluto, boolean isOpened, String pluto) throws ValidationException, EntityNotFoundException {
        plutoCode = pluto;
        UserModel coordinator = UserModel.get(coordinatorPluto);
        setName(name);
        setCredit(credit);
        setRequirements(requirements);
        setSemester(semester);
        setCoordinator(coordinator);
        setOpened(isOpened, coordinator);
        save();

        courses = new LinkedList<>();
    }

    public void addCourse(CourseModel course) {
        courses.add(course);
    }

    public void removeCourse(CourseModel course) {
        courses.remove(course);
    }

    public void update(String name, String credit, String requirements, String semester, boolean isOpened) throws ValidationException {
        setName(name);
        setCredit(credit);
        setRequirements(requirements);
        setSemester(semester);
        setOpened(isOpened, coordinator);
    }

    public static SubjectModel get(String pluto) throws EntityNotFoundException {
        SubjectModel result = Database.getSubjectWherePlutoCode(pluto);
        if (result == null) {
            throw new EntityNotFoundException("No subject found with Pluto code");
        }
        return result;
    }

    public static void delete(String pluto) throws EntityNotFoundException {
        SubjectModel subject = get(pluto);

        subject.getCoordinator().removeSubject(subject);
        List<StudentModel> students = new LinkedList<>();
        for (CourseModel c : subject.getCourses()) {
            students.addAll(c.getStudents());
            CourseModel.delete(c.getPlutoCode());
        }
        students.stream()
            .distinct()
            .forEach(s -> s.removeSubject(subject));
        Database.removeSubject(subject);
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

    @Override
    public JsonObject jsonify() {
        return Json.createObjectBuilder()
                .add("pluto", plutoCode)
                .add("name", name)
                .add("credit", credit)
                .add("requirements", requirements)
                .add("semester", semester)
                .add("coordinator", coordinator.getPlutoCode())
                .add("isOpened", isOpened)
                .build();
    }
}
