package pluto.models;

import pluto.database.Database;
import pluto.exceptions.EntityNotFoundException;
import pluto.exceptions.ValidationException;

import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.stream.Collectors;

public class StudentModel extends UserModel {
    public StudentModel(String em, String na, String pw, String d, String addr) throws ValidationException, NoSuchAlgorithmException {
        super(em, na, pw, d, addr);
    }

    @Override
    public String getTitle() {
        return "Student";
    }

    @Override
    public void initCoursesAndSubjects(List<String> plutoCodes) {
        myCourses = Database.getCoursesWherePlutoCodeIn(plutoCodes);
        mySubjects = myCourses.stream()
                .map(CourseModel::getSubject)
                .distinct()
                .collect(Collectors.toList());
    }

    @Override
    public void manageSubjectsAndCoursesBeforeDelete() throws EntityNotFoundException {
        myCourses.forEach(c -> c.removeStudent(this));
    }
}
