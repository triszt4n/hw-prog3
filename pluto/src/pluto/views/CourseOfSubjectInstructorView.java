package pluto.views;

import pluto.controllers.CourseController;
import pluto.models.CourseModel;
import pluto.models.SubjectModel;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

/***
 * View for showing the courses of one subject.
 * inherits the course index for instructor, but with different data.
 */
public class CourseOfSubjectInstructorView extends CourseIndexInstructorView {
    private final SubjectModel subject;

    protected void editComponents() {
        JButton createBtn = new JButton("New course");
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
        promptPanel.add(createBtn, BorderLayout.EAST);
        promptPanel.setBorder(
                new EmptyBorder(20,50,20,50)
        );
        main.add(promptPanel, BorderLayout.NORTH);

        createBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                courseController.build();
            }
        });

        showSubjectBtn.setVisible(false);
    }

    public CourseOfSubjectInstructorView(List<CourseModel> courses, SubjectModel subject, CourseController courseController) {
        super(courses, courseController, null);
        main.setTitle("Pluto | Administration: Courses of subject (Instructor mode)");
        this.subject = subject;
        courseController.setCurrentSubject(subject);
        editComponents();
    }
}
