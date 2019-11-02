package net.haesleinhuepf.clijx.clij1wrappers;
import net.haesleinhuepf.clij.CLIJ;
import net.haesleinhuepf.clij.clearcl.ClearCLImage;
import net.haesleinhuepf.clij.kernels.Kernels;
// this is generated code. See src/test/java/net/haesleinhuepf/clijx/codegenerator for details
public class CLIJ1DivideImages{
   
    /**
     * Divides two images X and Y by each other pixel wise.
     * 
     * <pre>f(x, y) = x / y</pre>
     */
    public boolean divideImages(CLIJ clij, ClearCLImage divident, ClearCLImage divisor, ClearCLImage destination) {
        return Kernels.divideImages(clij, divident, divisor, destination);
    }

}
