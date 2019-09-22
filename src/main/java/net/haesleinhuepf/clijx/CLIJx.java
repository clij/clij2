package net.haesleinhuepf.clijx;

import ij.ImagePlus;
import net.haesleinhuepf.clij.CLIJ;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.clearcl.ClearCLKernel;
import net.haesleinhuepf.clij.clearcl.util.ElapsedTime;
import net.haesleinhuepf.clij.coremem.enums.NativeTypeEnum;
import net.haesleinhuepf.clij.utilities.TypeFixer;
import net.haesleinhuepf.clijx.utilities.CLIJxOps;
import net.haesleinhuepf.clijx.utilities.CLKernelExecutor;

import java.io.IOException;
import java.util.HashMap;

/**
 * The CLIJx gateway
 */
public class CLIJx {
    private CLIJ clij;
    private static CLIJx instance;
    public final CLIJxOps op;

    private final CLKernelExecutor mCLKernelExecutor;

    /**
     * Marking this as deprecated as it will very likely go away before release.
     * Use CLIJx.getInstance() instead.
     * @param clij
     */
    @Deprecated
    public CLIJx(CLIJ clij) {
        this.clij = clij;
        op = new CLIJxOps(this);
        mCLKernelExecutor = new CLKernelExecutor(clij.getClearCLContext());
    }

    public static CLIJx getInstance() {
        CLIJ clij = CLIJ.getInstance();
        if (instance == null || instance.clij != CLIJ.getInstance()) {
            instance = new CLIJx(clij);
        }
        return instance;
    }

    public static CLIJx getInstance(String id) {
        CLIJ clij = CLIJ.getInstance(id);
        if (instance == null || instance.clij != clij) {
            instance = new CLIJx(clij);
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

    public void execute(Class anchorClass, String pProgramFilename, String pKernelname, long[] dimensions, long[] globalsizes, HashMap<String, Object> parameters) {
        ClearCLKernel kernel = executeSubsequently(anchorClass, pProgramFilename, pKernelname,  dimensions, globalsizes, parameters, null);
        kernel.close();
    }

    public ClearCLKernel executeSubsequently(Class anchorClass, String pProgramFilename, String pKernelname, long[] dimensions, long[] globalsizes, HashMap<String, Object> parameters, ClearCLKernel kernel) {
        final ClearCLKernel[] result = {kernel};

        if (CLIJ.debug) {
            for (String key : parameters.keySet()) {
                System.out.println(key + " = " + parameters.get(key));
            }
        }

        ElapsedTime.measure("kernel + build " + pKernelname, () -> {
            mCLKernelExecutor.setProgramFilename(pProgramFilename);
            mCLKernelExecutor.setKernelName(pKernelname);
            mCLKernelExecutor.setAnchorClass(anchorClass);
            mCLKernelExecutor.setParameterMap(parameters);
            mCLKernelExecutor.setGlobalSizes(globalsizes);

            result[0] = mCLKernelExecutor.enqueue(true, kernel);
        });

        return result[0];
    }

    @Deprecated
    public CLIJ getClij() {
        return clij;
    }
}
