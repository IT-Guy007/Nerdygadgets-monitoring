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

        OpslaanDialog.setSize(300,100);
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
            String uniqueQuery = "SELECT name from project";
            ResultSet rset = stmt.executeQuery(uniqueQuery);
            System.out.println("test1");
            while(rset.next()) {
                if (rset.getString("name").equals(setupID)) {
                    System.out.println("test2");
                    unique = false;
                }
            }


             if (unique) {
                 String query5 = "INSERT INTO project(name,wanted_Availability) VALUES ('"+setupID+"',100.0)";
                 stmt.executeUpdate(query5);
                 System.out.println("test3");
                 String query3 = "SELECT projectID from project where name = '"+setupID+"'";
                 ResultSet rset3 = stmt.executeQuery(query3);
                 String projectID="0";
                 while(rset3.next()) {
                     projectID = rset3.getString("projectID");
                 }
                for (ServerOptie opties: tempServerOpties){
                    try {
                        if (opties.getType().equals("webserver")) {
                            String query2 = "INSERT INTO serverSetups(name,type,price,availability,setupID) VALUES ('" + opties.getName() + "','webserver'," + opties.getPrijs() + "," + opties.getBeschikbaarheid() + ",'"+setupID+"')";
                            stmt.executeUpdate(query2);
                        } else if (opties.getType().equals("databaseserver")) {
                            String query2 = "INSERT INTO serverSetups(name,type,price,availability,setupID) VALUES ('" + opties.getName() + "','databaseserver'," + opties.getPrijs() + "," + opties.getBeschikbaarheid() + ",'"+setupID+"')";
                            stmt.executeUpdate(query2);
                        } else if (opties.getType().equals("firewall")) {
                            String query2 = "INSERT INTO serverSetups(name,type,price,availability,setupID) VALUES ('" + opties.getName() + "','firewall'," + opties.getPrijs() + "," + opties.getBeschikbaarheid() + ",'"+setupID+"')";
                            stmt.executeUpdate(query2);
                        }
                    }catch (Exception e){}
                }
                    for (ServerDragAndDrop server : designpanel.getServersArray_ArrayList()){
                        if (server instanceof WebServer) {
                            String query = "INSERT INTO servers(name, server_kindID, availability, price) VALUES ('" + server.getNaam() + "', 2, " + server.getBeschikbaarheid() + ", " + server.getPrijs() + ");";
                            stmt.executeUpdate(query);
                        } else if (server instanceof  DatabaseServer) {
                            String query = "INSERT INTO servers(name, server_kindID, availability, price) VALUES ('" + server.getNaam() + "',1, " + server.getBeschikbaarheid() + ", " + server.getPrijs() + ");";
                            stmt.executeUpdate(query);
                        } else if (server instanceof  Firewall) {
                            String query = "INSERT INTO servers(name, server_kindID, availability, price) VALUES ('" + server.getNaam() + "', 3, " + server.getBeschikbaarheid() + ", " + server.getPrijs() + ");";
                            stmt.executeUpdate(query);
                        }
                        String uniqueQuery2 = "SELECT serverID from servers WHERE name = '"+server.getNaam()+"'";
                        ResultSet rset2 = stmt.executeQuery(uniqueQuery2);
                        String juiste_id="0";
                        while(rset2.next()) {
                            juiste_id = rset2.getString("serverID");
                        }
                        String query2 = "INSERT INTO server_Present(serverID, x,y) VALUES ("+juiste_id+","+server.getBounds().getX()+","+server.getBounds().getY()+")";
                        stmt.executeUpdate(query2);

                        String uniqueQuery4 = "SELECT server_PresentID from server_Present WHERE serverID = "+juiste_id;
                        ResultSet rset7 = stmt.executeQuery(uniqueQuery4);
                        String juiste_id2="0";
                        while(rset7.next()) {
                            juiste_id2 = rset7.getString("server_PresentID");
                        }

                        String query4 = "INSERT INTO project_Has_Servers(projectID,server_PresentID) VALUES ("+projectID+","+juiste_id2+")";
                        stmt.executeUpdate(query4);

                    }
                } else {System.out.println("niet uniek");}





        } catch (SQLException ex) {
            System.out.println(ex);
        }
    }
}
