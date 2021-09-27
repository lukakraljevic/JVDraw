package hr.fer.zemris.java.hw16.jvdraw;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

import hr.fer.zemris.java.hw16.jvdraw.objects.CircleObject;
import hr.fer.zemris.java.hw16.jvdraw.objects.FilledCircleObject;
import hr.fer.zemris.java.hw16.jvdraw.objects.GeometricalObject;
import hr.fer.zemris.java.hw16.jvdraw.objects.LineObject;

/**
 * Defines all required actions JVDraw app provides, such as opening and saving
 * jvd files, exporting pictures and exiting app.
 * @author Luka Kraljević
 *
 */
public class JVDrawActions {
    
    /**
     * Reference to the main frame of the app.
     */
    private JVDraw frame;
    
    /**
     * Constructs parameters for initializing actions functionalities.
     * @param frame main frame of JVDraw
     */
    public JVDrawActions(JVDraw frame) {
        this.frame = frame;
    }
    
    /**
     * Action which provides opening file with specific extension: ".jvd" which
     * has stored all objects and their position, sizes, and colors.
     */
    private Action openAction = new AbstractAction() {

        /**
         * Default serial version UID.
         */
        private static final long serialVersionUID = 1L;

        @Override
        public void actionPerformed(ActionEvent e) {
            JFileChooser fc = new JFileChooser();
            fc.setDialogTitle("Open file");
            if (fc.showOpenDialog(frame) != JFileChooser.APPROVE_OPTION) {
                return;
            }

            File fileName = fc.getSelectedFile();
            Path filePath = fileName.toPath();
            if (!Files.isReadable(filePath)) {
                JOptionPane.showMessageDialog(frame, "File " + fileName.getAbsolutePath() + " does not exist!", "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            String path = filePath.toString();
            if (!path.substring(path.lastIndexOf('.') + 1).equals("jvd")) {
                JOptionPane.showMessageDialog(frame,
                        "File " + fileName.getAbsolutePath() + " hasn't proper .jvd extension!", "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            List<String> lines;
            try {
                lines = Files.readAllLines(filePath);
            } catch (IOException e1) {
                JOptionPane.showMessageDialog(frame, "File " + fileName.getAbsolutePath() + " can't be properly read!",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            frame.setOpenedFilePath(filePath);
            frame.getModel().clearObjects();

            for (String line : lines) {
                String[] params = line.split(" ");
                if (params[0].equals("LINE")) {
                    
                    LineObject lineObj = new LineObject();
                    lineObj.loadValues(params);
                    frame.getModel().add(lineObj);
                    
                } else if (params[0].equals("CIRCLE")) {
                    
                    CircleObject circle = new CircleObject();
                    circle.loadValues(params);
                    frame.getModel().add(circle);
                    
                } else if (params[0].equals("FCIRCLE")) {
                    
                    FilledCircleObject circle = new FilledCircleObject();
                    circle.loadValues(params);
                    frame.getModel().add(circle);
                }
            }

        }
    };
    
    /**
     * Checks if right extension is in opened file path, which is "jvd".
     * @return true if extension is right, otherwise false
     */
    private boolean checkExtension() {
        Path openedFile = frame.getOpenedFilePath();
        String path = openedFile.toString();
        if (!path.substring(path.lastIndexOf('.') + 1).equals("jvd")) {
            JOptionPane.showMessageDialog(frame,
                    "File " + openedFile.toFile().getAbsolutePath() + " hasn't proper .jvd extension!", "Error",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }
    
    /**
     * Writes given lines to specific path.
     * @param lines lines to be written in the stream
     * @param openedFilePath file path to be written in
     */
    private void writeFile(List<String> lines, Path openedFilePath) {
        try {
            Files.write(openedFilePath, lines);
        } catch (IOException e1) {
            JOptionPane.showMessageDialog(frame, "Error while writing to file " + openedFilePath, "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        frame.setImageChanged(false);
    }
    
    /**
     * Saves document under specific name and specific directory.
     * @return true is saving succeeded, otherwise false
     */
    private boolean saveDoc() {
        JFileChooser jfc = new JFileChooser();
        jfc.setDialogTitle("Save document");
        if (jfc.showSaveDialog(frame) != JFileChooser.APPROVE_OPTION) {
            JOptionPane.showMessageDialog(frame, "Nothing is saved.", "Warning", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        frame.setOpenedFilePath(jfc.getSelectedFile().toPath());
        return true;
    }
    
    /**
     * Returns jvd document representation of whole content in drawing canvas
     * provided by drawing model.
     * @return lines of jvd document representing drawing canvas
     */
    private List<String> getLines() {
        DrawingModelImpl model = frame.getModel();
        List<String> lines = new ArrayList<>();
        for (int i = 0; i < model.getSize(); i++) {
            GeometricalObject obj = model.getObject(i);
            lines.add(obj.getDocRepr());
        }
        return lines;
    }
    
    /**
     * Saves drawing in ".jvd" format, if it's not saved, acts like save as, 
     * otherwise just saves the changes.
     */
    private Action saveAction = new AbstractAction() {

        /**
         * Default serial version UID.
         */
        private static final long serialVersionUID = 1L;

        @Override
        public void actionPerformed(ActionEvent e) {

            List<String> lines = getLines();

            Path openedFilePath = frame.getOpenedFilePath();
            boolean saveSuccess=false;
            if (openedFilePath == null) {
                saveSuccess=saveDoc();
            }
            
            if (saveSuccess && checkExtension()) {
                writeFile(lines, frame.getOpenedFilePath());
            }
            

        }
    };
    
    /**
     * Saves drawing in ".jvd" format and provides name and directory choosing
     * for saved file.
     */
    private Action saveAsAction = new AbstractAction() {

        /**
         * Default serial version UID.
         */
        private static final long serialVersionUID = 1L;

        @Override
        public void actionPerformed(ActionEvent e) {
            List<String> lines = getLines();
            
            boolean saveSuccess=false;
            saveSuccess=saveDoc();
            
            if (saveSuccess && checkExtension()) {
                writeFile(lines, frame.getOpenedFilePath());
            }
        }
    };
    
    /**
     * Exports drawing in a picture format (jpg, gif, png) inside minimum bounding
     * box, so no extra white space will be seen.
     */
    private Action exportAction = new AbstractAction() {

        /**
         * Default serial version UID.
         */
        private static final long serialVersionUID = 1L;
        
        /**
         * Top left corner of minimum bounding box sorounding all objects.
         */
        private Point topLeft;
        
        /**
         * Down right corner of minimum bounding box sorounding all objects.
         */
        private Point downRight;

        @Override
        public void actionPerformed(ActionEvent e) {
            JFileChooser jfc = new JFileChooser();
            jfc.setDialogTitle("Export drawing");
            FileNameExtensionFilter filter = new FileNameExtensionFilter("picture", "jpg", "png", "gif");
            jfc.addChoosableFileFilter(filter);

            DrawingModelImpl model = frame.getModel();

            if (jfc.showSaveDialog(frame) != JFileChooser.APPROVE_OPTION) {
                JOptionPane.showMessageDialog(frame, "Nothing is exported.", "Warning", JOptionPane.WARNING_MESSAGE);
                return;
            }

            Path imagePath = jfc.getSelectedFile().toPath();
            String ext = imagePath.toString().substring(imagePath.toString().lastIndexOf('.') + 1);

            determineBoundingBox();

            BufferedImage image = new BufferedImage(downRight.x - topLeft.x, downRight.y - topLeft.y,
                    BufferedImage.TYPE_3BYTE_BGR);

            Graphics2D g = image.createGraphics();
            g.fillRect(0, 0, downRight.x - topLeft.x, downRight.y - topLeft.y);

            for (int i = 0; i < model.getSize(); i++) {
                GeometricalObject object = model.getObject(i);
                object.shiftObject(topLeft.x, topLeft.y);
                object.drawObject(g);
            }
            g.dispose();

            File file = imagePath.toFile();
            try {
                ImageIO.write(image, ext, file);
            } catch (IOException e1) {
                JOptionPane.showMessageDialog(frame, "Nothing is exported.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

        }
        
        /**
         * Determines minimum bounding box around objects and stores this
         * info in topLeft and downRight points because they uniquely
         * determine one bounding box.
         */
        private void determineBoundingBox() {
            DrawingModelImpl model = frame.getModel();

            for (int i = 0; i < model.getSize(); i++) {
                GeometricalObject object = model.getObject(i);
                if (i == 0) {
                    topLeft = new Point(object.getTopLeft());
                    downRight = new Point(object.getDownRight());
                    continue;
                }

                Point objTL = object.getTopLeft();
                Point objDR = object.getDownRight();

                if (objTL.x < topLeft.x) {
                    topLeft.x = objTL.x;
                }

                if (objTL.y < topLeft.y) {
                    topLeft.y = objTL.y;
                }

                if (objDR.x > downRight.x) {
                    downRight.x = objDR.x;
                }

                if (objDR.y > downRight.y) {
                    downRight.y = objDR.y;
                }

            }
        }
    };
    
    /**
     * Exits the app and saves changes if user accepts it.
     */
    private Action exitAction = new AbstractAction() {

        /**
         * Default serial version UID.
         */
        private static final long serialVersionUID = 1L;

        @Override
        public void actionPerformed(ActionEvent e) {
            if (frame.isImageChanged()) {
                int ans = JOptionPane.showConfirmDialog(
                        frame, "Do you want to save changes or discard them?"
                                + "\nYES - save and exit, NO - discard and exit," + "CANCEL - cancel exiting",
                        "Exit program", JOptionPane.YES_NO_CANCEL_OPTION);
                if (ans == JOptionPane.YES_OPTION) {
                    List<String> lines = getLines();

                    Path openedFilePath = frame.getOpenedFilePath();
                    boolean saveSuccess=false;
                    if (openedFilePath == null) {
                        saveSuccess=saveDoc();
                    }
                    
                    if (saveSuccess && checkExtension()) {
                        writeFile(lines, frame.getOpenedFilePath());
                    }
                } else if (ans == JOptionPane.CANCEL_OPTION) {
                    return;
                }
            }

            System.exit(0);
        }

    };

    /**
     * @return the openAction
     */
    public Action getOpenAction() {
        return openAction;
    }

    /**
     * @return the saveAction
     */
    public Action getSaveAction() {
        return saveAction;
    }

    /**
     * @return the saveAsAction
     */
    public Action getSaveAsAction() {
        return saveAsAction;
    }

    /**
     * @return the exportAction
     */
    public Action getExportAction() {
        return exportAction;
    }

    /**
     * @return the exitAction
     */
    public Action getExitAction() {
        return exitAction;
    }

}
