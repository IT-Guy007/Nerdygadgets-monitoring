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
    private DesignPanel designpanel;

    private Firewall firewall;

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
    ServerOptie optie1;
    ArrayList<ServerOptie> tempServerOpties = new ArrayList<>();

    private boolean isVolscherm = false;
    Dimension schermgrootte = Toolkit.getDefaultToolkit().getScreenSize();
    int schermhoogte = schermgrootte.height;
    int schermbreedte = schermgrootte.width;
    String save;




    public DesignFrame(String save) {
        this.save = save;
        list = new ServerLists(this);

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

        designpanel = new DesignPanel(this);
        //add(designpanel);

        Firewall ServerOptie8 = new Firewall( "pfSense", 99.998, 4000);
        ServerOptie8.setBounds(schermbreedte/2-200,schermhoogte/2-220,125,125);
        designpanel.add(ServerOptie8);
        designpanel.addArrayList(ServerOptie8);
        firewall = ServerOptie8;

        generateSeverOpties();

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
        for (ServerDragAndDrop server: list.getServers()) {
            if (server instanceof WebServer) {
                WSAvaliablityArray = voegDoubleToe(WSAvaliablityArray, server.getBeschikbaarheid() / 100);
                WSPrijsPerSoort = voegDoubleToe(WSPrijsPerSoort, server.getPrijs());
                WSAantalPerSoort = voegIntToe(WSAantalPerSoort, 0);
                WSAantalTotaal++;
            } else if (server instanceof DatabaseServer) {
                DSAvaliablityArray = voegDoubleToe(DSAvaliablityArray, server.getBeschikbaarheid() / 100);
                DSPrijsPerSoort = voegDoubleToe(DSPrijsPerSoort, server.getPrijs());
                DSAantalPerSoort = voegIntToe(DSAantalPerSoort, 0);
                DSAantalTotaal++;
            }
        }

        WebserverLoop(0, 0);

        TekenOptimaliseerd();
    }


    private void TekenOptimaliseerd(){
        designpanel.removeAll();

        firewall = new Firewall(firewall.getNaam(), firewall.getPrijs(), firewall.getBeschikbaarheid(), designpanel.getWidth()/2, designpanel.getHeight()/2);
        designpanel.add(firewall);

        for (int i = 0; i < WSgeoptimaliseerde.length; i++){
            for(int j = 0; j < WSgeoptimaliseerde[i]; j++){
                ServerDragAndDrop WS = list.getServers().get(i);
                ServerDragAndDrop WS2 = new WebServer(WS.getNaam(), WS.getPrijs(), WS.getBeschikbaarheid(), designpanel.getWidth()/4, 110*j);
                designpanel.add(WS2);
            }
        }

        for (int i = 0; i < DSgeoptimaliseerde.length; i++){
            for(int j = 0; j < DSgeoptimaliseerde[i]; j++){
                ServerDragAndDrop DS = list.getServers().get(i);
                ServerDragAndDrop DS2 = new DatabaseServer(DS.getNaam(), DS.getPrijs(), DS.getBeschikbaarheid(), designpanel.getWidth()/4, 110*j);
                designpanel.add(DS2);
            }
        }
        
    }
    private int WebserverLoop(int AantalWSTotaal, int WebServer){
        for (int i = 0; i < maxAantalServers - AantalWSTotaal; i++){

            WSAantalPerSoort[WebServer] = i;
            if(WebServer == WSAantalTotaal){
                // DSLoop (0,0); * functie moet nog geschreven worden *
            }
        }
        return WebServer;
    }
    private int DatabaseLoop(int AantalDBTotaal, int Database) {
        for (int i = 0; i < maxAantalServers - AantalDBTotaal; i++) {
            DSAantalPerSoort[Database] = i;
            if (Database < DSAantalTotaal) ;
            {
                DatabaseLoop(i + AantalDBTotaal, Database + 1);
            }
            if (Database == DSAantalTotaal) {
                double configBeschikbaarheid = OptimaliseerBerekenBeschikbaarheid();
                double configPrijs = OptimaliseerBerekenPrijs();

                if (configBeschikbaarheid > gewensteBeschikbaarheid){
                    if (configPrijs < minimaleKosten) {
                        DSgeoptimaliseerde = new int[]{};
                        WSgeoptimaliseerde = new int[]{};
                        minimaleKosten = configPrijs;

                        for (int j = 0; j < DSAantalPerSoort.length; j++){
                            DSgeoptimaliseerde = voegIntToe(DSgeoptimaliseerde, DSAantalPerSoort[j]);
                        }
                        for (int y = 0; y < WSAantalPerSoort.length; y++){
                            WSgeoptimaliseerde = voegIntToe(WSgeoptimaliseerde, WSAantalPerSoort[y]);
                        }

                        return Database;
                    }
                }

            }
        }
        return Database;
    }
    private double OptimaliseerBerekenBeschikbaarheid(){
        double beschikbaarheidFirewall = 1, beschikbaarheidDatabase = 1, beschikbaarheidWebserver = 1;

        for (int i = 0; i < DSAantalPerSoort.length; i++){
            beschikbaarheidDatabase *= Math.pow((1 - DSAantalPerSoort[i]), DSAvaliablityArray[i]);
        }
        beschikbaarheidWebserver = 1 - beschikbaarheidWebserver;

        for (int i = 0; i < WSAantalPerSoort.length; i++){
            beschikbaarheidWebserver *= Math.pow((1 - WSAvaliablityArray[i]), WSAantalPerSoort[i]);
        }
        beschikbaarheidDatabase = 1 - beschikbaarheidDatabase;

        beschikbaarheidFirewall = Math.pow((1- firewall.getBeschikbaarheid() / 100), 1);

        double beschikbaarheid = beschikbaarheidFirewall * beschikbaarheidDatabase * beschikbaarheidWebserver;
        return beschikbaarheid;
    }
    private double OptimaliseerBerekenPrijs(){
        double prijsFirewall = firewall.getPrijs();
        double prijsDatabase = 0;
        double prijsWebserver = 0;

        for (int i = 0; i < DSAantalPerSoort.length; i++){
            prijsDatabase += (DSAantalPerSoort[i] * DSPrijsPerSoort[i]);
        }

        for (int i = 0; i < WSAantalPerSoort.length; i++){
            prijsWebserver += (WSAantalPerSoort[i] * WSPrijsPerSoort[i]);
        }

        double prijsTotaal = prijsDatabase + prijsWebserver + prijsFirewall;
        return prijsTotaal;

    }
    public void generateSeverOpties() {
        int yhoogte = 10;//10
        for (int x=0; x< 1;x++) {
            for (ServerDragAndDrop webservertje : list.getServers()) {
                if (webservertje instanceof WebServer) {
                    webservertje.getPrijs();
                    optie1 = new ServerOptie(designpanel, webservertje.getNaam(), webservertje.getBeschikbaarheid(), webservertje.getPrijs(), "webserver");
                    optie1.setBounds(10, yhoogte, 121, 61);

                    int id = optie1.getId();
                    webservertje.setId(id);

                    tempServerOpties.add(optie1);
                    designpanel.add(optie1);
                    designpanel.repaint();
                    yhoogte = yhoogte + 71;
                } else if (webservertje instanceof DatabaseServer) {
                    webservertje.getPrijs();
                    optie1 = new ServerOptie(designpanel, webservertje.getNaam(), webservertje.getBeschikbaarheid(), webservertje.getPrijs(), "databaseserver");
                    optie1.setBounds(10, yhoogte, 121, 61);
                    tempServerOpties.add(optie1);
                    designpanel.add(optie1);
                    designpanel.repaint();

                    int id = optie1.getId();
                    webservertje.setId(id);
                    yhoogte = yhoogte + 71;
                }
            }
        }
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


    public void removesServerOpties() {
        for (ServerOptie s :
                tempServerOpties) {
            designpanel.remove(s);
        }

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
            System.out.println("test");
            removesServerOpties();
            generateSeverOpties();
            designpanel.repaint();
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
        for (ServerDragAndDrop webservertje : list.getServers()){
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
                    "jdbc:mysql://192.168.1.103:3306/application",
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

            int maxy;
            if (designpanel.getFrame().getisVolscherm()){
                maxy = designpanel.getFrame().getSchermhoogte() -(designpanel.getFrame().getSchermhoogte()/3);
            }else{
                maxy = designpanel.getFrame().getSchermhoogte()/41*26;
            }
            int miny = 0;
            int rangey = maxy - miny + 1;

            while (rset.next()) {
                if (Objects.equals(rset.getString("type"), "webserver")) {
                    int randx = (int)(Math.random() * range) + minx;
                    int randy = (int)(Math.random() * rangey) + miny;
                    ServerDragAndDrop server1 = new WebServer((int)Math.floor(Math.random()*(10000-0+1)+0),rset.getString("type"), rset.getDouble("beschikbaarheid"), rset.getDouble("prijs"));
                    server1.setBounds(randx, randy, 125, 125);
                    designpanel.addArrayList(server1);
                }else if(Objects.equals(rset.getString("type"), "databaseserver")){
                    int randx = (int)(Math.random() * range) + minx;
                    int randy = (int)(Math.random() * rangey) + miny;
                    ServerDragAndDrop server1 = new DatabaseServer((int)Math.floor(Math.random()*(10000-0+1)+0),rset.getString("type"), rset.getDouble("beschikbaarheid"), rset.getDouble("prijs"));
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
        String setupID = "jevader"; //TODO Via dialoog ff hier een unique "filename meegeven"
        boolean unique = true;
        try {
            Connection conn = DriverManager.getConnection(
                    "jdbc:mysql://192.168.1.103:3306/application",
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

    public ServerLists getList() {
        return list;
    }

    public ArrayList<ServerOptie> getTempServerOpties() {
        return tempServerOpties;
    }
}
