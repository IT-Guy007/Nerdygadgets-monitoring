package nerdygadgets.Monitoring;

import nerdygadgets.Design.components.ServerDragAndDrop;

import javax.swing.*;


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

    public void update()
    {

    }
}
