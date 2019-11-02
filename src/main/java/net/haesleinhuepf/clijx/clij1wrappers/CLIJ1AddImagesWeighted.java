package net.haesleinhuepf.clijx.clij1wrappers;
import net.haesleinhuepf.clij.CLIJ;
import net.haesleinhuepf.clij.clearcl.ClearCLImage;
import net.haesleinhuepf.clij.kernels.Kernels;
// this is generated code. See src/test/java/net/haesleinhuepf/clijx/codegenerator for details
public class CLIJ1AddImagesWeighted{
   
    /**
     * Calculates the sum of pairs of pixels x and y from images X and Y weighted with factors a and b.
     * 
     * <pre>f(x, y, a, b) = x * a + y * b</pre>
     */
    public boolean addImagesWeighted(CLIJ clij, ClearCLImage summand1, ClearCLImage summand2, ClearCLImage destination, double factor1, double factor2) {
        return Kernels.addImagesWeighted(clij, summand1, summand2, destination, new Double (factor1).floatValue(), new Double (factor2).floatValue());
    }

}
