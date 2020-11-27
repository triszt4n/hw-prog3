package pluto.views;

import pluto.views.helpers.PaddedFrame;

import javax.swing.*;

public abstract class AbstractView {
    protected JFrame main;

    protected abstract void addComponents();

    public AbstractView() {
        main = new PaddedFrame();
        main.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        main.setVisible(false);
    }

    public void open() {
        main.setVisible(true);
    }
    public void disable() {
        main.setEnabled(false);
    }
    public void enable() {
        main.setEnabled(true);
    }
    public void close() {
        main.setVisible(false);
    }
}
