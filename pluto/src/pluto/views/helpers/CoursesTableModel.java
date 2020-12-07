package pluto.views.helpers;

import pluto.models.CourseModel;
import pluto.models.StudentModel;
import pluto.models.UserModel;

import javax.swing.table.AbstractTableModel;
import java.util.List;

/***
 * Table model used widely by views in which courses get to be on display.
 */
public class CoursesTableModel extends AbstractTableModel {
    private final List<CourseModel> courses;
    private final UserModel student;

    private static final int DATA_COLUMN_COUNT = 8;
    public enum CourseColumn {
        PLUTO("Pluto code", String.class),
        SHORT_CODE("Short code", String.class),
        SUBJECT("Subject", String.class),
        TYPE("Course type", String.class),
        AVAILABILITY("Availability", String.class),
        INSTRUCTOR("Instructor", String.class),
        NOTES("Notes", Integer.class),
        IS_JOINED("Joined?", Boolean.class);

        private final String column;
        private final Class<?> _class;

        CourseColumn(String c, Class<?> cl) {
            this.column = c;
            this._class = cl;
        }

        public static String getColumnAt(int index) {
            CourseColumn[] array = CourseColumn.values();
            return array[index].column;
        }

        public static Class<?> getClassAt(int index) {
            CourseColumn[] array = CourseColumn.values();
            return array[index]._class;
        }
    }

    public CoursesTableModel(List<CourseModel> data, StudentModel student) {
        courses = data;
        this.student = student;
    }

    @Override
    public int getRowCount() {
        return courses.size();
    }

    @Override
    public int getColumnCount() {
        return DATA_COLUMN_COUNT;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        CourseModel course = courses.get(rowIndex);
        switch(columnIndex) {
            case 1: return course.getShortCode();
            case 2: return course.getSubject().getName();
            case 3: return course.getType().toString();
            case 4: return course.getStudents().size() + "/" + course.getMaxStudents();
            case 5: return course.getInstructor().getName();
            case 6: return course.getNotes();
            case 7: return student != null && course.getStudents().contains(student);
            default: return course.getPlutoCode();
        }
    }

    @Override
    public String getColumnName(int column) {
        return CourseColumn.getColumnAt(column);
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return CourseColumn.getClassAt(columnIndex);
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false;
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) { }


}
