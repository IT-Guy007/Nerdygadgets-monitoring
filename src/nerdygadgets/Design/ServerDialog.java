package nerdygadgets.Design;

import nerdygadgets.Design.components.ServerDragAndDrop;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;

public class ServerDialog extends JDialog implements ActionListener {

    JComboBox CBserverList;
    JButton plusButton;
    JTextField naamField;
    JTextField prijsField;
    JLabel prijsLabel;
    JTextField uptimeField;
    JLabel uptimeLabel;
    JButton opslaanButton;
    JButton cancelButton;
    Dimension schermgrootte = Toolkit.getDefaultToolkit().getScreenSize();
    int schermhoogte = schermgrootte.height;
    int schermbreedte = schermgrootte.width;
    ArrayList serverslist;
    String[] servers;
    boolean allowChange = true;


    public ServerDialog(JFrame frame, boolean modal, String[] servers, ArrayList serverslist){
        super(frame, modal);
        setSize(200,600);
        setTitle("Serveropties wijzigen");
        setLayout(new FlowLayout());
        this.serverslist = serverslist;
        this.servers = servers;

        plusButton = create_button(plusButton, "plusButton");
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
                for (String s :
                        servers) {
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
        add(naamField);
        add(prijsLabel);
        add(prijsField);
        add(uptimeLabel);
        add(uptimeField);
        add(opslaanButton);
        add(cancelButton);

        setVisible(true);
    }

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
    public void activebutton(JButton knop, String active, String normal){

        // Deze functie zorgt ervoor dat als een knop is ingedrukt, deze iets van kleur veranderd, en na een 200 miliseconde
        // stop weer terug veranderd.
        ImageIcon icon = new ImageIcon(this.getClass().getResource("/resources/"+active+".png"));
        Image img = icon.getImage();
        Image newimg = img.getScaledInstance(-5, schermbreedte/30,  java.awt.Image.SCALE_SMOOTH);
        ImageIcon newIcon = new ImageIcon(newimg);
        knop.setIcon(newIcon);

        Timer timer = new Timer( 200, t -> {
            ImageIcon icon2 = new ImageIcon(this.getClass().getResource("/resources/"+normal+".png"));
            Image img2 = icon2.getImage();
            Image newimg2 = img2.getScaledInstance(-5, schermbreedte/30,  java.awt.Image.SCALE_SMOOTH);
            ImageIcon newIcon2 = new ImageIcon(newimg2);
            knop.setIcon(newIcon2);

        });
        timer.setRepeats( false );
        timer.start();
    }
    //Image Icon
    public ImageIcon scaleImage(ImageIcon icon, int w, int h) {
        int nw = icon.getIconWidth();
        int nh = icon.getIconHeight();
        //if(icon.getIconWidth() > w) {
        //    nw = w;
        //    nh = (nw * icon.getIconHeight()) / icon.getIconWidth();
        //}
        if(nh > h) {
            nh = h;
            nw = (icon.getIconWidth() * nh) / icon.getIconHeight();
        }
        return new ImageIcon(icon.getImage().getScaledInstance(nw, nh, Image.SCALE_DEFAULT));
    }

    public ServerDragAndDrop getArrayServer() {
        ServerDragAndDrop server = (ServerDragAndDrop) serverslist.get(CBserverList.getSelectedIndex());
        return server;
    }

    public String convertDouble(double da) {
        Double d = da;
        String text = ""+d;
        return text;
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }
}
