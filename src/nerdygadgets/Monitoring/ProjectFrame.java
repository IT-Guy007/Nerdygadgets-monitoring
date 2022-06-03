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
        setMinimumSize(new Dimension(800,500));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        GridBagLayout gridbag = new GridBagLayout();
        GridBagConstraints layout = new GridBagConstraints();
        setLayout(gridbag);
        layout.fill = GridBagConstraints.HORIZONTAL;
        layout.weightx = 1.0;
        layout.weighty = 1.0;
        layout.gridwidth = 1;
        layout.gridheight = 1;
        layout.insets.set(0,0,0,0);
        getContentPane().setBackground(Color.white);

        //Back button
        layout.gridx = 0; layout.gridy = 0;back = new JButton("Back");back.addActionListener(this);back.setSize(2,1);back.setVisible(true);add(back, layout);
        //Refresh
        layout.gridx = 1; layout.gridy = 0;refresh = new JButton("Refresh");refresh.addActionListener(this);refresh.setSize(2,1);refresh.setVisible(true);add(refresh, layout);
        //create project;
        layout.gridx = 2; layout.gridy = 0;create_project = new JButton("New project");create_project.setSize(new Dimension(200,50));create_project.addActionListener(this);create_project.setVisible(true);add(create_project, layout);
        //Delete project
        layout.gridx = 3; layout.gridy = 0;delete_project = new JButton("Delete project");delete_project.setSize(new Dimension(200,50));delete_project.addActionListener(this);delete_project.setVisible(true);add(delete_project, layout);

        ArrayList<Project> projects;
        projects = getprojects();
        int number_of_projects;
        boolean projects_present = false;
        number_of_projects = projects.size();
        System.out.println("Found " + number_of_projects + " projects");

        if(number_of_projects == 0) {
            layout.gridx = 0;
            for (int i = 0; i != 5; i++) {
                JLabel spacer = new JLabel();
                layout.gridy = i + 2;
                spacer.setVisible(true);
                add(spacer, layout);
            }
        }
        if(number_of_projects < 5) {
            layout.gridx = 0;
            for (int i = 0; i != 5; i++) {
                JLabel spacer = new JLabel();
                layout.gridy = i + 2;
                spacer.setVisible(true);
                add(spacer, layout);
                projects_present = true;
            }

        } else {
            projects_present = true;
            setSize(450,(100 + (number_of_projects * 40)));
        }

        //Projects
        if(projects_present) {
            layout.gridx = 0; layout.gridy = 1; JLabel project_name = new JLabel("Projectnaam");project_name.setVisible(true);add(project_name, layout);
            layout.gridx = 2; layout.gridy = 1; layout.gridwidth = 2; JLabel spacer = new JLabel("Beschikbaarheid");spacer.setVisible(true);add(spacer, layout); layout.gridwidth = 1;
            for(int i = 0; i != projects.size(); i++) {
                Project project = projects.get(i);
                int projectID = project.getProjectID();

                //Projectname
                layout.gridx = 0; layout.gridy = i + 2; layout.gridwidth = 2;JLabel lbl_projectname = new JLabel(project.getName()); lbl_projectname.setVisible(true); add(lbl_projectname, layout); layout.gridwidth = 1;
                //Projectname
                layout.gridx = 2; layout.gridy = i + 2; JLabel lbl_availability = new JLabel(Double.toString(project.getWanted_availability())); lbl_availability.setVisible(true); add(lbl_availability, layout);
                layout.gridx = 3; layout.gridy = i+ 2; JButton projectbutton = new JButton("Project bekijken"); projectbutton.setVisible(true); add(projectbutton, layout); int finalI = i; projectbutton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {new MonitoringFrame(projectID);setVisible(false);}});}
        } else {
            layout.gridx = 1; layout.gridy = 2; layout.gridwidth = 2; JLabel none_found = new JLabel("Geen projecten gevonden");none_found.setVisible(true); add(none_found, layout); layout.gridwidth = 1;
        }

        setVisible(true);
    }

    //Connection to database and get all projects;
    public ArrayList<Project> getprojects() {
        Connection con;
        PreparedStatement p;
        ResultSet rs;
        int projectID;
        String name;
        int wanted_availability;

        ArrayList<Project> output = new ArrayList<Project>();
        String dbhost = "jdbc:mysql://192.168.1.1/application";
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
