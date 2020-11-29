package pluto.controllers;

import pluto.app.PlutoConsole;
import pluto.database.Database;
import pluto.models.InstructorModel;
import pluto.models.StudentModel;
import pluto.models.UserModel;
import pluto.exceptions.AuthorizationException;
import pluto.exceptions.EntityNotFoundException;
import pluto.exceptions.ValidationException;
import pluto.views.*;

import javax.swing.*;
import java.security.NoSuchAlgorithmException;
import java.util.Stack;

public class UserController extends AbstractController {
    public UserController(Stack<AbstractView> pageStack) {
        super(pageStack);
    }

    public void login() {
        pageStack.push(new LoginView(this));
        pageStack.peek().open();
    }

    @Override
    public void index() {
        openChildPage(new UserIndexView(UserModel.all(),this));
    }

    @Override
    public void build() {
        openChildPage(new RegistrationView(this));
    }

    @Override
    public void create() {
        RegistrationView regPage = (RegistrationView) pageStack.peek();
        UserModel user;
        try {
            if (regPage.getIsInstructorCheck()) {
                user = new InstructorModel(
                        regPage.getEmailField(),
                        regPage.getNameField(),
                        new String(regPage.getPwField()),
                        regPage.getDobField(),
                        regPage.getAddressField(),
                        false
                );
            }
            else {
                user = new StudentModel(
                        regPage.getEmailField(),
                        regPage.getNameField(),
                        new String(regPage.getPwField()),
                        regPage.getDobField(),
                        regPage.getAddressField()
                );
            }
            String pluto = user.getPlutoCode();
            JOptionPane.showMessageDialog(null, "Successful registration, your Pluto code: " + pluto);
            PlutoConsole.msg("Your Pluto code is ready, copy here: \u001B[32m" + pluto + "\u001B[0m");
            closeChildPage();
        } catch (ValidationException | NoSuchAlgorithmException e) {
            JOptionPane.showMessageDialog(null, "Error creating user: " + e.getMessage(), "Validation error", JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    public void edit(int index) {
        UserModel user = (index == -1)? loggedInUser : Database.getUserWhereIndex(index);
        openChildPage(new UserEditView(user, this, index == -1));
    }

    @Override
    public void update(int index) {
        UserEditView editPage = (UserEditView) pageStack.peek();
        UserModel user = (index == -1)? loggedInUser : Database.getUserWhereIndex(index);
        try {
            user.update(
                    editPage.getEmailField(),
                    editPage.getNameField(),
                    new String(editPage.getPwField()),
                    editPage.getDobField(),
                    editPage.getAddressField()
            );
            closeChildPage();
        } catch (ValidationException | NoSuchAlgorithmException e) {
            JOptionPane.showMessageDialog(null, "Error updating user: " + e.getMessage(), "Validation error", JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    public void delete(int index) {
        int input = JOptionPane.showConfirmDialog(null, "Confirm deleting selected user?");
        if (input == JOptionPane.OK_OPTION) {
            try {
                UserModel.delete(index);
            } catch (EntityNotFoundException e) {
                JOptionPane.showMessageDialog(null, "Error deleting user: " + e.getMessage(), "Not found error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    @Override
    public void show(int index) {

    }

    public void auth() {
        LoginView loginPage = (LoginView) pageStack.peek();
        UserModel user = null;
        try {
            user = UserModel.get(loginPage.getPlutoField());
            user.authorize(new String(loginPage.getPwField()));
            loggedInUser = user;
            SubjectController subjCtrl = new SubjectController(pageStack);
            CourseController courseCtrl = new CourseController(pageStack);
            subjCtrl.setCourseController(courseCtrl);
            changePage(new DashboardView(loggedInUser, this, subjCtrl, courseCtrl));
        } catch (EntityNotFoundException | AuthorizationException | ValidationException | NoSuchAlgorithmException e) {
            JOptionPane.showMessageDialog(null, "Error at log in: " + e.getMessage(), "Wrong credentials", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void logout() {
        loggedInUser = null;
        changePage(new LoginView(this));
    }

    public void seed() {
        try {
            Database.seed();
        } catch (ValidationException | NoSuchAlgorithmException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error at db seed: " + e.getMessage(), "Seed aborted", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void dbReset() {
        int input = JOptionPane.showConfirmDialog(null, "Are you sure? This action will delete everything except Administrators");
        if (input == JOptionPane.OK_OPTION) Database.reset();
    }
}
