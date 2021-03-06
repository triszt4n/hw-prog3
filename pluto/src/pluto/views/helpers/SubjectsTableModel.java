package pluto.views.helpers;

import pluto.models.SubjectModel;

import javax.swing.table.AbstractTableModel;
import java.util.List;

/***
 * Table model used widely by views in which subjects get to be on display.
 */
public class SubjectsTableModel extends AbstractTableModel {
    private final List<SubjectModel> subjects;

    private static final int DATA_COLUMN_COUNT = 7;
    public enum SubjectColumn {
        PLUTO("Pluto code", String.class),
        NAME("Name", String.class),
        CREDIT("Credit", Integer.class),
        REQUIREMENTS("Requirements", String.class),
        SEMESTER("Semester", Integer.class),
        COORDINATOR("Coordinator", String.class),
        MORE("More", String.class);

        private final String column;
        private final Class<?> _class;

        SubjectColumn(String c, Class<?> cl) {
            this.column = c;
            this._class = cl;
        }

        public static String getColumnAt(int index) {
            SubjectColumn[] array = SubjectColumn.values();
            return array[index].column;
        }

        public static Class<?> getClassAt(int index) {
            SubjectColumn[] array = SubjectColumn.values();
            return array[index]._class;
        }
    }

    public SubjectsTableModel(List<SubjectModel> data) {
        subjects = data;
    }

    @Override
    public int getRowCount() {
        return subjects.size();
    }

    @Override
    public int getColumnCount() {
        return DATA_COLUMN_COUNT;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        SubjectModel subject = subjects.get(rowIndex);
        switch(columnIndex) {
            case 1: return subject.getName();
            case 2: return subject.getCredit();
            case 3: return subject.getRequirements();
            case 4: return subject.getSemester();
            case 5: return subject.getCoordinator().getName();
            case 6: return subject.isOpened()? "Open" : "Closed";
            default: return subject.getPlutoCode();
        }
    }

    @Override
    public String getColumnName(int column) {
        return SubjectColumn.getColumnAt(column);
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return SubjectColumn.getClassAt(columnIndex);
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false;
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) { }


}
