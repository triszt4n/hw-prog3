package pluto.views;

import pluto.controllers.CourseController;
import pluto.controllers.SubjectController;
import pluto.controllers.UserController;
import pluto.models.UserModel;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class DashboardView extends AbstractView {
    private UserController userController;
    private CourseController courseController;
    private SubjectController subjectController;
    private UserModel loggedInUser;

    private JMenu userMenu;
    private JMenuItem manageUserBtn;
    private JMenuItem logoutBtn;

    private JMenu subjectMenu;
    private JMenuItem manageSubjectBtn;
    private JMenuItem applyBtn;

    private JMenu courseMenu;
    private JMenuItem manageCourseBtn;

    private JMenu dbMenu;
    private JMenuItem reqBtn;
    private JMenuItem usersBtn;
    private JMenuItem seedBtn;

    private JPanel createDetailsPanel() {
        JLabel emailLabel = new JLabel("Email address");
        JLabel plutoLabel = new JLabel("Pluto code");
        JLabel nameLabel = new JLabel("Name");
        JLabel dobLabel = new JLabel("Date of birth");
        JLabel addressLabel = new JLabel("Address");

        JLabel emailLabel2 = new JLabel(loggedInUser.getEmail());
        JLabel plutoLabel2 = new JLabel(loggedInUser.getPlutoCode());
        JLabel nameLabel2 = new JLabel(loggedInUser.getName());
        JLabel dobLabel2 = new JLabel(loggedInUser.getDob().toString());
        JLabel addressLabel2 = new JLabel(loggedInUser.getAddress());

        JPanel formPanel = new JPanel();
        GroupLayout formLayout = new GroupLayout(formPanel);
        formPanel.setLayout(formLayout);
        formLayout.setAutoCreateGaps(true);
        formLayout.setHorizontalGroup(formLayout.createSequentialGroup()
                .addGroup(formLayout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                        .addComponent(emailLabel)
                        .addComponent(plutoLabel)
                        .addComponent(nameLabel)
                        .addComponent(dobLabel)
                        .addComponent(addressLabel)
                )
                .addGroup(formLayout.createParallelGroup()
                        .addComponent(emailLabel2)
                        .addComponent(plutoLabel2)
                        .addComponent(nameLabel2)
                        .addComponent(dobLabel2)
                        .addComponent(addressLabel2)
                )
        );
        formLayout.setVerticalGroup(formLayout.createSequentialGroup()
                .addGroup(formLayout.createParallelGroup()
                        .addComponent(emailLabel)
                        .addComponent(emailLabel2)
                )
                .addGroup(formLayout.createParallelGroup()
                        .addComponent(plutoLabel)
                        .addComponent(plutoLabel2)
                )
                .addGroup(formLayout.createParallelGroup()
                        .addComponent(nameLabel)
                        .addComponent(nameLabel2)
                )
                .addGroup(formLayout.createParallelGroup()
                        .addComponent(dobLabel)
                        .addComponent(dobLabel2)
                )
                .addGroup(formLayout.createParallelGroup()
                        .addComponent(addressLabel)
                        .addComponent(addressLabel2)
                )
        );

        formPanel.setBorder(
                new CompoundBorder(
                        BorderFactory.createTitledBorder("User details"),
                        new EmptyBorder(20,20,20,20)
                )
        );
        return formPanel;
    }

    protected void addComponents() {
        JMenuBar mb = new JMenuBar();

        userMenu = new JMenu("User");
        manageUserBtn = new JMenuItem("Manage...");
        logoutBtn = new JMenuItem("Log out");
        userMenu.add(manageUserBtn);
        userMenu.add(logoutBtn);
        mb.add(userMenu);

        subjectMenu = new JMenu("Subjects");
        manageSubjectBtn = new JMenuItem("Manage...");
        applyBtn = new JMenuItem("Apply for...");
        subjectMenu.add(manageSubjectBtn);
        subjectMenu.add(applyBtn);
        mb.add(subjectMenu);

        courseMenu = new JMenu("Courses");
        manageCourseBtn = new JMenuItem("Manage...");
        courseMenu.add(manageCourseBtn);
        mb.add(courseMenu);

        dbMenu = new JMenu("Database");
        reqBtn = new JMenuItem("Instructor requests");
        usersBtn = new JMenuItem("Users");
        seedBtn = new JMenuItem("Set up seed");
        dbMenu.add(reqBtn);
        dbMenu.add(usersBtn);
        dbMenu.add(seedBtn);
        mb.add(dbMenu);

        main.setJMenuBar(mb);

        JLabel welcomeLabel = new JLabel("Hi, " + loggedInUser.getName());
        welcomeLabel.setFont(new Font(Font.SANS_SERIF,  Font.BOLD, 24));
        JLabel promptLabel = new JLabel("Browse the menu bar for actions");
        JLabel userTitleLabel = new JLabel("/" + loggedInUser.getTitle() + "/");
        JPanel detailsPanel = createDetailsPanel();

        Component paddingBox1 = Box.createRigidArea(new Dimension(50, 50));
        Component paddingBox2 = Box.createRigidArea(new Dimension(50, 50));

        JPanel mainPanel = new JPanel();
        GroupLayout layout = new GroupLayout(mainPanel);
        mainPanel.setLayout(layout);
        layout.setAutoCreateGaps(true);
        layout.setHorizontalGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup()
                        .addComponent(welcomeLabel)
                        .addComponent(paddingBox2)
                        .addComponent(promptLabel)
                )
                .addComponent(paddingBox1)
                .addGroup(layout.createParallelGroup()
                        .addComponent(userTitleLabel, GroupLayout.Alignment.TRAILING)
                        .addComponent(detailsPanel)
                )
        );
        layout.setVerticalGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup()
                        .addComponent(welcomeLabel)
                        .addComponent(userTitleLabel)
                )
                .addComponent(paddingBox2)
                .addGroup(layout.createParallelGroup()
                        .addComponent(promptLabel)
                        .addComponent(paddingBox1)
                        .addComponent(detailsPanel)
                )
        );
        main.add(mainPanel, BorderLayout.CENTER);
        setUpListeners();
    }

    private void setUpListeners() {
        logoutBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                userController.logout();
            }
        });
    }

    public DashboardView(UserModel user, UserController userCtrl, SubjectController subjectCtrl, CourseController courseCtrl) {
        super();
        loggedInUser = user;
        userController = userCtrl;
        subjectController = subjectCtrl;
        courseController = courseCtrl;

        main.setTitle("Pluto | Dashboard");
        addComponents();
        main.pack();
        main.setLocationRelativeTo(null);
    }
}
