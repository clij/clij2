package net.haesleinhuepf.clijx.clij1wrappers;
import net.haesleinhuepf.clij.CLIJ;
import net.haesleinhuepf.clij.clearcl.ClearCLImage;
import net.haesleinhuepf.clij.kernels.Kernels;
// this is generated code. See src/test/java/net/haesleinhuepf/clijx/codegenerator for details
public class CLIJ1SumZProjection{
   
    /**
     * Determines the sum projection of an image along Z.
     */
    public boolean sumZProjection(CLIJ clij, ClearCLImage source, ClearCLImage destination_sum) {
        return Kernels.sumZProjection(clij, source, destination_sum);
    }

}
