package pluto.views;

import pluto.controllers.CourseController;
import pluto.models.CourseModel;
import pluto.models.StudentModel;
import pluto.models.SubjectModel;
import pluto.views.helpers.CoursesTableModel;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;

/***
 * View for showing the courses of one subject.
 * inherits the course index for student, but with different data and more function (join).
 */
public class CourseOfSubjectStudentView extends CourseIndexStudentView {
    private final SubjectModel subject;
    private JButton joinBtn;

    private void editComponents() {
        promptPanel = new JPanel(new BorderLayout());
        promptPanel.setMinimumSize(new Dimension(200, 160));
        JLabel subjectLabel = new JLabel("Courses of Subject: " + subject.getName() +
                ", " + subject.getPlutoCode() +
                " - " + (subject.isOpened()? "Open" : "Closed"));
        JLabel coordinatorLabel = new JLabel("Coordinator: " + subject.getCoordinator().getName());
        JLabel othersLabel = new JLabel("Recommended semester: " + subject.getSemester() +
                ", Credit: " + subject.getCredit() +
                ", Requirements: " + subject.getRequirements());
        promptPanel.add(subjectLabel, BorderLayout.NORTH);
        promptPanel.add(coordinatorLabel, BorderLayout.CENTER);
        promptPanel.add(othersLabel, BorderLayout.SOUTH);
        promptPanel.setBorder(
                new EmptyBorder(20,50,20,50)
        );
        add(promptPanel, BorderLayout.NORTH);

        dropBtn.setEnabled(true);
        joinBtn.setEnabled(true);
        dropBtn.setVisible(false);
        joinBtn.setVisible(false);
        showSubjectBtn.setVisible(false);
    }

    private void manageJoinAndDropButtons() {
        if ((Boolean)table.getValueAt(table.getSelectedRow(), CoursesTableModel.CourseColumn.IS_JOINED.ordinal())) {
            dropBtn.setVisible(true);
            joinBtn.setVisible(false);
        }
        else {
            dropBtn.setVisible(false);
            joinBtn.setVisible(true);
        }
    }

    @Override
    protected void initListeners() {
        joinBtn = new JButton("Join course");
        actionPanel.add(joinBtn);

        ListSelectionModel selectionModel = table.getSelectionModel();
        selectionModel.addListSelectionListener(e -> {
            if (table.getSelectedRow() != -1) {
                manageJoinAndDropButtons();
                CourseModel course = courses.get(table.getSelectedRow());
                nameLabel.setText(course.getShortCode() + " (" + course.getType().toString() + ")" + " - by " + course.getInstructor().getName());
            }
            else {
                dropBtn.setVisible(false);
                joinBtn.setVisible(false);
                nameLabel.setText("");
            }
        });

        backBtn.addActionListener(e -> courseController.back());

        dropBtn.addActionListener(e -> {
            String pluto = (String) table.getValueAt(table.getSelectedRow(), CoursesTableModel.CourseColumn.PLUTO.ordinal());
            courseController.drop(pluto);
            data.fireTableDataChanged();
        });

        joinBtn.addActionListener(e -> {
            String pluto = (String) table.getValueAt(table.getSelectedRow(), CoursesTableModel.CourseColumn.PLUTO.ordinal());
            courseController.join(pluto);
            data.fireTableDataChanged();
        });
    }

    public CourseOfSubjectStudentView(List<CourseModel> courses, SubjectModel subject, CourseController courseController, StudentModel student) {
        super(courses, courseController, null, student);
        setTitle("Pluto | Administration: Courses of subject (Student mode)");
        this.subject = subject;
        courseController.setCurrentSubject(subject);
        editComponents();
    }
}
