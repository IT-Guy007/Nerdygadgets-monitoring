package nerdygadgets.Design;

import nerdygadgets.Design.components.ServerDragAndDrop;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;


public class openDialog extends JFrame{
    ArrayList<String> projectnames = new ArrayList<String>();
    public openDialog() {
        setTitle("Project Openen");
        setLayout(new FlowLayout());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ArrayList<String> projectnames = new ArrayList<String>();
try {
    Connection conn = DriverManager.getConnection(
            "jdbc:mysql://192.168.1.103:3306/application",
            "group4", "Qwerty1@");
    Statement stmt = conn.createStatement();
    String uniqueQuery = "SELECT DISTINCT setupId from serverSetups";
    ResultSet rset = stmt.executeQuery(uniqueQuery);

    while (rset.next()) {
        projectnames.add(rset.getString("setupID"));
    }
} catch (Exception e){
    System.out.println(e);
}

        String[] optionsToChoose = projectnames.toArray(new String[projectnames.size()]);

        JFrame jFrame = new JFrame();

        JLabel jLabel2 = new JLabel("Selecteer een project",JLabel.CENTER);

        JComboBox<String> jComboBox = new JComboBox<>(optionsToChoose);

        JButton jButton = new JButton("Open");





        jFrame.setLayout(new GridLayout(3,1));
        jFrame.setSize(350, 250);
        jFrame.setTitle("Project Openen");
        jFrame.setVisible(true);

        jFrame.add(jLabel2);
        jFrame.add(jComboBox);
        jFrame.add(jButton);


        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


        jButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                jFrame.setVisible(false);
                DesignFrame design = new DesignFrame(jComboBox.getItemAt(jComboBox.getSelectedIndex()));

            }
        });

    }

}