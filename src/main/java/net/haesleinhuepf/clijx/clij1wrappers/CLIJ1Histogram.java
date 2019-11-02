package net.haesleinhuepf.clijx.clij1wrappers;
import net.haesleinhuepf.clij.CLIJ;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.kernels.Kernels;
// this is generated code. See src/test/java/net/haesleinhuepf/clijx/codegenerator for details
public class CLIJ1Histogram{
   
    /**
     * Determines the histogram of a given image.
     */
    public float[] histogram(CLIJ clij, ClearCLBuffer arg1, double arg2, double arg3, double arg4) {
        return Kernels.histogram(clij, arg1, new Double (arg2).floatValue(), new Double (arg3).floatValue(), new Double (arg4).intValue());
    }

}
