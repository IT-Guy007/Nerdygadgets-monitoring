package nerdygadgets.Design.components;
import java.awt.Component.*;
import javax.swing.*;

public abstract class servers {
    private String naam;
    private int prijs;
    private double beschikbaarheid;
    private int panelX, panelY;
    private JPanel parentPanel;


    public servers(JPanel parentPanel, String naam, int prijs, double beschikbaarheid, int panelx, int panely){
    this.naam = naam;
    this.prijs = prijs;
    this.beschikbaarheid = beschikbaarheid;
    this.panelX = panelx;
    this.panelY = panely;
    }

    public servers(JPanel parentPanel, String naam, int prijs, double beschikbaarheid){
        this.naam = naam;
        this.prijs = prijs;
        this.beschikbaarheid = beschikbaarheid;
    }
    public void vernietigen(){
        System.out.println("jemoeder");
    }
    public void declareerType(){

    }
    public void getHoofdframeWidth(){

    }
    public void getHoofdframeHeight(){

    }

    public String getNaam() {
        return naam;
    }

    public void setNaam(String naam) {
        this.naam = naam;
    }

    public int getPrijs() {
        return prijs;
    }

    public void setPrijs(int prijs) {
        this.prijs = prijs;
    }

    public double getBeschikbaarheid() {
        return beschikbaarheid;
    }

    public void setBeschikbaarheid(double beschikbaarheid) {
        this.beschikbaarheid = beschikbaarheid;
    }

    public int getPanelX() {
        return panelX;
    }

    public void setPanelX(int panelX) {
        this.panelX = panelX;
    }

    public int getPanelY() {
        return panelY;
    }

    public void setPanelY(int panelY) {
        this.panelY = panelY;
    }

    public JPanel getParentPanel() {
        return parentPanel;
    }

    public void setParentPanel(JPanel parentPanel) {
        this.parentPanel = parentPanel;
    }
    public void opnieuwTekenen() {

    }

    @Override
    public String toString() {
        return "servers{" +
                "naam='" + naam + '\'' +
                ", prijs=" + prijs +
                ", beschikbaarheid=" + beschikbaarheid +
                ", panelX=" + panelX +
                ", panelY=" + panelY +
                ", parentPanel=" + parentPanel +
                '}';
    }
}
