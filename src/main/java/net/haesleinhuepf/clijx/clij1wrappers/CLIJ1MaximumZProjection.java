package net.haesleinhuepf.clijx.clij1wrappers;
import net.haesleinhuepf.clij.CLIJ;
import net.haesleinhuepf.clij.clearcl.ClearCLImage;
import net.haesleinhuepf.clij.kernels.Kernels;
// this is generated code. See src/test/java/net/haesleinhuepf/clijx/codegenerator for details
public class CLIJ1MaximumZProjection{
   
    /**
     * Determines the maximum projection of an image along Z.
     */
    public boolean maximumZProjection(CLIJ clij, ClearCLImage source, ClearCLImage destination_max) {
        return Kernels.maximumZProjection(clij, source, destination_max);
    }

}
