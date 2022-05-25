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

public class ServerToevoegen extends JFrame implements ActionListener {
    JButton back,submit;
    JLabel lbl_alias,lbl_price,lbl_availibility,lbl_port,lbl_server_kind,lbl_ip,lbl_storage;
    JTextField alias,price,availibility,port,ip,storage;

    String[] serverOptionsToChooseFrom = serverOptions();


    public ServerToevoegen(int projectID) {
        String projectName = getProjectName(projectID);
        setTitle("Server toevoegen aan het project " + projectName);
        setLayout(new FlowLayout());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400,500); //Maakt de groote van de gui de helft van de schermgrootte
        setVisible(true);

        //Back button
        back = new JButton("Cancel");
        back.setSize(100,50);
        back.addActionListener(this);
        back.setVisible(true);
        add(back);

        //Alias
        lbl_alias = new JLabel("Server name");
        lbl_alias.setVisible(true);
        add(lbl_alias);
        alias = new JTextField("",100);
        alias.setVisible(true);
        add(alias);

        //price
        lbl_price = new JLabel("Prijs");
        lbl_price.setVisible(true);
        add(lbl_price);
        price = new JTextField("",100);
        price.setVisible(true);
        add(price);

        //availibility
        lbl_availibility = new JLabel("Beschikbaarheid");
        lbl_availibility.setVisible(true);
        add(lbl_availibility);
        availibility = new JTextField("",100);
        availibility.setVisible(true);
        add(availibility);

        //port
        lbl_port = new JLabel("Serverport");
        lbl_port.setVisible(true);
        add(lbl_port);
        port = new JTextField("",100);
        port.setVisible(true);
        add(port);

        //server_kind
        lbl_server_kind = new JLabel("Serversoort");
        lbl_server_kind.setVisible(true);
        add(lbl_server_kind);
        JComboBox<String> server_kind= new JComboBox<>(serverOptionsToChooseFrom);
        server_kind.setVisible(true);
        add(server_kind);

        //ip
        lbl_ip = new JLabel("Server name");
        lbl_ip.setVisible(true);
        add(lbl_ip);
        ip = new JTextField("",100);
        ip.setVisible(true);
        add(ip);

        //storage
        lbl_storage = new JLabel("Server name");
        lbl_storage.setVisible(true);
        add(lbl_storage);
        storage = new JTextField("",100);
        storage.setVisible(true);
        add(storage);

        //submit
        submit = new JButton("Aanmaken");
        submit.setVisible(true);
        submit.addActionListener(this);
        add(submit);

    }
    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == back) {
            setVisible(false);

        } else if(e.getSource() == submit) {
            setVisible(false);
        }
    }

    public String[] serverOptions() {
        int i = 0;
        String[] serverOptions = null;
        String name = null;
        Connection con = null;
        PreparedStatement p = null;
        ResultSet rs = null;
        String dbhost = "jdbc:mysql://192.168.1.103/application";
        String user = "group4";
        String password = "Qwerty1@";

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection(dbhost, user, password);

            String sql = "select name from server_Kind";
            p = con.prepareStatement(sql);
            rs = p.executeQuery();

            // In array zetten;
            while (rs.next()) {
                name = rs.getString("name");
                serverOptions[i] = name;
                i++;
            }


        } catch (Exception ce) {
            System.err.println("error in connection");
            ce.printStackTrace();

        }

        return serverOptions;
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
