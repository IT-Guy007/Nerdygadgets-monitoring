package nerdygadgets.Monitoring;

public class Server {
    private int serverID;
    private String name;
    private int price;
    private double availability;
    private double actual_availability;
    private int port;
    private int storage;
    private int server_kind;
    private String server_kind2;
    private String ipadress;
    private boolean up;
    private int subnet;
    private int ram;


    public Server(int serverID, String name, int price, double availability, int port, int storage, String server_kind, String ipadress, boolean up, double actual_availability, int subnet, int ram) {
        this.serverID = serverID;
        this.name = name;
        this.price = price;
        this.availability = availability;
        this.actual_availability = actual_availability;
        this.port = port;
        this.storage = storage;
        this.server_kind2 = server_kind;
        this.ipadress = ipadress;
        this.up = up;
        this.subnet = subnet;
        this.ram = ram;
    }

    public Server(int serverID, String ipadress, int port) {
        this.serverID = serverID;
        this.ipadress = ipadress;
        this.port = port;
    }

    @Override
    public String toString() {
        return "Server{" +
                "serverID=" + serverID +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", availability=" + availability +
                ", port=" + port +
                ", storage=" + storage +
                ", server_kind=" + server_kind +
                ", server_kind2='" + server_kind2 + '\'' +
                ", ipadress='" + ipadress + '\'' +
                ", up=" + up +
                '}';
    }

    public int getServerID() {
        return serverID;
    }

    public void setServerID(int serverID) {
        this.serverID = serverID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public double getAvailability() {
        return availability;
    }

    public void setAvailability(double availability) {
        this.availability = availability;
    }

    public double getActual_availability() {
        return actual_availability;
    }

    public void setActual_availability(double actual_availability) {
        this.actual_availability = actual_availability;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public int getStorage() {
        return storage;
    }

    public void setStorage(int storage) {
        this.storage = storage;
    }

    public int getServer_kind() {
        return server_kind;
    }

    public void setServer_kind(int server_kind) {
        this.server_kind = server_kind;
    }

    public String getServer_kind2() {
        return server_kind2;
    }

    public void setServer_kind2(String server_kind2) {
        this.server_kind2 = server_kind2;
    }

    public String getIpadress() {
        return ipadress;
    }

    public void setIpadress(String ipadress) {
        this.ipadress = ipadress;
    }

    public boolean isUp() {
        return up;
    }

    public void setUp(boolean up) {
        this.up = up;
    }

    public int getSubnet() {
        return subnet;
    }

    public void setSubnet(int subnet) {
        this.subnet = subnet;
    }

    public int getRam() {
        return ram;
    }

    public void setRam(int ram) {
        this.ram = ram;
    }
}
