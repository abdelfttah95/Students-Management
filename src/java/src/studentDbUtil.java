package src;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author abdel
 */
public class studentDbUtil {

    public List<student> getStudents() throws Exception {

        List<student> students = new ArrayList<>();

        Connection myConn = null;
        Statement myStmt = null;
        ResultSet myRs = null;

        try {
            Class.forName("com.mysql.jdbc.Driver");

            myConn = DriverManager.getConnection("jdbc:mysql://localhost:3306/student_tracker", "root", "");

            String sql = "select * from student order by last_name";

            myStmt = myConn.createStatement();

            myRs = myStmt.executeQuery(sql);

            // process result set
            while (myRs.next()) {

                // retrieve data from result set row
                int id = myRs.getInt("id");
                String firstName = myRs.getString("first_name");
                String lastName = myRs.getString("last_name");
                String email = myRs.getString("email");

                // create new student object
                student tempStudent = new student(id, firstName, lastName, email);

                // add it to the list of students
                students.add(tempStudent);
            }

            return students;
        } finally {
            myConn.close();
            myStmt.close();
            myRs.close();
        }
    }

    public void addStudent(student theStudent) throws Exception {

        Connection myConn = null;
        PreparedStatement myStmt = null;

        try {
            Class.forName("com.mysql.jdbc.Driver");

            myConn = DriverManager.getConnection("jdbc:mysql://localhost:3306/student_tracker", "root", "");

            String sql = "insert into student (first_name, last_name, email) values (?, ?, ?)";

            myStmt = myConn.prepareStatement(sql);

            // set params
            myStmt.setString(1, theStudent.getFirstName());
            myStmt.setString(2, theStudent.getLastName());
            myStmt.setString(3, theStudent.getEmail());

            myStmt.execute();
        } finally {
            myConn.close();
            myStmt.close();
        }
    }

    public student getStudent(int studentId) throws Exception {

        Connection myConn = null;
        PreparedStatement myStmt = null;
        ResultSet myRs = null;

        try {
            Class.forName("com.mysql.jdbc.Driver");

            myConn = DriverManager.getConnection("jdbc:mysql://localhost:3306/student_tracker", "root", "");

            String sql = "select * from student where id=?";

            myStmt = myConn.prepareStatement(sql);

            // set params
            myStmt.setInt(1, studentId);

            myRs = myStmt.executeQuery();

            student theStudent = null;

            // retrieve data from result set row
            if (myRs.next()) {
                int id = myRs.getInt("id");
                String firstName = myRs.getString("first_name");
                String lastName = myRs.getString("last_name");
                String email = myRs.getString("email");

                theStudent = new student(id, firstName, lastName, email);
            } else {
                throw new Exception("Could not find student id: " + studentId);
            }

            return theStudent;
        } finally {
            myConn.close();
            myStmt.close();
            myRs.close();
        }
    }

    public void updateStudent(student theStudent) throws Exception {

        Connection myConn = null;
        PreparedStatement myStmt = null;

        try {
            Class.forName("com.mysql.jdbc.Driver");

            myConn = DriverManager.getConnection("jdbc:mysql://localhost:3306/student_tracker", "root", "");

            String sql = "update student "
                    + " set first_name=?, last_name=?, email=?"
                    + " where id=?";

            myStmt = myConn.prepareStatement(sql);

            // set params
            myStmt.setString(1, theStudent.getFirstName());
            myStmt.setString(2, theStudent.getLastName());
            myStmt.setString(3, theStudent.getEmail());
            myStmt.setInt(4, theStudent.getId());

            myStmt.execute();
        } finally {
            myConn.close();
            myStmt.close();
        }

    }

    public void deleteStudent(int studentId) throws Exception {

        Connection myConn = null;
        PreparedStatement myStmt = null;

        try {
            Class.forName("com.mysql.jdbc.Driver");

            myConn = DriverManager.getConnection("jdbc:mysql://localhost:3306/student_tracker", "root", "");

            String sql = "delete from student where id=?";

            myStmt = myConn.prepareStatement(sql);

            // set params
            myStmt.setInt(1, studentId);

            myStmt.execute();
        } finally {
            myConn.close();
            myStmt.close();
        }
    }
}
