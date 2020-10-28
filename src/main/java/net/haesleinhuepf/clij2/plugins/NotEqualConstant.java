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
 * 	        August 2019
 */

@Plugin(type = CLIJMacroPlugin.class, name = "CLIJ2_notEqualConstant")
public class NotEqualConstant extends AbstractCLIJ2Plugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation, IsCategorized, HasClassifiedInputOutput {
    @Override
    public String getInputType() {
        return "Image";
    }

    @Override
    public String getOutputType() {
        return "Binary Image";
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
        boolean result = getCLIJ2().notEqualConstant((ClearCLBuffer)( args[0]), (ClearCLBuffer)(args[1]), asFloat(args[2]));
        return result;
    }

    public static boolean notEqualConstant(CLIJ2 clij2, ClearCLBuffer src1, ClearCLBuffer dst, Float scalar) {

        HashMap<String, Object> parameters = new HashMap<>();
        
        parameters.clear();
        parameters.put("src1", src1);
        parameters.put("scalar", scalar);
        parameters.put("dst", dst);


        clij2.execute(NotEqualConstant.class, "not_equal_constant_" + src1.getDimension() + "d_x.cl", "not_equal_constant_" + src1.getDimension() + "d", dst.getDimensions(), dst.getDimensions(), parameters);
        return true;
    }
        
    
    
    @Override
    public String getParameterHelpText() {
        return "Image source, ByRef Image destination, Number constant";
    }

    @Override
    public String getDescription() {
        return "Determines if two images A and B equal pixel wise.\n\n" +
                "f(a, b) = 1 if a != b; 0 otherwise." +
                "Parameters\n" +
                "----------\n" +
                "source : Image\n" +
                "    The image where every pixel is compared to the constant.\n" +
                "destination : Image\n" +
                "    The resulting binary image where pixels will be 1 only if source1 and source2 equal in the given pixel.\n" +
                "constant : float\n" +
                "    The constant where every pixel is compared to.\n";
    }
    
    @Override
    public String getAvailableForDimensions() {
        return "2D, 3D";
    }
}
