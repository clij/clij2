package net.haesleinhuepf.clij.advancedfilters.tenengradfusion;

import net.haesleinhuepf.clij.macro.CLIJMacroPlugin;
import org.scijava.plugin.Plugin;

@Plugin(type = CLIJMacroPlugin.class, name = "CLIJ_tenengradFusionOf5")
public class TenengradFusionOf5 extends AbstractTenengradFusion {

    @Override
    public String getParameterHelpText() {
        return getParameterHelpText(5);
    }


}
