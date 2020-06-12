package net.haesleinhuepf.clij2.plugins;

import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.clearcl.interfaces.ClearCLImageInterface;
import net.haesleinhuepf.clij.macro.CLIJMacroPlugin;
import net.haesleinhuepf.clij.macro.CLIJOpenCLProcessor;
import net.haesleinhuepf.clij.macro.documentation.OffersDocumentation;
import net.haesleinhuepf.clij2.AbstractCLIJ2Plugin;
import net.haesleinhuepf.clij2.CLIJ2;
import net.haesleinhuepf.clij2.utilities.HasAuthor;
import net.haesleinhuepf.clij2.utilities.IsCategorized;
import org.scijava.plugin.Plugin;

import java.util.HashMap;

/**
 * 
 * Based on
 * 	net.haesleinhuepf.clij.macro.modules.Power.java
 * 	Author: @haesleinhuepf
 * 	December 2018
 * 
 * modified by: @phaub
 * July 2019
 * 
 */

@Plugin(type = CLIJMacroPlugin.class, name = "CLIJ2_logarithm")
public class Logarithm extends AbstractCLIJ2Plugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation, HasAuthor, IsCategorized {
    @Override
    public String getCategories() {
        return "Math";
    }

    @Override
    public boolean executeCL() {
        boolean result = getCLIJ2().logarithm((ClearCLBuffer)( args[0]), (ClearCLBuffer)(args[1]));
        return result;
    }

    public static boolean logarithm(CLIJ2 clij2, ClearCLImageInterface src, ClearCLImageInterface dst) {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("dst", dst);

        clij2.execute(Logarithm.class, "logarithm_" + src.getDimension() + "d_x.cl", "logarithm_" + src.getDimension() + "d", dst.getDimensions(), dst.getDimensions(), parameters);
        return true;
    }
     
    
    @Override
    public String getParameterHelpText() {
        return "Image source, ByRef Image destination";
    }

    @Override
    public String getDescription() {
        return "Computes base e logarithm of all pixels values.\n\nf(x) = log(x)";
    }

    @Override
    public String getAvailableForDimensions() {
        return "2D, 3D";
    }

    @Override
    public String getAuthorName() {
        return "Peter Haub, Robert Haase";
    }
}
