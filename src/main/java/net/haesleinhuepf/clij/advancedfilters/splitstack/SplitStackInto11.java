package net.haesleinhuepf.clij.advancedfilters.splitstack;

import net.haesleinhuepf.clij.macro.CLIJMacroPlugin;
import org.scijava.plugin.Plugin;

@Plugin(type = CLIJMacroPlugin.class, name = "CLIJ_splitStackInto11")
public class SplitStackInto11 extends AbstractSplitStack {

    @Override
    public String getParameterHelpText() {
        return getParameterHelpText(11);
    }


}
