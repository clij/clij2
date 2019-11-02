package net.haesleinhuepf.clijx.clij1wrappers;
import net.haesleinhuepf.clij.CLIJ;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.kernels.Kernels;
// this is generated code. See src/test/java/net/haesleinhuepf/clijx/codegenerator for details
public class CLIJ1FillHistogram{
   
    /**
     * 
     */
    public boolean fillHistogram(CLIJ clij, ClearCLBuffer arg1, ClearCLBuffer arg2, double arg3, double arg4) {
        return Kernels.fillHistogram(clij, arg1, arg2, new Double (arg3).floatValue(), new Double (arg4).floatValue());
    }

}
