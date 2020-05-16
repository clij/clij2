package net.haesleinhuepf.clij2;

import net.haesleinhuepf.clij.CLIJ;
import net.haesleinhuepf.clij.macro.AbstractCLIJPlugin;

public abstract class AbstractCLIJ2Plugin extends AbstractCLIJPlugin {

    private CLIJ2 clij2;

    @Override
    public void setClij(CLIJ clij) {
        if (clij2 == null || clij2.getCLIJ() != clij) {
            if (clij == CLIJ.getInstance()) {
                clij2 = CLIJ2.getInstance();
            } else {
                System.out.println("Warning AbstractCLIJ2Plugin.setCLIJ is deprecated. Use setCLIJ2 instead.");
                clij2 = new CLIJ2(clij);
            }
        }
        super.setClij(clij);
    }

    public void setCLIJ2(CLIJ2 clij2) {
        this.clij2 = clij2;
    }

    public CLIJ2 getCLIJ2() {
        if (clij2 == null) {
            clij2 = CLIJ2.getInstance();
        }
        return clij2;
    }
}
