package nerdygadgets.Design.components;

public class Firewall extends ServerDragAndDrop {
    public Firewall(String naam, double prijs, double beschikbaarheid, int panelx, int panely) {
        super(naam, prijs, beschikbaarheid, panelx, panely);
    }

<<<<<<< HEAD
    public Firewall(String naam, double beschikbaarheid, double prijs) {
        super(0,naam, beschikbaarheid, prijs);
=======
    public Firewall(String naam, double prijs, double beschikbaarheid) {
        super(naam, beschikbaarheid, prijs);

>>>>>>> parent of 9b65e0d (Merge branch 'Tobias' into Bryan)
    }
}