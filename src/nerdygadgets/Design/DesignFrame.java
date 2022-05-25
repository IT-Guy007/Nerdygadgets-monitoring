package nerdygadgets.Design;


import com.google.gson.*;
import nerdygadgets.Design.components.*;
import nerdygadgets.MainFrame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import java.util.Scanner;
import java.sql.*;


public class DesignFrame extends JFrame implements ActionListener {
    private JButton JBopslaan,JBnieuw_ontwerp,JBbestand_openen,JBoptimaliseren,JBserveropties_wijzigen, JBvolscherm, back;
    private Designpanel designpanel;

    private Firewall firewall;
    private ArrayList<WebServer> webServer = new ArrayList<WebServer>();
    private ArrayList<DatabaseServer> databaseServer = new ArrayList<DatabaseServer>();

    private int maxServerBacktracking;
    private int[] WSAantalPerSoort = {};
    private int[] DSAantalPerSoort = {};
    private int WSAantalTotaal;
    private int DSAantalTotaal;
    private double[] WSAvaliablityArray = {};
    private double[] DSAvaliablityArray  = {};
    private double[] WSPrijsPerSoort = {};
    private double[] DSPrijsPerSoort = {};
    private double gewensteBeschikbaarheid;
    private double minimaleKosten;
    private int ServerCount;
    private int maxAantalServers;
    private int[] WSgeoptimaliseerde = {};
    private int[] DSgeoptimaliseerde = {};
    ServerLists list;
    private boolean isVolscherm = false;
    Dimension schermgrootte = Toolkit.getDefaultToolkit().getScreenSize();
    int schermhoogte = schermgrootte.height;
    int schermbreedte = schermgrootte.width;
    String save;


    public DesignFrame(String save) {
        this.save = save;
        list = new ServerLists();

        setTitle("Nerdygadgets Monitoring Applicatie");

        setLayout(new FlowLayout());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(schermbreedte/30*26,schermhoogte/30*26); //Maakt de groote van de gui de helft van de schermgrootte

        back = create_button(back,"back");
        add(back);
        JBnieuw_ontwerp = create_button(JBnieuw_ontwerp,"nieuw-ontwerp-button");
        add(JBnieuw_ontwerp);
        JBopslaan = create_button(JBopslaan, "Opslaan");
        add(JBopslaan);
        JBoptimaliseren = create_button(JBoptimaliseren, "Optimaliseren");
        add(JBoptimaliseren);
        JBserveropties_wijzigen = create_button(JBserveropties_wijzigen, "Serveropties-wijzigen");
        add(JBserveropties_wijzigen);
        JBvolscherm = create_button(JBvolscherm, "enlargebutton");
        add(JBvolscherm);

        designpanel = new Designpanel(this);
        add(designpanel);

        Firewall ServerOptie8 = new Firewall( "pfSense", 4000, 99.998);
        ServerOptie8.setBounds(schermbreedte/2-200,schermhoogte/2-220,125,125);
        designpanel.add(ServerOptie8);
        designpanel.addArrayList(ServerOptie8);
        firewall = ServerOptie8;

        int yhoogte = 10;
        for (WebServer webservertje : webServer){
            webservertje.getPrijs();
            Serveroptie optie1 = new Serveroptie(designpanel,webservertje.getNaam(),webservertje.getBeschikbaarheid(),webservertje.getPrijs(),"webserver");
            optie1.setBounds(10, yhoogte, 121, 61);
            designpanel.add(optie1);
            designpanel.repaint();
            yhoogte = yhoogte + 71;
        }
        for (DatabaseServer webservertje : databaseServer){
            webservertje.getPrijs();
            Serveroptie optie1 = new Serveroptie(designpanel,webservertje.getNaam(),webservertje.getBeschikbaarheid(),webservertje.getPrijs(),"databaseserver");
            optie1.setBounds(10, yhoogte, 121, 61);
            designpanel.add(optie1);
            designpanel.repaint();
            yhoogte = yhoogte + 71;
        }
        setVisible(true);
        setResizable(false);
        open();
    }
    public ImageIcon scaleImage(ImageIcon icon, int w, int h) {
        // Een manier om afbeeldingen te veranderen van grootte, maar niet optimaal omdat je kwaliteit verliest.
        int nw = icon.getIconWidth();
        int nh = icon.getIconHeight();

        if(nh > h) {
            nh = h;
            nw = (icon.getIconWidth() * nh) / icon.getIconHeight();
        }
        return new ImageIcon(icon.getImage().getScaledInstance(nw, nh, Image.SCALE_DEFAULT));
    }

    public void Optimaliseer(){

        for (WebServer WS: webServer){
            WSAvaliablityArray = voegDoubleToe(WSAvaliablityArray, WS.getBeschikbaarheid()/100);
            WSPrijsPerSoort = voegDoubleToe(WSPrijsPerSoort, WS.getPrijs());
            WSAantalPerSoort = voegIntToe(WSAantalPerSoort,0);
            WSAantalTotaal++;
        }

        for (DatabaseServer DS: databaseServer){
            DSAvaliablityArray = voegDoubleToe(DSAvaliablityArray, DS.getBeschikbaarheid()/100);
            DSPrijsPerSoort = voegDoubleToe(DSPrijsPerSoort, DS.getPrijs());
            DSAantalPerSoort = voegIntToe(DSAantalPerSoort,0);
            DSAantalTotaal++;
        }
        WSLoop(0, 0);

        // Draw functie voor optimaal design; * hier *
    }

    private int WSLoop(int WSAantalTotaal, int WebServer){
        for (int i = 0; i < maxAantalServers - WSAantalTotaal; i++){
            WSAantalPerSoort[WebServer] = i;
            if(WebServer == WSAantalTotaal){
                // DSLoop (0,0); * functie moet nog geschreven worden *
            }
            if(WebServer < WSAantalTotaal){
                WSLoop(i+WSAantalTotaal, WebServer+1);
            }
        }
        return WebServer;
    }

    public void OptimaliseerHuidig(){
    }
    public void Huidig(){

    }
    public JButton create_button(JButton naam, String path) {
        naam = new JButton(""); // Knop die er voor zorgt dat de actuele toestand word opgeslagen.
        naam.setBorderPainted(false);
        naam.setContentAreaFilled(false);
        naam.setBorderPainted(false);

        ImageIcon icon = new ImageIcon(this.getClass().getResource("/resources/"+path+".png"));
        Image img = icon.getImage();
        Image newimg = img.getScaledInstance(-5, schermbreedte/30,  java.awt.Image.SCALE_SMOOTH);
        ImageIcon newIcon = new ImageIcon(newimg);
        naam.setIcon(newIcon);

        naam.addActionListener(this);
        return naam;
    }
    public void activebutton(JButton knop, String active, String normal){

        // Deze functie zorgt ervoor dat als een knop is ingedrukt, deze iets van kleur veranderd, en na een 200 miliseconde
        // stop weer terug veranderd.
        ImageIcon icon = new ImageIcon(this.getClass().getResource("/resources/"+active+".png"));
        Image img = icon.getImage();
        Image newimg = img.getScaledInstance(-5, schermbreedte/30,  java.awt.Image.SCALE_SMOOTH);
        ImageIcon newIcon = new ImageIcon(newimg);
        knop.setIcon(newIcon);

        Timer timer = new Timer( 200, t -> {
            ImageIcon icon2 = new ImageIcon(this.getClass().getResource("/resources/"+normal+".png"));
            Image img2 = icon2.getImage();
            Image newimg2 = img2.getScaledInstance(-5, schermbreedte/30,  java.awt.Image.SCALE_SMOOTH);
            ImageIcon newIcon2 = new ImageIcon(newimg2);
            knop.setIcon(newIcon2);

        });
        timer.setRepeats( false );
        timer.start();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == JBopslaan) {
            activebutton(JBopslaan,"Opslaan-active","Opslaan");
            save();


        }else if(e.getSource() == back){
            activebutton(back,"back-active","back");
            setVisible(false);
            JFrame Main = new MainFrame();
        }else if(e.getSource() == JBnieuw_ontwerp){
            activebutton(JBnieuw_ontwerp,"nieuw-ontwerp-button-active","nieuw-ontwerp-button");
            dispose();
            DesignFrame design = new DesignFrame(null);
            // center frame (Mustafa)
            Toolkit toolkit = Toolkit.getDefaultToolkit();
            Dimension size = toolkit.getScreenSize();
            design.setLocation(size.width/2 - design.getWidth()/2, size.height/2 - design.getHeight()/2);
        }else if(e.getSource() == JBoptimaliseren){
            activebutton(JBoptimaliseren,"Optimaliseren-active","Optimaliseren");
            OptimalisatieFrame frame = new OptimalisatieFrame(this);
            if(frame.isGo()){
                this.gewensteBeschikbaarheid = frame.getBeschikbaarheid_Double();
                if(frame.serverlimiet()){
                    this.maxAantalServers = frame.getServerlimiet_Int();
                } else {
                    this.maxAantalServers = frame.getStandaardaantalserver_Int();
                }
                Optimaliseer();
            }
        }else if(e.getSource() == JBserveropties_wijzigen){
            activebutton(JBserveropties_wijzigen,"Serveropties-wijzigen-active","Serveropties-wijzigen");
            ServerDialog dialog = new ServerDialog(this, true, list.generateArray(), list.getServers());
        }
        else if (e.getSource() == JBvolscherm) {
            if(isVolscherm) {
                dispose();
                setExtendedState(JFrame.NORMAL);
                setUndecorated(false);
                isVolscherm = false;
                setSize(schermbreedte/30*26,schermhoogte/30*26);
                setVisible(true);
                JBvolscherm.setIcon(scaleImage(new ImageIcon(this.getClass().getResource("/resources/enlargebutton.png")), schermbreedte/15, schermhoogte/20));
                designpanel.SetKleinScherm();
            } else {
                dispose();
                setExtendedState(JFrame.MAXIMIZED_BOTH);
                setUndecorated(true);
                isVolscherm = true;
                setVisible(true);
                JBvolscherm.setIcon(scaleImage(new ImageIcon(this.getClass().getResource("/resources/smallbutton.png")), schermbreedte/15, schermhoogte/20));
                designpanel.SetGrootScherm();
            }
            }
        }

    static double[] voegDoubleToe (double[] d, double o){
        d = Arrays.copyOf(d, d.length + 1);
        d[d.length - 1] = o;
        return d;
    }

    static int[] voegIntToe (int[] i, int n){
        i = Arrays.copyOf(i, i.length + 1);
        i[i.length - 1] = n;
        return i;
    }

    public boolean getisVolscherm() {
        return isVolscherm;
    }

    public Firewall getFirewall() {
        return firewall;
    }

    public JButton getJBopslaan() {
        return JBopslaan;
    }

    public int getSchermhoogte() {
        return schermhoogte;
    }

    public int getSchermbreedte() {
        return schermbreedte;
    }

    public int returnyhoogte(String servernaam){
        int yhoogte = 10;
        int hoogte = 600;
        for (WebServer webservertje : webServer){
            if (webservertje.getNaam().equals(servernaam)){
                hoogte=yhoogte;
            }
            yhoogte = yhoogte + 71;
        }
        for (DatabaseServer webservertje : databaseServer){
            if (webservertje.getNaam().equals(servernaam)){
                hoogte=yhoogte;
            }
            yhoogte = yhoogte + 71;
        }
        return hoogte;
    }
    public void open() {
        try {
            Connection conn = DriverManager.getConnection(
                    "jdbc:mysql://192.168.1.103:3306/nerdygadgets",
                    "group4", "Qwerty1@");
            Statement stmt = conn.createStatement();
            String uniqueQuery = "SELECT * from serverSetups WHERE setupId = '" + save + "';";
            ResultSet rset = stmt.executeQuery(uniqueQuery);
            int maxx;
            if (designpanel.getFrame().getisVolscherm()){
                maxx = designpanel.getFrame().getSchermbreedte() -280;
            }else{
                maxx = designpanel.getFrame().getSchermbreedte()/36*26;
            }
            int minx = 140;
            int range = maxx - minx + 1;
            int randx = (int)(Math.random() * range) + minx;

            int maxy;
            if (designpanel.getFrame().getisVolscherm()){
                maxy = designpanel.getFrame().getSchermhoogte() -180;
            }else{
                maxy = designpanel.getFrame().getSchermhoogte()/41*26;
            }
            int miny = 0;
            int rangey = maxy - miny + 1;
            int randy = (int)(Math.random() * rangey) + miny;

            while (rset.next()) {
                if (Objects.equals(rset.getString("type"), "webserver")) {

                    ServerDragAndDrop server1 = new WebServer(rset.getString("type"), rset.getDouble("prijs"), rset.getDouble("beschikbaarheid"));
                    server1.setBounds(randx, randy, 125, 125);
                    designpanel.addArrayList(server1);
                }else if(Objects.equals(rset.getString("type"), "databaseserver")){
                    ServerDragAndDrop server1 = new DatabaseServer(rset.getString("type"), rset.getDouble("prijs"), rset.getDouble("beschikbaarheid"));
                    server1.setBounds(randx, randy, 125, 125);
                    designpanel.addArrayList(server1);
                }
                for (ServerDragAndDrop server : designpanel.getServersArray_ArrayList()){
                    designpanel.add(designpanel.getFrame().getFirewall(),server);
                    designpanel.repaint();
                }
            }


        } catch (SQLException ex) {
            System.out.println(ex);
        }
    }

    public void save(){
        String setupID = "jemoeder"; //TODO Via dialoog ff hier een unique "filename meegeven"
        boolean unique = true;
        try {
            Connection conn = DriverManager.getConnection(
                    "jdbc:mysql://192.168.1.103:3306/nerdygadgets",
                    "group4", "Qwerty1@");
            Statement stmt = conn.createStatement();
            String uniqueQuery = "SELECT setupId from serverSetups";
            ResultSet rset = stmt.executeQuery(uniqueQuery);

            while(rset.next()) {
                if (rset.getString("setupID").equals(setupID)) {
                    unique = false;
                }
            }
            if (unique) {
                    for (ServerDragAndDrop server : designpanel.getServersArray_ArrayList()){
                        if (server instanceof WebServer) {
                            String query = "INSERT INTO serverSetups(name, type, beschikbaarheid, prijs, setupId) VALUES ('" + server.getNaam() + "', '" + "webserver" + "', " + server.getBeschikbaarheid() + ", " + server.getPrijs() + ", +'" + setupID + "');";
                            stmt.executeUpdate(query);
                        } else if (server instanceof  DatabaseServer) {
                            String query = "INSERT INTO serverSetups(name, type, beschikbaarheid, prijs, setupId) VALUES ('" + server.getNaam() + "', '" + "databaseserver" + "', " + server.getBeschikbaarheid() + ", " + server.getPrijs() + ", +'" + setupID + "');";
                            stmt.executeUpdate(query);
                        } else if (server instanceof  Firewall) {
                            String query = "INSERT INTO serverSetups(name, type, beschikbaarheid, prijs, setupId) VALUES ('" + server.getNaam() + "', '" + "firewall" + "', " + server.getBeschikbaarheid() + ", " + server.getPrijs() + ", +'" + setupID + "');";
                            stmt.executeUpdate(query);
                        }

                    }
                } else {System.out.println("niet uniek");}





        } catch (SQLException ex) {
            System.out.println(ex);
        }
    }
}
