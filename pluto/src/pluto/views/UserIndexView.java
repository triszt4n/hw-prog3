package pluto.views;

import pluto.controllers.UserController;
import pluto.models.InstructorModel;
import pluto.models.UserModel;
import pluto.models.helpers.UserType;
import pluto.views.helpers.UsersTableModel;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

/**
 * "index" view for user resource
 * only admins see this
 * @see pluto.controllers.UserController
 */
public class UserIndexView extends AbstractView {
    protected final List<? extends UserModel> users;
    private final UserController userController;
    protected UsersTableModel data;
    protected JTable table;
    protected JButton backBtn;
    protected JButton deleteBtn;
    protected JButton editBtn;
    protected JCheckBox acceptCheck;
    protected JLabel plutoLabel;
    protected JLabel promptLabel;

    @Override
    protected void initComponents() {
        JPanel promptPanel = new JPanel();
        promptLabel = new JLabel("Administrator dashboard - Modify users");
        promptPanel.add(promptLabel);
        promptPanel.setMinimumSize(new Dimension(200, 80));
        add(promptPanel, BorderLayout.NORTH);

        table = new JTable();
        data = new UsersTableModel(users);
        table.setModel(data);
        table.setRowSelectionAllowed(true);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setFillsViewportHeight(true);

        JScrollPane tablePane = new JScrollPane(table);
        add(tablePane, BorderLayout.CENTER);

        JPanel modifyPanel = new JPanel(new BorderLayout(10, 10));
        modifyPanel.setMinimumSize(new Dimension(600, 80));

        backBtn = new JButton("Back");
        plutoLabel = new JLabel("", JLabel.RIGHT);

        JPanel actionPanel = new JPanel();
        acceptCheck = new JCheckBox("Set as accepted");
        deleteBtn = new JButton("Delete");
        editBtn = new JButton("Edit");
        actionPanel.add(acceptCheck);
        actionPanel.add(editBtn);
        actionPanel.add(deleteBtn);

        editBtn.setEnabled(false);
        deleteBtn.setEnabled(false);
        acceptCheck.setEnabled(false);

        modifyPanel.add(backBtn, BorderLayout.WEST);
        modifyPanel.add(plutoLabel, BorderLayout.CENTER);
        modifyPanel.add(actionPanel, BorderLayout.EAST);

        modifyPanel.setBorder(new EmptyBorder(20,50,20,50));

        add(modifyPanel, BorderLayout.SOUTH);
        initListeners();
    }

    @Override
    protected void initListeners() {
        ListSelectionModel selectionModel = table.getSelectionModel();
        selectionModel.addListSelectionListener(e -> {
            if (table.getSelectedRow() != -1) {
                deleteBtn.setEnabled(true);
                editBtn.setEnabled(true);
                UserModel user = users.get(table.getSelectedRow());
                plutoLabel.setText(user.getName() + " - " + user.getType().toString());
                acceptCheck.setEnabled(user.getType().equals(UserType.INSTRUCTOR));
                acceptCheck.setSelected(user.getType().equals(UserType.INSTRUCTOR) && ((InstructorModel)user).isAccepted());
            }
            else {
                deleteBtn.setEnabled(false);
                editBtn.setEnabled(false);
                plutoLabel.setText("");
                acceptCheck.setEnabled(false);
            }
        });

        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent mouseEvent) {
                if (mouseEvent.getClickCount() == 2 && table.getSelectedRow() != -1) {
                    String pluto = (String) table.getValueAt(table.getSelectedRow(), UsersTableModel.UserColumn.PLUTO.ordinal());
                    userController.edit(pluto);
                }
            }
        });

        acceptCheck.addActionListener(e -> {
            InstructorModel user = (InstructorModel) users.get(table.getSelectedRow());
            user.setAccepted(acceptCheck.isSelected());
            data.fireTableDataChanged();
        });

        editBtn.addActionListener(e -> {
            String pluto = (String) table.getValueAt(table.getSelectedRow(), UsersTableModel.UserColumn.PLUTO.ordinal());
            userController.edit(pluto);
        });

        deleteBtn.addActionListener(e -> {
            String pluto = (String) table.getValueAt(table.getSelectedRow(), UsersTableModel.UserColumn.PLUTO.ordinal());
            userController.delete(pluto);
            data.fireTableDataChanged();
        });

        backBtn.addActionListener(e -> userController.back());
    }

    public UserIndexView(List<? extends UserModel> users, UserController userCtrl) {
        super();
        this.users = users;
        userController = userCtrl;
        setTitle("Pluto | Administration: Users");
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setMinimumSize(new Dimension(800, 560));
        initComponents();
        setLocationRelativeTo(null);
        initCloseListener(userController);
    }

    @Override
    public void setEnabled(boolean b) {
        super.setEnabled(b);
        data.fireTableDataChanged();
    }
}
