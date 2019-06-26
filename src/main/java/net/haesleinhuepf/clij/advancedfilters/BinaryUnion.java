package net.haesleinhuepf.clij.advancedfilters;

import net.haesleinhuepf.clij.macro.CLIJMacroPlugin;
import net.haesleinhuepf.clij.macro.modules.BinaryOr;
import org.scijava.plugin.Plugin;

@Plugin(type = CLIJMacroPlugin.class, name = "CLIJ_binaryUnion")
public class BinaryUnion extends BinaryOr {
    @Override
    public String getDescription() {
        return super.getDescription().replace("binary OR", "binary union");
    }
}
