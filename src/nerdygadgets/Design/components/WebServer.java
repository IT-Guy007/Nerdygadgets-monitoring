package nerdygadgets.Design.components;

public class WebServer extends ServerDragAndDrop {

    public WebServer(String naam, double beschikbaarheid, double prijs, int panelx, int panely){
        super( naam, beschikbaarheid, prijs, panelx,panely);
    }

    public WebServer(int id,String naam, double beschikbaarheid, double prijs){
        super(id, naam, beschikbaarheid, prijs);

    }
}
