package net.haesleinhuepf.clijx.advancedfilters;

import net.haesleinhuepf.clij.CLIJ;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.macro.AbstractCLIJPlugin;
import net.haesleinhuepf.clij.macro.CLIJMacroPlugin;
import net.haesleinhuepf.clij.macro.CLIJOpenCLProcessor;
import net.haesleinhuepf.clij.macro.documentation.OffersDocumentation;
import org.scijava.plugin.Plugin;

import java.util.HashMap;

/**
 * Author: @haesleinhuepf
 * June 2019
 */
@Plugin(type = CLIJMacroPlugin.class, name = "CLIJx_writeValuesToPositions")
public class WriteValuesToPositions extends AbstractCLIJPlugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation {

    @Override
    public boolean executeCL() {

        Object[] args = openCLBufferArgs();

        ClearCLBuffer positionsAndValues = (ClearCLBuffer)( args[0]);
        ClearCLBuffer buffer = (ClearCLBuffer)( args[1]);

        return writeValuesToPositions(clij, positionsAndValues, buffer);
    }

    public static boolean writeValuesToPositions(CLIJ clij, ClearCLBuffer positionsAndValues, ClearCLBuffer dst) {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", positionsAndValues);
        parameters.put("dst", dst);

        long[] size = new long[] { positionsAndValues.getWidth(), 1, 1};
        return clij.execute(WriteValuesToPositions.class, "writeValuesToPositions.cl", "write_values_to_positions_" + dst.getDimension() + "d", size, parameters);
    }

    @Override
    public String getParameterHelpText() {
        return "Image positionsAndValues, Image destination";
    }

    @Override
    public String getDescription() {
        return "Takes an image with three/four rows (2D: height = 3; 3D: height = 4): x, y [, z] and v and target image. " +
                "The value v will be written at position x/y[/z] in the target image.";
    }

    @Override
    public String getAvailableForDimensions() {
        return "2D, 3D";
    }

}
