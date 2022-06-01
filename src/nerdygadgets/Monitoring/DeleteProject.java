package nerdygadgets.Monitoring;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

public class DeleteProject extends JFrame implements ActionListener {
    JLabel txt_projectname;
    JTextField projectname;
    JButton submit,cancel;

    public DeleteProject() {
        setTitle("Project verwijderen");
        setLayout(new FlowLayout());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(300,200);
        setResizable(false);

        cancel = new JButton("Cancel");
        cancel.setSize(50,30);
        cancel.addActionListener(this);
        cancel.setVisible(true);
        add(cancel);

        txt_projectname = new JLabel("Projectnaam die verwijderd moet worden");
        txt_projectname.setSize(200,50);
        txt_projectname.setVisible(true);
        add(txt_projectname);

        projectname = new JTextField("",20);
        projectname.setVisible(true);
        add(projectname);

        submit = new JButton("Verwijderen");
        submit.setSize(50,30);
        submit.addActionListener(this);
        submit.setVisible(true);
        add(submit);

        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == cancel) {
            setVisible(false);

        } else if(e.getSource() == submit) {
                String name = projectname.getText();
                Connection con = null;
                PreparedStatement p = null;

                String dbhost = "jdbc:mysql://192.168.1.1/application";
                String user = "group4";
                String password = "Qwerty1@";

                try {
                    Class.forName("com.mysql.cj.jdbc.Driver");
                    con = DriverManager.getConnection(dbhost, user, password);

                    // SQL command data stored in String datatype
                    name = "\"" + name + "\"";
                    String sql = "DELETE FROM project WHERE name = " + name;
                    p = con.prepareStatement(sql);
                    p.executeUpdate();

                } catch (Exception ce) {
                    System.out.println(ce);
                    ce.printStackTrace();

                }
                setVisible(false);

            }
        }
}
