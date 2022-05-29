package nerdygadgets.Monitoring;

import com.mysql.cj.jdbc.exceptions.CommunicationsException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class MonitoringFrame extends JFrame implements ActionListener {
    int projectID = 0;
    String projectName = null;

    JButton back,five,one,twelve,twentyfour,zeven,thirty,refresh,add;

    public MonitoringFrame(int projectID) {
        ArrayList<Integer> servers = servers_in_project(projectID);
        int amount_of_servers = servers.size();
        System.out.println("Found: " + amount_of_servers + " servers in project.");
        String projectName = getProjectName(projectID);
        this.projectID = projectID;
        this.projectName = projectName;
        setTitle("Monitoring van " + projectName);
        GridBagLayout gridbag = new GridBagLayout();
        setLayout(gridbag);
        GridBagConstraints layout = new GridBagConstraints();
        layout.fill = GridBagConstraints.HORIZONTAL;

        layout.weightx = 1.0;
        layout.weighty = 1.0;
        layout.gridwidth = 1;
        layout.gridheight = 1;
        layout.insets.set(0, 0, 0, 0);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1050,500);

        //Back button
        layout.gridx = 0; layout.gridy = 0; back = new JButton("Terug"); back.setSize(100,50);back.addActionListener(this);back.setVisible(true); add(back,layout);
        layout.gridx = 12; layout.gridy = 0; refresh = new JButton("Refresh"); refresh.setSize(100,50);refresh.addActionListener(this);refresh.setVisible(true); add(refresh,layout);

        layout.fill = GridBagConstraints.HORIZONTAL;
        layout.gridwidth = 1;

        if(servers.size() == 0) {
            System.out.println("No servers in project found, showing different screen.");
            JLabel noServers = new JLabel("Geen servers gevonden in het project");
            layout.gridx = 4;
            layout.gridy = 1;
            layout.gridwidth = 3;
            noServers.setVisible(true);
            add(noServers,layout);
            for(int i = 0; i != 6; i++) {
                JLabel spacer = new JLabel();
                layout.gridy = (i + 1);
                add(spacer,layout);
            }
        } else {
            //Columns
            layout.gridwidth = 1;
            layout.gridy = 1;

            //ServerID
            layout.gridx = 0; JLabel serverid = new JLabel("ServerID"); serverid.setVisible(true); add(serverid,layout);
            //Servername
            layout.gridx = 1; JLabel servernaam = new JLabel("Servernaam"); servernaam.setVisible(true); add(servernaam,layout);
            //Server is up
            layout.gridx = 2; JLabel up = new JLabel("Server is up"); up.setVisible(true); layout.gridwidth = 2;add(up,layout); layout.gridwidth = 1;
            //Server price
            layout.gridx = 4; JLabel prijs = new JLabel("Prijs"); prijs.setVisible(true); add(prijs,layout);
            //Server availability
            layout.gridx =5; JLabel beschikbaarheid = new JLabel("Beschikbaarheid"); beschikbaarheid.setVisible(true); add(beschikbaarheid,layout);
            //Serverport
            layout.gridx = 6; JLabel serverport = new JLabel("Serverport"); serverport.setVisible(true); add(serverport,layout);
            //Server kind
            layout.gridx = 7; JLabel serversoort = new JLabel("Serversoort"); serversoort.setVisible(true); add(serversoort,layout);
            //Server ipadress
            layout.gridx = 8; JLabel ipadress = new JLabel("Ipaddress"); ipadress.setVisible(true); layout.gridwidth = 2;add(ipadress,layout); layout.gridwidth = 1;
            //Server storage
            layout.gridx = 10; JLabel storage = new JLabel("Opslag"); storage.setVisible(true); add(storage,layout);


            //Code that generates the serverlist
            for (int i = 0; i != servers.size(); i++) {
                int serverID = servers.get(i);
                Server server = getServerInfo(serverID);
                //Servername
                layout.gridx = 0; layout.gridy = (i + 2); JLabel lbl_ID = new JLabel("    " + (server.serverID));lbl_ID.setVisible(true);add(lbl_ID, layout);
                layout.gridx = 1; layout.gridy = (i + 2); JLabel lbl_servername = new JLabel(server.name);lbl_servername.setVisible(true);add(lbl_servername, layout);
                layout.gridx = 2; layout.gridy = (i + 2); JLabel lbl_up = new JLabel(Boolean.toString(server.up));lbl_up.setVisible(true);if(server.up){lbl_up.setForeground(Color.green);} else {lbl_up.setForeground(Color.red);}layout.gridwidth = 2;add(lbl_up, layout); layout.gridwidth = 1;
                layout.gridx = 4; layout.gridy = (i + 2); JLabel lbl_price = new JLabel(Integer.toString(server.price));lbl_price.setVisible(true);add(lbl_price, layout);
                layout.gridx = 5; layout.gridy = (i + 2); JLabel lbl_availability = new JLabel("      " + (server.availability));lbl_availability.setVisible(true);add(lbl_availability, layout);
                layout.gridx = 6; layout.gridy = (i + 2); JLabel lbl_serverport = new JLabel(Integer.toString(server.port));lbl_serverport.setVisible(true);add(lbl_serverport, layout);
                layout.gridx = 7; layout.gridy = (i + 2); JLabel lbl_server_kind = new JLabel(server.server_kind2);lbl_server_kind.setVisible(true);add(lbl_server_kind, layout);
                layout.gridx = 8; layout.gridy = (i + 2); JLabel lbl_ip = new JLabel(server.ipadress);lbl_ip.setVisible(true);layout.gridwidth = 2;add(lbl_ip, layout); layout.gridwidth = 1;
                layout.gridx = 10; layout.gridy = (i + 2); JLabel lbl_storage = new JLabel(Integer.toString(server.storage));lbl_storage.setVisible(true);add(lbl_storage, layout);
                layout.gridx = 11; layout.gridy = (i + 2); JButton analytics = new JButton("Analytics"); analytics.setVisible(true); add(analytics,layout); analytics.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {new ServerStats(serverID,projectID);}});
                layout.gridx = 12; layout.gridy = (i + 2); JButton delete = new JButton("Delete"); delete.setVisible(true); add(delete,layout); delete.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {deleteServerOfProject(serverID);dispose(); new MonitoringFrame(projectID);}});
            }

            if(amount_of_servers <= 3) {
                for(int i = 0;i != 3; i++){
                    JLabel servername = new JLabel();
                    layout.gridx = i;
                    layout.gridy = (i + 2 + amount_of_servers);
                    servername.setVisible(true);
                    add(servername, layout);
                }
            }
        }
        setVisible(true);

    }

    public ArrayList<Integer> servers_in_project(int projectID) {
        System.out.println("Checking for servers in project");
        ArrayList<Integer> servers_in_project = new ArrayList<>();

        try {
            Connection con = null;
            PreparedStatement p = null;
            ResultSet rs = null;
            String dbhost = "jdbc:mysql://192.168.1.103/application";
            String user = "group4";
            String password = "Qwerty1@";
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection(dbhost, user, password);

            String sql = "select server_PresentID from project_Has_Servers where projectID = " + projectID;
            p = con.prepareStatement(sql);
            rs = p.executeQuery();

            while (rs.next()) {
                servers_in_project.add(rs.getInt("server_PresentID"));
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
            System.out.println("Showing add server pane");
            new ServerToevoegen(projectID);
        } else if(e.getSource() == refresh) {
            System.out.println("Refresh Monitoring pane");
            dispose();
            new MonitoringFrame(projectID);
            updateUptime();
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

                Server server = new Server(serverID,name,price,availability,serverport,storage,server_kind,ipaddress, up);
                return server;
            }
        } catch (Exception ce) {
            System.err.println("error");
            System.out.println(ce);
            System.out.println();
        }

        return null;
    }

    public void deleteServerOfProject(int server_PresentID) {
        Connection con = null;
        PreparedStatement p = null;
        String dbhost = "jdbc:mysql://192.168.1.103/application";
        String user = "group4";
        String password = "Qwerty1@";

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection(dbhost, user, password);

            // SQL command data stored in String datatype
            String sql = "delete from project_Has_Servers where server_PresentID = " + server_PresentID;
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
        }
    }

    public static void updateUptimeServer(int server_PresentID, boolean up) {
        Connection con = null;
        PreparedStatement p = null;
        ResultSet rs = null;
        String sql = null;

        String dbhost = "jdbc:mysql://192.168.1.103/application";
        String user = "group4";
        String password = "Qwerty1@";

        if (up) {
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                con = DriverManager.getConnection(dbhost, user, password);

                sql = "Select up from server_Present where server_PresentID = " + server_PresentID;
                p = con.prepareStatement(sql);
                rs = p.executeQuery();

                while(rs.next()) {
                    if(rs.getBoolean("up")) {
                        sql = "update server_Present set up = 1 where server_PresentID = " + server_PresentID;
                        p = con.prepareStatement(sql);
                        p.executeUpdate();
                    } else {
                        LocalDateTime now = LocalDateTime.now();
                        DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                        String formattedDate = now.format(myFormatObj);
                        sql = "update server_Present set up = 1, up_Since = \" "+ formattedDate + "\" where server_PresentID = " + server_PresentID ;
                        p = con.prepareStatement(sql);
                        p.executeUpdate();
                    }
                }

            } catch(Exception exception) {
                System.out.println(exception);
            }
        } else {
            try {
                sql = "update server_Present set up = 0, up_Since = null where server_PresentID = " + server_PresentID;
                p = con.prepareStatement(sql);
                p.executeUpdate();
            } catch(Exception e) {
            }
        }
    }

    public static void updateUptime() {
        System.out.println("Updating database");
        //Get projects
        ArrayList<Project> projects = new ArrayList<Project>();

        Connection con = null;
        PreparedStatement p = null;
        ResultSet rs = null;
        String dbhost = "jdbc:mysql://192.168.1.103/application";
        String user = "group4";
        String password = "Qwerty1@";

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection(dbhost, user, password);
            String sql = "select * from project";

            p = con.prepareStatement(sql);
            rs = p.executeQuery();

            while(rs.next()) {
                int projectID = rs.getInt("ProjectID");
                String name = rs.getString("name");
                int availability = rs.getInt("wanted_Availability");

                Project project = new Project(projectID,name,availability);
                projects.add(project);
            }

        } catch (Exception e) {
            System.out.println(e);
            System.out.println();
        }

        for(int i = 0; i < projects.size(); i++) {
            Project project = projects.get(i);
            ArrayList<Server> servers = new ArrayList();

            try {
                con = null;
                p = null;
                rs = null;

                Class.forName("com.mysql.cj.jdbc.Driver");
                con = DriverManager.getConnection(dbhost, user, password);
                String sql = "select sP.server_PresentID as serverID, s.port as port, s.ipaddress as ip from servers as s\n" +
                        "join server_Present sP on s.serverID = sP.serverID\n" +
                        "join project_Has_Servers pHS on sP.server_PresentID = pHS.server_PresentID\n" +
                        "where projectID = " + project.ProjectID;

                p = con.prepareStatement(sql);
                rs = p.executeQuery();

                while (rs.next()) {
                    int serverID = rs.getInt("serverID");
                    String ip = rs.getString("ip");
                    int port = rs.getInt("port");
                    Server server = new Server(serverID, ip, port);
                    servers.add(server);
                }


            } catch (Exception e) {
                System.out.println(e);
            }

            for (int j = 0; j < servers.size(); j++) {
                Server server = servers.get(j);
                int serverID = server.serverID;
                String ipadress = server.ipadress;
                int port = server.port;
                boolean isup = false;
                if (ipadress != null && ipadress != "null" && ipadress != "NULL" && ipadress != "") {
                    System.out.print("Checking if " + ipadress + " is online");
                    try  {
                        Socket socket = new Socket();
                        socket.connect(new InetSocketAddress(ipadress, port), 300);
                        isup = true;
                        System.out.println(" OK");
                    } catch (Exception e) {
                        System.out.println(" False");
                        isup = false;
                    }
                    updateUptimeServer(serverID,isup);
                }

            }

            System.out.println("Update completed");
            System.out.println();
        }


    }
}