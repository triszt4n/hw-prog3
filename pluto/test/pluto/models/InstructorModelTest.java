package pluto.models;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pluto.database.Database;
import pluto.exceptions.EntityNotFoundException;
import pluto.exceptions.ValidationException;
import pluto.models.helpers.CourseType;
import pluto.models.helpers.UserType;

import java.security.NoSuchAlgorithmException;

import static org.junit.jupiter.api.Assertions.*;

class InstructorModelTest {
    private InstructorModel user;
    private SubjectModel subj;
    private CourseModel c1, c2;

    @BeforeEach
    void setup() throws ValidationException, NoSuchAlgorithmException {
        Database.reset();
        user = new InstructorModel("instructor@pluto.edu", "Pluto Instructor", "123456", "1972-03-14", "", true);
        subj = new SubjectModel("Main Subject", String.valueOf(5), "2/2/0/v", String.valueOf(3), user, true);
        c1 = new CourseModel("C1", CourseType.LECTURE, String.valueOf(3), "", subj, user);
        c2 = new CourseModel("C2", CourseType.PRACTICE, String.valueOf(3), "", subj, user);
        user.initCoursesAndSubjects();
    }

    @Test
    @DisplayName("Test associated entities of Instructor")
    void testCoursesAndSubjects() {
        assertAll(
                () -> assertEquals(2, user.getMyCourses().size(), "Should have 2 courses associated"),
                () -> assertEquals(1, user.getMySubjects().size(), "Should have 1 subject associated"),
                () -> assertEquals(c1.getPlutoCode(), user.getMyCourses().get(0).getPlutoCode(), "First course should be the same as given"),
                () -> assertEquals(c2.getPlutoCode(), user.getMyCourses().get(1).getPlutoCode(), "Second course should be the same as given"),
                () -> assertEquals(0, c1.getStudents().size(), "Shouldn't have student associated"),
                () -> assertEquals(0, c2.getStudents().size(), "Shouldn't have student associated"),
                () -> assertSame(user, c1.getInstructor(), "Course 1 should have this instructor"),
                () -> assertSame(user, c2.getInstructor(), "Course 2 should have this instructor"),
                () -> assertSame(user, subj.getCoordinator(), "Subject should have this coordinator")
        );
    }

    @Test
    @DisplayName("Creating instructor successfully")
    void createInstructor() {
        assertDoesNotThrow(() -> {
            user = new InstructorModel( "paul@anderson.org", "Paula Anderson", "password", "1999-08-30", "Some address to be added", false);
        });
        assertAll(
                () -> assertEquals(0, user.getMyCourses().size(), "User should have courses taken yet"),
                () -> assertEquals(0, user.getMySubjects().size(), "User should store taken courses' subjects yet"),
                () -> assertEquals(UserType.INSTRUCTOR, user.getType())
        );
    }

    @Test
    @DisplayName("See if subjects and courses are cleared before deletion")
    void manageSubjectsAndCoursesBeforeDelete() throws EntityNotFoundException, ValidationException, NoSuchAlgorithmException {
        StudentModel stud = new StudentModel( "pluto@pluto.edu", "Pluto Student", "password", "1970-01-01", "User Address", new String[]{c1.getPlutoCode(), c2.getPlutoCode()});

        user.manageSubjectsAndCoursesBeforeDelete();

        assertAll(
                () -> assertEquals(0, user.getMyCourses().size(), "Still should have 0 courses associated (all deleted)"),
                () -> assertEquals(0, user.getMySubjects().size(), "Still should have 0 subject associated (deleted)"),
                () -> assertEquals(0, Database.getAllSubjects().size(), "Subject must be deleted"),
                () -> assertEquals(0, Database.getAllCourses().size(), "Courses must be deleted"),
                () -> assertEquals(0, stud.getMyCourses().size(), "Student should lose all courses"),
                () -> assertEquals(0, stud.getMySubjects().size(), "Student should lose all subjects")
        );
    }

    @AfterEach
    void tidy() {
        Database.reset();
    }
}