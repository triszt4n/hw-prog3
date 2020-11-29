package pluto.models;

import pluto.app.PlutoConsole;
import pluto.database.Database;
import pluto.exceptions.AuthorizationException;
import pluto.exceptions.EntityNotFoundException;
import pluto.exceptions.ValidationException;
import pluto.models.validators.StringValidator;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class UserModel extends AbstractModel {
    private String email;
    private String name;
    private String rawPassword;
    private byte[] encryptedPassword;
    private byte[] salt;
    private String unparsedDob;
    private Date parsedDob;
    private String address;

    public void setEmail(String email) throws ValidationException {
        StringValidator sv = new StringValidator();
        sv.validate(email, "Email address")
                .checkLength(3, 200)
                .checkRegex(StringValidator.EMAIL_REGEX_PATTERN);
        this.email = email;
    }

    public void setName(String name) throws ValidationException {
        StringValidator sv = new StringValidator();
        sv.validate(name, "Name")
                .checkLength(3, 200);
        this.name = name;
    }

    public void setDob(String dob) throws ValidationException {
        StringValidator sv = new StringValidator();
        parsedDob = sv.validate(dob, "Date of birth")
                .checkDate("yyyy-MM-dd")
                .returnCheckedDate();
        unparsedDob = dob;
    }

    public void setAddress(String address) throws ValidationException {
        StringValidator sv = new StringValidator();
        sv.validate(address, "Address")
                .checkLength(0, 200);
        this.address = address;
    }

    public void setRawPassword(String rawPassword) throws ValidationException, NoSuchAlgorithmException {
        /// No need to change password, if already exists and no new password passed
        if (this.rawPassword != null && rawPassword.equals("")) {
            return;
        }

        StringValidator sv = new StringValidator();
        sv.validate(rawPassword, "Password")
                .checkLength(3, 200);
        this.rawPassword = rawPassword;

        generateSecurePw();
    }

    protected List<SubjectModel> mySubjects;
    protected List<CourseModel> myCourses;

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
    public Date getParsedDob() {
        return parsedDob;
    }
    public String getUnparsedDob() {
        return unparsedDob;
    }
    public String getAddress() {
        return address;
    }

    private static final String ALGORITHM_FOR_PW_HASHING = "SHA-256";

    private void generateSecurePw() throws NoSuchAlgorithmException {
        SecureRandom random = new SecureRandom();
        salt = new byte[16];
        random.nextBytes(salt);
        MessageDigest md = MessageDigest.getInstance(ALGORITHM_FOR_PW_HASHING);
        md.update(salt);
        encryptedPassword = md.digest(rawPassword.getBytes(StandardCharsets.UTF_8));
    }

    protected void initMySubjects() {
        mySubjects = new LinkedList<>();
    }

    protected void initMyCourses() {
        myCourses = new LinkedList<>();
    }

    @Override
    public int getIndex() {
        return Database.getCurrentIndexOfUser(this);
    }

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
        setRawPassword(pw);
        setAddress(addr);
        setDob(dob);

        initMySubjects();
        initMyCourses();
        save();
    }

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

    public static UserModel get(int index) throws EntityNotFoundException {
        UserModel result = Database.getUserWhereIndex(index);
        if (result == null) {
            throw new EntityNotFoundException("No user found under this index");
        }
        return result;
    }

    public static void delete(int index) throws EntityNotFoundException {
        boolean success = Database.deleteUserWhereIndex(index);
        if (!success) {
            throw new EntityNotFoundException("Couldn't delete user, not found under index");
        }
    }

    public static List<UserModel> all() {
        return Database.getUsers();
    }

    public void update(String email, String name, String pw, String dob, String addr) throws ValidationException, NoSuchAlgorithmException {
        encryptedPassword = null;

        setEmail(email);
        setName(name);
        setRawPassword(pw);
        setAddress(addr);
        setDob(dob);
    }

    public void authorize(String pw) throws AuthorizationException, ValidationException, NoSuchAlgorithmException {
        StringValidator sv = new StringValidator();
        sv.validate(pw, "Password")
                .checkLength(3, 200);

        MessageDigest md = null;
        md = MessageDigest.getInstance(ALGORITHM_FOR_PW_HASHING);
        md.update(salt);
        byte[] pwToCheck = md.digest(pw.getBytes(StandardCharsets.UTF_8));

        if (!Arrays.equals(pwToCheck, encryptedPassword)) {
            throw new AuthorizationException("Wrong password");
        }
    }

    public String getTitle() {
        return null;
    }

    public String getStatus() {
        return "";
    }

    @Override
    public String toString() {
        return PlutoConsole.createLog("[USER] " + plutoCode, email, name, rawPassword, address);
    }
}
