package net.haesleinhuepf.clijx.plugins.tenengradfusion;

import net.haesleinhuepf.clij.macro.CLIJMacroPlugin;
import org.scijava.plugin.Plugin;

@Plugin(type = CLIJMacroPlugin.class, name = "CLIJx_tenengradFusionOf4")
public class TenengradFusionOf4 extends AbstractTenengradFusion {

    @Override
    public String getParameterHelpText() {
        return getParameterHelpText(4);
    }


}
