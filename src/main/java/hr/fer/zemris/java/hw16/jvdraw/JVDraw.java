package hr.fer.zemris.java.hw16.jvdraw;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.nio.file.Path;

import javax.swing.Action;
import javax.swing.ButtonGroup;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

import hr.fer.zemris.java.hw16.jvdraw.components.ColorTrackLabel;
import hr.fer.zemris.java.hw16.jvdraw.components.JColorArea;
import hr.fer.zemris.java.hw16.jvdraw.components.JDrawingCanvas;
import hr.fer.zemris.java.hw16.jvdraw.interfaces.DrawingModel;
import hr.fer.zemris.java.hw16.jvdraw.interfaces.DrawingModelListener;
import hr.fer.zemris.java.hw16.jvdraw.objects.GeometricalObject;

/**
 * Window representing entire app's interface. This is application for simple
 * object rendering, similar to Paint and supports three sorts of objects: lines,
 * circles and filled circles. It also provides list of all rendered objects 
 * to the side of the frame.
 * @author Luka Kraljević
 *
 */
public class JVDraw extends JFrame implements DrawingModelListener {
    
    /**
     * Generated serial version UID.
     */
    private static final long serialVersionUID = 2665965485215869161L;
    
    
    /**
     * Default name for background color entity.
     */
    public static final String BG_COLOR = "bgColor";
    
    /**
     * Default name for foregrounf color entity.
     */
    public static final String FG_COLOR = "fgColor";
    
    /**
     * Panel where all components will be stored.
     */
    private JPanel mainPane;
    
    /**
     * Group of mutually exclusive buttons which determine the sort of object
     * which will be rendered on click.
     */
    private ButtonGroup group;
    
    /**
     * Button which provides chooser for foreground color.
     */
    private JColorArea fgColorArea;
    
    /**
     * Button which provides chooser for background color.
     */
    private JColorArea bgColorArea;
    
    /**
     * Implementation of drawing model which supervises everything and stores
     * all neccessary information about rendered objects.
     */
    private DrawingModelImpl model;
    
    /**
     * Current opened file, or null if current sketch is not saved.
     */
    private Path openedFilePath;
    
    /**
     * Detects if there was any changes after last saving.
     */
    private boolean imageChanged = false;
    
    /**
     * Constructs this window and all neccessary parameters for successfull
     * execution of this app.
     */
    public JVDraw() {
        setTitle("JVDraw");
        setSize(900, 500);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        initGUI();
    }
    
    /**
     * Initializes all components and fills the main pane with them,
     * registeres all listeners, sets toolbar and menus etc.
     */
    private void initGUI() {
        mainPane = new JPanel();
        mainPane.setLayout(new BorderLayout());
        
        createToolbar();
        
        model = new DrawingModelImpl();
        model.addDrawingModelListener(this);
        
        JDrawingCanvas canvas = new JDrawingCanvas(model, group, Color.BLACK, Color.BLACK);
        bgColorArea.addColorChangeListener(canvas);
        fgColorArea.addColorChangeListener(canvas);
        mainPane.add(canvas, BorderLayout.CENTER);
        
        DrawingObjectListModel listModel = new DrawingObjectListModel(model);
        
        JList<GeometricalObject> list = new JList<>(listModel);
        addListeners(list, model);
        JScrollPane scrollPane = new JScrollPane(list);
        scrollPane.setPreferredSize(new Dimension(100, getHeight()));
        mainPane.add(scrollPane, BorderLayout.LINE_END);
        
        ColorTrackLabel label = new ColorTrackLabel(Color.BLACK, Color.BLACK);
        bgColorArea.addColorChangeListener(label);
        fgColorArea.addColorChangeListener(label);
        mainPane.add(label, BorderLayout.PAGE_END);
        
        createActions();
        createMenus();
        
        setContentPane(mainPane);
        
    }
    

    @Override
    public void objectsAdded(DrawingModel source, int index0, int index1) {
        imageChanged=true;
    }

    @Override
    public void objectsRemoved(DrawingModel source, int index0, int index1) {
        imageChanged=true;
    }

    @Override
    public void objectsChanged(DrawingModel source, int index0, int index1) {
        imageChanged=true;
    }
    
    /**
     * Registeres two sorts of listeners for given list: opening object's
     * properties on double-click and deleting an object from the list and
     * the model when pressing delete key.
     * @param list list which will have these listeners registered
     * @param model drawing model responsible for all objects and their changes
     */
    private void addListeners(JList<GeometricalObject> list, DrawingModelImpl model) {
        list.addMouseListener(new MouseAdapter() {
            
            @Override
            @SuppressWarnings("unchecked")
            public void mouseClicked(MouseEvent evt) {
                JList<GeometricalObject> list = (JList<GeometricalObject>)evt.getSource();
                if (evt.getClickCount() == 2) {
                    int index = list.locationToIndex(evt.getPoint());
                    
                    GeometricalObject obj = model.getObject(index);
                    JPanel objectChange = obj.setChangePanel();
                    if (JOptionPane.showConfirmDialog(JVDraw.this, objectChange)
                            ==JOptionPane.YES_OPTION) {
                        obj.confirmChanges(objectChange);
                        model.changeObject(obj, index);
                        
                    }
                    
                }
            }
        });
        
        
        list.addKeyListener(new KeyAdapter() {
            @SuppressWarnings("unchecked")
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode()==KeyEvent.VK_DELETE) {
                    JList<GeometricalObject> list = (JList<GeometricalObject>)e.getSource();
                    model.removeObject(list.getSelectedIndex());
                }
            }
        });
    }
    
    /**
     * Action which provides opening file with specific extension: ".jvd" which
     * has stored all objects and their position, sizes, and colors.
     */
    private Action openAction;
    
    /**
     * Saves drawing in ".jvd" format, if it's not saved, acts like save as, 
     * otherwise just saves the changes.
     */
    private Action saveAction;
    
    /**
     * Saves drawing in ".jvd" format and provides name and directory choosing
     * for saved file.
     */
    private Action saveAsAction;
    
    /**
     * Exports drawing in a picture format (jpg, gif, png) inside minimum bounding
     * box, so no extra white space will be seen.
     */
    private Action exportAction;
    
    /**
     * Exits the app and saves changes if user accepts it.
     */
    private Action exitAction;
    
    /**
     * Initializes actions and their parameters.
     */
    private void createActions() {
        
        JVDrawActions actions = new JVDrawActions(this);
        
        openAction = actions.getOpenAction();
        saveAction = actions.getSaveAction();
        saveAsAction = actions.getSaveAsAction();
        exportAction = actions.getExportAction();
        exitAction = actions.getExitAction();
        
        openAction.putValue(Action.NAME, "Open");
        openAction.putValue(Action.ACCELERATOR_KEY,
                KeyStroke.getKeyStroke("control 0"));
        openAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_0);
        openAction.putValue(Action.SHORT_DESCRIPTION, 
                "Used to open existing file from disk.");
        
        saveAction.putValue(Action.NAME, "Save");
        saveAction.putValue(Action.ACCELERATOR_KEY, 
                KeyStroke.getKeyStroke("control S"));
        saveAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_S);
        saveAction.putValue(Action.SHORT_DESCRIPTION, 
                "Used to save current file to disk.");
        
        saveAsAction.putValue(Action.NAME, "Save As");
        saveAsAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control A"));
        saveAsAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_A);
        saveAsAction.putValue(Action.SHORT_DESCRIPTION, 
                "Used to save current file to disk under specific name.");
        
        exportAction.putValue(Action.NAME, "Export");
        exportAction.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control E"));
        exportAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_E);
        exportAction.putValue(Action.SHORT_DESCRIPTION, "Exports drawing in a picture format.");
        
        exitAction.putValue(Action.NAME, "Exit");
        exitAction.putValue(Action.ACCELERATOR_KEY, 
                KeyStroke.getKeyStroke("control X"));
        exitAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_X);
        exitAction.putValue(Action.SHORT_DESCRIPTION, "Exit application.");
    }
    
    /**
     * Creates required menus for this application.
     */
    private void createMenus() {
        JMenuBar menuBar = new JMenuBar();

        JMenu fileMenu = new JMenu("File");
        
        fileMenu.add(new JMenuItem(openAction));
        fileMenu.add(new JMenuItem(saveAction));
        fileMenu.add(new JMenuItem(saveAsAction));
        
        fileMenu.addSeparator();
        
        fileMenu.add(new JMenuItem(exportAction));
        
        fileMenu.addSeparator();
        
        fileMenu.add(new JMenuItem(exitAction));
        menuBar.add(fileMenu);
        
        setJMenuBar(menuBar);
    }
    
    /**
     * Creates toolbar for drawing geometrical objects.
     */
    private void createToolbar() {
        JToolBar toolBar = new JToolBar("Drawing tools");
        toolBar.setFloatable(true);
        
        fgColorArea = new JColorArea(Color.BLACK, JVDraw.FG_COLOR);
        fgColorArea.setMaximumSize(new Dimension(15,15));
        fgColorArea.setToolTipText("Foreground color");
        toolBar.add(fgColorArea);
        
        toolBar.addSeparator();
        
        bgColorArea = new JColorArea(Color.BLACK, JVDraw.BG_COLOR);
        bgColorArea.setMaximumSize(new Dimension(15,15));
        bgColorArea.setToolTipText("Background color");
        toolBar.add(bgColorArea);
        toolBar.addSeparator();
        
        group = new ButtonGroup();
        JToggleButton line = new JToggleButton("Line");
        line.setActionCommand("Line");
        group.add(line);
        toolBar.add(line);
        
        JToggleButton circle = new JToggleButton("Circle");
        circle.setActionCommand("Circle");
        group.add(circle);
        toolBar.add(circle);
        
        JToggleButton fCircle = new JToggleButton("Filled circle");
        fCircle.setActionCommand("Filled circle");
        group.add(fCircle);
        toolBar.add(fCircle);
        
        mainPane.add(toolBar, BorderLayout.PAGE_START);
        
        
    }
    
    /**
     * Main method for the program.
     * @param args command-line arguments, no arguments needed for this
     * program
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JVDraw draw = new JVDraw();
            draw.setVisible(true);
        });
    }

    /**
     * @return the model
     */
    public DrawingModelImpl getModel() {
        return model;
    }

    /**
     * @param model the model to set
     */
    public void setModel(DrawingModelImpl model) {
        this.model = model;
    }

    /**
     * @return the openedFilePath
     */
    public Path getOpenedFilePath() {
        return openedFilePath;
    }

    /**
     * @param openedFilePath the openedFilePath to set
     */
    public void setOpenedFilePath(Path openedFilePath) {
        this.openedFilePath = openedFilePath;
    }

    /**
     * @return the imageChanged
     */
    public boolean isImageChanged() {
        return imageChanged;
    }

    /**
     * @param imageChanged the imageChanged to set
     */
    public void setImageChanged(boolean imageChanged) {
        this.imageChanged = imageChanged;
    }

}
