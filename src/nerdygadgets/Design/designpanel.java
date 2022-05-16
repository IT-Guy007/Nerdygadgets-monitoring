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

import static java.lang.Math.round;

public class designpanel extends JPanel implements ComponentListener {
    private JFrame frame;
    private Dimension schermgrootte = Toolkit.getDefaultToolkit().getScreenSize();
    private int schermhoogte = schermgrootte.height;
    private int schermbreedte = schermgrootte.width;

    private ArrayList<ServerDragAndDrop> serversArray = new ArrayList<>();
    Firewall pfSense = new Firewall("pfSense", 4000, 99.998, schermbreedte/2, schermhoogte/2);
    WebServer w1 = new WebServer("HAL9001W", 2200, 80);
    WebServer w2 = new WebServer("HAL9002W",  3200, 90);
    WebServer w3 = new WebServer("HAL9003W",  5100, 95);
    WebServer w4 = new WebServer("HAL9003W",  5100, 100);
    DatabaseServer db1 = new DatabaseServer("HAL9001DB", 5100, 90);
    DatabaseServer db2 = new DatabaseServer("HAL9002DB", 7700, 95);
    DatabaseServer db3 = new DatabaseServer( "HAL9003DB", 12200, 98);



    public designpanel(JFrame frame) {
        this.frame = frame;
        this.frame.addComponentListener(this);
        setResponsiveSize();
        setBackground(Color.white);
        setLayout(null);
        repaint();
        setVisible(true);
        test();
        System.out.println("constructor");
    }
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setFont(new Font("Arial", Font.BOLD, 12));
        FontMetrics metrics = g.getFontMetrics();
        if (serverVoorwaardenCheck()) {
            g.setColor(Color.black);
            g.drawLine(getWidth() - metrics.stringWidth("Aantal Database Servers: " + countDBServers()) - 10,0,getWidth() - metrics.stringWidth("Aantal Database Servers: " + countDBServers()) - 10,getHeight());
            g.drawString("Aantal Database servers: " + countDBServers(), getWidth() - metrics.stringWidth("Aantal Database Servers: " + countDBServers()) - 5, 20);
            g.drawString("Aantal Web Servers : " + countWebServers(), getWidth() - metrics.stringWidth("Aantal Web Servers : " + countWebServers()) - 5, 40);
            g.drawString("Aantal PFSense Servers : 1", getWidth() - metrics.stringWidth("Aantal PFSense Servers : 1") - 5, 60);
            g.drawLine(getWidth() - metrics.stringWidth("Aantal Database Servers: " + countDBServers()) - 10,75, schermgrootte.width, 75);
            g.drawString("Prijs per jaar: €" + berekenTotalePrijs(), getWidth() - metrics.stringWidth("Prijs per jaar: €"+ berekenTotalePrijs()) - 5, 90);
            g.drawString("Beschikbaarheid: " + berekenTotaleBeschikbaarheid() + "%", getWidth() - metrics.stringWidth("Beschikbaarheid: " + berekenTotaleBeschikbaarheid() + "%") - 5, 110);
        } else {
            g.setColor(Color.red);
            g.drawString("ZORG DAT ER EEN FIREWALL, DATABASE SERVER EN WEBSERVER ZIJN TOEGEVOEGD!",getWidth() - metrics.stringWidth("ZORG DAT ER EEN FIREWALL, DATABASE SERVER EN WEBSERVER ZIJN TOEGEVOEGD!") - 5,20);
        }
    }
    @Override
    public void componentResized(ComponentEvent e) {SetKleinScherm();}
    public void setResponsiveSize() {
        setPreferredSize(new Dimension(frame.getWidth() - 25, frame.getHeight() - 100));
    }
    public void setvastesize(int width, int height){
        setPreferredSize(new Dimension(width - 40, height - 100));
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
        serversArray.add(w3);
        serversArray.add(db3);
    }
    public ArrayList<ServerDragAndDrop> getServersArray() {
        return serversArray;
    }

    public void setServersArray(ArrayList<ServerDragAndDrop> serversArray) {
        this.serversArray = serversArray;
    }

    public String berekenTotalePrijs() {
        double totalePrijs = 0;
        for (ServerDragAndDrop server : serversArray) {
            totalePrijs += server.getPrijs();
        }
        return removeTrailingZeros(totalePrijs);
    }
    public int countDBServers(){
        int i = 0;
        for (ServerDragAndDrop server : serversArray) {
            if (server instanceof DatabaseServer) {
                i++;
            }
        }  return i;
    }
    public int countWebServers(){
        int i = 0;
        for (ServerDragAndDrop server : serversArray) {
            if (server instanceof WebServer) {
                i++;
            }
        }  return i;
    }
    public String berekenTotaleBeschikbaarheid() {
        double firewallBeschikbaarheid = 1;
        double webServerBeschikbaarheid = 1;
        double databaseBeschikbaarheid = 1;

        for (ServerDragAndDrop server : serversArray) {
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
        for (ServerDragAndDrop server : serversArray) {
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

    public void SetGrootScherm() {
        setPreferredSize(new Dimension((int) round(0.99*schermbreedte), (int) round(0.92*schermhoogte) ));
        repaint();
    }
    public void SetKleinScherm(){
        setPreferredSize(new Dimension((int) round(0.98*(schermbreedte/30*26)),(int) round(0.78*(schermhoogte/30*26)) ));
        repaint();
    }
}
