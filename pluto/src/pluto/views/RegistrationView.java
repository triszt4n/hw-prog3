package pluto.views;

import pluto.controllers.UserController;
import pluto.views.helpers.PaddedFrame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class RegistrationView extends AbstractView {
    private JButton backBtn;
    private JButton regBtn;
    private JPasswordField pwField;
    private JTextField emailField;
    private JTextField nameField;
    private JTextField dobField;
    private JTextField addressField;

    public char[] getPwField() {
        return pwField.getPassword();
    }

    public String getEmailField() {
        return emailField.getText();
    }

    public String getNameField() {
        return nameField.getText();
    }

    public String getDobField() {
        return dobField.getText();
    }

    public String getAddressField() {
        return addressField.getText();
    }

    public boolean getIsInstructorCheck() {
        return isInstructorCheck.isSelected();
    }

    private JCheckBox isInstructorCheck;

    private UserController userController;

    protected void addComponents() {
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

        isInstructorCheck = new JCheckBox("Request this user to be Instructor.");

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
                                .addComponent(isInstructorCheck)
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
                                .addComponent(isInstructorCheck)
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
        main.getRootPane().setDefaultButton(regBtn);
    }

    private void setUpListeners() {
        backBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
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
        super();
        main.setTitle("Pluto | Register");
        addComponents();
        main.pack();
        main.setLocationRelativeTo(null);

        userController = userCtrl;
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
