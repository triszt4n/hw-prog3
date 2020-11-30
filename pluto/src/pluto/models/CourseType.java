package pluto.models;

public enum CourseType {
    LECTURE("Lecture"),
    PRACTICE("Practice"),
    SEMINAR("Seminar"),
    LABORATORY("Laboratory");

    private String name;

    CourseType(String name) {
        this.name = name();
    }

    public String getName() {
        return name;
    }
}
