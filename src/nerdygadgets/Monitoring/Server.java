package nerdygadgets.Monitoring;

public class Server {
    int serverID;
    String name;
    int price;
    double availability;
    int port;
    int storage;
    int server_kind;
    String ipadress;

    public Server(int serverID, String name, int price, double availability, int port, int storage, int server_kind, String ipadress) {
        this.serverID = serverID;
        this.name = name;
        this.price = price;
        this.availability = availability;
        this.port = port;
        this.storage = storage;
        this.server_kind = server_kind;
        this.ipadress = ipadress;
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
                ", ipadress='" + ipadress + '\'' +
                '}';
    }
}
