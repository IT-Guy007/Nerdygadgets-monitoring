import javax.swing.*;
import java.io.Serial;

public class main {
    public static void main(String[] args) {
        JPanel panel = new JPanel();
        servers a = new WebServer(panel, "jemoeder", 69, true,12,12);
        System.out.println(a);
        System.out.println(a.getPrijs());

    }
}
