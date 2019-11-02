package net.haesleinhuepf.clijx.clij1wrappers;
import net.haesleinhuepf.clij.CLIJ;
import net.haesleinhuepf.clij.clearcl.ClearCLImage;
import net.haesleinhuepf.clij.kernels.Kernels;
// this is generated code. See src/test/java/net/haesleinhuepf/clijx/codegenerator for details
public class CLIJ1MinimumBox{
   
    /**
     * Computes the local minimum of a pixels rectangular neighborhood. The rectangles size is specified by 
     * its half-width and half-height (radius).
     */
    public boolean minimumBox(CLIJ clij, ClearCLImage arg1, ClearCLImage arg2, int arg3, int arg4, int arg5) {
        return Kernels.minimumBox(clij, arg1, arg2, arg3, arg4, arg5);
    }

}
