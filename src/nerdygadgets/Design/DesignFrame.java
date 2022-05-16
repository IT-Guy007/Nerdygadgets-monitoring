package nerdygadgets.Design;
import nerdygadgets.Design.components.*;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class DesignFrame extends JFrame implements ActionListener {
    private JButton JBopslaan,JBnieuw_ontwerp,JBlegenveld,JBoptimaliseren,JBserveropties_wijzigen, JBvolscherm;
    private DesignPanel designpanel;
  
    private Firewall firewall;
    private ArrayList webServer = new ArrayList<WebServer>();
    private ArrayList databaseServer = new ArrayList<DatabaseServer>();
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
    Dimension schermgrootte = Toolkit.getDefaultToolkit().getScreenSize();
    int schermhoogte = schermgrootte.height;
    int schermbreedte = schermgrootte.width;

    public DesignFrame() {
        setTitle("Nerdygadgets monitoring aplicatie");
        setLayout(new FlowLayout());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(schermbreedte/30*26,schermhoogte/30*26); //Maakt de groote van de gui de helft van de schermgrootte

        JBnieuw_ontwerp = create_button(JBnieuw_ontwerp,"nieuw-ontwerp-button");
        add(JBnieuw_ontwerp);
        JBopslaan = create_button(JBopslaan, "Opslaan");
        add(JBopslaan);
        JBlegenveld = create_button(JBlegenveld, "Legen-veld");
        add(JBlegenveld);
        JBoptimaliseren = create_button(JBoptimaliseren, "Optimaliseren");
        add(JBoptimaliseren);
        JBserveropties_wijzigen = create_button(JBserveropties_wijzigen, "Serveropties-wijzigen");
        add(JBserveropties_wijzigen);
        JBvolscherm = create_button(JBvolscherm, "enlargebutton");
        add(JBvolscherm);

        //designpanel = new Designpanel(this);
        //designpanel.setBackground(Color.green);
        //designpanel.setPreferredSize(new Dimension(this.getWidth() - 25, this.getHeight() - 100));
        //add(designpanel);

        designpanel = new DesignPanel(this);
        //designpanel.setBackground(Color.green);
        //designpanel.setPreferredSize(new Dimension(this.getWidth() - 25, this.getHeight() - 100));
        add(designpanel);

        setVisible(true);
        setResizable(true);

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
    public JButton create_button(JButton naam, String path) {
        naam = new JButton(""); // Knop die er voor zorgt dat de actuele toestand word opgeslagen.
        naam.setBorderPainted(false);
        naam.setContentAreaFilled(false);

        ImageIcon icon = new ImageIcon(this.getClass().getResource("/resources/"+path+".png"));
        Image img = icon.getImage();
        Image newimg = img.getScaledInstance(-5, schermbreedte/30,  java.awt.Image.SCALE_SMOOTH);
        ImageIcon newIcon = new ImageIcon(newimg);
        naam.setIcon(newIcon);
        //naam.setIcon(scaleImage(new ImageIcon(this.getClass().getResource("resources/"+path+".png")), schermbreedte/15, schermhoogte/20));
        naam.addActionListener(this);
        return naam;
    }
    public void activebutton(JButton knop, String active, String normal){
        // Deze functie zorgt ervoor dat als een knop is ingedrukt, deze iets van kleur veranderd, en na een 200 miliseconde
        // stop weer terug veranderd.
        ImageIcon icon = new ImageIcon(this.getClass().getResource("resources/"+active+".png"));
        Image img = icon.getImage();
        Image newimg = img.getScaledInstance(-5, schermbreedte/30,  java.awt.Image.SCALE_SMOOTH);
        ImageIcon newIcon = new ImageIcon(newimg);
        knop.setIcon(newIcon);

        Timer timer = new Timer( 200, t -> {
            ImageIcon icon2 = new ImageIcon(this.getClass().getResource("resources/"+normal+".png"));
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
            //Serveroptie server1= new Serveroptie(designpanel,"webserver1",88,9000,"webserver");
            //designpanel.add(server1);
            //designpanel.repaint();
            ServerDragAndDrop server1 = new DatabaseServer("WD10239",99.99,9000);
            ServerDragAndDrop server2 = new WebServer("WD10239",99.99,9000);
            ServerDragAndDrop server3 = new Firewall("WD10239",99.99,9000);
            Serveroptie optie1 = new Serveroptie(designpanel,"Hal3405",99,900,"webserver");

            server1.setBounds(10, 10, 100, 100);
            server2.setBounds(10, 150, 100, 100);
            server3.setBounds(schermbreedte/2, schermhoogte/2, 100, 100);
            optie1.setBounds(100, 10, 100, 100);
            
            designpanel.add(server3,server2);
            designpanel.add(server3,server1);

            designpanel.add(optie1);
            designpanel.repaint();

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
                designpanel.SetKleinScherm();
            } else {
                dispose();
                setExtendedState(JFrame.MAXIMIZED_BOTH);
                setUndecorated(true);
                isVolscherm = true;
                setVisible(true);
                JBvolscherm.setIcon(scaleImage(new ImageIcon(this.getClass().getResource("resources/smallbutton.png")), schermbreedte/15, schermhoogte/20));
                designpanel.SetGrootScherm();
            }
        }
    }

    public boolean getisVolscherm() {
        return isVolscherm;
    }
}

