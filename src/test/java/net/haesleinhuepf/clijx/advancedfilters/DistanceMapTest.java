package net.haesleinhuepf.clijx.advancedfilters;

import net.haesleinhuepf.clij.CLIJ;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clijx.CLIJx;
import org.junit.Test;

import static org.junit.Assert.*;

public class DistanceMapTest {
    @Test
    public void test2D() {
        CLIJ.debug = true;
        CLIJx clijx = CLIJx.getInstance();

        ClearCLBuffer buffer = clijx.create(new long[]{10,10});
        ClearCLBuffer distance_map = clijx.create(buffer);

        clijx.op.set(buffer, 0f);
        clijx.op.drawBox(buffer, 2f, 2f, 5f, 5f);

        clijx.op.distanceMap(buffer, distance_map);

        double minimum = clijx.op.minimumOfAllPixels(distance_map);
        double maximum = clijx.op.maximumOfAllPixels(distance_map);

        assertEquals(0, minimum, 0.1);
        assertEquals(3, maximum, 0.1);
    }

    @Test
    public void test3D() {
        CLIJx clijx = CLIJx.getInstance();

        ClearCLBuffer buffer = clijx.create(new long[]{10, 10, 10});
        ClearCLBuffer distance_map = clijx.create(buffer);

        clijx.op.set(buffer, 0f);
        clijx.op.drawBox(buffer, 2f, 2f, 2f, 5f, 5f, 5f);

        clijx.op.distanceMap(buffer, distance_map);

        double minimum = clijx.op.minimumOfAllPixels(distance_map);
        double maximum = clijx.op.maximumOfAllPixels(distance_map);

        assertEquals(0, minimum, 0.1);
        assertEquals(3, maximum, 0.1);
    }
}