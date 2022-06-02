package nerdygadgets.Design;

import nerdygadgets.Design.components.DatabaseServer;
import nerdygadgets.Design.components.Firewall;
import nerdygadgets.Design.components.ServerDragAndDrop;
import nerdygadgets.Design.components.WebServer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
@SuppressWarnings("ALL")
public class OpslaanDialog extends JDialog implements ActionListener {
    private DesignPanel designpanel;
    private DesignFrame hoofdframe;

    private JButton opslaan = new JButton("Opslaan");
    private JTextField tekstveld = new JTextField(15);
    private JLabel success = new JLabel("Uw design is succesvol opgeslagen!");
    private JLabel failed = new JLabel("Voer een unieke naam in!");
    private JDialog OpslaanDialog;

    public OpslaanDialog(DesignPanel designpanel, DesignFrame hoofdframe){
        this.hoofdframe = hoofdframe;
        this.designpanel = designpanel;

        OpslaanDialog = new JDialog();
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
        success.setForeground(Color.green);

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
        String setupID = tekstveld.getText();
        boolean unique = true;
        try {
            Connection conn = DriverManager.getConnection(
                    "jdbc:mysql://192.168.1.1:3306/application",
                    "group4", "Qwerty1@");
            Statement stmt = conn.createStatement();
            String uniqueQuery = "SELECT name from project";
            ResultSet rset = stmt.executeQuery(uniqueQuery);
            while(rset.next()) {
                if (rset.getString("name").equals(setupID)) {
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
                for (ServerOptie opties: this.hoofdframe.getTempServerOpties()){
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


                        try {
                            if (server instanceof WebServer) {
                                String query = "INSERT INTO servers(name, server_kindID, availability, price) VALUES ('" + server.getNaam() + "', 4, " + server.getBeschikbaarheid() + ", " + server.getPrijs() + ");";
                                stmt.executeUpdate(query);
                            } else if (server instanceof DatabaseServer) {
                                String query = "INSERT INTO servers(name, server_kindID, availability, price) VALUES ('" + server.getNaam() + "',2, " + server.getBeschikbaarheid() + ", " + server.getPrijs() + ");";
                                stmt.executeUpdate(query);
                            } else if (server instanceof Firewall) {
                                String query = "INSERT INTO servers(name, server_kindID, availability, price) VALUES ('" + server.getNaam() + "', 6, " + server.getBeschikbaarheid() + ", " + server.getPrijs() + ");";
                                stmt.executeUpdate(query);
                            }
                        }catch (Exception e){
                            System.out.println(e);
                        }
                        try {
                            if (server instanceof WebServer) {
                                String query = "INSERT INTO servers(name, server_kindID, availability, price) VALUES ('" + server.getNaam() + "', 2, " + server.getBeschikbaarheid() + ", " + server.getPrijs() + ");";
                                stmt.executeUpdate(query);
                            } else if (server instanceof DatabaseServer) {
                                String query = "INSERT INTO servers(name, server_kindID, availability, price) VALUES ('" + server.getNaam() + "',1, " + server.getBeschikbaarheid() + ", " + server.getPrijs() + ");";
                                stmt.executeUpdate(query);
                            } else if (server instanceof Firewall) {
                                String query = "INSERT INTO servers(name, server_kindID, availability, price) VALUES ('" + server.getNaam() + "', 3, " + server.getBeschikbaarheid() + ", " + server.getPrijs() + ");";
                                stmt.executeUpdate(query);
                            }
                        }catch (Exception e){
                            System.out.println(e);
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
                        String query40 = "INSERT INTO uptime_Check(server_PresentID,up,dateTime) VALUES ("+juiste_id2+",1,now())";
                        stmt.executeUpdate(query40);

                        String query4 = "INSERT INTO project_Has_Servers(projectID,server_PresentID) VALUES ("+projectID+","+juiste_id2+")";
                        stmt.executeUpdate(query4);
                        failed.setVisible(false);
                        success.setVisible(true);
                    }
                } else {
                 System.out.println("niet uniek");
                 failed.setVisible(true);
                 success.setVisible(false);
             }
        } catch (SQLException ex) {
            System.out.println(ex);
        }
    }

    // Getters en setters
    public DesignPanel getDesignpanel() {
        return designpanel;
    }
    public void setDesignpanel(DesignPanel designpanel) {
        this.designpanel = designpanel;
    }
    public DesignFrame getHoofdframe() {
        return hoofdframe;
    }
    public void setHoofdframe(DesignFrame hoofdframe) {
        this.hoofdframe = hoofdframe;
    }
    public JButton getOpslaan() {
        return opslaan;
    }
    public void setOpslaan(JButton opslaan) {
        this.opslaan = opslaan;
    }
    public JTextField getTekstveld() {
        return tekstveld;
    }
    public void setTekstveld(JTextField tekstveld) {
        this.tekstveld = tekstveld;
    }
    public JLabel getSuccess() {
        return success;
    }
    public void setSuccess(JLabel success) {
        this.success = success;
    }
    public JLabel getFailed() {
        return failed;
    }
    public void setFailed(JLabel failed) {
        this.failed = failed;
    }
    public JDialog getOpslaanDialog() {
        return OpslaanDialog;
    }
    public void setOpslaanDialog(JDialog opslaanDialog) {
        OpslaanDialog = opslaanDialog;
    }
}
