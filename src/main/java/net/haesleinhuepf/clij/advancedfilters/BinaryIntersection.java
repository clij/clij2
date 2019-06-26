package net.haesleinhuepf.clij.advancedfilters;

import net.haesleinhuepf.clij.macro.CLIJMacroPlugin;
import net.haesleinhuepf.clij.macro.modules.BinaryOr;
import org.scijava.plugin.Plugin;

@Plugin(type = CLIJMacroPlugin.class, name = "CLIJ_binaryIntersection")
public class BinaryIntersection extends BinaryOr {
    @Override
    public String getDescription() {
        return super.getDescription().replace("binary AND", "binary intersection");
    }
}
