package pluto.views;

import pluto.controllers.CourseController;
import pluto.controllers.SubjectController;
import pluto.models.CourseModel;
import pluto.models.StudentModel;
import pluto.views.helpers.CoursesTableModel;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

/***
 * View for all the student's taken courses
 */
public class CourseIndexStudentView extends AbstractView {
    protected final List<CourseModel> courses;
    protected final CourseController courseController;
    protected final SubjectController subjectController;
    protected final StudentModel student;
    protected CoursesTableModel data;
    protected JTable table;
    protected JButton backBtn;
    protected JButton dropBtn;
    protected JButton showSubjectBtn;
    protected JLabel nameLabel;
    protected JPanel promptPanel;
    protected JPanel actionPanel;

    @Override
    protected void initComponents() {
        promptPanel = new JPanel();
        JLabel promptLabel = new JLabel("Courses you've already taken");
        promptPanel.add(promptLabel);
        promptPanel.setMinimumSize(new Dimension(200, 80));
        add(promptPanel, BorderLayout.NORTH);

        table = new JTable();
        data = new CoursesTableModel(courses, student);
        table.setModel(data);
        table.setRowSelectionAllowed(true);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setFillsViewportHeight(true);

        JScrollPane tablePane = new JScrollPane(table);
        add(tablePane, BorderLayout.CENTER);

        JPanel modifyPanel = new JPanel(new BorderLayout(10, 10));
        modifyPanel.setMinimumSize(new Dimension(600, 80));

        backBtn = new JButton("Back");
        nameLabel = new JLabel("", JLabel.RIGHT);

        actionPanel = new JPanel();
        dropBtn = new JButton("Drop course");
        showSubjectBtn = new JButton("Show subject");
        actionPanel.add(showSubjectBtn);
        actionPanel.add(dropBtn);

        showSubjectBtn.setEnabled(false);
        dropBtn.setEnabled(false);

        modifyPanel.add(backBtn, BorderLayout.WEST);
        modifyPanel.add(nameLabel, BorderLayout.CENTER);
        modifyPanel.add(actionPanel, BorderLayout.EAST);

        modifyPanel.setBorder(
                new EmptyBorder(20,50,20,50)
        );

        add(modifyPanel, BorderLayout.SOUTH);
        initListeners();
    }

    @Override
    protected void initListeners() {
        ListSelectionModel selectionModel = table.getSelectionModel();
        selectionModel.addListSelectionListener(e -> {
            if (table.getSelectedRow() != -1) {
                dropBtn.setEnabled(true);
                showSubjectBtn.setEnabled(true);
                CourseModel course = courses.get(table.getSelectedRow());
                nameLabel.setText(course.getShortCode() + " (" + course.getType().toString() + ")" + " - by " + course.getInstructor().getName());
            }
            else {
                dropBtn.setEnabled(false);
                showSubjectBtn.setEnabled(false);
                nameLabel.setText("");
            }
        });

        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent mouseEvent) {
                if (mouseEvent.getClickCount() == 2 && table.getSelectedRow() != -1) {
                    String pluto = courses.get(table.getSelectedRow()).getSubject().getPlutoCode();
                    subjectController.show(pluto);
                }
            }
        });

        backBtn.addActionListener(e -> courseController.back());

        showSubjectBtn.addActionListener(e -> {
            String pluto = courses.get(table.getSelectedRow()).getSubject().getPlutoCode();
            subjectController.show(pluto);
        });

        dropBtn.addActionListener(e -> {
            String pluto = (String) table.getValueAt(table.getSelectedRow(), CoursesTableModel.CourseColumn.PLUTO.ordinal());
            courseController.drop(pluto);
            data.fireTableDataChanged();
        });
    }

    public CourseIndexStudentView(List<CourseModel> courses, CourseController courseController, SubjectController subjectController, StudentModel student) {
        super();
        this.courses = courses;
        this.courseController = courseController;
        this.subjectController = subjectController;
        this.student = student;
        setTitle("Pluto | Administration: Your courses (Student mode)");
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setMinimumSize(new Dimension(800, 560));
        initComponents();
        setLocationRelativeTo(null);
        initCloseListener(courseController);
    }

    @Override
    public void setEnabled(boolean b) {
        super.setEnabled(b);
        data.fireTableDataChanged();
    }
}
