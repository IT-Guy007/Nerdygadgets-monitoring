package nerdygadgets.Design.components;

import javax.swing.*;



public class DatabaseServer extends ServerDragAndDrop {
    public DatabaseServer(JPanel parentPanel, String naam, double prijs, double beschikbaarheid, int panelx, int panely){
        super(naam,beschikbaarheid, prijs, panelx,panely);
    }

    public DatabaseServer( String naam, double prijs, double beschikbaarheid){
        super( naam, beschikbaarheid, prijs);

    }

}
