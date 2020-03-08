package net.haesleinhuepf.clij2;

import net.haesleinhuepf.clij.macro.AbstractCLIJPlugin;

public abstract class AbstractCLIJ2Plugin extends AbstractCLIJPlugin {
    public CLIJ2 getCLIJ2() {
        // Note: The internals here may change
        return CLIJ2.getInstance();
    }
}
