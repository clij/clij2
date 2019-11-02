package net.haesleinhuepf.clijx.clij1wrappers;
import net.haesleinhuepf.clij.CLIJ;
import net.haesleinhuepf.clij.clearcl.ClearCLImage;
import net.haesleinhuepf.clij.kernels.Kernels;
// this is generated code. See src/test/java/net/haesleinhuepf/clijx/codegenerator for details
public class CLIJ1Flip{
   
    /**
     * Flips an image in X and/or Y direction depending on boolean flags.
     */
    public boolean flip(CLIJ clij, ClearCLImage arg1, ClearCLImage arg2, boolean arg3, boolean arg4, boolean arg5) {
        return Kernels.flip(clij, arg1, arg2, arg3, arg4, arg5);
    }

}
