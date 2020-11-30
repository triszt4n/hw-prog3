package pluto.models;

public class CourseModel extends AbstractModel {
    private String shortCode;
    private CourseType type;
    private String notes;

    private SubjectModel subject;

    @Override
    protected void save() {

    }
}
