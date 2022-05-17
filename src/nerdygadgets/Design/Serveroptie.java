package nerdygadgets.Design;

import nerdygadgets.Design.components.DatabaseServer;
import nerdygadgets.Design.components.ServerDragAndDrop;
import nerdygadgets.Design.components.WebServer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Objects;

public class Serveroptie extends JButton implements ActionListener {
    private Designpanel hoofdpanel;
    private String naam, type;
    private double beschikbaarheid, prijs;
    private ImageIcon icon;
    private int x,y,width,height;
    private Color transparent=new Color(1f,0f,0f,0f );

    public Serveroptie(Designpanel parentPanel, String name, double availability, double annualPrice, String type){

        this.beschikbaarheid = availability;
        this.hoofdpanel = parentPanel;
        this.naam = name;
        this.prijs = annualPrice;
        this.type = type;
        toevoegenafbeelding();
        JLabel tekst = new JLabel();
        String myString = name + "\n" + availability*100 + "%, " + prijs + "€";
        tekst.setText("<html>" + myString.replaceAll("<","&lt;").replaceAll(">", "&gt;").replaceAll("\n", "<br/>") + "</html>");
        tekst.setBounds(20,0,121,61);
        add(tekst);
        setBackground(transparent);
        setBorderPainted(false);
        setVisible(true);
        addActionListener(this);
        repaintParentPanel();
    }
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

            ServerDragAndDrop server1 = new WebServer(naam, prijs, beschikbaarheid);
            server1.setBounds(randx, randy, 100, 125);
            hoofdpanel.add(hoofdpanel.getFrame().getFirewall(),server1);
            hoofdpanel.repaint();
        }else if(type == "databaseserver"){
            ServerDragAndDrop server1 = new DatabaseServer(naam, prijs, beschikbaarheid);
            server1.setBounds(randx, randy, 100, 125);
            hoofdpanel.add(hoofdpanel.getFrame().getFirewall(),server1);
            hoofdpanel.repaint();
        }
    }
}