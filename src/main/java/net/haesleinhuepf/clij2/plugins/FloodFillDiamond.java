package net.haesleinhuepf.clij2.plugins;

import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.clearcl.ClearCLKernel;
import net.haesleinhuepf.clij.macro.CLIJMacroPlugin;
import net.haesleinhuepf.clij.macro.CLIJOpenCLProcessor;
import net.haesleinhuepf.clij.macro.documentation.OffersDocumentation;
import net.haesleinhuepf.clij2.AbstractCLIJ2Plugin;
import net.haesleinhuepf.clij2.CLIJ2;
import net.haesleinhuepf.clij2.utilities.HasAuthor;
import net.haesleinhuepf.clij2.utilities.HasClassifiedInputOutput;
import net.haesleinhuepf.clij2.utilities.HasLicense;
import net.haesleinhuepf.clij2.utilities.IsCategorized;
import org.scijava.plugin.Plugin;

import java.nio.FloatBuffer;
import java.util.HashMap;

import static net.haesleinhuepf.clij.utilities.CLIJUtilities.assertDifferent;
import static net.haesleinhuepf.clij2.utilities.CLIJUtilities.checkDimensions;

/**
 * Author: @haesleinhuepf
 *         March 2020
 */
@Plugin(type = CLIJMacroPlugin.class, name = "CLIJ2_floodFillDiamond")
public class FloodFillDiamond extends AbstractCLIJ2Plugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation, IsCategorized, HasClassifiedInputOutput {
    @Override
    public String getInputType() {
        return "Image";
    }

    @Override
    public String getOutputType() {
        return "Image";
    }


    @Override
    public Object[] getDefaultValues() {
        return new Object[]{null, null, 0, 1};
    }

    @Override
    public boolean executeCL() {
        return getCLIJ2().floodFillDiamond((ClearCLBuffer)( args[0]), (ClearCLBuffer)(args[1]), asFloat(args[2]), asFloat(args[3]));
    }

    public static boolean floodFillDiamond(CLIJ2 clij2, ClearCLBuffer src, ClearCLBuffer dst, Float valueToReplace, Float valueReplacement) {
        assertDifferent(src, dst);
        if (!checkDimensions(src.getDimension(), src.getDimension(), dst.getDimension())) {
            throw new IllegalArgumentException("Error: number of dimensions don't match! (floodFillDiamond)");
        }

        ClearCLBuffer flag = clij2.create(1,1,1);
        ClearCLBuffer temp = clij2.create(dst);

        float[] flag_arr = new float[1];
        FloatBuffer floatBuffer = FloatBuffer.wrap(flag_arr);

        ClearCLKernel kernel = null;

        clij2.copy(src, temp);

        int dimension = 2;
        if (dst.getDimension() == 3 && dst.getDepth() > 1) {
            dimension = 3;
        }

        boolean flipFlag = true;
        flag_arr[0] = 1;
        while (flag_arr[0] == 1){

            HashMap<String, Object> parameters = new HashMap<>();
            if (flipFlag) {
                parameters.put("src", temp);
                parameters.put("dst", dst);
            } else {
                parameters.put("src", dst);
                parameters.put("dst", temp);
            }
            flipFlag = !flipFlag;

            flag_arr[0] = 0;
            flag.readFrom(floatBuffer, true);

            parameters.put("flag_dst", flag);
            parameters.put("value_to_replace", valueToReplace);
            parameters.put("value_replacement", valueReplacement);
            parameters.put("dimension", dimension);

            kernel = clij2.executeSubsequently(FloodFillDiamond.class, "flood_fill_diamond_x.cl", "flood_fill_diamond", dst.getDimensions(), dst.getDimensions(), parameters, kernel);

            flag.writeTo(floatBuffer, true);

            //clij2.print(dst);
            //System.out.println("---");
        }

        if (flipFlag) {
            clij2.copy(temp, dst);
        }

        clij2.release(temp);
        clij2.release(flag);

        return true;
    }

    @Override
    public String getParameterHelpText() {
        return "Image source, ByRef Image destination, Number value_to_replace, Number value_replacement";
    }

    @Override
    public String getDescription() {
        return "Replaces recursively all pixels of value a with value b if the pixels have a neighbor with value b.";
    }

    @Override
    public String getAvailableForDimensions() {
        return "2D, 3D";
    }

    @Override
    public String getCategories() {
        return "Filter";
    }
}
