package net.haesleinhuepf.clijx.clij1wrappers;
import net.haesleinhuepf.clij.CLIJ;
import net.haesleinhuepf.clij.clearcl.ClearCLImage;
import net.haesleinhuepf.clij.kernels.Kernels;
// this is generated code. See src/test/java/net/haesleinhuepf/clijx/codegenerator for details
public class CLIJ1Downsample{
   
    /**
     * Scales an image using given scaling factors for X and Y dimensions. The nearest-neighbor method
     * is applied. In ImageJ the method which is similar is called 'Interpolation method: none'.
     */
    public boolean downsample(CLIJ clij, ClearCLImage source, ClearCLImage destination, double factorX, double factorY) {
        return Kernels.downsample(clij, source, destination, new Double (factorX).floatValue(), new Double (factorY).floatValue());
    }

}
