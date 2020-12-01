package pluto.controllers;

import pluto.app.PlutoConsole;
import pluto.database.Database;
import pluto.exceptions.AuthorizationException;
import pluto.exceptions.EntityNotFoundException;
import pluto.exceptions.ValidationException;
import pluto.models.InstructorModel;
import pluto.models.StudentModel;
import pluto.models.UserModel;
import pluto.views.*;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.security.NoSuchAlgorithmException;

public class UserController extends AbstractController {
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
            StringSelection stringSelection = new StringSelection(pluto);
            Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            clipboard.setContents(stringSelection, null);
            JOptionPane.showMessageDialog(null, "Successful registration, your Pluto code is copied to the clipboard! " + pluto);
            PlutoConsole.msg("Your Pluto code is ready, copy here: \u001B[32m" + pluto + "\u001B[0m");
            closeChildPage();
        } catch (ValidationException | NoSuchAlgorithmException e) {
            JOptionPane.showMessageDialog(null, "Error creating user: " + e.getMessage(), "Validation error", JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    public void edit(String pluto) {
        UserModel user = null;
        try {
            user = (pluto == null)? loggedInUser : UserModel.get(pluto);
            openChildPage(new UserEditView(user, this, user == loggedInUser));
        } catch (EntityNotFoundException | ValidationException e) {
            JOptionPane.showMessageDialog(null, "Error editing user: " + e.getMessage(), "Not found error", JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    public void update(String pluto) {
        UserEditView editPage = (UserEditView) pageStack.peek();
        UserModel user = (pluto == null)? loggedInUser : Database.getUserWherePlutoCode(pluto);
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
    public void delete(String pluto) {
        int input = JOptionPane.showConfirmDialog(null, "I hope you know what you are doing... Delete user?",
                "DELETING USER", JOptionPane.YES_NO_CANCEL_OPTION);
        if (input == JOptionPane.YES_OPTION) {
            try {
                UserModel.delete(pluto);
            } catch (EntityNotFoundException | ValidationException e) {
                JOptionPane.showMessageDialog(null, "Error deleting user: " + e.getMessage(), "Not found error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    @Override
    public void show(String pluto) { }

    public void auth() {
        LoginView loginPage = (LoginView) pageStack.peek();
        UserModel user = null;
        try {
            user = UserModel.get(loginPage.getPlutoField());
            user.authorize(new String(loginPage.getPwField()));
            loggedInUser = user;
            SubjectController subjCtrl = new SubjectController();
            CourseController courseCtrl = new CourseController();
            subjCtrl.setCourseController(courseCtrl);
            courseCtrl.setSubjectController(subjCtrl);
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
        int input = JOptionPane.showConfirmDialog(null, "This action will add mocked data to the database. Continue?");
        if (input == JOptionPane.YES_OPTION) {
            try {
                Database.seed();
            } catch (ValidationException | NoSuchAlgorithmException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "Error at db seed: " + e.getMessage(), "Seed aborted", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public void dbReset() {
        int input = JOptionPane.showConfirmDialog(null, "Are you sure? This action will delete everything except Administrators");
        if (input == JOptionPane.YES_OPTION) Database.reset();
    }
}
