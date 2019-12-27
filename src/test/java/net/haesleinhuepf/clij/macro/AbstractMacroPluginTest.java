package net.haesleinhuepf.clij.macro;

import net.haesleinhuepf.clij.CLIJ;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.test.TestUtilities;

/**
 * AbstractMacroPluginTest
 * <p>
 * <p>
 * <p>
 * Author: @haesleinhuepf
 * 12 2018
 */
public class AbstractMacroPluginTest {
    protected boolean clBuffersEqual(CLIJ clij, ClearCLBuffer buffer1, ClearCLBuffer buffer2) {
        TestUtilities.clBuffersEqual(clij, buffer1, buffer2, 0);
        return true;
    }

}
