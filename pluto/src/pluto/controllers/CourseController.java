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

/***
 * The controller class for controlling the Courses, it accepts interactions from the end user.
 * Implements basic resource management (CRUD) methods.
 */
public class CourseController extends AbstractController {
    /***
     * Currently used subject controller in the application.
     */
    private SubjectController subjectController;

    /***
     * If a course is connected to a subject, it is stored here.
     */
    private SubjectModel currentSubject;

    /***
     * Simple setter. After instantiating a CourseController, one must set the already existing subjectController for
     * CourseController to work.
     *
     * @param subjectController the SubjectController used in the application
     */
    public void setSubjectController(SubjectController subjectController) {
        this.subjectController = subjectController;
    }

    /***
     * Simple setter. Helps joining the courses to the currently chosen subject, useful in cases e.g.: creating a course
     * under a subject (by an instructor).
     *
     * @param subject subject selected in the application
     */
    public void setCurrentSubject(SubjectModel subject) {
        currentSubject = subject;
    }

    /***
     * Makes it possible for the end user to see all of their courses taken/instructed.
     * Administrator gets all of the courses.
     *
     * @see AbstractController
     */
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

    /***
     * AbstractController's method with the same name, applied to the Courses as the resource.
     *
     * @see AbstractController
     */
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

    /***
     * AbstractController's method with the same name, applied to the Courses as the resource.
     *
     * @see AbstractController
     */
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

    /***
     * AbstractController's method with the same name, applied to the Courses as the resource.
     *
     * @see AbstractController
     */
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

    /***
     * AbstractController's method with the same name, applied to the Courses as resource
     *
     * @see AbstractController
     */
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

    /***
     * AbstractController's method with the same name, applied to the Courses as resource
     *
     * @see AbstractController
     */
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

    /***
     * AbstractController's method with the same name, applied to the Courses as resource
     *
     * @see AbstractController
     */
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

    /***
     * Path method for the user to join an existing course as a student.
     * Strictly accessible by students (secured on view side)
     *
     * @param pluto pluto code of the resource to join to
     */
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
                        "Validation", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /***
     * Inverse of join - Path method for the student to drop course already taken before.
     * Strictly accessible by students (secured on view side)
     *
     * @param pluto pluto code of the resource to drop
     */
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

    /***
     * Path method for the instructor/coordinator to kick a student from the course
     * Strictly accessible by instructors and admins (secured on view side)
     *
     * @param pluto pluto code of the course to manage
     * @param student instance of the student to remove from the course
     */
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
