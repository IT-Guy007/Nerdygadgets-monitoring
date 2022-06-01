package nerdygadgets.Design;

import nerdygadgets.Design.components.DatabaseServer;
import nerdygadgets.Design.components.ServerDragAndDrop;
import nerdygadgets.Design.components.WebServer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Objects;

import static java.lang.Math.round;

@SuppressWarnings("ALL")
public class ServerOptie extends JButton implements ActionListener {
    private DesignPanel hoofdpanel;
    private String naam, type;
    private double beschikbaarheid, prijs;
    private ImageIcon icon;
    private Color transparent=new Color(1f,0f,0f,0f );
    private int id;

    // Constructor
    public ServerOptie(DesignPanel parentPanel, String name, double availability, double annualPrice, String type){
        int min = 0;
        int max = 10000000;

        int random_int = (int)Math.floor(Math.random()*(max-min+1)+min);
        this.id = random_int;

        this.beschikbaarheid = availability;
        this.hoofdpanel = parentPanel;
        this.naam = name;
        this.prijs = annualPrice;
        this.type = type;
        toevoegenafbeelding();
        JLabel tekst = new JLabel();
        String myString = name + "\n" + availability + "%, " + "€" + prijs;
        tekst.setText("<html>" + myString.replaceAll("<","&lt;").replaceAll(">", "&gt;").replaceAll("\n", "<br/>") + "</html>");
        tekst.setBounds(20,0,121,61);
        add(tekst);
        setBackground(transparent);
        setBorderPainted(false);
        setVisible(true);
        addActionListener(this);
        MouseAdapter ma = new MouseAdapter() {
            private Component dragComponent;
            private Point offset;
            int counter =0;
            ServerDragAndDrop server1 = null;
            public void mousePressed(MouseEvent e) {
                // Deze functie zorgt ervoor dat als je op een knop rechtermuis klikt, deze word aangeroepen om te verdwijnen
                Component component = getComponentAt(e.getPoint());
                if(e.getButton() == MouseEvent.BUTTON3){}else{
                    dragComponent = component;
                    Point clickPoint = e.getPoint();
                    int deltaX = clickPoint.x - dragComponent.getX();
                    int deltaY = clickPoint.y - dragComponent.getY();
                    offset = new Point(deltaX, deltaY);
                }
            }
            @Override
            public void mouseDragged(MouseEvent e) {
                if (counter==0){
                    if (type=="databaseserver"){
                        System.out.println("Ik genereer een database met als naam: "+naam+" | "+beschikbaarheid+" | "+prijs);
                        server1 = new DatabaseServer(getId(),naam, beschikbaarheid,prijs);
                    }else{
                        System.out.println("Ik genereer een webserver met als naam: "+naam+" | "+beschikbaarheid+" | "+prijs);
                        server1 = new WebServer(getId(),naam, beschikbaarheid,prijs);
                    }
                    server1.setBounds(10, hoofdpanel.getFrame().returnyhoogte(name), 125, 140);
                    server1.setLocation(10, e.getY());
                    hoofdpanel.add(hoofdpanel.getFrame().getFirewall(),server1);
                    hoofdpanel.addArrayList(server1);
                }
                counter++;
                int mouseX = e.getX();
                int mouseY = e.getY();
                int screenX = e.getXOnScreen();
                int screenY = e.getYOnScreen();
                int xDelta;
                int yDelta;
                try {
                    if (!hoofdpanel.getFrame().getisVolscherm()) {
                        xDelta = (int) (screenX - offset.x - Math.round(0.06463541666666666666666666666667 * hoofdpanel.getFrame().getSchermbreedte()));//- 130;
                        yDelta = (int) (screenY - offset.y - Math.round(0.28935185185185185185185185185185 * hoofdpanel.getFrame().getSchermhoogte()));// -250;
                    }else{
                        xDelta = screenX - offset.x;
                        yDelta = (int) (screenY - offset.y - Math.round(0.22935185185185185185185185185185 * hoofdpanel.getFrame().getSchermhoogte()));
                    }
                }catch (Exception FE){
                    xDelta = mouseX - 0;
                    yDelta = mouseY - 0;
                }
                server1.setLocation(xDelta,yDelta);
                repaintParentPanel();
            }
            public void mouseReleased(MouseEvent mld) {
                //int screenX = mld.getXOnScreen();
                //int screenY = mld.getYOnScreen();
                //if (screenX<140){
                //    parentPanel.remove(server1);
                //}
                counter=0;
            }
        };
        addMouseListener(ma);
        addMouseMotionListener(ma);
        repaintParentPanel();
    }

    // Genereren van serveroptie
    public void toevoegenafbeelding(){
        if (type=="webserver"){
            icon= new ImageIcon(Objects.requireNonNull(this.getClass().getResource("/resources/Server-blauw.png")));
        }else if(type =="databaseserver"){
            icon= new ImageIcon(Objects.requireNonNull(this.getClass().getResource("/resources/Server-groen.png")));
        }else{
            icon= new ImageIcon(Objects.requireNonNull(this.getClass().getResource("/resources/Server-rood.png")));
        }
        setIcon(icon);
        setOpaque(false);
    }
    public void repaintParentPanel(){
        hoofdpanel.repaint();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        int maxx;
        if (hoofdpanel.getFrame().getisVolscherm()){
            maxx = hoofdpanel.getFrame().getSchermbreedte() -280;
        }else{
            maxx = hoofdpanel.getFrame().getSchermbreedte()/36*26;
        }
        int minx = 140;
        int range = maxx - minx + 1;
        int randx = (int)(Math.random() * range) + minx;

        int maxy;
        if (hoofdpanel.getFrame().getisVolscherm()){
            maxy = hoofdpanel.getFrame().getSchermhoogte() -180;
        }else{
            maxy = hoofdpanel.getFrame().getSchermhoogte()/41*26;
        }
        int miny = 0;
        int rangey = maxy - miny + 1;
        int randy = (int)(Math.random() * rangey) + miny;
        if (type == "webserver") {

            ServerDragAndDrop server1 = new WebServer(this.id,naam,beschikbaarheid, prijs);
            server1.setBounds(randx, randy, 125, 125);
            hoofdpanel.addArrayList(server1);
        }else if(type == "databaseserver"){
            ServerDragAndDrop server1 = new DatabaseServer(this.id,naam,beschikbaarheid, prijs);
            server1.setBounds(randx, randy, 125, 125);
            hoofdpanel.addArrayList(server1);
        }
        for (ServerDragAndDrop server : hoofdpanel.getServersArray_ArrayList()){
            hoofdpanel.add(hoofdpanel.getFrame().getFirewall(),server);
            hoofdpanel.repaint();
        }

    }

    // Getters en Setters
    public double getBeschikbaarheid() {
        return beschikbaarheid;
    }
    public double getPrijs() {
        return prijs;
    }
    public int getId() {
        return this.id;
    }
    public String getType() {
        return type;
    }
    public DesignPanel getHoofdpanel() {
        return hoofdpanel;
    }
    public void setHoofdpanel(DesignPanel hoofdpanel) {
        this.hoofdpanel = hoofdpanel;
    }
    public String getNaam() {
        return naam;
    }
    public void setNaam(String naam) {
        this.naam = naam;
    }
    public void setType(String type) {
        this.type = type;
    }
    public void setBeschikbaarheid(double beschikbaarheid) {
        this.beschikbaarheid = beschikbaarheid;
    }
    public void setPrijs(double prijs) {
        this.prijs = prijs;
    }
    @Override
    public ImageIcon getIcon() {
        return icon;
    }
    public void setIcon(ImageIcon icon) {
        this.icon = icon;
    }
    public Color getTransparent() {
        return transparent;
    }
    public void setTransparent(Color transparent) {
        this.transparent = transparent;
    }
    public void setId(int id) {
        this.id = id;
    }
}