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
 * View for the subjects of taken courses by the student.
 */
public class SubjectIndexStudentView extends AbstractView {
    private final SubjectController subjectController;
    private final List<SubjectModel> subjects;
    private SubjectsTableModel data;
    private JTable table;
    private JButton backBtn;
    private JButton dropBtn;
    private JButton changeBtn;
    private JLabel nameLabel;

    @Override
    protected void initComponents() {
        JPanel promptPanel = new JPanel();
        JLabel promptLabel = new JLabel("Manage user's taken subjects");
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

        JPanel actionPanel = new JPanel();
        dropBtn = new JButton("Drop taken courses");
        changeBtn = new JButton("Change taken courses");
        actionPanel.add(changeBtn);
        actionPanel.add(dropBtn);

        changeBtn.setEnabled(false);
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
                SubjectModel subject = subjects.get(table.getSelectedRow());
                dropBtn.setEnabled(subject.isOpened());
                changeBtn.setEnabled(subject.isOpened());
                nameLabel.setText(subject.getName() + " - " + (subject.isOpened()? "Opened" : "Closed"));
            }
            else {
                dropBtn.setEnabled(false);
                changeBtn.setEnabled(false);
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

        changeBtn.addActionListener(e -> {
            String pluto = (String) table.getValueAt(table.getSelectedRow(), SubjectsTableModel.SubjectColumn.PLUTO.ordinal());
            subjectController.show(pluto);
        });

        dropBtn.addActionListener(e -> {
            String pluto = (String) table.getValueAt(table.getSelectedRow(), SubjectsTableModel.SubjectColumn.PLUTO.ordinal());
            subjectController.drop(pluto);
            data.fireTableDataChanged();
        });

        backBtn.addActionListener(e -> subjectController.back());
    }

    public SubjectIndexStudentView(List<SubjectModel> subjects, SubjectController subjCtrl) {
        super();
        subjectController = subjCtrl;
        this.subjects = subjects;
        setTitle("Pluto | Administration: Your subjects");
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
