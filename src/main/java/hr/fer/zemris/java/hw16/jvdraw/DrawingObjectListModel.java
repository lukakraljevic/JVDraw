package hr.fer.zemris.java.hw16.jvdraw;

import javax.swing.AbstractListModel;

import hr.fer.zemris.java.hw16.jvdraw.interfaces.DrawingModel;
import hr.fer.zemris.java.hw16.jvdraw.interfaces.DrawingModelListener;
import hr.fer.zemris.java.hw16.jvdraw.objects.GeometricalObject;

/**
 * List model which listens to central model - drawing model and lists all
 * current objects drawn in the canvas.
 * @author Luka Kraljević
 *
 */
public class DrawingObjectListModel extends AbstractListModel<GeometricalObject> 
                        implements DrawingModelListener {
    
    
    /**
     * Generated serial version UID.
     */
    private static final long serialVersionUID = 480796994612926498L;
    
    /**
     * Reference to drawing model which this list model listens to.
     */
    private DrawingModel model;
    
    /**
     * Constructs this list model.
     * @param model drawing model to be stored in this model
     */
    public DrawingObjectListModel(DrawingModel model) {
        this.model = model;
        model.addDrawingModelListener(this);
    }

    @Override
    public int getSize() {
        return model.getSize();
    }

    @Override
    public GeometricalObject getElementAt(int index) {
        return model.getObject(index);
    }

    @Override
    public void objectsAdded(DrawingModel source, int index0, int index1) {
        this.fireIntervalAdded(this, index0, index1);
    }

    @Override
    public void objectsRemoved(DrawingModel source, int index0, int index1) {
        this.fireIntervalRemoved(this, index0, index1);
    }

    @Override
    public void objectsChanged(DrawingModel source, int index0, int index1) {
        this.fireContentsChanged(this, index0, index1);
    }

}
