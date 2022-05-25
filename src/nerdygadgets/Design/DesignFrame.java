package nerdygadgets.Design;

import com.google.gson.*;
import nerdygadgets.Design.components.*;
import nerdygadgets.MainFrame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

import java.util.Scanner;


public class DesignFrame extends JFrame implements ActionListener {
    private JButton JBopslaan,JBnieuw_ontwerp,JBbestand_openen,JBoptimaliseren,JBserveropties_wijzigen, JBvolscherm, back;
    private Designpanel designpanel;

    private Firewall firewall;

    private int maxServerBacktracking;
    private int[] WSAantalPerSoort = {};
    private int[] DSAantalPerSoort = {};

    private int WSAantalTotaal = -1;
    private int DSAantalTotaal = -1;

    private double[] WSAvaliablityArray = {};
    private double[] DSAvaliablityArray  = {};
    private double[] WSPrijsPerSoort = {};
    private double[] DSPrijsPerSoort = {};

    private double gewensteBeschikbaarheid = -1;
    private double minimaleKosten = Double.MAX_VALUE;
    private int ServerCount;
    private int maxAantalServers;
    private int[] WSgeoptimaliseerde = {};
    private int[] DSgeoptimaliseerde = {};
    ServerLists list;
    Serveroptie optie1;
    ArrayList<Serveroptie> tempServerOpties = new ArrayList<>();

    private boolean isVolscherm = false;
    Dimension schermgrootte = Toolkit.getDefaultToolkit().getScreenSize();
    int schermhoogte = schermgrootte.height;
    int schermbreedte = schermgrootte.width;

    public DesignFrame() {
        list = new ServerLists();

        setTitle("Nerdygadgets monitoring aplicatie");

        setLayout(new FlowLayout());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(schermbreedte/30*26,schermhoogte/30*26); //Maakt de groote van de gui de helft van de schermgrootte
        
        back = new JButton("Back");
        back.addActionListener(this);
        back.setSize(2,1);
        back.setVisible(true);
        add(back);
        
        JBnieuw_ontwerp = create_button(JBnieuw_ontwerp,"nieuw-ontwerp-button");
        add(JBnieuw_ontwerp);
        JBopslaan = create_button(JBopslaan, "Opslaan");
        add(JBopslaan);
        JBbestand_openen = create_button(JBbestand_openen, "Legen-veld");
        add(JBbestand_openen);
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

        generateSeverOpties();

        setVisible(true);
        setResizable(false);
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

    public int BerekenBeschikbaarheid(){
        return 0;
    }
    public int BerekenKosten(){
        return 0;
    }


    public void Optimaliseer(){

        for (ServerDragAndDrop server: list.getServers()){
            if (server instanceof WebServer) {
                WSAvaliablityArray = voegDoubleToe(WSAvaliablityArray, server.getBeschikbaarheid()/100);
                WSPrijsPerSoort = voegDoubleToe(WSPrijsPerSoort, server.getPrijs());
                WSAantalPerSoort = voegIntToe(WSAantalPerSoort,0);
                WSAantalTotaal++;
            } else if (server instanceof DatabaseServer) {
                DSAvaliablityArray = voegDoubleToe(DSAvaliablityArray, server.getBeschikbaarheid()/100);
                DSPrijsPerSoort = voegDoubleToe(DSPrijsPerSoort, server.getPrijs());
                DSAantalPerSoort = voegIntToe(DSAantalPerSoort,0);
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
            if(WebServer < WSAantalTotaal){
                WebserverLoop(i + AantalWSTotaal, WebServer + 1);
            }
            if(WebServer == WSAantalTotaal){
                DatabaseLoop (0,0);
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

    public void OptimaliseerHuidig(){
    }
    public void Huidig(){

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
    public JButton create_button(JButton naam, String path) {
        naam = new JButton(""); // Knop die er voor zorgt dat de actuele toestand word opgeslagen.
        naam.setBorderPainted(false);
        naam.setContentAreaFilled(false);
        naam.setBorderPainted(false);

        ImageIcon icon = new ImageIcon(Objects.requireNonNull(this.getClass().getResource("/resources/" + path + ".png")));
        Image img = icon.getImage();
        Image newimg = img.getScaledInstance(-5, schermbreedte/30,  java.awt.Image.SCALE_SMOOTH);
        ImageIcon newIcon = new ImageIcon(newimg);
        naam.setIcon(newIcon);

        naam.addActionListener(this);
        return naam;
    }

    public void generateSeverOpties() {
        int yhoogte = 10;
        for (ServerDragAndDrop webservertje : list.getServers()){
            if (webservertje instanceof WebServer) {
                webservertje.getPrijs();
                optie1 = new Serveroptie(designpanel,webservertje.getNaam(),webservertje.getBeschikbaarheid(),webservertje.getPrijs(),"webserver");
                optie1.setBounds(10, yhoogte, 121, 61);
                tempServerOpties.add(optie1);
                designpanel.add(optie1);
                designpanel.repaint();
                yhoogte = yhoogte + 71;
            } else if (webservertje instanceof DatabaseServer) {
                webservertje.getPrijs();
                optie1 = new Serveroptie(designpanel,webservertje.getNaam(),webservertje.getBeschikbaarheid(),webservertje.getPrijs(),"databaseserver");
                optie1.setBounds(10, yhoogte, 121, 61);
                tempServerOpties.add(optie1);
                designpanel.add(optie1);
                designpanel.repaint();
                yhoogte = yhoogte + 71;
            }

        }
    }

    public void removesServerOpties() {
        for (Serveroptie s :
                tempServerOpties) {
            designpanel.remove(s);
        }

    }

    public void activebutton(JButton knop, String active, String normal){

        // Deze functie zorgt ervoor dat als een knop is ingedrukt, deze iets van kleur veranderd, en na een 200 miliseconde
        // stop weer terug veranderd.
        ImageIcon icon = new ImageIcon(Objects.requireNonNull(this.getClass().getResource("/resources/" + active + ".png")));
        Image img = icon.getImage();
        Image newimg = img.getScaledInstance(-5, schermbreedte/30,  java.awt.Image.SCALE_SMOOTH);
        ImageIcon newIcon = new ImageIcon(newimg);
        knop.setIcon(newIcon);

        Timer timer = new Timer( 200, t -> {
            ImageIcon icon2 = new ImageIcon(Objects.requireNonNull(this.getClass().getResource("/resources/" + normal + ".png")));
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
        }else if(e.getSource() == JBnieuw_ontwerp){
            activebutton(JBnieuw_ontwerp,"nieuw-ontwerp-button-active","nieuw-ontwerp-button");
            dispose();
            DesignFrame design = new DesignFrame();
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
        }else if(e.getSource() == JBbestand_openen){
            Gson gson = new GsonBuilder()
                    .excludeFieldsWithoutExposeAnnotation()
                    .create();

            // Let user pick a file to open
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Open Infrastructure Design File");
            int option = fileChooser.showOpenDialog(this);
            if(option == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();
                JsonArray array = null;
                try {
                    // Create file reader
                    Scanner reader = new Scanner(file);
                    JsonParser parser = new JsonParser();

                    // Convert file to a json array
                    array = (JsonArray) parser.parse(new FileReader(file.getAbsolutePath()));
                } catch (FileNotFoundException fileNotFoundException) {
                    fileNotFoundException.printStackTrace();
                }

                try {
                    // Loop over the json array to retrieve the infrastructure components
                    for (Object object : array) {
                        JsonObject jsonObject = (JsonObject) object;

                        // Convert json component values to usable variables
                        String name = jsonObject.get("name").getAsString();
                        String type = jsonObject.get("type").getAsString();
                        double availability = jsonObject.get("availability").getAsDouble();
                        double annualPrice = jsonObject.get("annualPrice").getAsDouble();
                        int panelX = jsonObject.get("panelX").getAsInt();
                        int panelY = jsonObject.get("panelY").getAsInt();

                    }
                } catch (Exception ex) {
                    System.out.println(ex);
                }
            }
        }else if(e.getSource() == back) {
            setVisible(false);
            JFrame Main = new MainFrame();
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
}
