package pluto.views;

import pluto.controllers.UserController;
import pluto.views.helpers.PaddedFrame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class RegistrationView implements IView {
    private JFrame main;
    private JButton backBtn;
    private JButton regBtn;
    private JPasswordField pwField;
    private JTextField emailField;
    private JTextField nameField;
    private JTextField dobField;
    private JTextField addressField;

    private UserController userController;

    private void addComponents() {
        JLabel promptLabel = new JLabel("Please fill in the registration form");
        JPanel formPanel = new JPanel();
        JLabel emailLabel = new JLabel("Email address");
        emailField = new JTextField(30);
        JLabel pwLabel = new JLabel("Password");
        pwField = new JPasswordField(30);
        JLabel nameLabel = new JLabel("Name");
        nameField = new JTextField(30);
        JLabel dobLabel = new JLabel("Date of birth");
        dobField = new JTextField(30);
        JLabel addressLabel = new JLabel("Address");
        addressField = new JTextField(30);
        backBtn = new JButton("Back");
        regBtn = new JButton("Register");
        Component paddingBox1 = Box.createRigidArea(new Dimension(30, 30));
        Component paddingBox2 = Box.createRigidArea(new Dimension(30, 30));
        Component paddingBox3 = Box.createRigidArea(new Dimension(30, 30));

        GroupLayout formLayout = new GroupLayout(formPanel);
        formPanel.setLayout(formLayout);
        formLayout.setAutoCreateGaps(true);
        formLayout.setHorizontalGroup(formLayout.createParallelGroup()
                .addComponent(promptLabel, GroupLayout.Alignment.CENTER)
                .addComponent(paddingBox3)
                .addGroup(formLayout.createSequentialGroup()
                        .addGroup(formLayout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                                .addComponent(emailLabel)
                                .addComponent(pwLabel)
                                .addComponent(nameLabel)
                                .addComponent(dobLabel)
                                .addComponent(addressLabel)
                                .addComponent(paddingBox1)
                                .addComponent(backBtn)
                        )
                        .addGroup(formLayout.createParallelGroup()
                                .addComponent(emailField)
                                .addComponent(pwField)
                                .addComponent(nameField)
                                .addComponent(dobField)
                                .addComponent(addressField)
                                .addComponent(paddingBox2)
                                .addComponent(regBtn, GroupLayout.Alignment.TRAILING)
                        )
                )
        );
        formLayout.setVerticalGroup(formLayout.createSequentialGroup()
                .addComponent(promptLabel)
                .addComponent(paddingBox3)
                .addGroup(formLayout.createSequentialGroup()
                        .addGroup(formLayout.createParallelGroup()
                                .addComponent(emailLabel)
                                .addComponent(emailField)
                        )
                        .addGroup(formLayout.createParallelGroup()
                                .addComponent(pwLabel)
                                .addComponent(pwField)
                        )
                        .addGroup(formLayout.createParallelGroup()
                                .addComponent(nameLabel)
                                .addComponent(nameField)
                        )
                        .addGroup(formLayout.createParallelGroup()
                                .addComponent(dobLabel)
                                .addComponent(dobField)
                        )
                        .addGroup(formLayout.createParallelGroup()
                                .addComponent(addressLabel)
                                .addComponent(addressField)
                        )
                        .addGroup(formLayout.createParallelGroup()
                                .addComponent(paddingBox1)
                                .addComponent(paddingBox2)
                        )
                        .addGroup(formLayout.createParallelGroup()
                                .addComponent(backBtn)
                                .addComponent(regBtn)
                        )
                )
        );
        main.add(formPanel, BorderLayout.CENTER);
        setUpListeners();
    }

    private void setUpListeners() {
        backBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                close();
                userController.login();
            }
        });

        regBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                userController.create();
            }
        });
    }

    public RegistrationView(UserController userCtrl) {
        userController = userCtrl;

        main = new PaddedFrame("Pluto | Register");
        main.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
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
