package net.haesleinhuepf.clijx.clij1wrappers;
import net.haesleinhuepf.clij.CLIJ;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.kernels.Kernels;
// this is generated code. See src/test/java/net/haesleinhuepf/clijx/codegenerator for details
public class CLIJ1MeanZProjection{
   
    /**
     * Determines the mean average projection of an image along Z.
     */
    public boolean meanZProjection(CLIJ clij, ClearCLBuffer source, ClearCLBuffer destination) {
        return Kernels.meanZProjection(clij, source, destination);
    }

}
