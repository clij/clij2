package net.haesleinhuepf.clij2.plugins;

import ij.IJ;
import ij.ImageJ;
import ij.ImagePlus;
import ij.gui.WaitForUserDialog;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.clearcl.ClearCLKernel;
import net.haesleinhuepf.clij.clearcl.interfaces.ClearCLImageInterface;
import net.haesleinhuepf.clij.coremem.enums.NativeTypeEnum;
import net.haesleinhuepf.clij.macro.CLIJMacroPlugin;
import net.haesleinhuepf.clij.macro.CLIJOpenCLProcessor;
import net.haesleinhuepf.clij.macro.documentation.OffersDocumentation;
import net.haesleinhuepf.clij2.AbstractCLIJ2Plugin;
import net.haesleinhuepf.clij2.CLIJ2;
import org.scijava.plugin.Plugin;

import java.nio.ByteBuffer;

//import net.haesleinhuepf.clijx.CLIJx;

/**
 * ConnectedComponentsLabeling
 * <p>
 * <p>
 * <p>
 * Author: @haesleinhuepf
 * 06 2019
 */
@Plugin(type = CLIJMacroPlugin.class, name = "CLIJ2_connectedComponentsLabelingBox")
public class ConnectedComponentsLabelingBox extends ConnectedComponentsLabeling {

    @Override
    public boolean executeCL() {
        Object[] args = openCLBufferArgs();
        ClearCLBuffer input = (ClearCLBuffer) args[0];
        ClearCLBuffer output = (ClearCLBuffer) args[1];

        boolean result = connectedComponentsLabelingBox(getCLIJ2(), input, output);
        releaseBuffers(args);
        return result;
    }
    public static boolean connectedComponentsLabelingBox(CLIJ2 clij2, ClearCLImageInterface input, ClearCLImageInterface output) {
        return connectedComponentsLabeling_internal(clij2, input, output, true, true);
    }

    public static boolean connectedComponentsLabelingBox(CLIJ2 clij2, ClearCLImageInterface input, ClearCLImageInterface output, boolean forceContinousLabeling) {
        return connectedComponentsLabeling_internal(clij2, input, output, true, forceContinousLabeling);
    }

}
