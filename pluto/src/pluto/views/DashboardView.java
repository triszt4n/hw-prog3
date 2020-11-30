package pluto.views;

import pluto.controllers.CourseController;
import pluto.controllers.SubjectController;
import pluto.controllers.UserController;
import pluto.models.UserModel;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class DashboardView extends AbstractView {
    private final UserController userController;
    private final CourseController courseController;
    private final SubjectController subjectController;
    private final UserModel user;

    private JMenuItem manageUserBtn;
    private JMenuItem logoutBtn;

    private JMenuItem manageSubjectBtn;

    private JMenuItem manageCourseBtn;
    private JMenuItem takeBtn;

    private JMenuItem usersBtn;
    private JMenuItem seedBtn;
    private JMenuItem resetBtn;
    private JMenuItem newSubjectBtn;

    private JPanel createDetailsPanel() {
        JLabel emailLabel = new JLabel("Email address");
        JLabel plutoLabel = new JLabel("Pluto code");
        JLabel nameLabel = new JLabel("Name");
        JLabel dobLabel = new JLabel("Date of birth");
        JLabel addressLabel = new JLabel("Address");

        JLabel emailLabel2 = new JLabel(user.getEmail());
        JLabel plutoLabel2 = new JLabel(user.getPlutoCode());
        JLabel nameLabel2 = new JLabel(user.getName());
        JLabel dobLabel2 = new JLabel(user.getParsedDob().toString());
        JLabel addressLabel2 = new JLabel(user.getAddress());

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

    @Override
    protected void initComponents() {
        JMenuBar mb = new JMenuBar();

        JMenu userMenu = new JMenu("User");
        manageUserBtn = new JMenuItem("Manage...");
        logoutBtn = new JMenuItem("Log out");
        userMenu.add(manageUserBtn);
        userMenu.add(logoutBtn);
        mb.add(userMenu);

        JMenu subjectMenu = new JMenu("Subjects");
        manageSubjectBtn = new JMenuItem("Manage my subjects...");
        newSubjectBtn = new JMenuItem("New subject");
        subjectMenu.add(manageSubjectBtn);
        subjectMenu.add(newSubjectBtn);
        mb.add(subjectMenu);

        JMenu courseMenu = new JMenu("Courses");
        takeBtn = new JMenuItem("Take courses...");
        manageCourseBtn = new JMenuItem("Manage my courses...");
        courseMenu.add(takeBtn);
        courseMenu.add(manageCourseBtn);
        mb.add(courseMenu);

        JMenu dbMenu = new JMenu("Database");
        usersBtn = new JMenuItem("Users");
        seedBtn = new JMenuItem("Set up seed");
        resetBtn = new JMenuItem("Delete database");
        dbMenu.add(usersBtn);
        dbMenu.add(seedBtn);
        dbMenu.add(resetBtn);
        if (user.getTitle() != "Administrator") {
            dbMenu.setVisible(false);
        }
        mb.add(dbMenu);

        main.setJMenuBar(mb);

        JLabel welcomeLabel = new JLabel("Hi, " + user.getName() + "!");
        welcomeLabel.setFont(new Font(Font.SANS_SERIF,  Font.BOLD, 30));
        JLabel promptLabel = new JLabel("Browse the menu bar for actions");
        JLabel userTitleLabel = new JLabel("/ " + user.getTitle() + " /");
        JPanel detailsPanel = createDetailsPanel();

        Component paddingBox1 = Box.createRigidArea(new Dimension(50, 50));
        Component paddingBox2 = Box.createRigidArea(new Dimension(30, 30));

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
        initListeners();
    }

    @Override
    protected void initListeners() {
        logoutBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                userController.logout();
            }
        });

        usersBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                userController.index();
            }
        });

        manageUserBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                userController.edit(null);
            }
        });

        manageSubjectBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                subjectController.allForLoggedInUser();
            }
        });

        newSubjectBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                subjectController.build();
            }
        });

        takeBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                subjectController.index();
            }
        });

        manageCourseBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                courseController.index();
            }
        });

        seedBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                userController.seed();
            }
        });

        resetBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                userController.dbReset();
            }
        });
    }

    public DashboardView(UserModel user, UserController userCtrl, SubjectController subjectCtrl, CourseController courseCtrl) {
        super();
        this.user = user;
        userController = userCtrl;
        subjectController = subjectCtrl;
        courseController = courseCtrl;

        main.setTitle("Pluto | Dashboard");
        initComponents();
        main.pack();
        main.setLocationRelativeTo(null);
    }
}
