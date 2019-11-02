package net.haesleinhuepf.clijx.advancedfilters.splitstack;

import net.haesleinhuepf.clij.macro.CLIJMacroPlugin;
import org.scijava.plugin.Plugin;

@Plugin(type = CLIJMacroPlugin.class, name = "CLIJx_splitStackInto10")
public class SplitStackInto10 extends AbstractSplitStack {

    @Override
    public String getParameterHelpText() {
        return getParameterHelpText(10);
    }


}
