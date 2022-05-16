package nerdygadgets.Design;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;

public class Serveroptie extends JLabel{
    private JPanel hoofdpanel;
    private String naam, type;
    private double beschikbaarheid, prijs;
    private BufferedImage icon;
    private int x,y,width,height;

    public Serveroptie(JPanel parentPanel, String name, double availability, double annualPrice, String type){
        this.beschikbaarheid = availability;
        this.hoofdpanel = parentPanel;
        this.naam = name;
        this.prijs = annualPrice;
        this.type = type;
        toevoegenafbeelding();
        setLocation(30,30);
        repaintParentPanel();

        addMouseMotionListener(new MouseMotionListener() {
            @Override
            public void mouseDragged(MouseEvent e) {
                try {
                    icon = ImageIO.read(Objects.requireNonNull(this.getClass().getResource("/recources/server.png")));
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                setIcon(new ImageIcon(String.valueOf(icon)));
                setBounds(getParentPanelWidth()/2, getParentPanelHeight()/2, 64, 64);
                repaintParentPanel();
            }

            @Override
            public void mouseMoved(MouseEvent e) {

            }
        });
        setVisible(true);
    }
    public void toevoegenafbeelding(){
        try{
            // Determine icon and type
            if(type.equals("firewall")){
                icon = ImageIO.read(Objects.requireNonNull(this.getClass().getResource("/recources/Server-rood.png")));
                type = "Firewall";
            }else if(type.equals("database")){
                icon = ImageIO.read(Objects.requireNonNull(this.getClass().getResource("/recources/Server-groen.png")));
                type = "Database Server";
            }else if (type.equals("webserver")){
                icon = ImageIO.read(Objects.requireNonNull(this.getClass().getResource("/resources/Server-blauw.png")));
                type = "Web Server";
            }else {
                System.err.println("Invalid component type");
            }
        } catch(Exception e){
            System.err.println("Something went wrong while loading the icons");
        }

        // Assign icon
        setIcon(new ImageIcon(String.valueOf(icon)));
        //setBounds(getParentPanelWidth()/2, getParentPanelHeight()/2, 64, 64);
        //setBackground(Color.green);
        setOpaque(false);
        //setVisible(true);
        repaintParentPanel();
    }
    public int getParentPanelWidth(){
        return hoofdpanel.getWidth();
    }
    public int getParentPanelHeight(){
        return hoofdpanel.getHeight();
    }
    public void repaintParentPanel(){
        hoofdpanel.repaint();
    }
}
