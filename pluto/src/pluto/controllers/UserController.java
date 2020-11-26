package pluto.controllers;

import pluto.models.AbstractModel;
import pluto.models.UserModel;
import pluto.views.IView;
import pluto.views.LoginView;
import pluto.views.RegistrationView;

public class UserController extends AbstractController {
    private AbstractModel loggedInUser;

    private IView loginPage;
    private IView regPage;

    public UserController() {
        loginPage = new LoginView(this);
        regPage = new RegistrationView(this);
    }

    public void login() {
        loginPage.open();
    }

    @Override
    public void loadAll() {

    }

    @Override
    public void form() {
        regPage.open();
    }

    @Override
    public void create() {

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

    }

    public void setUser(UserModel loggedInUser) {
    }
}
