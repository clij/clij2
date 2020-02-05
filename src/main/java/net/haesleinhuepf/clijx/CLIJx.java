package net.haesleinhuepf.clijx;

import net.haesleinhuepf.clij.CLIJ;
import net.haesleinhuepf.clij2.CLIJ2;
import net.haesleinhuepf.clijx.utilities.CLIJxOps;

/**
 * The CLIJx gateway
 */
public class CLIJx extends CLIJ2 implements CLIJxOps {
    private static CLIJx instance;

    public CLIJx getCLIJx() {
        return this;
    }


    /**
     * Marking this as deprecated as it will very likely go away before release.
     * Use CLIJx.getInstance() instead.
     * @param clij
     */
    @Deprecated
    public CLIJx(CLIJ clij) {
        super(clij);
    }

    public static CLIJx getInstance() {
        CLIJ clij = CLIJ.getInstance();
        if (instance == null || instance.clij != CLIJ.getInstance()) {
            instance = new CLIJx(clij);
        }
        return instance;
    }

    public static CLIJx getInstance(String id) {
        CLIJ clij = CLIJ.getInstance(id);
        if (instance == null || instance.clij != clij) {
            instance = new CLIJx(clij);
        }
        return instance;
    }

    public CLIJx __enter__() {
        clear();
        return this;
    }

}
