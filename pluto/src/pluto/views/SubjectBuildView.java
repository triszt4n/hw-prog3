package pluto.views;

import pluto.controllers.SubjectController;

import javax.swing.*;
import java.awt.*;

/**
 * "build" view for subject resource
 * @see pluto.controllers.AbstractController
 */
public class SubjectBuildView extends AbstractView {
    protected final SubjectController subjectController;

    protected JButton backBtn;
    protected JButton saveBtn;
    protected JLabel promptLabel;
    protected JTextField reqField;
    protected JTextField nameField;
    protected JTextField creditField;
    protected JTextField semesterField;
    protected JCheckBox isOpenedCheck;

    public String getReqField() {
        return reqField.getText();
    }
    public String getNameField() {
        return nameField.getText();
    }
    public String getCreditField() {
        return creditField.getText();
    }
    public String getSemesterField() {
        return semesterField.getText();
    }
    public boolean getIsOpenedCheck() {
        return isOpenedCheck.isSelected();
    }

    public SubjectBuildView(SubjectController subjectController) {
        super();
        this.subjectController = subjectController;
        setTitle("Pluto | Create new subject");
        initComponents();
        pack();
        setLocationRelativeTo(null);
        initCloseListener(subjectController);
    }

    @Override
    protected void initComponents() {
        promptLabel = new JLabel("Fill in the form with the details of the new subject!");
        JPanel formPanel = new JPanel();

        JLabel nameLabel = new JLabel("Name of subject");
        nameField = new JTextField(30);
        JLabel reqLabel = new JLabel("Requirements schema");
        reqField = new JTextField(30);
        reqField.setText("0/0/0/[fvs]");

        JLabel creditLabel = new JLabel("Credit");
        creditField = new JTextField(3);
        JLabel semesterLabel = new JLabel("Recommended semester");
        semesterField = new JTextField(2);

        backBtn = new JButton("Back");
        saveBtn = new JButton("Save");

        Component paddingBox1 = Box.createRigidArea(new Dimension(30, 30));
        Component paddingBox2 = Box.createRigidArea(new Dimension(30, 30));
        Component paddingBox3 = Box.createRigidArea(new Dimension(30, 10));

        isOpenedCheck = new JCheckBox("Set subject to be open for students");

        GroupLayout formLayout = new GroupLayout(formPanel);
        formPanel.setLayout(formLayout);
        formLayout.setAutoCreateGaps(true);
        formLayout.setHorizontalGroup(formLayout.createParallelGroup()
                .addComponent(promptLabel, GroupLayout.Alignment.CENTER)
                .addComponent(paddingBox3)
                .addGroup(formLayout.createSequentialGroup()
                        .addGroup(formLayout.createParallelGroup()
                                .addComponent(nameLabel, GroupLayout.Alignment.TRAILING)
                                .addComponent(reqLabel, GroupLayout.Alignment.TRAILING)
                                .addComponent(creditLabel, GroupLayout.Alignment.TRAILING)
                                .addComponent(semesterLabel, GroupLayout.Alignment.TRAILING)
                                .addComponent(paddingBox1, GroupLayout.Alignment.TRAILING)
                                .addComponent(backBtn, GroupLayout.Alignment.LEADING)
                        )
                        .addGroup(formLayout.createParallelGroup()
                                .addComponent(nameField)
                                .addComponent(reqField)
                                .addComponent(creditField)
                                .addComponent(semesterField)
                                .addComponent(isOpenedCheck)
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
                                .addComponent(nameLabel)
                                .addComponent(nameField)
                        )
                        .addGroup(formLayout.createParallelGroup()
                                .addComponent(reqLabel)
                                .addComponent(reqField)
                        )
                        .addGroup(formLayout.createParallelGroup()
                                .addComponent(creditLabel)
                                .addComponent(creditField)
                        )
                        .addGroup(formLayout.createParallelGroup()
                                .addComponent(semesterLabel)
                                .addComponent(semesterField)
                        )
                        .addGroup(formLayout.createParallelGroup()
                                .addComponent(isOpenedCheck)
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
        add(formPanel, BorderLayout.CENTER);
        initListeners();
        getRootPane().setDefaultButton(saveBtn);
    }

    @Override
    protected void initListeners() {
        backBtn.addActionListener(e -> subjectController.back());

        saveBtn.addActionListener(e -> subjectController.create());
    }
}
