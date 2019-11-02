package net.haesleinhuepf.clijx.clij1wrappers;
import net.haesleinhuepf.clij.CLIJ;
import net.haesleinhuepf.clij.clearcl.ClearCLImage;
import net.haesleinhuepf.clij.kernels.Kernels;
// this is generated code. See src/test/java/net/haesleinhuepf/clijx/codegenerator for details
public class CLIJ1MedianSphere{
   
    /**
     * Computes the local median of a pixels rectangular neighborhood. The rectangle is specified by 
     * its half-width and half-height (radius).
     * 
     * For technical reasons, the area of the rectangle must have less than 1000 pixels.
     */
    public boolean medianSphere(CLIJ clij, ClearCLImage source, ClearCLImage destination, double radiusX, double radiusY) {
        return Kernels.medianSphere(clij, source, destination, new Double (radiusX).intValue(), new Double (radiusY).intValue());
    }

}
