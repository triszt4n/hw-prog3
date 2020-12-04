package pluto.views;

import pluto.controllers.SubjectController;
import pluto.models.SubjectModel;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * "edit" view for subject resource
 * @see pluto.controllers.AbstractController
 */
public class SubjectEditView extends SubjectBuildView {
    private final SubjectModel subject;

    public SubjectEditView(SubjectModel subject, SubjectController subjectController) {
        super(subjectController);
        this.subject = subject;
        editComponents();
    }

    private void editComponents() {
        main.setTitle("Pluto | Edit subject");
        promptLabel.setText("Editing subject: " + subject.getPlutoCode());
        reqField.setText(subject.getRequirements());
        nameField.setText(subject.getName());
        creditField.setText(String.valueOf(subject.getCredit()));
        semesterField.setText(String.valueOf(subject.getSemester()));
        isOpenedCheck.setSelected(subject.isOpened());
    }

    @Override
    protected void initListeners() {
        backBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                subjectController.back();
            }
        });

        saveBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                subjectController.update(subject.getPlutoCode());
            }
        });
    }
}
