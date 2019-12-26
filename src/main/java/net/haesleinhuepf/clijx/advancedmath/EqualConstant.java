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

@Plugin(type = CLIJMacroPlugin.class, name = "CLIJx_equalConstant")
public class EqualConstant extends AbstractCLIJxPlugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation {

    @Override
    public boolean executeCL() {
        boolean result = equalConstant(getCLIJx(), (ClearCLBuffer)( args[0]), (ClearCLBuffer)(args[1]), asFloat(args[2]));
        return result;
    }

    public static boolean equalConstant(CLIJx clijx, ClearCLImageInterface src1, ClearCLImageInterface dst, Float scalar) {

        HashMap<String, Object> parameters = new HashMap<>();
        
        parameters.clear();
        parameters.put("src1", src1);
        parameters.put("scalar", scalar);
        parameters.put("dst", dst);

        clijx.execute(EqualConstant.class, "equal_constant_" + src1.getDimension() + "_x.cl", "equal_constant_" + src1.getDimension() + "d", src1.getDimensions(), src1.getDimensions(), parameters);
        return true;
    }
    
    @Override
    public String getParameterHelpText() {
        return "Image source, Image destination, Number constant";
    }

    @Override
    public String getDescription() {
        return "Determines if an image A and a constant b are equal.\n\nf(a, b) = 1 if a == b; 0 otherwise. ";
    }
    
    @Override
    public String getAvailableForDimensions() {
        return "2D, 3D";
    }
}
