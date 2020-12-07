package pluto.views;

import pluto.controllers.SubjectController;
import pluto.models.SubjectModel;
import pluto.views.helpers.SubjectsTableModel;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

/***
 * View for taking courses, shows the available subjects, can go on to take courses after selecting one subject.
 */
public class SubjectIndexView extends AbstractView {
    private final SubjectController subjectController;
    private final List<SubjectModel> subjects;
    private SubjectsTableModel data;
    private JTable table;
    private JButton backBtn;
    private JButton showCoursesBtn;
    private JLabel nameLabel;

    @Override
    protected void initComponents() {
        JPanel promptPanel = new JPanel();
        JLabel promptLabel = new JLabel("Subjects dashboard - Take courses...");
        promptPanel.add(promptLabel);
        promptPanel.setMinimumSize(new Dimension(200, 80));
        add(promptPanel, BorderLayout.NORTH);

        table = new JTable();
        data = new SubjectsTableModel(subjects);
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
        showCoursesBtn = new JButton("Show courses");
        showCoursesBtn.setEnabled(false);

        modifyPanel.add(backBtn, BorderLayout.WEST);
        modifyPanel.add(nameLabel, BorderLayout.CENTER);
        modifyPanel.add(showCoursesBtn, BorderLayout.EAST);

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
                SubjectModel subject = subjects.get(table.getSelectedRow());
                showCoursesBtn.setEnabled(subject.isOpened());
                nameLabel.setText(subject.getName() + (subject.isOpened()? "" : " CLOSED FOR STUDENTS"));
            }
            else {
                showCoursesBtn.setEnabled(false);
                nameLabel.setText("");
            }
        });

        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent mouseEvent) {
                if (mouseEvent.getClickCount() == 2 && table.getSelectedRow() != -1) {
                    String pluto = (String) table.getValueAt(table.getSelectedRow(), SubjectsTableModel.SubjectColumn.PLUTO.ordinal());
                    subjectController.show(pluto);
                }
            }
        });

        showCoursesBtn.addActionListener(e -> {
            String pluto = (String) table.getValueAt(table.getSelectedRow(), SubjectsTableModel.SubjectColumn.PLUTO.ordinal());
            subjectController.show(pluto);
        });

        backBtn.addActionListener(e -> subjectController.back());
    }

    public SubjectIndexView(List<SubjectModel> subjects, SubjectController subjCtrl) {
        super();
        subjectController = subjCtrl;
        this.subjects = subjects;
        setTitle("Pluto | Taking courses: Subject list");
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setMinimumSize(new Dimension(800, 560));
        initComponents();
        setLocationRelativeTo(null);
        initCloseListener(subjectController);
    }

    @Override
    public void setEnabled(boolean b) {
        super.setEnabled(b);
        data.fireTableDataChanged();
    }
}
