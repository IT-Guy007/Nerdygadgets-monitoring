package nerdygadgets;

import nerdygadgets.Design.DesignFrame;
import nerdygadgets.Design.openDialog;
import nerdygadgets.Monitoring.ProjectFrame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static java.lang.Math.round;

@SuppressWarnings("ALL")
public class MainFrame extends JFrame implements ActionListener {
    //Main frame
    private Dimension schermgrootte = Toolkit.getDefaultToolkit().getScreenSize();
    private int schermhoogte = schermgrootte.height;
    private int schermbreedte = schermgrootte.width;
    private JButton Nieuw_Ontwerp, monitoring, Openen_Ontwerp;
    private JLabel tekst_label = new JLabel("Nerdygadgets monitoring & ontwerpapplicatie©");

    public MainFrame() {
        setTitle("Nerdygadgets monitoring");
        setLayout(new FlowLayout());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(schermbreedte/30*26,schermhoogte/30*26);

        Nieuw_Ontwerp = create_button(Nieuw_Ontwerp,"Nieuw Ontwerp_mainframe",(int) ((schermbreedte/30*26)/3.5),(int) (round((schermbreedte/30*26)/3)),220,50); add(Nieuw_Ontwerp); // Nieuw ontwerp
        monitoring = create_button(monitoring, "Monitoring_mainframe",(int) ((schermbreedte/30*26)/2.4),(int) (round((schermbreedte/30*26)/4)),220,50); add(monitoring); //Veld legen
        Openen_Ontwerp = create_button(Openen_Ontwerp, "Openen Ontwerp_mainframe",(int) ((schermbreedte/30*26)/1.8),(int) (round((schermbreedte/30*26)/3)),240,50); add(Openen_Ontwerp); //Optimaliseren

        tekst_label.setBounds((int) ((schermbreedte/30*26)/3.5), (int) (round((schermbreedte/30*26)/8)),600,40);
        tekst_label.setFont(new Font("HelveticaNeue-Light", Font.PLAIN, 24));
        tekst_label.setHorizontalAlignment(JLabel.CENTER);

        add(tekst_label);

        setLayout(null);
        setResizable(false);
        setVisible(true);


    }
    //Create button
    public JButton create_button(JButton naam, String path,int x,int y,int width,int height){
        naam = new JButton(""); // Knop die er voor zorgt dat de actuele toestand word opgeslagen.
        naam.setBorderPainted(false);
        naam.setBounds(x,y,width,height);
        naam.setFocusable(false);
        naam.setContentAreaFilled(false);
        naam.setIcon(scaleImage(new ImageIcon(this.getClass().getResource("/resources/"+path+".png")), schermbreedte/15, schermhoogte/20));
        naam.addActionListener(this);
        return naam;
    }

    //Image Icon
    public ImageIcon scaleImage(ImageIcon icon, int w, int h) {
        int nw = icon.getIconWidth();
        int nh = icon.getIconHeight();
        if(nh > h) {
            nh = h;
            nw = (icon.getIconWidth() * nh) / icon.getIconHeight();
        }
        return new ImageIcon(icon.getImage().getScaledInstance(nw, nh, Image.SCALE_DEFAULT));
    }

    //Active
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
        if(e.getSource() ==  Nieuw_Ontwerp) {
            setVisible(false);
            DesignFrame design = new DesignFrame(null);
            Toolkit toolkit = Toolkit.getDefaultToolkit();
            Dimension size = toolkit.getScreenSize();
            design.setLocation(size.width/2 - design.getWidth()/2, size.height/2 - design.getHeight()/2);
        } else if(e.getSource() == monitoring) {
            setVisible(false);
            ProjectFrame Framemonitoring = new ProjectFrame();
            Framemonitoring.setVisible(true);
            Toolkit toolkit = Toolkit.getDefaultToolkit();
            Dimension size = toolkit.getScreenSize();
            monitoring.setLocation(size.width/2 - monitoring.getWidth()/2, size.height/2 - monitoring.getHeight()/2);
        } else if(e.getSource() == Openen_Ontwerp) {
            setVisible(false);
            openDialog dialog = new openDialog();
            Toolkit toolkit = Toolkit.getDefaultToolkit();
            Dimension size = toolkit.getScreenSize();
            dialog.setLocation(size.width/2 - dialog.getWidth()/2, size.height/2 - dialog.getHeight()/2);
        }
    }

}