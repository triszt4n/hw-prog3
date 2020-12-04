package pluto.models;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pluto.database.Database;
import pluto.exceptions.ValidationException;
import pluto.models.helpers.CourseType;
import pluto.models.helpers.UserType;

import java.security.NoSuchAlgorithmException;

import static org.junit.jupiter.api.Assertions.*;

class StudentModelTest {
    private StudentModel user;
    private CourseModel c1, c2;

    @BeforeEach
    void setup() throws ValidationException, NoSuchAlgorithmException {
        Database.reset();
        InstructorModel inst = new InstructorModel("instructor@pluto.edu", "Main Instructor", "123456", "1972-03-14", "", true);
        SubjectModel subj = new SubjectModel("Main Subject", String.valueOf(5), "2/2/0/v", String.valueOf(3), inst, true);
        c1 = new CourseModel("C1", CourseType.LECTURE, String.valueOf(3), "", subj, inst);
        c2 = new CourseModel("C2", CourseType.PRACTICE, String.valueOf(3), "", subj, inst);
        user = new StudentModel( "pluto@pluto.edu", "Pluto Student", "password", "1970-01-01", "User Address", new String[]{c1.getPlutoCode(), c2.getPlutoCode()});
        user.initCoursesAndSubjects();
        c1.initStudents();
        c2.initStudents();
    }

    @Test
    @DisplayName("Test associated entities of Student")
    void testCoursesAndSubjects() {
        assertAll(
                () -> assertEquals(2, user.getMyCourses().size(), "Should have 2 courses associated"),
                () -> assertEquals(1, user.getMySubjects().size(), "Should have 1 subject associated"),
                () -> assertEquals(c1.getPlutoCode(), user.getMyCourses().get(0).getPlutoCode(), "First course should be the same as given"),
                () -> assertEquals(c2.getPlutoCode(), user.getMyCourses().get(1).getPlutoCode(), "Second course should be the same as given"),
                () -> assertEquals(1, c1.getStudents().size(), "Should have 1 student associated"),
                () -> assertEquals(1, c2.getStudents().size(), "Should have 1 student associated"),
                () -> assertTrue(c1.getStudents().contains(user), "Course 1 should have this student anymore"),
                () -> assertTrue(c2.getStudents().contains(user), "Course 2 should have this student anymore")
        );
    }

    @Test
    @DisplayName("Creating student successfully")
    void createStudent() {
        assertDoesNotThrow(() -> {
            user = new StudentModel( "paul@anderson.org", "Paula Anderson", "password", "1999-08-30", "Some address to be added");
        });
        assertAll(
                () -> assertEquals(0, user.getMyCourses().size(), "User should have courses taken yet"),
                () -> assertEquals(0, user.getMySubjects().size(), "User should store taken courses' subjects yet"),
                () -> assertEquals(UserType.STUDENT, user.getType())
        );
    }

    @Test
    @DisplayName("See if subjects and courses are cleared before deletion")
    void manageSubjectsAndCoursesBeforeDelete() {
        user.manageSubjectsAndCoursesBeforeDelete();

        assertAll(
                () -> assertEquals(2, user.getMyCourses().size(), "Still should have 2 courses associated"),
                () -> assertFalse(c1.getStudents().contains(user), "Course 1 shouldn't have this student anymore"),
                () -> assertFalse(c2.getStudents().contains(user), "Course 2 shouldn't have this student anymore")
        );
    }

    @AfterEach
    void tidy() {
        Database.reset();
    }
}