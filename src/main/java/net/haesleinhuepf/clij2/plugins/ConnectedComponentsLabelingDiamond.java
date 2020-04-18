package net.haesleinhuepf.clij2.plugins;

import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.clearcl.interfaces.ClearCLImageInterface;
import net.haesleinhuepf.clij.macro.CLIJMacroPlugin;
import net.haesleinhuepf.clij2.CLIJ2;
import org.scijava.plugin.Plugin;

//import net.haesleinhuepf.clijx.CLIJx;

/**
 * ConnectedComponentsLabeling
 * <p>
 * <p>
 * <p>
 * Author: @haesleinhuepf
 * 06 2019
 */
@Plugin(type = CLIJMacroPlugin.class, name = "CLIJ2_connectedComponentsLabelingDiamond")
public class ConnectedComponentsLabelingDiamond extends ConnectedComponentsLabeling {

    @Override
    public boolean executeCL() {
        Object[] args = openCLBufferArgs();
        ClearCLBuffer input = (ClearCLBuffer) args[0];
        ClearCLBuffer output = (ClearCLBuffer) args[1];

        boolean result = connectedComponentsLabelingDiamond(getCLIJ2(), input, output);
        releaseBuffers(args);
        return result;
    }

    public static boolean connectedComponentsLabelingDiamond(CLIJ2 clij2, ClearCLImageInterface input, ClearCLImageInterface output) {
        return connectedComponentsLabeling_internal(clij2, input, output, true, true);
    }

    public static boolean connectedComponentsLabelingDiamond(CLIJ2 clij2, ClearCLImageInterface input, ClearCLImageInterface output, boolean forceContinousLabeling) {
        return connectedComponentsLabeling_internal(clij2, input, output, true, forceContinousLabeling);
    }

    @Override
    public String getDescription() {
        return "Performs connected components analysis inspecting the diamond neighborhood of every pixel to a binary image and generates a label map.";
    }
}
