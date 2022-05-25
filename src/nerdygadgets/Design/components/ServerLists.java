package nerdygadgets.Design.components;

import java.util.ArrayList;

public class ServerLists {
    private ArrayList<ServerDragAndDrop> servers = new ArrayList<>();

    public ServerLists() {
        DatabaseServer ServerOptie1 = new DatabaseServer("WD10239",80,99.99);
        DatabaseServer ServerOptie2 = new DatabaseServer("WD10240",85,130.4);
        DatabaseServer ServerOptie3 = new DatabaseServer("WD10241",90,2200);
        WebServer ServerOptie4 = new WebServer("HAL10239",80,99.99);
        WebServer ServerOptie5 = new WebServer("HAL10240",85,130.4);
        WebServer ServerOptie6 = new WebServer("HAL10241",90,2200);
        servers.add(ServerOptie1);servers.add(ServerOptie2); servers.add(ServerOptie3);
        servers.add(ServerOptie4);servers.add(ServerOptie5);servers.add(ServerOptie6);

    }

    public String[] generateArray() {
        String[] serverArray = new String[servers.size()];
        for (int i = 0; i < servers.size(); i++) {
            serverArray[i] = servers.get(i).getNaam();
        }
        return serverArray;
    }

    public ArrayList<ServerDragAndDrop> getServers() {
        return servers;
    }
}
