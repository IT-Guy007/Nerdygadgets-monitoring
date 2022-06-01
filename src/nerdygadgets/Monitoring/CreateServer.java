package nerdygadgets.Monitoring;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class CreateServer extends JFrame implements ActionListener {
    JButton back,submit;
    JLabel lbl1,lbl2,lbl_alias,lbl_price,lbl_availibility,lbl_port,lbl_server_kind,lbl_ip,lbl_storage,lbl_subnet,lbl_ram;
    JTextField alias,price,availibility,port,ip,storage,subnet,ram;

    String[] serverOptionsToChooseFrom = serverOptions();
    int projectID;
    int id;
    JComboBox<String> server_kind;


    public CreateServer(int projectID) {
        this.projectID = projectID;
        setTitle("Server toevoegen aan het project " + getProjectName(projectID));
        setLayout(new GridLayout(0,2,20,20));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400,500);


        //Back button
        back = new JButton("Cancel");
        back.setSize(100,50);
        back.addActionListener(this);
        back.setVisible(true);
        add(back);

        //lbl
        lbl1 = new JLabel("");
        lbl1.setVisible(true);
        add(lbl1);

        //Alias
        lbl_alias = new JLabel("Servernaam:");
        lbl_alias.setVisible(true);
        add(lbl_alias);
        alias = new JTextField("",10);
        alias.setEditable(true);
        alias.setVisible(true);
        add(alias);

        //price
        lbl_price = new JLabel("Prijs:");
        lbl_price.setVisible(true);
        add(lbl_price);
        price = new JTextField("",10);
        price.setVisible(true);
        add(price);

        //availibility
        lbl_availibility = new JLabel("Beschikbaarheid:");
        lbl_availibility.setVisible(true);
        add(lbl_availibility);
        availibility = new JTextField("",10);
        availibility.setVisible(true);
        add(availibility);

        //port
        lbl_port = new JLabel("Serverport:");
        lbl_port.setVisible(true);
        add(lbl_port);
        port = new JTextField("",10);
        port.setVisible(true);
        add(port);

        //server_kind
        lbl_server_kind = new JLabel("Serversoort:");
        lbl_server_kind.setVisible(true);
        add(lbl_server_kind);
        JComboBox<String> server_kind = new JComboBox<>(serverOptionsToChooseFrom);
        server_kind.setVisible(true);
        add(server_kind);

        //ip
        lbl_ip = new JLabel("IP address:");
        lbl_ip.setVisible(true);
        add(lbl_ip);
        ip = new JTextField("",10);
        ip.setVisible(true);
        add(ip);
        
        //subnet
        lbl_subnet = new JLabel("Subnet:");
        lbl_subnet.setVisible(true);
        add(lbl_subnet);
        subnet = new JTextField("",10);
        subnet.setVisible(true);
        add(subnet);

        //storage
        lbl_storage = new JLabel("Opslag:");
        lbl_storage.setVisible(true);
        add(lbl_storage);
        storage = new JTextField("",10);
        storage.setVisible(true);
        add(storage);

        //storage
        lbl_ram = new JLabel("Ram:");
        lbl_ram.setVisible(true);
        add(lbl_ram);
        ram = new JTextField("",10);
        ram.setVisible(true);
        add(ram);

        //lbl2
        lbl2 = new JLabel("");
        lbl2.setVisible(true);
        add(lbl2);

        //submit
        submit = new JButton("Aanmaken");
        submit.setVisible(true);
        submit.addActionListener(this);
        add(submit);

        setVisible(true);

    }
    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == back) {
            setVisible(false);

        } else if(e.getSource() == submit) {
            System.out.println("Adding server to" + getProjectName(projectID));
            setVisible(false);

            Connection con = null;
            PreparedStatement p = null;
            ResultSet rs = null;
            String dbhost = "jdbc:mysql://192.168.1.1/application";
            String user = "group4";
            String password = "Qwerty1@";
            String string_alias =  alias.getText();
            String string_price =  price.getText();
            String string_availibility = availibility.getText();
            String string_port  = port.getText();
            String string_storage =  storage.getText();
            String string_ip  = ip.getText();
            String string_subnet = subnet.getText();
            String string_ram = ram.getText();
            int int_server_kind = 2;
            if(server_kind == null) {
                int_server_kind = 2;
            }


            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                con = DriverManager.getConnection(dbhost,user,password);

                String sql = "INSERT INTO servers values(null,\"" + string_alias + "\","  + string_price +"," + string_availibility + "," + string_port + "," + string_storage + "," + int_server_kind + ",\""  + string_ip + "\"" + "," + string_subnet + "," + string_ram + ")";
                p = con.prepareStatement(sql);
                p.executeUpdate();

                sql = "select serverID from servers order by serverID DESC limit 1";
                p = con.prepareStatement(sql);
                rs = p.executeQuery();

                while (rs.next()) {
                    int id = rs.getInt("serverID");
                    this.id = id;
                }

                sql = "INSERT INTO server_Present values(null," + id +",null,null,null,0)";
                p = con.prepareStatement(sql);
                p.executeUpdate();

                sql = "select server_PresentID from server_Present order by server_PresentID DESC limit 1";
                p = con.prepareStatement(sql);
                rs = p.executeQuery();

                while (rs.next()) {
                    int id = rs.getInt("server_PresentID");
                    this.id = id;
                }

                sql = "INSERT INTO project_Has_Servers values(" + projectID + "," + id + ")";
                p = con.prepareStatement(sql);
                p.executeUpdate();


            } catch (Exception ce) {
                System.err.println("error in connection with database");
                ce.printStackTrace();

            }

            try {
                Socket socket = new Socket();
                socket.connect(new InetSocketAddress(string_ip, Integer.getInteger(string_port)), 300);
                MonitoringFrame.updateUptimeServer(id,true);
                System.out.println(" OK");
            } catch (Exception exception) {
                MonitoringFrame.updateUptimeServer(id,false);

            }

        }

    }

    public String[] serverOptions() {
        int amount = 0;
        String name;
        Connection con;
        PreparedStatement p;
        ResultSet rs;
        String dbhost = "jdbc:mysql://192.168.1.1/application";
        String user = "group4";
        String password = "Qwerty1@";
        String[] serverOptions = null;

        try {
            con = DriverManager.getConnection(dbhost, user, password);

            String sql = "select count(name) as amount from server_Kind";
            p = con.prepareStatement(sql);
            rs = p.executeQuery();
            while (rs.next()) {
                amount = rs.getInt("amount");
            }
            serverOptions = new String[amount];

            sql = "select name from server_Kind";
            p = con.prepareStatement(sql);
            rs = p.executeQuery();

            // In array zetten;
            int i = 0;
            while (rs.next()) {
                if(rs.getString("name") != null) {
                    name = rs.getString("name");
                    serverOptions[i] = name;
                    i++;
                }
            }
            return serverOptions;

        } catch (Exception ce) {
            System.err.println("error" + ce);
            ce.printStackTrace();

        }

        return serverOptions;

    }

    public String getProjectName(int i) {
        String name = null;
        Connection con;
        PreparedStatement p;
        ResultSet rs;

        String dbhost = "jdbc:mysql://192.168.1.1/application";
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
                name = name + rs.getString("name");
                return name;
            }


        } catch (Exception ce) {
            System.err.println("error in connection");
            ce.printStackTrace();

        }
        return name;
    }
}
