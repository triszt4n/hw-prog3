package pluto.views;

import pluto.app.PlutoConsole;
import pluto.controllers.UserController;
import pluto.models.InstructorModel;
import pluto.models.UserModel;
import pluto.views.helpers.UsersTableModel;

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

public class UserIndexView extends AbstractView {
    private final List<UserModel> users;
    private final UserController userController;
    private UsersTableModel data;
    private JTable table;
    private JButton backBtn;
    private JButton deleteBtn;
    private JButton editBtn;
    private JCheckBox acceptCheck;
    private JLabel plutoLabel;

    @Override
    protected void initComponents() {
        JPanel promptPanel = new JPanel();
        JLabel promptLabel = new JLabel("Administrator dashboard - Modify users");
        promptPanel.add(promptLabel);
        promptPanel.setMinimumSize(new Dimension(200, 80));
        main.add(promptPanel, BorderLayout.NORTH);

        table = new JTable();
        data = new UsersTableModel(users);
        table.setModel(data);
        table.setRowSelectionAllowed(true);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setFillsViewportHeight(true);

        JScrollPane tablePane = new JScrollPane(table);
        main.add(tablePane, BorderLayout.CENTER);

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

        modifyPanel.setBorder(
                new EmptyBorder(20,50,20,50)
        );

        main.add(modifyPanel, BorderLayout.SOUTH);
    }

    @Override
    protected void initListeners() {
        ListSelectionModel selectionModel = table.getSelectionModel();
        selectionModel.addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                PlutoConsole.log("IN LISTLISTENER");
                if (table.getSelectedRow() != -1) {
                    deleteBtn.setEnabled(true);
                    editBtn.setEnabled(true);
                    UserModel user = users.get(table.getSelectedRow());
                    plutoLabel.setText(user.getName() + " - " + user.getTitle());
                    acceptCheck.setEnabled(user.getTitle() == "Instructor");
                    acceptCheck.setSelected(user.getTitle() == "Instructor" && ((InstructorModel)user).isAccepted());
                }
                else {
                    deleteBtn.setEnabled(false);
                    editBtn.setEnabled(false);
                    plutoLabel.setText("");
                    acceptCheck.setEnabled(false);
                }
            }
        });

        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent mouseEvent) {
                PlutoConsole.log("CLICKED TWICE");
                if (mouseEvent.getClickCount() == 2 && table.getSelectedRow() != -1) {
                    userController.edit(table.getSelectedRow());
                }
            }
        });

        acceptCheck.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                InstructorModel user = (InstructorModel) users.get(table.getSelectedRow());
                user.setAccepted(acceptCheck.isSelected());
            }
        });

        editBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                userController.edit(table.getSelectedRow());
            }
        });

        deleteBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                userController.delete(table.getSelectedRow());
                data.fireTableDataChanged();
            }
        });

        backBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                userController.back();
            }
        });
    }

    public UserIndexView(List<UserModel> users, UserController userCtrl) {
        super();
        this.users = users;
        userController = userCtrl;
        main.setTitle("Pluto | Administration: Users");
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
