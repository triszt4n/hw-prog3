package pluto.views;

import pluto.controllers.UserController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class RegistrationView extends AbstractView {
    protected JButton backBtn;
    protected JButton saveBtn;
    protected JLabel promptLabel;
    protected JPasswordField pwField;
    protected JTextField emailField;
    protected JTextField nameField;
    protected JTextField dobField;
    protected JTextField addressField;
    protected JCheckBox isInstructorCheck;
    protected UserController userController;
    protected JLabel pwLabel;

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

    @Override
    protected void initComponents() {
        promptLabel = new JLabel("Please fill in the registration form");
        JPanel formPanel = new JPanel();

        JLabel emailLabel = new JLabel("Email address");
        emailField = new JTextField(30);
        pwLabel = new JLabel("Password");
        pwField = new JPasswordField(30);
        JLabel nameLabel = new JLabel("Name");
        nameField = new JTextField(30);
        JLabel dobLabel = new JLabel("Date of birth");
        dobField = new JTextField(30);
        JLabel dobFormatLabel = new JLabel("Format: yyyy-mm-dd, e.g.: 1989-09-10");
        dobFormatLabel.setFont(new Font(Font.SANS_SERIF, Font.ITALIC, 9));
        JLabel addressLabel = new JLabel("Address");
        addressField = new JTextField(30);

        backBtn = new JButton("Back");
        saveBtn = new JButton("Register");

        Component paddingBox1 = Box.createRigidArea(new Dimension(30, 30));
        Component paddingBox2 = Box.createRigidArea(new Dimension(30, 30));
        Component paddingBox3 = Box.createRigidArea(new Dimension(30, 10));

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
                                .addComponent(dobFormatLabel)
                                .addComponent(addressField)
                                .addComponent(isInstructorCheck)
                                .addComponent(paddingBox2)
                                .addComponent(saveBtn, GroupLayout.Alignment.TRAILING)
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
                        .addComponent(dobFormatLabel)
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
                                .addComponent(saveBtn)
                        )
                )
        );
        main.add(formPanel, BorderLayout.CENTER);
        initListeners();
        main.getRootPane().setDefaultButton(saveBtn);
    }

    @Override
    protected void initListeners() {
        backBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                userController.back();
            }
        });

        saveBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                userController.create();
            }
        });
    }

    public RegistrationView(UserController userCtrl) {
        super();
        userController = userCtrl;

        main.setTitle("Pluto | Register");
        initComponents();
        main.pack();
        main.setLocationRelativeTo(null);
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
