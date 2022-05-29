package nerdygadgets;

import nerdygadgets.Monitoring.MonitoringFrame;

import javax.swing.*;
import java.util.concurrent.TimeUnit;

public class main {
    public static void main(String[] args) {
        JFrame main = new MainFrame();
        while (true) {
            MonitoringFrame.updateUptime();
            try {
                TimeUnit.SECONDS.sleep(30);
            } catch (Exception e) {
                System.out.println(e);
            }
        }
    }
}
