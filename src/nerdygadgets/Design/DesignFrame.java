package nerdygadgets.Design;

import com.google.gson.*;
import nerdygadgets.Design.components.DatabaseServer;
import nerdygadgets.Design.components.Firewall;
import nerdygadgets.Design.components.WebServer;
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
import java.util.Scanner;


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
    private int[] WSgeoptimaliseerde;
    private int[] DSgeoptimaliseerde;
    private boolean isVolscherm = false;
    Dimension schermgrootte = Toolkit.getDefaultToolkit().getScreenSize();
    int schermhoogte = schermgrootte.height;
    int schermbreedte = schermgrootte.width;

    public DesignFrame() {
        DatabaseServer ServerOptie1 = new DatabaseServer( "HAL9001DB", 5100, 90);
        DatabaseServer ServerOptie2 = new DatabaseServer( "HAL9002DB", 7700, 95);
        DatabaseServer ServerOptie3 = new DatabaseServer( "HAL9003DB", 12200, 98);
        WebServer ServerOptie4 = new WebServer( "HAL9001W", 2200, 80);
        WebServer ServerOptie5 = new WebServer( "HAL9002W",  3200, 90);
        WebServer ServerOptie6 = new WebServer( "HAL9003W",  5100, 95);
        webServer.add(ServerOptie4);webServer.add(ServerOptie5);webServer.add(ServerOptie6);
        databaseServer.add(ServerOptie1);databaseServer.add(ServerOptie2); databaseServer.add(ServerOptie3);

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

}
