package pluto.views;

import pluto.controllers.CourseController;
import pluto.controllers.SubjectController;
import pluto.models.CourseModel;
import pluto.views.helpers.CoursesTableModel;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class CourseIndexInstructorView extends AbstractView {
    protected final List<CourseModel> courses;
    protected final CourseController courseController;
    protected final SubjectController subjectController;
    protected CoursesTableModel data;
    protected JTable table;
    protected JButton backBtn;
    protected JButton deleteBtn;
    protected JButton editBtn;
    protected JLabel nameLabel;
    protected JButton showStudentsBtn;
    protected JPanel promptPanel;
    protected JButton showSubjectBtn;

    @Override
    protected void initComponents() {
        promptPanel = new JPanel();
        JLabel promptLabel = new JLabel("All your courses you are instructor of");
        promptPanel.add(promptLabel);
        promptPanel.setMinimumSize(new Dimension(200, 80));
        main.add(promptPanel, BorderLayout.NORTH);

        table = new JTable();
        data = new CoursesTableModel(courses, null);
        table.setModel(data);
        table.setRowSelectionAllowed(true);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setFillsViewportHeight(true);

        JScrollPane tablePane = new JScrollPane(table);
        main.add(tablePane, BorderLayout.CENTER);

        JPanel modifyPanel = new JPanel(new BorderLayout(10, 10));
        modifyPanel.setMinimumSize(new Dimension(600, 80));

        backBtn = new JButton("Back");
        nameLabel = new JLabel(" ", JLabel.RIGHT);
        nameLabel.setBorder(new EmptyBorder(0,0,5,5));

        JPanel actionPanel = new JPanel();
        deleteBtn = new JButton("Delete");
        editBtn = new JButton("Edit");
        showStudentsBtn = new JButton("Show students");
        showSubjectBtn = new JButton("Show subject");
        actionPanel.add(editBtn);
        actionPanel.add(showSubjectBtn);
        actionPanel.add(showStudentsBtn);
        actionPanel.add(deleteBtn);

        editBtn.setEnabled(false);
        showStudentsBtn.setEnabled(false);
        deleteBtn.setEnabled(false);
        showSubjectBtn.setEnabled(false);

        JPanel infoPanel = new JPanel(new BorderLayout());
        infoPanel.add(nameLabel, BorderLayout.NORTH);
        infoPanel.add(actionPanel, BorderLayout.SOUTH);

        modifyPanel.add(backBtn, BorderLayout.WEST);
        modifyPanel.add(infoPanel, BorderLayout.EAST);

        modifyPanel.setBorder(new EmptyBorder(20,50,20,50));

        main.add(modifyPanel, BorderLayout.SOUTH);
        initListeners();
    }

    @Override
    protected void initListeners() {
        ListSelectionModel selectionModel = table.getSelectionModel();
        selectionModel.addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                if (table.getSelectedRow() != -1) {
                    deleteBtn.setEnabled(true);
                    showStudentsBtn.setEnabled(true);
                    showSubjectBtn.setEnabled(true);
                    editBtn.setEnabled(true);
                    CourseModel course = courses.get(table.getSelectedRow());
                    nameLabel.setText(course.getShortCode() + " (" + course.getType().toString() + ")" + " - by " + course.getInstructor().getName());
                }
                else {
                    deleteBtn.setEnabled(false);
                    showStudentsBtn.setEnabled(false);
                    editBtn.setEnabled(false);
                    showSubjectBtn.setEnabled(false);
                    nameLabel.setText(" ");
                }
            }
        });

        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent mouseEvent) {
                if (mouseEvent.getClickCount() == 2 && table.getSelectedRow() != -1) {
                    String pluto = (String) table.getValueAt(table.getSelectedRow(), CoursesTableModel.CourseColumn.PLUTO.ordinal());
                    courseController.show(pluto);
                }
            }
        });

        backBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                courseController.back();
            }
        });

        editBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String pluto = (String) table.getValueAt(table.getSelectedRow(), CoursesTableModel.CourseColumn.PLUTO.ordinal());
                courseController.edit(pluto);
            }
        });

        showStudentsBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String pluto = (String) table.getValueAt(table.getSelectedRow(), CoursesTableModel.CourseColumn.PLUTO.ordinal());
                courseController.show(pluto);
            }
        });

        showSubjectBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String pluto = courses.get(table.getSelectedRow()).getSubject().getPlutoCode();
                subjectController.show(pluto);
            }
        });

        deleteBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String pluto = (String) table.getValueAt(table.getSelectedRow(), CoursesTableModel.CourseColumn.PLUTO.ordinal());
                courseController.delete(pluto);
                data.fireTableDataChanged();
            }
        });
    }

    public CourseIndexInstructorView(List<CourseModel> courses, CourseController courseController, SubjectController subjectController) {
        super();
        this.courses = courses;
        this.courseController = courseController;
        this.subjectController = subjectController;
        main.setTitle("Pluto | Administration: Your courses (Instructor mode)");
        main.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        main.setMinimumSize(new Dimension(800, 560));
        initComponents();
        main.setLocationRelativeTo(null);

        main.addWindowListener(new WindowAdapter(){
            @Override
            public void windowClosing(WindowEvent e){
                courseController.back();
            }
        });
    }

    @Override
    public void enable() {
        super.enable();
        data.fireTableDataChanged();
    }
}
