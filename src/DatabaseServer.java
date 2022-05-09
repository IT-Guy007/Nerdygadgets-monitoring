import javax.swing.*;


public class DatabaseServer extends servers {
    public DatabaseServer(String naam, int prijs, boolean beschikbaarheid, int panelx, int panely){
        super(naam, prijs, beschikbaarheid, panelx,panely);
    }

    public DatabaseServer(String naam, int prijs, boolean beschikbaarheid){
        super(naam, prijs, beschikbaarheid);
    }
}
