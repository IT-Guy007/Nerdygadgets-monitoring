package nerdygadgets.Design.components;

public class WebServer extends ServerDragAndDrop {

    public WebServer(String naam, double beschikbaarheid, double prijs, int panelx, int panely){
        super( naam, beschikbaarheid, prijs, panelx,panely);
    }

    public WebServer(String naam, double beschikbaarheid, double prijs){
        super( naam, beschikbaarheid, prijs);

    }
}
