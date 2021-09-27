package hr.fer.zemris.java.hw16.jvdraw.components;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;

import javax.swing.ButtonGroup;
import javax.swing.JComponent;
import javax.swing.event.MouseInputAdapter;

import hr.fer.zemris.java.hw16.jvdraw.JVDraw;
import hr.fer.zemris.java.hw16.jvdraw.interfaces.ColorChangeListener;
import hr.fer.zemris.java.hw16.jvdraw.interfaces.DrawingModel;
import hr.fer.zemris.java.hw16.jvdraw.interfaces.DrawingModelListener;
import hr.fer.zemris.java.hw16.jvdraw.interfaces.IColorProvider;
import hr.fer.zemris.java.hw16.jvdraw.objects.CircleObject;
import hr.fer.zemris.java.hw16.jvdraw.objects.FilledCircleObject;
import hr.fer.zemris.java.hw16.jvdraw.objects.GeometricalObject;
import hr.fer.zemris.java.hw16.jvdraw.objects.LineObject;

/**
 * Main component for this app, provides drawing geometrical objects inside it
 * on users click. When user clicks once, first point is determined and by user's
 * mouse movement, the rest of the object's shape and parameters define and the
 * user can see how the object will be rendered in every point.
 * 
 * @author Luka Kraljević
 *
 */
public class JDrawingCanvas extends JComponent implements DrawingModelListener,
                                    ColorChangeListener {

    /**
     * Generated serial version UID.
     */
    private static final long serialVersionUID = -1855886795897050637L;
    
    /**
     * Determines if the following click that will occur is first out of two
     * crucial clicks in the canvas.
     */
    private boolean firstClick = true;
    
    /**
     * Drawing model which operates whole object rendering system.
     */
    private DrawingModel model;
    
    /**
     * Group of mutually exclusive buttons which determine sort of object
     * to be rendered.
     */
    private ButtonGroup group;
    
    /**
     * Current object in process of rendering (between two key clicks) or
     * null if no objects are rendering at the moment.
     */
    private GeometricalObject currObject;
    
    /**
     * Current foreground color.
     */
    private Color fgCol;
    
    /**
     * Current background color.
     */
    private Color bgCol;
    
    /**
     * Constructs drawing canvas and following parameters of canvas.
     * @param model Drawing model which operates whole object rendering system.
     * @param group Mutually exclusive buttons.
     * @param fgCol Current foreground color.
     * @param bgCol Current background color.
     */
    public JDrawingCanvas(DrawingModel model, ButtonGroup group, Color fgCol,
                            Color bgCol) {
        this.model=model;
        this.group=group;
        this.fgCol=fgCol;
        this.bgCol=bgCol;
        model.addDrawingModelListener(this);
        MouseInputAdapter adapter = new MouseListener();
        addMouseListener(adapter);
        addMouseMotionListener(adapter);
    }
    
    @Override
    public void paint(Graphics g) {
        int numObj = model.getSize();
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(

                RenderingHints.KEY_ANTIALIASING,

                RenderingHints.VALUE_ANTIALIAS_ON

        );
        
        for (int i=0; i < numObj; i++) {
            GeometricalObject object = model.getObject(i);
            object.drawObject(g2d);
        }
        
        if (currObject!=null) {
            currObject.drawObject(g2d);
        }
    }

    @Override
    public void objectsAdded(DrawingModel source, int index0, int index1) {
        repaint();
    }

    @Override
    public void objectsRemoved(DrawingModel source, int index0, int index1) {
        repaint();
    }

    @Override
    public void objectsChanged(DrawingModel source, int index0, int index1) {
        repaint();
    }
    
    /**
     * Mouse listener which detects mouse clicks and movements after first
     * click and processes them.
     * @author Luka Kraljević
     *
     */
    private class MouseListener extends MouseInputAdapter {
        
        @Override
        public void mouseClicked(MouseEvent e) {
            
            if (group.getSelection() == null) {
                return;
            }
            
            if (firstClick) {
                currObject=loadGeometricalObject(group.getSelection()
                                               .getActionCommand());
                currObject.setFirstPoint(e.getPoint());
                currObject.setSecondPoint(e.getPoint());
                currObject.setFgColor(fgCol);
                currObject.setBgColor(bgCol);
                firstClick=false;
                repaint();
            } else {
                currObject.setSecondPoint(e.getPoint());
                model.add(currObject);
                currObject=null;
                firstClick=true;
            }
        }
        
        @Override
        public void mouseMoved(MouseEvent e) {
            if (!firstClick) {
                currObject.setSecondPoint(e.getPoint());
                repaint();
            }
        }
        
        /**
         * Detects which sort of geometricall object user wants to draw.
         * @param actionCommand command came from buttons
         * @return new instance of geometrical object to be rendered
         */
        private GeometricalObject loadGeometricalObject(String actionCommand) {
            if (actionCommand.equals("Line")) {
                return new LineObject();
            } else if (actionCommand.equals("Circle")) {
                return new CircleObject();
            } else {
                return new FilledCircleObject();
            }
        }
    }
    
    @Override
    public void newColorSelected(IColorProvider source, Color oldColor, Color newColor) {
        JColorArea trigger = (JColorArea) source;
        if (trigger.getName().equals(JVDraw.BG_COLOR)) {
            bgCol=newColor;
        } else {
            fgCol=newColor;
        }
    }
    


}
