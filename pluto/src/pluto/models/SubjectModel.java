package pluto.models;

import pluto.app.PlutoConsole;
import pluto.database.Database;
import pluto.exceptions.EntityNotFoundException;
import pluto.exceptions.ValidationException;
import pluto.models.helpers.StringValidator;
import pluto.models.helpers.UserType;

import javax.json.Json;
import javax.json.JsonObject;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class SubjectModel extends AbstractModel {
    /***
     * Name for the subject
     */
    private String name;

    /***
     * credit number of the subject
     */
    private int credit;

    /***
     * Requirement formula of the subject
     */
    private String requirements;

    /***
     * Number of the recommended semester
     */
    private int semester;

    /***
     * The main coordinator of the subject, who created it
     */
    private UserModel coordinator;

    /***
     * if the subject should be opened to the students
     */
    private boolean isOpened;

    /***
     * Associated courses of the subject, all the courses that run under this subject.
     */
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

    /***
     * Validated setter
     * @param name
     * @throws ValidationException
     */
    public void setName(String name) throws ValidationException {
        StringValidator sv = new StringValidator();
        sv.validate(name, "Name")
                .checkLength(3, 200);
        this.name = name;
    }

    /**
     * Validated setter
     * @param credit
     * @throws ValidationException
     */
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

    /**
     * Validated setter
     * @param credit
     * @throws ValidationException
     */
    public void setCredit(int credit) throws ValidationException {
        if (credit > 100) {
            throw new ValidationException("Are you sure about the number of credits?");
        }
        this.credit = credit;
    }

    /***
     * Validated setter
     * @param requirements
     * @throws ValidationException
     */
    public void setRequirements(String requirements) throws ValidationException {
        StringValidator sv = new StringValidator();
        sv.validate(requirements, "Requirements")
                .checkRegex("[0-9]/[0-9]/[0-9]/[fsv]$");
        this.requirements = requirements;
    }

    /***
     * Validated setter
     * @param semester
     * @throws ValidationException
     */
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

    /***
     * Validated setter
     * @param semester
     * @throws ValidationException
     */
    public void setSemester(int semester) throws ValidationException {
        if (semester > 14) {
            throw new ValidationException("Are you sure about the recommended semester number?");
        }
        this.semester = semester;
    }

    public void setCoordinator(UserModel coordinator) {
        this.coordinator = coordinator;
    }

    /***
     * Validated setter
     * @param opened
     * @param user
     * @throws ValidationException
     */
    public void setOpened(boolean opened, UserModel user) throws ValidationException {
        if (!(user == coordinator || user.getType().equals(UserType.ADMINISTRATOR))) {
            throw new ValidationException("You have no permission to change this subject!");
        }
        else {
            isOpened = opened;
        }
    }

    /**
     * @see AbstractModel
     */
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

    public SubjectModel(JsonObject json) throws ValidationException, EntityNotFoundException {
        plutoCode = json.getString("pluto");
        setName(json.getString("name"));
        setCredit(json.getInt("credit"));
        setRequirements(json.getString("requirements"));
        setSemester(json.getInt("semester"));
        UserModel coordinator = UserModel.get(json.getString("coordinator"));
        setCoordinator(coordinator);
        setOpened(json.getBoolean("isOpened"), coordinator);
        save();
        courses = new LinkedList<>();
    }

    public void addCourse(CourseModel course) {
        courses.add(course);
    }

    public void removeCourse(CourseModel course) {
        courses.remove(course);
    }

    /***
     * Updater method (modification)
     * @param name
     * @param credit
     * @param requirements
     * @param semester
     * @param isOpened
     * @throws ValidationException
     */
    public void update(String name, String credit, String requirements, String semester, boolean isOpened) throws ValidationException {
        setName(name);
        setCredit(credit);
        setRequirements(requirements);
        setSemester(semester);
        setOpened(isOpened, coordinator);
    }

    /***
     * Static method that connects the query to the database.
     * @param pluto pluto code of searched subject
     * @return the searched SubjectModel
     * @throws EntityNotFoundException if the pluto code belongs to no subject in db
     */
    public static SubjectModel get(String pluto) throws EntityNotFoundException {
        SubjectModel result = Database.getSubjectWherePlutoCode(pluto);
        if (result == null) {
            throw new EntityNotFoundException("No subject found with Pluto code");
        }
        return result;
    }

    /***
     * Static method that connects the query to the database
     * @param pluto
     * @throws EntityNotFoundException
     */
    public static void delete(String pluto) throws EntityNotFoundException {
        SubjectModel subject = get(pluto);
        List<StudentModel> students = new LinkedList<>();
        List<CourseModel> shallowCopyCourses = new LinkedList<>(subject.getCourses());
        Collections.copy(shallowCopyCourses, subject.getCourses());

        for (CourseModel c : shallowCopyCourses) {
            students.addAll(c.getStudents());
            CourseModel.delete(c.getPlutoCode());
        }

        List<StudentModel> distinctStudents = students.stream()
                .distinct()
                .collect(Collectors.toList());

        for (StudentModel s : distinctStudents) {
            s.removeSubject(subject);
        }

        subject.getCoordinator().removeSubject(subject);
        Database.removeSubject(subject);
    }

    /***
     * Static method that connects the query to the database
     * @return all the subjects
     */
    public static List<SubjectModel> all() {
        return Database.getAllSubjects();
    }

    /**
     * @see AbstractModel
     */
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

    /**
     * @see AbstractModel
     */
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
