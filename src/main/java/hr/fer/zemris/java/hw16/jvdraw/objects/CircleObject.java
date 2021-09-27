package hr.fer.zemris.java.hw16.jvdraw.objects;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Point;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * Represents a single circle rendered in drawing canvas. User defines with
 * first click center and with second click this circle's radius.
 * @author Luka Kraljević
 *
 */
public class CircleObject extends GeometricalObject {
    
    /**
     * Center point of the circle.
     */
    protected Point center;
    
    /**
     * Radius of the circle.
     */
    protected Integer radius;
    
    /**
     * Shifting point, initially zeroed.
     */
    protected Point shift = new Point(0,0);
    
    /**
     * Determines circle's id.
     */
    private static int circleNumber;
    
    /**
     * Constructs circle and it's id.
     */
    public CircleObject() {
        id=++circleNumber;
    }
    
    @Override
    public void drawObject(Graphics g) {
        g.setColor(fgColor);
        g.drawOval(center.x-radius-shift.x, center.y-radius-shift.y, 2*radius, 2*radius);
        if (shift.x != 0 || shift.y != 0) {
            shift = new Point(0,0);
        }
    }
    
    @Override
    public Point getTopLeft() {
        return new Point(center.x-radius, center.y-radius);
    }
    
    @Override
    public Point getDownRight() {
        return new Point(center.x+radius, center.y+radius);
    }
    
    @Override
    public void shiftObject(int x, int y) {
        shift=new Point(x,y);
    }
    
    @Override
    public String toString() {
        return "Circle " + id;
    }
    
    @Override
    public void setFirstPoint(Point point) {
        center=point;
    }
    
    @Override
    public void setSecondPoint(Point point) {
        radius=(int)Math.round(Math.sqrt(Math.pow(point.x-center.x, 2) + 
                Math.pow(point.y-center.y, 2)));
    }

    /**
     * @return the center
     */
    public Point getCenter() {
        return center;
    }



    /**
     * @param center the center to set
     */
    public void setCenter(Point center) {
        this.center = center;
    }



    /**
     * @return the radius
     */
    public Integer getRadius() {
        return radius;
    }



    /**
     * @param radius the radius to set
     */
    public void setRadius(Integer radius) {
        this.radius = radius;
    }
    
    @Override
    public String getDocRepr() {
        return "CIRCLE " + getCenter().x + " " +
                getCenter().y + " " +
                getRadius() + " " +
                getFgColor().getRed() + " " +
                getFgColor().getGreen() + " " +
                getFgColor().getBlue();
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
    }
    
    /**
     * Defines panel where all circle's parameters can be changed.
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
        }
    }


}
