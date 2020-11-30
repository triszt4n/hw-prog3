package pluto.views;

import pluto.controllers.UserController;
import pluto.models.UserModel;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;

public class UserEditView extends RegistrationView {
    private final UserModel user;

    private void editComponents() {
        emailField.setText(user.getEmail());
        addressField.setText(user.getAddress());
        dobField.setText((new SimpleDateFormat("yyyy-MM-dd")).format(user.getParsedDob()));
        nameField.setText(user.getName());
        isInstructorCheck.setVisible(false);

        main.setTitle("Pluto | Edit user");
        promptLabel.setText("Fill in the form to edit user data");
        saveBtn.setText("Save");
        main.getRootPane().setDefaultButton(saveBtn);
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
                userController.update(user.getPlutoCode());
            }
        });
    }

    public UserEditView(UserModel user, UserController userCtrl, boolean canChangePassword) {
        super(userCtrl);
        this.user = user;
        editComponents();
        if (!canChangePassword) {
            pwField.setVisible(false);
            pwLabel.setVisible(false);
        }
        main.pack();
    }
}
