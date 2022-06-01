package nerdygadgets.Design.components;

@SuppressWarnings("ALL")
public class WebServer extends ServerDragAndDrop {
    public WebServer(String naam, double prijs, double beschikbaarheid, int panelx, int panely){
        super( naam, prijs, beschikbaarheid, panelx,panely);
    }
    public WebServer(int id,String naam, double beschikbaarheid, double prijs){
        super(id, naam, beschikbaarheid, prijs);

    }
}
