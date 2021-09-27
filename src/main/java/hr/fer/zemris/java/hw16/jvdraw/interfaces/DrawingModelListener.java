package hr.fer.zemris.java.hw16.jvdraw.interfaces;

/**
 * Listener which is observer to all possible changes in the drawing model,
 * follows object adding, removing or even changing some object's parameters.
 * @author Luka Kraljević
 *
 */
public interface DrawingModelListener {
    
    /**
     * Detects when object is added to the drawing model.
     * @param source model where change occured
     * @param index0 index where added objects begin
     * @param index1 index where added objects finish
     */
    public void objectsAdded(DrawingModel source, int index0, int index1);
    
    /**
     * Detects when object is removed from the drawing model.
     * @param source model where change occured
     * @param index0 index where removed objects begin
     * @param index1 index where removed objects finish
     */
    public void objectsRemoved(DrawingModel source, int index0, int index1);
    
    /**
     * Detects when object is changed in the drawing model.
     * @param source model where change occured
     * @param index0 index where changed objects begin
     * @param index1 index where changed objects finish
     */
    public void objectsChanged(DrawingModel source, int index0, int index1);

}
