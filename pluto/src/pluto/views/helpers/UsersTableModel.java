package pluto.views.helpers;

import pluto.models.UserModel;

import javax.swing.table.AbstractTableModel;
import java.util.List;

/***
 * Table model used widely by views in which users get to be on display.
 */
public class UsersTableModel extends AbstractTableModel {
    private final List<? extends UserModel> users;

    private static final int DATA_COLUMN_COUNT = 6;
    public enum UserColumn {
        PLUTO("Pluto code", String.class),
        NAME("Name", String.class),
        EMAIL("Email address", String.class),
        DOB("Date of Birth", String.class),
        ADDRESS("Address", String.class),
        MORE("More", String.class);

        private final String column;
        private final Class<?> _class;

        UserColumn(String c, Class<?> cl) {
            this.column = c;
            this._class = cl;
        }

        public static String getColumnAt(int index) {
            UserColumn[] array = UserColumn.values();
            return array[index].column;
        }

        public static Class<?> getClassAt(int index) {
            UserColumn[] array = UserColumn.values();
            return array[index]._class;
        }
    }

    public UsersTableModel(List<? extends UserModel> data) {
        users = data;
    }

    @Override
    public int getRowCount() {
        return users.size();
    }

    @Override
    public int getColumnCount() {
        return DATA_COLUMN_COUNT;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        UserModel user = users.get(rowIndex);
        switch(columnIndex) {
            case 1: return user.getName();
            case 2: return user.getEmail();
            case 3: return user.getUnparsedDob();
            case 4: return user.getAddress();
            case 5: return user.getStatus();
            default: return user.getPlutoCode();
        }
    }

    @Override
    public String getColumnName(int column) {
        return UserColumn.getColumnAt(column);
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return UserColumn.getClassAt(columnIndex);
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false;
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) { }
}
