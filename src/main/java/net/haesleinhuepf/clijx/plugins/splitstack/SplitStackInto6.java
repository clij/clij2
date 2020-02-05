package net.haesleinhuepf.clijx.plugins.splitstack;

import net.haesleinhuepf.clij.macro.CLIJMacroPlugin;
import org.scijava.plugin.Plugin;

@Plugin(type = CLIJMacroPlugin.class, name = "CLIJx_splitStackInto6")
public class SplitStackInto6 extends AbstractSplitStack {

    @Override
    public String getParameterHelpText() {
        return getParameterHelpText(6);
    }


}
