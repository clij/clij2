package net.haesleinhuepf.clij.advancedfilters;

import net.haesleinhuepf.clij.CLIJ;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.kernels.Kernels;
import net.haesleinhuepf.clij.macro.CLIJMacroPlugin;
import net.haesleinhuepf.clij.macro.modules.BinaryAnd;
import org.scijava.plugin.Plugin;

@Plugin(type = CLIJMacroPlugin.class, name = "CLIJx_binaryIntersection")
public class BinaryIntersection extends BinaryAnd {
    @Override
    public String getDescription() {
        return super.getDescription().replace("binary AND", "binary intersection");
    }

    public static boolean binaryIntersection(CLIJ clij, ClearCLBuffer input1, ClearCLBuffer input2, ClearCLBuffer destination)  {
        return Kernels.binaryAnd(clij, input1, input2, destination);
    }
}
