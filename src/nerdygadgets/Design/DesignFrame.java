package nerdygadgets.Design;

import com.google.gson.*;
import nerdygadgets.Design.components.DatabaseServer;
import nerdygadgets.Design.components.Firewall;
import nerdygadgets.Design.components.WebServer;
import nerdygadgets.Design.components.servers;

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
    private JButton JBbestand_openen,JBopslaan,JBnieuw_ontwerp,JBlegenveld,JBoptimaliseren,JBserveropties_wijzigen, JBvolscherm;
    private Designpanel designpanel;
  
    private Firewall firewall;
    private ArrayList webServers = new ArrayList<WebServer>();
    private ArrayList databaseServers = new ArrayList<DatabaseServer>();
    private int maxServerBacktracking;
    private int[] WSCountPerSoort;
    private int[] DSCountPerSoort;
    private int WSCountTotaal;
    private int DSCountTotaal;
    private double WSAvaliablityArray;
    private double DSAvaliablityArray;
    private double gewensteBeschikbaarheid;
    private double minimaleKosten;
    private int ServerCount;
    private int[] WSgeoptimaliseerde;
    private int[] DSgeoptimaliseerde;
    private boolean isVolscherm = false;
    private Dimension schermgrootte = Toolkit.getDefaultToolkit().getScreenSize();
    int schermhoogte = schermgrootte.height;
    int schermbreedte = schermgrootte.width;


    public DesignFrame() {
        setTitle("Design");
        setLayout(new FlowLayout());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(schermbreedte/30*26,schermhoogte/30*26); //Maakt de groote van de gui de helft van de schermgrootte

        JBbestand_openen = create_button(JBbestand_openen,"Opslaan"); add(JBbestand_openen); //Bestand openen
        JBopslaan = create_button(JBopslaan, "Opslaan"); add(JBopslaan); // Opslaan
        JBnieuw_ontwerp = create_button(JBnieuw_ontwerp,"nieuw-ontwerp-button"); add(JBnieuw_ontwerp); // Nieuw ontwerp
        JBlegenveld = create_button(JBlegenveld, "Legen-veld"); add(JBlegenveld); //Veld legen
        JBoptimaliseren = create_button(JBoptimaliseren, "Optimaliseren"); add(JBoptimaliseren); //Optimaliseren
        JBserveropties_wijzigen = create_button(JBserveropties_wijzigen, "Serveropties-wijzigen"); add(JBserveropties_wijzigen); // Serveropties
        JBvolscherm = create_button(JBvolscherm, "enlargebutton"); add(JBvolscherm); //Fullscreen

        designpanel = new Designpanel(this);
        add(designpanel);
        setVisible(true);
    }
    public ImageIcon scaleImage(ImageIcon icon, int w, int h) {
        int nw = icon.getIconWidth();
        int nh = icon.getIconHeight();
        //if(icon.getIconWidth() > w) {
        //    nw = w;
        //    nh = (nw * icon.getIconHeight()) / icon.getIconWidth();
        //}
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

    }
    public void OptimaliseerHuidig(){

    }
    public void Huidig(){

    }
    public JButton create_button(JButton naam, String path){
        naam = new JButton(""); // Knop die er voor zorgt dat de actuele toestand word opgeslagen.
        naam.setBorderPainted(false);
        naam.setContentAreaFilled(false);
        naam.setIcon(scaleImage(new ImageIcon(this.getClass().getResource("/resources/"+path+".png")), schermbreedte/15, schermhoogte/20));
        naam.addActionListener(this);
        return naam;
    }
    public void activebutton(JButton knop, String active, String normal){
        knop.setIcon(scaleImage(new ImageIcon(this.getClass().getResource("/resources/" + active +".png")), schermbreedte/15, schermhoogte/20));
        Timer timer = new Timer( 100, t -> {
            knop.setIcon(scaleImage(new ImageIcon(this.getClass().getResource("/resources/" + normal +".png")), schermbreedte/15, schermhoogte/20));
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
        }else if(e.getSource() == JBlegenveld){
            activebutton(JBlegenveld,"Legen-veld-active","Legen-veld");
        }else if(e.getSource() == JBoptimaliseren){
            activebutton(JBoptimaliseren,"Optimaliseren-active","Optimaliseren");
        }else if(e.getSource() == JBserveropties_wijzigen){
            activebutton(JBserveropties_wijzigen,"Serveropties-wijzigen-active","Serveropties-wijzigen");
        } else if (e.getSource() == JBvolscherm) {
            if(isVolscherm) {
                dispose();
                setExtendedState(JFrame.NORMAL);
                setUndecorated(false);
                isVolscherm = false;
                setSize(schermbreedte/30*26,schermhoogte/30*26);
                setVisible(true);
                JBvolscherm.setIcon(scaleImage(new ImageIcon(this.getClass().getResource("resources/enlargebutton.png")), schermbreedte/15, schermhoogte/20));
                designpanel.setvastesize(schermbreedte/30*26-25,schermhoogte/30*26-25);
            } else {
                dispose();
                setExtendedState(JFrame.MAXIMIZED_BOTH);
                setUndecorated(true);
                isVolscherm = true;
                setVisible(true);
                JBvolscherm.setIcon(scaleImage(new ImageIcon(this.getClass().getResource("resources/smallbutton.png")), schermbreedte/15, schermhoogte/20));
                designpanel.setvastesize(schermbreedte,schermhoogte);
            }
        } else if(e.getSource() == JBbestand_openen){
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
        }
    }
}

