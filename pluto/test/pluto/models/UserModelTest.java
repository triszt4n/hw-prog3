package pluto.models;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pluto.database.Database;
import pluto.exceptions.AuthorizationException;
import pluto.exceptions.EntityNotFoundException;
import pluto.exceptions.ValidationException;
import pluto.models.helpers.CourseType;

import javax.json.JsonObject;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;

import static org.junit.jupiter.api.Assertions.*;

class UserModelTest {
    private UserModel user, user2;

    @BeforeEach
    void setup() throws ValidationException, NoSuchAlgorithmException {
        Database.reset();
        user = new AdministratorModel("PLUTOCODE123", "pluto@pluto.edu", "Pluto Administrator", "password", "1970-01-01", "User Address");
    }

    @Test
    @DisplayName("Creating user successfully")
    void createUser() {
        assertDoesNotThrow(() -> {
            user = new AdministratorModel( "paul@anderson.org", "Paula Anderson", "password", "1999-08-30", "Some address to be added");
        });
        assertAll(
                () -> assertNotEquals(Database.getAllUsers().get(0).getPlutoCode(), user.getPlutoCode(), "Shouldn't the same Pluto code as other user"),
                () -> assertNotNull(user.getEncryptedPassword(), "Encrypted password should be stored"),
                () -> assertNotEquals("password".getBytes(), user.getEncryptedPassword(), "Entered password should be encrypted"),
                () -> assertEquals((new SimpleDateFormat("yyyy-MM-dd")).parse("1999-08-30"), user.getParsedDob(), "Date of birth should be parsed correctly")
        );
    }

    @Test
    @DisplayName("Creating users with missing field")
    void createUsersMissingField() {
        assertThrows(ValidationException.class, () -> new StudentModel( "", "Paula Anderson", "password", "1999-08-30", "Some address to be added"), "Email should be given");
        assertThrows(ValidationException.class, () -> new StudentModel( "paul@anderson.org", "", "password", "1999-08-30", "Some address to be added"), "Name should be given");
        assertThrows(ValidationException.class, () -> new StudentModel( "paul@anderson.org", "Paula Anderson", "", "1999-08-30", "Some address to be added"), "Password should be given");
        assertThrows(ValidationException.class, () -> new StudentModel( "this_is_not_an_email", "Paula Anderson", "password", "1999-08-30", "Some address to be added"), "Correct email should be given");
        assertThrows(ValidationException.class, () -> new StudentModel( "paul@anderson.org", "Paula Anderson", "password", "1999.08.30", "Some address to be added"), "Format of date of birth is strictly yyyy-MM-dd");
        assertDoesNotThrow(() -> {
            new StudentModel( "paul@anderson.org", "Paula Anderson", "password", "1999-08-30", "");
        }, "Blank address shouldn't be a problem");
    }

    @Test
    @DisplayName("Testing the static get method")
    void getUser() {
        assertDoesNotThrow(() -> {
            user2 = UserModel.get(user.getPlutoCode());
        }, "Should find existing user");
        assertThrows(EntityNotFoundException.class, () -> UserModel.get("FALSE9PLUTO9"), "Shouldn't find with not existing pluto code");
        assertEquals(user.getName(), user2.getName());
    }

    @Test
    @DisplayName("Testing the static delete method")
    void deleteUser() {
        assertDoesNotThrow(() -> UserModel.delete(user.getPlutoCode()), "Should find user to delete");
    }

    @Test
    @DisplayName("Updating user")
    void updateUser() {
        assertThrows(ValidationException.class, () -> user.update( "", "Paula Anderson", "password", "1999-08-30", "Some address to be added"), "Email should be given");
        assertThrows(ValidationException.class, () -> user.update( "paul@anderson.org", "", "password", "1999-08-30", "Some address to be added"), "Name should be given");
        assertDoesNotThrow(() -> user.update( "paul@anderson.org", "Paula Anderson", "", "1999-08-30", "Some address to be added"), "Password can be left blank");
        assertThrows(ValidationException.class, () -> user.update( "this_is_not_an_email", "Paula Anderson", "password", "1999-08-30", "Some address to be added"), "Correct email should be given");
        assertThrows(ValidationException.class, () -> user.update( "paul@anderson.org", "Paula Anderson", "password", "1999.08.30", "Some address to be added"), "Format of date of birth is strictly yyyy-MM-dd");
        assertDoesNotThrow(() -> user.update( "paul@anderson.org", "Paula Anderson", "password", "1999-08-30", ""), "Blank address shouldn't be a problem");
    }

    @Test
    @DisplayName("Changing password")
    void changePw() {
        assertDoesNotThrow(() -> user.update( "paul@anderson.org", "Paula Anderson", "new_pass", "1999-08-30", "Some address to be added"), "Valid password should be accepted");
        assertDoesNotThrow(() -> user.authorize("new_pass"), "Should access new password");
        assertThrows(AuthorizationException.class, () -> user.authorize("password"), "Shouldn't accept old password");
    }

    @Test
    @DisplayName("Removing and adding associated attributes")
    void changingAssociations() throws ValidationException, NoSuchAlgorithmException {
        InstructorModel i = new InstructorModel("instr@instr.com", "Instructor", "123456", "1999-08-30", "", true);
        SubjectModel s = new SubjectModel("Subject", "4", "3/3/3/f", "1", i, true);
        CourseModel c = new CourseModel("ASD", CourseType.PRACTICE, "3", "", s, i);
        user.addCourse(c);
        assertSame(c, user.getMyCourses().get(0), "Should have the given course");
        user.removeCourse(c);
        assertEquals(0, user.getMyCourses().size(), "Shouldn't have any courses");
        user.addSubject(s);
        assertSame(s, user.getMySubjects().get(0), "Should have the given subject");
        user.removeSubject(s);
        assertEquals(0, user.getMySubjects().size(), "Shouldn't have any subject");
    }

    @Test
    @DisplayName("Test successful and unsuccessful authorization")
    void testAuthorization() {
        assertDoesNotThrow(() -> user.authorize("password"), "Should access original password");
        assertThrows(AuthorizationException.class, () -> user.authorize("notmypassword"), "Shouldn't access foreign password");
        assertThrows(ValidationException.class, () -> user.authorize(""), "Non-valid password code won't even get trough validation");
    }

    @Test
    @DisplayName("Test if jsonify and reverse is correct and password is okay after")
    void jsonWriteAndRead() {
        JsonObject object = user.jsonify();
        assertDoesNotThrow(() -> {
            user2 = new AdministratorModel(object);
        });
        assertAll(
                () -> assertEquals(user.getPlutoCode(), user2.getPlutoCode()),
                () -> assertEquals(user.getName(), user2.getName()),
                () -> assertEquals(user.getAddress(), user2.getAddress()),
                () -> assertEquals(user.getEmail(), user2.getEmail()),
                () -> assertEquals(user.getUnparsedDob(), user2.getUnparsedDob()),
                () -> assertEquals(new String(user.getEncryptedPassword()), new String(user2.getEncryptedPassword()))
        );
        assertDoesNotThrow(() -> user2.authorize("password"));
        assertThrows(AuthorizationException.class, () -> user2.authorize("random pw"));
    }

    @AfterEach
    void tidy() {
        Database.reset();
    }
}