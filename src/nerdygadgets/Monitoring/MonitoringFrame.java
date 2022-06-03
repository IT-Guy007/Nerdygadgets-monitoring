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


@SuppressWarnings("resource")
public class MonitoringFrame extends JFrame implements ActionListener {
    int projectID;
    String projectName;

    JButton back,refresh,add;

    public MonitoringFrame(int projectID) {
        ArrayList<Integer> servers = servers_in_project(projectID);
        int amount_of_servers = servers.size();
        System.out.println("Found: " + amount_of_servers + " servers in project.");
        Project project = getProjectDetails(projectID);
        String projectName = project.getName();
        this.projectID = projectID;
        this.projectName = projectName;
        setTitle("Monitoring van " + projectName);
        GridBagLayout gridbag = new GridBagLayout();
        setLayout(gridbag);
        GridBagConstraints layout = new GridBagConstraints();
        layout.fill = GridBagConstraints.HORIZONTAL;
        getContentPane().setBackground(Color.white);
        double project_actual_availability = 0;

        layout.weightx = 1.0;
        layout.weighty = 1.0;
        layout.gridwidth = 1;
        layout.gridheight = 1;
        layout.insets.set(0, 0, 0, 0);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1600,600);

        //Back button
        layout.gridx = 0; layout.gridy = 0; back = new JButton("Terug"); back.setSize(100,50);back.addActionListener(this);back.setVisible(true); add(back,layout);
        layout.gridx = 3; layout.gridy = 0; JLabel wanted_availability = new JLabel("Gewenste beschikbaarheid: " + project.getWanted_availability() + "%"); wanted_availability.setSize(100,50);wanted_availability.setVisible(true); layout.gridwidth = 4; add(wanted_availability,layout); layout.gridwidth = 1;
        layout.gridx = 6; layout.gridy = 0; JLabel actual_availability = new JLabel(); actual_availability.setSize(100,50);actual_availability.setVisible(true);
        layout.gridx = 14; layout.gridy = 0; refresh = new JButton("Refresh"); refresh.setSize(100,50);refresh.addActionListener(this);refresh.setVisible(true); add(refresh,layout);
        layout.gridx = 15; layout.gridy = 0; add = new JButton("Server toevoegen"); add.setSize(100,50);add.addActionListener(this);add.setVisible(true); add(add,layout);

        layout.fill = GridBagConstraints.HORIZONTAL;
        layout.gridwidth = 1;

        if(servers.size() == 0) {
            System.out.println("No servers in project found, showing different screen.");
            JLabel noServers = new JLabel("Geen servers gevonden in het project");
            layout.gridx = 7;
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
            layout.gridy = 1;

            //ServerID
            layout.gridx = 0; JLabel serverid = new JLabel("ServerID"); serverid.setVisible(true); add(serverid,layout);
            //Servername
            layout.gridx = 1; JLabel servernaam = new JLabel("Servernaam"); servernaam.setVisible(true); layout.gridwidth = 2;add(servernaam,layout); layout.gridwidth = 1;
            //Server is up
            layout.gridx = 3; JLabel up = new JLabel("Server is up   "); up.setVisible(true); add(up,layout);
            //Server availability
            layout.gridx = 4; JLabel beschikbaarheid = new JLabel("Gewenste   "); beschikbaarheid.setVisible(true); add(beschikbaarheid,layout);
            //Server actual availability
            layout.gridx = 5; JLabel eig_beschikbaarheid = new JLabel("Actuele"); eig_beschikbaarheid.setVisible(true); add(eig_beschikbaarheid,layout);
            //Server price
            layout.gridx = 6; JLabel prijs = new JLabel("Prijs"); prijs.setVisible(true); add(prijs,layout);
            //Server kind
            layout.gridx = 7; JLabel serversoort = new JLabel("Serversoort"); serversoort.setVisible(true); add(serversoort,layout);
            //Server ipadress
            layout.gridx = 8; JLabel ipadress = new JLabel("Ipadres"); ipadress.setVisible(true); layout.gridwidth = 2;add(ipadress,layout); layout.gridwidth = 1;
            //Serverport
            layout.gridx = 10; JLabel serverport = new JLabel("Serverport"); serverport.setVisible(true); add(serverport,layout);
            //Subnet
            layout.gridx = 11;JLabel subnet = new JLabel("Subnet"); subnet.setVisible(true); add(subnet,layout);
            //Server storage
            layout.gridx = 12; JLabel storage = new JLabel("Opslag"); storage.setVisible(true); add(storage,layout);
            //RAM storage
            layout.gridx = 13; JLabel ram = new JLabel("  Ram"); ram.setVisible(true); add(ram,layout);


            //Code that generates the serverlist
            JLabel lbl_up;
            for (int i = 0; i != servers.size(); i++) {
                int serverID = servers.get(i);
                Server server = getServerInfo(serverID);
                //ServerID
                layout.gridx = 0; layout.gridy = (i + 2); JLabel lbl_ID = new JLabel("    " + (server.getServerID()));lbl_ID.setVisible(true);add(lbl_ID, layout);
                //ServerName
                layout.gridx = 1; layout.gridy = (i + 2); JLabel lbl_servername = new JLabel(server.getName());lbl_servername.setVisible(true);layout.gridwidth = 2; add(lbl_servername, layout); layout.gridwidth = 1;
                //Is up
                layout.gridx = 3; layout.gridy = (i + 2); if(server.isUp()) {lbl_up = new JLabel("   True");} else {lbl_up = new JLabel("   False");} lbl_up.setVisible(true);if(server.isUp()){lbl_up.setForeground(Color.green);} else {lbl_up.setForeground(Color.red);}add(lbl_up, layout);
                //Availability
                layout.gridx = 4; layout.gridy = (i + 2); JLabel lbl_availability = new JLabel((server.getAvailability())+ "%");lbl_availability.setVisible(true);add(lbl_availability, layout);
                //Actual  Availability
                layout.gridx = 5; layout.gridy = (i + 2); JLabel lbl_eig_availability = new JLabel((String.format("%.2f",(server.getActual_availability()) * 100) + "%"));lbl_eig_availability.setVisible(true);if((server.getAvailability() * 100) >= server.getAvailability()){lbl_eig_availability.setForeground(Color.green);} else {lbl_eig_availability.setForeground(Color.red);}add(lbl_eig_availability, layout);
                //Price
                layout.gridx = 6; layout.gridy = (i + 2); JLabel lbl_price = new JLabel("€" + (server.getPrice()));lbl_price.setVisible(true);add(lbl_price, layout);
                //Serverkind
                layout.gridx = 7; layout.gridy = (i + 2); JLabel lbl_server_kind = new JLabel(server.getServer_kind2());lbl_server_kind.setVisible(true);add(lbl_server_kind, layout);
                //Ipaddress
                layout.gridx = 8; layout.gridy = (i + 2); JLabel lbl_ip = new JLabel(server.getIpadress());lbl_ip.setVisible(true);layout.gridwidth = 2;add(lbl_ip, layout); layout.gridwidth = 1;
                //Port
                layout.gridx = 10; layout.gridy = (i + 2); JLabel lbl_serverport = new JLabel(Integer.toString(server.getPort()));lbl_serverport.setVisible(true);add(lbl_serverport, layout);
                //subnet
                layout.gridx = 11; layout.gridy = (i + 2); JLabel lbl_subnet = new JLabel(Integer.toString(server.getSubnet()));lbl_subnet.setVisible(true);add(lbl_subnet, layout);
                //Storage
                layout.gridx = 12; layout.gridy = (i + 2); JLabel lbl_storage = new JLabel(server.getStorage() + "GB");lbl_storage.setVisible(true);add(lbl_storage, layout);
                //ram
                layout.gridx = 13; layout.gridy = (i + 2); JLabel lbl_ram = new JLabel("  " + server.getRam() + "GB");lbl_ram.setVisible(true);add(lbl_ram, layout);
                //Analytics
                layout.gridx = 14; layout.gridy = (i + 2); JButton analytics = new JButton("Analytics"); analytics.setVisible(true); add(analytics,layout); analytics.addActionListener(e -> new ServerStats(serverID,projectID,24));
                // Delete
                layout.gridx = 15
                ; layout.gridy = (i + 2); JButton delete = new JButton("Delete"); delete.setVisible(true); add(delete,layout); delete.addActionListener(e -> {deleteServerOfProject(serverID);dispose(); new MonitoringFrame(projectID);});

                project_actual_availability = project_actual_availability + server.getActual_availability();
            }
            layout.gridy = 0;layout.gridx = 8;project_actual_availability = (project_actual_availability / amount_of_servers) * 100; layout.gridwidth = 4; actual_availability.setText("Actuele beschikbaarheid project: " + (String.format("%.2f",(project_actual_availability))) + "%");add(actual_availability,layout); layout.gridwidth = 1;
            if(amount_of_servers <= 5) {
                for(int i = 0;i != 5; i++){
                    JLabel servername = new JLabel();
                    layout.gridx = i;
                    layout.gridy = (i + 2 + amount_of_servers);
                    servername.setVisible(true);
                    add(servername, layout);
                }
            }
        }
        setResizable(true);
        setVisible(true);
    }

    public ArrayList<Integer> servers_in_project(int projectID) {
        System.out.println("Checking for servers in project");
        ArrayList<Integer> servers_in_project = new ArrayList<>();

        try {
            Connection con;
            PreparedStatement p;
            ResultSet rs;
            String dbhost = "jdbc:mysql://192.168.1.1/application";
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
            new CreateServer(projectID);
        } else if(e.getSource() == refresh) {
            System.out.println("Refresh Monitoring pane");
            dispose();
            new MonitoringFrame(projectID);
            updateUptime();
        }

    }

    public Project getProjectDetails(int projectID) {
        String name;
        double wanted_availability;
        Connection con;
        PreparedStatement p;
        ResultSet rs;

        String dbhost = "jdbc:mysql://192.168.1.1/application";
        String user = "group4";
        String password = "Qwerty1@";

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection(dbhost, user, password);

            String sql = "select * from project where projectID = " + projectID;
            p = con.prepareStatement(sql);
            rs = p.executeQuery();

            // In array zetten;
            while (rs.next()) {
                name = rs.getString("name");
                wanted_availability = rs.getDouble("wanted_availability");
                return new Project(projectID,name,wanted_availability);
            }


        } catch (Exception ce) {
            System.err.println("error in connection");
            ce.printStackTrace();

        }

        return null;
    }

    public Server getServerInfo(int serverPresentID) {

        Connection con;
        PreparedStatement p;
        ResultSet rs;

        int serverID;
        String name;
        int price;
        double availability;
        int serverport;
        int storage ;
        String server_kind;
        String ipaddress;
        boolean up;
        double actual;
        int subnet;
        int ram;

        String dbhost = "jdbc:mysql://192.168.1.1/application";
        String user = "group4";
        String password = "Qwerty1@";

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection(dbhost, user, password);

            String sql = "select s.serverID as serverID,s.name as name,s.ram as ram,s.price as price,s.availability as availability,s.port as port,s.subnet as subnet,s.storage_Total as storage,sk.name as serverkind,s.ipaddress as ipaddress, sp.up as up, AVG(uc.up) as actual from servers as s\n" +
                    "join server_Present sp on s.serverID = sp.serverID\n" +
                    "join server_Kind sk on sk.server_KindID = s.server_KindID\n" +
                    "join uptime_Check uc on sp.server_PresentID = uc.server_PresentID\n" +
                    "where sp.server_PresentID = " + serverPresentID;
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
                actual = rs.getDouble("actual");
                subnet = rs.getInt("subnet");
                ram = rs.getInt("ram");

                return new Server(serverID,name,price,availability,serverport,storage,server_kind,ipaddress,up,actual,subnet,ram);
            }
        } catch (Exception ce) {
            System.err.println("error");
            System.out.println(ce);
            System.out.println();
        }

        return null;
    }

    public void deleteServerOfProject(int server_PresentID) {
        Connection con;
        PreparedStatement p;
        String dbhost = "jdbc:mysql://192.168.1.1/application";
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

            } catch (ClassNotFoundException ignored) {
        }
    }

    public static void updateUptimeServer(int server_PresentID, boolean up) {
        Connection con = null;
        PreparedStatement p;
        ResultSet rs;
        String sql;

        String dbhost = "jdbc:mysql://192.168.1.1/application";
        String user = "group4";
        String password = "Qwerty1@";
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedDate = now.format(myFormatObj);


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
                        sql = "update server_Present set up = 1, up_Since = \" "+ formattedDate + "\" where server_PresentID = " + server_PresentID ;
                        p = con.prepareStatement(sql);
                        p.executeUpdate();
                    }
                }

                sql = "INSERT INTO uptime_Check values(null, " + server_PresentID + "," + up + "," + " \"" + formattedDate + "\" )";
                p = con.prepareStatement(sql);
                p.executeUpdate();


            } catch(Exception exception) {
                System.out.println(exception);
            }
        } else {
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                con = DriverManager.getConnection(dbhost, user, password);
                sql = "INSERT INTO uptime_Check values(null, " + server_PresentID + "," + up + "," + " \"" + formattedDate + "\" )";
                p = con.prepareStatement(sql);
                p.executeUpdate();

                sql = "update server_Present set up = 0, up_Since = null where server_PresentID = " + server_PresentID;
                p = con.prepareStatement(sql);
                p.executeUpdate();

            } catch(Exception exception) {
                System.out.println(exception);
                exception.printStackTrace();
            }
        }
    }

    public static void updateUptime() {
        System.out.println("Updating database");
        //Get projects
        ArrayList<Project> projects = new ArrayList<Project>();

        Connection con;
        PreparedStatement p;
        ResultSet rs;
        String dbhost = "jdbc:mysql://192.168.1.1/application";
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

        for (Project project : projects) {
            ArrayList<Server> servers = new ArrayList();

            try {

                Class.forName("com.mysql.cj.jdbc.Driver");
                con = DriverManager.getConnection(dbhost, user, password);
                String sql = "select sP.server_PresentID as serverID, s.port as port, s.ipaddress as ip from servers as s\n" +
                        "join server_Present sP on s.serverID = sP.serverID\n" +
                        "join project_Has_Servers pHS on sP.server_PresentID = pHS.server_PresentID\n" +
                        "where projectID = " + project.getProjectID();

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
                int serverID = server.getServerID();
                String ipadress = server.getIpadress();
                int port = server.getPort();
                boolean isup;
                if (ipadress != null && !ipadress.equals("null") && !ipadress.equals("NULL") && !ipadress.equals("")) {
                    System.out.print("Checking if " + ipadress + " is online");
                    try {
                        Socket socket = new Socket();
                        socket.connect(new InetSocketAddress(ipadress, port), 300);
                        isup = true;
                        System.out.println(" OK");
                    } catch (Exception e) {
                        System.out.println(" False");
                        isup = false;
                    }
                    updateUptimeServer(serverID, isup);
                }

            }

        }
        System.out.println("Update completed");
        System.out.println();

    }
}