package net.haesleinhuepf.clij.advancedmath;

import net.haesleinhuepf.clij.CLIJ;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.clearcl.ClearCLImage;
import net.haesleinhuepf.clij.macro.AbstractCLIJPlugin;
import net.haesleinhuepf.clij.macro.CLIJMacroPlugin;
import net.haesleinhuepf.clij.macro.CLIJOpenCLProcessor;
import net.haesleinhuepf.clij.macro.documentation.OffersDocumentation;

import java.util.HashMap;

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

@Plugin(type = CLIJMacroPlugin.class, name = "CLIJ_log")
public class Log extends AbstractCLIJPlugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation {

    @Override
    public boolean executeCL() {
        if (containsCLImageArguments()) {
            return log(clij, (ClearCLImage)( args[0]), (ClearCLImage)(args[1]));
        } else {
            Object[] args = openCLBufferArgs();
            boolean result = log(clij, (ClearCLBuffer)( args[0]), (ClearCLBuffer)(args[1]));
            releaseBuffers(args);
            return result;
        }
    }

    
    public static boolean log(CLIJ clij, ClearCLImage src, ClearCLImage dst) {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("dst", dst);
        
        return clij.execute(Log.class, "advmath.cl", "log_" + src.getDimension() + "d", parameters);
    }

    public static boolean log(CLIJ clij, ClearCLBuffer src, ClearCLBuffer dst) {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("dst", dst);

        return clij.execute(Log.class, "advmath.cl", "log_" + src.getDimension() + "d", parameters);
    }
     
    
    @Override
    public String getParameterHelpText() {
        return "Image source, Image destination";
    }

    @Override
    public String getDescription() {
        return "Computes baseE logarithm of all pixels values.\n\nf(x) = log(x)";
    }

    @Override
    public String getAvailableForDimensions() {
        return "2D, 3D";
    }
}
