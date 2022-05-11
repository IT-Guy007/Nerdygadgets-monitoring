import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Mainframe extends JFrame implements ActionListener {

    private boolean isVolscherm = false;
    Dimension schermgrootte = Toolkit.getDefaultToolkit().getScreenSize();
    int schermhoogte = schermgrootte.height;
    int schermbreedte = schermgrootte.width;

    public Mainframe() {
        setTitle("Nerdygadgets monitoring aplicatie");
        setLayout(new FlowLayout());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(schermbreedte/30*26,schermhoogte/30*26); //Maakt de groote van de gui de helft van de schermgrootte
    }
    @Override
    public void actionPerformed(ActionEvent e) {

    }
    //Main frame
}
