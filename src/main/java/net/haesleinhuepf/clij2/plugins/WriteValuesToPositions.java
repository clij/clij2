package net.haesleinhuepf.clij2.plugins;

import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.macro.CLIJMacroPlugin;
import net.haesleinhuepf.clij.macro.CLIJOpenCLProcessor;
import net.haesleinhuepf.clij.macro.documentation.OffersDocumentation;
import net.haesleinhuepf.clij2.AbstractCLIJ2Plugin;
import net.haesleinhuepf.clij2.CLIJ2;
import org.scijava.plugin.Plugin;

import java.util.HashMap;

/**
 * Author: @haesleinhuepf
 * June 2019
 */
@Plugin(type = CLIJMacroPlugin.class, name = "CLIJ2_writeValuesToPositions")
public class WriteValuesToPositions extends AbstractCLIJ2Plugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation {

    @Override
    public boolean executeCL() {
        ClearCLBuffer positionsAndValues = (ClearCLBuffer)( args[0]);
        ClearCLBuffer buffer = (ClearCLBuffer)( args[1]);

        return writeValuesToPositions(getCLIJ2(), positionsAndValues, buffer);
    }

    public static boolean writeValuesToPositions(CLIJ2 clij2, ClearCLBuffer positionsAndValues, ClearCLBuffer dst) {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", positionsAndValues);
        parameters.put("dst", dst);

        long[] size = new long[] { positionsAndValues.getWidth(), 1, 1};
        clij2.execute(WriteValuesToPositions.class, "write_values_to_positions_3d_x.cl", "write_values_to_positions_" + dst.getDimension() + "d", size, size, parameters);
        return true;
    }

    @Override
    public String getParameterHelpText() {
        return "Image positionsAndValues, ByRef Image destination";
    }

    @Override
    public String getDescription() {
        return "Takes an image with three/four rows (2D: height = 3; 3D: height = 4): x, y [, z] and v and target image. \n\n" +
                "The value v will be written at position x/y[/z] in the target image.";
    }

    @Override
    public String getAvailableForDimensions() {
        return "2D, 3D";
    }

}
