package pluto.views.helpers;

import pluto.models.helpers.CourseType;

import javax.swing.*;
import javax.swing.event.ListDataListener;

/***
 * Checkbox model used widely by views in which courses get to be edited or created.
 */
public class CourseTypeComboBoxModel implements ComboBoxModel<CourseType> {
    private CourseType item;

    public CourseTypeComboBoxModel() {
        item = CourseType.values()[0];
    }

    @Override
    public void setSelectedItem(Object anItem) {
        item = (CourseType) anItem;
    }

    @Override
    public Object getSelectedItem() {
        return item;
    }

    @Override
    public int getSize() {
        return CourseType.COURSE_TYPE_NUMBER;
    }

    @Override
    public CourseType getElementAt(int index) {
        return CourseType.values()[index];
    }

    @Override
    public void addListDataListener(ListDataListener l) { }

    @Override
    public void removeListDataListener(ListDataListener l) { }
}
