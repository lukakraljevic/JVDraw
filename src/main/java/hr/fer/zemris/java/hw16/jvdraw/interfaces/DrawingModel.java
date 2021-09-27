package hr.fer.zemris.java.hw16.jvdraw.interfaces;

import hr.fer.zemris.java.hw16.jvdraw.objects.GeometricalObject;

/**
 * Represents central model for whole drawing app which stores info about
 * all rendered objects and every change on every object, every removal or
 * adding the object notifies all registered listeners which behave in a
 * specific way and this way we get a fully functional behaviour of the whole
 * app.
 * @author Luka Kraljević
 *
 */
public interface DrawingModel {
    
    /**
     * Retirns number of stored objects.
     * @return number of objects
     */
    public int getSize();
    
    
    /**
     * Returns object at specific index.
     * @param index position of asked object
     * @return reference to object stored at given index
     */
    public GeometricalObject getObject(int index);
    
    /**
     * Adds geometrical object to the colletcion of objects in the model.
     * @param object object to be added
     */
    public void add(GeometricalObject object);
    
    /**
     * Adds listener to any changes in the model.
     * @param l drawing model listener
     */
    public void addDrawingModelListener(DrawingModelListener l);
    
    /**
     * Deregisteres listener from this model.
     * @param l drawing model listener
     */
    public void removeDrawingModelListener(DrawingModelListener l);

}
