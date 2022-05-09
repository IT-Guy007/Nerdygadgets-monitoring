import java.util.ArrayList;

public class DesignFrame {
    private Firewall firewall;
    private ArrayList webServer = new ArrayList<WebServer>();
    private ArrayList databaseServer = new ArrayList<DatabaseServer>();
    private int maxServerBacktracking;
    private int[] WSCountPerSoort;
    private int[] DSCountPerSoort;
    private int WSCountTotaal;
    private int DSCountTotaal;
    private double WSAvaliablityArray;
    private double DSAvaliablityArray;
    private double gewensteBeschikbaarheid;
    private double minimaleKosten;
    private int ServerCount;
    private int[] WSgeoptimaliseerde;
    private int[] DSgeoptimaliseerde;

    public DesignFrame() {
    }
    public int BerekenBeschikbaarheid(){
        return 0;
    }
    public int BerekenKosten(){
        return 0;
    }
    public void Optimaliseer(){

    }
    public void OptimaliseerHuidig(){

    }
    public void Huidig(){

    }
}

