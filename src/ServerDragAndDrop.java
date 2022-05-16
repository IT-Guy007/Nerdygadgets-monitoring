import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

import static javax.swing.text.StyleConstants.setIcon;

public abstract class ServerDragAndDrop extends JLabel {
    private BufferedImage icon;
    private JPanel hoofdpanel;
    private String naam, type;
    private double beschikbaarheid, prijs;
    private volatile int screenx =0,screeny =0, myx =0,myy =0;
    private int x,y;
    private ImageIcon icoon;
    private Color transparent=new Color(1f,0f,0f,0f );

    public ServerDragAndDrop(String naam, double availability, double annualPrice){
        if (this instanceof Firewall){
            icoon = new ImageIcon(this.getClass().getResource("/resources/firewall.png"));
        }else if (this instanceof WebServer){
            icoon = new ImageIcon(this.getClass().getResource("/resources/server.png"));
        }else if (this instanceof DatabaseServer){
            icoon = new ImageIcon(this.getClass().getResource("/resources/database-icon.png"));
        }
        //setLayout(new GridBagLayout());
        JLabel label = new JLabel(naam);
        label.setBounds(0,0,100,100);
        label.setForeground(Color.black);
        setBounds(0,0,121,61);
        //setBackground(transparent);
        setIcon(icoon);
        add(label);
        //assignIconAndType();

    }
    public ServerDragAndDrop(String naam, double availability, double annualPrice, int panelx, int panely){}
    public void assignIconAndType(){
        try{
            // Determine icon and type
            if(this instanceof Firewall){
                icon = ImageIO.read(this.getClass().getResource("/recources/firewall.png"));
                type = "Firewall";
            } else if(this instanceof DatabaseServer){
                icon = ImageIO.read(this.getClass().getResource("/recources/database-icon.png"));
                type = "Database Server";
            } else if(this instanceof WebServer){
                icon = ImageIO.read(this.getClass().getResource("/recources/server.png"));
                type = "Web Server";
            } else {
                System.err.println("Invalid component type");
            }
        } catch(IOException e){
            System.err.println("File not found");
        } catch(Exception e){
            System.err.println("Something went wrong while loading the icons");
        }

        // Assign icon
        //setIcon(new ImageIcon(icon));
        //setBounds(getParentPanelWidth()/2, getParentPanelHeight()/2, 64, 64);
        setOpaque(false);
    }
}
