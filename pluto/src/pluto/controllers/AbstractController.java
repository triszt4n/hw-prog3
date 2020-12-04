package pluto.controllers;

import pluto.models.UserModel;
import pluto.views.AbstractView;

import java.util.Stack;

/***
 * Abstract class for controller classes in the MVC desing pattern.
 * Uses a page stack and makes it possible to arrange those pages all around in the application.
 * Usually this is the communaction layer between Models and the end user, this instantiates the Views and
 * makes commands to the Database through the Models.
 */
public abstract class AbstractController {
    /***
     * Stack for pages opened during the running of application.
     */
    protected static Stack<AbstractView> pageStack = new Stack<>();

    /***
     * The running application's (a.k.a session's) stored user, that logged in already.
     */
    protected static UserModel loggedInUser;

    /***
     * Simple method that closes current page shown and opens new one.
     * @param newPage AbstractView page to be opened newly
     */
    protected void changePage(AbstractView newPage) {
        AbstractView oldPage = pageStack.pop();
        oldPage.close();
        newPage.open();
        pageStack.push(newPage);
    }

    /***
     * Simple method that disables current page and opens a new one over the old one(s).
     * @param newPage AbstractView page to be opened newly
     */
    protected void openChildPage(AbstractView newPage) {
        AbstractView oldPage = pageStack.peek();
        oldPage.disable();
        newPage.open();
        pageStack.push(newPage);
    }

    /***
     * Closes the currently opened page. It's important, that the current page is not the only opened one in the stack!
     */
    protected void closeChildPage() {
        AbstractView currentPage = pageStack.pop();
        pageStack.peek().enable();
        currentPage.close();
    }

    /***
     * Uses closeChildPage(), this method is the public method available in the views.
     */
    public void back() {
        closeChildPage();
    }

    /***
     * Abstract method for the controller, generally used to show all the resources of one model
     */
    public abstract void index();

    /***
     * Abstract method for the controller, generally used to show a page for creating a new resource entity
     */
    public abstract void build();

    /***
     * Abstract method for the controller, generally used to realize the creation of a new resource entity
     * Looks out for validation and entity not found errors and communicates it to the end user.
     */
    public abstract void create();

    /***
     * Abstract method for the controller, generally used to show a page for editing a given resource entity
     *
     * @param pluto pluto code of the resource
     */
    public abstract void edit(String pluto);

    /***
     * Abstract method for the controller, generally used to realize the editing of a given resource entity
     * Looks out for validation and entity not found errors and communicates it to the end user.
     *
     * @param pluto pluto code of the resource
     */
    public abstract void update(String pluto);

    /***
     * Abstract method for the controller, generally used to delete the given resource entity
     *
     * @param pluto pluto code of the resource
     */
    public abstract void delete(String pluto);

    /***
     * Abstract method for the controller, generally used to show one resource of one model
     *
     * @param pluto pluto code of the resource
     */
    public abstract void show(String pluto);
}
