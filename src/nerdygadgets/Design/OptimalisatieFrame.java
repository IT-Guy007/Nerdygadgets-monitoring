package nerdygadgets.Design;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import static javax.swing.JOptionPane.showMessageDialog;

@SuppressWarnings("ALL")
public class OptimalisatieFrame extends JDialog implements ActionListener {
    private JButton JBok, JBannuleer;
    private JTextField JTbeschickbaarheid, JTserverlimiet;
    private JLabel JLbeschikbaarheid;
    private JCheckBox JCserverlimiet;
    private double beschikbaarheid_Double;
    private int serverlimiet_Int;
    private int standaardaantalserver_Int = 10;
    private boolean go = false;

    // Nodig om frame op een percentage van schermgrootte te zetten
    private Dimension schermgrootte = Toolkit.getDefaultToolkit().getScreenSize();
    private int schermhoogte = schermgrootte.height;
    private int schermbreedte = schermgrootte.width;

    public OptimalisatieFrame(JFrame frame) {
        // Optimalisatie frame
        super(frame, true);
        setLayout(new GridLayout(3, 2));

        setSize(schermbreedte/300*70,schermhoogte/300*70);

        setTitle("Optimaliseer");

        JLbeschikbaarheid = new JLabel("Beschikbaarheid %: ");
        add(JLbeschikbaarheid);
        JTbeschickbaarheid = new JTextField(10);
        add(JTbeschickbaarheid);

        JCserverlimiet = new JCheckBox("Server limiet");
        JCserverlimiet.setSelected(false);
        JCserverlimiet.addActionListener(this);
        add(JCserverlimiet);

        JTserverlimiet = new JTextField("Standaard: " + standaardaantalserver_Int);
        JTserverlimiet.setEditable(false);
        JTserverlimiet.addActionListener(this);
        add(JTserverlimiet);

        JBok = new JButton("Optimaliseer");
        JBok.addActionListener(this);
        add(JBok);

        JBannuleer = new JButton("Annuleer");
        JBannuleer.addActionListener(this);
        add(JBannuleer);
        setVisible(true);
    }
    public boolean serverlimiet() {
        if (JCserverlimiet.isSelected()) {return true;}
        else {return false;}
    }
    public boolean isGo() {
        return go;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == JBok)
        {
            // Controleert of er een aantal server limiet is ingevoerd wanneer de checkbox geselecteerd is
            if (JTserverlimiet.getText().equals("") && JCserverlimiet.isSelected())
            {
                showMessageDialog(this, "Aantal server limiet is leeg!", "ERROR", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Controleert of gewenste beschikbaarheid is ingevoerd
            else if (JTbeschickbaarheid.getText().equals(""))
            {
                showMessageDialog(this, "Gewenste beschikbaarheid is leeg!", "ERROR", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Probeert ingevoerde aantal beschikbaarheid door te pasen naar double
            try
            {
                setBeschikbaarheid_Double(Double.parseDouble(JTbeschickbaarheid.getText().replaceAll("", "")));
            }
            catch (NumberFormatException nfe)
            {
                showMessageDialog(this, "Punt gebruiken geen komma!", "ERROR", JOptionPane.ERROR_MESSAGE);
                JTbeschickbaarheid.setText("");
                return;
            }

            // Controleert of ingevoerde beschikbaarheid een geldige waarde bevat
            if (getBeschikbaarheid_Double() > 100 || getBeschikbaarheid_Double() < 0)
            {
                showMessageDialog(this, "Gewenste beschikbaarheid moet tussen 0 en 100% liggen!", "Error", JOptionPane.ERROR_MESSAGE);
                JTbeschickbaarheid.setText("");
                return;
            }

            // Probeert aantal ingevoerd server limiet door te pasen naar een int
            if(JCserverlimiet.isSelected())
            {
                try
                {
                    setServerlimiet_Int(Integer.parseInt(JTserverlimiet.getText()));
                }
                catch (NumberFormatException nfe)
                {
                    showMessageDialog(this, "Ongeldig server limiet!", "Error", JOptionPane.ERROR_MESSAGE);
                    JTserverlimiet.setText("");
                    return;
                }
            }
            setGo(true);
            setVisible(false);
        }

        else if(e.getSource() == JCserverlimiet)
        {
            if(JCserverlimiet.isSelected())
            {
                JTserverlimiet.setEditable(true);
                JTserverlimiet.setText("");
            }
            else
            {
                JTserverlimiet.setEditable(false);
                JTserverlimiet.setText("Standaard: " + standaardaantalserver_Int);
            }
        }

        else if (e.getSource() == JBannuleer)
        {
            setGo(false);
            setVisible(false);
        }
    }

    // Getters en setters
    public void setGo(boolean go) {
        this.go = go;
    }
    public double getBeschikbaarheid_Double() {
        return beschikbaarheid_Double;
    }
    public void setBeschikbaarheid_Double(double beschikbaarheid_Double) {
        this.beschikbaarheid_Double = beschikbaarheid_Double;
    }
    public int getServerlimiet_Int() {
        return serverlimiet_Int;
    }
    public void setServerlimiet_Int(int serverlimiet_Int) {
        this.serverlimiet_Int = serverlimiet_Int;
    }
    public int getStandaardaantalserver_Int() {
        return standaardaantalserver_Int;
    }
    public JButton getJBok() {
        return JBok;
    }
    public void setJBok(JButton JBok) {
        this.JBok = JBok;
    }
    public JButton getJBannuleer() {
        return JBannuleer;
    }
    public void setJBannuleer(JButton JBannuleer) {
        this.JBannuleer = JBannuleer;
    }
    public JTextField getJTbeschickbaarheid() {
        return JTbeschickbaarheid;
    }
    public void setJTbeschickbaarheid(JTextField JTbeschickbaarheid) {
        this.JTbeschickbaarheid = JTbeschickbaarheid;
    }
    public JTextField getJTserverlimiet() {
        return JTserverlimiet;
    }
    public void setJTserverlimiet(JTextField JTserverlimiet) {
        this.JTserverlimiet = JTserverlimiet;
    }
    public JLabel getJLbeschikbaarheid() {
        return JLbeschikbaarheid;
    }
    public void setJLbeschikbaarheid(JLabel JLbeschikbaarheid) {
        this.JLbeschikbaarheid = JLbeschikbaarheid;
    }
    public JCheckBox getJCserverlimiet() {
        return JCserverlimiet;
    }
    public void setJCserverlimiet(JCheckBox JCserverlimiet) {
        this.JCserverlimiet = JCserverlimiet;
    }
    public void setStandaardaantalserver_Int(int standaardaantalserver_Int) {
        this.standaardaantalserver_Int = standaardaantalserver_Int;
    }
    public Dimension getSchermgrootte() {
        return schermgrootte;
    }
    public void setSchermgrootte(Dimension schermgrootte) {
        this.schermgrootte = schermgrootte;
    }
    public int getSchermhoogte() {
        return schermhoogte;
    }
    public void setSchermhoogte(int schermhoogte) {
        this.schermhoogte = schermhoogte;
    }
    public int getSchermbreedte() {
        return schermbreedte;
    }
    public void setSchermbreedte(int schermbreedte) {
        this.schermbreedte = schermbreedte;
    }
}
