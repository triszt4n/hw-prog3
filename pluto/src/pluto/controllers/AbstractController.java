package pluto.controllers;

import pluto.models.UserModel;
import pluto.views.AbstractView;

import java.util.Stack;

public abstract class AbstractController {
    protected static Stack<AbstractView> pageStack = new Stack<>();

    /***
     * The session's stored user, that logged in.
     */
    protected static UserModel loggedInUser;

    protected void changePage(AbstractView newPage) {
        AbstractView oldPage = pageStack.pop();
        oldPage.close();
        newPage.open();
        pageStack.push(newPage);
    }

    protected void openChildPage(AbstractView newPage) {
        AbstractView oldPage = pageStack.peek();
        oldPage.disable();
        newPage.open();
        pageStack.push(newPage);
    }

    protected void closeChildPage() {
        AbstractView currentPage = pageStack.pop();
        pageStack.peek().enable();
        currentPage.close();
    }

    public void back() {
        closeChildPage();
    }
    public abstract void index();
    public abstract void build();
    public abstract void create();
    public abstract void edit(String pluto);
    public abstract void update(String pluto);
    public abstract void delete(String pluto);
    public abstract void show(String pluto);
}
