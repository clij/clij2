package net.haesleinhuepf.clijx.utilities;

import net.haesleinhuepf.clij.macro.AbstractCLIJPlugin;
import net.haesleinhuepf.clijx.CLIJx;

public abstract class AbstractCLIJxPlugin extends AbstractCLIJPlugin {
    public CLIJx getCLIJx() {
        // Note: The internals here may change
        return CLIJx.getInstance();
    }
}
