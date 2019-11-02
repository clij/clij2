package net.haesleinhuepf.clijx.clij1wrappers;
import net.haesleinhuepf.clij.CLIJ;
import net.haesleinhuepf.clij.clearcl.ClearCLImage;
import net.haesleinhuepf.clij.kernels.Kernels;
// this is generated code. See src/test/java/net/haesleinhuepf/clijx/codegenerator for details
public class CLIJ1TenengradFusion{
   
    /**
     * 
     */
    public boolean tenengradFusion(CLIJ clij, ClearCLImage arg1, float[] arg2, float arg3, ClearCLImage[] arg4) {
        return Kernels.tenengradFusion(clij, arg1, arg2, arg3, arg4);
    }

}
