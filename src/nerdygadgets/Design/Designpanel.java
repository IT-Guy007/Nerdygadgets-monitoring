package nerdygadgets.Design;

import nerdygadgets.Design.components.*;
import java.awt.FontMetrics;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class Designpanel extends JPanel implements ComponentListener {
    private JFrame frame;
    private Dimension schermgrootte = Toolkit.getDefaultToolkit().getScreenSize();
    private int schermhoogte = schermgrootte.height;
    private int schermbreedte = schermgrootte.width;
    private ArrayList<servers> serversArray = new ArrayList<>();
    Firewall pfSense = new Firewall(this, "pfSense", 4000, 99.998, schermbreedte/2, schermhoogte/2);
    WebServer w1 = new WebServer(this, "HAL9001W", 2200, 80);
    WebServer w2 = new WebServer(this, "HAL9002W",  3200, 90);
    WebServer w3 = new WebServer(this, "HAL9003W",  5100, 95);
    WebServer w4 = new WebServer(this, "HAL9003W",  5100, 100);
    DatabaseServer db1 = new DatabaseServer(this, "HAL9001DB", 5100, 90);
    DatabaseServer db2 = new DatabaseServer(this, "HAL9002DB", 7700, 95);
    DatabaseServer db3 = new DatabaseServer(this, "HAL9003DB", 12200, 98);



    public Designpanel(JFrame frame) {
        this.frame = frame;
        this.frame.addComponentListener(this);
        setResponsiveSize();
        setBackground(Color.white);
        setLayout(null);
        repaint();
        setVisible(true);
        test();
    }
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setFont(new Font("Arial", Font.BOLD, 20));
        FontMetrics metrics = g.getFontMetrics();

        if (serverVoorwaardenCheck()) {
            g.setColor(Color.black);
            g.drawString("Prijs per jaar: €" + berekenTotalePrijs(), getWidth() - metrics.stringWidth("Prijs per jaar: €"+ berekenTotalePrijs()), 20);
            g.drawString("Beschikbaarheid: " + berekenTotaleBeschikbaarheid() + "%", getWidth() - metrics.stringWidth("Beschikbaarheid: " + berekenTotaleBeschikbaarheid()), 40);
        } else {
            g.setColor(Color.red);
            g.drawString("ZORG DAT ER EEN FIREWALL, DATABASE SERVER EN WEBSERVER ZIJN TOEGEVOEGD!",getWidth() - metrics.stringWidth("ZORG DAT ER EEN FIREWALL, DATABASE SERVER EN WEBSERVER ZIJN TOEGEVOEGD!"),20);
        }
    }
    @Override
    public void componentResized(ComponentEvent e) {setResponsiveSize();}
    public void setResponsiveSize() {
        setPreferredSize(new Dimension(frame.getWidth() - 25, frame.getHeight() - 100));
    }


    @Override
    public void componentMoved(ComponentEvent e) {

    }

    @Override
    public void componentShown(ComponentEvent e) {

    }

    @Override
    public void componentHidden(ComponentEvent e) {

    }
    public void test(){
        serversArray.add(w1);
        serversArray.add(db1);
        serversArray.add(pfSense);
    }
    public ArrayList<servers> getServersArray() {
        return serversArray;
    }

    public void setServersArray(ArrayList<servers> serversArray) {
        this.serversArray = serversArray;
    }


    public String berekenTotalePrijs() {
        double totalePrijs = 0;
        for (servers server : serversArray) {
            totalePrijs += server.getPrijs();
        }
        return removeTrailingZeros(totalePrijs);
    }

    public String berekenTotaleBeschikbaarheid() {
        double firewallBeschikbaarheid = 1;
        double webServerBeschikbaarheid = 1;
        double databaseBeschikbaarheid = 1;

        for (servers server : serversArray) {
                if (server instanceof Firewall) {
                    firewallBeschikbaarheid *= (1 - (server.getBeschikbaarheid() / 100));
                }else if (server instanceof WebServer) {
                    webServerBeschikbaarheid *= (1 - (server.getBeschikbaarheid() / 100));
                }else if (server instanceof DatabaseServer) {
                    databaseBeschikbaarheid *= (1 - (server.getBeschikbaarheid() / 100));
                }
        }
        double totaleBeschikbaarheid = (1 - firewallBeschikbaarheid) * (1 - webServerBeschikbaarheid) * (1 - databaseBeschikbaarheid);
        return removeTrailingZeros((double) Math.round((totaleBeschikbaarheid*100) * 1000d)/1000d);

    }
    public boolean serverVoorwaardenCheck(){
        boolean firewallCheck = false;
        boolean webCheck = false;
        boolean dbCheck = false;
        for (servers server : serversArray) {
            if (server instanceof Firewall) {
                firewallCheck = true;
            }
            if (server instanceof WebServer) {
                webCheck = true;
            }
            if (server instanceof DatabaseServer) {
                dbCheck = true;
            }
            if (firewallCheck && webCheck && dbCheck){
                return true;
            }} return false;
    }
    public String removeTrailingZeros(double number) {
        if (number % 1 == 0) {
            return String.format("%.0f", number);
        } else {
            return String.valueOf(number);
        }
    }
}
