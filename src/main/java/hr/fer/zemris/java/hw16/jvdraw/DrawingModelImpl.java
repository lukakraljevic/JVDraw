package hr.fer.zemris.java.hw16.jvdraw;

import java.util.ArrayList;
import java.util.List;

import hr.fer.zemris.java.hw16.jvdraw.interfaces.DrawingModel;
import hr.fer.zemris.java.hw16.jvdraw.interfaces.DrawingModelListener;
import hr.fer.zemris.java.hw16.jvdraw.objects.GeometricalObject;

/**
 * Implementation of DrawingModel which is a central object of the app, it
 * notifies the most listeners about all crucial changes during the execution.
 * @author Luka Kraljević
 *
 */
public class DrawingModelImpl implements DrawingModel {
    
    /**
     * List of all geometrical objects rendered at the moment.
     */
    private List<GeometricalObject> objects;
    
    /**
     * List of all listeners to this instance.
     */
    private List<DrawingModelListener> listeners;
    
    /**
     * Constructs and initializes object.
     */
    public DrawingModelImpl() {
        objects=new ArrayList<>();
        listeners=new ArrayList<>();
        
    }

    @Override
    public int getSize() {
        return objects.size();
    }

    @Override
    public GeometricalObject getObject(int index) {
        return objects.get(index);
    }

    @Override
    public void add(GeometricalObject object) {
        objects.add(object);
        fireListenersAdd(objects.size()-1);
        
    }
    
    /**
     * Notifies all registered listeners that object is added.
     * @param index index of added object
     */
    public void fireListenersAdd(int index) {
        for (DrawingModelListener l : listeners) {
            l.objectsAdded(this, index, index);
        }
    }
    
    /**
     * Notifies all registered listeners that one object is
     * changed.
     * @param index index of changed object
     */
    public void fireListenersChange(int index) {
        for (DrawingModelListener l : listeners) {
            l.objectsChanged(this, index, index);
        }
    }
    
    /**
     * Notifies all registered listeners that one object is
     * removed.
     * @param index index of removed object
     */
    public void fireListenersRemoved(int index) {
        for (DrawingModelListener l : listeners) {
            l.objectsRemoved(this, index, index);
        }
    }
    
    

    @Override
    public void addDrawingModelListener(DrawingModelListener l) {
        listeners = new ArrayList<>(listeners);
        listeners.add(l);
    }

    @Override
    public void removeDrawingModelListener(DrawingModelListener l) {
        if (listeners.contains(l)) {
            listeners = new ArrayList<>(listeners);
            listeners.remove(l);
        }
    }
    
    /**
     * Changes certain object at certain index.
     * @param obj reference to changed object
     * @param index index of changed object
     */
    public void changeObject(GeometricalObject obj, int index) {
        objects.remove(index);
        objects.add(index, obj);
        fireListenersChange(index);
    }
    
    /**
     * Removes certain object at specified index.
     * @param index position of object to be removed
     */
    public void removeObject(int index) {
        objects.remove(index);
        fireListenersRemoved(index);
    }
    
    /**
     * Removes all current objects in the canvas.
     */
    public void clearObjects() {
        for (int i=0; i < objects.size(); i++) {
            removeObject(i);
        }
    }

    
    

}
