package net.haesleinhuepf.clij2.plugins;

import ij.measure.ResultsTable;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.macro.CLIJMacroPlugin;
import net.haesleinhuepf.clij.macro.CLIJOpenCLProcessor;
import net.haesleinhuepf.clij.macro.documentation.OffersDocumentation;
import net.haesleinhuepf.clij2.AbstractCLIJ2Plugin;
import net.haesleinhuepf.clij2.CLIJ2;
import org.scijava.plugin.Plugin;

/**
 * Author: @haesleinhuepf
 *         April 2020
 */
@Plugin(type = CLIJMacroPlugin.class, name = "CLIJ2_getBoundingBox")
public class GetBoundingBox extends AbstractCLIJ2Plugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation {

    @Override
    public boolean executeCL() {
        ClearCLBuffer buffer1 = (ClearCLBuffer)( args[0]);
        double[] boundingBox = getCLIJ2().getBoundingBox(buffer1);

        for (int v = 0; v < 6; v++) {
            ((Double[]) args[v + 1])[0] = boundingBox[v];
        }
        return true;
    }

    public static double[] getBoundingBox(CLIJ2 clij2, ClearCLBuffer buffer) {
        return clij2.boundingBox(buffer);
    }

    @Override
    public String getParameterHelpText() {
        return "Image source, ByRef Number boundingBoxX, ByRef Number boundingBoxY, ByRef Number boundingBoxZ, ByRef Number boundingBoxWidth, ByRef Number boundingBoxHeight, ByRef Number boundingBoxDepth";
    }

    @Override
    public String getDescription() {
        return "Determines the bounding box of all non-zero pixels in a binary image. \n\nIf called from macro, the " +
                "positions will be stored in the variables 'boundingBoxX', " +
                "'boundingBoxY', 'boundingBoxZ', 'boundingBoxWidth', 'boundingBoxHeight' and 'boundingBoxDepth'." +
                "In case of 2D images Z and depth will be zero.";
    }

    @Override
    public String getAvailableForDimensions() {
        return "2D, 3D";
    }

}
