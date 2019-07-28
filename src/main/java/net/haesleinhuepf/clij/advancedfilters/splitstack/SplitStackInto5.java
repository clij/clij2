package net.haesleinhuepf.clij.advancedfilters.splitstack;

import net.haesleinhuepf.clij.macro.CLIJMacroPlugin;
import org.scijava.plugin.Plugin;

@Plugin(type = CLIJMacroPlugin.class, name = "CLIJ_splitStackInto5")
public class SplitStackInto5 extends AbstractSplitStack {

    @Override
    public String getParameterHelpText() {
        return getParameterHelpText(5);
    }


}
