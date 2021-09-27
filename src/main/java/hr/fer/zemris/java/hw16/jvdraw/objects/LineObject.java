package hr.fer.zemris.java.hw16.jvdraw.objects;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Point;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * Represents a single line rendered in drawing canvas. User defines with
 * first click start point and with second click ending point of the line.
 * @author Luka Kraljević
 *
 */
public class LineObject extends GeometricalObject {
    
    /**
     * Start point of the line.
     */
    private Point start;
    
    /**
     * Ending point of the line.
     */
    private Point end;
    
    /**
     * Shift point which is initially zeroed.
     */
    private Point shift = new Point(0,0);
    
    /**
     * Line number which defines line's id.
     */
    private static int lineNumber;
    
    /**
     * Constructs line object, gives this line's id.
     */
    public LineObject() {
        id=++lineNumber;
    }
    
    @Override
    public void drawObject(Graphics g) {
        g.setColor(fgColor);
        g.drawLine(start.x-shift.x, start.y-shift.y, 
                   end.x-shift.x, end.y-shift.y);
        
        if (shift.x != 0 || shift.y != 0) {
            shift = new Point(0,0);
        }
        
    }
    
    @Override
    public Point getTopLeft() {
        return new Point(Math.min(start.x, end.x), Math.min(start.y, end.y));
    }
    
    @Override
    public Point getDownRight() {
        return new Point(Math.max(start.x, end.x), Math.max(start.y, end.y));
    }
    
    @Override
    public void shiftObject(int x, int y) {
        shift=new Point(x, y);
    }
    
    @Override
    public String toString() {
        return "Line " + id;
    }
    
    @Override
    public void setFirstPoint(Point point) {
        start=point;
    }
    
    @Override
    public void setSecondPoint(Point point) {
        end=point;
    }

    /**
     * @return the start
     */
    public Point getStart() {
        return start;
    }



    /**
     * @param start the start to set
     */
    public void setStart(Point start) {
        this.start = start;
    }



    /**
     * @return the end
     */
    public Point getEnd() {
        return end;
    }



    /**
     * @param end the end to set
     */
    public void setEnd(Point end) {
        this.end = end;
    }
    
    @Override
    public void loadValues(String[] params) {
        setStart(new Point(Integer.parseInt(params[1]),
                Integer.parseInt(params[2])));
        setEnd(new Point(Integer.parseInt(params[3]),
                Integer.parseInt(params[4])));
        setFgColor(new Color(Integer.parseInt(params[5]),
                Integer.parseInt(params[6]),
                Integer.parseInt(params[7])));
    }
    
    @Override
    public String getDocRepr() {
        return "LINE " + getStart().x + " " +
                getStart().y + " " +
                getEnd().x + " " +
                getEnd().y + " " +
                getFgColor().getRed() + " " +
                getFgColor().getGreen() + " " +
                getFgColor().getBlue();
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
        
        setStart(new Point(
                Integer.parseInt(objectChange.startInpX.getText()),
                Integer.parseInt(objectChange.startInpY.getText())));
        setEnd(new Point(
                Integer.parseInt(objectChange.endInpX.getText()),
                Integer.parseInt(objectChange.endInpY.getText())));
        String[] rgb=objectChange.lineCol.getText().split(",");
        setFgColor(new Color(Integer.parseInt(rgb[0]),
                        Integer.parseInt(rgb[1]),
                        Integer.parseInt(rgb[2])));
    }
    
    /**
     * Defines panel where all line's parameters can be changed.
     * @author Luka Kraljević
     *
     */
    private class ChangePanel extends JPanel {
        
        /**
         * Default serial version UID.
         */
        private static final long serialVersionUID = 1L;
        
        /**
         * x-coordinate of start point.
         */
        JTextField startInpX;
        
        /**
         * y-coordinate of start point
         */
        JTextField startInpY;
        
        /**
         * x-coordinate of end point
         */
        JTextField endInpX;
        
        /**
         * y-coordinate of end point
         */
        JTextField endInpY;
        
        /**
         * Color of line.
         */
        JTextField lineCol;
        
        /**
         * Constructs this change panel.
         */
        public ChangePanel() {
            JLabel startX = new JLabel("Start x: ");
            startInpX = new JTextField();
            startInpX.setText(Integer.toString((getStart().x)));
            add(startX);
            add(startInpX);
            
            JLabel startY = new JLabel("Start y: ");
            startInpY = new JTextField();
            startInpY.setText(Integer.toString(getStart().y));
            add(startY);
            add(startInpY);
            
            JLabel endX = new JLabel("End x: ");
            endInpX = new JTextField();
            endInpX.setText(Integer.toString(getEnd().x));
            add(endX);
            add(endInpX);
            
            JLabel endY = new JLabel("End y: ");
            endInpY = new JTextField();
            endInpY.setText(Integer.toString(getEnd().y));
            add(endY);
            add(endInpY);
            
            JLabel lineColor = new JLabel("Line color: ");
            lineCol = new JTextField();
            Color col = getFgColor();
            lineCol.setText(col.getRed() + "," + col.getGreen() + "," + col.getBlue());
            add(lineColor);
            add(lineCol);
        }
    }

}
