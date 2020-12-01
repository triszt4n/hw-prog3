package pluto.models;

import pluto.app.PlutoConsole;
import pluto.database.Database;
import pluto.exceptions.EntityNotFoundException;
import pluto.exceptions.ValidationException;
import pluto.models.helpers.CourseType;
import pluto.models.helpers.StringValidator;

import javax.json.Json;
import javax.json.JsonObject;
import java.util.LinkedList;
import java.util.List;

public class CourseModel extends AbstractModel {
    private String shortCode;
    private CourseType type;
    private int maxStudents;
    private String notes;

    private SubjectModel subject;
    private List<StudentModel> students;
    private InstructorModel instructor;

    public String getShortCode() {
        return shortCode;
    }

    public CourseType getType() {
        return type;
    }

    public int getMaxStudents() {
        return maxStudents;
    }

    public String getNotes() {
        return notes;
    }

    public SubjectModel getSubject() {
        return subject;
    }

    public List<StudentModel> getStudents() {
        return students;
    }

    public InstructorModel getInstructor() {
        return instructor;
    }

    public void setShortCode(String shortCode) throws ValidationException {
        StringValidator sv = new StringValidator();
        sv.validate(shortCode, "Short code")
                .checkLength(1, 10);
        this.shortCode = shortCode;
    }

    public void setType(CourseType type) {
        this.type = type;
    }

    public void setType(int type) {
        this.type = CourseType.values()[type];
    }

    public void setMaxStudents(String maxStudents) throws ValidationException {
        int max;
        try {
            max = Integer.parseInt(maxStudents);
        } catch (NumberFormatException e) {
            throw new ValidationException("Maximum of attending students must be a number between 1 and 1000");
        }

        if (max > 1000) {
            throw new ValidationException("Are you sure about the maximum of attending students?");
        }
        else if (max < 1) {
            throw new ValidationException("Course must be available to students!");
        }
        this.maxStudents = max;
    }

    public void setMaxStudents(int maxStudents) throws ValidationException {
        if (maxStudents > 1000) {
            throw new ValidationException("Are you sure about the maximum of attending students?");
        }
        else if (maxStudents < 1) {
            throw new ValidationException("Course must be available to students!");
        }
        this.maxStudents = maxStudents;
    }

    public void setNotes(String notes) throws ValidationException {
        StringValidator sv = new StringValidator();
        sv.validate(notes, "Notes")
                .checkLength(0, 500);
        this.notes = notes;
    }

    public void setSubject(SubjectModel subject) {
        this.subject = subject;
    }

    public void setInstructor(InstructorModel instructor) {
        this.instructor = instructor;
    }

    public CourseModel(String shortCode, CourseType type, String maxStudents, String notes, SubjectModel subject, InstructorModel instructor) throws ValidationException {
        setShortCode(shortCode);
        setType(type);
        setMaxStudents(maxStudents);
        setSubject(subject);
        setInstructor(instructor);
        setNotes(notes);
        save();

        students = new LinkedList<>();
    }

    public CourseModel(JsonObject json) throws ValidationException, EntityNotFoundException {
        plutoCode = json.getString("pluto");
        SubjectModel subject = SubjectModel.get(json.getString("subject"));
        InstructorModel instructor = (InstructorModel) UserModel.get(json.getString("instructor"));

        setShortCode(json.getString("shortCode"));
        setType(json.getInt("type"));
        setMaxStudents(json.getInt("maxStudents"));
        setNotes(json.getString("notes"));
        setSubject(subject);
        setInstructor(instructor);
        save();

        students = new LinkedList<>();
    }

    public static CourseModel get(String pluto) throws EntityNotFoundException {
        CourseModel result = Database.getCourseWherePlutoCode(pluto);
        if (result == null) {
            throw new EntityNotFoundException("No course found with Pluto code");
        }
        return result;
    }

    public static List<CourseModel> all() {
        return Database.getAllCourses();
    }

    public static void delete(String pluto) throws EntityNotFoundException {
        CourseModel course = get(pluto);

        List<StudentModel> students = course.getStudents();
        for (StudentModel student : students) {
            student.removeCourse(course);
        }

        course.getSubject().removeCourse(course);
        course.getInstructor().removeCourse(course);

        Database.removeCourse(course);
    }

    public void update(String shortCode, CourseType type, String maxStudents, String notes, InstructorModel instructor) throws ValidationException {
        setShortCode(shortCode);
        setType(type);
        setMaxStudents(maxStudents);
        setNotes(notes);
        setInstructor(instructor);
    }

    public void initStudents() {
        students = Database.getStudentsWhereMyCoursesHas(this);
    }

    public void addStudent(StudentModel student) throws ValidationException {
        if (maxStudents > students.size()) {
            students.add(student);
        }
        else if (students.contains(student)) {
            throw new ValidationException("You've already joined this course!");
        }
        else {
            throw new ValidationException("Course is full");
        }
    }

    public void removeStudent(StudentModel student) {
        students.remove(student);
    }

    @Override
    protected void save() {
        if (plutoCode == null) {
            generatePlutoCode();
        }

        Database.addCourse(this);
        instructor.addCourse(this);
        subject.addCourse(this);
    }

    @Override
    public String toString() {
        return PlutoConsole.createLog("[COURSE] " + plutoCode,
                shortCode,
                type.toString(),
                String.valueOf(maxStudents),
                instructor.getName(),
                subject.getName()
        );
    }

    @Override
    public JsonObject jsonify() {
        return Json.createObjectBuilder()
                .add("pluto", plutoCode)
                .add("shortCode", shortCode)
                .add("type", type.ordinal())
                .add("maxStudents", maxStudents)
                .add("notes", notes)
                .add("subject", subject.getPlutoCode())
                .add("instructor", instructor.getPlutoCode())
                .build();
    }
}
