package nerdygadgets.Monitoring;

import com.mysql.cj.jdbc.exceptions.CommunicationsException;
import nerdygadgets.Design.components.ServerDragAndDrop;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.ArrayList;

public class MonitoringFrame extends JFrame implements ActionListener {
    private int maxiumservercount;
    private boolean isVolscherm = false;
    Dimension schermgrootte = Toolkit.getDefaultToolkit().getScreenSize();
    int schermhoogte = schermgrootte.height;
    int schermbreedte = schermgrootte.width;
    // private paneel MonitoringPanel;
    public MonitoringFrame(int projectID) {
        setTitle("Monitoring van " + getProjectName(projectID));
        setLayout(new FlowLayout());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(schermbreedte/30*26,schermhoogte/30*26); //Maakt de groote van de gui de helft van de schermgrootte
        setVisible(true);
    }

    public MonitoringFrame(ServerDragAndDrop server){

    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }

    public String getProjectName(int i) {
        Connection connection = null;
        PreparedStatement p = null;
        ResultSet rs = null;
        ArrayList<String> output = new ArrayList<String>();
        String output2 = null;
        try {
            // Importing and registering drivers
            Class.forName("com.mysql.cj.jdbc.Driver");

            Connection con = DriverManager.getConnection("jdbc:mysql:/windesheim.app:3306/application", "group4", "Qwerty1@");

            // SQL command data stored in String datatype
            String sql = "select * from project where ID = " + i;
            p = con.prepareStatement(sql);
            rs = p.executeQuery();

            // In array zetten;
            while (rs.next()) {
                output.set(rs.getInt("ID"),rs.getString("naam"));
            }


        } catch (CommunicationsException ce) {
            JLabel error = new JLabel("Error, kan niet verbinden met de server");
            error.setVisible(true);
            error.repaint();
            add(error);

        } catch (SQLException e) {
            System.out.println(e);

        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);

        }

        output2 = output.get(0);

        return output2;
    }
}