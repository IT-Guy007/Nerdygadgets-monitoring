package nerdygadgets.Monitoring;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ServerStats extends JFrame implements ActionListener {

    JFrame serverStats = new JFrame();
    int serverPresentID;
    int projectID;
    int interval;
    String interval_time;
    JButton back,five,one,twelve,twentyfour,zeven,thirty,refresh;

    LocalDateTime now = LocalDateTime.now();
    DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    String formattedDate = now.format(myFormatObj);
    String time_kind;


    //Axis

    public ServerStats(int server_PresentID, int projectID, int interval) {
        this.interval = interval;
        set_interval_for_charts(interval);
        this.serverPresentID = server_PresentID;
        this.projectID = projectID;
        Server server = getServerInfo(serverPresentID);
        boolean agent_installed = check_if_agent_present();

        serverStats.setTitle("Monitoring van " + server.getName());
        GridBagLayout gridbag = new GridBagLayout();
        GridBagConstraints layout = new GridBagConstraints();
        serverStats.setLayout(gridbag);
        layout.fill = GridBagConstraints.HORIZONTAL;
        layout.weightx = 1.0;
        layout.weighty = 1.0;
        layout.gridwidth = 1;
        layout.gridheight = 1;
        layout.insets.set(0,0,0,0);
        serverStats.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        serverStats.setSize(1000,800);
        serverStats.getContentPane().setBackground(Color.white);

        //back button
        layout.gridx = 0; layout.gridy = 0; back = new JButton("Sluiten");back.setSize(100,50);back.addActionListener(this);back.setVisible(true);
        serverStats.add(back,layout);
        //five button
        layout.gridx = 1; layout.gridy = 0; five = new JButton("5 minuten");five.setSize(100,50);five.addActionListener(this);five.setVisible(true);
        serverStats.add(five,layout);
        //one button
        layout.gridx = 2; layout.gridy = 0; one = new JButton("1 uur");one.setSize(100,50);one.addActionListener(this);one.setVisible(true);
        serverStats.add(one,layout);
        //twelve button
        layout.gridx = 3; layout.gridy = 0; twelve = new JButton("12 uur");twelve.setSize(100,50);twelve.addActionListener(this);twelve.setVisible(true);
        serverStats.add(twelve,layout);
        //twentyfour button
        layout.gridx = 4; layout.gridy = 0; twentyfour = new JButton("24 uur");twentyfour.setSize(100,50);twentyfour.addActionListener(this);twentyfour.setVisible(true);
        serverStats.add(twentyfour,layout);
        //zeven button
        layout.gridx = 5; layout.gridy = 0; zeven = new JButton("7 dagen");zeven.setSize(100,50);zeven.addActionListener(this);zeven.setVisible(true);
        serverStats.add(zeven,layout);
        //thirty button
        layout.gridx = 6; layout.gridy = 0; thirty = new JButton("30 dagen");thirty.setSize(100,50);thirty.addActionListener(this);thirty.setVisible(true);
        serverStats.add(thirty,layout);
        //Refresh button
        layout.gridx = 7; layout.gridy = 0; refresh = new JButton("Refresh");refresh.setSize(100,50);refresh.addActionListener(this);refresh.setVisible(true);
        serverStats.add(refresh,layout);

        serverStats.setVisible(true);
        serverStats.setResizable(false);

        ////////////Charts////////////

        //--Uptime--
        final XYSeries uptime_series = new XYSeries("Uptime_line");

        Connection con;
        PreparedStatement p;
        ResultSet rs;
        String dbhost = "jdbc:mysql://192.168.1.1/application";
        String user = "group4";
        String password = "Qwerty1@";

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection(dbhost, user, password);
            String sql = sql_String(1,interval);
            p = con.prepareStatement(sql);
            rs = p.executeQuery();
            int i =0;

            if(!rs.next()) {
                uptime_series.add(0,0);
                uptime_series.add(10,0);
            } else {
                while (rs.next()) {
                    uptime_series.add(i, rs.getInt("up"));
                    i++;
                }
            }
        } catch (Exception e) {
            System.out.println(e);
        }

        System.out.println("Drawing uptime chart");
        XYSeriesCollection uptime_data_collection = new XYSeriesCollection(uptime_series);
        JFreeChart uptime_chart = ChartFactory.createXYLineChart("Uptime chart","","Uptime in %",uptime_data_collection, PlotOrientation.VERTICAL,true,true,true); uptime_chart.setTitle("Uptime van " + server.getName());
        XYPlot up_plot = uptime_chart.getXYPlot(); up_plot.setBackgroundPaint(Color.DARK_GRAY);up_plot.setRangeGridlinesVisible(true);up_plot.setRangeGridlinePaint(Color.BLACK);up_plot.setDomainGridlinesVisible(true);up_plot.setDomainGridlinePaint(Color.BLACK); up_plot.setOutlinePaint(Color.BLUE);up_plot.setOutlineStroke(new BasicStroke(2.0f));
        ChartPanel uptime_panel = new ChartPanel(uptime_chart);
        setContentPane(uptime_panel); uptime_panel.setMinimumSize(new Dimension(500,300));
        layout.weighty = 10;layout.gridx = 0; layout.gridy = 1; layout.gridwidth = 4; layout.gridheight = 10;serverStats.add(uptime_panel, layout); layout.gridwidth = 1; layout.gridheight = 1;layout.weightx = 1;


        if(!agent_installed) {
            layout.gridx = 6; layout.gridy = 2; JLabel no_agent  = new JLabel("No agent installed on this server."); no_agent.setVisible(true); serverStats.add(no_agent,layout);
            for(int i = 0;i != 5; i++){
                layout.gridy = i + 10; JLabel spacer = new JLabel(""); spacer.setSize(100,50);spacer.setVisible(true); serverStats.add(spacer, layout);
            }
        } else {
            //--Storage chart--
            final XYSeries storage_available_series = new XYSeries("Beschikbaar opslag");

            final XYSeries storage_total_series = new XYSeries("Totale opslag");
            storage_total_series.add(0,server.getStorage());
            storage_total_series.add(20,server.getStorage());

            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                con = DriverManager.getConnection(dbhost, user, password);
                String sql = sql_String(2,interval);
                p = con.prepareStatement(sql);
                rs = p.executeQuery();
                int i =0;

                if(!rs.next()) {

                } else {
                    while (rs.next()) {
                        storage_available_series.add(i, rs.getInt("storage"));
                        i++;
                    }
                }

            } catch (Exception e) {
                System.out.println(e);
            }
            System.out.println("Drawing storage chart");
            XYSeriesCollection storage_data_collection = new XYSeriesCollection();
            storage_data_collection.addSeries(storage_total_series); storage_data_collection.addSeries(storage_available_series);
            JFreeChart storage_chart = ChartFactory.createXYLineChart("Storage chart","","Storage in GB",storage_data_collection, PlotOrientation.VERTICAL,true,true,true); storage_chart.setTitle("Opslag van " + server.getName());
            XYPlot storage_plot = storage_chart.getXYPlot(); storage_plot.setBackgroundPaint(Color.DARK_GRAY);storage_plot.setRangeGridlinesVisible(true);storage_plot.setRangeGridlinePaint(Color.BLACK);storage_plot.setDomainGridlinesVisible(true);storage_plot.setDomainGridlinePaint(Color.BLACK); storage_plot.setOutlinePaint(Color.BLUE);storage_plot.setOutlineStroke(new BasicStroke(2.0f));
            ChartPanel storage_panel = new ChartPanel(storage_chart);
            setContentPane(storage_panel); storage_panel.setMinimumSize(new Dimension(500,300));
            layout.weighty = 10;layout.gridx = 5; layout.gridy = 1; layout.gridwidth = 4; layout.gridheight = 10;serverStats.add(storage_panel, layout); layout.gridwidth = 1; layout.gridheight = 1;layout.weightx = 1;


            //--CPU--
            final XYSeries cpu_series = new XYSeries("cpu gebruik");

            final XYSeries cpu_series_max = new XYSeries("cpu max gebruik");
            cpu_series_max.add(0,100);
            cpu_series_max.add(10,100);

            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                con = DriverManager.getConnection(dbhost, user, password);
                String sql = sql_String(4,interval);
                p = con.prepareStatement(sql);
                rs = p.executeQuery();
                int i =0;

                if(!rs.next()) {
                    cpu_series.add(0,0);
                    cpu_series.add(10,0);

                } else {
                    while (rs.next()) {
                        cpu_series.add(i, rs.getInt("cpu"));
                        i++;
                    }
                }

            } catch (Exception e) {
                System.out.println(e);
            }

            System.out.println("Drawing CPU chart");
            XYSeriesCollection cpu_collection = new XYSeriesCollection();
            cpu_collection.addSeries(cpu_series);cpu_collection.addSeries(cpu_series_max);
            JFreeChart cpu_chart = ChartFactory.createXYLineChart("CPU chart","","CPU in %",cpu_collection, PlotOrientation.VERTICAL,true,true,true); cpu_chart.setTitle("CPU gebruik van " + server.getName());
            XYPlot cpu_plot = cpu_chart.getXYPlot(); cpu_plot.setBackgroundPaint(Color.DARK_GRAY); cpu_plot.setRangeGridlinesVisible(true);cpu_plot.setRangeGridlinePaint(Color.BLACK);cpu_plot.setDomainGridlinesVisible(true);cpu_plot.setDomainGridlinePaint(Color.BLACK); cpu_plot.setOutlinePaint(Color.BLUE);cpu_plot.setOutlineStroke(new BasicStroke(2.0f));
            ChartPanel cpu_panel = new ChartPanel(cpu_chart);
            setContentPane(cpu_panel); cpu_panel.setMinimumSize(new Dimension(500,300));
            layout.weighty = 10;layout.gridx = 0; layout.gridy = 11; layout.gridwidth = 4; layout.gridheight = 10;serverStats.add(cpu_panel, layout); layout.gridwidth = 1; layout.gridheight = 1;layout.weightx = 1;


            //RAM chart
            final XYSeries ram_available_series = new XYSeries("Beschikbaar ram");

            final XYSeries ram_total_series = new XYSeries("Totale ram");
            ram_total_series.add(0,server.getRam());
            ram_total_series.add(20,server.getRam());

            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                con = DriverManager.getConnection(dbhost, user, password);
                String sql = sql_String(3,interval);
                p = con.prepareStatement(sql);
                rs = p.executeQuery();
                int i =0;

                if(!rs.next()) {
                    ram_available_series.add(0,0);
                    ram_available_series.add(10,0);

                } else {
                    while (rs.next()) {
                        ram_available_series.add(i, rs.getInt("cpu"));
                        i++;
                    }
                }

            } catch (Exception e) {
                System.out.println(e);
            }

            System.out.println("Drawing ram chart");
            XYSeriesCollection ram_collection = new XYSeriesCollection();
            ram_collection.addSeries(ram_available_series);ram_collection.addSeries(ram_total_series);
            JFreeChart ram_chart = ChartFactory.createXYLineChart("RAM chart","","RAM in GB",ram_collection, PlotOrientation.VERTICAL,true,true,true); ram_chart.setTitle("RAM gebruik van " + server.getName());
            XYPlot ram_plot = ram_chart.getXYPlot(); ram_plot.setBackgroundPaint(Color.DARK_GRAY); ram_plot.setRangeGridlinesVisible(true);ram_plot.setRangeGridlinePaint(Color.BLACK);ram_plot.setDomainGridlinesVisible(true);ram_plot.setDomainGridlinePaint(Color.BLACK); ram_plot.setOutlinePaint(Color.BLUE);ram_plot.setOutlineStroke(new BasicStroke(2.0f));
            ChartPanel ram_panel = new ChartPanel(ram_chart);
            setContentPane(ram_panel); ram_panel.setMinimumSize(new Dimension(500,300));
            layout.weighty = 10;layout.gridx = 5; layout.gridy = 11; layout.gridwidth = 4; layout.gridheight = 10;serverStats.add(ram_panel, layout); layout.gridwidth = 1; layout.gridheight = 1;layout.weightx = 1;


        }
        serverStats.setVisible(true); //Main JFrame
    }


    public void set_interval_for_charts(int interval) {

        if(this.interval != interval) {
            this.interval = interval;
        }

        if(interval == 5) {
            //5 minuten
            System.out.println("Setting interval to 5 minutes");
            time_kind = "minuten";
            LocalDateTime unformatted_interval_time = LocalDateTime.now().minusMinutes(5); this.interval_time = unformatted_interval_time.format(myFormatObj);
        } else if(interval == 1) {
            //1 uur
            time_kind = "uren";
            System.out.println("Setting interval to 1 hour");
            LocalDateTime unformatted_interval_time = LocalDateTime.now().minusHours(1); this.interval_time = unformatted_interval_time.format(myFormatObj);
        } else if(interval == 12) {
            //12 uur
            time_kind = "uren";

            System.out.println("Setting interval to 12 hours");
            LocalDateTime unformatted_interval_time = LocalDateTime.now().minusHours(12); this.interval_time = unformatted_interval_time.format(myFormatObj);
        } else if(interval == 24) {
            // 24 uur
            time_kind = "uren";
            LocalDateTime unformatted_interval_time = LocalDateTime.now().minusDays(1); this.interval_time = unformatted_interval_time.format(myFormatObj);
            System.out.println("Setting interval to 24 hours");
        } else if(interval == 7) {
            //7 dagen
            time_kind = "dagen";
            LocalDateTime unformatted_interval_time = LocalDateTime.now().minusDays(7); this.interval_time = unformatted_interval_time.format(myFormatObj);
            System.out.println("Setting interval to 7 days");
        } else if(interval == 30) {
            //30 dagen
            time_kind = "dagen";
            LocalDateTime unformatted_interval_time = LocalDateTime.now().minusDays(30); this.interval_time = unformatted_interval_time.format(myFormatObj);
            System.out.println("Setting interval to 30 days");
        } else {
            System.out.println("Time format not found");
        }
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

            String sql = "select s.serverID as serverID,s.ram as ram,s.name as name,s.price as price,s.availability as availability,s.port as port,s.subnet as subnet,s.storage_Total as storage,sk.name as serverkind,s.ipaddress as ipaddress, sp.up as up, AVG(uc.up) as actual from servers as s\n" +
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
    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == back) {
            serverStats.dispose();
        } else if(e.getSource() == five) {
            set_interval_for_charts(5);serverStats.dispose(); new ServerStats(serverPresentID,projectID,5);
        } else if(e.getSource() == one) {
            set_interval_for_charts(1);serverStats.dispose(); new ServerStats(serverPresentID,projectID,1);
        }else if(e.getSource() == twelve) {
            set_interval_for_charts(12);serverStats.dispose(); new ServerStats(serverPresentID,projectID,12);
        }else if(e.getSource() == twentyfour) {
            set_interval_for_charts(24);serverStats.dispose(); new ServerStats(serverPresentID,projectID,24);
        } else if(e.getSource() == zeven) {
            set_interval_for_charts(7);serverStats.dispose(); new ServerStats(serverPresentID,projectID,7);
        } else if(e.getSource() == thirty) {
            set_interval_for_charts(30);serverStats.dispose(); new ServerStats(serverPresentID,projectID,30);
        } else if(e.getSource() == refresh) {serverStats.repaint();
        }
        System.out.println("Repainting");
    }

    public boolean check_if_agent_present() {
        Connection con;
        PreparedStatement p;
        ResultSet rs;
        String dbhost = "jdbc:mysql://192.168.1.1/application";
        String user = "group4";
        String password = "Qwerty1@";

        double cpu_usage = 0;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection(dbhost, user, password);
            String sql = "select cpuID from cpu_History where server_PresentID = " + serverPresentID;
            p = con.prepareStatement(sql);
            rs = p.executeQuery();

            if(!rs.next()) {
                return false;
            } else {
                return true;
            }
        } catch (Exception e) {
            System.out.println(e);
        }


        return false;
    }

    public String sql_String(int datatype, int interval) {
        String sql = null;
        String column ="";
        String column_table ="";
        //Datatypes: 1 -> uptime, 2 -> storage, 3 -> ram, 4 -> cpu
        if(datatype == 1) {
            column = "avg(up) * 100 as up, dateTime";
            column_table = "uptime_Check";
        } else if (datatype == 2) {
            column = "avg(storage_Usage) * 100 as storage,dateTime";
            column_table = "storage_History";
        } else if (datatype == 3) {
            column = "avg(ram_Usage) * 100 as ram, dateTime";
            column_table = "ram_History";
        } else if (datatype == 4) {
            column = "avg(cpu_Usage) * 100 as cpu, dateTime";
            column_table = "cpu_History";
        }

        if(this.interval != interval) {
            this.interval = interval;
        }

        if(interval == 5) {
            //5 minuten
            sql = "select " + column + ", dateTime \n" +
                    "from " + column_table + "\n" +
                    "where server_PresentID = " + serverPresentID + " and dateTime between '" + interval_time + "' AND '" + formattedDate  + "'\n" +
                    "group by dateTime";
        } else if(interval == 1) {
            //1 uur
            sql = "select " + column + ", dateTime \n" +
                    "from " + column_table + "\n" +
                    "where server_PresentID = " + serverPresentID + " and dateTime between '" + interval_time + "' AND '" + formattedDate + "'\n" +
                    "group by dateTime";
        } else if(interval == 12) {
            //12 uur
            sql = "select " + column + ", dateTime \n" +
                    "from " + column_table + "\n" +
                    "where server_PresentID = " + serverPresentID + " and dateTime between '" + interval_time + "' AND '" + formattedDate + "'\n" +
                    "group by dateTime";
        } else if(interval == 24) {
            //24 uur
            sql = "select " + column + ", dateTime \n" +
                    "from " + column_table + "\n" +
                    "where server_PresentID = " + serverPresentID + " and dateTime between '" + interval_time + "' AND '" + formattedDate + "'\n" +
                    "group by dateTime";
        } else if(interval == 7) {
            //7 dagen
            sql = "select " + column + ", dateTime \n" +
                    "from " + column_table + "\n" +
                    "where server_PresentID = " + serverPresentID + " and dateTime between '" + interval_time + "' AND '" + formattedDate + "'\n" +
                    "group by dateTime";
        } else if(interval == 30) {
            //30 dagen
            sql = "select " + column + ", dateTime \n" +
                    "from " + column_table + "\n" +
                    "where server_PresentID = " + serverPresentID + " and dateTime between '" + interval_time + "' AND '" + formattedDate + "'\n" +
                    "group by dateTime";
        }

        return sql;

    }

}
