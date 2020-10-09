package net.haesleinhuepf.clij2.plugins;


import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.clearcl.interfaces.ClearCLImageInterface;
import net.haesleinhuepf.clij.macro.CLIJMacroPlugin;
import net.haesleinhuepf.clij.macro.CLIJOpenCLProcessor;
import net.haesleinhuepf.clij.macro.documentation.OffersDocumentation;
import net.haesleinhuepf.clij2.AbstractCLIJ2Plugin;
import net.haesleinhuepf.clij2.CLIJ2;
import net.haesleinhuepf.clij2.utilities.HasClassifiedInputOutput;
import net.haesleinhuepf.clij2.utilities.IsCategorized;
import org.scijava.plugin.Plugin;

import java.util.HashMap;

@Plugin(type = CLIJMacroPlugin.class, name = "CLIJ2_generateParametricImage")
public class GenerateParametricImage extends AbstractCLIJ2Plugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation, IsCategorized, HasClassifiedInputOutput {
    @Override
    public String getInputType() {
        return "Vector, Image";
    }

    @Override
    public String getOutputType() {
        return "Image";
    }

    @Override
    public String getParameterHelpText() {
        return "Image label_map, Image parameter_value_vector, ByRef Image parametric_image_destination";
    }

    @Override
    public boolean executeCL() {
        boolean result = getCLIJ2().generateParametricImage((ClearCLBuffer) (args[0]),(ClearCLBuffer) (args[1]), (ClearCLBuffer) (args[2]));
        return result;
    }

    public static boolean generateParametricImage(CLIJ2 clij2, ClearCLImageInterface label_map, ClearCLImageInterface parameter_value_vector,  ClearCLImageInterface parametric_image_destination) {
       clij2.replaceIntensities(parameter_value_vector, label_map, parametric_image_destination);
       return true;
    }

    @Override
    public String getDescription() {
        return "Take a labelmap and a vector of values to replace label 1 with the 1st value in the vector. \n\n" +
                "Note that indexing in the vector starts at zero. The 0th entry corresponds to background in the label map." +
                "Internally this method just calls ReplaceIntensities.\n";
    }

    @Override
    public String getAvailableForDimensions() {
        return "2D, 3D";
    }

    @Override
    public String getCategories() {
        return "Label, Measurement, Visualisation";
    }
}
