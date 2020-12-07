package pluto.controllers;

import pluto.database.Database;
import pluto.exceptions.EntityNotFoundException;
import pluto.exceptions.ValidationException;
import pluto.models.StudentModel;
import pluto.models.SubjectModel;
import pluto.models.helpers.UserType;
import pluto.views.*;

import javax.swing.*;

/***
 * The controller class for controlling the Subjects, it accepts interactions from the end user.
 * Implements basic resource management (CRUD) methods.
 */
public class SubjectController extends AbstractController {
    /***
     * Currently used course controller in the application.
     */
    private CourseController courseController;

    /***
     * Simple setter. Sets up the already instantiated CourseController in application for use by this SubjectController.
     * @param courseController the CourseController in application
     */
    public void setCourseController(CourseController courseController) {
        this.courseController = courseController;
    }

    /***
     * Path method for taking courses. This shows all the subjects in the database, student can go on choosing one and
     * take courses of the subject.
     */
    @Override
    public void index() {
        openChildPage(new SubjectIndexView(SubjectModel.all(), this));
    }

    /***
     * Path method for the users to show only their subjects. Student's see the subjects, of which courses they've taken,
     * Instructor see the subjects they control, and Administrators see all the subjects.
     */
    public void allForLoggedInUser() {
        if (loggedInUser.getType().equals(UserType.INSTRUCTOR)) {
            openChildPage(new SubjectIndexInstructorView(loggedInUser.getMySubjects(), loggedInUser, this));
        }
        else if (loggedInUser.getType().equals(UserType.STUDENT)) {
            openChildPage(new SubjectIndexStudentView(loggedInUser.getMySubjects(), this));
        }
        else {
            openChildPage(new SubjectIndexInstructorView(SubjectModel.all(), loggedInUser, this));
        }
    }

    /***
     * AbstractController's method with the same name, applied to Subjects as the resource.
     * Watches out for permissions!
     *
     * @see AbstractController
     */
    @Override
    public void build() {
        if (loggedInUser.getType().equals(UserType.STUDENT)) {
            JOptionPane.showMessageDialog(null, "Can't create subject as a student!", "No permission", JOptionPane.INFORMATION_MESSAGE);
        }
        else {
            openChildPage(new SubjectBuildView(this));
        }
    }

    /***
     * AbstractController's method with the same name, applied to Subjects as the resource.
     *
     * @see AbstractController
     */
    @Override
    public void create() {
        SubjectBuildView buildPage = (SubjectBuildView) pageStack.peek();
        try {
            new SubjectModel(
                    buildPage.getNameField(),
                    buildPage.getCreditField(),
                    buildPage.getReqField(),
                    buildPage.getSemesterField(),
                    loggedInUser,
                    buildPage.getIsOpenedCheck()
            );
            JOptionPane.showMessageDialog(null, "Subject successfully created");
            closeChildPage();
        } catch (ValidationException e) {
            JOptionPane.showMessageDialog(null, "Error creating subject: " + e.getMessage(), "Validation error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /***
     * AbstractController's method with the same name, applied to Subjects as the resource.
     *
     * @see AbstractController
     */
    @Override
    public void edit(String pluto) {
        try {
            SubjectModel subject = SubjectModel.get(pluto);
            openChildPage(new SubjectEditView(subject, this));
        } catch (EntityNotFoundException e) {
            JOptionPane.showMessageDialog(null, "Error editing subject: " + e.getMessage(), "Validation error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /***
     * AbstractController's method with the same name, applied to Subjects as the resource.
     *
     * @see AbstractController
     */
    @Override
    public void update(String pluto) {
        SubjectEditView editPage = (SubjectEditView) pageStack.peek();
        try {
            SubjectModel subject = SubjectModel.get(pluto);
            subject.update(
                    editPage.getNameField(),
                    editPage.getCreditField(),
                    editPage.getReqField(),
                    editPage.getSemesterField(),
                    editPage.getIsOpenedCheck()
            );
            closeChildPage();
        } catch (ValidationException | EntityNotFoundException e) {
            JOptionPane.showMessageDialog(null, "Error updating subject: " + e.getMessage(), "Validation error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /***
     * AbstractController's method with the same name, applied to Subjects as the resource.
     * @see AbstractController
     */
    @Override
    public void delete(String pluto) {
        int input = JOptionPane.showConfirmDialog(null, "Confirm deleting selected subject?");
        if (input == JOptionPane.YES_OPTION) {
            try {
                SubjectModel.delete(pluto);
            } catch (EntityNotFoundException e) {
                JOptionPane.showMessageDialog(null, "Error deleting subject: " + e.getMessage(), "Not found error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /***
     * Path method for showing the courses in the given subject. De facto gateway to CourseController.
     * @param pluto pluto code of the subject which we want to see the courses of
     */
    @Override
    public void show(String pluto) {
        try {
            SubjectModel subject = SubjectModel.get(pluto);

            if (loggedInUser.getType().equals(UserType.STUDENT)) {
                openChildPage(new CourseOfSubjectStudentView(subject.getCourses(), subject, courseController, (StudentModel)loggedInUser));
            }
            else {
                openChildPage(new CourseOfSubjectInstructorView(subject.getCourses(), subject, courseController));
            }
        } catch (EntityNotFoundException e) {
            JOptionPane.showMessageDialog(null, "Error showing courses of subject: " + e.getMessage(), "Not found error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /***
     * Path method for realizing the closing down of all the subjects. Changes all the subjects' isOpened attribute to false.
     */
    public void closeAll() {
        int input = JOptionPane.showConfirmDialog(null, "This action will close all Subject for taking courses in. Continue?");
        if (input == JOptionPane.YES_OPTION) {
            try {
                for (SubjectModel s : Database.getAllSubjects()) {
                    s.setOpened(false, loggedInUser);
                }
            } catch (ValidationException e) {
                JOptionPane.showMessageDialog(null, "Can't close subject: " + e.getMessage(), "No permission", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /***
     * Path methods for students to drop all the courses (and the subject) under this subject they selected.
     * Accessible by students only (secured on view side).
     *
     * @param pluto pluto code of the subject which courses will lose student logged in
     */
    public void drop(String pluto) {
        int input = JOptionPane.showConfirmDialog(null,
                "Are you sure you want to drop every course you've taken under this subject?");
        if (input == JOptionPane.YES_OPTION) {
            try {
                SubjectModel subject = SubjectModel.get(pluto);
                subject.getCourses().forEach(c -> {
                    c.removeStudent((StudentModel) loggedInUser);
                    loggedInUser.removeCourse(c);
                });
                loggedInUser.removeSubject(subject);
            } catch (EntityNotFoundException e) {
                JOptionPane.showMessageDialog(null, "Can't drop subject: " + e.getMessage(), "Not found", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
