package pluto.models.validators;

import pluto.models.exceptions.ValidationException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class StringValidator {
    private String data;
    private Date formatted;
    private String variName;

    public StringValidator() {
        formatted = null;
        data = null;
        variName = "";
    }

    public StringValidator validate(String validatable) {
        data = validatable;
        return this;
    }

    public StringValidator checkNotNull() throws ValidationException {
        if (data == null) {
            throw new ValidationException("Can't validate null, use .validate(data) to start validating");
        }
        return this;
    }

    public StringValidator validate(String validatable, String variableName) {
        data = validatable;
        variName = variableName;
        return this;
    }

    public StringValidator checkRegex(String pattern) throws ValidationException {
        checkNotNull();
        if (!data.matches(pattern)) {
            throw new ValidationException("Invalid input: " + variName);
        }
        return this;
    }

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

    public StringValidator checkDate(String dateFormat) throws ValidationException {
        checkNotNull();
        try {
            formatted = (new SimpleDateFormat(dateFormat)).parse(data);
        } catch (ParseException e) {
            throw new ValidationException("Date not conform to date format: " + variName);
        }
        return this;
    }

    public Date returnCheckedDate() throws NullPointerException {
        if (formatted == null) {
            throw new NullPointerException("You did not specify a date to check first");
        }
        return formatted;
    }
 }
