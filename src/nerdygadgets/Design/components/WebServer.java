package nerdygadgets.Design.components;

public class WebServer extends ServerDragAndDrop {

    public WebServer(String naam, double prijs, double beschikbaarheid, int panelx, int panely){
        super( naam, prijs, beschikbaarheid, panelx,panely);
    }

    public WebServer(String naam, double prijs, double beschikbaarheid){
        super( naam, beschikbaarheid, prijs);

    }
}
