import javax.swing.*;

public abstract class servers {
    private String naam;
    private int prijs;
    private boolean beschikbaarheid;
    private int panelX, panelY;
    private JPanel parentPanel;


    public servers(JPanel parentPanel, String naam, int prijs, boolean beschikbaarheid, int panelx, int panely){
    this.naam = naam;
    this.prijs = prijs;
    this.beschikbaarheid = beschikbaarheid;
    this.panelX = panelX;
    this.panelY = panelY;
    }

    public servers(JPanel parentPanel, String naam, int prijs, boolean beschikbaarheid){

    }
    public void vernietigen(){

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

    public boolean isBeschikbaarheid() {
        return beschikbaarheid;
    }

    public void setBeschikbaarheid(boolean beschikbaarheid) {
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
