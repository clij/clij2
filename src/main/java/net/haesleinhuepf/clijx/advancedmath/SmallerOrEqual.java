package net.haesleinhuepf.clijx.advancedmath;

import net.haesleinhuepf.clij.CLIJ;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.macro.AbstractCLIJPlugin;
import net.haesleinhuepf.clij.macro.CLIJMacroPlugin;
import net.haesleinhuepf.clij.macro.CLIJOpenCLProcessor;
import net.haesleinhuepf.clij.macro.documentation.OffersDocumentation;
import net.haesleinhuepf.clijx.CLIJx;
import net.haesleinhuepf.clijx.utilities.AbstractCLIJxPlugin;
import org.scijava.plugin.Plugin;

import java.util.HashMap;

/**
 * 	Author: @haesleinhuepf
 * 	        August 2019
 */

@Plugin(type = CLIJMacroPlugin.class, name = "CLIJx_smallerOrEqual")
public class SmallerOrEqual extends AbstractCLIJxPlugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation {

    @Override
    public boolean executeCL() {
        boolean result = smallerOrEqual(getCLIJx(), (ClearCLBuffer)( args[0]), (ClearCLBuffer)(args[1]), (ClearCLBuffer)(args[2]));
        return result;
    }

    public static boolean smallerOrEqual(CLIJx clijx, ClearCLBuffer src1, ClearCLBuffer src2, ClearCLBuffer dst) {

        HashMap<String, Object> parameters = new HashMap<>();
        
        parameters.clear();
        parameters.put("src1", src1);
        parameters.put("src2", src2);
        parameters.put("dst", dst);

        clijx.execute(SmallerOrEqual.class, "smaller_or_equal_" + src1.getDimension() + "d_x.cl", "smaller_or_equal_" + src1.getDimension() + "d", src1.getDimensions(), src1.getDimensions(), parameters);
        return true;
    }
    
    @Override
    public String getParameterHelpText() {
        return "Image source1, Image source2, Image destination";
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
