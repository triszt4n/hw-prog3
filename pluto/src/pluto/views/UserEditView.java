package pluto.views;

import pluto.controllers.UserController;
import pluto.models.UserModel;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;

public class UserEditView extends RegistrationView {
    private void editComponents() {
        promptLabel.setText("Fill in the form to edit user data");
        saveBtn.setText("Save");
        isInstructorCheck.setEnabled(false);

        backBtn.removeActionListener(backBtnListener);
        backBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                userController.update(false);
            }
        });

        saveBtn.removeActionListener(saveBtnListener);
        saveBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                userController.update(true);
            }
        });
    }

    public UserEditView(UserModel user, UserController userCtrl) {
        super(userCtrl);
        emailField.setText(user.getEmail());
        addressField.setText(user.getAddress());
        dobField.setText((new SimpleDateFormat("yyyy-MM-dd")).format(user.getParsedDob()));
        nameField.setText(user.getName());
        isInstructorCheck.setSelected(user.getTitle() == "Instructor");

        editComponents();
    }
}
