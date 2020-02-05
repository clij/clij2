package net.haesleinhuepf.clijx.plugins.splitstack;

import net.haesleinhuepf.clij.macro.CLIJMacroPlugin;
import org.scijava.plugin.Plugin;

@Plugin(type = CLIJMacroPlugin.class, name = "CLIJx_splitStackInto5")
public class SplitStackInto5 extends AbstractSplitStack {

    @Override
    public String getParameterHelpText() {
        return getParameterHelpText(5);
    }


}
