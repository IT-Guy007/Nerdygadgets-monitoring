package nerdygadgets.Design.components;

import javax.swing.*;



public class DatabaseServer extends ServerDragAndDrop {
    public DatabaseServer(String naam, double beschikbaarheid, double prijs, int panelx, int panely){
        super(naam, beschikbaarheid, prijs, panelx,panely);
    }

    public DatabaseServer( String naam, double beschikbaarheid, double prijs){
        super( naam, beschikbaarheid, prijs);

    }

}