package nerdygadgets.Design.components;

@SuppressWarnings("ALL")
public class DatabaseServer extends ServerDragAndDrop {
    public DatabaseServer(String naam, double beschikbaarheid, double prijs, int panelx, int panely){
        super(naam, beschikbaarheid, prijs, panelx,panely);
    }
    public DatabaseServer(int id,String naam, double beschikbaarheid, double prijs){
        super(id, naam, beschikbaarheid, prijs);

    }
}