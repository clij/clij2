package net.haesleinhuepf.clij.piv.visualisation;

import fiji.util.gui.GenericDialogPlus;
import ij.IJ;
import ij.ImageJ;
import ij.ImagePlus;
import ij.LookUpTable;
import ij.gui.Line;
import ij.gui.NewImage;
import ij.gui.Overlay;
import ij.plugin.Duplicator;
import ij.plugin.LutLoader;
import ij.process.ImageProcessor;
import ij.process.LUT;
import org.scijava.command.Command;
import org.scijava.plugin.Plugin;

import java.awt.*;
import java.awt.image.IndexColorModel;
import java.io.File;
import java.io.IOException;

/**
 * VisualiseVectorFieldsPlugin
 * <p>
 * <p>
 * <p>
 * Author: @haesleinhuepf
 * 03 2019
 */

@Plugin(type = Command.class, menuPath = "Plugins>ImageJ on GPU (CLIJ)>Measure>PIV>Visualise vector field (experimental)")
public class VisualiseVectorFieldsPlugin implements Command {

    protected ImagePlus inputImage;
    protected ImagePlus vectorXImage;
    protected ImagePlus vectorYImage;
    protected ImagePlus outputImage;
    protected boolean showResult = true;
    protected boolean silent = false;
    protected String lookupTable = "Fire";
    protected double minimumLength = 2;
    protected double maximumLength = 10;
    protected int stepSize = 10;
    protected float lineWidth = 2;
    protected boolean invertVectors = true;
    private String lutFolder = "./luts/";


    private final int finalColorStep = 100;

    public VisualiseVectorFieldsPlugin() {

        //todo: make system independent
        if (! new File(lutFolder).exists()) {
            lutFolder = "C:/Programs/fiji-win64/Fiji.app/luts/";
        }
    }

    @Override
    public void run() {

        if (!showDialog()) {
            return;
        }

        // create LUT
        ImagePlus lutImp = NewImage.createByteImage("Untitled", (int)(maximumLength * finalColorStep), 1, 1, NewImage.FILL_RAMP);
        String lutFilename = lutFolder + lookupTable + ".lut";
        if (new File(lutFilename).exists()) {
            try {
                IndexColorModel lut1 = LutLoader.open(lutFilename);
                LookUpTable lut2 = new LookUpTable(lut1);

                byte[] reds = new byte[lut2.getMapSize()];
                lut1.getReds(reds);
                byte[] greens = new byte[lut2.getMapSize()];
                lut1.getGreens(greens);
                byte[] blues = new byte[lut2.getMapSize()];
                lut1.getBlues(blues);

                LUT lut3 = new LUT(reds, greens, blues);
                lutImp.setLut(lut3);

            } catch (IOException e) {
                e.printStackTrace();
            }

        } else {
            String [] luts = IJ.getLuts();
            for (String lutname : luts) {
                if (lutname.compareTo(lookupTable) == 0) {
                    IJ.run(lutImp, lookupTable, "");
                    break;
                }
            }
        }
        //lutImp = IJ.getImage();
        IJ.run(lutImp, "RGB Color", "");
        //lutImp = IJ.getImage();
        IJ.run(lutImp, "RGB Stack", "");
        //lutImp = IJ.getImage();
        //lutImp.show();

        lutImp.setC(1);
        byte[] pixelsR = (byte[]) lutImp.getProcessor().getPixels();
        lutImp.setC(2);
        byte[] pixelsG = (byte[]) lutImp.getProcessor().getPixels();
        lutImp.setC(3);
        byte[] pixelsB = (byte[]) lutImp.getProcessor().getPixels();






        // read image
        ImageProcessor ipVectorX = vectorXImage.getProcessor();
        ImageProcessor ipVectorY = vectorYImage.getProcessor();
        ImagePlus imp = new Duplicator().run(inputImage);
        Overlay overlay = imp.getOverlay();
        if (overlay == null) {
            overlay = new Overlay();
        }

        int stepX = stepSize;
        int stepY = stepSize;
        for (int x = 0; x < imp.getWidth(); x+=stepX) {
            for (int y = 0; y < imp.getHeight(); y+=stepY) {
                float deltaX = ipVectorX.getf(x, y);
                float deltaY = ipVectorY.getf(x, y);
                if (invertVectors){
                    deltaX = -deltaX;
                    deltaY = -deltaY;
                }

                int length = (int) (Math.sqrt(
                        Math.pow(deltaX,2) +
                        Math.pow(deltaY,2)) * finalColorStep);

                if (length > minimumLength * finalColorStep) {
                    if (length >= pixelsR.length) {
                        length = pixelsR.length - 1;
                    }
                    int r = pixelsR[length] & 0xFF;
                    int g = pixelsG[length] & 0xFF;
                    int b = pixelsB[length] & 0xFF;


                    //System.out.println("R/G/B " + r + "/" + g + "/" + b);
                    Color color = new Color(r, g, b);

                    //System.out.println(length);
                    if (deltaX != 0 && deltaY != 0) {
                        Line line = new Line(x, y, x + deltaX, y + deltaY);
                        line.setStrokeColor(color);
                        BasicStroke stroke = new BasicStroke(lineWidth);
                        line.setStroke(stroke);
                        overlay.add(line);
                    }
                }
            }
        }
        imp.setOverlay(overlay);

        //imp.show();
        //IJ.run(imp, "Flatten", "");
        //imp.show();
        outputImage = imp.flatten();

        if (showResult) {
            outputImage.show();
        }
    }

    protected boolean showDialog() {
        if (silent) {
            return true;
        }
        String defaultImage = IJ.getImage().getTitle();

        GenericDialogPlus gd = new GenericDialogPlus("Visualise Vector Field");
        gd.addImageChoice("Target image", defaultImage);
        gd.addImageChoice("Vector_X image", defaultImage);
        gd.addImageChoice("Vector_Y image", defaultImage);

        gd.addStringField("LUT folder", lutFolder);
        gd.addStringField("Lookup_table", lookupTable);

        gd.addNumericField("Minimum_length (in pixels, ignore below)", minimumLength, 2);
        gd.addNumericField("Maximum_length (in pixels, just for colouring)", maximumLength, 2);

        gd.addNumericField("Step_size", stepSize, 0);
        gd.addNumericField("Line_width", lineWidth, 2);

        gd.addCheckbox("Invert vectors", invertVectors);
        gd.showDialog();
        if (gd.wasCanceled()) {
            return false;
        }

        inputImage = gd.getNextImage();
        vectorXImage = gd.getNextImage();
        vectorYImage = gd.getNextImage();

        lookupTable = gd.getNextString();

        minimumLength = gd.getNextNumber();
        maximumLength = gd.getNextNumber();

        stepSize = (int)gd.getNextNumber();
        lineWidth = (float)gd.getNextNumber();

        invertVectors = gd.getNextBoolean();
        return true;
    }

    public void setInputImage(ImagePlus inputImage) {
        this.inputImage = inputImage;
    }

    public ImagePlus getOutputImage() {
        return outputImage;
    }

    public void setSilent(boolean value) {
        silent = value;
    }

    public void setShowResult(boolean value) {
        showResult = value;
    }

    public void setInvertVectors(boolean invertVectors) {
        this.invertVectors = invertVectors;
    }

    public void setLineWidth(float lineWidth) {
        this.lineWidth = lineWidth;
    }

    public void setLookupTable(String lookupTable) {
        this.lookupTable = lookupTable;
    }

    public void setMaximumLength(double maximumLength) {
        this.maximumLength = maximumLength;
    }

    public void setMinimumLength(double minimumLength) {
        this.minimumLength = minimumLength;
    }

    public void setStepSize(int stepSize) {
        this.stepSize = stepSize;
    }

    public void setVectorXImage(ImagePlus vectorXImage) {
        this.vectorXImage = vectorXImage;
    }

    public void setVectorYImage(ImagePlus vectorYImage) {
        this.vectorYImage = vectorYImage;
    }

    public void setLutFolder(String lutFolder) {
        this.lutFolder = lutFolder;
    }

    public static ImagePlus visualiseVectorField(ImagePlus input, ImagePlus vectorXImage, ImagePlus vectorYImage, double maximumLength) {
        VisualiseVectorFieldsPlugin vvpd = new VisualiseVectorFieldsPlugin();
        vvpd.inputImage = input;
        vvpd.vectorXImage = vectorXImage;
        vvpd.vectorYImage = vectorYImage;
        vvpd.setSilent(true);
        vvpd.setShowResult(false);
        vvpd.maximumLength = maximumLength;
        vvpd.run();

        return vvpd.getOutputImage();
    }

}
