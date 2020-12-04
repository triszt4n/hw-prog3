package pluto.models;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pluto.database.Database;
import pluto.exceptions.EntityNotFoundException;
import pluto.exceptions.ValidationException;
import pluto.models.helpers.CourseType;

import javax.json.JsonObject;
import java.security.NoSuchAlgorithmException;

import static org.junit.jupiter.api.Assertions.*;

class CourseModelTest {
    private SubjectModel subj;
    private InstructorModel instr;
    private StudentModel stud;
    private CourseModel c1, c2;
    private String pluto;

    @BeforeEach
    void setup() throws ValidationException, NoSuchAlgorithmException {
        Database.reset();
        instr = new InstructorModel("instructor@pluto.edu", "Pluto Instructor", "123456", "1972-03-14", "", true);
        subj = new SubjectModel("Main Subject", String.valueOf(5), "2/2/0/v", String.valueOf(3), instr, true);
        c1 = new CourseModel("C1", CourseType.LECTURE, String.valueOf(3), "", subj, instr);
        c2 = new CourseModel("C2", CourseType.PRACTICE, String.valueOf(3), "", subj, instr);
        stud = new StudentModel( "pluto@pluto.edu", "Pluto Student", "password", "1970-01-01", "User Address", new String[]{c1.getPlutoCode()});
        stud.initCoursesAndSubjects();
        c1.initStudents();
    }

    @Test
    @DisplayName("Creating course successfully")
    void createCourse() {
        assertDoesNotThrow(() -> {
            c1 = new CourseModel("C1", CourseType.LECTURE, String.valueOf(3), "", subj, instr);
        });
        assertAll(
                () -> assertNotEquals(Database.getAllCourses().get(0).getPlutoCode(), c1.getPlutoCode(), "Shouldn't the same Pluto code as other course"),
                () -> assertSame(subj, c1.getSubject(), "Should have give subject"),
                () -> assertSame(instr, c1.getInstructor(), "Should have given instructor"),
                () -> assertEquals(0, c1.getStudents().size(), "Shouldn't have students yet")
        );
    }

    @Test
    @DisplayName("Creating courses with missing field")
    void createCoursesMissingField() {
        assertThrows(ValidationException.class, () -> {
            new CourseModel("", CourseType.LECTURE, "3", "", subj, instr);
        }, "Short code should be given");
        assertThrows(ValidationException.class, () -> {
            new CourseModel("E", CourseType.LECTURE, "", "", subj, instr);
        }, "Max students should be given");
        assertThrows(ValidationException.class, () -> {
            new CourseModel("E", CourseType.LECTURE, "asd", "", subj, instr);
        }, "Max students should be valid number");
        assertThrows(ValidationException.class, () -> {
            new CourseModel("E", CourseType.LECTURE, "0", "", subj, instr);
        }, "Max students should be at least 1");
        assertDoesNotThrow(() -> {
            new CourseModel("E", CourseType.LECTURE, "5", "", subj, instr);
        }, "Blank notes shouldn't be a problem");
    }

    @Test
    @DisplayName("Testing the static get method")
    void getCourse() {
        assertDoesNotThrow(() -> {
            c2 = CourseModel.get(c1.getPlutoCode());
        }, "Should find existing course");
        assertThrows(EntityNotFoundException.class, () -> {
            CourseModel.get("FALSE9PLUTO9");
        }, "Shouldn't find with not existing pluto code");
    }

    @Test
    @DisplayName("Testing the static delete method")
    void deleteCourse() {
        assertDoesNotThrow(() -> {
            CourseModel.delete(c2.getPlutoCode());
        }, "Should find course to delete");
    }

    @Test
    @DisplayName("Updating course")
    void updateCourse() {
        assertThrows(ValidationException.class, () -> {
            c1.update("", CourseType.LECTURE, "3", "", instr);
        }, "Short code should be given");
        assertThrows(ValidationException.class, () -> {
            c1.update("E", CourseType.LECTURE, "", "", instr);
        }, "Max students should be given");
        assertThrows(ValidationException.class, () -> {
            c1.update("E", CourseType.LECTURE, "asd", "", instr);
        }, "Max students should be valid number");
        assertThrows(ValidationException.class, () -> {
            c1.update("E", CourseType.LECTURE, "0", "", instr);
        }, "Max students should be at least 1");
        assertDoesNotThrow(() -> {
            c1.update("E", CourseType.LECTURE, "5", "", instr);
        }, "Blank notes shouldn't be a problem");
    }

    @Test
    @DisplayName("Removing and adding associated attributes")
    void changingAssociations() throws ValidationException, NoSuchAlgorithmException {
        InstructorModel i = new InstructorModel("instr@instr.com", "Instructor", "123456", "1999-08-30", "", true);
        SubjectModel s = new SubjectModel("Subject", "4", "3/3/3/f", "1", i, true);
        c1.setSubject(s);
        assertSame(s, c1.getSubject(), "Should have the given subject");
        c1.setInstructor(i);
        assertSame(i, c1.getInstructor(), "Should have the given instructor");
        assertSame(stud, c1.getStudents().get(0), "Should have the given student");
        c1.removeStudent(stud);
        assertSame(0, c1.getStudents().size(), "Shouldn't have any students");
    }

    @Test
    @DisplayName("Test if jsonify and reverse is correct")
    void jsonWriteAndRead() {
        JsonObject object = c1.jsonify();
        assertDoesNotThrow(() -> {
            c2 = new CourseModel(object);
        });
        assertAll(
                () -> assertEquals(c1.getPlutoCode(), c2.getPlutoCode()),
                () -> assertEquals(c1.getShortCode(), c2.getShortCode()),
                () -> assertSame(c1.getSubject(), c2.getSubject()),
                () -> assertEquals(c1.getNotes(), c2.getNotes()),
                () -> assertEquals(c1.getType(), c2.getType()),
                () -> assertEquals(c1.getMaxStudents(), c2.getMaxStudents()),
                () -> assertSame(c1.getInstructor(), c2.getInstructor())
        );
    }

    @AfterEach
    void tidy() {
        Database.reset();
    }
}