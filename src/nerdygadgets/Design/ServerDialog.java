package nerdygadgets.Design;

import javax.swing.*;
import java.awt.*;

public class ServerDialog extends JDialog {

    public ServerDialog(JFrame frame, boolean modal){
        super(frame, modal);
        setSize(400,600);
        setTitle("Serveropties wijzigen");
        setLayout(new FlowLayout());
        setVisible(true);
    }
}
