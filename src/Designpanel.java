import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

public class Designpanel extends JPanel implements ComponentListener {
    private JFrame frame;

    public Designpanel(JFrame frame) {
        this.frame = frame;
        this.frame.addComponentListener(this);
        setResponsiveSize();
        setBackground(Color.white);
        setLayout(null);
        repaint();
    }
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
    }
    @Override
    public void componentResized(ComponentEvent e) {setResponsiveSize();}
    public void setResponsiveSize() {
        setPreferredSize(new Dimension(frame.getWidth() - 25, frame.getHeight() - 100));
    }
    public void setvastesize(int width, int height){
        setPreferredSize(new Dimension(width - 40, height - 100));
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
