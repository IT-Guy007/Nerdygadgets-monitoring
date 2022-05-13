package nerdygadgets.Design.components;

import javax.swing.*;

public class Firewall extends servers {
    public Firewall(JPanel parentPanel, String naam, int prijs, double beschikbaarheid, int panelx, int panely){
        super(parentPanel, naam, prijs, beschikbaarheid, panelx,panely);
    }

    public Firewall(JPanel parentPanel, String naam, int prijs, double beschikbaarheid){
        super(parentPanel, naam, prijs, beschikbaarheid);
    }
}

