package nerdygadgets.Design.components;

import java.util.ArrayList;

public class ServerLists {
    private ArrayList<ServerDragAndDrop> servers = new ArrayList<>();

<<<<<<< HEAD
    public ServerLists(DesignFrame hoofdframe) {
        this.hoofdframe = hoofdframe;
        DatabaseServer ServerOptie1 = new DatabaseServer(0,"WD10239",80,99.99);
        DatabaseServer ServerOptie2 = new DatabaseServer(0,"WD10240",85,130.4);
        DatabaseServer ServerOptie3 = new DatabaseServer(0,"WD10241",90,2200);
        WebServer ServerOptie4 = new WebServer(0,"HAL10239",80,99.99);
        WebServer ServerOptie5 = new WebServer(0,"HAL10240",85,130.4);
        WebServer ServerOptie6 = new WebServer(0,"HAL10241",90,2200);

=======
    public ServerLists() {
        DatabaseServer ServerOptie1 = new DatabaseServer("HAL9001DB",90,5100);
        DatabaseServer ServerOptie2 = new DatabaseServer("HAL9002DB",95,7700);
        DatabaseServer ServerOptie3 = new DatabaseServer("HAL9003DB",98,12200);
        WebServer ServerOptie4 = new WebServer("HAL9001W",2200,80);
        WebServer ServerOptie5 = new WebServer("HAL9002W",3200,90);
        WebServer ServerOptie6 = new WebServer("HAL9003W",5100,95);
>>>>>>> parent of 9b65e0d (Merge branch 'Tobias' into Bryan)
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
