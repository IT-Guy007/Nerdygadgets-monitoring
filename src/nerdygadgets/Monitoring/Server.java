package nerdygadgets.Monitoring;

public class Server {
    int serverID;
    String name;
    int price;
    double availability;
    double actual_availability;
    int port;
    int storage;
    int server_kind;
    String server_kind2;
    String ipadress;
    boolean up;
    int subnet;
    int ram;

    public Server(int serverID, String name, int price, double availability, int port, int storage, String server_kind, String ipadress, boolean up, double actual_availability, int subnet) {
        this.serverID = serverID;
        this.name = name;
        this.price = price;
        this.availability = availability;
        this.port = port;
        this.storage = storage;
        this.server_kind2 = server_kind;
        this.ipadress = ipadress;
        this.up = up;
        this.actual_availability = actual_availability;
        this.subnet = subnet;
    }

    //With RAM
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
}
