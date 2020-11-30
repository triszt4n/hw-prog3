package pluto.controllers;

import pluto.exceptions.EntityNotFoundException;
import pluto.exceptions.ValidationException;
import pluto.models.SubjectModel;
import pluto.views.SubjectBuildView;
import pluto.views.SubjectIndexInstructorView;
import pluto.views.SubjectIndexStudentView;
import pluto.views.SubjectIndexView;

import javax.swing.*;

public class SubjectController extends AbstractController {
    private CourseController courseController;

    public void setCourseController(CourseController courseController) {
        this.courseController = courseController;
    }

    @Override
    public void index() {
        openChildPage(new SubjectIndexView(SubjectModel.all(), this));
    }

    public void allForLoggedInUser() {
        if (loggedInUser.getTitle() == "Instructor") {
            openChildPage(new SubjectIndexInstructorView(loggedInUser.getMySubjects(), loggedInUser, this));
        }
        else if (loggedInUser.getTitle() == "Student") {
            openChildPage(new SubjectIndexStudentView(loggedInUser.getMySubjects(), loggedInUser, this));
        }
        else {
            openChildPage(new SubjectIndexInstructorView(SubjectModel.all(), loggedInUser, this));
        }
    }

    @Override
    public void build() {
        if (loggedInUser.getTitle() == "Student") {
            JOptionPane.showMessageDialog(null, "Can't create subject as a student!", "No permission", JOptionPane.INFORMATION_MESSAGE);
        }
        else {
            openChildPage(new SubjectBuildView(this));
        }
    }

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

    @Override
    public void edit(String pluto) {
        try {
            SubjectModel subject = SubjectModel.get(pluto);
        } catch (EntityNotFoundException e) {
            JOptionPane.showMessageDialog(null, "Error editing subject: " + e.getMessage(), "Validation error", JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    public void update(String pluto) {

    }

    @Override
    public void delete(String pluto) {

    }

    @Override
    public void show(String pluto) {

    }
}
