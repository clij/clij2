package net.haesleinhuepf.clijx.clij1wrappers;
import net.haesleinhuepf.clij.CLIJ;
import net.haesleinhuepf.clij.clearcl.ClearCLImage;
import net.haesleinhuepf.clij.kernels.Kernels;
// this is generated code. See src/test/java/net/haesleinhuepf/clijx/codegenerator for details
public class CLIJ1SumPixelsSliceBySlice{
   
    /**
     * 
     */
    public double[] sumPixelsSliceBySlice(CLIJ clij, ClearCLImage arg1) {
        return Kernels.sumPixelsSliceBySlice(clij, arg1);
    }

}
