package net.haesleinhuepf.clijx.gui;

import fiji.util.gui.GenericDialogPlus;
import ij.IJ;
import ij.ImageJ;
import ij.ImagePlus;
import ij.WindowManager;
import ij.measure.Calibration;
import ij.measure.ResultsTable;
import ij.plugin.PlugIn;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clijx.CLIJx;
import net.imglib2.realtransform.AffineTransform3D;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Arrays;

/**
 * ImageRegistration3D
 * <p>
 * <p>
 * <p>
 * Author: @haesleinhuepf
 * 11 2019
 */
public class Segmentation3D implements PlugIn {

    private static String WINDOW_XY_TITLE = "XY";
    private static String WINDOW_XZ_TITLE = "XZ";
    private static String WINDOW_YZ_TITLE = "YZ";

    float scale1X = 1.0f;
    float scale1Y = 1.0f;
    float scale1Z = 1.0f;

    @Override
    public void run(String arg) {
        GenericDialogPlus gdp = new GenericDialogPlus("3D Image Segmentation");
        gdp.addImageChoice("Image to segment", IJ.getImage().getTitle());
        gdp.addNumericField("Zoom (influences speed and segmentation parameters)", 1.5, 1);
        gdp.showDialog();

        if (gdp.wasCanceled()) {
            return;
        }

        ImagePlus imp1 = gdp.getNextImage();
        float zoom = (float) gdp.getNextNumber();

        CLIJx clijx = CLIJx.getInstance();
        clijx.clear();

        //# Noise / background removal
        boolean formerDoNoiseRemoval = true;
        double formerSigma = 2;
        boolean formerDoTopHatBackgroundRemoval = true;
        double formerTopHatRadius = 10;


        //# Rigid transform
        double formerViewTranslationX = 0;
        double formerViewTranslationY = 0;
        double formerViewTranslationZ = 0;

        double formerViewRotationX = 0;
        double formerViewRotationY = 0;
        double formerViewRotationZ = 0;

        boolean formerDoThresholding = false;
        double formerThreshold = 0;
        boolean formerDoConnectedComponents = false;

        //# configure initial scaling step
        Calibration calib = imp1.getCalibration();
        scale1X = (float) (calib.pixelWidth / calib.pixelDepth * zoom);
        scale1Y = (float) (calib.pixelHeight / calib.pixelDepth * zoom);
        scale1Z = (float) (1.0 * zoom);

        boolean interactionInitialized = false;

        //# initialize state
        ClearCLBuffer input1 = null;
        ClearCLBuffer labelled = null;
        ClearCLBuffer temp = null;
        ClearCLBuffer backgroundRemoved = null;
        ClearCLBuffer transformed = null;

        ClearCLBuffer maxXProjection1 = null;
        ClearCLBuffer maxXProjection2 = null;
        ClearCLBuffer maxYProjection1 = null;
        ClearCLBuffer maxYProjection2 = null;
        ClearCLBuffer maxZProjection1 = null;
        ClearCLBuffer maxZProjection2 = null;

        int formerT1 = -1;

        //# build up user interface
        gdp = new GenericDialogPlus("Image segmentation");
        gdp.addCheckbox("Do noise removal (Gaussian blur)", formerDoNoiseRemoval);
        gdp.addSlider("Sigma (in 0.1 pixel)", 0, 100, formerSigma);
        gdp.addCheckbox("Do background removal (top hat) ", formerDoTopHatBackgroundRemoval);
        gdp.addSlider("Radius (in pixel)", 0, 100, formerTopHatRadius);
        gdp.addMessage("View transform");
        gdp.addSlider("View Translation X (in pixel)", -100, 100, formerViewTranslationX);
        gdp.addSlider("View Translation Y (in pixel)", -100, 100, formerViewTranslationY);
        gdp.addSlider("View Translation Z (in pixel)", -100, 100, formerViewTranslationZ);
        gdp.addSlider("View Rotation X (in degrees)", -180, 180, formerViewRotationX);
        gdp.addSlider("View Rotation Y (in degrees)", -180, 180, formerViewRotationY);
        gdp.addSlider("View Rotation Z (in degrees)", -180, 180, formerViewRotationZ);

        gdp.addCheckbox("Do thresholding ", formerDoThresholding);
        gdp.addSlider("Threshold", 0, 5000, formerViewRotationZ);

        gdp.addCheckbox("Do connected components analysis ", formerDoConnectedComponents);

        gdp.setModal(false);
        gdp.showDialog();


        Checkbox doNoiseRemovalCheckbox = (Checkbox) gdp.getCheckboxes().get(0);
        Scrollbar sigmaSlider = (Scrollbar) gdp.getSliders().get(0);
        Checkbox doTopHatCheckBox = (Checkbox) gdp.getCheckboxes().get(1);

        Scrollbar tophatRadiusSlider = (Scrollbar) gdp.getSliders().get(1);

        Scrollbar viewTranslationXSlider = (Scrollbar) gdp.getSliders().get(2);
        Scrollbar viewTranslationYSlider = (Scrollbar) gdp.getSliders().get(3);
        Scrollbar viewTranslationZSlider = (Scrollbar) gdp.getSliders().get(4);

        Scrollbar viewRotationXSlider = (Scrollbar) gdp.getSliders().get(5);
        Scrollbar viewRotationYSlider = (Scrollbar) gdp.getSliders().get(6);
        Scrollbar viewRotationZSlider = (Scrollbar) gdp.getSliders().get(7);

        Checkbox doThresholdingCheckbox = (Checkbox) gdp.getCheckboxes().get(2);
        Scrollbar thresholdSlider = (Scrollbar) gdp.getSliders().get(8);
        Checkbox doConnectedComponentsCheckbox = (Checkbox) gdp.getCheckboxes().get(3);


        AffineTransform3D at = new AffineTransform3D();

        //# loop until user closed the dialog
        boolean stillValid = false;
        while ((! gdp.wasCanceled()) && (!gdp.wasOKed())) {

            // # reserve memory for input and output images
            if (input1 == null) {
                input1 = clijx.create(new long[]{
                        (long) (imp1.getWidth() * scale1X),
                        (long) (imp1.getHeight() * scale1Y),
                        (long) (imp1.getNSlices() * scale1Z)}, clijx.Float);

                labelled = clijx.create(input1);
                temp = clijx.create(input1);
                transformed = clijx.create(input1);
                backgroundRemoved = clijx.create(input1);

                maxXProjection1 = clijx.create(new long[]{transformed.getDepth(), transformed.getHeight()}, input1.getNativeType());
                maxXProjection2 = clijx.create(new long[]{transformed.getDepth(), transformed.getHeight()}, input1.getNativeType());
                maxYProjection1 = clijx.create(new long[]{transformed.getWidth(), transformed.getDepth()}, input1.getNativeType());
                maxYProjection2 = clijx.create(new long[]{transformed.getWidth(), transformed.getDepth()}, input1.getNativeType());
                maxZProjection1 = clijx.create(new long[]{transformed.getWidth(), transformed.getHeight()}, input1.getNativeType());
                maxZProjection2 = clijx.create(new long[]{transformed.getWidth(), transformed.getHeight()}, input1.getNativeType());
            }
            //# read current values from dialog
            boolean doNoiseRemoval = doNoiseRemovalCheckbox.getState();
            float sigma = (float) (0.1 * sigmaSlider.getValue());
            boolean doTopHatBackgroundRemoval = doTopHatCheckBox.getState();
            float tophatRadius = (float) (tophatRadiusSlider.getValue());

            double viewTranslationX = viewTranslationXSlider.getValue();
            double viewTranslationY = viewTranslationYSlider.getValue();
            double viewTranslationZ = viewTranslationZSlider.getValue();

            double viewRotationX = viewRotationXSlider.getValue() * Math.PI / 180.0;
            double viewRotationY = viewRotationYSlider.getValue() * Math.PI / 180.0;
            double viewRotationZ = viewRotationZSlider.getValue() * Math.PI / 180.0;

            boolean doThresholding = doThresholdingCheckbox.getState();
            double threshold = thresholdSlider.getValue();
            if (!doThresholding) {
                doConnectedComponentsCheckbox.setState(false);
            }
            boolean doConnectedComponents = doConnectedComponentsCheckbox.getState();

            //# check if something changed
            if (
                    doNoiseRemoval == formerDoNoiseRemoval &&
                            sigma == formerSigma &&
                            doTopHatBackgroundRemoval == formerDoTopHatBackgroundRemoval &&
                            tophatRadius == formerTopHatRadius &&
                            viewTranslationX == formerViewTranslationX &&
                            viewTranslationY == formerViewTranslationY &&
                            viewTranslationZ == formerViewTranslationZ &&
                            viewRotationX == formerViewRotationX &&
                            viewRotationY == formerViewRotationY &&
                            viewRotationZ == formerViewRotationZ &&
                            doThresholding == formerDoThresholding &&
                            threshold == formerThreshold &&
                            doConnectedComponents == formerDoConnectedComponents &&
                            formerT1 == imp1.getFrame()
            ) {
                //# sleep some msec
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                continue;
            }

            //# measure start time for benchmarking
            long timeStamp = System.currentTimeMillis();

            if (formerT1 != imp1.getFrame()) {
                formerT1 = imp1.getFrame();
                //#push image to GPU
                ClearCLBuffer pushed = clijx.pushCurrentZStack(imp1);
                //#scale it initially;
                // depends on zoom factor and voxel size
                AffineTransform3D scaleTransform = new AffineTransform3D();
                scaleTransform.scale(scale1X, scale1Y, scale1Z);
                clijx.affineTransform3D(pushed, input1, scaleTransform);
                pushed.close();
                stillValid = false;
            }

            //# Noise/background removal
            if (formerDoNoiseRemoval != doNoiseRemoval ||
                    formerSigma != sigma ||
                    formerDoTopHatBackgroundRemoval != doTopHatBackgroundRemoval ||
                    formerTopHatRadius != tophatRadius) {
                formerDoNoiseRemoval = doNoiseRemoval;
                formerSigma = sigma;
                formerDoTopHatBackgroundRemoval = doTopHatBackgroundRemoval;
                formerTopHatRadius = tophatRadius;
                stillValid = false;
            }

            if (!stillValid) {
                if (doNoiseRemoval) {
                    clijx.blur(input1, temp, sigma, sigma, 0);
                } else {
                    clijx.copy(input1, temp);
                }
                if (doTopHatBackgroundRemoval) {
                    clijx.topHatBox(temp, backgroundRemoved, tophatRadius, tophatRadius, tophatRadius);
                } else {
                    clijx.copy(temp, backgroundRemoved);
                }
            }

            //# Rigid transform
            if (!(viewTranslationX == formerViewTranslationX &&
                    viewTranslationY == formerViewTranslationY &&
                    viewTranslationZ == formerViewTranslationZ &&
                    viewRotationX == formerViewRotationX &&
                    viewRotationY == formerViewRotationY &&
                    viewRotationZ == formerViewRotationZ )) {

                formerViewTranslationX = viewTranslationX;
                formerViewTranslationY = viewTranslationY;
                formerViewTranslationZ = viewTranslationZ;
                formerViewRotationX = viewRotationX;
                formerViewRotationY = viewRotationY;
                formerViewRotationZ = viewRotationZ;


                stillValid = false;
            }


            if (!stillValid) {

                at = new AffineTransform3D();
                at.translate(-transformed.getWidth() / 2, -transformed.getHeight() / 2, -transformed.getDepth() / 2);
                at.translate(viewTranslationX, viewTranslationY, viewTranslationZ);
                at.rotate(0, viewRotationX);
                at.rotate(1, viewRotationY);
                at.rotate(2, viewRotationZ);
                at.translate(transformed.getWidth() / 2, transformed.getHeight() / 2, transformed.getDepth() / 2);

                //# Execute operation on GPU
                clijx.affineTransform3D(backgroundRemoved, transformed, at);
            } else {
                clijx.copy(backgroundRemoved, transformed);
            }

            //# Maximum Intensity Projection
            clijx.maximumXProjection(transformed, maxXProjection1);
            clijx.maximumYProjection(transformed, maxYProjection1);
            clijx.maximumZProjection(transformed, maxZProjection1);

            //# thresholding
            if (doThresholding != formerDoThresholding ||
                    threshold != formerThreshold ||
                    doConnectedComponents != formerDoConnectedComponents
            ) {
                formerDoThresholding = doThresholding;
                formerThreshold = threshold;
                formerDoConnectedComponents = doConnectedComponents;

                stillValid = false;
            }

            if (!stillValid) {
                if (doThresholding) {
                    clijx.threshold(transformed, temp, threshold);
                    if (doConnectedComponents) {
                        clijx.connectedComponentsLabeling(temp, labelled);
                    } else {
                        clijx.copy(temp, labelled);
                    }
                } else {
                    clijx.copy(transformed, labelled);
                }
            }

            //# Maximum Intensity Projection
            clijx.maximumXProjection(labelled, maxXProjection2);
            clijx.maximumYProjection(labelled, maxYProjection2);
            clijx.maximumZProjection(labelled, maxZProjection2);

            //# Result visualisation
            clijx.showRGB(maxXProjection1, maxXProjection2, maxXProjection2, WINDOW_YZ_TITLE);
            clijx.showRGB(maxYProjection1, maxYProjection2, maxYProjection2, WINDOW_XZ_TITLE);
            clijx.showRGB(maxZProjection1, maxZProjection2, maxZProjection2, WINDOW_XY_TITLE);

            if (!interactionInitialized) {
                impX = WindowManager.getImage(WINDOW_YZ_TITLE);
                impY = WindowManager.getImage(WINDOW_XZ_TITLE);
                impZ = WindowManager.getImage(WINDOW_XY_TITLE);

                impX.getWindow().setLocation(impZ.getWindow().getX() + impZ.getWindow().getWidth(), impZ.getWindow().getY());
                impY.getWindow().setLocation(impZ.getWindow().getX(), impZ.getWindow().getY() + impZ.getWindow().getHeight());

                /////// initInteraction();

                interactionInitialized = true;
            }

            formerT1 = imp1.getFrame();
            System.out.println("Time: " + (System.currentTimeMillis() - timeStamp) + " ms");

            stillValid = true;
            //System.out.println(clijx.reportMemory());
        }

        //# clean up
        clijx.clear();

        if (gdp.wasOKed()) {

            System.out.println("Zoom: " + zoom);
            System.out.println("Scale1X: " + scale1X);
            System.out.println("Scale1Y: " + scale1Y);
            System.out.println("Scale1Z: " + scale1Z);
            System.out.println("Affine transform: " + Arrays.toString(at.getRowPackedCopy()));

            ResultsTable table = ResultsTable.getResultsTable();
            if (table == null) {
                table = new ResultsTable();
            }
            table.incrementCounter();

            table.addValue("Fixed_image", imp1.getTitle());
            table.addValue("Fixed_image_frame", imp1.getFrame());
            table.addValue("Zoom", zoom);
            table.addValue("Scale1X", scale1X);
            table.addValue("Scale1Y", scale1Y);
            table.addValue("Scale1Z", scale1Z);

            double[] matrix = at.getRowPackedCopy();
            for (int i = 0; i < matrix.length; i++) {
                table.addValue("m" + i, matrix[i]);
            }

            table.addValue("Do_noiseRemoval", formerDoNoiseRemoval?1:0);
            table.addValue("sigma", formerSigma);
            table.addValue("Do_tophat_background_subtraction", formerDoTopHatBackgroundRemoval?1:0);
            table.addValue("tophat_radius", formerTopHatRadius);

            table.addValue("Do_Thresholding", formerDoThresholding?1:0);
            table.addValue("Threshold", formerThreshold);
            table.addValue("Do_Connected_Components_Analysis", formerThreshold);

            table.show("Results");
        }
    }

    ImagePlus impX = null;
    ImagePlus impY = null;
    ImagePlus impZ = null;


    public static void main(String... args){
        String data_folder = "C:/structure/data/";

        new ImageJ();

        ImagePlus imp1 = IJ.openImage(data_folder + "Finsterwalde_001250.tif");

        imp1.show();

        new Segmentation3D().run("");
        System.exit(0);
    }
}
