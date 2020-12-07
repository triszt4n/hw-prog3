package pluto.views;

import pluto.controllers.UserController;
import pluto.models.UserModel;

import java.text.SimpleDateFormat;

/**
 * "edit" view for user resource
 * won't show password field for the administrator (they can edit any user)
 * @see pluto.controllers.UserController
 */
public class UserEditView extends RegistrationView {
    private final UserModel user;

    private void editComponents() {
        emailField.setText(user.getEmail());
        addressField.setText(user.getAddress());
        dobField.setText((new SimpleDateFormat("yyyy-MM-dd")).format(user.getParsedDob()));
        nameField.setText(user.getName());
        isInstructorCheck.setVisible(false);

        setTitle("Pluto | Edit user");
        promptLabel.setText("Fill in the form to edit user data");
        saveBtn.setText("Save");
        getRootPane().setDefaultButton(saveBtn);
    }

    @Override
    protected void initListeners() {
        backBtn.addActionListener(e -> userController.back());

        saveBtn.addActionListener(e -> userController.update(user.getPlutoCode()));
    }

    public UserEditView(UserModel user, UserController userCtrl, boolean canChangePassword) {
        super(userCtrl);
        this.user = user;
        editComponents();
        if (!canChangePassword) {
            pwField.setVisible(false);
            pwLabel.setVisible(false);
        }
        pack();
    }
}
