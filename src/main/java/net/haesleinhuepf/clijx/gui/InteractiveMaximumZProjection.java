package net.haesleinhuepf.clijx.gui;

import ij.IJ;
import ij.ImageListener;
import ij.ImagePlus;
import ij.WindowManager;
import ij.plugin.filter.PlugInFilter;
import ij.process.ImageProcessor;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clijx.CLIJx;

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
    ClearCLBuffer myMaxProjection;
    int old_max_z = -1;

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

        clijx = CLIJx.getInstance();
        my_source = imp;
        myBuffer = clijx.push(imp);
        myMaxProjection = clijx.create(new long[]{myBuffer.getWidth(), myBuffer.getHeight()}, myBuffer.getNativeType());
        refresh();
        ImagePlus.addImageListener(this);
    }

    private ImagePlus my_display = null;
    private ImagePlus my_source = null;
    private void refresh() {
        synchronized (this) {
            int min_z = 0;
            int max_z = my_source.getZ() - 1;
            if (old_max_z != max_z) {
                String window_title = my_source.getTitle() + " Interactive Maximum Z projection";

                clijx.projectMaximumZBounded(myBuffer, myMaxProjection, min_z, max_z);
                clijx.showGrey(myMaxProjection, window_title);

                my_display = WindowManager.getImage(window_title);
                old_max_z = max_z;
            }
        }
    }

    private void finish() {
        my_source = null;
        my_display = null;

        ImagePlus.removeImageListener(this);
        clijx.release(myBuffer);
        clijx.release(myMaxProjection);
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
