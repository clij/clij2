package net.haesleinhuepf.clij2.plugins;

import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.clearcl.interfaces.ClearCLImageInterface;
import net.haesleinhuepf.clij.macro.CLIJMacroPlugin;
import net.haesleinhuepf.clij.macro.CLIJOpenCLProcessor;
import net.haesleinhuepf.clij.macro.documentation.OffersDocumentation;
import net.haesleinhuepf.clij2.AbstractCLIJ2Plugin;
import net.haesleinhuepf.clij2.CLIJ2;
import org.scijava.plugin.Plugin;

import static net.haesleinhuepf.clij2.plugins.AddImagesWeighted.addImagesWeighted;

/**
 * Author: @haesleinhuepf
 *         March 2020
 */

@Plugin(type = CLIJMacroPlugin.class, name = "CLIJ2_concatenateStacks")
public class ConcatenateStacks extends AbstractCLIJ2Plugin implements CLIJMacroPlugin, CLIJOpenCLProcessor, OffersDocumentation {

    @Override
    public boolean executeCL() {
        return getCLIJ2().concatenateStacks((ClearCLImageInterface)( args[0]), (ClearCLImageInterface)(args[1]), (ClearCLImageInterface)(args[2]));
    }

    public static boolean concatenateStacks(CLIJ2 clij2, ClearCLImageInterface stack1, ClearCLImageInterface stack2, ClearCLImageInterface dst) {
        clij2.paste(stack1, dst, 0, 0, 0);
        clij2.paste(stack2, dst, 0, 0, stack1.getDepth());
        return true;
    }

    @Override
    public ClearCLBuffer createOutputBufferFromSource(ClearCLBuffer input) {
        ClearCLBuffer stack1 = (ClearCLBuffer) args[0];
        ClearCLBuffer stack2 = (ClearCLBuffer) args[1];
        return getCLIJ2().create(new long[]{stack1.getWidth(), stack2.getWidth(), stack1.getDepth() + stack2.getDepth()});
    }

    @Override
    public String getParameterHelpText() {
        return "Image stack1, Image stack2, ByRef Image destination";
    }

    @Override
    public String getDescription() {
        return "Concatenates two stacks in Z.";
    }

    @Override
    public String getAvailableForDimensions() {
        return "3D";
    }
}
