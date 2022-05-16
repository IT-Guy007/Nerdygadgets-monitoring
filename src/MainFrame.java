import nerdygadgets.Design.DesignFrame;
import nerdygadgets.Monitoring.MonitoringFrame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainFrame extends JFrame implements ActionListener {
    //Main frame
    Dimension schermgrootte = Toolkit.getDefaultToolkit().getScreenSize();
    int schermhoogte = schermgrootte.height;
    int schermbreedte = schermgrootte.width;
    private JButton design, monitoring;

    public MainFrame() {
        System.out.println("test");
        setTitle("Nerdygadgets monitoring");
        setLayout(new FlowLayout());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(300,75);

        design = new JButton("Design");
        design.addActionListener(this);

        monitoring = new JButton("Monitoring");
        monitoring.addActionListener(this);
        add(design);
        add(monitoring);

        setVisible(true);
        setResizable(false);
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() ==  design) {
            setVisible(false);
            JFrame design = new JFrame();
            design = new DesignFrame();
            System.out.println("main");
        } else if(e.getSource() == monitoring) {
            setVisible(false);
            JFrame monitoring = new JFrame();
            monitoring = new MonitoringFrame();

        }
    }
}
