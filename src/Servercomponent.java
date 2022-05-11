import javax.swing.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public class Servercomponent extends JLabel {
    private ImageIcon icon;
    private JPanel hoofdpanel;
    private String naam, type;
    private double beschikbaarheid, prijs;
    private volatile int screenx =0,screeny =0, myx =0,myy =0;
    private int x,y;

    public Servercomponent(JPanel parentPanel, String name, double availability, double annualPrice) {
        this.beschikbaarheid = availability;
        this.hoofdpanel = parentPanel;
        this.naam = name;
        this.prijs = annualPrice;
        addMouseListener(new MouseListener() {
            @Override
            public void mousePressed(MouseEvent e) {
                x = e.getXOnScreen();
                y = e.getYOnScreen();

                myx = getX();
                myy = getY();

                // Remove this component if right mouse button is pressed
                if(e.getButton() == MouseEvent.BUTTON3){
                    suicide();
                }
            }

            @Override
            public void mouseClicked(MouseEvent e) { }

            @Override
            public void mouseReleased(MouseEvent e) { }

            @Override
            public void mouseEntered(MouseEvent e) { }

            @Override
            public void mouseExited(MouseEvent e) { }
        });
        addMouseMotionListener(new MouseMotionListener() {
            @Override
            public void mouseDragged(MouseEvent e) {
                // Remove component on right click
                if(e.getButton() == MouseEvent.BUTTON3){ // Right mouse button click
                    suicide();
                }

                // Drag and drop
                int deltaX = e.getXOnScreen() - screenx;
                int deltaY = e.getYOnScreen() - screeny;

                int newX = myx + deltaX;
                int newY = myy + deltaY;

                // Bottom boundary
                if(newX >= 0 && newX <= getParentPanelWidth()-getWidth() && newY >= 0){
                    setLocation(newX, getParentPanelHeight()-getHeight());
                }

                // Top boundary
                if(newX >= 0 && newX <= getParentPanelWidth()-getWidth() && newY <= getParentPanelHeight()-getHeight()){
                    setLocation(newX, 0);
                }

                // Left boundary
                if(newY >= 0 && newY <= getParentPanelHeight()-getHeight() && newX <= 0){
                    setLocation(0, newY);
                }

                // Right boundary
                if(newY >= 0 && newY <= getParentPanelHeight()-getHeight() && newX >= getParentPanelWidth()-getWidth()){
                    setLocation(getParentPanelWidth()-getWidth(), newY);
                }

                // Set location if cursor is inside all boundaries
                if(newX <= getParentPanelWidth()-getWidth() && newX >= 0 && newY >= 0 && newY <= getParentPanelHeight()-getHeight()){
                    setLocation(newX, newY);
                }

                // Redraw line(s)
                repaintHoofdPanel();
            }

            @Override
            public void mouseMoved(MouseEvent e) { }
        });
    }
    public void repaintHoofdPanel(){
        hoofdpanel.repaint();
    }
    public void suicide(){
        hoofdpanel.remove(this);
        repaintHoofdPanel();
        // Check if JLabel isnt a Firewall
        //if(!(this instanceof Firewall)){
        //    hoofdpanel.remove(this);
            //repaintParentPanel();
        //}
    }
    public int getParentPanelWidth(){
        return hoofdpanel.getWidth();
    }
    public int getParentPanelHeight(){
        return hoofdpanel.getHeight();
    }
    public String getComponentName() {
        return naam;
    }
    public String getType() {
        return type;
    }
    public double getAvailability() {
        return beschikbaarheid;
    }
    public double getAnnualPrice() {
        return prijs;
    }
    public JPanel getParentPanel() {
        return hoofdpanel;
    }
    public int getPanelX() {
        return x;
    }
    public void setPanelX(int panelX) {
        this.x = panelX;
    }
    public int getPanelY() {
        return y;
    }
    public void setPanelY(int panelY) {
        this.y = panelY;
    }
}