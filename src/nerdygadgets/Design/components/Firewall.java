package nerdygadgets.Design.components;

public class Firewall extends ServerDragAndDrop {
    public Firewall(String naam, double beschikbaarheid, double prijs, int panelx, int panely) {
        super(naam, beschikbaarheid, prijs, panelx, panely);
    }

    public Firewall(String naam, double beschikbaarheid, double prijs) {
        super(naam, beschikbaarheid, prijs);

    }
}