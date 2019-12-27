package net.haesleinhuepf.clijx.advancedfilters;

import net.haesleinhuepf.clij.CLIJ;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.macro.CLIJMacroPlugin;
import net.haesleinhuepf.clijx.CLIJx;
import org.scijava.plugin.Plugin;

@Plugin(type = CLIJMacroPlugin.class, name = "CLIJx_binaryUnion")
public class BinaryUnion extends BinaryOr {
    @Override
    public String getDescription() {
        return super.getDescription().replace("binary OR", "binary union");
    }

    public static boolean binaryUnion(CLIJx clijx, ClearCLBuffer input1, ClearCLBuffer input2, ClearCLBuffer destination)  {
        return BinaryOr.binaryOr(clijx, input1, input2, destination);
    }
}
