package pluto.models.helpers;

import pluto.exceptions.ValidationException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/***
 * String validation helper class. Can be used based on the Builder design pattern. Must start validation with setting the validatable
 * object in the validate method.
 */
public class StringValidator {
    /***
     * Stores the validatable data object.
     */
    private String data;

    /***
     * Stores the validated Date object for further use.
     */
    private Date formatted;

    /***
     * Optional field to pass to validation exception. Makes validation errors more verbose.
     */
    private String variableName;

    /***
     * Commonly used simplified non-RFC-conform email address regex pattern
     */
    public static final String EMAIL_REGEX_PATTERN = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$";

    public StringValidator() {
        formatted = null;
        data = null;
        variableName = "";
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
     * Initializes the value to be validated with a validator object.
     *
     * @param validatable the value to be validated.
     * @param variableName the name of the variable (for identifying the problem)
     * @return the StringValidator object for building chain
     */
    public StringValidator validate(String validatable, String variableName) {
        data = validatable;
        this.variableName = variableName;
        return this;
    }

    /***
     * Checks if the string is regex conform.
     *
     * @return the StringValidator object for building chain
     * @throws ValidationException if the value is not conform to the regex pattern
     */
    public StringValidator checkRegex(String pattern) throws ValidationException {
        checkNotNull();
        if (!data.matches(pattern)) {
            throw new ValidationException("Invalid input: " + variableName);
        }
        return this;
    }

    /***
     * Checks if validatable's length is in the given interval. Checks for only positive intervals, leave it at
     * 0 or less if you don't want to check one side of the interval.
     *
     * @return the StringValidator object for building chain
     * @throws ValidationException if the value is out of interval
     */
    public StringValidator checkLength(int min, int max) throws ValidationException {
        checkNotNull();
        if (max > 0 && max < data.length()) {
            throw new ValidationException("Input too long: " + variableName);
        }
        if (min > 0 && min > data.length()) {
            throw new ValidationException("Input too short: " + variableName);
        }
        return this;
    }

    /***
     * Checks if the string is valid format of yyyy-MM-dd
     *
     * @return the StringValidator object for building chain
     * @throws ValidationException if the value is not a yyyy-MM-dd date
     */
    public StringValidator checkDate(String dateFormat) throws ValidationException {
        checkNotNull();
        try {
            formatted = (new SimpleDateFormat(dateFormat)).parse(data);
        } catch (ParseException e) {
            throw new ValidationException("Date not conform to date format: " + variableName);
        }
        return this;
    }

    /***
     * Previously validated date gets returned for further use.
     *
     * @return the date validated before
     * @throws NullPointerException if you query the formatted date without checking one before
     */
    public Date returnCheckedDate() throws NullPointerException {
        if (formatted == null) {
            throw new NullPointerException("You did not specify a date to check first");
        }
        return formatted;
    }
 }
