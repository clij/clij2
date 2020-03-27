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
 * June 2019
 */
@Plugin(type = CLIJMacroPlugin.class, name = "CLIJ2_getDimensions")
public class GetDimensions extends AbstractCLIJ2Plugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation {

    @Override
    public boolean executeCL() {
        ClearCLBuffer buffer = (ClearCLBuffer)( args[0]);

        long[] dimensions = getDimensions(getCLIJ2(), buffer);

        ((Double[])args[1])[0] = (double)dimensions[0];
        ((Double[])args[1])[1] = (double)dimensions[1];
        if (buffer.getDimension() > 2) {
            ((Double[])args[1])[2] = (double)dimensions[2];
        } else {
            ((Double[])args[1])[2] = 1.0;
        }
        return true;
    }

    public static long[] getDimensions(CLIJ2 clij2, ClearCLBuffer buffer) {
        if (buffer.getDimension() == 3) {
            return new long[]{buffer.getWidth(), buffer.getHeight(), buffer.getDepth()};
        } else {
            return new long[]{buffer.getWidth(), buffer.getHeight()};
        }
    }



    @Override
    public String getParameterHelpText() {
        return "Image source, ByRef Number width, ByRef Number height, ByRef Number depth";
    }

    @Override
    public String getDescription() {
        return "Reads out the size of an image [stack] and writes it to the" +
                " parameters 'width', 'height' and 'depth'.";
    }

    @Override
    public String getAvailableForDimensions() {
        return "2D, 3D";
    }

}
