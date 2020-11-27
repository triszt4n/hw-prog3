package pluto.controllers;

import pluto.app.PlutoConsole;
import pluto.models.*;
import pluto.models.exceptions.EntityNotFoundException;
import pluto.models.exceptions.ValidationException;
import pluto.views.DashboardView;
import pluto.views.LoginView;
import pluto.views.RegistrationView;

import javax.swing.*;

public class UserController extends AbstractController {
    private UserModel loggedInUser;

    private LoginView loginPage;
    private RegistrationView regPage;
    private DashboardView dashboard;

    public UserController() {
        loginPage = new LoginView(this);
        regPage = new RegistrationView(this);
    }

    public void login() {
        regPage.close();
        loginPage.open();
    }

    @Override
    public void loadAll() {

    }

    @Override
    public void form() {
        loginPage.close();
        regPage.open();
    }

    @Override
    public void create() {
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
            regPage.close();
            loginPage.open();
        } catch (ValidationException e) {
            JOptionPane.showMessageDialog(null, "Error creating user: " + e.getMessage(), "Validation error", JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    public void edit() {

    }

    @Override
    public void update() {

    }

    @Override
    public void delete() {

    }

    @Override
    public void show() {
        UserModel user = null;
        try {
            user = UserModel.get(loginPage.getPlutoField());
            user.authorize(new String(loginPage.getPwField()));
            loggedInUser = user;
            dashboard = new DashboardView(loggedInUser, this, new SubjectController(), new CourseController());
            loginPage.close();
            dashboard.open();
        } catch (EntityNotFoundException | UserModel.AuthorizationException e) {
            JOptionPane.showMessageDialog(null, "Error at log in: " + e.getMessage(), "Wrong credentials", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void logout() {
        loggedInUser = null;
        dashboard.close();
        loginPage.open();
    }
}
