package pluto.views;

import pluto.controllers.UserController;
import pluto.views.helpers.PaddedFrame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginView implements IView {
    private JFrame main;
    private JButton loginBtn;
    private JButton regBtn;
    private JPasswordField pwField;
    private JTextField plutoField;

    private UserController userController;

    private void addComponents() {
        JLabel welcomeLabel = new JLabel("Welcome to Pluto Course Manager");
        welcomeLabel.setFont(new Font(Font.SANS_SERIF,  Font.BOLD, 24));

        JLabel promptLabel = new JLabel("Please log in to your account or register");

        JLabel plutoLabel = new JLabel("Pluto code");
        plutoField = new JTextField(30);

        JLabel pwLabel = new JLabel("Password");
        pwField = new JPasswordField(30);

        loginBtn = new JButton("Log in");
        regBtn = new JButton("Register");
        Component paddingBox1 = Box.createRigidArea(new Dimension(30, 30));
        Component paddingBox2 = Box.createRigidArea(new Dimension(30, 30));
        Component paddingBox3 = Box.createRigidArea(new Dimension(30, 30));

        JPanel formPanel = new JPanel();
        GroupLayout formLayout = new GroupLayout(formPanel);
        formPanel.setLayout(formLayout);
        formLayout.setAutoCreateGaps(true);
        formLayout.setHorizontalGroup(formLayout.createParallelGroup()
                .addGroup(formLayout.createParallelGroup(GroupLayout.Alignment.CENTER)
                        .addComponent(welcomeLabel)
                        .addComponent(promptLabel)
                        .addComponent(paddingBox3)
                )
                .addGroup(formLayout.createSequentialGroup()
                        .addGroup(formLayout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                                .addComponent(plutoLabel)
                                .addComponent(pwLabel)
                                .addComponent(paddingBox1)
                                .addComponent(loginBtn)
                        )
                        .addGroup(formLayout.createParallelGroup()
                                .addComponent(plutoField)
                                .addComponent(pwField)
                                .addComponent(paddingBox2)
                                .addComponent(regBtn, GroupLayout.Alignment.TRAILING)
                        )
                )
        );
        formLayout.setVerticalGroup(formLayout.createSequentialGroup()
                .addGroup(formLayout.createSequentialGroup()
                        .addComponent(welcomeLabel)
                        .addComponent(promptLabel)
                        .addComponent(paddingBox3)
                )
                .addGroup(formLayout.createSequentialGroup()
                        .addGroup(formLayout.createParallelGroup()
                                .addComponent(plutoLabel)
                                .addComponent(plutoField)
                        )
                        .addGroup(formLayout.createParallelGroup()
                                .addComponent(pwLabel)
                                .addComponent(pwField)
                        )
                        .addGroup(formLayout.createParallelGroup()
                                .addComponent(paddingBox1)
                                .addComponent(paddingBox2)
                        )
                        .addGroup(formLayout.createParallelGroup()
                                .addComponent(loginBtn)
                                .addComponent(regBtn)
                        )
                )
        );
        setUpListeners();
        main.add(formPanel, BorderLayout.CENTER);
    }

    private void setUpListeners() {
        loginBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });

        regBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                close();
                userController.form();
            }
        });
    }

    public LoginView(UserController userCtrl) {
        userController = userCtrl;

        main = new PaddedFrame("Pluto | Log in");
        main.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        addComponents();
        main.pack();
        main.setLocationRelativeTo(null);
        main.setVisible(false);
    }

    @Override
    public void open() {
        main.setVisible(true);
    }

    @Override
    public void close() {
        main.setVisible(false);
    }

}
