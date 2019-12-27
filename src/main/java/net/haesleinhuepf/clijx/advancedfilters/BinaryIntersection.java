package net.haesleinhuepf.clijx.advancedfilters;

import net.haesleinhuepf.clij.CLIJ;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.macro.CLIJMacroPlugin;
import net.haesleinhuepf.clijx.CLIJx;
import org.scijava.plugin.Plugin;

@Plugin(type = CLIJMacroPlugin.class, name = "CLIJx_binaryIntersection")
public class BinaryIntersection extends BinaryAnd {
    @Override
    public String getDescription() {
        return super.getDescription().replace("binary AND", "binary intersection");
    }

    public static boolean binaryIntersection(CLIJx clijx, ClearCLBuffer input1, ClearCLBuffer input2, ClearCLBuffer destination)  {
        return BinaryAnd.binaryAnd(clijx, input1, input2, destination);
    }
}
