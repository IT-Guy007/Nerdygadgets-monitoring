package nerdygadgets.Monitoring;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class ServerStats extends JFrame implements ActionListener {
    int serverPresentID;
    int projectID;
    JButton back,five,one,twelve,twentyfour,zeven,thirty,refresh,add;


    public ServerStats(int server_PresentID, int projectID) {
        this.serverPresentID = server_PresentID;
        this.projectID = projectID;
        Server serverInfo = getServerInfo(serverPresentID);

        setTitle("Monitoring van " + serverInfo.name);
        GridBagLayout gridbag = new GridBagLayout();
        GridBagConstraints layout = new GridBagConstraints();
        setLayout(gridbag);
        layout.fill = GridBagConstraints.HORIZONTAL;
        layout.weightx = 1.0;
        layout.weighty = 1.0;
        layout.gridwidth = 1;
        layout.gridheight = 1;
        layout.insets.set(0,0,0,0);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(950,500);

        //five button
        layout.gridx = 1; layout.gridy = 0; five = new JButton("5 minuten");five.setSize(100,50);five.addActionListener(this);five.setVisible(true);;add(five,layout);
        //one button
        layout.gridx = 2; layout.gridy = 0; one = new JButton("1 uur");one.setSize(100,50);one.addActionListener(this);one.setVisible(true);add(one,layout);
        //twelve button
        layout.gridx = 3; layout.gridy = 0; twelve = new JButton("12 uur");twelve.setSize(100,50);twelve.addActionListener(this);twelve.setVisible(true);add(twelve,layout);
        //twentyfour button
        layout.gridx = 4; layout.gridy = 0; twentyfour = new JButton("24 uur");twentyfour.setSize(100,50);twentyfour.addActionListener(this);twentyfour.setVisible(true);;add(twentyfour,layout);
        //zeven button
        layout.gridx = 5; layout.gridy = 0; zeven = new JButton("7 dagen");zeven.setSize(100,50);zeven.addActionListener(this);zeven.setVisible(true);add(zeven,layout);
        //thirty button
        layout.gridx = 6; layout.gridy = 0; thirty = new JButton("30 dagen");thirty.setSize(100,50);thirty.addActionListener(this);thirty.setVisible(true);add(thirty,layout);
        //Refresh button
        layout.gridx = 7; layout.gridy = 0; refresh = new JButton("Refresh");refresh.setSize(100,50);refresh.addActionListener(this);refresh.setVisible(true);add(refresh,layout);

        setVisible(true);
    }

    public Server getServerInfo(int serverPresentID) {

        Connection con = null;
        PreparedStatement p = null;
        ResultSet rs = null;

        int serverID;
        String name;
        int price;
        double availability;
        int serverport;
        int storage ;
        String server_kind;
        String ipaddress;
        boolean up;

        String dbhost = "jdbc:mysql://192.168.1.103/application";
        String user = "group4";
        String password = "Qwerty1@";

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection(dbhost, user, password);

            String sql = "select s.serverID as serverID,s.name as name,s.price as price,s.availability as availability,s.port as port,s.storage_Total as storage,sk.name as serverkind,s.ipaddress as ipaddress, sp.up as up from servers as s\n" +
                    "join server_Present sp on s.serverID = sp.serverID\n" +
                    "join server_Kind sk on sk.server_KindID = s.server_KindID\n" +
                    "where server_PresentID = " + serverPresentID;
            p = con.prepareStatement(sql);
            rs = p.executeQuery();

            // In array zetten;
            while (rs.next()) {
                serverID = rs.getInt("serverID");
                name = rs.getString("Name");
                price = rs.getInt("price");
                availability = rs.getInt("availability");
                serverport = rs.getInt("port");
                storage = rs.getInt("storage");
                server_kind = rs.getString("serverkind");
                ipaddress = rs.getString("ipaddress");
                up = rs.getBoolean("up");

                Server server = new Server(serverID,name,price,availability,serverport,storage,server_kind,ipaddress,up);
                return server;
            }
        } catch (Exception ce) {
            System.err.println("error");
            System.out.println(ce);
            System.out.println();
        }

        return null;
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == back) {
            dispose();
            new MonitoringFrame(projectID);
        }
    }
}
