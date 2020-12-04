package pluto.models.helpers;

import pluto.exceptions.ValidationException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/***
 * String validation helper class.
 */
public class StringValidator {
    private String data;
    private Date formatted;
    private String variName;

    public static final String EMAIL_REGEX_PATTERN = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$";

    public StringValidator() {
        formatted = null;
        data = null;
        variName = "";
    }

    /***
     * Initializes the value to be validated with a validator object
     * @param validatable the value to be validated
     * @return the StringValidator object for building chain
     */
    public StringValidator validate(String validatable) {
        data = validatable;
        return this;
    }

    /***
     * @return the StringValidator object for building chain
     * @throws ValidationException if the value if null
     */
    public StringValidator checkNotNull() throws ValidationException {
        if (data == null) {
            throw new ValidationException("Can't validate null, use .validate(data) to start validating");
        }
        return this;
    }

    /***
     * Initializes the value to be validated with a validator object
     * @param validatable the value to be validated.
     * @param variableName the name of the variable (for identifying the problem)
     * @return the StringValidator object for building chain
     */
    public StringValidator validate(String validatable, String variableName) {
        data = validatable;
        variName = variableName;
        return this;
    }

    /***
     * @return the StringValidator object for building chain
     * @throws ValidationException if the value is not conform to the regex pattern
     */
    public StringValidator checkRegex(String pattern) throws ValidationException {
        checkNotNull();
        if (!data.matches(pattern)) {
            throw new ValidationException("Invalid input: " + variName);
        }
        return this;
    }

    /***
     * Checks only positive intervals
     * @return the StringValidator object for building chain
     * @throws ValidationException if the value is out of interval
     */
    public StringValidator checkLength(int min, int max) throws ValidationException {
        checkNotNull();
        if (max > 0 && max < data.length()) {
            throw new ValidationException("Input too long: " + variName);
        }
        if (min > 0 && min > data.length()) {
            throw new ValidationException("Input too short: " + variName);
        }
        return this;
    }

    /***
     * @return the StringValidator object for building chain
     * @throws ValidationException if the value is not a yyyy-MM-dd date
     */
    public StringValidator checkDate(String dateFormat) throws ValidationException {
        checkNotNull();
        try {
            formatted = (new SimpleDateFormat(dateFormat)).parse(data);
        } catch (ParseException e) {
            throw new ValidationException("Date not conform to date format: " + variName);
        }
        return this;
    }

    /***
     * @return the date validated before
     */
    public Date returnCheckedDate() throws NullPointerException {
        if (formatted == null) {
            throw new NullPointerException("You did not specify a date to check first");
        }
        return formatted;
    }
 }
