package pluto.models;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pluto.database.Database;
import pluto.exceptions.EntityNotFoundException;
import pluto.exceptions.ValidationException;
import pluto.models.helpers.UserType;

import java.security.NoSuchAlgorithmException;

import static org.junit.jupiter.api.Assertions.*;

class AdministratorModelTest {
    private AdministratorModel user, user2;

    @BeforeEach
    void setup() throws ValidationException, NoSuchAlgorithmException {
        Database.reset();
        user = new AdministratorModel( "ADMINADMIN11", "pluto@pluto.edu", "Pluto Administrator", "password", "1970-01-01", "User Address");
        new SubjectModel("Main Subject", String.valueOf(5), "2/2/0/v", String.valueOf(3), user, true);
        user.initCoursesAndSubjects();
    }

    @Test
    @DisplayName("Creating admin (given pluto code) successfully")
    void createAdmin1() {
        assertDoesNotThrow(() -> {
            user2 = new AdministratorModel( "ADMINADMIN22", "paul@anderson.org", "Paula Anderson", "password", "1999-08-30", "Some address to be added");
        });
        assertAll(
                () -> assertEquals(UserType.ADMINISTRATOR, user.getType()),
                () -> assertNotEquals(user.getPlutoCode(), user2.getPlutoCode(), "Shouldn't have the same pluto code")
        );
    }

    @Test
    @DisplayName("Creating admin (generated pluto) successfully")
    void createAdmin2() {
        assertDoesNotThrow(() -> {
            user2 = new AdministratorModel( "paul@anderson.org", "Paula Anderson", "password", "1999-08-30", "Some address to be added");
        });
        assertAll(
                () -> assertEquals(UserType.ADMINISTRATOR, user.getType()),
                () -> assertNotEquals(user.getPlutoCode(), user2.getPlutoCode(), "Shouldn't have the same pluto code")
        );
    }

    @Test
    @DisplayName("See if subjects and courses are cleared before deletion")
    void manageSubjectsAndCoursesBeforeDelete() throws EntityNotFoundException {
        user.manageSubjectsAndCoursesBeforeDelete();

        assertAll(
                () -> assertEquals(0, user.getMyCourses().size(), "Should have 0 courses associated"),
                () -> assertEquals(0, user.getMySubjects().size(), "Shouldn't have subject associated")
        );
    }

    @AfterEach
    void tidy() {
        Database.reset();
    }
}