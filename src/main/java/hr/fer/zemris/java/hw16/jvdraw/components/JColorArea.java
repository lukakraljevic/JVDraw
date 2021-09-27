package hr.fer.zemris.java.hw16.jvdraw.components;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JColorChooser;

import hr.fer.zemris.java.hw16.jvdraw.interfaces.ColorChangeListener;
import hr.fer.zemris.java.hw16.jvdraw.interfaces.IColorProvider;

/**
 * Component which provides choosing color and notifying all registered
 * listeners that the change of color occured.
 * @author Luka Kraljević
 *
 */
public class JColorArea extends JButton implements IColorProvider{
    
    /**
     * Generated serial version UID.
     */
    private static final long serialVersionUID = -3051757141761904617L;
    
    /**
     * Current color of this instance.
     */
    private Color selectedColor;
    
    /**
     * Name of this instance which should be unique inside an app.
     */
    private String name;
    
    /**
     * List of registered listeners to this component.
     */
    private List<ColorChangeListener> listeners = new ArrayList<>();
    
    /**
     * Initializes this instance and sets initial parameters.
     * @param selectedColor initial color of this instance
     * @param name this instance's name, must be unique inside an
     * app
     */
    public JColorArea(Color selectedColor, String name) {
        this.selectedColor = selectedColor;
        this.name=name;
        addActionListener((e) -> {
            Color newColor=JColorChooser.showDialog(this, "Choose color", selectedColor);
            if (newColor!=null) {
                fireListeners(newColor);
            }
            
        });
        
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(15, 15);
    }
    
    @Override
    public void paint(Graphics g) {
        g.setColor(selectedColor);
        g.fillRect(0, 0, 15, 15);
    }

    /**
     * Notifies all listeners that the change of color occured.
     * @param newColor new selected color
     */
    private void fireListeners(Color newColor) {
        for (ColorChangeListener l : listeners) {
            l.newColorSelected(this, selectedColor, newColor);
        }
        
        selectedColor = newColor;
        repaint();
    }

    @Override
    public Color getCurrentColor() {
        return selectedColor;
    }
    
    /**
     * Adds listener to the color change.
     * @param l color change listener
     */
    public void addColorChangeListener(ColorChangeListener l) {
        listeners = new ArrayList<>(listeners);
        listeners.add(l);
    }
    
    /**
     * Deregisteres color change listener from this instance.
     * @param l color change listener
     */
    public void removeColorChangeListener(ColorChangeListener l) {
        if (listeners.contains(l)) {
            listeners = new ArrayList<>(listeners);
            listeners.remove(l);
        }
    }
    
    @Override
    public String getName() {
        return name;
    }
    
    

}
