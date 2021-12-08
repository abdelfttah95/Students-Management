package src;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

/**
 *
 * @author abdel
 */
@ManagedBean
@SessionScoped
public class studentController {

    private List<student> students;
    private studentDbUtil studentdbUtil;
    private Logger logger = Logger.getLogger(getClass().getName());

    public studentController() throws Exception {
        students = new ArrayList<>();
        studentdbUtil = new studentDbUtil();

    }

    public List<student> getStudents() {
        return students;
    }

    public void loadStudents() {

        logger.info("Loading students");

        students.clear();

        try {

            // get all students from database
            students = studentdbUtil.getStudents();

        } catch (Exception exc) {
            // send this to server logs
            logger.log(Level.SEVERE, "Error loading students", exc);

            // add error message for JSF page
            addErrorMessage(exc);
        }
    }

    public String addStudent(student theStudent) {

        logger.info("Adding student: " + theStudent);

        try {

            // add student to the database
            studentdbUtil.addStudent(theStudent);

        } catch (Exception exc) {
            // send this to server logs
            logger.log(Level.SEVERE, "Error adding students", exc);

            // add error message for JSF page
            addErrorMessage(exc);

            return null;
        }

        return "list-students?faces-redirect=true";
    }

    public String loadStudent(int studentId) {

        logger.info("loading student: " + studentId);

        try {
            // get student from database
            student theStudent = studentdbUtil.getStudent(studentId);

            // put in the request attribute ... so we can use it on the form page
            ExternalContext externalContext
                    = FacesContext.getCurrentInstance().getExternalContext();

            Map<String, Object> requestMap = externalContext.getRequestMap();
            requestMap.put("student", theStudent);

        } catch (Exception exc) {
            // send this to server logs
            logger.log(Level.SEVERE, "Error loading student id:" + studentId, exc);

            // add error message for JSF page
            addErrorMessage(exc);

            return null;
        }

        return "update-student-form.xhtml";
    }

    public String updateStudent(student theStudent) {

        logger.info("updating student: " + theStudent);

        try {

            // update student in the database
            studentdbUtil.updateStudent(theStudent);

        } catch (Exception exc) {
            // send this to server logs
            logger.log(Level.SEVERE, "Error updating student: " + theStudent, exc);

            // add error message for JSF page
            addErrorMessage(exc);

            return null;
        }

        return "list-students?faces-redirect=true";
    }

    public String deleteStudent(int studentId) {

        logger.info("Deleting student id: " + studentId);

        try {

            // delete the student from the database
            studentdbUtil.deleteStudent(studentId);

        } catch (Exception exc) {
            // send this to server logs
            logger.log(Level.SEVERE, "Error deleting student id: " + studentId, exc);

            // add error message for JSF page
            addErrorMessage(exc);

            return null;
        }

        return "list-students";
    }

    private void addErrorMessage(Exception exc) {
        FacesMessage message = new FacesMessage("Error: " + exc.getMessage());
        FacesContext.getCurrentInstance().addMessage(null, message);
    }
}
