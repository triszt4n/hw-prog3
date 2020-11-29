package pluto.views.helpers;

import pluto.models.SubjectModel;

import javax.swing.table.AbstractTableModel;
import java.util.List;

public class SubjectsTableModel extends AbstractTableModel {
    private List<SubjectModel> subjects;

    private static final int DATA_COLUMN_COUNT = 6;
    public enum SubjectColumn {
        PLUTO("Pluto code", String.class),
        NAME("Name", String.class),
        CREDIT("Credit", Integer.class),
        REQUIREMENTS("Requirements", String.class),
        SEMESTER("Semester", Integer.class),
        COORDINATOR("Coordinator", String.class);

        private final String column;
        private final Class _class;

        SubjectColumn(String c, Class cl) {
            this.column = c;
            this._class = cl;
        }

        public static String getColumnAt(int index) {
            SubjectsTableModel.SubjectColumn[] array = SubjectsTableModel.SubjectColumn.values();
            return array[index].column;
        }

        public static Class getClassAt(int index) {
            SubjectsTableModel.SubjectColumn[] array = SubjectsTableModel.SubjectColumn.values();
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
        return null;
    }

    @Override
    public String getColumnName(int column) {
        return SubjectsTableModel.SubjectColumn.getColumnAt(column);
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return SubjectsTableModel.SubjectColumn.getClassAt(columnIndex);
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false;
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) { }


}
