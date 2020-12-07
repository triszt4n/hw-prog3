package pluto.views;

import pluto.controllers.CourseController;
import pluto.models.InstructorModel;
import pluto.models.SubjectModel;
import pluto.models.helpers.CourseType;
import pluto.views.helpers.CourseTypeComboBoxModel;
import pluto.views.helpers.InstructorComboBoxModel;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * "build" view for course resource
 * @see pluto.controllers.AbstractController
 */
public class CourseBuildView extends AbstractView {
    protected final CourseController courseController;
    protected final SubjectModel subject;
    protected final List<InstructorModel> instructors;

    public String getMaxStudentsField() {
        return maxStudentsField.getText();
    }

    public String getShortCodeField() {
        return shortCodeField.getText();
    }

    public InstructorModel getInstructorCombo() {
        return instructors.get(instructorCombo.getSelectedIndex());
    }

    public CourseType getTypeCombo() {
        return (CourseType) typeCombo.getSelectedItem();
    }

    public String getNotesArea() {
        return notesArea.getText();
    }

    protected JButton backBtn;
    protected JButton saveBtn;
    protected JLabel promptLabel;
    protected JTextField maxStudentsField;
    protected JTextField shortCodeField;
    protected JComboBox<InstructorModel> instructorCombo;
    protected JComboBox<CourseType> typeCombo;
    protected JTextArea notesArea;

    @Override
    protected void initComponents() {
        promptLabel = new JLabel("New course for Subject: " + subject.getName());
        JPanel formPanel = new JPanel();

        JLabel shortCodeLabel = new JLabel("Short code for course");
        shortCodeField = new JTextField(30);
        JLabel maxStudentsLabel = new JLabel("Maximum of students");
        maxStudentsField = new JTextField(4);

        JLabel typeLabel = new JLabel("Course type");
        typeCombo = new JComboBox<>(new CourseTypeComboBoxModel());
        JLabel instructorLabel = new JLabel("Instructor");
        instructorCombo = new JComboBox<>(new InstructorComboBoxModel(instructors));
        instructorCombo.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                JLabel temp = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                temp.setText(((InstructorModel) value).getName());
                return temp;
            }
        });

        JLabel notesLabel = new JLabel("Notes");
        notesArea = new JTextArea(5, 20);

        backBtn = new JButton("Back");
        saveBtn = new JButton("Save");

        Component paddingBox1 = Box.createRigidArea(new Dimension(30, 30));
        Component paddingBox2 = Box.createRigidArea(new Dimension(30, 30));
        Component paddingBox3 = Box.createRigidArea(new Dimension(30, 10));

        GroupLayout formLayout = new GroupLayout(formPanel);
        formPanel.setLayout(formLayout);
        formLayout.setAutoCreateGaps(true);
        formLayout.setHorizontalGroup(formLayout.createParallelGroup()
                .addComponent(promptLabel, GroupLayout.Alignment.CENTER)
                .addComponent(paddingBox3)
                .addGroup(formLayout.createSequentialGroup()
                        .addGroup(formLayout.createParallelGroup()
                                .addComponent(shortCodeLabel, GroupLayout.Alignment.TRAILING)
                                .addComponent(maxStudentsLabel, GroupLayout.Alignment.TRAILING)
                                .addComponent(typeLabel, GroupLayout.Alignment.TRAILING)
                                .addComponent(instructorLabel, GroupLayout.Alignment.TRAILING)
                                .addComponent(notesLabel, GroupLayout.Alignment.TRAILING)
                                .addComponent(paddingBox1, GroupLayout.Alignment.TRAILING)
                                .addComponent(backBtn, GroupLayout.Alignment.LEADING)
                        )
                        .addGroup(formLayout.createParallelGroup()
                                .addComponent(shortCodeField)
                                .addComponent(maxStudentsField)
                                .addComponent(typeCombo)
                                .addComponent(instructorCombo)
                                .addComponent(notesArea)
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
                                .addComponent(shortCodeLabel)
                                .addComponent(shortCodeField)
                        )
                        .addGroup(formLayout.createParallelGroup()
                                .addComponent(maxStudentsLabel)
                                .addComponent(maxStudentsField)
                        )
                        .addGroup(formLayout.createParallelGroup()
                                .addComponent(typeLabel)
                                .addComponent(typeCombo)
                        )
                        .addGroup(formLayout.createParallelGroup()
                                .addComponent(instructorLabel)
                                .addComponent(instructorCombo)
                        )
                        .addGroup(formLayout.createParallelGroup()
                                .addComponent(notesLabel)
                                .addComponent(notesArea)
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
        backBtn.addActionListener(e -> courseController.back());

        saveBtn.addActionListener(e -> courseController.create());
    }

    public CourseBuildView(CourseController courseController, SubjectModel subject, List<InstructorModel> instructors) {
        super();
        this.instructors = instructors;
        this.subject = subject;
        this.courseController = courseController;
        setTitle("Pluto | Create new course");
        initComponents();
        pack();
        setLocationRelativeTo(null);
        initCloseListener(courseController);
    }
}
