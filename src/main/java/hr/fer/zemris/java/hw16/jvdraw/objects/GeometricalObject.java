package hr.fer.zemris.java.hw16.jvdraw.objects;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;

import javax.swing.JPanel;

/**
 * Represents general geometrical object which can be rendered in drawing
 * application.
 * @author Luka Kraljević
 *
 */
public abstract class GeometricalObject {
    
    /**
     * First point determined by first click of the user.
     */
    Point firstPoint;
    
    /**
     * Second point is defined by second user's click.
     */
    Point secondPoint;
    
    /**
     * Background color of the object (represents filling).
     */
    Color bgColor;
    
    /**
     * Foreground color of the object (represents outer line color).
     */
    Color fgColor;
    
    /**
     * Object id.
     */
    int id;
    
    
    /**
     * @return the bgColor
     */
    public Color getBgColor() {
        return bgColor;
    }

    /**
     * @param bgColor the bgColor to set
     */
    public void setBgColor(Color bgColor) {
        this.bgColor = bgColor;
    }

    /**
     * @return the fgColor
     */
    public Color getFgColor() {
        return fgColor;
    }

    /**
     * @param fgColor the fgColor to set
     */
    public void setFgColor(Color fgColor) {
        this.fgColor = fgColor;
    }
    
    /**
     * Draws object in given graphics reference.
     * @param g graphics where this object will be drawn
     */
    public abstract void drawObject(Graphics g);
    
    /**
     * Sets first point.
     * @param point first point
     */
    public void setFirstPoint(Point point) {
        this.firstPoint=point;
    }
    
    /**
     * Sets second point.
     * @param point second point
     */
    public void setSecondPoint(Point point) {
        this.secondPoint=point;
    }

    /**
     * Gets first point.
     * @return the firstPoint
     */
    public Point getFirstPoint() {
        return firstPoint;
    }

    /**
     * Gets second point.
     * @return the secondPoint
     */
    public Point getSecondPoint() {
        return secondPoint;
    }
    
    /**
     * Gets top left corner of this object.
     * @return top left corner
     */
    public abstract Point getTopLeft();
    
    /**
     * Gets down right corner of this object.
     * @return down right corner
     */
    public abstract Point getDownRight();
    
    /**
     * Shifts object for x units to the left and y units up.
     * @param x defines shift to the left
     * @param y defines shift up
     */
    public abstract void shiftObject(int x, int y);
    
    /**
     * Sets panel where all parameters of this object can be changed.
     * @return panel which provides changing this object
     */
    public abstract JPanel setChangePanel();
    
    /**
     * Reads back all parameters from the change panel and stores them.
     * @param objectChange change panel for this object
     */
    public abstract void confirmChanges(JPanel objectChange);
    
    /**
     * Loads values from given string array in predefined way from the
     * jvd file.
     * @param params parameters read from jvd file for specific object
     */
    public abstract void loadValues(String[] params);
    
    /**
     * Returns document representation, turns all object's parameters in
     * a jvd line.
     * @return string jvd representation of this object
     */
    public abstract String getDocRepr();

}
