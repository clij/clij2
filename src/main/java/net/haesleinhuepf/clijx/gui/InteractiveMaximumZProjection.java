package net.haesleinhuepf.clijx.gui;

import ij.IJ;
import ij.ImageListener;
import ij.ImagePlus;
import ij.WindowManager;
import ij.gui.GenericDialog;
import ij.measure.Calibration;
import ij.plugin.filter.PlugInFilter;
import ij.process.ImageProcessor;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clijx.CLIJx;
import net.imglib2.realtransform.AffineTransform3D;


import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * InteractiveMaximumZProjection
 * <p>
 * <p>
 * <p>
 * Author: @haesleinhuepf
 * 12 2019
 */
public class InteractiveMaximumZProjection implements PlugInFilter, ImageListener {

    CLIJx clijx;
    ClearCLBuffer myBuffer;
    ClearCLBuffer transformed;
    ClearCLBuffer myMaxProjection;
    int old_max_z = -1;
    double old_angleX = -1;
    double old_angleY = -1;
    double angleX = 0;
    double angleY = 0;

    double angleStartX = 0;
    double angleStartY = 0;

    int mouseStartX = 0;
    int mouseStartY = 0;

    double zoom = 1.0;

    float scale1X = 1.0f;
    float scale1Y = 1.0f;
    float scale1Z = 1.0f;

    String projection = "Max";

    @Override
    public int setup(String arg, ImagePlus imp) {
        return PlugInFilter.DOES_ALL;
    }

    @Override
    public void run(ImageProcessor ip) {
        ImagePlus imp = IJ.getImage();
        if (imp == null) {
            return;
        }

        GenericDialog gd = new GenericDialog("Interactive Z Projection");
        gd.addNumericField("Zoom (the smaller, the faster)", zoom, 2);
        gd.addChoice("Projection", new String[]{"Max", "Min", "Mean"}, projection);
        gd.showDialog();
        if (gd.wasCanceled()) {
            return;
        }
        zoom = gd.getNextNumber();
        projection = gd.getNextChoice();

        Calibration calib = imp.getCalibration();
        scale1X = (float) (calib.pixelWidth * zoom);
        scale1Y = (float) (calib.pixelHeight * zoom);
        scale1Z = (float) (calib.pixelDepth * zoom);

        clijx = CLIJx.getInstance();
        my_source = imp;
        myBuffer = clijx.push(imp);
        //transformed = clijx.create(new long[]{myBuffer.getWidth(), myBuffer.getHeight(), (long)(myBuffer.getDepth() * 1.5)}, myBuffer.getNativeType());
        transformed = clijx.create(new long[]{
                (long) (myBuffer.getWidth() * scale1X),
                (long) (myBuffer.getHeight() * scale1Y),
                (long) (myBuffer.getDepth() * scale1Z)}, myBuffer.getNativeType());
        myMaxProjection = clijx.create(new long[]{transformed.getWidth(), transformed.getHeight()}, transformed.getNativeType());
        refresh();
        ImagePlus.addImageListener(this);
    }

    private ImagePlus my_display = null;
    private ImagePlus my_source = null;
    private void refresh() {
        synchronized (this) {
            int min_z = 0;
            int max_z = my_source.getZ() - 1;
            if (old_max_z != max_z || old_angleX != angleX || old_angleY != angleY) {
                String window_title = my_source.getTitle() + " Interactive Maximum Z projection";

                if (old_angleX != angleX || old_angleY != angleY) {
                    AffineTransform3D at = new AffineTransform3D();
                    at.scale(scale1X, scale1Y, scale1Z);
                    at.translate(-transformed.getWidth() / 2, -transformed.getHeight() / 2, 0);
                    at.rotate(0, angleX / 180.0 * Math.PI);
                    at.rotate(1, angleY / 180.0 * Math.PI);
                    at.translate(transformed.getWidth() / 2, transformed.getHeight() / 2, 0);

                    //# Execute operation on GPU
                    clijx.affineTransform3D(myBuffer, transformed, at);

                }

                if (projection.compareTo("Max") == 0) {
                    clijx.maximumZProjectionBounded(transformed, myMaxProjection, min_z, max_z);
                } else if (projection.compareTo("Min") == 0) {
                    clijx.minimumZProjectionBounded(transformed, myMaxProjection, min_z, max_z);
                } else if (projection.compareTo("Mean") == 0) {
                    clijx.meanZProjectionBounded(transformed, myMaxProjection, min_z, max_z);
                }
                clijx.showGrey(myMaxProjection, window_title);

                if (my_display == null) {
                    my_display = WindowManager.getImage(window_title);
                    my_display.getWindow().getCanvas().disablePopupMenu(false);
                    while (my_display.getWindow().getCanvas().getMouseListeners().length > 0) {
                        my_display.getWindow().getCanvas().removeMouseListener(my_display.getWindow().getCanvas().getMouseListeners()[0]);
                    }
                    my_display.getWindow().getCanvas().addMouseMotionListener(new MouseAdapter() {
                        @Override
                        public void mouseDragged(MouseEvent e) {
                            double deltaX = e.getX() - mouseStartX;
                            double deltaY = e.getY() - mouseStartY;

                            angleY = angleStartY - deltaX / 5;
                            angleX = angleStartX + deltaY / 5;

                            refresh();
                            System.out.println("Refreshing...");
                        }
                    });
                    my_display.getWindow().getCanvas().addMouseListener(new MouseAdapter() {
                        @Override
                        public void mousePressed(MouseEvent e) {
                            angleStartX = angleX;
                            angleStartY = angleY;
                            mouseStartX = e.getX();
                            mouseStartY = e.getY();
                        }
                    });
                }

                if (projection.compareTo("Mean") != 0) {
                    my_display.setDisplayRange(my_source.getDisplayRangeMin(), my_source.getDisplayRangeMax());
                }
                old_max_z = max_z;
                old_angleY = angleY;
                old_angleX = angleX;
            }
        }
    }

    private void finish() {
        my_source = null;
        my_display = null;

        ImagePlus.removeImageListener(this);
        clijx.release(myBuffer);
        clijx.release(myMaxProjection);
        clijx.release(transformed);
        clijx = null;
    }

    @Override
    public void imageOpened(ImagePlus imp) {

    }

    @Override
    public void imageClosed(ImagePlus imp) {
        if (imp == my_source || imp == my_display) {
            finish();
        }
    }

    @Override
    public void imageUpdated(ImagePlus imp) {
        if (imp == my_source) {
            refresh();
        }
    }
}
