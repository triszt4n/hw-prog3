package pluto.views.helpers;

import pluto.models.InstructorModel;

import javax.swing.*;
import javax.swing.event.ListDataListener;
import java.util.List;

/***
 * Checkbox model used widely by views in which instructors get to be edited or created.
 */
public class InstructorComboBoxModel implements ComboBoxModel<InstructorModel> {
    private InstructorModel item;
    private final List<InstructorModel> instructors;

    public InstructorComboBoxModel(List<InstructorModel> instructors) {
        this.instructors = instructors;
        item = instructors.get(0);
    }

    @Override
    public void setSelectedItem(Object anItem) {
        item = (InstructorModel) anItem;
    }

    @Override
    public Object getSelectedItem() {
        return item;
    }

    @Override
    public int getSize() {
        return instructors.size();
    }

    @Override
    public InstructorModel getElementAt(int index) {
        return instructors.get(index);
    }

    @Override
    public void addListDataListener(ListDataListener l) { }

    @Override
    public void removeListDataListener(ListDataListener l) { }
}
