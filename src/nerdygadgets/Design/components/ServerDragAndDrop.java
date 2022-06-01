package nerdygadgets.Design.components;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.UUID;

@SuppressWarnings("ALL")
public abstract class ServerDragAndDrop extends JLabel {
    private BufferedImage icon;
    private JPanel hoofdpanel;
    private String naam, type,random_id = UUID.randomUUID().toString();
    private double beschikbaarheid, prijs;
    private volatile int screenx =0,screeny =0, myx =0,myy =0;
    private int x,y,id=0;
    private ImageIcon icoon;
    private Color transparent=new Color(1f,0f,0f,0f );
    private JLabel label;

    // Constructors
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
            System.out.println("Ik ben een webserver");
        }else if (this instanceof DatabaseServer){
            icoon = new ImageIcon(this.getClass().getResource("/resources/database-icon.png"));
            label.setBounds(10,100,100,25);
            label.setForeground(Color.black);
            System.out.println("Ik ben een database"+naam+" | "+beschikbaarheid+" | "+prijs+" | ");
        }
        setBounds(0,0,121,61);
        setIcon(icoon);
        add(label);

    }
    public ServerDragAndDrop(String naam, double availability, double annualPrice, int panelx, int panely){}

    // Getters en Setters
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
        String myString = "   "+this.naam + "\n" + this.beschikbaarheid + "%, " + this.prijs + "€";
        label.setText("<html>" + myString.replaceAll("<","&lt;").replaceAll(">", "&gt;").replaceAll("\n", "<br/>") + "</html>");
        System.out.println(myString);
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

}