package net.haesleinhuepf.clijx.advancedmath;

import net.haesleinhuepf.clij.CLIJ;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.clearcl.interfaces.ClearCLImageInterface;
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

@Plugin(type = CLIJMacroPlugin.class, name = "CLIJx_greaterOrEqual")
public class GreaterOrEqual extends AbstractCLIJxPlugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation {

    @Override
    public boolean executeCL() {
        boolean result = greaterOrEqual(getCLIJx(), (ClearCLBuffer)( args[0]), (ClearCLBuffer)(args[1]), (ClearCLBuffer)(args[2]));
        return result;
    }

    public static boolean greaterOrEqual(CLIJx clijx, ClearCLImageInterface src1, ClearCLImageInterface src2, ClearCLImageInterface dst) {

        HashMap<String, Object> parameters = new HashMap<>();
        
        parameters.clear();
        parameters.put("src1", src1);
        parameters.put("src2", src2);
        parameters.put("dst", dst);

        clijx.execute(GreaterOrEqual.class, "greater_or_equal_" + src1.getDimension() + "d_x.cl", "greater_or_equal_" + src1.getDimension() + "d", src1.getDimensions(), src1.getDimensions(), parameters);
        return true;
    }
        
    
    
    @Override
    public String getParameterHelpText() {
        return "Image source1, Image source2, Image destination";
    }

    @Override
    public String getDescription() {
        return "Determines if two images A and B greater or equal pixel wise.\n\nf(a, b) = 1 if a >= b; 0 otherwise. ";
    }
    
    @Override
    public String getAvailableForDimensions() {
        return "2D, 3D";
    }
}
