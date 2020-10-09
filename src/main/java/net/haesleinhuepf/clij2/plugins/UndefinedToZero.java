package net.haesleinhuepf.clij2.plugins;

import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.macro.CLIJMacroPlugin;
import net.haesleinhuepf.clij.macro.CLIJOpenCLProcessor;
import net.haesleinhuepf.clij.macro.documentation.OffersDocumentation;
import net.haesleinhuepf.clij2.AbstractCLIJ2Plugin;
import net.haesleinhuepf.clij2.CLIJ2;
import net.haesleinhuepf.clij2.utilities.HasClassifiedInputOutput;
import net.haesleinhuepf.clij2.utilities.IsCategorized;
import org.scijava.plugin.Plugin;

import java.util.HashMap;

/**
 * 	Author: @haesleinhuepf
 * 	        June 2020
 */

@Plugin(type = CLIJMacroPlugin.class, name = "CLIJ2_undefinedToZero")
public class UndefinedToZero extends AbstractCLIJ2Plugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation, IsCategorized, HasClassifiedInputOutput {
    @Override
    public String getInputType() {
        return "Image";
    }

    @Override
    public String getOutputType() {
        return "Image";
    }

    @Override
    public String getCategories() {
        return "Math";
    }


    @Override
    public Object[] getDefaultValues() {
        return new Object[]{null, null, 0};
    }

    @Override
    public boolean executeCL() {
        boolean result = getCLIJ2().undefinedToZero((ClearCLBuffer)( args[0]), (ClearCLBuffer)(args[1]));
        return result;
    }

    public static boolean undefinedToZero(CLIJ2 clij2, ClearCLBuffer src, ClearCLBuffer dst) {

        HashMap<String, Object> parameters = new HashMap<>();
        
        parameters.clear();
        parameters.put("src", src);
        parameters.put("dst", dst);

        clij2.execute(UndefinedToZero.class, "undefined_to_zero_x.cl", "undefined_to_zero", src.getDimensions(), src.getDimensions(), parameters);
        return true;
    }
    
    @Override
    public String getParameterHelpText() {
        return "Image source, ByRef Image destination";
    }

    @Override
    public String getDescription() {
        return "Copies all pixels instead those which are not a number (NaN) or infinity (inf), which are replaced by 0.";
    }
    
    @Override
    public String getAvailableForDimensions() {
        return "2D, 3D";
    }
}
