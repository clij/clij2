package net.haesleinhuepf.clij.advancedfilters.splitstack;

import net.haesleinhuepf.clij.macro.CLIJMacroPlugin;
import org.scijava.plugin.Plugin;

@Plugin(type = CLIJMacroPlugin.class, name = "CLIJx_splitStackInto8")
public class SplitStackInto8 extends AbstractSplitStack {

    @Override
    public String getParameterHelpText() {
        return getParameterHelpText(8);
    }


}
