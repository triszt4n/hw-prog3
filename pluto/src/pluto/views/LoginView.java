package pluto.views;

import pluto.controllers.UserController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * "login" view for user resource
 * @see pluto.controllers.UserController
 */
public class LoginView extends AbstractView {
    private JButton loginBtn;
    private JButton regBtn;
    private JPasswordField pwField;
    private JTextField plutoField;

    public char[] getPwField() {
        return pwField.getPassword();
    }

    public String getPlutoField() {
        return plutoField.getText();
    }

    private final UserController userController;

    @Override
    protected void initComponents() {
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
        initListeners();
        main.add(formPanel, BorderLayout.CENTER);

        main.getRootPane().setDefaultButton(loginBtn);
    }

    @Override
    protected void initListeners() {
        loginBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                userController.auth();
            }
        });

        regBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                userController.build();
            }
        });
    }

    public LoginView(UserController userCtrl) {
        super();
        main.setTitle("Pluto | Log in");
        main.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        initComponents();
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
