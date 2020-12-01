package pluto.views;

import pluto.controllers.CourseController;
import pluto.models.CourseModel;
import pluto.models.InstructorModel;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class CourseEditView extends CourseBuildView {
    private final CourseModel course;

    private void editComponents() {
        main.setTitle("Pluto | Edit course");
        promptLabel.setText("Edit course (" + course.getPlutoCode() + ") of subject: " + subject.getName());
        shortCodeField.setText(course.getShortCode());
        maxStudentsField.setText(String.valueOf(course.getMaxStudents()));
        typeCombo.setSelectedItem(course.getType());
        instructorCombo.setSelectedItem(course.getInstructor());
        notesArea.setText(course.getNotes());
    }

    public CourseEditView(CourseController courseController, CourseModel course, List<InstructorModel> instructors) {
        super(courseController, course.getSubject(), instructors);
        this.course = course;
        editComponents();
    }

    @Override
    protected void initListeners() {
        backBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                courseController.back();
            }
        });

        saveBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                courseController.update(course.getPlutoCode());
            }
        });
    }
}
