package pluto.models;

import pluto.app.PlutoConsole;
import pluto.database.Database;
import pluto.exceptions.AuthorizationException;
import pluto.exceptions.EntityNotFoundException;
import pluto.exceptions.ValidationException;
import pluto.models.helpers.StringValidator;
import pluto.models.helpers.UserType;

import javax.json.*;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/***
 * User entity representation in the application.
 */
public class UserModel extends AbstractModel {
    /***
     * email address of the user
     */
    protected String email;

    /***
     * name of the user
     */
    protected String name;

    /***
     * encrypted byte array of the original password
     */
    protected byte[] encryptedPassword;

    /***
     * salt to help the security
     */
    protected byte[] salt;

    protected String unparsedDob;

    /***
     * date of birth for the user
     */
    private Date parsedDob;

    /***
     * address of the user
     */
    protected String address;

    /***
     * student: subject of taken courses, instructors: coordinated subjects
     */
    protected List<SubjectModel> mySubjects;

    /***
     * student: courses taken, instructor: instructed courses
     */
    protected List<CourseModel> myCourses;

    private static final String ALGORITHM_FOR_PW_HASHING = "SHA-256";

    /***
     * Validated setter
     * @param email
     * @throws ValidationException
     */
    public void setEmail(String email) throws ValidationException {
        StringValidator sv = new StringValidator();
        sv.validate(email, "Email address")
                .checkLength(3, 200)
                .checkRegex(StringValidator.EMAIL_REGEX_PATTERN);
        this.email = email;
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

    /***
     * Validated setter
     * Sets both of the date of births
     * @param dob String representation
     * @throws ValidationException
     */
    public void setDob(String dob) throws ValidationException {
        StringValidator sv = new StringValidator();
        parsedDob = sv.validate(dob, "Date of birth")
                .checkDate("yyyy-MM-dd")
                .returnCheckedDate();
        unparsedDob = dob;
    }

    /***
     * Validates setter
     * @param address
     * @throws ValidationException
     */
    public void setAddress(String address) throws ValidationException {
        StringValidator sv = new StringValidator();
        sv.validate(address, "Address")
                .checkLength(0, 200);
        this.address = address;
    }

    /***
     * Validated setter
     * Generates the encrypted password as well
     * @param rawPassword
     * @throws ValidationException
     * @throws NoSuchAlgorithmException
     */
    public void setPassword(String rawPassword) throws ValidationException, NoSuchAlgorithmException {
        StringValidator sv = new StringValidator();
        sv.validate(rawPassword, "Password")
                .checkLength(3, 200);

        SecureRandom random = new SecureRandom();
        salt = new byte[16];
        random.nextBytes(salt);
        MessageDigest md = MessageDigest.getInstance(ALGORITHM_FOR_PW_HASHING);
        md.update(salt);
        encryptedPassword = md.digest(rawPassword.getBytes(StandardCharsets.UTF_8));
    }

    public List<SubjectModel> getMySubjects() {
        return mySubjects;
    }

    public List<CourseModel> getMyCourses() {
        return myCourses;
    }

    public String getEmail() {
        return email;
    }
    public String getName() {
        return name;
    }
    public byte[] getEncryptedPassword() {
        return encryptedPassword;
    }
    public Date getParsedDob() {
        return parsedDob;
    }
    public String getUnparsedDob() {
        return unparsedDob;
    }
    public String getAddress() {
        return address;
    }
    public UserType getType() {
        return null;
    }
    public String getStatus() {
        return "";
    }

    /***
     * Need to implement the procedure of loading in the associated courses and subjects of the user.
     */
    public void initCoursesAndSubjects() { }

    /***
     * Needs to manage removal of associations between subjects, courses and users before the database removal.
     * @throws EntityNotFoundException
     */
    public void manageSubjectsAndCoursesBeforeDelete() throws EntityNotFoundException { }

    /**
     * @see AbstractModel
     */
    public void save() {
        if (plutoCode == null) {
            generatePlutoCode();
        }

        Database.addUser(this);
    }

    public UserModel(String email, String name, String pw, String dob, String addr) throws ValidationException, NoSuchAlgorithmException {
        plutoCode = null;
        encryptedPassword = null;

        setEmail(email);
        setName(name);
        setPassword(pw);
        setAddress(addr);
        setDob(dob);
        save();

        mySubjects = new LinkedList<>();
        myCourses = new LinkedList<>();
    }

    public UserModel(String plutoCode, String email, String name, String pw, String dob, String addr) throws ValidationException, NoSuchAlgorithmException {
        encryptedPassword = null;

        this.plutoCode = plutoCode;
        setEmail(email);
        setName(name);
        setPassword(pw);
        setAddress(addr);
        setDob(dob);
        save();

        mySubjects = new LinkedList<>();
        myCourses = new LinkedList<>();
    }

    public UserModel(JsonObject json) throws ValidationException {
        JsonObject userObject = json.getJsonObject("details");

        plutoCode = userObject.getString("pluto");
        setEmail(userObject.getString("email"));
        setName(userObject.getString("name"));
        setAddress(userObject.getString("address"));
        setDob(userObject.getString("dob"));

        JsonObject credentialsObject = userObject.getJsonObject("credentials");

        List<Byte> pwList = credentialsObject.getJsonArray("password").stream()
                .map(b -> Byte.parseByte(b.toString()))
                .collect(Collectors.toList());
        encryptedPassword = new byte[pwList.size()];
        for (int i = 0; i < pwList.size(); ++i) {
            encryptedPassword[i] = pwList.get(i);
        }

        List<Byte> saltList = credentialsObject.getJsonArray("salt").stream()
                .map(b -> Byte.parseByte(b.toString()))
                .collect(Collectors.toList());
        salt = new byte[saltList.size()];
        for (int i = 0; i < saltList.size(); ++i) {
            salt[i] = saltList.get(i);
        }
        save();

        mySubjects = new LinkedList<>();
        myCourses = new LinkedList<>();
    }

    public void addCourse(CourseModel course) {
        myCourses.add(course);
    }

    public void addSubject(SubjectModel subject) {
        mySubjects.add(subject);
    }

    public void removeCourse(CourseModel course) {
        myCourses.remove(course);
    }

    public void removeSubject(SubjectModel subject) {
        mySubjects.remove(subject);
    }

    /***
     * Static method that connects the query to the database.
     * @param pluto pluto code of searched user
     * @return the searched UserModel
     * @throws EntityNotFoundException if the pluto code belongs to nobody
     * @throws ValidationException if the pluto code is invalid
     */
    public static UserModel get(String pluto) throws EntityNotFoundException, ValidationException {
        StringValidator sv = new StringValidator();
        sv.validate(pluto, "Pluto code")
                .checkLength(PLUTO_CODE_LENGTH, PLUTO_CODE_LENGTH)
                .checkRegex("[A-Z0-9]+$");

        UserModel result = Database.getUserWherePlutoCode(pluto);
        if (result == null) {
            throw new EntityNotFoundException("No user found with entered Pluto code");
        }
        return result;
    }

    /***
     * Static method that connects the query to the database
     * @param pluto pluto code of the user to be deleted from database
     * @throws EntityNotFoundException if the pluto code belongs to nobody
     * @throws ValidationException if the pluto code is invalid
     */
    public static void delete(String pluto) throws EntityNotFoundException, ValidationException {
        UserModel user = get(pluto);
        user.manageSubjectsAndCoursesBeforeDelete();
        Database.removeUser(user);
    }

    /***
     * Static method that connects the query to the database (get all the users)
     * @return the UserModels in the database
     */
    public static List<UserModel> all() {
        return Database.getAllUsers();
    }

    /***
     * The method for one user to be updated with the given data (modification)
     * @param email
     * @param name
     * @param pw
     * @param dob
     * @param addr
     * @throws ValidationException
     * @throws NoSuchAlgorithmException
     */
    public void update(String email, String name, String pw, String dob, String addr) throws ValidationException, NoSuchAlgorithmException {
        setEmail(email);
        setName(name);
        if (!pw.equals("")) {
            setPassword(pw);
        }
        setAddress(addr);
        setDob(dob);
    }

    /***
     * Checks if the password is the same as the user's
     * @param pw raw password to be authorized with
     * @throws AuthorizationException if the password is wrong
     * @throws ValidationException is the password is invalid
     * @throws NoSuchAlgorithmException
     */
    public void authorize(String pw) throws AuthorizationException, ValidationException, NoSuchAlgorithmException {
        StringValidator sv = new StringValidator();
        sv.validate(pw, "Password")
                .checkLength(3, 200);

        MessageDigest md = MessageDigest.getInstance(ALGORITHM_FOR_PW_HASHING);
        md.update(salt);
        byte[] pwToCheck = md.digest(pw.getBytes(StandardCharsets.UTF_8));

        if (!Arrays.equals(pwToCheck, encryptedPassword)) {
            throw new AuthorizationException("Wrong password");
        }
    }

    /**
     * @see AbstractModel
     */
    @Override
    public String toString() {
        return PlutoConsole.createLog("[USER] " + plutoCode, email, name, address);
    }

    /**
     * @see AbstractModel
     */
    @Override
    public JsonObject jsonify() {
        JsonArrayBuilder pwBuilder = Json.createArrayBuilder();
        JsonArrayBuilder saltBuilder = Json.createArrayBuilder();
        for (byte b : encryptedPassword) {
            pwBuilder.add(b);
        }
        for (byte b : salt) {
            saltBuilder.add(b);
        }

        return Json.createObjectBuilder()
                .add("pluto", plutoCode)
                .add("email", email)
                .add("name", name)
                .add("dob", unparsedDob)
                .add("address", address)
                .add("credentials", Json.createObjectBuilder()
                        .add("password", pwBuilder.build())
                        .add("salt", saltBuilder.build())
                        .build()
                )
                .build();
    }
}
