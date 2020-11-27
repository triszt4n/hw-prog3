package pluto.models;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

public class UserModel extends AbstractModel {
    private static final int PLUTO_CODE_LENGTH = 12;
    private String plutoCode;
    private String email;
    private String name;
    private String unsecurePw;
    private byte[] securePw;
    private byte[] salt;
    private String unparsedDob;
    private Date dob;
    private String address;

    public String getPlutoCode() {
        return plutoCode;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public Date getDob() {
        return dob;
    }

    public String getAddress() {
        return address;
    }

    private static final String PLUTO_CODE_CHARSET = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";

    private void generatePlutoCode() {
        SecureRandom rnd = new SecureRandom();

        StringBuilder sb = new StringBuilder(PLUTO_CODE_LENGTH);
        for (int i = 0; i < PLUTO_CODE_LENGTH; i++) {
            sb.append(PLUTO_CODE_CHARSET.charAt(rnd.nextInt(PLUTO_CODE_CHARSET.length())));
        }
        plutoCode = sb.toString();
    }

    private static final String ALGORITHM_FOR_PW_HASHING = "SHA-256";

    private void generateSecurePw() throws NoSuchAlgorithmException {
        SecureRandom random = new SecureRandom();
        salt = new byte[16];
        random.nextBytes(salt);
        MessageDigest md = MessageDigest.getInstance(ALGORITHM_FOR_PW_HASHING);
        md.update(salt);
        securePw = md.digest(unsecurePw.getBytes(StandardCharsets.UTF_8));
    }

    private void validate() throws ValidationException {
        if (email.length() < 3) {
            throw new ValidationException("Email address too short");
        }
        if (!email.matches("^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$")) {
            throw new ValidationException("Invalid email address");
        }
        if (name.length() < 3) {
            throw new ValidationException("Name too short");
        }
        if (unsecurePw.length() < 3) {
            throw new ValidationException("Password too short");
        }
        try {
            dob = (new SimpleDateFormat("yyyy-MM-dd")).parse(unparsedDob);
        } catch (ParseException e) {
            throw new ValidationException("Invalid date of birth");
        }
        try {
            generateSecurePw();
        }
        catch (NoSuchAlgorithmException e) {
            throw new ValidationException("Couldn't encrypt password, no such algorithm: " + ALGORITHM_FOR_PW_HASHING);
        }
        generatePlutoCode();
    }

    private void save() {
        db.addUser(this);
    }

    public UserModel(String em, String na, String pw, String d, String addr) throws ValidationException {
        email = em;
        name = na;
        unsecurePw = pw;
        unparsedDob = d;
        address = addr;

        validate();
        save();
    }

    //used by binarysearch in getting
    public UserModel(String pluto) {
        plutoCode = pluto;
    }

    public class AuthorizationException extends Exception {
        public AuthorizationException(String msg) {
            super(msg);
        }
    }

    public static UserModel get(String pluto) throws EntityNotFoundException {
        UserModel result = db.getUserWherePlutoCode(pluto);
        if (result == null) {
            throw new EntityNotFoundException("No user found with entered Pluto code");
        }
        return result;
    }

    public void authorize(String pw) throws AuthorizationException {
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance(ALGORITHM_FOR_PW_HASHING);
        } catch (NoSuchAlgorithmException e) {
            throw new AuthorizationException("Couldn't encrypt password, no such algorithm: " + ALGORITHM_FOR_PW_HASHING);
        }
        md.update(salt);
        byte[] pwToCheck = md.digest(pw.getBytes(StandardCharsets.UTF_8));

        if (!Arrays.equals(pwToCheck, securePw)) {
            throw new AuthorizationException("Wrong password");
        }
    }
}
