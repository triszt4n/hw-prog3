package pluto.controllers;

import pluto.database.Database;
import pluto.exceptions.EntityNotFoundException;
import pluto.exceptions.ValidationException;
import pluto.models.CourseModel;
import pluto.models.InstructorModel;
import pluto.models.StudentModel;
import pluto.models.SubjectModel;
import pluto.models.helpers.UserType;
import pluto.views.*;

import javax.swing.*;
import java.util.List;

public class CourseController extends AbstractController {
    private SubjectController subjectController;
    private SubjectModel currentSubject;

    public void setSubjectController(SubjectController subjectController) {
        this.subjectController = subjectController;
    }

    public void setCurrentSubject(SubjectModel subject) {
        currentSubject = subject;
    }

    @Override
    public void index() {
        if (loggedInUser.getType().equals(UserType.INSTRUCTOR)) {
            openChildPage(new CourseIndexInstructorView(loggedInUser.getMyCourses(), this, subjectController));
        }
        else if (loggedInUser.getType().equals(UserType.STUDENT)) {
            openChildPage(new CourseIndexStudentView(loggedInUser.getMyCourses(), this, subjectController, (StudentModel)loggedInUser));
        }
        else {
            openChildPage(new CourseIndexInstructorView(CourseModel.all(), this, subjectController));
        }
    }

    @Override
    public void build() {
        List<InstructorModel> instructors = Database.getAllInstructors();
        if (loggedInUser != currentSubject.getCoordinator()) {
            JOptionPane.showMessageDialog(null, "You have no permission to add course to this subject!",
                    "No permission", JOptionPane.INFORMATION_MESSAGE);
        }
        else if (instructors.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Can't create course without instructors in the database!",
                    "No permission", JOptionPane.INFORMATION_MESSAGE);
        }
        else {
            openChildPage(new CourseBuildView(this, currentSubject, instructors));
        }
    }

    @Override
    public void create() {
        CourseBuildView buildPage = (CourseBuildView) pageStack.peek();
        try {
            new CourseModel(
                    buildPage.getShortCodeField(),
                    buildPage.getTypeCombo(),
                    buildPage.getMaxStudentsField(),
                    buildPage.getNotesArea(),
                    currentSubject,
                    buildPage.getInstructorCombo()
            );
            JOptionPane.showMessageDialog(null, "Course successfully created");
            closeChildPage();
        } catch (ValidationException e) {
            JOptionPane.showMessageDialog(null, "Error creating course: " + e.getMessage(),
                    "Validation error", JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    public void edit(String pluto) {
        try {
            CourseModel course = CourseModel.get(pluto);
            if (course.getSubject().getCoordinator() != loggedInUser) {
                JOptionPane.showMessageDialog(null, "You have no permission to edit this course!",
                        "No permission", JOptionPane.ERROR_MESSAGE);
            }
            else {
                openChildPage(new CourseEditView(this, course, Database.getAllInstructors()));
            }
        } catch (EntityNotFoundException e) {
            JOptionPane.showMessageDialog(null, "Error editing course: " + e.getMessage(),
                    "Validation error", JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    public void update(String pluto) {
        CourseEditView editPage = (CourseEditView) pageStack.peek();
        try {
            CourseModel course = CourseModel.get(pluto);
            course.update(
                    editPage.getShortCodeField(),
                    editPage.getTypeCombo(),
                    editPage.getMaxStudentsField(),
                    editPage.getNotesArea(),
                    editPage.getInstructorCombo()
            );
            closeChildPage();
        } catch (ValidationException | EntityNotFoundException e) {
            JOptionPane.showMessageDialog(null, "Error updating course: " + e.getMessage(),
                    "Validation error", JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    public void delete(String pluto) {
        int input = JOptionPane.showConfirmDialog(null, "Confirm deleting selected course?");
        if (input == JOptionPane.YES_OPTION) {
            try {
                CourseModel.delete(pluto);
            } catch (EntityNotFoundException e) {
                JOptionPane.showMessageDialog(null, "Error deleting course: " + e.getMessage(),
                        "Not found error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    @Override
    public void show(String pluto) {
        try {
            CourseModel course = CourseModel.get(pluto);
            openChildPage(new CourseShowView(course, this));
        } catch (EntityNotFoundException e) {
            JOptionPane.showMessageDialog(null, "Error showing students of course: " + e.getMessage(),
                    "Not found error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void join(String pluto) {
        int input = JOptionPane.showConfirmDialog(null,
                "Take this course?");
        if (input == JOptionPane.YES_OPTION) {
            try {
                CourseModel course = CourseModel.get(pluto);
                course.addStudent((StudentModel) loggedInUser);
                loggedInUser.addCourse(course);
            } catch (EntityNotFoundException | ValidationException e) {
                JOptionPane.showMessageDialog(null, "Error joining course: " + e.getMessage(),
                        "Not found error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public void drop(String pluto) {
        int input = JOptionPane.showConfirmDialog(null,
                "Are you sure you want to drop the selected course?");
        if (input == JOptionPane.YES_OPTION) {
            try {
                CourseModel course = CourseModel.get(pluto);
                course.removeStudent((StudentModel) loggedInUser);
                loggedInUser.removeCourse(course);
            } catch (EntityNotFoundException e) {
                JOptionPane.showMessageDialog(null, "Error dropping course: " + e.getMessage(),
                        "Not found error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public void kick(String pluto, StudentModel student) {
        int input = JOptionPane.showConfirmDialog(null,
                "Are you sure you want to kick the selected student from this course?");
        if (input == JOptionPane.YES_OPTION) {
            try {
                CourseModel course = CourseModel.get(pluto);
                course.removeStudent(student);
                student.removeCourse(course);
            } catch (EntityNotFoundException e) {
                JOptionPane.showMessageDialog(null, "Error kicking student from course: " + e.getMessage(),
                        "Not found error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
