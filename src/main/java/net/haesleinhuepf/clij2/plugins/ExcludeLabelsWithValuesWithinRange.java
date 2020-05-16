package net.haesleinhuepf.clij2.plugins;

import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.coremem.enums.NativeTypeEnum;
import net.haesleinhuepf.clij.macro.CLIJMacroPlugin;
import net.haesleinhuepf.clij.macro.CLIJOpenCLProcessor;
import net.haesleinhuepf.clij.macro.documentation.OffersDocumentation;
import net.haesleinhuepf.clij2.AbstractCLIJ2Plugin;
import net.haesleinhuepf.clij2.CLIJ2;
import org.scijava.plugin.Plugin;


/**
 * Author: @haesleinhuepf
 *         May 2020
 */
@Plugin(type = CLIJMacroPlugin.class, name = "CLIJ2_excludeLabelsWithValuesWithinRange")
public class ExcludeLabelsWithValuesWithinRange extends AbstractCLIJ2Plugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation {

    @Override
    public boolean executeCL() {
        ClearCLBuffer values = (ClearCLBuffer)(args[0]);
        ClearCLBuffer label_map_in = (ClearCLBuffer)( args[1]);
        ClearCLBuffer label_map_out = (ClearCLBuffer)( args[2]);

        float min = asFloat(args[3]);
        float max = asFloat(args[4]);

        return getCLIJ2().excludeLabelsWithValuesWithinRange( values, label_map_in, label_map_out, min, max);
    }

    public static boolean excludeLabelsWithValuesWithinRange(CLIJ2 clij2, ClearCLBuffer values, ClearCLBuffer label_map_in, ClearCLBuffer label_map_out, Float min, Float max) {
        ClearCLBuffer below = clij2.create(values.getDimensions(), NativeTypeEnum.UnsignedByte);
        ClearCLBuffer above = clij2.create(values.getDimensions(), NativeTypeEnum.UnsignedByte);

        clij2.smallerOrEqualConstant(values, below, max);
        clij2.greaterOrEqualConstant(values, above, min);
        ClearCLBuffer temp = clij2.create(values.getDimensions(), NativeTypeEnum.UnsignedByte);

        clij2.binaryAnd(below, above, temp);

        clij2.excludeLabels(temp, label_map_in, label_map_out);

        below.close();
        above.close();
        temp.close();

        return true;
    }


    @Override
    public String getParameterHelpText() {
        return "Image values_vector, Image label_map_input, ByRef Image label_map_destination, Number minimum_value_range, Number maximum_value_range";
    }

    @Override
    public String getDescription() {
        return "This operation removes labels from a labelmap and renumbers the remaining labels. \n\n" +
                "Hand over a vector of values and a range specifying which labels with which values are eliminated.";
    }

    @Override
    public String getAvailableForDimensions() {
        return "2D, 3D";
    }

    @Override
    public ClearCLBuffer createOutputBufferFromSource(ClearCLBuffer input) {
        ClearCLBuffer labelmap =(ClearCLBuffer)( args[1]);
        return getCLIJ2().create(labelmap);
    }
}
