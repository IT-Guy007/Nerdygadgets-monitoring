package nerdygadgets.Monitoring;

import nerdygadgets.MainFrame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class ProjectFrame extends JFrame implements ActionListener {
    private JButton back,create_project,delete_project,refresh;

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

        //Refresh
        refresh = new JButton("refresh");
        refresh.addActionListener(this);
        refresh.setSize(2,1);
        refresh.setVisible(true);
        add(refresh);

        //create project;
        create_project = new JButton("New project");
        create_project.setSize(new Dimension(200,50));
        create_project.addActionListener(this);
        create_project.setVisible(true);
        add(create_project);

        //Delete project
        delete_project = new JButton("Delete project");
        delete_project.setSize(new Dimension(200,50));
        delete_project.addActionListener(this);
        delete_project.setVisible(true);
        add(delete_project);

        ArrayList<Project> projects = new ArrayList<Project>();
        projects = getprojects();
        int number_of_projects = 0;
        boolean projects_present = false;
        number_of_projects = projects.size();

        if(number_of_projects == 0) {
            setSize(450,100);
        } else if (number_of_projects >= 1) {
            int height = ((number_of_projects / 4) * 100) + 100;
            setSize(450,height);
            projects_present = true;
        }

        //Projects
        if(projects_present) {
            for(int i = 0; i != projects.size(); i++) {
                JButton projectbutton;
                Project project = projects.get(i);
                int projectID = project.ProjectID;
                projectbutton = new JButton(project.name);
                int finalI = i;
                projectbutton.setSize(200, 100);
                projectbutton.setVisible(true);
                add(projectbutton);
                projectbutton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        new MonitoringFrame(projectID);
                        setVisible(false);
                    }
                });
                projectbutton.setVisible(true);
            }
        } else {
            JLabel none_found = new JLabel("Geen projecten gevonden");
            none_found.setVisible(true);
            add(none_found);
            repaint();
        }

        setVisible(true);
    }

    //Connection to database and get all projects;
    public ArrayList<Project> getprojects() {
        Connection con = null;
        PreparedStatement p = null;
        ResultSet rs = null;
        int projectID = 0;
        String name = null;
        int wanted_availability = 0;

        ArrayList<Project> output = new ArrayList<Project>();
        String dbhost = "jdbc:mysql://192.168.1.103/application";
        String user = "group4";
        String password = "Qwerty1@";

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection(dbhost, user, password);

            String sql = "select * from project";
            p = con.prepareStatement(sql);
            rs = p.executeQuery();

            // In array zetten;
            while (rs.next()) {
                projectID = rs.getInt("projectID");
                name = rs.getString("name");
                wanted_availability = rs.getInt("wanted_Availability");

                Project listed = new Project(projectID,name,wanted_availability);
                output.add(listed);
            }


        } catch (Exception ce) {
            System.err.println("error in connection");
            ce.printStackTrace();
            JLabel error = new JLabel("Error, kan niet verbinden met de server");
            System.out.println(ce);
            error.setVisible(true);
            error.repaint();
            add(error);
            create_project.setVisible(false);
            delete_project.setVisible(false);

        }

        return output;
    }

    public void deleteProject(String name) {
        Connection con = null;
        PreparedStatement p = null;

        String dbhost = "jdbc:mysql://192.168.1.103/application";
        String user = "group4";
        String password = "Qwerty1@";

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection(dbhost, user, password);

            // SQL command data stored in String datatype
            String sql = "DELETE FROM project WHERE name = " + name;
            p = con.prepareStatement(sql);
            p.executeUpdate();

        } catch (Exception ce) {
            System.err.println("error in connection");
            ce.printStackTrace();
            JLabel error = new JLabel("Error, kan niet verbinden met de server");
            System.out.println(ce);
            error.setVisible(true);
            error.repaint();
            add(error);
            create_project.setVisible(false);
            delete_project.setVisible(false);

        }
        setVisible(true);
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == back) {
            setVisible(false);
            new MainFrame();
        } else if (e.getSource() == create_project) {
            try {
                new CreateProject();
                repaint();
            } catch(Exception exception) {
                System.out.println(exception);
            }

        } else if(e.getSource() == refresh) {
            dispose();
            new ProjectFrame();

        } else if(e.getSource() == delete_project) {
            try {
                new DeleteProject();
            } catch (Exception exception) {
                System.out.println(exception);
            }
        }

    }
}
