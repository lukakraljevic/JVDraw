package hr.fer.zemris.java.hw16.jvdraw.components;

import java.awt.Color;

import javax.swing.JLabel;

import hr.fer.zemris.java.hw16.jvdraw.JVDraw;
import hr.fer.zemris.java.hw16.jvdraw.interfaces.ColorChangeListener;
import hr.fer.zemris.java.hw16.jvdraw.interfaces.IColorProvider;

/**
 * Label which tracks current foreground and background color and writes
 * out result as label text.
 * @author Luka Kraljević
 *
 */
public class ColorTrackLabel extends JLabel implements ColorChangeListener {
    
    /**
     * Generated serial version UID.
     */
    private static final long serialVersionUID = -8683285212704208073L;
    
    /**
     * Current foreground color.
     */
    private Color fgColor;
    
    /**
     * Current background color.
     */
    private Color bgColor;
    
    /**
     * Initializes tracking color by taking initial colors.
     * @param fgColor initial foreground color
     * @param bgColor initial background color
     */
    public ColorTrackLabel(Color fgColor, Color bgColor) {
        this.fgColor = fgColor;
        this.bgColor = bgColor;
        setText("Foreground color: (" + 
                fgColor.getRed() + "," + 
                fgColor.getGreen() + "," +
                fgColor.getBlue() + "), background color: (" +
                bgColor.getRed() + "," + 
                bgColor.getGreen() + "," +
                bgColor.getBlue() +").");
    }


    @Override
    public void newColorSelected(IColorProvider source, Color oldColor, Color newColor) {
        JColorArea trigger = (JColorArea) source;
        if (trigger.getName().equals(JVDraw.BG_COLOR)) {
            bgColor=newColor;
        } else {
            fgColor=newColor;
        }
        setText("Foreground color: (" + 
                fgColor.getRed() + "," + 
                fgColor.getGreen() + "," +
                fgColor.getBlue() + "), background color: (" +
                bgColor.getRed() + "," + 
                bgColor.getGreen() + "," +
                bgColor.getBlue() +").");
    }

}
