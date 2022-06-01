package nerdygadgets.Design;

import nerdygadgets.Design.components.DatabaseServer;
import nerdygadgets.Design.components.ServerDragAndDrop;
import nerdygadgets.Design.components.WebServer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;

@SuppressWarnings("ALL")
public class ServerDialog extends JDialog implements ActionListener {

    private JComboBox CBserverList;
    private JRadioButton radioWeb,radioDb;
    private JTextField naamField,prijsField,uptimeField;
    private JLabel prijsLabel,uptimeLabel;
    private JButton opslaanButton,cancelButton,plusButton;
    private Dimension schermgrootte = Toolkit.getDefaultToolkit().getScreenSize();
    private int schermhoogte = schermgrootte.height,schermbreedte = schermgrootte.width;
    private ArrayList serverslist;
    private String[] servers;
    private boolean allowChange = true;
    private int serverCountWeb = 0,serverCountDb = 0;

    // Constructor
    public ServerDialog(JFrame frame, boolean modal, String[] servers, ArrayList serverslist){
        super(frame, modal);
        setSize(200,600);
        setTitle("Serveropties wijzigen");
        setLayout(new FlowLayout());
        this.serverslist = serverslist;
        this.servers = servers;

        plusButton = create_button(plusButton, "plusButton");
        radioWeb = new JRadioButton("Webserver", true);
        radioDb = new JRadioButton("Databaseserver");
        radioWeb.setBounds(75,50,100,30);
        radioDb.setBounds(75,100,100,30);
        ButtonGroup radioButtons = new ButtonGroup();
        radioButtons.add(radioWeb); radioButtons.add(radioDb);
        opslaanButton = create_button(opslaanButton, "saveButton");
        cancelButton = create_button(cancelButton, "cancelButton");
        CBserverList = new JComboBox(servers);
        CBserverList.setSelectedIndex(0);
        naamField = new JTextField(getArrayServer().getNaam(), 10);

        prijsField = new JTextField(convertDouble(getArrayServer().getPrijs()), 5);
        prijsLabel = new JLabel("Prijs per maand: ");

        uptimeField = new JTextField(convertDouble(getArrayServer().getBeschikbaarheid()),5);
        uptimeLabel = new JLabel("Beschkibaarheid");
        CBserverList.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (allowChange) {
                    naamField.setText(getArrayServer().getNaam());
                    prijsField.setText(convertDouble(getArrayServer().getPrijs()));
                    uptimeField.setText(convertDouble(getArrayServer().getBeschikbaarheid()));
                }
            }
        });

        plusButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (radioWeb.isSelected()) {
                    String serverName = "Webserver [" + serverCountWeb + "]";
                    ServerDragAndDrop tempServer = new WebServer(0, serverName, 50, 50);
                    serverslist.add(tempServer);
                    getArrayServer();
                    for (String s :
                            servers) {
                        CBserverList.removeItemAt(0);
                        CBserverList.addItem(s);
                    }

                } else if (radioDb.isSelected()) {
                    String serverName = "Databaseserver [" + serverCountWeb + "]";
                    ServerDragAndDrop tempServer = new DatabaseServer(0, serverName, 50, 50);
                    serverslist.add(tempServer);
                    getArrayServer();
                    for (String s :
                            servers) {
                        CBserverList.removeItemAt(0);
                        CBserverList.addItem(s);
                    }
                }

            }
        });
        opslaanButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                allowChange = false;
                int index = CBserverList.getSelectedIndex();
                double beschikbaarheid = Double.parseDouble(uptimeField.getText());
                double prijs = Double.parseDouble(prijsField.getText());
                String naam = naamField.getText();
                ServerDragAndDrop tempServer = (ServerDragAndDrop) serverslist.get(index);
                tempServer.setNaam(naam);
                tempServer.setBeschikbaarheid(beschikbaarheid);
                tempServer.setPrijs(prijs);
                serverslist.set(index, tempServer);
                servers[index] = naam;
                for (String s : servers) {
                    CBserverList.removeItemAt(0);
                    CBserverList.addItem(s);
                }
                allowChange = true;
            }
        });
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        add(CBserverList);
        add(plusButton);
        add(radioWeb);
        add(radioDb);
        add(naamField);
        add(prijsLabel);
        add(prijsField);
        add(uptimeLabel);
        add(uptimeField);
        add(opslaanButton);
        add(cancelButton);

        setVisible(true);
    }

    //Functies voor serverdialog
    public JButton create_button(JButton naam, String path) {
        naam = new JButton(""); // Knop die er voor zorgt dat de actuele toestand word opgeslagen.
        naam.setBorderPainted(false);
        naam.setContentAreaFilled(false);
        naam.setBorderPainted(false);

        ImageIcon icon = new ImageIcon(this.getClass().getResource("/resources/"+path+".png"));
        Image img = icon.getImage();
        Image newimg = img.getScaledInstance(-5, schermbreedte/30,  java.awt.Image.SCALE_SMOOTH);
        ImageIcon newIcon = new ImageIcon(newimg);
        naam.setIcon(newIcon);

        naam.addActionListener(this);
        return naam;
    }
    public String convertDouble(double da) {
        Double d = da;
        String text = ""+d;
        return text;
    }

    @Override
    public void actionPerformed(ActionEvent e) {}

    // Getters en Setters
    public ServerDragAndDrop getArrayServer() {
        ServerDragAndDrop server = (ServerDragAndDrop) serverslist.get(CBserverList.getSelectedIndex());
        return server;
    }
    public JComboBox getCBserverList() {
        return CBserverList;
    }
    public void setCBserverList(JComboBox CBserverList) {
        this.CBserverList = CBserverList;
    }
    public JButton getPlusButton() {
        return plusButton;
    }
    public void setPlusButton(JButton plusButton) {
        this.plusButton = plusButton;
    }
    public JRadioButton getRadioWeb() {
        return radioWeb;
    }
    public void setRadioWeb(JRadioButton radioWeb) {
        this.radioWeb = radioWeb;
    }
    public JRadioButton getRadioDb() {
        return radioDb;
    }
    public void setRadioDb(JRadioButton radioDb) {
        this.radioDb = radioDb;
    }
    public JTextField getNaamField() {
        return naamField;
    }
    public void setNaamField(JTextField naamField) {
        this.naamField = naamField;
    }
    public JTextField getPrijsField() {
        return prijsField;
    }
    public void setPrijsField(JTextField prijsField) {
        this.prijsField = prijsField;
    }
    public JLabel getPrijsLabel() {
        return prijsLabel;
    }
    public void setPrijsLabel(JLabel prijsLabel) {
        this.prijsLabel = prijsLabel;
    }
    public JTextField getUptimeField() {
        return uptimeField;
    }
    public void setUptimeField(JTextField uptimeField) {
        this.uptimeField = uptimeField;
    }
    public JLabel getUptimeLabel() {
        return uptimeLabel;
    }
    public void setUptimeLabel(JLabel uptimeLabel) {
        this.uptimeLabel = uptimeLabel;
    }
    public JButton getOpslaanButton() {
        return opslaanButton;
    }
    public void setOpslaanButton(JButton opslaanButton) {
        this.opslaanButton = opslaanButton;
    }
    public JButton getCancelButton() {
        return cancelButton;
    }
    public void setCancelButton(JButton cancelButton) {
        this.cancelButton = cancelButton;
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
    public ArrayList getServerslist() {
        return serverslist;
    }
    public void setServerslist(ArrayList serverslist) {
        this.serverslist = serverslist;
    }
    public String[] getServers() {
        return servers;
    }
    public void setServers(String[] servers) {
        this.servers = servers;
    }
    public boolean isAllowChange() {
        return allowChange;
    }
    public void setAllowChange(boolean allowChange) {
        this.allowChange = allowChange;
    }
    public int getServerCountWeb() {
        return serverCountWeb;
    }
    public void setServerCountWeb(int serverCountWeb) {
        this.serverCountWeb = serverCountWeb;
    }
    public int getServerCountDb() {
        return serverCountDb;
    }
    public void setServerCountDb(int serverCountDb) {
        this.serverCountDb = serverCountDb;
    }
}
