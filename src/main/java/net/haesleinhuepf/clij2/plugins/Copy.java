package net.haesleinhuepf.clij2.plugins;

import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.clearcl.interfaces.ClearCLImageInterface;
import net.haesleinhuepf.clij.macro.CLIJMacroPlugin;
import net.haesleinhuepf.clij.macro.CLIJOpenCLProcessor;
import net.haesleinhuepf.clij.macro.documentation.OffersDocumentation;
import net.haesleinhuepf.clij2.AbstractCLIJ2Plugin;
import net.haesleinhuepf.clij2.CLIJ2;
import net.haesleinhuepf.clij2.utilities.IsCategorized;
import org.scijava.plugin.Plugin;

import java.util.HashMap;

import static net.haesleinhuepf.clij.utilities.CLIJUtilities.assertDifferent;
import static net.haesleinhuepf.clij2.utilities.CLIJUtilities.checkDimensions;

/**
 * Author: @haesleinhuepf
 * 12 2018
 */

@Plugin(type = CLIJMacroPlugin.class, name = "CLIJ2_copy")
public class Copy extends AbstractCLIJ2Plugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation, IsCategorized {
    @Override
    public String getCategories() {
        return "Transform";
    }

    @Override
    public boolean executeCL() {
        return getCLIJ2().copy((ClearCLBuffer)( args[0]), (ClearCLBuffer)(args[1]));
    }

    public static boolean copy(CLIJ2 clij2, ClearCLImageInterface src, ClearCLImageInterface dst) {
        assertDifferent(src, dst);

        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("src", src);
        parameters.put("dst", dst);
        if (!checkDimensions(src.getDimension(), dst.getDimension())) {
            clij2.activateSizeIndependentKernelCompilation();
            return clij2.copySlice(src, dst, 0);
            //throw new IllegalArgumentException("Error: number of dimensions don't match! (copy) " + src.getDimension() + " vs " + dst.getDimension() +  "|" + src.getWidth() + "/" + src.getHeight() + "/" + src.getDepth() + " vs "  + dst.getWidth() + "/" + dst.getHeight() + "/" + dst.getDepth() );
        }
        clij2.activateSizeIndependentKernelCompilation();
        clij2.execute(Copy.class, "copy_" + dst.getDimension() + "d_x.cl", "copy_" + dst.getDimension() + "d", dst.getDimensions(), dst.getDimensions(), parameters);
        return true;
    }

    @Override
    public String getParameterHelpText() {
        return "Image source, ByRef Image destination";
    }

    @Override
    public String getDescription() {
        return "Copies an image.\n\n<pre>f(x) = x</pre>";
    }

    @Override
    public String getAvailableForDimensions() {
        return "2D, 3D";
    }
}
