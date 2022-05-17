package nerdygadgets.Monitoring;

import nerdygadgets.Design.components.servers;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MonitoringFrame extends JFrame implements ActionListener {
    private int maxiumservercount;
    private boolean isVolscherm = false;
    Dimension schermgrootte = Toolkit.getDefaultToolkit().getScreenSize();
    int schermhoogte = schermgrootte.height;
    int schermbreedte = schermgrootte.width;

    public MonitoringFrame(int projectID) {
        setTitle("Monitoring");
        setLayout(new FlowLayout());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(schermbreedte/30*26,schermhoogte/30*26); //Maakt de groote van de gui de helft van de schermgrootte
        setVisible(true);
    }

    public MonitoringFrame(servers server) {}

    @Override
    public void actionPerformed(ActionEvent e) {}
}
