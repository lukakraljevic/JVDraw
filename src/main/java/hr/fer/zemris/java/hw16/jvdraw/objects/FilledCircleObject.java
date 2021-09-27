package hr.fer.zemris.java.hw16.jvdraw.objects;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Point;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * Filled circle is similar to circle, the only difference here is that
 * filled circle is filled with color stored as background color in the app.
 * @author Luka Kraljević
 *
 */
public class FilledCircleObject extends CircleObject {
    
    /**
     * Determines filled circle's id.
     */
    private static int fCircNum;
    
    /**
     * Constructs this filled circle and it's id.
     */
    public FilledCircleObject() {
        id=++fCircNum;
    }
    
    @Override
    public void drawObject(Graphics g) {
        
        g.setColor(bgColor);
        g.fillOval(center.x-radius-shift.x, center.y-radius-shift.y, 2*radius, 2*radius);
        
        g.setColor(fgColor);
        g.drawOval(center.x-radius-shift.x, center.y-radius-shift.y, 2*radius, 2*radius);
        
        if (shift.x != 0 || shift.y != 0) {
            shift = new Point(0,0);
        }
    }

    @Override
    public String toString() {
        return "Filled circle " + id;
    }

    
    @Override
    public String getDocRepr() {
        return "FCIRCLE " + getCenter().x + " " +
                getCenter().y + " " +
                getRadius() + " " +
                getFgColor().getRed() + " " +
                getFgColor().getGreen() + " " +
                getFgColor().getBlue() + " " +
                getBgColor().getRed() + " " +
                getBgColor().getGreen() + " " +
                getBgColor().getBlue();
    }
    
    @Override
    public void loadValues(String[] params) {
        setCenter(new Point(
                Integer.parseInt(params[1]),
                Integer.parseInt(params[2])));
        
        setRadius(Integer.parseInt(params[3]));
        
        setFgColor(new Color(Integer.parseInt(params[4]),
                Integer.parseInt(params[5]),
                Integer.parseInt(params[6])));
        
        setBgColor(new Color(Integer.parseInt(params[7]),
                Integer.parseInt(params[8]),
                Integer.parseInt(params[9])));
    }

    @Override
    public JPanel setChangePanel() {
        JPanel objectChange = new ChangePanel();
        objectChange.setLayout(new GridLayout(0,2));
        
        return objectChange;
    }

    @Override
    public void confirmChanges(JPanel objChange) {
        ChangePanel objectChange = (ChangePanel) objChange;
        
        setCenter(new Point(
                Integer.parseInt(objectChange.startInpX.getText()),
                Integer.parseInt(objectChange.startInpY.getText())));
        
        setRadius(Integer.parseInt(objectChange.radiusInp.getText()));
        
        String[] rgb=objectChange.lineCol.getText().split(",");
        setFgColor(new Color(Integer.parseInt(rgb[0]),
                        Integer.parseInt(rgb[1]),
                        Integer.parseInt(rgb[2])));
        
        rgb=objectChange.areaCol.getText().split(",");
        setBgColor(new Color(Integer.parseInt(rgb[0]),
                        Integer.parseInt(rgb[1]),
                        Integer.parseInt(rgb[2])));
    }
    
    /**
     * 
     * Defines panel where all filled circle's parameters can be changed.
     * @author Luka Kraljević
     *
     */
    private class ChangePanel extends JPanel {
        
        /**
         * Default serial version UID.
         */
        private static final long serialVersionUID = 1L;
        
        /**
         * x-coordinate of center
         */
        JTextField startInpX;
        
        /**
         * y-coordinate of the center
         */
        JTextField startInpY;
        
        /**
         * Color of the circle outline.
         */
        JTextField lineCol;
        
        /**
         * Radius of the circle.
         */
        JTextField radiusInp;
        
        /**
         * Color of the circle area.
         */
        JTextField areaCol;
        
        /**
         * Constructs and fills this change panel.
         */
        public ChangePanel() {
            JLabel startX = new JLabel("Center x: ");
            startInpX = new JTextField();
            startInpX.setText(Integer.toString(getCenter().x));
            add(startX);
            add(startInpX);
            
            JLabel startY = new JLabel("Center y: ");
            startInpY = new JTextField();
            startInpY.setText(Integer.toString(getCenter().y));
            add(startY);
            add(startInpY);
            
            JLabel radius = new JLabel("Radius: ");
            radiusInp = new JTextField();
            radiusInp.setText(Integer.toString(getRadius()));
            add(radius);
            add(radiusInp);
            
            JLabel lineColor = new JLabel("Circle color: ");
            lineCol = new JTextField();
            Color col = getFgColor();
            lineCol.setText(col.getRed() + "," + col.getGreen() + "," + col.getBlue());
            add(lineColor);
            add(lineCol);
            
            JLabel areaColor = new JLabel("Circle area color: ");
            areaCol = new JTextField();
            col = getBgColor();
            areaCol.setText(col.getRed() + "," + col.getGreen() + "," + col.getBlue());
            add(areaColor);
            add(areaCol);
        }
        
    }

}
