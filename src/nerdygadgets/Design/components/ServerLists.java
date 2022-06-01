package nerdygadgets.Design.components;

import nerdygadgets.Design.DesignFrame;
import nerdygadgets.Design.ServerOptie;

import java.util.ArrayList;

public class ServerLists {
    private ArrayList<ServerDragAndDrop> servers = new ArrayList<>();
    private DesignFrame hoofdframe;

    public ServerLists(DesignFrame hoofdframe) {
        this.hoofdframe = hoofdframe;
        DatabaseServer ServerOptie1 = new DatabaseServer(0,"HAL9001DB",90,5100);
        DatabaseServer ServerOptie2 = new DatabaseServer(0,"HAL9002DB",95,7700);
        DatabaseServer ServerOptie3 = new DatabaseServer(0,"HAL9003DB",98,12200);
        WebServer ServerOptie4 = new WebServer(0,"HAL9001W",80,2200);
        WebServer ServerOptie5 = new WebServer(0,"HAL9002W",90,3200);
        WebServer ServerOptie6 = new WebServer(0,"HAL9003W",95,5100);
        servers.add(ServerOptie1);servers.add(ServerOptie2); servers.add(ServerOptie3);
        servers.add(ServerOptie4);servers.add(ServerOptie5);servers.add(ServerOptie6);
        /*
        for (ServerOptie serverOptie:this.hoofdframe.getTempServerOpties()){
            if (serverOptie.getName().equals("WD10239")&&serverOptie.getBeschikbaarheid() == 80&&serverOptie.getPrijs()==99.99){
                DatabaseServer ServerOptie1 = new DatabaseServer(serverOptie.getId(),"WD10239",80,99.99);
                servers.add(ServerOptie1);
            }else if (serverOptie.getName().equals("WD10240")&&serverOptie.getBeschikbaarheid() == 85&&serverOptie.getPrijs()==130.4){
                DatabaseServer ServerOptie1 = new DatabaseServer(serverOptie.getId(),"WD10240",85,130.4);
                servers.add(ServerOptie1);
            }else if (serverOptie.getName().equals("WD10241")&&serverOptie.getBeschikbaarheid() == 90&&serverOptie.getPrijs()==2200){
                DatabaseServer ServerOptie1 = new DatabaseServer(serverOptie.getId(),"WD10241",90,2200);
                servers.add(ServerOptie1);
            }else if (serverOptie.getName().equals("HAL10239")&&serverOptie.getBeschikbaarheid() == 80&&serverOptie.getPrijs()==99.99){
                WebServer ServerOptie1 = new WebServer(serverOptie.getId(),"HAL10239",80,99.99);
                servers.add(ServerOptie1);
            }else if (serverOptie.getName().equals("HAL10240")&&serverOptie.getBeschikbaarheid() == 85&&serverOptie.getPrijs()==130.4){
                WebServer ServerOptie1 = new WebServer(serverOptie.getId(),"HAL10239",85,130.4);
                servers.add(ServerOptie1);
            }else if (serverOptie.getName().equals("HAL10241")&&serverOptie.getBeschikbaarheid() == 90&&serverOptie.getPrijs()==2200){
                WebServer ServerOptie1 = new WebServer(serverOptie.getId(),"HAL10239",90,2200);
                servers.add(ServerOptie1);
            }
        }

         */

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

    public void setServers(ArrayList<ServerDragAndDrop> servers) {
        this.servers = servers;
    }

    public int countWebServers() {
        int webCount = 0;
        for (ServerDragAndDrop s : servers) {
            if (s instanceof WebServer) {
                webCount++;
            }
        }
        return webCount;
    }

    public int countDbServers() {
        int dbCount = 0;
        for (ServerDragAndDrop s : servers) {
            if (s instanceof DatabaseServer) {
                dbCount++;
            }
        }
        return dbCount;
    }

}


