package net.haesleinhuepf.clijx.clij1wrappers;
import net.haesleinhuepf.clij.CLIJ;
import net.haesleinhuepf.clij.clearcl.ClearCLImage;
import net.haesleinhuepf.clij.kernels.Kernels;
// this is generated code. See src/test/java/net/haesleinhuepf/clijx/codegenerator for details
public class CLIJ1MultiplyImageAndScalar{
   
    /**
     * Multiplies all pixels value x in a given image X with a constant scalar s.
     * 
     * <pre>f(x, s) = x * s</pre>
     */
    public boolean multiplyImageAndScalar(CLIJ clij, ClearCLImage source, ClearCLImage destination, double scalar) {
        return Kernels.multiplyImageAndScalar(clij, source, destination, new Double (scalar).floatValue());
    }

}
