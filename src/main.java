import javax.swing.*;
import java.awt.*;

public class main {
    public static void main(String[] args) {
        JFrame main = new JFrame();
        main = new MainFrame();

        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension size = toolkit.getScreenSize();
        main.setLocation(size.width/2 - main.getWidth()/2, size.height/2 - main.getHeight()/2);

    }
}
