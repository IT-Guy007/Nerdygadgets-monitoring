package nerdygadgets.Design;

import nerdygadgets.Design.components.*;
import nerdygadgets.MainFrame;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.sql.*;

@SuppressWarnings("ALL")
public class DesignFrame extends JFrame implements ActionListener {

    // Variabeles gebruikt voor designpanel
    private JButton JBopslaan,JBnieuw_ontwerp,JBoptimaliseren,JBserveropties_wijzigen, JBvolscherm, back;
    private DesignPanel designpanel;
    private Firewall firewall;
    private boolean isVolscherm = false;
    private final Dimension schermgrootte = Toolkit.getDefaultToolkit().getScreenSize();
    private final int schermhoogte = schermgrootte.height;
    private final int schermbreedte = schermgrootte.width;
    private final String save;
    private final ServerLists list;
    private ArrayList<ServerOptie> tempServerOpties = new ArrayList<>();

    // Variabeles gebruikt voor algoritme
    private int[] WSAantalPerSoort = {},DSAantalPerSoort = {},WSgeoptimaliseerde = {},DSgeoptimaliseerde = {};
    private int WSAantalTotaal = -1,DSAantalTotaal = -1,maxAantalServers;
    private double[] WSAvaliablityArray = {},DSAvaliablityArray  = {},WSPrijsPerSoort = {},DSPrijsPerSoort = {};
    private double gewensteBeschikbaarheid = -1,minimaleKosten = Double.MAX_VALUE;
    private String serverSetup;


    public DesignFrame(String save) {
        this.save = save;

        // Opmaken van JFrame
        setTitle("Nerdygadgets Monitoring Applicatie");
        setLayout(new FlowLayout());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(schermbreedte/30*26,schermhoogte/30*26); //Maakt de groote van de gui de helft van de schermgrootte

        // Toevoegen knoppen aan JFrame
        back = create_button(back,"back");
        JBnieuw_ontwerp = create_button(JBnieuw_ontwerp,"nieuw-ontwerp-button");
        JBopslaan = create_button(JBopslaan, "Opslaan");
        JBoptimaliseren = create_button(JBoptimaliseren, "Optimaliseren");
        JBserveropties_wijzigen = create_button(JBserveropties_wijzigen, "Serveropties-wijzigen");
        JBvolscherm = create_button(JBvolscherm, "enlargebutton");
        add(back); add(JBnieuw_ontwerp); add(JBopslaan); add(JBoptimaliseren); add(JBserveropties_wijzigen); add(JBvolscherm);

        // Aanmaken standaardservers en designpanel
        list = new ServerLists(this);
        designpanel = new DesignPanel(this);

        // Aanmaken en toevoegen Firewall server
        Firewall ServerOptie8 = new Firewall( "pfSense", 99.998, 4000);
        ServerOptie8.setBounds(schermbreedte/2-200,schermhoogte/2-220,125,125);
        designpanel.add(ServerOptie8);
        designpanel.addArrayList(ServerOptie8);
        firewall = ServerOptie8;

        // Toevoegen Alle knoppen linkerzijde voor slepen servers
        generateSeverOpties();

        // Visible maken van code
        setVisible(true);
        setResizable(false);
        open();
    }

    // Functies voor genereren startscherm
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
    public JButton create_button(JButton naam, String path) {
        // Functie die er voor zorgt dat all knoppen bovenaan het beginscherm gemaakt worden
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
    public void generateSeverOpties() {
        // Deze functie zorgt ervoor dat er link op het hoofdscherm serveropties worden gegenereerd zodat er servers in het veld gesleept kunnen worden
        int yhoogte = 10;//10
        for (int x=0; x< 1;x++) {
            for (ServerDragAndDrop webservertje : list.getServers()) {
                ServerOptie optie1;
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
    public void removesServerOpties() {
        //  Hiermee word een serveroptie uit de array gehaald
        for (ServerOptie s :
                tempServerOpties) {
            designpanel.remove(s);
        }

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

    // Functies voor optimalisatie
    public void Optimaliseer(double gewensteBeschikbaarheid){
        optimaliseerReset();
        this.gewensteBeschikbaarheid = gewensteBeschikbaarheid;

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

        System.out.println(serverSetup);
        TekenOptimaliseerd();
    }
    private void TekenOptimaliseerd(){
        dispose();
        DesignFrame design = new DesignFrame(null);
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension size = toolkit.getScreenSize();
        design.setLocation(size.width/2 - design.getWidth()/2, size.height/2 - design.getHeight()/2);

        int maxx;
        if (design.getDesignpanel().getFrame().getisVolscherm()){
            maxx = design.getDesignpanel().getFrame().getSchermbreedte() -280;
        }else{
            maxx = design.getDesignpanel().getFrame().getSchermbreedte()/36*26;
        }
        int minx = 140;
        int range = maxx - minx + 1;
        int randx;

        int maxy;
        if (design.getDesignpanel().getFrame().getisVolscherm()){
            maxy = design.getDesignpanel().getFrame().getSchermhoogte() -180;
        }else{
            maxy = design.getDesignpanel().getFrame().getSchermhoogte()/41*26;
        }
        int miny = 0;
        int rangey = maxy - miny + 1;
        int randy ;


        for (int i = 0; i < WSgeoptimaliseerde.length; i++){
            for(int j = 0; j < WSgeoptimaliseerde[i]; j++){
                randy = (int)(Math.random() * rangey) + miny;
                randx = (int)(Math.random() * range) + minx;

                ServerDragAndDrop WS = list.getServers().get(i+3);
                ServerDragAndDrop WS2 = new WebServer(0,WS.getNaam(),WS.getBeschikbaarheid(), WS.getPrijs());
                WS2.setBounds(randx, randy, 125, 125);
                design.getDesignpanel().addArrayList(WS2);
                System.out.println("Ik ben een database"+WS.getNaam()+" | "+WS.getBeschikbaarheid()+" | "+WS.getPrijs()+" | i = "+ i);
            }
        }for (int i = 0; i < DSgeoptimaliseerde.length; i++){
            for(int j = 0; j < DSgeoptimaliseerde[i]; j++){
                randy = (int)(Math.random() * rangey) + miny;
                randx = (int)(Math.random() * range) + minx;

                ServerDragAndDrop DS = list.getServers().get(i);
                ServerDragAndDrop DS2 = new DatabaseServer(0,DS.getNaam(),DS.getBeschikbaarheid(), DS.getPrijs());
                DS2.setBounds(randx, randy, 125, 125);
                design.getDesignpanel().addArrayList(DS2);
            }
        }
        for (ServerDragAndDrop server : design.getDesignpanel().getServersArray_ArrayList()){
            design.getDesignpanel().add(design.getDesignpanel().getFrame().getFirewall(),server);
            design.getDesignpanel().repaint();
        }
        generateSeverOpties();

    }
    private int WebserverLoop(int AantalWSTotaal, int WebServer){
        int teller = 0;
        while (teller < maxAantalServers - AantalWSTotaal){
            WSAantalPerSoort[WebServer] = teller;
            if(WebServer < WSAantalTotaal){
                WebserverLoop(teller+AantalWSTotaal, WebServer+1);
            }
            if (WebServer == WSAantalTotaal){
                DatabaseLoop(0 , 0);
            }

            teller++;
        }
        return WebServer;
    }
    private int DatabaseLoop(int AantalDBTotaal, int Database) {
        int teller = 0;
        while (teller < maxAantalServers - AantalDBTotaal) {
            DSAantalPerSoort[Database] = teller;
            teller++;
            if (Database < DSAantalTotaal)
            {
                DatabaseLoop(teller+AantalDBTotaal, Database+1);
            }
            if (Database == DSAantalTotaal) {
                double configBeschikbaarheid = OptimaliseerBerekenBeschikbaarheid();
                double configPrijs = OptimaliseerBerekenPrijs();

                if (configBeschikbaarheid > gewensteBeschikbaarheid){
                    if (configPrijs < minimaleKosten) {
                        DSgeoptimaliseerde = new int[]{};
                        WSgeoptimaliseerde = new int[]{};
                        minimaleKosten = configPrijs;
                        serverSetup = "Fw: 1 | Wb: ";
                        for (int y = 0; y < WSAantalPerSoort.length; y++){
                            if (y == 0){
                                serverSetup += WSAantalPerSoort[y];
                            } else{
                                serverSetup += "-" + WSAantalPerSoort[y];
                            }
                            WSgeoptimaliseerde = voegIntToe(WSgeoptimaliseerde, WSAantalPerSoort[y]);
                        }
                        serverSetup += " | Db: ";
                        for (int j = 0; j < DSAantalPerSoort.length; j++) {
                            if (j == 0) {
                                serverSetup += DSAantalPerSoort[j];
                            } else {
                                serverSetup += "-" + DSAantalPerSoort[j];
                            }
                            DSgeoptimaliseerde = voegIntToe(DSgeoptimaliseerde, DSAantalPerSoort[j]);
                        }
                    }
                    return Database;
                }
            }
        }
        return Database;
    }
    private double OptimaliseerBerekenBeschikbaarheid(){
        double beschikbaarheidFirewall = 0.9999800000000001;
        double beschikbaarheidDatabase = 1;
        double beschikbaarheidWebserver = 1;

        for (int i = 0; i < WSAantalPerSoort.length; i++){
            beschikbaarheidWebserver *= Math.pow((1 - WSAvaliablityArray[i]), WSAantalPerSoort[i]);
        }
        beschikbaarheidWebserver = 1-beschikbaarheidWebserver;

        for (int i = 0; i < DSAantalPerSoort.length; i++){
            beschikbaarheidDatabase *= Math.pow((1 - DSAvaliablityArray[i]), DSAantalPerSoort[i]);
        }
        beschikbaarheidDatabase = 1-beschikbaarheidDatabase;

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
    private void optimaliseerReset(){
        WSAantalPerSoort = new int[]{};
        DSAantalPerSoort = new int[]{};
        WSAvaliablityArray = new double[]{};
        DSAvaliablityArray = new double[]{};
        WSPrijsPerSoort = new double[]{};
        DSPrijsPerSoort = new double[]{};
        WSAantalTotaal = -1;
        DSAantalTotaal = -1;
        minimaleKosten = Double.MAX_VALUE;
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

    // Functies voor het opslaan / gebruik van knoppen
    public void open() {
        int projectID = 0;
        ArrayList<Integer> idServers = new ArrayList<>();
        try {
            Connection conn = DriverManager.getConnection(
                    "jdbc:mysql://192.168.1.1:3306/application",
                    "group4", "Qwerty1@");
            Statement stmt = conn.createStatement();
            String query1 = "SELECT projectID from project WHERE name = '" + save + "';";
            ResultSet rset = stmt.executeQuery(query1);
            while (rset.next()) {
                projectID = rset.getInt("projectID");
            }

            String query2 = "SELECT server_PresentID from project_Has_Servers WHERE projectID = " + projectID + ";";
            Statement stmt2 = conn.createStatement();
            ResultSet rset2 = stmt2.executeQuery(query2);
            while (rset2.next()) {
                idServers.add(rset2.getInt("server_PresentID"));
            }
        } catch (Exception e){
            System.out.println(e);}
        try{
            for (Integer servers : idServers) {
                Connection conn = DriverManager.getConnection(
                        "jdbc:mysql://192.168.1.1:3306/application",
                        "group4", "Qwerty1@");
                Statement stmt = conn.createStatement();
                String query3 = "SELECT * from servers WHERE serverID = " + servers + ";";
                ResultSet rset3 = stmt.executeQuery(query3);
                while (rset3.next()) {
                    int maxx;
                    if (designpanel.getFrame().getisVolscherm()) {
                        maxx = designpanel.getFrame().getSchermbreedte() - 280;
                    } else {
                        maxx = designpanel.getFrame().getSchermbreedte() / 36 * 26;
                    }
                    int minx = 140;
                    int range = maxx - minx + 1;

                    int maxy;
                    if (designpanel.getFrame().getisVolscherm()) {
                        maxy = designpanel.getFrame().getSchermhoogte() - (designpanel.getFrame().getSchermhoogte() / 3);
                    } else {
                        maxy = designpanel.getFrame().getSchermhoogte() / 41 * 26;
                    }
                    int miny = 0;
                    int rangey = maxy - miny + 1;
                    if (rset3.getInt("server_KindID") == 2) {
                        int randx = (int) (Math.random() * range) + minx;
                        int randy = (int) (Math.random() * rangey) + miny;
                        ServerDragAndDrop server1 = new WebServer((int) Math.floor(Math.random() * (10000 - 0 + 1) + 0),rset3.getString("name"),rset3.getDouble("availability"),rset3.getDouble("price"));
                        server1.setBounds(randx, randy, 125, 125);
                        designpanel.addArrayList(server1);
                    } else if (rset3.getInt("server_KindID") == 1) {
                        int randx = (int) (Math.random() * range) + minx;
                        int randy = (int) (Math.random() * rangey) + miny;
                        ServerDragAndDrop server1 = new DatabaseServer((int) Math.floor(Math.random() * (10000 - 0 + 1) + 0),rset3.getString("name"),rset3.getDouble("availability"),rset3.getDouble("price"));
                        server1.setBounds(randx, randy, 125, 125);
                        designpanel.addArrayList(server1);
                    }
                    for (ServerDragAndDrop server : designpanel.getServersArray_ArrayList()) {
                        designpanel.add(designpanel.getFrame().getFirewall(), server);
                        designpanel.repaint();
                    }
                }
            }


        } catch (SQLException ex) {
            System.out.println(ex);
        }
    }
    public void save() throws SQLException {
        String setupID = "jevader4"; //TODO Via dialoog ff hier een unique "filename meegeven"
        boolean unique = true;
        try {
            Connection conn = DriverManager.getConnection(
                    "jdbc:mysql://192.168.1.1:3306/application",
                    "group4", "Qwerty1@");
            Statement stmt = conn.createStatement();
            String uniqueQuery = "SELECT name from project";
            ResultSet rset = stmt.executeQuery(uniqueQuery);
            System.out.println("test1");
            while(rset.next()) {
                if (rset.getString("name").equals(setupID)) {
                    System.out.println("test2");
                    unique = false;
                }
            }


            if (unique) {
                String query5 = "INSERT INTO project(name,wanted_Availability) VALUES ('"+setupID+"',100.0)";
                stmt.executeUpdate(query5);
                System.out.println("test3");
                String query3 = "SELECT projectID from project where name = '"+setupID+"'";
                ResultSet rset3 = stmt.executeQuery(query3);
                String projectID="0";
                while(rset3.next()) {
                    projectID = rset3.getString("projectID");
                }
                for (ServerOptie opties: tempServerOpties){
                    try {
                        if (opties.getType().equals("webserver")) {
                            String query2 = "INSERT INTO serverSetups(name,type,price,availability,setupID) VALUES ('" + opties.getName() + "','webserver'," + opties.getPrijs() + "," + opties.getBeschikbaarheid() + ",'"+setupID+"')";
                            stmt.executeUpdate(query2);
                        } else if (opties.getType().equals("databaseserver")) {
                            String query2 = "INSERT INTO serverSetups(name,type,price,availability,setupID) VALUES ('" + opties.getName() + "','databaseserver'," + opties.getPrijs() + "," + opties.getBeschikbaarheid() + ",'"+setupID+"')";
                            stmt.executeUpdate(query2);
                        } else if (opties.getType().equals("firewall")) {
                            String query2 = "INSERT INTO serverSetups(name,type,price,availability,setupID) VALUES ('" + opties.getName() + "','firewall'," + opties.getPrijs() + "," + opties.getBeschikbaarheid() + ",'"+setupID+"')";
                            stmt.executeUpdate(query2);
                        }
                    }catch (Exception e){}
                }
                for (ServerDragAndDrop server : designpanel.getServersArray_ArrayList()){
                    if (server instanceof WebServer) {
                        String query = "INSERT INTO servers(name, server_kindID, availability, price) VALUES ('" + server.getNaam() + "', 2, " + server.getBeschikbaarheid() + ", " + server.getPrijs() + ");";
                        stmt.executeUpdate(query);
                    } else if (server instanceof  DatabaseServer) {
                        String query = "INSERT INTO servers(name, server_kindID, availability, price) VALUES ('" + server.getNaam() + "',1, " + server.getBeschikbaarheid() + ", " + server.getPrijs() + ");";
                        stmt.executeUpdate(query);
                    } else if (server instanceof  Firewall) {
                        String query = "INSERT INTO servers(name, server_kindID, availability, price) VALUES ('" + server.getNaam() + "', 3, " + server.getBeschikbaarheid() + ", " + server.getPrijs() + ");";
                        stmt.executeUpdate(query);
                    }
                    String uniqueQuery2 = "SELECT serverID from servers WHERE name = '"+server.getNaam()+"'";
                    ResultSet rset2 = stmt.executeQuery(uniqueQuery2);
                    String juiste_id="0";
                    while(rset2.next()) {
                        juiste_id = rset2.getString("serverID");
                    }
                    String query2 = "INSERT INTO server_Present(serverID, x,y) VALUES ("+juiste_id+","+server.getBounds().getX()+","+server.getBounds().getY()+")";
                    stmt.executeUpdate(query2);

                    String uniqueQuery4 = "SELECT server_PresentID from server_Present WHERE serverID = "+juiste_id;
                    ResultSet rset7 = stmt.executeQuery(uniqueQuery4);
                    String juiste_id2="0";
                    while(rset7.next()) {
                        juiste_id2 = rset7.getString("server_PresentID");
                    }

                    String query4 = "INSERT INTO project_Has_Servers(projectID,server_PresentID) VALUES ("+projectID+","+juiste_id2+")";
                    stmt.executeUpdate(query4);


                }

            }
        }catch (Exception e){

        }

    }
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == JBopslaan) {
            activebutton(JBopslaan,"Opslaan-active","Opslaan");
//            save();

            new OpslaanDialog(designpanel,this);

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
                gewensteBeschikbaarheid = frame.getBeschikbaarheid_Double();
                if(frame.serverlimiet()){
                    this.maxAantalServers = frame.getServerlimiet_Int();
                } else {
                    this.maxAantalServers = frame.getStandaardaantalserver_Int();
                }
                Optimaliseer(gewensteBeschikbaarheid/100);
            }

        }else if(e.getSource() == JBserveropties_wijzigen){
            activebutton(JBserveropties_wijzigen,"Serveropties-wijzigen-active","Serveropties-wijzigen");
            ServerDialog dialog = new ServerDialog(this, true, list.generateArray(), list.getServers());
            System.out.println("test");
            list.setServers(dialog.getServerslist());
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



    // Getters en Setters
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
    public ServerLists getList() {
        return list;
    }
    public ArrayList<ServerOptie> getTempServerOpties() {
        return tempServerOpties;
    }
    public DesignPanel getDesignpanel() {
        return designpanel;
    }
    public void setTempServerOpties(ArrayList<ServerOptie> tempServerOpties) {
        this.tempServerOpties = tempServerOpties;
    }
    public void setJBopslaan(JButton JBopslaan) {
        this.JBopslaan = JBopslaan;
    }
    public JButton getJBnieuw_ontwerp() {
        return JBnieuw_ontwerp;
    }
    public void setJBnieuw_ontwerp(JButton JBnieuw_ontwerp) {
        this.JBnieuw_ontwerp = JBnieuw_ontwerp;
    }
    public JButton getJBoptimaliseren() {
        return JBoptimaliseren;
    }
    public void setJBoptimaliseren(JButton JBoptimaliseren) {
        this.JBoptimaliseren = JBoptimaliseren;
    }
    public JButton getJBserveropties_wijzigen() {
        return JBserveropties_wijzigen;
    }
    public void setJBserveropties_wijzigen(JButton JBserveropties_wijzigen) {
        this.JBserveropties_wijzigen = JBserveropties_wijzigen;
    }
    public JButton getJBvolscherm() {
        return JBvolscherm;
    }
    public void setJBvolscherm(JButton JBvolscherm) {
        this.JBvolscherm = JBvolscherm;
    }
    public JButton getBack() {
        return back;
    }
    public void setBack(JButton back) {
        this.back = back;
    }
    public void setDesignpanel(DesignPanel designpanel) {
        this.designpanel = designpanel;
    }
    public void setFirewall(Firewall firewall) {
        this.firewall = firewall;
    }
    public int[] getWSAantalPerSoort() {
        return WSAantalPerSoort;
    }
    public void setWSAantalPerSoort(int[] WSAantalPerSoort) {
        this.WSAantalPerSoort = WSAantalPerSoort;
    }
    public int[] getDSAantalPerSoort() {
        return DSAantalPerSoort;
    }
    public void setDSAantalPerSoort(int[] DSAantalPerSoort) {
        this.DSAantalPerSoort = DSAantalPerSoort;
    }
    public int[] getWSgeoptimaliseerde() {
        return WSgeoptimaliseerde;
    }
    public void setWSgeoptimaliseerde(int[] WSgeoptimaliseerde) {
        this.WSgeoptimaliseerde = WSgeoptimaliseerde;
    }
    public int[] getDSgeoptimaliseerde() {
        return DSgeoptimaliseerde;
    }
    public void setDSgeoptimaliseerde(int[] DSgeoptimaliseerde) {
        this.DSgeoptimaliseerde = DSgeoptimaliseerde;
    }
    public int getWSAantalTotaal() {
        return WSAantalTotaal;
    }
    public void setWSAantalTotaal(int WSAantalTotaal) {
        this.WSAantalTotaal = WSAantalTotaal;
    }
    public int getDSAantalTotaal() {
        return DSAantalTotaal;
    }
    public void setDSAantalTotaal(int DSAantalTotaal) {
        this.DSAantalTotaal = DSAantalTotaal;
    }
    public int getMaxAantalServers() {
        return maxAantalServers;
    }
    public void setMaxAantalServers(int maxAantalServers) {
        this.maxAantalServers = maxAantalServers;
    }
    public double[] getWSAvaliablityArray() {
        return WSAvaliablityArray;
    }
    public void setWSAvaliablityArray(double[] WSAvaliablityArray) {
        this.WSAvaliablityArray = WSAvaliablityArray;
    }
    public double[] getDSAvaliablityArray() {
        return DSAvaliablityArray;
    }
    public void setDSAvaliablityArray(double[] DSAvaliablityArray) {
        this.DSAvaliablityArray = DSAvaliablityArray;
    }
    public double[] getWSPrijsPerSoort() {
        return WSPrijsPerSoort;
    }
    public void setWSPrijsPerSoort(double[] WSPrijsPerSoort) {
        this.WSPrijsPerSoort = WSPrijsPerSoort;
    }
    public double[] getDSPrijsPerSoort() {
        return DSPrijsPerSoort;
    }
    public void setDSPrijsPerSoort(double[] DSPrijsPerSoort) {
        this.DSPrijsPerSoort = DSPrijsPerSoort;
    }
    public double getGewensteBeschikbaarheid() {
        return gewensteBeschikbaarheid;
    }
    public void setGewensteBeschikbaarheid(double gewensteBeschikbaarheid) {
        this.gewensteBeschikbaarheid = gewensteBeschikbaarheid;
    }
    public double getMinimaleKosten() {
        return minimaleKosten;
    }
    public void setMinimaleKosten(double minimaleKosten) {
        this.minimaleKosten = minimaleKosten;
    }
    public String getServerSetup() {
        return serverSetup;
    }
    public void setServerSetup(String serverSetup) {
        this.serverSetup = serverSetup;
    }
    public boolean isVolscherm() {
        return isVolscherm;
    }
    public void setVolscherm(boolean volscherm) {
        isVolscherm = volscherm;
    }
    public Dimension getSchermgrootte() {
        return schermgrootte;
    }
    public String getSave() {
        return save;
    }
}
