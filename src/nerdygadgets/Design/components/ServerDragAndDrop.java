package nerdygadgets.Design.components;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.UUID;

public abstract class ServerDragAndDrop extends JLabel {
    private BufferedImage icon;
    private JPanel hoofdpanel;
    private String naam, type;
    private double beschikbaarheid;
    private double prijs;
    private volatile int screenx =0,screeny =0, myx =0,myy =0;
    private int x,y;
    private ImageIcon icoon;
    private Color transparent=new Color(1f,0f,0f,0f );
    private String random_id = UUID.randomUUID().toString();
    private JLabel label;
    private int id=0;

    public ServerDragAndDrop(int id,String naam, double availability, double annualPrice){

        this.id=id;

        this.naam = naam;
        this.beschikbaarheid = availability;
        this.prijs = annualPrice;
        label = new JLabel();
        setText();


        if (this instanceof Firewall){
            icoon = new ImageIcon(this.getClass().getResource("/resources/firewall.png"));
            label.setBounds(10,100,100,25);
            label.setForeground(Color.black);
        }else if (this instanceof WebServer){
            icoon = new ImageIcon(this.getClass().getResource("/resources/server.png"));
            label.setBounds(10,100,100,25);
            label.setForeground(Color.black);
        }else if (this instanceof DatabaseServer){
            icoon = new ImageIcon(this.getClass().getResource("/resources/database-icon.png"));
            label.setBounds(10,100,100,25);
            label.setForeground(Color.black);
        }
        setBounds(0,0,121,61);
        setIcon(icoon);
        add(label);

    }
    public ServerDragAndDrop(String naam, double availability, double annualPrice, int panelx, int panely){}

    public double getPrijs() {
        return prijs;
    }

    public double getBeschikbaarheid() {
        return beschikbaarheid;
    }

    public String getNaam() {
        return naam;
    }

    public String getRandom_id() {
        return random_id;
    }

    public void setNaam(String naam) {
        this.naam = naam;
    }

    public void setBeschikbaarheid(double beschikbaarheid) {
        this.beschikbaarheid = beschikbaarheid;
    }

    public void setPrijs(double prijs) {
        this.prijs = prijs;
    }
    public void setText(){
        String myString = "   "+naam + "\n" + beschikbaarheid + "%, " + prijs + "€";
        label.setText("<html>" + myString.replaceAll("<","&lt;").replaceAll(">", "&gt;").replaceAll("\n", "<br/>") + "</html>");
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

}