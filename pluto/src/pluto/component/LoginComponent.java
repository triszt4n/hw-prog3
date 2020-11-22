package pluto.component;

import javax.swing.*;
import java.awt.*;

public class LoginComponent implements IComponent {
    private JFrame main;

    public LoginComponent() {
        main = new JFrame("Pluto | Log in");
        main.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel mainPanel = new JPanel();
        JLabel label = new JLabel("Please log in to your account");
        mainPanel.add(label);
        main.add(mainPanel);

        main.setSize(500, 300);
        main.setVisible(false);
    }

    @Override
    public void open() {
        main.setVisible(true);
    }

    @Override
    public void close() {

    }

    @Override
    public void refresh() {

    }
}
