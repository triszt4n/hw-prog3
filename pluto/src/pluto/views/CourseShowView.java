package pluto.views;

import pluto.controllers.CourseController;
import pluto.models.CourseModel;
import pluto.models.StudentModel;
import pluto.models.UserModel;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * "show" view for course resource
 * Actually shows the students that took this course
 * @see pluto.controllers.CourseController
 */
public class CourseShowView extends UserIndexView {
    private final CourseController courseController;
    private final CourseModel course;

    private void editComponents() {
        setTitle("Pluto | Manage students of course");
        promptLabel.setText("Joined students of course \"" + course.getShortCode() +
                "\" (" + course.getPlutoCode() +
                ") from subject \"" + course.getSubject().getName() + "\"");
        editBtn.setVisible(false);
        acceptCheck.setVisible(false);
        deleteBtn.setText("Kick student");
    }

    @Override
    protected void initListeners() {
        ListSelectionModel selectionModel = table.getSelectionModel();
        selectionModel.addListSelectionListener(e -> {
            if (table.getSelectedRow() != -1) {
                deleteBtn.setEnabled(true);
                UserModel user = users.get(table.getSelectedRow());
                plutoLabel.setText(user.getName() + " (" + user.getPlutoCode() + ")");
            }
            else {
                deleteBtn.setEnabled(false);
                plutoLabel.setText("");
            }
        });

        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent mouseEvent) {
                if (mouseEvent.getClickCount() == 2 && table.getSelectedRow() != -1) {
                    courseController.kick(course.getPlutoCode(), course.getStudents().get(table.getSelectedRow()));
                    data.fireTableDataChanged();
                }
            }
        });

        deleteBtn.addActionListener(e -> {
            courseController.kick(course.getPlutoCode(), (StudentModel) users.get(table.getSelectedRow()));
            data.fireTableDataChanged();
        });

        backBtn.addActionListener(e -> courseController.back());
    }

    public CourseShowView(CourseModel course, CourseController courseController) {
        super(course.getStudents(), null);
        this.course = course;
        this.courseController = courseController;
        editComponents();
    }
}
