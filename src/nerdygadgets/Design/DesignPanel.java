package nerdygadgets.Design;


import nerdygadgets.Design.components.DatabaseServer;
import nerdygadgets.Design.components.Firewall;
import nerdygadgets.Design.components.ServerDragAndDrop;
import nerdygadgets.Design.components.WebServer;

import java.awt.event.*;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.round;

@SuppressWarnings("ALL")
public class DesignPanel extends JPanel implements ComponentListener{
    private final DesignFrame frame_DesignFrame;
    private final Dimension schermgrootte_Dimension = Toolkit.getDefaultToolkit().getScreenSize();
    private final int schermhoogte_int = schermgrootte_Dimension.height;
    private final int schermbreedte_int = schermgrootte_Dimension.width;
    private final List<Component[]> connections_list;
    private static final int x=0;
    private final JScrollPane schrollscherm;
    private ArrayList<ServerDragAndDrop> serversArray_ArrayList = new ArrayList<>();

    // Constructor
    public DesignPanel(DesignFrame frame) {
        // Deze constructor zorgt ervoor dat het panel de juiste kleur, layout en dergelijke krijgt.
        schrollscherm = new JScrollPane(this,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        frame.add(schrollscherm);
        schrollscherm.setPreferredSize(new Dimension(frame.getWidth() - 25, frame.getHeight() - 100));

        connections_list = new ArrayList<>();
        this.frame_DesignFrame = frame;
        this.frame_DesignFrame.addComponentListener(this);
        setResponsiveSize();
        setBackground(Color.white);
        setLayout(null);
        repaint();

        MouseAdapter ma = new MouseAdapter() {
            private Component dragComponent;
            private Point offset;

            @Override
            public void mousePressed(MouseEvent e) {
                // Deze functie zorgt ervoor dat als je op een knop rechtermuis klikt, deze word aangeroepen om te verdwijnen
                Component component = getComponentAt(e.getPoint());
                if(e.getButton() == MouseEvent.BUTTON3){
                    int screenX = e.getXOnScreen();
                    int screenY = e.getYOnScreen();

                    suicide(component, screenY, screenX);
                    suicide(component, screenY, screenX);

                }else{
                    if (component != DesignPanel.this && component != null) {
                        dragComponent = component;
                        Point clickPoint = e.getPoint();
                        int deltaX = clickPoint.x - dragComponent.getX();
                        int deltaY = clickPoint.y - dragComponent.getY();
                        offset = new Point(deltaX, deltaY);
                    }
                }
            }
            @Override
            public void mouseDragged(MouseEvent e) {
                // Deze functie zorgt ervoor dat als je een object sleept, de locatie van het object veranderd.
                int mouseX = e.getX();
                int mouseY = e.getY();
                int xDelta;
                int yDelta;
                try {
                    xDelta = mouseX - offset.x;
                    yDelta = mouseY - offset.y;
                }catch (Exception FE){
                    xDelta = mouseX - 0;
                    yDelta = mouseY - 0;
                }
                if (frame.getisVolscherm()){
                    if (xDelta >=140 &&yDelta >= 0 && xDelta <= schermbreedte_int -(schermbreedte_int/5.8) && yDelta <= schermhoogte_int-180){
                        dragComponent.setLocation(xDelta, yDelta);
                    }
                }else{
                    if (xDelta >=140 && yDelta >= 0 && xDelta <= schermbreedte_int/36*26 && yDelta <= schermhoogte_int/41*26){
                        dragComponent.setLocation(xDelta, yDelta);
                    }
                }
                repaint();
            }
        };
        addMouseListener(ma);
        addMouseMotionListener(ma);
        repaint();
    }

    // Methodes voor toevoegen servers
    public void add(Component parent, Component child) {
        // Deze functie voegt de firewall toe als parent en zorgt dat het andere attribuut een child is, zodat deze er altijd overheen gaat.
        if (parent.getParent() != this) {
            add(parent);
        }
        if (child.getParent() != this) {
            add(child);
        }
        connections_list.add(new Component[]{parent, child});
    }
    public void suicide(Component server, int screeny, int screenx){
        // Kijkt of het object dat je wilt verwijderen geen firewall is en haalt vervolgens het object van het scherm, en haalt deze uit de lijst met objecten.
        if(!(server instanceof Firewall)){
            remove(server);
            removeArrayList(server);
            int counter =0;
            try {
                for (Component[] coneections : connections_list) {
                    try {
                        if (server.getBounds().equals(coneections[1].getBounds())) {
                            connections_list.remove(counter);
                        }
                        counter++;
                    } catch (Exception e) {
                        System.out.println("test");
                    }
                }
            }catch (Exception e){
                System.out.println("Error in loop 1");
                try {
                    for (Component[] coneections : connections_list) {
                        try {
                            if (server.getBounds().equals(coneections[1].getBounds())) {
                                connections_list.remove(counter);
                            }
                            counter++;
                        } catch (Exception c) {
                            System.out.println("test");
                        }
                    }
                }catch (Exception b){
                    System.out.println("Error in loop 2");
                    try {
                        for (Component[] coneections : connections_list) {
                            try {
                                if (server.getBounds().equals(coneections[1].getBounds())) {
                                    connections_list.remove(counter);
                                }
                                counter++;
                            } catch (Exception c) {
                                System.out.println("test");
                            }
                        }
                    }catch (Exception g){
                        System.out.println("Error in loop 3");
                    }
                }
            }
            repaint();
        }
    }


    // Overridings methodes voor  painten components
    @Override
    protected void paintComponent(Graphics g) {
        // Deze functie tekent te lijnen tussen servers en schrijft de beschikbaarheid rechtsbovenenin.


        for (ServerDragAndDrop webservertje : frame_DesignFrame.getList().getServers()) {
            String naam = webservertje.getNaam();
            double prijs = webservertje.getPrijs();
            double beschikbaarheid = webservertje.getBeschikbaarheid();
            if ((naam != "HAL9001DB"&&naam != "HAL9002DB"&&naam != "HAL9003DB"&&naam != "HAL9001DB"&&naam != "HAL9001W"
                    &&naam != "HAL9002W"&&naam != "HAL9003W") || (prijs!=5100&&prijs!=3200&&prijs!=2200&&prijs!=7700
                    &&prijs!=12200) || (beschikbaarheid!=80&&beschikbaarheid!=90&&beschikbaarheid!=95&&beschikbaarheid!=98)) {

                if (webservertje instanceof WebServer) {
                    for (ServerDragAndDrop item : serversArray_ArrayList) {
                        if (item.getPrijs() == webservertje.getPrijs() || item.getNaam().equals(webservertje.getNaam()) || item.getBeschikbaarheid() == webservertje.getBeschikbaarheid() && item instanceof WebServer) {
                            item.setNaam(naam);
                            item.setPrijs(prijs);
                            item.setBeschikbaarheid(beschikbaarheid);
                            item.setText();
                        }
                    }
                } else if (webservertje instanceof DatabaseServer) {
                    for (ServerDragAndDrop item : serversArray_ArrayList) {
                        if (item.getPrijs() == webservertje.getPrijs() || item.getNaam().equals(webservertje.getNaam()) || item.getBeschikbaarheid() == webservertje.getBeschikbaarheid() && item instanceof DatabaseServer) {
                            item.setNaam(naam);
                            item.setPrijs(prijs);
                            item.setBeschikbaarheid(beschikbaarheid);
                            item.setText();
                        }
                    }
                }
            }else {}
        }
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g.create();
        for (Component[] connection : connections_list) {
            if (connection[1] instanceof WebServer || connection[1] instanceof DatabaseServer|| connection[1] instanceof Firewall){
                Rectangle parent = connection[0].getBounds();
                Rectangle child = connection[1].getBounds();

                g2d.draw(new Line2D.Double(parent.getCenterX(), parent.getCenterY(), child.getCenterX(), child.getCenterY()));
            }
        }
        g2d.dispose();
        g.drawLine(140,0,140,getHeight());
        g.setFont(new Font("Arial", Font.BOLD, 12));
        FontMetrics metrics = g.getFontMetrics();
        g.setColor(Color.black);
        g.drawLine(getWidth() - metrics.stringWidth("Aantal Database Servers: " + countDBServers()) - 10-50,0,getWidth() - metrics.stringWidth("Aantal Database Servers: " + countDBServers()) - 10-50,getHeight());
        g.drawString("Aantal Database servers: " + countDBServers(), getWidth() - metrics.stringWidth("Aantal Database Servers: " + countDBServers()) - 5-50, 20);
        g.drawString("Aantal Web Servers : " + countWebServers(), getWidth() - metrics.stringWidth("Aantal Web Servers : " + countWebServers()) - 5-50, 40);
        g.drawString("Aantal PFSense Servers : 1", getWidth() - metrics.stringWidth("Aantal PFSense Servers : 1") - 5-50, 60);
        g.drawLine(getWidth() - metrics.stringWidth("Aantal Database Servers: " + countDBServers()) - 10-50,75, schermgrootte_Dimension.width, 75);
        g.drawString("Prijs per jaar: €" + berekenTotalePrijs(), getWidth() - metrics.stringWidth("Prijs per jaar: €"+ berekenTotalePrijs()) - 5-50, 90);
        g.drawString("Beschikbaarheid: " + berekenTotaleBeschikbaarheid() + "%", getWidth() - metrics.stringWidth("Beschikbaarheid: " + berekenTotaleBeschikbaarheid() + "%") - 5-50, 110);
    }
    @Override
    public void componentResized(ComponentEvent e) {SetKleinScherm();}
    @Override
    public void componentMoved(ComponentEvent e) {
        // Deze fucntie is nodig voor de Componentlistener

    }
    @Override
    public void componentShown(ComponentEvent e) {
        // Deze fucntie is nodig voor de Componentlistener
    }
    @Override
    public void componentHidden(ComponentEvent e) {
        // Deze fucntie is nodig voor de Componentlistener
    }

    // Methodes voor grootte van panel
    @Override
    public Dimension getPreferredSize(){
        return new Dimension(frame_DesignFrame.getWidth(),2000);
    }
    public void setResponsiveSize() {
        //Deze functie ze de responsive size van het panel
        //setPreferredSize(new Dimension(frame_DesignFrame.getWidth() - 25, frame_DesignFrame.getHeight() - 100));
        setPreferredSize(new Dimension(frame_DesignFrame.getWidth() - 25, 3000));
    }
    public void SetGrootScherm() {
        // Deze functie vergroot het panel zodat dit ook in fullscreen werkt
        setPreferredSize(new Dimension((int) round(0.99*schermbreedte_int), (int) round(0.92*schermhoogte_int) ));
        schrollscherm.setPreferredSize(new Dimension((int) round(0.99*schermbreedte_int), (int) round(0.92*schermhoogte_int) ));
        repaint();
    }
    public void SetKleinScherm(){
        // Deze fucntie verkleind het panel zodat deze ook in normale omstandigheden werkt.
        setPreferredSize(new Dimension((int) round(0.98*(schermbreedte_int/30*26)),(int) round(0.78*(schermhoogte_int/30*26)) ));
        schrollscherm.setPreferredSize(new Dimension((int) round(0.98*(schermbreedte_int/30*26)),(int) round(0.78*(schermhoogte_int/30*26)) ));
        repaint();
    }

    // Actuele beschikbaarheid berekenen.
    public String berekenTotalePrijs() {
        // Deze fucntie berekend de totale prijs per maand van het actuele design.
        double totalePrijs = 0;
        for (ServerDragAndDrop server : serversArray_ArrayList) {
            totalePrijs += server.getPrijs();
        }
        return removeTrailingZeros(Math.round(totalePrijs * 100.0) / 100.0);
    }
    public int countDBServers(){
        // Deze functie kijkt hoeveel databaseservers er zijn.
        int i = 0;
        for (ServerDragAndDrop server : serversArray_ArrayList) {
            if (server instanceof DatabaseServer) {
                i++;
            }
        }  return i;
    }
    public int countWebServers(){
        // Deze functie kijkt hoeveel webservers er zijn.
        int i = 0;
        for (ServerDragAndDrop server : serversArray_ArrayList) {
            if (server instanceof WebServer) {
                i++;
            }
        }  return i;
    }
    public String berekenTotaleBeschikbaarheid() {
        // Deze functie berekend de totale beschikbaarheid van het huidige ontwerp.
        double firewallBeschikbaarheid = 1;
        double webServerBeschikbaarheid = 1;
        double databaseBeschikbaarheid = 1;

        for (ServerDragAndDrop server : serversArray_ArrayList) {
            if (server instanceof Firewall) {
                firewallBeschikbaarheid *= (1 - (server.getBeschikbaarheid() / 100));
            }else if (server instanceof WebServer) {
                webServerBeschikbaarheid *= (1 - (server.getBeschikbaarheid() / 100));
            }else if (server instanceof DatabaseServer) {
                databaseBeschikbaarheid *= (1 - (server.getBeschikbaarheid() / 100));
            }
        }
        double totaleBeschikbaarheid = (1 - firewallBeschikbaarheid) * (1 - webServerBeschikbaarheid) * (1 - databaseBeschikbaarheid);
        return removeTrailingZeros((double) Math.round((totaleBeschikbaarheid*100) * 1000d)/1000d);
    }
    public String removeTrailingZeros(double number) {
        // Deze functie haalt overbodige nullen weg.
        if (number % 1 == 0) {
            return String.format("%.0f", number);
        } else {
            return String.valueOf(number);
        }
    }

    // Toevoegen aan servers array
    public void addArrayList(ServerDragAndDrop server){
        serversArray_ArrayList.add(server);
    }
    public void removeArrayList(Component server){
        serversArray_ArrayList.remove(server);
    }
    public void removeArrayList(ServerDragAndDrop server){
        serversArray_ArrayList.remove(server);
    }

    // Getters en setters
    public ArrayList<ServerDragAndDrop> getServersArray_ArrayList() {
        return serversArray_ArrayList;
    }
    public void setServersArray_ArrayList(ArrayList<ServerDragAndDrop> serversArray_ArrayList) {
        this.serversArray_ArrayList = serversArray_ArrayList;
    }
    public DesignFrame getFrame() {
        //getter van het Designframe.
        return frame_DesignFrame;

    }
    public DesignFrame getFrame_DesignFrame() {
        return frame_DesignFrame;
    }
    public Dimension getSchermgrootte_Dimension() {
        return schermgrootte_Dimension;
    }
    public int getSchermhoogte_int() {
        return schermhoogte_int;
    }
    public int getSchermbreedte_int() {
        return schermbreedte_int;
    }
    public List<Component[]> getConnections_list() {
        return connections_list;
    }
    public JScrollPane getSchrollscherm() {
        return schrollscherm;
    }
}