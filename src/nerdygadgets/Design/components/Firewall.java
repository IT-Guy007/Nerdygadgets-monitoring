package nerdygadgets.Design.components;

public class Firewall extends ServerDragAndDrop {
    public Firewall(String naam, double prijs, double beschikbaarheid, int panelx, int panely) {
        super(naam, prijs, beschikbaarheid, panelx, panely);
    }

    public Firewall(String naam, double prijs, double beschikbaarheid) {
        super(naam, beschikbaarheid, prijs);

    }
}