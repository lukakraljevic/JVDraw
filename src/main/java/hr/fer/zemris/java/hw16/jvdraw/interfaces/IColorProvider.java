package hr.fer.zemris.java.hw16.jvdraw.interfaces;

import java.awt.Color;

/**
 * Provides current color stored in implementation of this interface.
 * @author Luka Kraljević
 *
 */
public interface IColorProvider {
    
    /**
     * Returns current color of any instance, component or anything else 
     * which can store color.
     * @return current color of this provider
     */
    public Color getCurrentColor();

}
