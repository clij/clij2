package net.haesleinhuepf.clijx.gui;

import fiji.util.gui.GenericDialogPlus;
import ij.IJ;
import ij.ImageJ;
import ij.ImagePlus;
import ij.WindowManager;
import ij.measure.Calibration;
import ij.plugin.PlugIn;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clijx.CLIJx;
import net.imglib2.realtransform.AffineTransform3D;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * ImageRegistration3D
 * <p>
 * <p>
 * <p>
 * Author: @haesleinhuepf
 * 11 2019
 */
public class ImageRegistration3D implements PlugIn {

    Scrollbar registrationTranslationXSlider = null;
    Scrollbar registrationTranslationYSlider = null;
    Scrollbar registrationTranslationZSlider = null;

    Scrollbar registrationRotationXSlider = null;
    Scrollbar registrationRotationYSlider = null;
    Scrollbar registrationRotationZSlider = null;

    @Override
    public void run(String arg) {
        GenericDialogPlus gdp = new GenericDialogPlus("3D Image Registration");
        gdp.addImageChoice("Fixed image", IJ.getImage().getTitle());
        gdp.addImageChoice("Moving image", IJ.getImage().getTitle());
        gdp.addNumericField("Zoom (influences speed and registration parameters)", 1, 1);
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
        float scale1X = (float) (calib.pixelWidth / calib.pixelDepth * zoom);
        float scale1Y = (float) (calib.pixelHeight / calib.pixelDepth * zoom);
        float scale1Z = (float) (1.0 * zoom);
        calib = imp2.getCalibration();
        float scale2X = (float) (calib.pixelWidth / calib.pixelDepth * zoom);
        float scale2Y = (float) (calib.pixelHeight / calib.pixelDepth * zoom);
        float scale2Z = (float) (1.0 * zoom);


        //# initialize state
        ClearCLBuffer input1 = null;
        ClearCLBuffer input2 = null;
        int formerT1 = -1;
        int formerT2 = -1;

        //# build up user interface
        gdp = new GenericDialogPlus("Image registration");
        gdp.addMessage("Noise and background subtraction (DoG)");
        gdp.addCheckbox("Do noise and background subtraction ", formerDoNoiseAndBackgroundRemoval);
        gdp.addSlider("Sigma 1 (in 0.1 pixel)", 0, 100, formerSigma1);
        gdp.addSlider("Sigma 2 (in 0.1 pixel)", 0, 100, formerSigma2);
        gdp.addMessage("Rigid transform");
        gdp.addSlider("View Translation X (in pixel)", -100, 100, formerViewTranslationX);
        gdp.addSlider("View Translation Y (in pixel)", -100, 100, formerViewTranslationY);
        gdp.addSlider("View Translation Z (in pixel)", -100, 100, formerViewTranslationZ);
        gdp.addSlider("View Rotation X (in degrees)", -180, 180, formerViewRotationX);
        gdp.addSlider("View Rotation Y (in degrees)", -180, 180, formerViewRotationY);
        gdp.addSlider("View Rotation Z (in degrees)", -180, 180, formerViewRotationZ);

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

        //# loop until user closed the dialog
        boolean stillValid = false;
        while ((! gdp.wasCanceled()) && (!gdp.wasOKed())) {

            // # reserve memory for input and output images
            if (input1 == null) {
                input1 = clijx.create(new long[]{
                        (long) (imp1.getWidth() * scale1X),
                        (long) (imp1.getHeight() * scale1Y),
                        (long) (imp1.getNSlices() * scale1Z)}, clijx.Float);
            }
            input2 = clijx.create(new long[]{
                    (long) (imp2.getWidth() * scale2X),
                    (long) (imp2.getHeight() * scale2Y),
                    (long) (imp2.getNSlices() * scale2Z)}, clijx.Float);

            ClearCLBuffer temp1 = clijx.create(input1);
            ClearCLBuffer temp2 = clijx.create(input2);
            ClearCLBuffer dog1 = clijx.create(input1);
            ClearCLBuffer dog2 = clijx.create(input2);
            ClearCLBuffer transformed = clijx.create(input1);
            ClearCLBuffer transformedX = clijx.create(new long[]{input1.getHeight(), input1.getDepth(), input1.getWidth()}, input1.getNativeType());
            ClearCLBuffer transformedY = clijx.create(new long[]{input1.getWidth(), input1.getDepth(), input1.getHeight()}, input1.getNativeType());


            ClearCLBuffer maxXProjection1 = clijx.create(new long[]{transformedX.getWidth(), transformedX.getHeight()}, input1.getNativeType());
            ClearCLBuffer maxXProjection2 = clijx.create(new long[]{transformedX.getWidth(), transformedX.getHeight()}, input2.getNativeType());
            ClearCLBuffer maxYProjection1 = clijx.create(new long[]{transformedY.getWidth(), transformedY.getHeight()}, input1.getNativeType());
            ClearCLBuffer maxYProjection2 = clijx.create(new long[]{transformedY.getWidth(), transformedY.getHeight()}, input2.getNativeType());
            ClearCLBuffer maxZProjection1 = clijx.create(new long[]{transformed.getWidth(), transformed.getHeight()}, input1.getNativeType());
            ClearCLBuffer maxZProjection2 = clijx.create(new long[]{transformed.getWidth(), transformed.getHeight()}, input2.getNativeType());

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

            AffineTransform3D at = new AffineTransform3D();
            at.translate(-transformed.getWidth() / 2, -transformed.getHeight() / 2, -transformed.getDepth() / 2);
            at.translate(viewTranslationX, viewTranslationY, viewTranslationZ);
            at.rotate(0, viewRotationX);
            at.rotate(1, viewRotationY);
            at.rotate(2, viewRotationZ);
            at.translate(transformed.getWidth() / 2, transformed.getHeight() / 2, transformed.getDepth() / 2);

            //# Execute operation on GPU
            clijx.affineTransform3D(dog1, transformed, at);
            clijx.resliceLeft(transformed, transformedX);
            clijx.resliceTop(transformed, transformedY);

            //# Maximum Intensity Projection
            clijx.maximumZProjection(transformedX, maxXProjection1);
            clijx.maximumZProjection(transformedY, maxYProjection1);
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
            clijx.resliceLeft(transformed, transformedX);
            clijx.resliceTop(transformed, transformedY);

            //# Maximum Intensity Projection
            clijx.maximumZProjection(transformedX, maxXProjection2);
            clijx.maximumZProjection(transformedY, maxYProjection2);
            clijx.maximumZProjection(transformed, maxZProjection2);

            //# Result visualisation
            clijx.showRGB(maxXProjection1, maxXProjection2, maxXProjection2, "fused X");
            clijx.showRGB(maxYProjection1, maxYProjection2, maxYProjection2, "fused Y");
            clijx.showRGB(maxZProjection1, maxZProjection2, maxZProjection2, "fused Z");

            initInteraction();


            formerT1 = imp1.getFrame();
            formerT2 = imp2.getFrame();
            System.out.println("Time: " + (System.currentTimeMillis() - timeStamp) + " ms");

            stillValid = true;
            //System.out.println(clijx.reportMemory());
        }

        //# clean up
        clijx.clear();
    }

    private void initInteraction() {
        //ImagePlus impX = WindowManager.getImage("fused X");
        ImagePlus impZ = WindowManager.getImage("fused Z");

        final int[] parameters = new int[2];

        impZ.getWindow().getCanvas().addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {

            }

            @Override
            public void mousePressed(MouseEvent e) {
                parameters[0] = e.getX();
                parameters[1] = e.getY();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                int deltaX = e.getX() - parameters[0];
                int deltaY = e.getX() - parameters[1];
                registrationTranslationXSlider.setValue(registrationTranslationXSlider.getValue() + deltaX);
                registrationTranslationYSlider.setValue(registrationTranslationYSlider.getValue() + deltaY);
            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });


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
