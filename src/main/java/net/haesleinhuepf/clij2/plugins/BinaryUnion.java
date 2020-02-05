package net.haesleinhuepf.clij2.plugins;

import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.macro.CLIJMacroPlugin;
import net.haesleinhuepf.clij2.CLIJ2;
import org.scijava.plugin.Plugin;

@Plugin(type = CLIJMacroPlugin.class, name = "CLIJ2_binaryUnion")
public class BinaryUnion extends BinaryOr {
    @Override
    public String getDescription() {
        return super.getDescription().replace("binary OR", "binary union");
    }

    public static boolean binaryUnion(CLIJ2 clij2, ClearCLBuffer input1, ClearCLBuffer input2, ClearCLBuffer destination)  {
        return binaryOr(clij2, input1, input2, destination);
    }
}
