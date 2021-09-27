package hr.fer.zemris.java.hw16.jvdraw.interfaces;

import java.awt.Color;

/**
 * Listener which is notified every time the color changes in IColorProvider
 * instance which this listener is registered to.
 * @author Luka Kraljević
 *
 */
public interface ColorChangeListener {
    
    /**
     * Triggered when color changes.
     * @param source color provider who triggred the listener
     * @param oldColor old color of the provider
     * @param newColor new color of the provider
     */
    public void newColorSelected(IColorProvider source, Color oldColor, Color newColor);

}
