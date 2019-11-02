package net.haesleinhuepf.clijx.advancedfilters.splitstack;

import net.haesleinhuepf.clij.macro.CLIJMacroPlugin;
import org.scijava.plugin.Plugin;

@Plugin(type = CLIJMacroPlugin.class, name = "CLIJx_splitStackInto3")
public class SplitStackInto3 extends AbstractSplitStack {

    @Override
    public String getParameterHelpText() {
        return getParameterHelpText(3);
    }


}
