package net.haesleinhuepf.clij2.plugins;


import net.haesleinhuepf.clij.macro.CLIJMacroPlugin;
import org.scijava.plugin.Plugin;

@Deprecated
@Plugin(type = CLIJMacroPlugin.class, name = "CLIJ2_blur2D")
public class Blur2D extends GaussianBlur2D {
    @Override
    public String getParameterHelpText() {
        return super.getParameterHelpText() + "\n\nDEPRECATED: This method is deprecated. Use gaussianBlur2D instead.";
    }
}
