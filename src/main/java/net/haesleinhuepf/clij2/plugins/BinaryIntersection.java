package net.haesleinhuepf.clij2.plugins;

import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.macro.CLIJMacroPlugin;
import net.haesleinhuepf.clij2.CLIJ2;
import org.scijava.plugin.Plugin;

@Plugin(type = CLIJMacroPlugin.class, name = "CLIJ2_binaryIntersection")
public class BinaryIntersection extends BinaryAnd {
    @Override
    public String getDescription() {
        return super.getDescription().replace("binary AND", "binary intersection");
    }

    public static boolean binaryIntersection(CLIJ2 clij2, ClearCLBuffer input1, ClearCLBuffer input2, ClearCLBuffer destination)  {
        return binaryAnd(clij2, input1, input2, destination);
    }
}
