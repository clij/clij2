package net.haesleinhuepf.clijx.clij1wrappers;
import net.haesleinhuepf.clij.CLIJ;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.kernels.Kernels;
// this is generated code. See src/test/java/net/haesleinhuepf/clijx/codegenerator for details
public class CLIJ1SplitStack{
   
    /**
     * 
     */
    public boolean splitStack(CLIJ clij, ClearCLBuffer arg1, ClearCLBuffer[] arg2) {
        return Kernels.splitStack(clij, arg1, arg2);
    }

}
