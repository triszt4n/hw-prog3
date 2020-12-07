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

class SubjectModelTest {
    private SubjectModel subj, subj2;
    private InstructorModel instr;
    private CourseModel c1, c2;

    @BeforeEach
    void setup() throws ValidationException, NoSuchAlgorithmException {
        Database.reset();
        instr = new InstructorModel("instructor@pluto.edu", "Pluto Instructor", "123456", "1972-03-14", "", true);
        subj = new SubjectModel("Main Subject", String.valueOf(5), "2/2/0/v", String.valueOf(3), instr, true);
        c1 = new CourseModel("C1", CourseType.LECTURE, String.valueOf(3), "", subj, instr);
        c2 = new CourseModel("C2", CourseType.PRACTICE, String.valueOf(3), "", subj, instr);
        StudentModel stud = new StudentModel("pluto@pluto.edu", "Pluto Student", "password", "1970-01-01", "User Address", new String[]{c1.getPlutoCode()});
        stud.initCoursesAndSubjects();
        c1.initStudents();
    }

    @Test
    @DisplayName("Testing given subject")
    void testSubject() {
        assertAll(
                () -> assertEquals(Database.getAllSubjects().get(0).getPlutoCode(), subj.getPlutoCode(), "Should have same Pluto code as database first"),
                () -> assertSame(instr, subj.getCoordinator(), "Should have given instructor as coordinator"),
                () -> assertEquals(2, subj.getCourses().size(), "Should have 2 given courses")
        );
    }

    @Test
    @DisplayName("Creating subject successfully")
    void createSubject() {
        assertDoesNotThrow(() -> {
            subj = new SubjectModel("Main Subject", "5", "2/2/0/v", String.valueOf(3), instr, false);
        });
        assertAll(
                () -> assertNotEquals(Database.getAllSubjects().get(0).getPlutoCode(), subj.getPlutoCode(), "Shouldn't have same Pluto code as other subject"),
                () -> assertSame(instr, subj.getCoordinator(), "Should have given instructor as coordinator"),
                () -> assertEquals(0, subj.getCourses().size(), "Shouldn't have courses yet")
        );
    }

    @Test
    @DisplayName("Creating subjects with missing field")
    void createSubjectsMissingField() {
        assertThrows(ValidationException.class, () -> new SubjectModel("", "5", "2/2/0/v", String.valueOf(3), instr, false), "Name should be given");
        assertThrows(ValidationException.class, () -> new SubjectModel("Generic subject", "", "2/2/0/v", String.valueOf(3), instr, false), "Credit should be given");
        assertThrows(ValidationException.class, () -> new SubjectModel("Generic subject", "asd", "2/2/0/v", String.valueOf(3), instr, false), "Credit should be number");
        assertThrows(ValidationException.class, () -> new SubjectModel("Generic subject", "5", "only exam", String.valueOf(3), instr, false), "Requirements should be valid to format");
        assertThrows(ValidationException.class, () -> new SubjectModel("Generic subject", "5", "2/2/0/v", "", instr, false), "Semester should be given");
        assertDoesNotThrow(() -> {
            new SubjectModel("Generic subject", "5", "2/2/0/v", "3", instr, false);
        }, "Everything should be valid");
    }

    @Test
    @DisplayName("Testing the static get method")
    void getSubject() {
        assertDoesNotThrow(() -> {
            subj2 = SubjectModel.get(subj.getPlutoCode());
        }, "Should find existing course");
        assertThrows(EntityNotFoundException.class, () -> CourseModel.get("FALSE9PLUTO9"), "Shouldn't find with not existing pluto code");
    }

    @Test
    @DisplayName("Testing the static delete method")
    void deleteSubject() {
        assertDoesNotThrow(() -> SubjectModel.delete(subj.getPlutoCode()), "Should find course to delete");
    }

    @Test
    @DisplayName("Updating subject")
    void updateSubject() {
        assertThrows(ValidationException.class, () -> subj.update("", "5", "2/2/0/v", String.valueOf(3), false), "Name should be given");
        assertThrows(ValidationException.class, () -> subj.update("Generic subject", "", "2/2/0/v", String.valueOf(3), false), "Credit should be given");
        assertThrows(ValidationException.class, () -> subj.update("Generic subject", "asd", "2/2/0/v", String.valueOf(3), false), "Credit should be number");
        assertThrows(ValidationException.class, () -> subj.update("Generic subject", "5", "only exam", String.valueOf(3), false), "Requirements should be valid to format");
        assertThrows(ValidationException.class, () -> subj.update("Generic subject", "5", "2/2/0/v", "", false), "Semester should be given");
        assertDoesNotThrow(() -> subj.update("Generic subject", "5", "2/2/0/v", "3", false), "Everything should be valid");
    }

    @Test
    @DisplayName("Removing and adding associated attributes")
    void changingAssociations() throws ValidationException, NoSuchAlgorithmException {
        InstructorModel i = new InstructorModel("instr@instr.com", "Instructor", "123456", "1999-08-30", "", true);
        subj.setCoordinator(i);
        assertSame(i, subj.getCoordinator(), "Should have the given instructor");
        subj.removeCourse(c1);
        subj.removeCourse(c2);
        assertEquals(0, subj.getCourses().size(), "Shouldn't have any courses");
    }

    @Test
    @DisplayName("Test if jsonify and reverse is correct")
    void jsonWriteAndRead() {
        JsonObject object = subj.jsonify();
        assertDoesNotThrow(() -> {
            subj2 = new SubjectModel(object);
        });
        assertAll(
                () -> assertEquals(subj.getPlutoCode(), subj2.getPlutoCode()),
                () -> assertEquals(subj.getName(), subj2.getName()),
                () -> assertEquals(subj.getRequirements(), subj2.getRequirements()),
                () -> assertEquals(subj.isOpened(), subj2.isOpened()),
                () -> assertSame(subj.getCoordinator(), subj2.getCoordinator()),
                () -> assertEquals(subj.getCredit(), subj2.getCredit()),
                () -> assertEquals(subj.getSemester(), subj2.getSemester())
        );
    }

    @AfterEach
    void tidy() {
        Database.reset();
    }
}