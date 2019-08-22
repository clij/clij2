package net.haesleinhuepf.clij2;

import ij.ImagePlus;
import net.haesleinhuepf.clij.CLIJ;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij2.utilities.CLIJ2Ops;

/**
 * The CLIJ2 gatewey
 */
public class CLIJ2 {
    private CLIJ clij;
    private static CLIJ2 instance;
    private CLIJ2Ops ops;

    /**
     * Marking this as deprecated as it will very likely go away before release.
     * Use CLIJ2.getInstance() instead.
     * @param clij
     */
    @Deprecated
    public CLIJ2(CLIJ clij) {
        this.clij = clij;
        ops = new CLIJ2Ops(clij);
    }

    public static CLIJ2 getInstance() {
        if (instance == null) {
            instance = new CLIJ2(CLIJ.getInstance());
        }
        return instance;
    }

    public static CLIJ2 getInstance(String id) {
        if (instance == null) {
            instance = new CLIJ2(CLIJ.getInstance(id));
        }
        return instance;
    }

    public String getGPUName() {
        return clij.getGPUName();
    }

    public double getOpenCLVersion() {
        return clij.getOpenCLVersion();
    }

    public ClearCLBuffer push(Object object) {
        return clij.convert(object, ClearCLBuffer.class);
    }

    public ImagePlus pull(Object object) {
        return clij.convert(object, ImagePlus.class);
    }

    public void show(Object object, String title) {
        ImagePlus imp = clij.convert(object, ImagePlus.class);
        clij.show(imp, title);
    }

    public <T> T convert(Object object, Class<T> klass) {
        return clij.convert(object, klass);
    }

    public CLIJ2Ops ops() {
        return ops;
    }
}
