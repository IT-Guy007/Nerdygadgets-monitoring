package nerdygadgets.Design;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.round;

public class DesignPanel extends JPanel implements ComponentListener {

    private DesignFrame frame;
    Dimension schermgrootte = Toolkit.getDefaultToolkit().getScreenSize();
    int schermhoogte = schermgrootte.height;
    int schermbreedte = schermgrootte.width;
    private List<Component[]> connections;


    public DesignPanel(DesignFrame frame) {
        connections = new ArrayList<>();
        this.frame = frame;
        this.frame.addComponentListener(this);
        SetKleinScherm();
        setBackground(Color.white);
        setLayout(null);


        MouseAdapter ma = new MouseAdapter() {
            private Component dragComponent;
            private Point clickPoint;
            private Point offset;

            @Override
            public void mousePressed(MouseEvent e) {
                Component component = getComponentAt(e.getPoint());
                if (component != DesignPanel.this && component != null) {
                    dragComponent = component;
                    clickPoint = e.getPoint();
                    int deltaX = clickPoint.x - dragComponent.getX();
                    int deltaY = clickPoint.y - dragComponent.getY();
                    offset = new Point(deltaX, deltaY);
                }
            }

            @Override
            public void mouseDragged(MouseEvent e) {

                int mouseX = e.getX();
                int mouseY = e.getY();

                int xDelta = mouseX - offset.x;
                int yDelta = mouseY - offset.y;
                if (frame.getisVolscherm() == true){
                    if (xDelta >=0 &&yDelta >= 0 && xDelta <= schermbreedte && yDelta <= schermhoogte){
                        dragComponent.setLocation(xDelta, yDelta);
                    }
                }else{
                    if (xDelta >=0 &&yDelta >= 0 && xDelta <= schermbreedte/30*26 && yDelta <= schermhoogte/30*26){
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

    public void add(Component parent, Component child) {
        if (parent.getParent() != this) {
            add(parent);
        }
        if (child.getParent() != this) {
            add(child);
        }
        connections.add(new Component[]{parent, child});
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g.create();
        for (Component[] connection : connections) {
            Rectangle parent = connection[0].getBounds();
            Rectangle child = connection[1].getBounds();

            g2d.draw(new Line2D.Double(parent.getCenterX(), parent.getCenterY(), child.getCenterX(), child.getCenterY()));
        }
        g2d.dispose();
    }

    @Override
    public void componentResized(ComponentEvent e) {SetKleinScherm();}
    public void SetGrootScherm() {
        setPreferredSize(new Dimension((int) round(0.99*schermbreedte), (int) round(0.92*schermhoogte) ));
        repaint();
    }
    public void SetKleinScherm(){
        setPreferredSize(new Dimension((int) round(0.98*(schermbreedte/30*26)),(int) round(0.78*(schermhoogte/30*26)) ));
        repaint();
    }

    @Override
    public void componentMoved(ComponentEvent e) {

    }

    @Override
    public void componentShown(ComponentEvent e) {

    }

    @Override
    public void componentHidden(ComponentEvent e) {

    }
}
