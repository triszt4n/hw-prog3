package pluto.models.helpers;

/***
 * String enumerator for the Course types
 */
public enum CourseType {
    LECTURE("Lecture"),
    PRACTICE("Practice"),
    SEMINAR("Seminar"),
    LABORATORY("Laboratory");

    private final String name;
    public static int COURSE_TYPE_NUMBER = 4;

    CourseType(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
