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
    JLabel title, name;
    JTextField txt_name;
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

        //Inputfield
        txt_name = new JTextField("",15);
        add(txt_name);

        // Submit
        submit = new JButton("Aanmaken");
        submit.addActionListener(this);
        submit.setSize(1,2);
        add(submit);

        setVisible(true);
    }


    public void CreateProject(String projectnaam) {

        Connection connection = null;
        PreparedStatement p = null;
        ResultSet rs = null;
        ArrayList<String> output = new ArrayList<String>();
        try {
            // Importing and registering drivers
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql:/windesheim.app:3306/application", "group4", "Qwerty1@");

            // SQL command data stored in String datatype
            String sql = "Insert Into project values(null," + projectnaam + ")";
            p = con.prepareStatement(sql);
            rs = p.executeQuery();

            // In array zetten;
            while (rs.next()) {
                output.set(rs.getInt("ID"),rs.getString("naam"));
            }


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
            CreateProject(txt_name.getText());
            dispose();
        }
    }
}
