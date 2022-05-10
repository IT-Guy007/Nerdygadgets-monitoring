import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class DesignFrame extends JFrame implements ActionListener {
    private JButton JBopslaan;

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

    public DesignFrame() {
        setTitle("Nerdygadgets monitoring aplicatie");
        setLayout(new FlowLayout());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Dimension schermgrootte = Toolkit.getDefaultToolkit().getScreenSize();
        int schermhoogte = schermgrootte.height;
        int schermbreedte = schermgrootte.width;
        setSize(schermbreedte/3*2,schermhoogte/3*2); //Maakt de groote van de gui de helft van de schermgrootte


        JBopslaan = new JButton(""); // Knop die er voor zorgt dat de actuele toestand word opgeslagen.
        //JBopslaan.setPreferredSize(new Dimension(schermbreedte/20,schermhoogte/25));
        JBopslaan.setBorderPainted(false);
        JBopslaan.setContentAreaFilled(false);
        //JBopslaan.setOpaque(true);
        //JBopslaan.setBackground(Color.green);


        JBopslaan.setIcon(scaleImage(new ImageIcon(this.getClass().getResource("resources/button.png")), schermbreedte/15, schermhoogte/20));

        JBopslaan.addActionListener(this);
        add(JBopslaan);




        setVisible(true);
        //setResizable(false);
    }
    public ImageIcon scaleImage(ImageIcon icon, int w, int h)
    {
        int nw = icon.getIconWidth();
        int nh = icon.getIconHeight();

        if(icon.getIconWidth() > w)
        {
            nw = w;
            nh = (nw * icon.getIconHeight()) / icon.getIconWidth();
        }

        if(nh > h)
        {
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

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == JBopslaan) {
            Dimension schermgrootte = Toolkit.getDefaultToolkit().getScreenSize();
            int schermhoogte = schermgrootte.height;
            int schermbreedte = schermgrootte.width;
            JBopslaan.setIcon(scaleImage(new ImageIcon(this.getClass().getResource("resources/button-active.png")), schermbreedte/15, schermhoogte/20));
            System.out.println("hoi");
            Timer timer = new Timer( 200, t -> {
                JBopslaan.setIcon(scaleImage(new ImageIcon(this.getClass().getResource("resources/button.png")), schermbreedte/15, schermhoogte/20));
            });
            timer.setRepeats( false );
            timer.start();

        }
    }
}

