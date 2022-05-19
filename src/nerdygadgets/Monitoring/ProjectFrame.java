package nerdygadgets.Monitoring;

import com.mysql.cj.jdbc.exceptions.CommunicationsException;
import nerdygadgets.MainFrame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.ArrayList;

public class ProjectFrame extends JFrame implements ActionListener {
    private ArrayList<String> projects;
    private JButton back,create_project;
    int number_of_projects;

    public ProjectFrame() {
        setTitle("Project lijst");
        setLayout(new FlowLayout());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Back button
        back = new JButton("Back");
        back.addActionListener(this);
        back.setSize(2,1);
        back.setVisible(true);
        add(back);

        //create project;
        create_project = new JButton("New project");
        create_project.setSize(new Dimension(100,50));
        create_project.addActionListener(this);
        create_project.setVisible(true);
        add(create_project);

        projects = getprojects();
        boolean projects_present = false;
        int number_of_projects = projects.size();

        if(number_of_projects == 0) {
            setSize(300,100);
        } else if (number_of_projects >= 1) {
            int height = ((number_of_projects / 4) * 100) + 100;
            setSize(400,height);
        }

        //Projects
        if(projects_present) {
            for(int i = 0; i != projects.size(); i++) {
                number_of_projects++;
                JButton naam;
                String projectnaam = projects.get(i);
                naam = new JButton(projectnaam);
                naam.setSize(100, 50);
                int finalI = i;
                naam.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        JFrame MonitoringFrame = new MonitoringFrame(finalI);
                    }
                });
                naam.setVisible(true);
            }
        }

        setVisible(true);
    }

    //Connection to database and get all projects;
    public ArrayList<String> getprojects() {

        Connection connection = null;
        PreparedStatement p = null;
        ResultSet rs = null;
        ArrayList<String> output = new ArrayList<String>();
        try {
            // Importing and registering drivers
            Class.forName("com.mysql.cj.jdbc.Driver");

            Connection con = DriverManager.getConnection("jdbc:mysql:/windesheim.nl:3306/application", "group4", "Qwerty1@");

            // SQL command data stored in String datatype
            String sql = "select * from project";
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

        return output;
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == back) {
            setVisible(false);
            new MainFrame();
        } else if (e.getSource() == create_project) {
            try {
                new CreateProject();
            } catch(Exception exception) {
                System.out.println(exception);
            }
            repaint();
        }

    }
}
