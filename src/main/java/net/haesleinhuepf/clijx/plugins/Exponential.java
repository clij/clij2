package net.haesleinhuepf.clijx.plugins;

import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.clearcl.interfaces.ClearCLImageInterface;
import net.haesleinhuepf.clij.macro.CLIJMacroPlugin;
import net.haesleinhuepf.clij.macro.CLIJOpenCLProcessor;
import net.haesleinhuepf.clij.macro.documentation.OffersDocumentation;

import java.util.HashMap;

import net.haesleinhuepf.clijx.CLIJx;
import net.haesleinhuepf.clijx.utilities.AbstractCLIJxPlugin;
import net.haesleinhuepf.clijx.utilities.HasAuthor;
import org.scijava.plugin.Plugin;

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

@Plugin(type = CLIJMacroPlugin.class, name = "CLIJx_exponential")
public class Exponential extends AbstractCLIJxPlugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation, HasAuthor {

    @Override
    public boolean executeCL() {
         return exponential(getCLIJx(), (ClearCLBuffer)( args[0]), (ClearCLBuffer)(args[1]));
    }


    public static boolean exponential(CLIJx clijx, ClearCLImageInterface src, ClearCLImageInterface dst) {

        HashMap<String, Object> parameters = new HashMap<>();
        
        parameters.clear();
        parameters.put("src", src);
        parameters.put("dst", dst);

        clijx.execute(Exponential.class, "exponential_" + src.getDimension() + "d_x.cl", "exponential_" + src.getDimension() + "d", dst.getDimensions(), dst.getDimensions(), parameters);
        return true;
    }
    
    @Override
    public String getParameterHelpText() {
        return "Image source, Image destination";
    }

    @Override
    public String getDescription() {
        return "Computes base exponential of all pixels values.\n\nf(x) = exp(x)";
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
