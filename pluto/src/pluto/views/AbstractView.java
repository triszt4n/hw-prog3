package pluto.views;

import pluto.views.helpers.PaddedFrame;

import javax.swing.*;

/***
 * Abstract class of views in the MVC design pattern.
 * They show the interface for the end user, and have the interaction buttons/field for the end user to give commands to the
 * controller.
 */
public abstract class AbstractView {
    protected JFrame main;

    /***
     * Method inits the components used in the view
     */
    protected abstract void initComponents();

    /**
     * Method inits the listeners in the view
     */
    protected abstract void initListeners();

    public AbstractView() {
        main = new PaddedFrame();
        main.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        main.setVisible(false);
    }

    /***
     * opens up page
     */
    public void open() {
        main.setVisible(true);
    }

    /***
     * closes the page
     */
    public void close() {
        main.setVisible(false);
    }

    /***
     * makes the page disabled
     */
    public void disable() {
        main.setEnabled(false);
    }

    /***
     * makes the page enabled
     */
    public void enable() {
        main.setEnabled(true);
    }

    /***
     * Helper method for debugging
     * @return name of the class
     */
    @Override
    public String toString() {
        return this.getClass().toString();
    }
}
