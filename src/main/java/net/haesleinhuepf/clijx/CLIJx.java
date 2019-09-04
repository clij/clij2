package net.haesleinhuepf.clijx;

import ij.ImagePlus;
import net.haesleinhuepf.clij.CLIJ;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.coremem.enums.NativeTypeEnum;
import net.haesleinhuepf.clijx.utilities.CLIJxOps;

/**
 * The CLIJx gateway
 */
public class CLIJx {
    private CLIJ clij;
    private static CLIJx instance;
    public final CLIJxOps op;

    /**
     * Marking this as deprecated as it will very likely go away before release.
     * Use CLIJx.getInstance() instead.
     * @param clij
     */
    @Deprecated
    public CLIJx(CLIJ clij) {
        this.clij = clij;
        op = new CLIJxOps(clij);
    }

    public static CLIJx getInstance() {
        if (instance == null) {
            instance = new CLIJx(CLIJ.getInstance());
        }
        return instance;
    }

    public static CLIJx getInstance(String id) {
        if (instance == null) {
            instance = new CLIJx(CLIJ.getInstance(id));
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

    public ClearCLBuffer create(ClearCLBuffer buffer) {
        return clij.create(buffer);
    }


    public ClearCLBuffer create(long[] dimensions) {
        return create(dimensions, NativeTypeEnum.Float);
    }

    public ClearCLBuffer create(double[] dblDimensions) {
        long[] dimensions = new long[dblDimensions.length];
        for (int i = 0; i < dimensions.length; i++) {
            dimensions[i] = (long)dblDimensions[i];
        }
        return create(dimensions, NativeTypeEnum.Float);
    }


    public ClearCLBuffer create(long[] dimensions, NativeTypeEnum typeEnum) {
        return clij.create(dimensions, typeEnum);
    }

    /**
     * use op without brackets instead.
     * @return
     */
    @Deprecated
    public CLIJxOps op() {
        return op;
    }
}
