package nerdygadgets.Design.components;

import java.util.ArrayList;

public class ServerLists {
    private ArrayList<ServerDragAndDrop> servers = new ArrayList<>();

    ServerLists() {
        DatabaseServer ServerOptie1 = new DatabaseServer("WD10239",99.99,0.8);
        DatabaseServer ServerOptie2 = new DatabaseServer("WD10240",130.4,0.85);
        DatabaseServer ServerOptie3 = new DatabaseServer("WD10241",2200,0.9);
        WebServer ServerOptie4 = new WebServer("HAL10239",99.99,0.8);
        WebServer ServerOptie5 = new WebServer("HAL10240",130.4,0.85);
        WebServer ServerOptie6 = new WebServer("HAL10241",2200,0.9);
        servers.add(ServerOptie4);servers.add(ServerOptie5);servers.add(ServerOptie6);
        servers.add(ServerOptie1);servers.add(ServerOptie2); servers.add(ServerOptie3);
    }

    public ArrayList<ServerDragAndDrop> getServers() {
        return servers;
    }
}
