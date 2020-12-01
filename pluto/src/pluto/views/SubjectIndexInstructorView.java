package pluto.views;

import pluto.controllers.SubjectController;
import pluto.exceptions.ValidationException;
import pluto.models.SubjectModel;
import pluto.models.UserModel;
import pluto.views.helpers.SubjectsTableModel;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class SubjectIndexInstructorView extends AbstractView {
    private final SubjectController subjectController;
    private final UserModel user;
    private final List<SubjectModel> subjects;
    private SubjectsTableModel data;
    private JTable table;
    private JButton backBtn;
    private JButton deleteBtn;
    private JButton editBtn;
    private JCheckBox openCheck;
    private JLabel nameLabel;
    private JButton showCoursesBtn;

    @Override
    protected void initComponents() {
        JPanel promptPanel = new JPanel();
        JLabel promptLabel = new JLabel("Manage subjects where you are the coordinator!");
        promptPanel.add(promptLabel);
        promptPanel.setMinimumSize(new Dimension(200, 80));
        main.add(promptPanel, BorderLayout.NORTH);

        table = new JTable();
        data = new SubjectsTableModel(subjects);
        table.setModel(data);
        table.setRowSelectionAllowed(true);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setFillsViewportHeight(true);

        JScrollPane tablePane = new JScrollPane(table);
        main.add(tablePane, BorderLayout.CENTER);

        JPanel modifyPanel = new JPanel(new BorderLayout(10, 10));
        modifyPanel.setMinimumSize(new Dimension(600, 80));

        backBtn = new JButton("Back");
        nameLabel = new JLabel("", JLabel.RIGHT);

        JPanel actionPanel = new JPanel();
        openCheck = new JCheckBox("Open for taking courses");
        deleteBtn = new JButton("Delete");
        editBtn = new JButton("Edit");
        showCoursesBtn = new JButton("Show courses");
        actionPanel.add(openCheck);
        actionPanel.add(editBtn);
        actionPanel.add(showCoursesBtn);
        actionPanel.add(deleteBtn);

        editBtn.setEnabled(false);
        showCoursesBtn.setEnabled(false);
        deleteBtn.setEnabled(false);
        openCheck.setEnabled(false);

        modifyPanel.add(backBtn, BorderLayout.WEST);
        modifyPanel.add(nameLabel, BorderLayout.CENTER);
        modifyPanel.add(actionPanel, BorderLayout.EAST);

        modifyPanel.setBorder(
                new EmptyBorder(20,50,20,50)
        );

        main.add(modifyPanel, BorderLayout.SOUTH);
        initListeners();
    }

    @Override
    protected void initListeners() {
        ListSelectionModel selectionModel = table.getSelectionModel();
        selectionModel.addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                if (table.getSelectedRow() != -1) {
                    openCheck.setEnabled(true);
                    deleteBtn.setEnabled(true);
                    showCoursesBtn.setEnabled(true);
                    editBtn.setEnabled(true);
                    SubjectModel subject = subjects.get(table.getSelectedRow());
                    nameLabel.setText(subject.getName() + " - " + (subject.isOpened()? "Opened" : "Closed"));
                    openCheck.setSelected(subject.isOpened());
                }
                else {
                    deleteBtn.setEnabled(false);
                    showCoursesBtn.setEnabled(false);
                    editBtn.setEnabled(false);
                    nameLabel.setText("");
                    openCheck.setEnabled(false);
                }
            }
        });

        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent mouseEvent) {
                if (mouseEvent.getClickCount() == 2 && table.getSelectedRow() != -1) {
                    String pluto = (String) table.getValueAt(table.getSelectedRow(), SubjectsTableModel.SubjectColumn.PLUTO.ordinal());
                    subjectController.edit(pluto);
                }
            }
        });

        openCheck.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SubjectModel subject = subjects.get(table.getSelectedRow());
                try {
                    subject.setOpened(openCheck.isSelected(), user);
                } catch (ValidationException exception) {
                    JOptionPane.showMessageDialog(null, exception.getMessage(), "Validation error", JOptionPane.ERROR_MESSAGE);
                }
                nameLabel.setText(subject.getName() + " - " + (subject.isOpened()? "Opened" : "Closed"));
                data.fireTableDataChanged();
            }
        });

        editBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String pluto = (String) table.getValueAt(table.getSelectedRow(), SubjectsTableModel.SubjectColumn.PLUTO.ordinal());
                subjectController.edit(pluto);
            }
        });

        showCoursesBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String pluto = (String) table.getValueAt(table.getSelectedRow(), SubjectsTableModel.SubjectColumn.PLUTO.ordinal());
                subjectController.show(pluto);
            }
        });

        deleteBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String pluto = (String) table.getValueAt(table.getSelectedRow(), SubjectsTableModel.SubjectColumn.PLUTO.ordinal());
                subjectController.delete(pluto);
                data.fireTableDataChanged();
            }
        });

        backBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                subjectController.back();
            }
        });
    }

    public SubjectIndexInstructorView(List<SubjectModel> subjects, UserModel user, SubjectController subjectController) {
        super();
        this.subjectController = subjectController;
        this.user = user;
        this.subjects = subjects;
        main.setTitle("Pluto | Administration: Your subjects");
        main.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        main.setMinimumSize(new Dimension(800, 560));
        initComponents();
        main.setLocationRelativeTo(null);
    }

    @Override
    public void enable() {
        super.enable();
        data.fireTableDataChanged();
    }
}
