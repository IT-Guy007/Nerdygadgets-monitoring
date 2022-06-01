package nerdygadgets.Monitoring;

import com.mysql.cj.jdbc.exceptions.CommunicationsException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.ArrayList;

public class CreateProject extends JFrame implements ActionListener {

    JButton cancel, submit;
    JLabel title, name, availibility;
    JTextField txt_name,txt_availibility;
    int projectID = 0;

    public CreateProject() {
        setTitle("Project aanmaken");
        setLayout(new FlowLayout());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(300,200);
        setResizable(false);

        //Back button
        cancel = new JButton("cancel");
        cancel.addActionListener(this);
        cancel.setSize(100,20);
        add(cancel);

        //Projectname
        name = new JLabel("Project naam");
        name.setSize(2,1);
        add(name);
        txt_name = new JTextField("",12);
        add(txt_name);

        //Availibility
        availibility = new JLabel("Gewenste beschikbaarheid");
        availibility.setSize(2,1);
        add(availibility);
        txt_availibility = new JTextField("",4);
        add(txt_availibility);

        // Submit
        submit = new JButton("Aanmaken");
        submit.addActionListener(this);
        submit.setSize(1,2);
        add(submit);

        setVisible(true);
    }


    public void CreateProject(String projectnaam, int wanted_availability) {

        Connection con = null;
        PreparedStatement p = null;
        ResultSet rs = null;
        ArrayList<String> output = new ArrayList<String>();
        String dbhost = "jdbc:mysql://192.168.1.1/application";
        String user = "group4";
        String password = "Qwerty1@";
        projectnaam = "\"" + projectnaam + "\"";

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection(dbhost, user, password);

            // SQL command data stored in String datatype
            String sql = "INSERT INTO project VALUES(NULL," + projectnaam + "," + wanted_availability + ")";
            p = con.prepareStatement(sql);
            p.executeUpdate();


        } catch (CommunicationsException ce) {
            JLabel error = new JLabel("Error, kan niet verbinden met de server");
            error.setVisible(true);
            error.repaint();
            add(error);

        } catch (SQLException e) {
            System.out.println(e);

        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);

        }
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == cancel) {
            setVisible(false);
            new ProjectFrame();
        } else if(e.getSource() == submit) {
            CreateProject(txt_name.getText(), Integer.parseInt(txt_availibility.getText()));
            dispose();
        }
    }
}
