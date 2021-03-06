package pluto.models.helpers;

/***
 * String enumerator for the Student types
 */
public enum UserType {
    STUDENT("Student"),
    INSTRUCTOR("Instructor"),
    ADMINISTRATOR("Administrator");

    private final String name;
    public static int USER_TYPE_NUMBER = 3;

    UserType(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
