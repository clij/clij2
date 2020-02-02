package net.haesleinhuepf.clijx;

import ij.IJ;
import ij.ImagePlus;
import net.haesleinhuepf.clij.CLIJ;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.clearcl.ClearCLImage;
import net.haesleinhuepf.clij.clearcl.ClearCLKernel;
import net.haesleinhuepf.clij.clearcl.enums.ImageChannelDataType;
import net.haesleinhuepf.clij.clearcl.interfaces.ClearCLImageInterface;
import net.haesleinhuepf.clij.clearcl.util.ElapsedTime;
import net.haesleinhuepf.clij.converters.implementations.ClearCLBufferToImagePlusConverter;
import net.haesleinhuepf.clij.coremem.enums.NativeTypeEnum;
import net.haesleinhuepf.clij.utilities.TypeFixer;
import net.haesleinhuepf.clij2.CLIJ2;
import net.haesleinhuepf.clijx.utilities.CLIJxOps;
import net.haesleinhuepf.clijx.utilities.CLKernelExecutor;
import net.imglib2.Cursor;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.type.numeric.RealType;
import net.imglib2.view.Views;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * The CLIJx gateway
 */
public class CLIJx extends CLIJ2 implements CLIJxOps {
    private static CLIJx instance;

    public CLIJx getCLIJx() {
        return this;
    }


    /**
     * Marking this as deprecated as it will very likely go away before release.
     * Use CLIJx.getInstance() instead.
     * @param clij
     */
    @Deprecated
    public CLIJx(CLIJ clij) {
        super(clij);
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

    public CLIJx __enter__() {
        clear();
        return this;
    }

}
