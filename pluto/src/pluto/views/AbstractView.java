package pluto.views;

import pluto.controllers.AbstractController;
import pluto.views.helpers.PaddedFrame;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/***
 * Abstract class of views in the MVC design pattern.
 * They show the interface for the end user, and have the interaction buttons/field for the end user to give commands to the
 * controller.
 */
public abstract class AbstractView extends PaddedFrame {
    /***
     * Method inits the components used in the view
     */
    protected abstract void initComponents();

    /**
     * Method inits the listeners in the view
     */
    protected abstract void initListeners();

    /**
     * Method inits the window listener for closing the window
     *
     * @param mainController the controller which's back method is used for moving back one page
     */
    protected void initCloseListener(AbstractController mainController) {
        addWindowListener(new WindowAdapter(){
            @Override
            public void windowClosing(WindowEvent e){
                mainController.back();
            }
        });
    }

    public AbstractView() {
        super();
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setVisible(false);
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
