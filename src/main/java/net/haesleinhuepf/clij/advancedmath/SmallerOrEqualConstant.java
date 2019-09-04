package net.haesleinhuepf.clij.advancedmath;

import net.haesleinhuepf.clij.CLIJ;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.macro.AbstractCLIJPlugin;
import net.haesleinhuepf.clij.macro.CLIJMacroPlugin;
import net.haesleinhuepf.clij.macro.CLIJOpenCLProcessor;
import net.haesleinhuepf.clij.macro.documentation.OffersDocumentation;
import org.scijava.plugin.Plugin;

import java.util.HashMap;

/**
 * 	Author: @haesleinhuepf
 * 	        August 2019
 */

@Plugin(type = CLIJMacroPlugin.class, name = "CLIJx_smallerOrEqualConstant")
public class SmallerOrEqualConstant extends AbstractCLIJPlugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation {

    @Override
    public boolean executeCL() {
        boolean result = smallerOrEqualConstant(clij, (ClearCLBuffer)( args[0]), (ClearCLBuffer)(args[1]), asFloat(args[2]));
        return result;
    }

    public static boolean smallerOrEqualConstant(CLIJ clij, ClearCLBuffer src1, ClearCLBuffer dst, Float scalar) {

        HashMap<String, Object> parameters = new HashMap<>();
        
        parameters.clear();
        parameters.put("src1", src1);
        parameters.put("scalar", scalar);
        parameters.put("dst", dst);

        return clij.execute(SmallerOrEqualConstant.class, "comparison_constants.cl", "smaller_or_equal_" + src1.getDimension() + "d", parameters);
    }
        
    
    
    @Override
    public String getParameterHelpText() {
        return "Image source, Image destination, Number constant";
    }

    @Override
    public String getDescription() {
        return "Determines if two images A and B smaller or equal pixel wise.\n\nf(a, b) = 1 if a <= b; 0 otherwise. ";
    }
    
    @Override
    public String getAvailableForDimensions() {
        return "2D, 3D";
    }
}
