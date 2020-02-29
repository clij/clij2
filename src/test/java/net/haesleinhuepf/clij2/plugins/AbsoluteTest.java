package net.haesleinhuepf.clij2.plugins;

import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clijx.CLIJx;
import org.junit.Test;

import static org.junit.Assert.*;

public class AbsoluteTest {
    @Test
    public void testAbsolute4D() {
        CLIJx clijx = CLIJx.getInstance();
        ClearCLBuffer bufferIn = clijx.create(new long[]{2,3,4,5});
        ClearCLBuffer bufferOut = clijx.create(bufferIn);

        clijx.absolute(bufferIn, bufferOut);

        clijx.clear();

    }

    @Test
    public void testAbsolute3D() {
        CLIJx clijx = CLIJx.getInstance();
        ClearCLBuffer bufferIn = clijx.create(new long[]{2,3,4});
        ClearCLBuffer bufferOut = clijx.create(bufferIn);

        clijx.absolute(bufferIn, bufferOut);

        clijx.clear();

    }

}