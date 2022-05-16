import nerdygadgets.Design.DesignFrame;
import nerdygadgets.Monitoring.MonitoringFrame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainFrame extends JFrame implements ActionListener {
    //Main frame
    Dimension schermgrootte = Toolkit.getDefaultToolkit().getScreenSize();
    int schermhoogte = schermgrootte.height;
    int schermbreedte = schermgrootte.width;
    private JButton Nieuw_Ontwerp, monitoring, Openen_Ontwerp;

    // Applicatienaam (Mustafa)
    JLabel tekst_label = new JLabel("Nerdygadgets monitoring & ontwerpapplicatie©");

    public MainFrame() {

        Nieuw_Ontwerp = create_button(Nieuw_Ontwerp,"Nieuw Ontwerp_mainframe"); add(Nieuw_Ontwerp); // Nieuw ontwerp
        monitoring = create_button(monitoring, "Monitoring_mainframe"); add(monitoring); //Veld legen
        Openen_Ontwerp = create_button(monitoring, "Openen Ontwerp_mainframe"); add(monitoring); //Optimaliseren

        tekst_label.setBounds(520,200,600,40);
        tekst_label.setFont(new Font("HelveticaNeue-Light", Font.PLAIN, 24));
        tekst_label.setHorizontalAlignment(JLabel.CENTER);

        setTitle("Nerdygadgets monitoring");
//        setLayout(new FlowLayout());
//        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
//        setSize(300,75);
        setSize(schermbreedte/30*26,schermhoogte/30*26);
        //x y
        Nieuw_Ontwerp.setBounds(590,500,220,50);
        Nieuw_Ontwerp.setFocusable(false);
        Nieuw_Ontwerp.addActionListener(this);

        Openen_Ontwerp.setBounds(850,500,240,50);
        Openen_Ontwerp.setFocusable(false);
        Openen_Ontwerp.addActionListener(this);
        // x y
        monitoring.setBounds(720,400,220,50);
        monitoring.setFocusable(false);
        monitoring.addActionListener(this);

        add(Nieuw_Ontwerp);
        add(monitoring);
        add(tekst_label);
        add(Openen_Ontwerp);

        setLayout(null);
        setResizable(false);
        setVisible(true);


    }

    //▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼

    public JButton create_button(JButton naam, String path){
        naam = new JButton(""); // Knop die er voor zorgt dat de actuele toestand word opgeslagen.
        naam.setBorderPainted(false);
        naam.setContentAreaFilled(false);
        naam.setIcon(scaleImage(new ImageIcon(this.getClass().getResource("/resources/"+path+".png")), schermbreedte/15, schermhoogte/20));
        naam.addActionListener(this);
        return naam;
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

    public void activebutton(JButton knop, String active, String normal){
        knop.setIcon(scaleImage(new ImageIcon(this.getClass().getResource("/resources/" + active +".png")), schermbreedte/15, schermhoogte/20));
        Timer timer = new Timer( 100, t -> {
            knop.setIcon(scaleImage(new ImageIcon(this.getClass().getResource("/resources/" + normal +".png")), schermbreedte/15, schermhoogte/20));
        });
        timer.setRepeats( false );
        timer.start();
    }

    //▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲

    //▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼
    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() ==  Nieuw_Ontwerp) {
            setVisible(false);
            JFrame design = new JFrame();
            design = new DesignFrame();

            // center frame (Mustafa)
            Toolkit toolkit = Toolkit.getDefaultToolkit();
            Dimension size = toolkit.getScreenSize();
            design.setLocation(size.width/2 - design.getWidth()/2, size.height/2 - design.getHeight()/2);

        } else if(e.getSource() == monitoring) {
            setVisible(false);
            JFrame monitoring = new JFrame();
            monitoring = new MonitoringFrame();

            // center frame (Mustafa)
            Toolkit toolkit = Toolkit.getDefaultToolkit();
            Dimension size = toolkit.getScreenSize();
            monitoring.setLocation(size.width/2 - monitoring.getWidth()/2, size.height/2 - monitoring.getHeight()/2);
        }
    }

    //▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲

}
