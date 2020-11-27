package pluto.models;

public enum UserType {
    INSTRUCTOR("Instructor"),
    STUDENT("Student"),
    ADMINISTRATOR("Administrator");

    private String title;
    UserType(String t) {
        title = t;
    }
}
