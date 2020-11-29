package pluto.views;

import pluto.controllers.UserController;
import pluto.models.UserModel;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;

public class UserEditView extends RegistrationView {
    private UserModel user;
    private int userIndex;

    private void editComponents() {
        promptLabel.setText("Fill in the form to edit user data");
        saveBtn.setText("Save");
        initListeners();
    }

    @Override
    protected void initListeners() {
        backBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                userController.back();
            }
        });

        saveBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                userController.update(user.getIndex());
            }
        });
    }

    public UserEditView(UserModel user, UserController userCtrl, boolean canChangePassword) {
        super(userCtrl);
        main.setTitle("Pluto | Edit user");
        this.user = user;
        emailField.setText(user.getEmail());
        addressField.setText(user.getAddress());
        dobField.setText((new SimpleDateFormat("yyyy-MM-dd")).format(user.getParsedDob()));
        nameField.setText(user.getName());

        isInstructorCheck.setVisible(false);
        if (!canChangePassword) {
            pwField.setVisible(false);
            pwLabel.setVisible(false);
        }

        editComponents();
        main.pack();
    }
}
