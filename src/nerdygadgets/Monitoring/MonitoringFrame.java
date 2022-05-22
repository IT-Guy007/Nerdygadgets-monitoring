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
    private int maxiumservercount;
    private boolean isVolscherm = false;
    int projectID = 0;
    String projectName = null;

    JButton back,five,one,twelve,twentyfour,zeven,thirty,refresh,add;
    ArrayList<Server> servers;

    public MonitoringFrame(int projectID) {
        String projectName = getProjectName(projectID);
        this.projectID = projectID;
        this.projectName = projectName;
        setTitle("Monitoring van " + projectName);
        setLayout(new FlowLayout());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(950,500); //Maakt de groote van de gui de helft van de schermgrootte
        setVisible(true);

        //Back button
        back = new JButton("Terug");
        back.setSize(100,50);
        back.addActionListener(this);
        back.setVisible(true);
        add(back);

        //five button
        five = new JButton("5 minuten");
        five.setSize(100,50);
        five.addActionListener(this);
        five.setVisible(true);
        add(five);

        //one button
        one = new JButton("1 uur");
        one.setSize(100,50);
        one.addActionListener(this);
        one.setVisible(true);
        add(one);

        //twelve button
        twelve = new JButton("12 uur");
        twelve.setSize(100,50);
        twelve.addActionListener(this);
        twelve.setVisible(true);
        add(twelve);

        //twentyfour button
        twentyfour = new JButton("24 uur");
        twentyfour.setSize(100,50);
        twentyfour.addActionListener(this);
        twentyfour.setVisible(true);
        add(twentyfour);

        //zeven button
        zeven = new JButton("7 dagen");
        zeven.setSize(100,50);
        zeven.addActionListener(this);
        zeven.setVisible(true);
        add(zeven);

        //thirty button
        thirty = new JButton("30 dagen");
        thirty.setSize(100,50);
        thirty.addActionListener(this);
        thirty.setVisible(true);
        add(thirty);

        //Refresh button
        refresh = new JButton("Refresh");
        refresh.setSize(100,50);
        refresh.addActionListener(this);
        refresh.setVisible(true);
        add(refresh);

        //add button
        add = new JButton("Server toevoegen");
        add.setSize(100,50);
        add.addActionListener(this);
        add.setVisible(true);
        add(add);


        if(servers == null) {
            JLabel noServers = new JLabel("Geen servers gevonden in het project");
            noServers.setVisible(true);
            add(noServers);
        }

        setVisible(true);

    }

    public void charts() {
        //Code that generates the charts
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