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
import java.awt.event.*;
import java.util.Arrays;

/**
 * ImageRegistration3D
 * <p>
 * <p>
 * <p>
 * Author: @haesleinhuepf
 * 11 2019
 */
public class ImageRegistration3D implements PlugIn {

    String fixedDataSourceFolder = "C:/structure/data/2019-10-28-17-22-59-23-Finsterwalde_Tribolium_nGFP/";
    String fixedDatSetName = "C0opticsprefused";
    String movingDataSourceFolder = "C:/structure/data/2019-10-28-17-22-59-23-Finsterwalde_Tribolium_nGFP/";
    String movingDataSetName = "C0opticsprefused";

    private static String WINDOW_XY_TITLE = "XY";
    private static String WINDOW_XZ_TITLE = "XZ";
    private static String WINDOW_YZ_TITLE = "YZ";

    Scrollbar registrationTranslationXSlider = null;
    Scrollbar registrationTranslationYSlider = null;
    Scrollbar registrationTranslationZSlider = null;

    Scrollbar registrationRotationXSlider = null;
    Scrollbar registrationRotationYSlider = null;
    Scrollbar registrationRotationZSlider = null;

    float scale1X = 1.0f;
    float scale1Y = 1.0f;
    float scale1Z = 1.0f;

    @Override
    public void run(String arg) {
        GenericDialogPlus gdp = new GenericDialogPlus("3D Image Registration");
        gdp.addImageChoice("Fixed image", IJ.getImage().getTitle());
        gdp.addImageChoice("Moving image", IJ.getImage().getTitle());
        gdp.addNumericField("Zoom (influences speed and registration parameters)", 1.5, 1);
        gdp.showDialog();

        if (gdp.wasCanceled()) {
            return;
        }

        ImagePlus imp1 = gdp.getNextImage();
        ImagePlus imp2 = gdp.getNextImage();
        float zoom = (float) gdp.getNextNumber();

        CLIJx clijx = CLIJx.getInstance();
        clijx.clear();

        //# Noise / background removal
        boolean formerDoNoiseAndBackgroundRemoval = true;
        double formerSigma1 = 10;
        double formerSigma2 = 30;

        //# Rigid transform
        double formerViewTranslationX = 0;
        double formerViewTranslationY = 0;
        double formerViewTranslationZ = 0;

        double formerViewRotationX = 0;
        double formerViewRotationY = 0;
        double formerViewRotationZ = 0;

        double formerRegistrationTranslationX = 0;
        double formerRegistrationTranslationY = 0;
        double formerRegistrationTranslationZ = 0;

        double formerRegistrationRotationX = 0;
        double formerRegistrationRotationY = 0;
        double formerRegistrationRotationZ = 0;

        //# configure initial scaling step
        Calibration calib = imp1.getCalibration();
        scale1X = (float) (calib.pixelWidth / calib.pixelDepth * zoom);
        scale1Y = (float) (calib.pixelHeight / calib.pixelDepth * zoom);
        scale1Z = (float) (1.0 * zoom);
        calib = imp2.getCalibration();
        float scale2X = (float) (calib.pixelWidth / calib.pixelDepth * zoom);
        float scale2Y = (float) (calib.pixelHeight / calib.pixelDepth * zoom);
        float scale2Z = (float) (1.0 * zoom);

        boolean interactionInitialized = false;

        //# initialize state
        ClearCLBuffer input1 = null;
        ClearCLBuffer input2 = null;
        ClearCLBuffer temp1 = null;
        ClearCLBuffer temp2 = null;
        ClearCLBuffer dog1 = null;
        ClearCLBuffer dog2 = null;
        ClearCLBuffer transformed = null;
        //ClearCLBuffer transformedX = null;
        //ClearCLBuffer transformedY = null;

        ClearCLBuffer maxXProjection1 = null;
        ClearCLBuffer maxXProjection2 = null;
        ClearCLBuffer maxYProjection1 = null;
        ClearCLBuffer maxYProjection2 = null;
        ClearCLBuffer maxZProjection1 = null;
        ClearCLBuffer maxZProjection2 = null;

        int formerT1 = -1;
        int formerT2 = -1;

        //# build up user interface
        gdp = new GenericDialogPlus("Image registration");
        gdp.addCheckbox("Do noise and background subtraction (Difference of Gaussian)", formerDoNoiseAndBackgroundRemoval);
        gdp.addSlider("Sigma 1 (in 0.1 pixel)", 0, 100, formerSigma1);
        gdp.addSlider("Sigma 2 (in 0.1 pixel)", 0, 100, formerSigma2);
        gdp.addMessage("View transform");
        gdp.addSlider("View Translation X (in pixel)", -100, 100, formerViewTranslationX);
        gdp.addSlider("View Translation Y (in pixel)", -100, 100, formerViewTranslationY);
        gdp.addSlider("View Translation Z (in pixel)", -100, 100, formerViewTranslationZ);
        gdp.addSlider("View Rotation X (in degrees)", -180, 180, formerViewRotationX);
        gdp.addSlider("View Rotation Y (in degrees)", -180, 180, formerViewRotationY);
        gdp.addSlider("View Rotation Z (in degrees)", -180, 180, formerViewRotationZ);

        gdp.addMessage("Registration");
        gdp.addSlider("Registration Translation X (in pixel)", -100, 100, formerRegistrationTranslationX);
        gdp.addSlider("Registration Translation Y (in pixel)", -100, 100, formerRegistrationTranslationY);
        gdp.addSlider("Registration Translation Z (in pixel)", -100, 100, formerRegistrationTranslationZ);
        gdp.addSlider("Registration Rotation X (in degrees)", -180, 180, formerRegistrationRotationX);
        gdp.addSlider("Registration Rotation Y (in degrees)", -180, 180, formerRegistrationRotationY);
        gdp.addSlider("Registration Rotation Z (in degrees)", -180, 180, formerRegistrationRotationZ);


        gdp.setModal(false);
        gdp.showDialog();


        Checkbox doNoiseAndBackgroundRemovalCheckbox = (Checkbox) gdp.getCheckboxes().get(0);
        Scrollbar sigma1Slider = (Scrollbar) gdp.getSliders().get(0);
        Scrollbar sigma2Slider = (Scrollbar) gdp.getSliders().get(1);

        Scrollbar viewTranslationXSlider = (Scrollbar) gdp.getSliders().get(2);
        Scrollbar viewTranslationYSlider = (Scrollbar) gdp.getSliders().get(3);
        Scrollbar viewTranslationZSlider = (Scrollbar) gdp.getSliders().get(4);

        Scrollbar viewRotationXSlider = (Scrollbar) gdp.getSliders().get(5);
        Scrollbar viewRotationYSlider = (Scrollbar) gdp.getSliders().get(6);
        Scrollbar viewRotationZSlider = (Scrollbar) gdp.getSliders().get(7);

        registrationTranslationXSlider = (Scrollbar) gdp.getSliders().get(8);
        registrationTranslationYSlider = (Scrollbar) gdp.getSliders().get(9);
        registrationTranslationZSlider = (Scrollbar) gdp.getSliders().get(10);

        registrationRotationXSlider = (Scrollbar) gdp.getSliders().get(11);
        registrationRotationYSlider = (Scrollbar) gdp.getSliders().get(12);
        registrationRotationZSlider = (Scrollbar) gdp.getSliders().get(13);

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

                input2 = clijx.create(new long[]{
                        (long) (imp2.getWidth() * scale2X),
                        (long) (imp2.getHeight() * scale2Y),
                        (long) (imp2.getNSlices() * scale2Z)}, clijx.Float);

                temp1 = clijx.create(input1);
                temp2 = clijx.create(input2);
                dog1 = clijx.create(input1);
                dog2 = clijx.create(input2);
                transformed = clijx.create(input1);
                //transformedX = clijx.create(new long[]{input1.getHeight(), input1.getDepth(), input1.getWidth()}, input1.getNativeType());
                //transformedY = clijx.create(new long[]{input1.getWidth(), input1.getDepth(), input1.getHeight()}, input1.getNativeType());


                maxXProjection1 = clijx.create(new long[]{transformed.getDepth(), transformed.getHeight()}, input1.getNativeType());
                maxXProjection2 = clijx.create(new long[]{transformed.getDepth(), transformed.getHeight()}, input2.getNativeType());
                maxYProjection1 = clijx.create(new long[]{transformed.getWidth(), transformed.getDepth()}, input1.getNativeType());
                maxYProjection2 = clijx.create(new long[]{transformed.getWidth(), transformed.getDepth()}, input2.getNativeType());
                maxZProjection1 = clijx.create(new long[]{transformed.getWidth(), transformed.getHeight()}, input1.getNativeType());
                maxZProjection2 = clijx.create(new long[]{transformed.getWidth(), transformed.getHeight()}, input2.getNativeType());
            }
            //# read current values from dialog
            boolean doNoiseAndBackgroundRemoval = doNoiseAndBackgroundRemovalCheckbox.getState();
            float sigma1 = (float) (0.1 * sigma1Slider.getValue());
            float sigma2 = (float) (0.1 * sigma2Slider.getValue());

            double viewTranslationX = viewTranslationXSlider.getValue();
            double viewTranslationY = viewTranslationYSlider.getValue();
            double viewTranslationZ = viewTranslationZSlider.getValue();

            double viewRotationX = viewRotationXSlider.getValue() * Math.PI / 180.0;
            double viewRotationY = viewRotationYSlider.getValue() * Math.PI / 180.0;
            double viewRotationZ = viewRotationZSlider.getValue() * Math.PI / 180.0;

            double registrationTranslationX = registrationTranslationXSlider.getValue();
            double registrationTranslationY = registrationTranslationYSlider.getValue();
            double registrationTranslationZ = registrationTranslationZSlider.getValue();

            double registrationRotationX = registrationRotationXSlider.getValue() * Math.PI / 180.0;
            double registrationRotationY = registrationRotationYSlider.getValue() * Math.PI / 180.0;
            double registrationRotationZ = registrationRotationZSlider.getValue() * Math.PI / 180.0;


            //# check if something changed
            if (
                    formerDoNoiseAndBackgroundRemoval == doNoiseAndBackgroundRemoval &&
                            sigma1 == formerSigma1 &&
                            sigma2 == formerSigma2 &&
                            viewTranslationX == formerViewTranslationX &&
                            viewTranslationY == formerViewTranslationY &&
                            viewTranslationZ == formerViewTranslationZ &&
                            viewRotationX == formerViewRotationX &&
                            viewRotationY == formerViewRotationY &&
                            viewRotationZ == formerViewRotationZ &&
                            registrationTranslationX == formerRegistrationTranslationX &&
                            registrationTranslationY == formerRegistrationTranslationY &&
                            registrationTranslationZ == formerRegistrationTranslationZ &&
                            registrationRotationX == formerRegistrationRotationX &&
                            registrationRotationY == formerRegistrationRotationY &&
                            registrationRotationZ == formerRegistrationRotationZ &&
                            formerT1 == imp1.getFrame() &&
                            formerT2 == imp2.getFrame()
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

            if (formerT2 != imp2.getFrame()) {
                formerT2 = imp2.getFrame();
                //#push image to GPU
                ClearCLBuffer pushed = clijx.pushCurrentZStack(imp2);
                //#scale it initially;
                //depends on zoom factor and voxel size
                AffineTransform3D scaleTransform = new AffineTransform3D();
                scaleTransform.scale(scale2X, scale2Y, scale2Z);
                clijx.affineTransform3D(pushed, input2, scaleTransform);
                pushed.close();
                stillValid = false;
            }

            //# Noise/background removal
            if (formerDoNoiseAndBackgroundRemoval != doNoiseAndBackgroundRemoval ||
                    formerSigma1 != sigma1 ||
                    formerSigma2 != sigma2) {
                formerDoNoiseAndBackgroundRemoval = doNoiseAndBackgroundRemoval;
                formerSigma1 = sigma1;
                formerSigma2 = sigma2;
                stillValid = false;
            }

            if (!stillValid) {
                if (doNoiseAndBackgroundRemoval) {
                    clijx.blur(input1, temp1, sigma1, sigma1);
                    clijx.blur(input1, temp2, sigma2, sigma2);
                    clijx.subtract(temp1, temp2, dog1);
                    clijx.blur(input2, temp1, sigma1, sigma1);
                    clijx.blur(input2, temp2, sigma2, sigma2);
                    clijx.subtract(temp1, temp2, dog2);
                } else {
                    clijx.copy(input1, dog1);
                    clijx.copy(input2, dog2);
                }
            }

            //# Rigid transform
            if (!(viewTranslationX == formerViewTranslationX &&
                    viewTranslationY == formerViewTranslationY &&
                    viewTranslationZ == formerViewTranslationZ &&
                    viewRotationX == formerViewRotationX &&
                    viewRotationY == formerViewRotationY &&
                    viewRotationZ == formerViewRotationZ &&
                    registrationTranslationX == formerRegistrationTranslationX &&
                    registrationTranslationY == formerRegistrationTranslationY &&
                    registrationTranslationZ == formerRegistrationTranslationZ &&
                    registrationRotationX == formerRegistrationRotationX &&
                    registrationRotationY == formerRegistrationRotationY &&
                    registrationRotationZ == formerRegistrationRotationZ)) {
                stillValid = false;
            }

            formerViewTranslationX = viewTranslationX;
            formerViewTranslationY = viewTranslationY;
            formerViewTranslationZ = viewTranslationZ;
            formerViewRotationX = viewRotationX;
            formerViewRotationY = viewRotationY;
            formerViewRotationZ = viewRotationZ;
            formerRegistrationTranslationX = registrationTranslationX;
            formerRegistrationTranslationY = registrationTranslationY;
            formerRegistrationTranslationZ = registrationTranslationZ;
            formerRegistrationRotationX = registrationRotationX;
            formerRegistrationRotationY = registrationRotationY;
            formerRegistrationRotationZ = registrationRotationZ;

            at = new AffineTransform3D();
            at.translate(-transformed.getWidth() / 2, -transformed.getHeight() / 2, -transformed.getDepth() / 2);
            at.translate(viewTranslationX, viewTranslationY, viewTranslationZ);
            at.rotate(0, viewRotationX);
            at.rotate(1, viewRotationY);
            at.rotate(2, viewRotationZ);
            at.translate(transformed.getWidth() / 2, transformed.getHeight() / 2, transformed.getDepth() / 2);

            //# Execute operation on GPU
            clijx.affineTransform3D(dog1, transformed, at);

            //# Maximum Intensity Projection
            clijx.maximumXProjection(transformed, maxXProjection1);
            clijx.maximumYProjection(transformed, maxYProjection1);
            clijx.maximumZProjection(transformed, maxZProjection1);


            at = new AffineTransform3D();
            at.translate(-transformed.getWidth() / 2, -transformed.getHeight() / 2, -transformed.getDepth() / 2);
            at.translate(viewTranslationX, viewTranslationY, viewTranslationZ);
            at.rotate(0, viewRotationX);
            at.rotate(1, viewRotationY);
            at.rotate(2, viewRotationZ);
            at.translate(registrationTranslationX, registrationTranslationY, registrationTranslationZ);
            at.rotate(0, registrationRotationX);
            at.rotate(1, registrationRotationY);
            at.rotate(2, registrationRotationZ);
            at.translate(transformed.getWidth() / 2, transformed.getHeight() / 2, transformed.getDepth() / 2);

            //# Execute operation on GPU
            clijx.affineTransform3D(dog2, transformed, at);

            //# Maximum Intensity Projection
            clijx.maximumXProjection(transformed, maxXProjection2);
            clijx.maximumYProjection(transformed, maxYProjection2);
            clijx.maximumZProjection(transformed, maxZProjection2);

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

                initInteraction();

                interactionInitialized = true;
            }

            formerT1 = imp1.getFrame();
            formerT2 = imp2.getFrame();
            System.out.println("Time: " + (System.currentTimeMillis() - timeStamp) + " ms");

            stillValid = true;
            //System.out.println(clijx.reportMemory());
        }

        //# clean up
        clijx.clear();

        if (gdp.wasOKed()) {
            System.out.println("Translation X: " + registrationTranslationXSlider.getValue());
            System.out.println("Translation Y: " + registrationTranslationYSlider.getValue());
            System.out.println("Translation Z: " + registrationTranslationZSlider.getValue());
            System.out.println("Rotation X: " + registrationRotationXSlider.getValue());
            System.out.println("Rotation Y: " + registrationRotationYSlider.getValue());
            System.out.println("Rotation Z: " + registrationRotationZSlider.getValue());
            System.out.println("Zoom: " + zoom);
            System.out.println("Scale1X: " + scale1X);
            System.out.println("Scale1Y: " + scale1Y);
            System.out.println("Scale1Z: " + scale1Z);
            System.out.println("Scale2X: " + scale2X);
            System.out.println("Scale2Y: " + scale2Y);
            System.out.println("Scale2Z: " + scale2Z);
            System.out.println("Affine transform: " + Arrays.toString(at.getRowPackedCopy()));

            ResultsTable table = ResultsTable.getResultsTable();
            if (table == null) {
                table = new ResultsTable();
            }
            table.incrementCounter();

            table.addValue("Fixed_image", imp1.getTitle());
            table.addValue("Fixed_image_frame", imp1.getFrame());
            table.addValue("Moving_image", imp2.getTitle());
            table.addValue("Moving_image_frame", imp2.getFrame());
            table.addValue("Translation_X", registrationTranslationXSlider.getValue());
            table.addValue("Translation_Y", registrationTranslationYSlider.getValue());
            table.addValue("Translation_Z", registrationTranslationZSlider.getValue());
            table.addValue("Rotation_X", registrationRotationXSlider.getValue());
            table.addValue("Rotation_Y", registrationRotationYSlider.getValue());
            table.addValue("Rotation_Z", registrationRotationZSlider.getValue());
            table.addValue("Zoom", zoom);
            table.addValue("Scale1X", scale1X);
            table.addValue("Scale1Y", scale1Y);
            table.addValue("Scale1Z", scale1Z);
            table.addValue("Scale2X", scale2X);
            table.addValue("Scale2Y", scale2Y);
            table.addValue("Scale2Z", scale2Z);

            double[] matrix = at.getRowPackedCopy();
            for (int i = 0; i < matrix.length; i++) {
                table.addValue("m" + i, matrix[i]);
            }
            table.show("Results");
        }
    }

    ImagePlus impX = null;
    ImagePlus impY = null;
    ImagePlus impZ = null;

    class AffineTransformModificationState{
        public int mouseStartX = 0;
        public int mouseStartY = 0;
        public double translationX = 0;
        public double translationY = 0;
        public double translationZ = 0;
        public double rotationX = 0;
        public double rotationY = 0;
        public double rotationZ = 0;
    }

    final AffineTransformModificationState parameters = new AffineTransformModificationState();
    class MouseHandler extends MouseAdapter {
        @Override
        public void mouseDragged(MouseEvent e) {
            int deltaX = e.getX() - parameters.mouseStartX;
            int deltaY = e.getY() - parameters.mouseStartY;
            // System.out.println(e.);

            if (e.getSource() == impZ.getWindow().getCanvas()) {
                if (SwingUtilities.isLeftMouseButton(e)) {
                    registrationTranslationXSlider.setValue((int) (parameters.translationX + (deltaX * scale1X)));
                    registrationTranslationYSlider.setValue((int) (parameters.translationY + (deltaY * scale1Y)));
                } else {
                    registrationRotationYSlider.setValue((int) (parameters.rotationY + (deltaX)));
                    registrationRotationXSlider.setValue((int) (parameters.rotationX - (deltaY)));
                }
            } else if (e.getSource() == impX.getWindow().getCanvas()) {
                if (SwingUtilities.isLeftMouseButton(e)) {
                    registrationTranslationZSlider.setValue((int) (parameters.translationZ + (deltaX * scale1X)));
                    registrationTranslationYSlider.setValue((int) (parameters.translationY + (deltaY * scale1Y)));
                } else {
                    registrationRotationZSlider.setValue((int) (parameters.rotationZ - (deltaY)));
                    registrationRotationYSlider.setValue((int) (parameters.rotationY + (deltaX)));
                }
            } else if (e.getSource() == impY.getWindow().getCanvas()) {
                if (SwingUtilities.isLeftMouseButton(e)) {
                    registrationTranslationXSlider.setValue((int) (parameters.translationX + (deltaX * scale1X)));
                    registrationTranslationZSlider.setValue((int) (parameters.translationZ + (deltaY * scale1Y)));
                } else {
                    registrationRotationZSlider.setValue((int) (parameters.rotationZ + (deltaX)));
                    registrationRotationXSlider.setValue((int) (parameters.rotationX - (deltaY)));
                }
            }
        }

        @Override
        public void mousePressed(MouseEvent e) {
            initAffineTransformState(parameters, e);
        }

    }

    private void initInteraction() {
        IJ.setTool("hand");

        MouseHandler mouseHandler = new MouseHandler();
        for (ImagePlus imp : new ImagePlus[]{impX, impY, impZ}) {
            imp.getWindow().getCanvas().disablePopupMenu(false);
            while (imp.getWindow().getCanvas().getKeyListeners().length > 0) {
                imp.getWindow().getCanvas().removeKeyListener(imp.getWindow().getCanvas().getKeyListeners()[0]);
            }
            while (imp.getWindow().getCanvas().getMouseListeners().length > 0) {
                imp.getWindow().getCanvas().removeMouseListener(imp.getWindow().getCanvas().getMouseListeners()[0]);
            }
            imp.getWindow().getCanvas().addMouseMotionListener(mouseHandler);
            imp.getWindow().getCanvas().addMouseListener(mouseHandler);
            imp.getWindow().getCanvas().addKeyListener(new KeyAdapter() {
                @Override
                public void keyReleased(KeyEvent e) {
                    if (e.isShiftDown()) {
                        if (e.getKeyCode() == KeyEvent.VK_UP) {
                            registrationRotationXSlider.setValue(registrationRotationXSlider.getValue() - 1);
                        }
                        if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                            registrationRotationXSlider.setValue(registrationRotationXSlider.getValue() + 1);
                        }
                        if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                            registrationRotationZSlider.setValue(registrationRotationZSlider.getValue() - 1);
                        }
                        if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                            registrationRotationZSlider.setValue(registrationRotationZSlider.getValue() + 1);
                        }
                        if (e.getKeyCode() == KeyEvent.VK_PAGE_DOWN) {
                            registrationRotationYSlider.setValue(registrationRotationYSlider.getValue() + 1);
                        }
                        if (e.getKeyCode() == KeyEvent.VK_PAGE_UP) {
                            registrationRotationYSlider.setValue(registrationRotationYSlider.getValue() - 1);
                        }

                    } else {
                        if (e.getKeyCode() == KeyEvent.VK_UP) {
                            registrationTranslationYSlider.setValue(registrationTranslationYSlider.getValue() - 1);
                        }
                        if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                            registrationTranslationYSlider.setValue(registrationTranslationYSlider.getValue() + 1);
                        }
                        if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                            registrationTranslationXSlider.setValue(registrationTranslationXSlider.getValue() - 1);
                        }
                        if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                            registrationTranslationXSlider.setValue(registrationTranslationXSlider.getValue() + 1);
                        }
                        if (e.getKeyCode() == KeyEvent.VK_PAGE_DOWN) {
                            registrationTranslationZSlider.setValue(registrationTranslationZSlider.getValue() + 1);
                        }
                        if (e.getKeyCode() == KeyEvent.VK_PAGE_UP) {
                            registrationTranslationZSlider.setValue(registrationTranslationZSlider.getValue() - 1);
                        }
                    }
                }
            });
        }


    }

    private void initAffineTransformState(AffineTransformModificationState parameters, MouseEvent e) {
        parameters.mouseStartX = e.getX();
        parameters.mouseStartY = e.getY();
        parameters.translationX = registrationTranslationXSlider.getValue();
        parameters.translationY = registrationTranslationYSlider.getValue();
        parameters.translationZ = registrationTranslationZSlider.getValue();
        parameters.rotationX = registrationRotationXSlider.getValue();
        parameters.rotationY = registrationRotationYSlider.getValue();
        parameters.rotationZ = registrationRotationZSlider.getValue();
    }

    public static void main(String... args){
        String data_folder = "C:/structure/data/florence/";

        new ImageJ();

        ImagePlus imp1 = IJ.openImage(data_folder + "000330.raw.tif");
        ImagePlus imp2 = IJ.openImage(data_folder + "000350.raw.tif");

        imp1.show();
        imp2.show();

        new ImageRegistration3D().run("");
        System.exit(0);
    }
}
