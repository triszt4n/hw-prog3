package pluto.models;

import pluto.app.PlutoConsole;
import pluto.models.exceptions.AuthorizationException;
import pluto.models.exceptions.EntityNotFoundException;
import pluto.models.exceptions.ValidationException;
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
    private static final int PLUTO_CODE_LENGTH = 12;
    private String plutoCode;
    private String email;
    private String name;
    private String rawPassword;
    private byte[] encryptedPassword;
    private byte[] salt;
    private String unparsedDob;
    private Date parsedDob;
    private String address;

    private List<SubjectModel> mySubjects;
    private List<CourseModel> myCourses;

    public String getPlutoCode() {
        return plutoCode;
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
    public String getAddress() {
        return address;
    }

    private static final String PLUTO_CODE_CHARSET = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String ALGORITHM_FOR_PW_HASHING = "SHA-256";

    private void generatePlutoCode() {
        SecureRandom rnd = new SecureRandom();

        StringBuilder sb = new StringBuilder(PLUTO_CODE_LENGTH);
        for (int i = 0; i < PLUTO_CODE_LENGTH; i++) {
            sb.append(PLUTO_CODE_CHARSET.charAt(rnd.nextInt(PLUTO_CODE_CHARSET.length())));
        }
        plutoCode = sb.toString();
    }

    private void generateSecurePw() throws NoSuchAlgorithmException {
        SecureRandom random = new SecureRandom();
        salt = new byte[16];
        random.nextBytes(salt);
        MessageDigest md = MessageDigest.getInstance(ALGORITHM_FOR_PW_HASHING);
        md.update(salt);
        encryptedPassword = md.digest(rawPassword.getBytes(StandardCharsets.UTF_8));
    }

    protected void validate() throws ValidationException {
        StringValidator sv = new StringValidator();
        sv.validate(email, "Email address")
                .checkLength(3, 200)
                .checkRegex("^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$");
        sv.validate(name, "Name")
                .checkLength(3, 200);
        sv.validate(rawPassword, "Password")
                .checkLength(3, 200);
        parsedDob = sv.validate(unparsedDob, "Date of birth")
                .checkDate("yyyy-MM-dd")
                .returnCheckedDate();
        sv.validate(address, "Address")
                .checkLength(0, 200);
    }

    public void save() throws ValidationException {
        if (plutoCode == null) {
            generatePlutoCode();
            try {
                generateSecurePw();
            }
            catch (NoSuchAlgorithmException e) {
                throw new ValidationException("Couldn't encrypt password, no such algorithm: " + e.getMessage() + " " + ALGORITHM_FOR_PW_HASHING);
            }
        }

        db.addUser(this);
    }

    public UserModel(String em, String na, String pw, String dob, String addr) throws ValidationException {
        plutoCode = null;
        email = em;
        name = na;
        encryptedPassword = null;
        rawPassword = pw;
        unparsedDob = dob;
        address = addr;
        myCourses = new LinkedList<>();
        mySubjects = new LinkedList<>();
        validate();
        save();
    }

    //used by binarysearch in getting
    public UserModel(String pluto) {
        plutoCode = pluto;
    }

    public static UserModel get(String pluto) throws EntityNotFoundException, ValidationException {
        StringValidator sv = new StringValidator();
        sv.validate(pluto, "Pluto code")
                .checkLength(PLUTO_CODE_LENGTH, PLUTO_CODE_LENGTH)
                .checkRegex("[A-Z0-9]+$");

        UserModel result = db.getUserWherePlutoCode(pluto);
        if (result == null) {
            throw new EntityNotFoundException("No user found with entered Pluto code");
        }
        return result;
    }

    public void update(String em, String na, String pw, String dob, String addr) throws ValidationException {
        email = em;
        name = na;
        encryptedPassword = null;
        rawPassword = pw;
        unparsedDob = dob;
        address = addr;
        validate();
        try {
            generateSecurePw();
        }
        catch (NoSuchAlgorithmException e) {
            throw new ValidationException("Couldn't encrypt password " + e.getMessage() + " " + ALGORITHM_FOR_PW_HASHING);
        }
        PlutoConsole.taglessLog(this.toString());
    }

    public void authorize(String pw) throws AuthorizationException, ValidationException {
        StringValidator sv = new StringValidator();
        sv.validate(pw, "Password")
                .checkLength(3, 200);

        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance(ALGORITHM_FOR_PW_HASHING);
        } catch (NoSuchAlgorithmException e) {
            throw new AuthorizationException("Couldn't encrypt password, no such algorithm: " + ALGORITHM_FOR_PW_HASHING);
        }
        md.update(salt);
        byte[] pwToCheck = md.digest(pw.getBytes(StandardCharsets.UTF_8));

        if (!Arrays.equals(pwToCheck, encryptedPassword)) {
            throw new AuthorizationException("Wrong password");
        }
    }

    public String getTitle() {
        return null;
    }

    @Override
    public String toString() {
        return PlutoConsole.createLog("[USER] " + plutoCode, email, name, rawPassword, address);
    }
}
