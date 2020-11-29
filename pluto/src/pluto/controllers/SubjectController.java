package pluto.controllers;

import pluto.models.SubjectModel;
import pluto.views.AbstractView;
import pluto.views.SubjectIndexView;

import java.util.Stack;

public class SubjectController extends AbstractController {
    private CourseController courseController;

    public SubjectController(Stack<AbstractView> pageStack) {
        super(pageStack);
    }

    public void setCourseController(CourseController courseController) {
        this.courseController = courseController;
    }

    @Override
    public void index() {
        openChildPage(new SubjectIndexView(SubjectModel.all(), loggedInUser, this));
    }

    public void allForLoggedInUser() {
        openChildPage(new SubjectIndexView(loggedInUser.getMySubjects(), loggedInUser, this));
    }

    @Override
    public void build() {

    }

    @Override
    public void create() {

    }

    @Override
    public void edit(int index) {

    }

    @Override
    public void update(int index) {

    }

    @Override
    public void delete(int index) {

    }

    @Override
    public void show(int index) {

    }
}
