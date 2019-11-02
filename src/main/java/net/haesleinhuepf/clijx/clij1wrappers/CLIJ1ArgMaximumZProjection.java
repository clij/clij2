package net.haesleinhuepf.clijx.clij1wrappers;
import net.haesleinhuepf.clij.CLIJ;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.kernels.Kernels;
// this is generated code. See src/test/java/net/haesleinhuepf/clijx/codegenerator for details
public class CLIJ1ArgMaximumZProjection{
   
    /**
     * Determines the maximum projection of an image stack along Z.
     * Furthermore, another 2D image is generated with pixels containing the z-index where the maximum was found (zero based).
     */
    public boolean argMaximumZProjection(CLIJ clij, ClearCLBuffer source, ClearCLBuffer destination_max, ClearCLBuffer destination_arg_max) {
        return Kernels.argMaximumZProjection(clij, source, destination_max, destination_arg_max);
    }

}
