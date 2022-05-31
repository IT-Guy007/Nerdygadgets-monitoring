package nerdygadgets.Design;

import nerdygadgets.Design.components.DatabaseServer;
import nerdygadgets.Design.components.Firewall;
import nerdygadgets.Design.components.ServerDragAndDrop;
import nerdygadgets.Design.components.WebServer;
import nerdygadgets.Design.DesignFrame;
import nerdygadgets.MainFrame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.*;
import java.sql.*;
import java.util.ArrayList;

import javax.swing.SwingUtilities;
import javax.swing.Timer;

public class OpslaanDialog extends JDialog implements ActionListener {

    private DesignPanel designpanel;
    private DesignFrame designFrame;

    private Timer timer;

    JButton opslaan = new JButton("Opslaan");
    JTextField tekstveld = new JTextField(15);
//    JLabel success = new JLabel("Uw design is succesvol opgeslagen.");
//    JLabel failed = new JLabel("Voer een unieke naam in!");
    JLabel success = new JLabel("Uw design is succesvol opgeslagen!");
    JLabel failed = new JLabel("Voer een unieke naam in!");

    public OpslaanDialog(DesignPanel designpanel){

        this.designpanel = designpanel;

        //(Mustafa)
        JDialog OpslaanDialog = new JDialog();
        OpslaanDialog.setTitle("Sla project op");

        OpslaanDialog.setSize(300,130);
        OpslaanDialog.setLayout(new FlowLayout());

        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension size = toolkit.getScreenSize();
        OpslaanDialog.setLocation(size.width/2 - OpslaanDialog.getWidth()/2, size.height/2 - OpslaanDialog.getHeight()/2);

        opslaan.addActionListener(this);
        OpslaanDialog.add(tekstveld);
        OpslaanDialog.add(opslaan);
        OpslaanDialog.add(failed);
        OpslaanDialog.add(success);

        failed.setForeground(Color.red);

        failed.setVisible(false);
        success.setVisible(false);

        OpslaanDialog.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == opslaan){

            save();

        }

    }

    public void save() {

        String setupID = tekstveld.getText(); //TODO Via dialoog ff hier een unique "filename meegeven"
        boolean unique = true;
        try {
            Connection conn = DriverManager.getConnection(
                    "jdbc:mysql://192.168.1.103:3306/application",
                    "group4", "Qwerty1@");
            Statement stmt = conn.createStatement();
            String uniqueQuery = "SELECT setupId from serverSetups";
            ResultSet rset = stmt.executeQuery(uniqueQuery);

            while (rset.next()) {
                if (rset.getString("setupID").equals(setupID)) {
                    unique = false;
                }
            }
//            if (unique && tekstveld.getText() != "") {
            if (unique) {
                failed.setVisible(false);
                success.setVisible(true);

                for (ServerDragAndDrop server : designpanel.getServersArray_ArrayList()) {
                    if (server instanceof WebServer) {
                        String query = "INSERT INTO serverSetups(name, type, beschikbaarheid, prijs, setupId) VALUES ('" + server.getNaam() + "', '" + "webserver" + "', " + server.getBeschikbaarheid() + ", " + server.getPrijs() + ", +'" + setupID + "');";
                        stmt.executeUpdate(query);
                    } else if (server instanceof DatabaseServer) {
                        String query = "INSERT INTO serverSetups(name, type, beschikbaarheid, prijs, setupId) VALUES ('" + server.getNaam() + "', '" + "databaseserver" + "', " + server.getBeschikbaarheid() + ", " + server.getPrijs() + ", +'" + setupID + "');";
                        stmt.executeUpdate(query);
                    } else if (server instanceof Firewall) {
                        String query = "INSERT INTO serverSetups(name, type, beschikbaarheid, prijs, setupId) VALUES ('" + server.getNaam() + "', '" + "firewall" + "', " + server.getBeschikbaarheid() + ", " + server.getPrijs() + ", +'" + setupID + "');";
                        stmt.executeUpdate(query);
                    }

                    if (!(unique)){
                        success.setVisible(false);
                    }

                }
            } else {
                    success.setVisible(false);
                    failed.setVisible(true);

            }


        } catch (SQLException ex) {
            System.out.println(ex);

        }
    }
}
