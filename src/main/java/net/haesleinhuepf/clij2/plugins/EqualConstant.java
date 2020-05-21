package net.haesleinhuepf.clij2.plugins;

import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.clearcl.interfaces.ClearCLImageInterface;
import net.haesleinhuepf.clij.macro.CLIJMacroPlugin;
import net.haesleinhuepf.clij.macro.CLIJOpenCLProcessor;
import net.haesleinhuepf.clij.macro.documentation.OffersDocumentation;
import net.haesleinhuepf.clij2.AbstractCLIJ2Plugin;
import net.haesleinhuepf.clij2.CLIJ2;
import org.scijava.plugin.Plugin;

import java.util.HashMap;

/**
 * 	Author: @haesleinhuepf
 * 	        August 2019
 */

@Plugin(type = CLIJMacroPlugin.class, name = "CLIJ2_equalConstant")
public class EqualConstant extends AbstractCLIJ2Plugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation {

    @Override
    protected Object[] getDefaultValues() {
        return new Object[]{null, null, 0};
    }

    @Override
    public boolean executeCL() {
        boolean result = getCLIJ2().equalConstant((ClearCLBuffer)( args[0]), (ClearCLBuffer)(args[1]), asFloat(args[2]));
        return result;
    }

    public static boolean equalConstant(CLIJ2 clij2, ClearCLImageInterface src1, ClearCLImageInterface dst, Float scalar) {

        HashMap<String, Object> parameters = new HashMap<>();
        
        parameters.clear();
        parameters.put("src1", src1);
        parameters.put("scalar", scalar);
        parameters.put("dst", dst);

        clij2.execute(EqualConstant.class, "equal_constant_" + src1.getDimension() + "d_x.cl", "equal_constant_" + src1.getDimension() + "d", src1.getDimensions(), src1.getDimensions(), parameters);
        return true;
    }
    
    @Override
    public String getParameterHelpText() {
        return "Image source, ByRef Image destination, Number constant";
    }

    @Override
    public String getDescription() {
        return "Determines if an image A and a constant b are equal.\n\n<pre>f(a, b) = 1 if a == b; 0 otherwise.</pre> ";
    }
    
    @Override
    public String getAvailableForDimensions() {
        return "2D, 3D";
    }
}
