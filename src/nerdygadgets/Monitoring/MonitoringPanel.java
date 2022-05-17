package nerdygadgets.Monitoring;

import javax.swing.*;

import nerdygadgets.Design.components.ServerDragAndDrop;

public class MonitoringPanel extends JFrame
{
    private JFrame frame;
    private ServerDragAndDrop Servers[];
    private boolean cpuUsage[];
    private boolean memoryUsage[];
    private int websiteUptime;
    private int databaseUptime;
    private boolean WebsiteOnline;
    private boolean DatabaseOnline;

    public MonitoringPanel(ServerDragAndDrop server)
    {

    }

    public void update()
    {

    }
}
