package net.haesleinhuepf.clij.advancedfilters;

import net.haesleinhuepf.clij.CLIJ;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.kernels.Kernels;
import net.haesleinhuepf.clij.macro.CLIJMacroPlugin;
import net.haesleinhuepf.clij.macro.modules.BinaryOr;
import org.scijava.plugin.Plugin;

@Plugin(type = CLIJMacroPlugin.class, name = "CLIJx_binaryUnion")
public class BinaryUnion extends BinaryOr {
    @Override
    public String getDescription() {
        return super.getDescription().replace("binary OR", "binary union");
    }

    public static boolean binaryUnion(CLIJ clij, ClearCLBuffer input1, ClearCLBuffer input2, ClearCLBuffer destination)  {
        return Kernels.binaryOr(clij, input1, input2, destination);
    }
}
