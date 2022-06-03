package nerdygadgets;
import javax.swing.*;

import static nerdygadgets.Monitoring.MonitoringFrame.updateUptime;

@SuppressWarnings("ALL")
public class main {
    public static void main(String[] args) {
        JFrame main = new MainFrame();
        updateUptime();
    }
}
