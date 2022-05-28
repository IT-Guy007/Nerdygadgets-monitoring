package nerdygadgets.Monitoring;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class MonitoringFrame extends JFrame implements ActionListener {
    int projectID = 0;
    String projectName = null;

    JButton back,five,one,twelve,twentyfour,zeven,thirty,refresh,add;
    ArrayList<Server> servers;

    public MonitoringFrame(int projectID) {
        String projectName = getProjectName(projectID);
        this.projectID = projectID;
        this.projectName = projectName;
        setTitle("Monitoring van " + projectName);
        setLayout(new GridLayout(9,5));
        GridBagConstraints layout = new GridBagConstraints();
        layout.weightx = 1;
        layout.weighty = 1;
        layout.gridwidth = 1;
        layout.gridheight = 1;
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(950,500);
        layout.fill = GridBagConstraints.HORIZONTAL;

        //Back button
        back = new JButton("Terug");
        back.setSize(100,50);
        back.addActionListener(this);
        back.setVisible(true);
        layout.gridx = 0;
        layout.gridy = 0;
        add(back,layout);

        //five button
        five = new JButton("5 minuten");
        five.setSize(100,50);
        five.addActionListener(this);
        five.setVisible(true);
        layout.gridx = 1;
        layout.gridy = 0;
        add(five,layout);

        //one button
        one = new JButton("1 uur");
        one.setSize(100,50);
        one.addActionListener(this);
        one.setVisible(true);
        layout.gridx = 2;
        layout.gridy = 0;
        add(one,layout);

        //twelve button
        twelve = new JButton("12 uur");
        twelve.setSize(100,50);
        twelve.addActionListener(this);
        twelve.setVisible(true);
        layout.gridx =3;
        layout.gridy = 0;
        add(twelve,layout);

        //twentyfour button
        twentyfour = new JButton("24 uur");
        twentyfour.setSize(100,50);
        twentyfour.addActionListener(this);
        twentyfour.setVisible(true);
        layout.gridx = 4;
        layout.gridy = 0;
        add(twentyfour,layout);

        //zeven button
        zeven = new JButton("7 dagen");
        zeven.setSize(100,50);
        zeven.addActionListener(this);
        zeven.setVisible(true);
        layout.gridx = 5;
        layout.gridy = 0;
        add(zeven,layout);

        //thirty button
        thirty = new JButton("30 dagen");
        thirty.setSize(100,50);
        thirty.addActionListener(this);
        thirty.setVisible(true);
        layout.gridx = 6;
        layout.gridy = 0;
        add(thirty,layout);

        //Refresh button
        refresh = new JButton("Refresh");
        refresh.setSize(100,50);
        refresh.addActionListener(this);
        refresh.setVisible(true);
        layout.gridx = 7;
        layout.gridy = 0;
        add(refresh,layout);

        //add button
        add = new JButton("Server toevoegen");
        add.setSize(100,50);
        add.addActionListener(this);
        add.setVisible(true);
        layout.gridx = 8;
        layout.gridy = 0;
        add(add,layout);


        layout.fill = GridBagConstraints.HORIZONTAL;


        if(servers == null) {
            JLabel noServers = new JLabel("Geen servers gevonden in het project");
            layout.gridx = 3;
            layout.gridy = 2;
            layout.gridwidth = 4;
            noServers.setVisible(true);
            add(noServers,layout);

            for(int i = 0; i != 5; i++) {
                JLabel spacer = new JLabel();
                layout.gridy = (i + 1);
                add(spacer,layout);
            }
        } else {
            //Code that generates the serverlist
            ArrayList<Integer> servers_in_project = servers_in_project(projectID);
            layout.gridx = 1;
            layout.gridwidth = 3;
            for (int i = 0; i != servers_in_project.size(); i++) {
                JLabel servername = new JLabel("test");
                layout.gridx = (i + 1);
                servername.setVisible(true);
                add(servername, layout);
            }
        }
        setVisible(true);

    }

    public ArrayList<Integer> servers_in_project(int projectID) {
        ArrayList<Integer> servers_in_project = new ArrayList<>();

        try {
            Connection con = null;
            PreparedStatement p = null;
            ResultSet rs = null;
            String dbhost = "jdbc:mysql://192.168.1.103/application";
            String user = "group4";
            String password = "Qwerty1@";

            String sql = "select server_PresentID from project_Has_Servers where projectID = " + projectID;
            p = con.prepareStatement(sql);
            rs = p.executeQuery();

            while (rs.next()) {
                servers_in_project.add(rs.getInt("serverPresentID"));
            }
        } catch(Exception exception) {
            System.out.println(exception);
        }

        return servers_in_project;
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == back) {
            setVisible(false);
            new ProjectFrame();
        } else if(e.getSource() == add) {
            new ServerToevoegen(projectID);
        } else if(e.getSource() == refresh) {
            dispose();
            new MonitoringFrame(projectID);
        }

    }

    public String getProjectName(int i) {
        String name = null;
        Connection con = null;
        PreparedStatement p = null;
        ResultSet rs = null;

        ArrayList<Project> output = new ArrayList<Project>();
        String dbhost = "jdbc:mysql://192.168.1.103/application";
        String user = "group4";
        String password = "Qwerty1@";

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection(dbhost, user, password);

            String sql = "select name from project where projectID = " + i;
            p = con.prepareStatement(sql);
            rs = p.executeQuery();

            // In array zetten;
            while (rs.next()) {
                name = rs.getString("name");
                return name;
            }


        } catch (Exception ce) {
            System.err.println("error in connection");
            ce.printStackTrace();

        }


        return name;
    }
}