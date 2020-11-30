package pluto.views;

import pluto.controllers.SubjectController;
import pluto.models.SubjectModel;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SubjectEditView extends SubjectBuildView {
    private SubjectModel subject;

    public SubjectEditView(SubjectModel subject, SubjectController subjectController) {
        super(subjectController);
        this.subject = subject;
        editComponents();
    }

    private void editComponents() {
        promptLabel.setText("Editing subject");
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
