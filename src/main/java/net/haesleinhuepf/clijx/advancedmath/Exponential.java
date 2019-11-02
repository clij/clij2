package net.haesleinhuepf.clijx.advancedmath;

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

@Plugin(type = CLIJMacroPlugin.class, name = "CLIJx_exponential")
public class Exponential extends AbstractCLIJPlugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation {

    @Override
    public boolean executeCL() {
        if (containsCLImageArguments()) {
            return exponential(clij, (ClearCLImage)( args[0]), (ClearCLImage)(args[1]));
        } else {
            Object[] args = openCLBufferArgs();
            boolean result = exponential(clij, (ClearCLBuffer)( args[0]), (ClearCLBuffer)(args[1]));
            releaseBuffers(args);
            return result;
        }
    }


    public static boolean exponential(CLIJ clij, ClearCLImage src, ClearCLImage dst) {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("dst", dst);
        
        // Note: Problems with GPU 'Intel(R) UHD Graphics 630'
        // Test net.haesleinhuepf.advancedmath.ExpTest.java 
        //    failed on GPU 'Intel(R) UHD Graphics 630'
        //    passed on GPU 'Geforce GTX 1060'
        return clij.execute(Exponential.class, "advmath.cl", "exp_" + src.getDimension() + "d", parameters);
    }

    public static boolean exponential(CLIJ clij, ClearCLBuffer src, ClearCLBuffer dst) {

        HashMap<String, Object> parameters = new HashMap<>();
        
        parameters.clear();
        parameters.put("src", src);
        parameters.put("dst", dst);

        // Note: Problems with GPU 'Intel(R) UHD Graphics 630'
        // Test net.haesleinhuepf.advancedmath.ExpTest.java 
        //    failed on GPU 'Intel(R) UHD Graphics 630'
        //    passed on GPU 'Geforce GTX 1060'
        return clij.execute(Exponential.class, "advmath.cl", "exp_" + src.getDimension() + "d", parameters);
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
}
