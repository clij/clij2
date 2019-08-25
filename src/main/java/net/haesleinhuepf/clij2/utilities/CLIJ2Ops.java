package net.haesleinhuepf.clij2.utilities;
import net.haesleinhuepf.clij.CLIJ;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.clearcl.ClearCLImage;
import net.imglib2.realtransform.AffineTransform2D;
import net.imglib2.realtransform.AffineTransform3D;
import net.haesleinhuepf.clij.kernels.Kernels;
import net.haesleinhuepf.clij.advancedfilters.BinaryUnion;
import net.haesleinhuepf.clij.advancedfilters.BinaryIntersection;
import net.haesleinhuepf.clij.advancedfilters.ConnectedComponentsLabeling;
import net.haesleinhuepf.clij.advancedfilters.CountNonZeroPixels;
import net.haesleinhuepf.clij.advancedfilters.CrossCorrelation;
import net.haesleinhuepf.clij.advancedfilters.DifferenceOfGaussian2D;
import net.haesleinhuepf.clij.advancedfilters.DifferenceOfGaussian3D;
import net.haesleinhuepf.clij.advancedfilters.Extrema;
import net.haesleinhuepf.clij.advancedfilters.LocalExtremaBox;
import net.haesleinhuepf.clij.advancedfilters.LocalID;
import net.haesleinhuepf.clij.advancedfilters.MaskLabel;
import net.haesleinhuepf.clij.advancedfilters.MeanClosestSpotDistance;
import net.haesleinhuepf.clij.advancedfilters.MeanSquaredError;
import net.haesleinhuepf.clij.advancedfilters.MedianZProjection;
import net.haesleinhuepf.clij.advancedfilters.NonzeroMinimum3DDiamond;
import net.haesleinhuepf.clij.advancedfilters.Paste2D;
import net.haesleinhuepf.clij.advancedfilters.Paste3D;
import net.haesleinhuepf.clij.advancedfilters.Presign;
import net.haesleinhuepf.clij.advancedfilters.SorensenDiceJaccardIndex;
import net.haesleinhuepf.clij.advancedfilters.StandardDeviationZProjection;
import net.haesleinhuepf.clij.advancedfilters.StackToTiles;
import net.haesleinhuepf.clij.advancedfilters.SubtractBackground2D;
import net.haesleinhuepf.clij.advancedfilters.SubtractBackground3D;
import net.haesleinhuepf.clij.advancedfilters.TopHatBox;
import net.haesleinhuepf.clij.advancedfilters.TopHatSphere;
import net.haesleinhuepf.clij.advancedmath.Exponential;
import net.haesleinhuepf.clij.advancedmath.Logarithm;
import net.haesleinhuepf.clij.matrix.GenerateDistanceMatrix;
import net.haesleinhuepf.clij.matrix.ShortestDistances;
import net.haesleinhuepf.clij.matrix.SpotsToPointList;
import net.haesleinhuepf.clij.matrix.TransposeXY;
import net.haesleinhuepf.clij.matrix.TransposeXZ;
import net.haesleinhuepf.clij.matrix.TransposeYZ;
import net.haesleinhuepf.clij.piv.FastParticleImageVelocimetry;
import net.haesleinhuepf.clij.piv.ParticleImageVelocimetry;
import net.haesleinhuepf.clij.piv.ParticleImageVelocimetryTimelapse;
import net.haesleinhuepf.clij.registration.DeformableRegistration2D;
import net.haesleinhuepf.clij.registration.TranslationRegistration;
import net.haesleinhuepf.clij.registration.TranslationTimelapseRegistration;
import net.haesleinhuepf.clij.advancedfilters.SetWhereXequalsY;
import net.haesleinhuepf.clij.advancedfilters.Laplace;
import net.haesleinhuepf.clij.advancedfilters.WriteValuesToPositions;
import net.haesleinhuepf.clij.advancedfilters.GetSize;
import net.haesleinhuepf.clij.matrix.MultiplyMatrix;
import net.haesleinhuepf.clij.matrix.MatrixEqual;
import net.haesleinhuepf.clij.advancedfilters.PowerImages;
import net.haesleinhuepf.clij.advancedmath.Equal;
import net.haesleinhuepf.clij.advancedmath.GreaterOrEqual;
import net.haesleinhuepf.clij.advancedmath.Greater;
import net.haesleinhuepf.clij.advancedmath.Smaller;
import net.haesleinhuepf.clij.advancedmath.SmallerOrEqual;
import net.haesleinhuepf.clij.advancedmath.NotEqual;
import net.haesleinhuepf.clij.advancedfilters.ReadImageFromDisc;
import net.haesleinhuepf.clij.advancedfilters.ReadRawImageFromDisc;
// this is generated code. See src/test/java/net/haesleinhuepf/clij2/codegenerator for details
public class CLIJ2Ops {
   private CLIJ clij;
   public CLIJ2Ops(CLIJ clij) {
       this.clij = clij;
   }

    // net.haesleinhuepf.clij.kernels.Kernels
    //----------------------------------------------------
    /**
     * Computes a binary image with pixel values 0 and 1. All pixel values x of a given input image with 
     * value larger or equal to a given threshold t will be set to 1.
     * 
     * f(x,t) = (1 if (x >= t); (0 otherwise))
     * 
     * This plugin is comparable to setting a raw threshold in ImageJ and using the 'Convert to Mask' menu.
     */
    public boolean threshold(ClearCLImage arg1, ClearCLImage arg2, Float arg3) {
        return Kernels.threshold(clij, arg1, arg2, arg3);
    }

    /**
     * Computes a binary image with pixel values 0 and 1. All pixel values x of a given input image with 
     * value larger or equal to a given threshold t will be set to 1.
     * 
     * f(x,t) = (1 if (x >= t); (0 otherwise))
     * 
     * This plugin is comparable to setting a raw threshold in ImageJ and using the 'Convert to Mask' menu.
     */
    public boolean threshold(ClearCLBuffer arg1, ClearCLBuffer arg2, Float arg3) {
        return Kernels.threshold(clij, arg1, arg2, arg3);
    }

    /**
     * Copies an image.
     * 
     * <pre>f(x) = x</pre>
     */
    public boolean copy(ClearCLImage arg1, ClearCLBuffer arg2) {
        return Kernels.copy(clij, arg1, arg2);
    }

    /**
     * Copies an image.
     * 
     * <pre>f(x) = x</pre>
     */
    public boolean copy(ClearCLBuffer arg1, ClearCLBuffer arg2) {
        return Kernels.copy(clij, arg1, arg2);
    }

    /**
     * Copies an image.
     * 
     * <pre>f(x) = x</pre>
     */
    public boolean copy(ClearCLImage arg1, ClearCLImage arg2) {
        return Kernels.copy(clij, arg1, arg2);
    }

    /**
     * Copies an image.
     * 
     * <pre>f(x) = x</pre>
     */
    public boolean copy(ClearCLBuffer arg1, ClearCLImage arg2) {
        return Kernels.copy(clij, arg1, arg2);
    }

    /**
     * Sets all pixel values x of a given image X to a constant value v.
     * 
     * <pre>f(x) = v</pre>
     */
    public boolean set(ClearCLBuffer arg1, Float arg2) {
        return Kernels.set(clij, arg1, arg2);
    }

    /**
     * Sets all pixel values x of a given image X to a constant value v.
     * 
     * <pre>f(x) = v</pre>
     */
    public boolean set(ClearCLImage arg1, Float arg2) {
        return Kernels.set(clij, arg1, arg2);
    }

    /**
     * Flips an image in X and/or Y direction depending on boolean flags.
     */
    public boolean flip(ClearCLBuffer arg1, ClearCLBuffer arg2, Boolean arg3, Boolean arg4, Boolean arg5) {
        return Kernels.flip(clij, arg1, arg2, arg3, arg4, arg5);
    }

    /**
     * Flips an image in X and/or Y direction depending on boolean flags.
     */
    public boolean flip(ClearCLImage arg1, ClearCLImage arg2, Boolean arg3, Boolean arg4) {
        return Kernels.flip(clij, arg1, arg2, arg3, arg4);
    }

    /**
     * Flips an image in X and/or Y direction depending on boolean flags.
     */
    public boolean flip(ClearCLBuffer arg1, ClearCLBuffer arg2, Boolean arg3, Boolean arg4) {
        return Kernels.flip(clij, arg1, arg2, arg3, arg4);
    }

    /**
     * Flips an image in X and/or Y direction depending on boolean flags.
     */
    public boolean flip(ClearCLImage arg1, ClearCLImage arg2, Boolean arg3, Boolean arg4, Boolean arg5) {
        return Kernels.flip(clij, arg1, arg2, arg3, arg4, arg5);
    }

    /**
     * Rotates a given input image by 90 degrees counter-clockwise. For that, X and Y axis of an image stack
     * are flipped. This operation is similar to ImageJs 'Reslice [/]' method but offers less flexibility 
     * such as interpolation.
     */
    public boolean rotateLeft(ClearCLImage arg1, ClearCLImage arg2) {
        return Kernels.rotateLeft(clij, arg1, arg2);
    }

    /**
     * Rotates a given input image by 90 degrees counter-clockwise. For that, X and Y axis of an image stack
     * are flipped. This operation is similar to ImageJs 'Reslice [/]' method but offers less flexibility 
     * such as interpolation.
     */
    public boolean rotateLeft(ClearCLBuffer arg1, ClearCLBuffer arg2) {
        return Kernels.rotateLeft(clij, arg1, arg2);
    }

    /**
     * Rotates a given input image by 90 degrees clockwise. For that, X and Y axis of an image stack
     * are flipped. This operation is similar to ImageJs 'Reslice [/]' method but offers less flexibility 
     * such as interpolation.
     */
    public boolean rotateRight(ClearCLBuffer arg1, ClearCLBuffer arg2) {
        return Kernels.rotateRight(clij, arg1, arg2);
    }

    /**
     * Rotates a given input image by 90 degrees clockwise. For that, X and Y axis of an image stack
     * are flipped. This operation is similar to ImageJs 'Reslice [/]' method but offers less flexibility 
     * such as interpolation.
     */
    public boolean rotateRight(ClearCLImage arg1, ClearCLImage arg2) {
        return Kernels.rotateRight(clij, arg1, arg2);
    }

    /**
     * Computes a masked image by applying a mask to an image. All pixel values x of image X will be copied
     * to the destination image in case pixel value m at the same position in the mask image is not equal to 
     * zero.
     * 
     * <pre>f(x,m) = (x if (m != 0); (0 otherwise))</pre>
     */
    public boolean mask(ClearCLBuffer arg1, ClearCLBuffer arg2, ClearCLBuffer arg3) {
        return Kernels.mask(clij, arg1, arg2, arg3);
    }

    /**
     * Computes a masked image by applying a mask to an image. All pixel values x of image X will be copied
     * to the destination image in case pixel value m at the same position in the mask image is not equal to 
     * zero.
     * 
     * <pre>f(x,m) = (x if (m != 0); (0 otherwise))</pre>
     */
    public boolean mask(ClearCLImage arg1, ClearCLImage arg2, ClearCLImage arg3) {
        return Kernels.mask(clij, arg1, arg2, arg3);
    }

    /**
     * Computes a binary image with pixel values 0 and 1 depending on if a pixel value x in image X 
     * was above of equal to the pixel value m in mask image M.
     * 
     * <pre>f(x) = (1 if (x >=  m)); (0 otherwise)</pre>
     */
    public boolean localThreshold(ClearCLBuffer arg1, ClearCLBuffer arg2, ClearCLBuffer arg3) {
        return Kernels.localThreshold(clij, arg1, arg2, arg3);
    }

    /**
     * Computes a binary image with pixel values 0 and 1 depending on if a pixel value x in image X 
     * was above of equal to the pixel value m in mask image M.
     * 
     * <pre>f(x) = (1 if (x >=  m)); (0 otherwise)</pre>
     */
    public boolean localThreshold(ClearCLImage arg1, ClearCLImage arg2, ClearCLImage arg3) {
        return Kernels.localThreshold(clij, arg1, arg2, arg3);
    }

    /**
     * 
     */
    public boolean countNonZeroPixelsLocallySliceBySlice(ClearCLImage arg1, ClearCLImage arg2, Integer arg3, Integer arg4) {
        return Kernels.countNonZeroPixelsLocallySliceBySlice(clij, arg1, arg2, arg3, arg4);
    }

    /**
     * 
     */
    public boolean countNonZeroPixelsLocallySliceBySlice(ClearCLBuffer arg1, ClearCLBuffer arg2, Integer arg3, Integer arg4) {
        return Kernels.countNonZeroPixelsLocallySliceBySlice(clij, arg1, arg2, arg3, arg4);
    }

    /**
     * The automatic thresholder utilizes the threshold methods from ImageJ on a histogram determined on 
     * the GPU to create binary images as similar as possible to ImageJ 'Apply Threshold' method. Enter one 
     * of these methods in the method text field:
     * [Default, Huang, Intermodes, IsoData, IJ_IsoData, Li, MaxEntropy, Mean, MinError, Minimum, Moments, Otsu, Percentile, RenyiEntropy, Shanbhag, Triangle, Yen]
     */
    public boolean automaticThreshold(ClearCLBuffer arg1, ClearCLBuffer arg2, String arg3, Float arg4, Float arg5, Integer arg6) {
        return Kernels.automaticThreshold(clij, arg1, arg2, arg3, arg4, arg5, arg6);
    }

    /**
     * The automatic thresholder utilizes the threshold methods from ImageJ on a histogram determined on 
     * the GPU to create binary images as similar as possible to ImageJ 'Apply Threshold' method. Enter one 
     * of these methods in the method text field:
     * [Default, Huang, Intermodes, IsoData, IJ_IsoData, Li, MaxEntropy, Mean, MinError, Minimum, Moments, Otsu, Percentile, RenyiEntropy, Shanbhag, Triangle, Yen]
     */
    public boolean automaticThreshold(ClearCLBuffer arg1, ClearCLBuffer arg2, String arg3) {
        return Kernels.automaticThreshold(clij, arg1, arg2, arg3);
    }

    /**
     * Computes the local median of a pixels ellipsoidal neighborhood. This is done slice-by-slice in a 3D 
     * image stack. The ellipses size is specified by its half-width and half-height (radius).
     * 
     * For technical reasons, the area of the ellipse must have less than 1000 pixels.
     */
    public boolean medianSliceBySliceSphere(ClearCLImage arg1, ClearCLImage arg2, Integer arg3, Integer arg4) {
        return Kernels.medianSliceBySliceSphere(clij, arg1, arg2, arg3, arg4);
    }

    /**
     * Computes the local median of a pixels ellipsoidal neighborhood. This is done slice-by-slice in a 3D 
     * image stack. The ellipses size is specified by its half-width and half-height (radius).
     * 
     * For technical reasons, the area of the ellipse must have less than 1000 pixels.
     */
    public boolean medianSliceBySliceSphere(ClearCLBuffer arg1, ClearCLBuffer arg2, Integer arg3, Integer arg4) {
        return Kernels.medianSliceBySliceSphere(clij, arg1, arg2, arg3, arg4);
    }

    /**
     * Calculates the sum of pairs of pixels x and y from images X and Y weighted with factors a and b.
     * 
     * <pre>f(x, y, a, b) = x * a + y * b</pre>
     */
    public boolean addImagesWeighted(ClearCLBuffer arg1, ClearCLBuffer arg2, ClearCLBuffer arg3, Float arg4, Float arg5) {
        return Kernels.addImagesWeighted(clij, arg1, arg2, arg3, arg4, arg5);
    }

    /**
     * Calculates the sum of pairs of pixels x and y from images X and Y weighted with factors a and b.
     * 
     * <pre>f(x, y, a, b) = x * a + y * b</pre>
     */
    public boolean addImagesWeighted(ClearCLImage arg1, ClearCLImage arg2, ClearCLImage arg3, Float arg4, Float arg5) {
        return Kernels.addImagesWeighted(clij, arg1, arg2, arg3, arg4, arg5);
    }

    /**
     * Applies an affine transform to a 2D image. Individual transforms must be separated by spaces.
     * 
     * Supported transforms:
     * * center: translate the coordinate origin to the center of the image
     * * -center: translate the coordinate origin back to the initial origin
     * * rotate=[angle]: rotate in X/Y plane (around Z-axis) by the given angle in degrees
     * * scale=[factor]: isotropic scaling according to given zoom factor
     * * scaleX=[factor]: scaling along X-axis according to given zoom factor
     * * scaleY=[factor]: scaling along Y-axis according to given zoom factor
     * * shearXY=[factor]: shearing along X-axis in XY plane according to given factor
     * * translateX=[distance]: translate along X-axis by distance given in pixels
     * * translateY=[distance]: translate along X-axis by distance given in pixels
     * 
     * Example transform:
     * transform = "center scale=2 rotate=45 -center";
     */
    public boolean affineTransform2D(ClearCLImage arg1, ClearCLImage arg2, float[] arg3) {
        return Kernels.affineTransform2D(clij, arg1, arg2, arg3);
    }

    /**
     * Applies an affine transform to a 2D image. Individual transforms must be separated by spaces.
     * 
     * Supported transforms:
     * * center: translate the coordinate origin to the center of the image
     * * -center: translate the coordinate origin back to the initial origin
     * * rotate=[angle]: rotate in X/Y plane (around Z-axis) by the given angle in degrees
     * * scale=[factor]: isotropic scaling according to given zoom factor
     * * scaleX=[factor]: scaling along X-axis according to given zoom factor
     * * scaleY=[factor]: scaling along Y-axis according to given zoom factor
     * * shearXY=[factor]: shearing along X-axis in XY plane according to given factor
     * * translateX=[distance]: translate along X-axis by distance given in pixels
     * * translateY=[distance]: translate along X-axis by distance given in pixels
     * 
     * Example transform:
     * transform = "center scale=2 rotate=45 -center";
     */
    public boolean affineTransform2D(ClearCLBuffer arg1, ClearCLBuffer arg2, float[] arg3) {
        return Kernels.affineTransform2D(clij, arg1, arg2, arg3);
    }

    /**
     * Applies an affine transform to a 2D image. Individual transforms must be separated by spaces.
     * 
     * Supported transforms:
     * * center: translate the coordinate origin to the center of the image
     * * -center: translate the coordinate origin back to the initial origin
     * * rotate=[angle]: rotate in X/Y plane (around Z-axis) by the given angle in degrees
     * * scale=[factor]: isotropic scaling according to given zoom factor
     * * scaleX=[factor]: scaling along X-axis according to given zoom factor
     * * scaleY=[factor]: scaling along Y-axis according to given zoom factor
     * * shearXY=[factor]: shearing along X-axis in XY plane according to given factor
     * * translateX=[distance]: translate along X-axis by distance given in pixels
     * * translateY=[distance]: translate along X-axis by distance given in pixels
     * 
     * Example transform:
     * transform = "center scale=2 rotate=45 -center";
     */
    public boolean affineTransform2D(ClearCLImage arg1, ClearCLImage arg2, AffineTransform2D arg3) {
        return Kernels.affineTransform2D(clij, arg1, arg2, arg3);
    }

    /**
     * Applies an affine transform to a 2D image. Individual transforms must be separated by spaces.
     * 
     * Supported transforms:
     * * center: translate the coordinate origin to the center of the image
     * * -center: translate the coordinate origin back to the initial origin
     * * rotate=[angle]: rotate in X/Y plane (around Z-axis) by the given angle in degrees
     * * scale=[factor]: isotropic scaling according to given zoom factor
     * * scaleX=[factor]: scaling along X-axis according to given zoom factor
     * * scaleY=[factor]: scaling along Y-axis according to given zoom factor
     * * shearXY=[factor]: shearing along X-axis in XY plane according to given factor
     * * translateX=[distance]: translate along X-axis by distance given in pixels
     * * translateY=[distance]: translate along X-axis by distance given in pixels
     * 
     * Example transform:
     * transform = "center scale=2 rotate=45 -center";
     */
    public boolean affineTransform2D(ClearCLBuffer arg1, ClearCLBuffer arg2, AffineTransform2D arg3) {
        return Kernels.affineTransform2D(clij, arg1, arg2, arg3);
    }

    /**
     * Scales an image using scaling factors 0.5 for X and Y dimensions. The Z dimension stays untouched. Thus, each slice is processed separately.
     * The median method is applied. Thus, each pixel value in the destination image equals to the median of
     * four corresponding pixels in the source image.
     */
    public boolean downsampleSliceBySliceHalfMedian(ClearCLImage arg1, ClearCLImage arg2) {
        return Kernels.downsampleSliceBySliceHalfMedian(clij, arg1, arg2);
    }

    /**
     * Scales an image using scaling factors 0.5 for X and Y dimensions. The Z dimension stays untouched. Thus, each slice is processed separately.
     * The median method is applied. Thus, each pixel value in the destination image equals to the median of
     * four corresponding pixels in the source image.
     */
    public boolean downsampleSliceBySliceHalfMedian(ClearCLBuffer arg1, ClearCLBuffer arg2) {
        return Kernels.downsampleSliceBySliceHalfMedian(clij, arg1, arg2);
    }

    /**
     * Applies an affine transform to a 3D image. Individual transforms must be separated by spaces.
     * 
     * Supported transforms:
     * * center: translate the coordinate origin to the center of the image
     * * -center: translate the coordinate origin back to the initial origin
     * * rotate=[angle]: rotate in X/Y plane (around Z-axis) by the given angle in degrees
     * * rotateX=[angle]: rotate in Y/Z plane (around X-axis) by the given angle in degrees
     * * rotateY=[angle]: rotate in X/Z plane (around Y-axis) by the given angle in degrees
     * * rotateZ=[angle]: rotate in X/Y plane (around Z-axis) by the given angle in degrees
     * * scale=[factor]: isotropic scaling according to given zoom factor
     * * scaleX=[factor]: scaling along X-axis according to given zoom factor
     * * scaleY=[factor]: scaling along Y-axis according to given zoom factor
     * * scaleZ=[factor]: scaling along Z-axis according to given zoom factor
     * * shearXY=[factor]: shearing along X-axis in XY plane according to given factor
     * * shearXZ=[factor]: shearing along X-axis in XZ plane according to given factor
     * * shearYX=[factor]: shearing along Y-axis in XY plane according to given factor
     * * shearYZ=[factor]: shearing along Y-axis in YZ plane according to given factor
     * * shearZX=[factor]: shearing along Z-axis in XZ plane according to given factor
     * * shearZY=[factor]: shearing along Z-axis in YZ plane according to given factor
     * * translateX=[distance]: translate along X-axis by distance given in pixels
     * * translateY=[distance]: translate along X-axis by distance given in pixels
     * * translateZ=[distance]: translate along X-axis by distance given in pixels
     * 
     * Example transform:
     * transform = "center scale=2 rotate=45 -center";
     */
    public boolean affineTransform3D(ClearCLImage arg1, ClearCLImage arg2, AffineTransform3D arg3) {
        return Kernels.affineTransform3D(clij, arg1, arg2, arg3);
    }

    /**
     * Applies an affine transform to a 3D image. Individual transforms must be separated by spaces.
     * 
     * Supported transforms:
     * * center: translate the coordinate origin to the center of the image
     * * -center: translate the coordinate origin back to the initial origin
     * * rotate=[angle]: rotate in X/Y plane (around Z-axis) by the given angle in degrees
     * * rotateX=[angle]: rotate in Y/Z plane (around X-axis) by the given angle in degrees
     * * rotateY=[angle]: rotate in X/Z plane (around Y-axis) by the given angle in degrees
     * * rotateZ=[angle]: rotate in X/Y plane (around Z-axis) by the given angle in degrees
     * * scale=[factor]: isotropic scaling according to given zoom factor
     * * scaleX=[factor]: scaling along X-axis according to given zoom factor
     * * scaleY=[factor]: scaling along Y-axis according to given zoom factor
     * * scaleZ=[factor]: scaling along Z-axis according to given zoom factor
     * * shearXY=[factor]: shearing along X-axis in XY plane according to given factor
     * * shearXZ=[factor]: shearing along X-axis in XZ plane according to given factor
     * * shearYX=[factor]: shearing along Y-axis in XY plane according to given factor
     * * shearYZ=[factor]: shearing along Y-axis in YZ plane according to given factor
     * * shearZX=[factor]: shearing along Z-axis in XZ plane according to given factor
     * * shearZY=[factor]: shearing along Z-axis in YZ plane according to given factor
     * * translateX=[distance]: translate along X-axis by distance given in pixels
     * * translateY=[distance]: translate along X-axis by distance given in pixels
     * * translateZ=[distance]: translate along X-axis by distance given in pixels
     * 
     * Example transform:
     * transform = "center scale=2 rotate=45 -center";
     */
    public boolean affineTransform3D(ClearCLBuffer arg1, ClearCLBuffer arg2, float[] arg3) {
        return Kernels.affineTransform3D(clij, arg1, arg2, arg3);
    }

    /**
     * Applies an affine transform to a 3D image. Individual transforms must be separated by spaces.
     * 
     * Supported transforms:
     * * center: translate the coordinate origin to the center of the image
     * * -center: translate the coordinate origin back to the initial origin
     * * rotate=[angle]: rotate in X/Y plane (around Z-axis) by the given angle in degrees
     * * rotateX=[angle]: rotate in Y/Z plane (around X-axis) by the given angle in degrees
     * * rotateY=[angle]: rotate in X/Z plane (around Y-axis) by the given angle in degrees
     * * rotateZ=[angle]: rotate in X/Y plane (around Z-axis) by the given angle in degrees
     * * scale=[factor]: isotropic scaling according to given zoom factor
     * * scaleX=[factor]: scaling along X-axis according to given zoom factor
     * * scaleY=[factor]: scaling along Y-axis according to given zoom factor
     * * scaleZ=[factor]: scaling along Z-axis according to given zoom factor
     * * shearXY=[factor]: shearing along X-axis in XY plane according to given factor
     * * shearXZ=[factor]: shearing along X-axis in XZ plane according to given factor
     * * shearYX=[factor]: shearing along Y-axis in XY plane according to given factor
     * * shearYZ=[factor]: shearing along Y-axis in YZ plane according to given factor
     * * shearZX=[factor]: shearing along Z-axis in XZ plane according to given factor
     * * shearZY=[factor]: shearing along Z-axis in YZ plane according to given factor
     * * translateX=[distance]: translate along X-axis by distance given in pixels
     * * translateY=[distance]: translate along X-axis by distance given in pixels
     * * translateZ=[distance]: translate along X-axis by distance given in pixels
     * 
     * Example transform:
     * transform = "center scale=2 rotate=45 -center";
     */
    public boolean affineTransform3D(ClearCLBuffer arg1, ClearCLBuffer arg2, AffineTransform3D arg3) {
        return Kernels.affineTransform3D(clij, arg1, arg2, arg3);
    }

    /**
     * Applies an affine transform to a 3D image. Individual transforms must be separated by spaces.
     * 
     * Supported transforms:
     * * center: translate the coordinate origin to the center of the image
     * * -center: translate the coordinate origin back to the initial origin
     * * rotate=[angle]: rotate in X/Y plane (around Z-axis) by the given angle in degrees
     * * rotateX=[angle]: rotate in Y/Z plane (around X-axis) by the given angle in degrees
     * * rotateY=[angle]: rotate in X/Z plane (around Y-axis) by the given angle in degrees
     * * rotateZ=[angle]: rotate in X/Y plane (around Z-axis) by the given angle in degrees
     * * scale=[factor]: isotropic scaling according to given zoom factor
     * * scaleX=[factor]: scaling along X-axis according to given zoom factor
     * * scaleY=[factor]: scaling along Y-axis according to given zoom factor
     * * scaleZ=[factor]: scaling along Z-axis according to given zoom factor
     * * shearXY=[factor]: shearing along X-axis in XY plane according to given factor
     * * shearXZ=[factor]: shearing along X-axis in XZ plane according to given factor
     * * shearYX=[factor]: shearing along Y-axis in XY plane according to given factor
     * * shearYZ=[factor]: shearing along Y-axis in YZ plane according to given factor
     * * shearZX=[factor]: shearing along Z-axis in XZ plane according to given factor
     * * shearZY=[factor]: shearing along Z-axis in YZ plane according to given factor
     * * translateX=[distance]: translate along X-axis by distance given in pixels
     * * translateY=[distance]: translate along X-axis by distance given in pixels
     * * translateZ=[distance]: translate along X-axis by distance given in pixels
     * 
     * Example transform:
     * transform = "center scale=2 rotate=45 -center";
     */
    public boolean affineTransform3D(ClearCLImage arg1, ClearCLImage arg2, float[] arg3) {
        return Kernels.affineTransform3D(clij, arg1, arg2, arg3);
    }

    /**
     * Computes a binary image with pixel values 0 and 1 containing the binary erosion of a given input image.
     * The erosion takes the Moore-neighborhood (8 pixels in 2D and 26 pixels in 3d) into account.
     * The pixels in the input image with pixel value not equal to 0 will be interpreted as 1.
     * 
     * This method is comparable to the 'Erode' menu in ImageJ in case it is applied to a 2D image. The only
     * difference is that the output image contains values 0 and 1 instead of 0 and 255.
     * 
     * This filter is applied slice by slice in 2D.
     */
    public boolean erodeBoxSliceBySlice(ClearCLBuffer arg1, ClearCLBuffer arg2) {
        return Kernels.erodeBoxSliceBySlice(clij, arg1, arg2);
    }

    /**
     * Computes a binary image with pixel values 0 and 1 containing the binary erosion of a given input image.
     * The erosion takes the Moore-neighborhood (8 pixels in 2D and 26 pixels in 3d) into account.
     * The pixels in the input image with pixel value not equal to 0 will be interpreted as 1.
     * 
     * This method is comparable to the 'Erode' menu in ImageJ in case it is applied to a 2D image. The only
     * difference is that the output image contains values 0 and 1 instead of 0 and 255.
     * 
     * This filter is applied slice by slice in 2D.
     */
    public boolean erodeBoxSliceBySlice(ClearCLImage arg1, ClearCLImage arg2) {
        return Kernels.erodeBoxSliceBySlice(clij, arg1, arg2);
    }

    /**
     * Determines the maximum of all pixels in a given image. It will be stored in a new row of ImageJs
     * Results table in the column 'Max'.
     */
    public double maximumOfAllPixels(ClearCLImage arg1) {
        return Kernels.maximumOfAllPixels(clij, arg1);
    }

    /**
     * Determines the maximum of all pixels in a given image. It will be stored in a new row of ImageJs
     * Results table in the column 'Max'.
     */
    public double maximumOfAllPixels(ClearCLBuffer arg1) {
        return Kernels.maximumOfAllPixels(clij, arg1);
    }

    /**
     * Detects local minima in a given square neighborhood of an input image stack. The input image stack is 
     * processed slice by slice. Pixels in the resulting image are set to 1 if there is no other pixel in a 
     * given radius which has a lower intensity, and to 0 otherwise.
     */
    public boolean detectMinimaSliceBySliceBox(ClearCLBuffer arg1, ClearCLBuffer arg2, Integer arg3) {
        return Kernels.detectMinimaSliceBySliceBox(clij, arg1, arg2, arg3);
    }

    /**
     * Detects local minima in a given square neighborhood of an input image stack. The input image stack is 
     * processed slice by slice. Pixels in the resulting image are set to 1 if there is no other pixel in a 
     * given radius which has a lower intensity, and to 0 otherwise.
     */
    public boolean detectMinimaSliceBySliceBox(ClearCLImage arg1, ClearCLImage arg2, Integer arg3) {
        return Kernels.detectMinimaSliceBySliceBox(clij, arg1, arg2, arg3);
    }

    /**
     * Determines the minimum projection of an image along Z.
     */
    public boolean minimumZProjection(ClearCLBuffer arg1, ClearCLBuffer arg2) {
        return Kernels.minimumZProjection(clij, arg1, arg2);
    }

    /**
     * Determines the minimum projection of an image along Z.
     */
    public boolean minimumZProjection(ClearCLImage arg1, ClearCLImage arg2) {
        return Kernels.minimumZProjection(clij, arg1, arg2);
    }

    /**
     * Determines the maximum projection of an image along Z.
     */
    public boolean maximumZProjection(ClearCLImage arg1, ClearCLImage arg2) {
        return Kernels.maximumZProjection(clij, arg1, arg2);
    }

    /**
     * Determines the maximum projection of an image along Z.
     */
    public boolean maximumZProjection(ClearCLBuffer arg1, ClearCLBuffer arg2) {
        return Kernels.maximumZProjection(clij, arg1, arg2);
    }

    /**
     * Detects local maxima in a given square neighborhood of an input image stack. The input image stack is 
     * processed slice by slice. Pixels in the resulting image are set to 1 if there is no other pixel in a 
     * given radius which has a higher intensity, and to 0 otherwise.
     */
    public boolean detectMaximaSliceBySliceBox(ClearCLBuffer arg1, ClearCLBuffer arg2, Integer arg3) {
        return Kernels.detectMaximaSliceBySliceBox(clij, arg1, arg2, arg3);
    }

    /**
     * Detects local maxima in a given square neighborhood of an input image stack. The input image stack is 
     * processed slice by slice. Pixels in the resulting image are set to 1 if there is no other pixel in a 
     * given radius which has a higher intensity, and to 0 otherwise.
     */
    public boolean detectMaximaSliceBySliceBox(ClearCLImage arg1, ClearCLImage arg2, Integer arg3) {
        return Kernels.detectMaximaSliceBySliceBox(clij, arg1, arg2, arg3);
    }

    /**
     * Computes the maximum of a constant scalar s and each pixel value x in a given image X.
     * 
     * <pre>f(x, s) = max(x, s)</pre>
     */
    public boolean maximumImageAndScalar(ClearCLBuffer arg1, ClearCLBuffer arg2, Float arg3) {
        return Kernels.maximumImageAndScalar(clij, arg1, arg2, arg3);
    }

    /**
     * Computes the maximum of a constant scalar s and each pixel value x in a given image X.
     * 
     * <pre>f(x, s) = max(x, s)</pre>
     */
    public boolean maximumImageAndScalar(ClearCLImage arg1, ClearCLImage arg2, Float arg3) {
        return Kernels.maximumImageAndScalar(clij, arg1, arg2, arg3);
    }

    /**
     * 
     */
    public boolean multiplyImageAndCoordinate(ClearCLImage arg1, ClearCLImage arg2, Integer arg3) {
        return Kernels.multiplyImageAndCoordinate(clij, arg1, arg2, arg3);
    }

    /**
     * 
     */
    public boolean multiplyImageAndCoordinate(ClearCLBuffer arg1, ClearCLBuffer arg2, Integer arg3) {
        return Kernels.multiplyImageAndCoordinate(clij, arg1, arg2, arg3);
    }

    /**
     * Determines the minimum of all pixels in a given image. It will be stored in a new row of ImageJs
     * Results table in the column 'Min'.
     */
    public double minimumOfAllPixels(ClearCLImage arg1) {
        return Kernels.minimumOfAllPixels(clij, arg1);
    }

    /**
     * Determines the minimum of all pixels in a given image. It will be stored in a new row of ImageJs
     * Results table in the column 'Min'.
     */
    public double minimumOfAllPixels(ClearCLBuffer arg1) {
        return Kernels.minimumOfAllPixels(clij, arg1);
    }

    /**
     * 
     */
    public boolean tenengradWeightsSliceBySlice(ClearCLImage arg1, ClearCLImage arg2) {
        return Kernels.tenengradWeightsSliceBySlice(clij, arg1, arg2);
    }

    /**
     * Determines the maximum projection of an image stack along Z.
     * Furthermore, another 2D image is generated with pixels containing the z-index where the maximum was found (zero based).
     */
    public boolean argMaximumZProjection(ClearCLImage arg1, ClearCLImage arg2, ClearCLImage arg3) {
        return Kernels.argMaximumZProjection(clij, arg1, arg2, arg3);
    }

    /**
     * Determines the maximum projection of an image stack along Z.
     * Furthermore, another 2D image is generated with pixels containing the z-index where the maximum was found (zero based).
     */
    public boolean argMaximumZProjection(ClearCLBuffer arg1, ClearCLBuffer arg2, ClearCLBuffer arg3) {
        return Kernels.argMaximumZProjection(clij, arg1, arg2, arg3);
    }

    /**
     * Adds a scalar value s to all pixels x of a given image X.
     * 
     * <pre>f(x, s) = x + s</pre>
     */
    public boolean addImageAndScalar(ClearCLBuffer arg1, ClearCLBuffer arg2, Float arg3) {
        return Kernels.addImageAndScalar(clij, arg1, arg2, arg3);
    }

    /**
     * Adds a scalar value s to all pixels x of a given image X.
     * 
     * <pre>f(x, s) = x + s</pre>
     */
    public boolean addImageAndScalar(ClearCLImage arg1, ClearCLImage arg2, Float arg3) {
        return Kernels.addImageAndScalar(clij, arg1, arg2, arg3);
    }

    /**
     * Computes the local mean average of a pixels ellipsoidal 2D neighborhood in an image stack 
     * slice by slice. The ellipses size is specified by its half-width and half-height (radius).
     * 
     * This filter is applied slice by slice in 2D.
     */
    public boolean meanSliceBySliceSphere(ClearCLBuffer arg1, ClearCLBuffer arg2, Integer arg3, Integer arg4) {
        return Kernels.meanSliceBySliceSphere(clij, arg1, arg2, arg3, arg4);
    }

    /**
     * Computes the local mean average of a pixels ellipsoidal 2D neighborhood in an image stack 
     * slice by slice. The ellipses size is specified by its half-width and half-height (radius).
     * 
     * This filter is applied slice by slice in 2D.
     */
    public boolean meanSliceBySliceSphere(ClearCLImage arg1, ClearCLImage arg2, Integer arg3, Integer arg4) {
        return Kernels.meanSliceBySliceSphere(clij, arg1, arg2, arg3, arg4);
    }

    /**
     * 
     */
    public double[] sumPixelsSliceBySlice(ClearCLImage arg1) {
        return Kernels.sumPixelsSliceBySlice(clij, arg1);
    }

    /**
     * 
     */
    public double[] sumPixelsSliceBySlice(ClearCLBuffer arg1) {
        return Kernels.sumPixelsSliceBySlice(clij, arg1);
    }

    /**
     * 
     */
    public boolean countNonZeroVoxelsLocally(ClearCLImage arg1, ClearCLImage arg2, Integer arg3, Integer arg4, Integer arg5) {
        return Kernels.countNonZeroVoxelsLocally(clij, arg1, arg2, arg3, arg4, arg5);
    }

    /**
     * 
     */
    public boolean countNonZeroVoxelsLocally(ClearCLBuffer arg1, ClearCLBuffer arg2, Integer arg3, Integer arg4, Integer arg5) {
        return Kernels.countNonZeroVoxelsLocally(clij, arg1, arg2, arg3, arg4, arg5);
    }

    /**
     * 
     */
    public boolean differenceOfGaussianSliceBySlice(ClearCLImage arg1, ClearCLImage arg2, Integer arg3, Float arg4, Float arg5) {
        return Kernels.differenceOfGaussianSliceBySlice(clij, arg1, arg2, arg3, arg4, arg5);
    }

    /**
     * Computes a masked image by applying a 2D mask to an image stack. All pixel values x of image X will be copied
     * to the destination image in case pixel value m at the same spatial position in the mask image is not equal to 
     * zero.
     * 
     * <pre>f(x,m) = (x if (m != 0); (0 otherwise))</pre>
     */
    public boolean maskStackWithPlane(ClearCLBuffer arg1, ClearCLBuffer arg2, ClearCLBuffer arg3) {
        return Kernels.maskStackWithPlane(clij, arg1, arg2, arg3);
    }

    /**
     * Computes a masked image by applying a 2D mask to an image stack. All pixel values x of image X will be copied
     * to the destination image in case pixel value m at the same spatial position in the mask image is not equal to 
     * zero.
     * 
     * <pre>f(x,m) = (x if (m != 0); (0 otherwise))</pre>
     */
    public boolean maskStackWithPlane(ClearCLImage arg1, ClearCLImage arg2, ClearCLImage arg3) {
        return Kernels.maskStackWithPlane(clij, arg1, arg2, arg3);
    }

    /**
     * Multiplies all pixels value x in a given image X with a constant scalar s.
     * 
     * <pre>f(x, s) = x * s</pre>
     */
    public boolean multiplyImageAndScalar(ClearCLBuffer arg1, ClearCLBuffer arg2, Float arg3) {
        return Kernels.multiplyImageAndScalar(clij, arg1, arg2, arg3);
    }

    /**
     * Multiplies all pixels value x in a given image X with a constant scalar s.
     * 
     * <pre>f(x, s) = x * s</pre>
     */
    public boolean multiplyImageAndScalar(ClearCLImage arg1, ClearCLImage arg2, Float arg3) {
        return Kernels.multiplyImageAndScalar(clij, arg1, arg2, arg3);
    }

    /**
     * 
     */
    public boolean convertToImageJBinary(ClearCLImage arg1, ClearCLImage arg2) {
        return Kernels.convertToImageJBinary(clij, arg1, arg2);
    }

    /**
     * 
     */
    public boolean convertToImageJBinary(ClearCLBuffer arg1, ClearCLBuffer arg2) {
        return Kernels.convertToImageJBinary(clij, arg1, arg2);
    }

    /**
     * 
     */
    public boolean detectOptimaSliceBySlice(ClearCLBuffer arg1, ClearCLBuffer arg2, Integer arg3, Boolean arg4) {
        return Kernels.detectOptimaSliceBySlice(clij, arg1, arg2, arg3, arg4);
    }

    /**
     * 
     */
    public boolean detectOptimaSliceBySlice(ClearCLImage arg1, ClearCLImage arg2, Integer arg3, Boolean arg4) {
        return Kernels.detectOptimaSliceBySlice(clij, arg1, arg2, arg3, arg4);
    }

    /**
     * Applies Gaussian blur to the input image twice resulting in two images which are then subtracted from each other.
     */
    public boolean differenceOfGaussian(ClearCLImage arg1, ClearCLImage arg2, Integer arg3, Float arg4, Float arg5) {
        return Kernels.differenceOfGaussian(clij, arg1, arg2, arg3, arg4, arg5);
    }

    /**
     * Computes a binary image with pixel values 0 and 1 containing the binary dilation of a given input image.
     * The dilation takes the Moore-neighborhood (8 pixels in 2D and 26 pixels in 3d) into account.
     * The pixels in the input image with pixel value not equal to 0 will be interpreted as 1.
     * 
     * This method is comparable to the 'Dilate' menu in ImageJ in case it is applied to a 2D image. The only
     * difference is that the output image contains values 0 and 1 instead of 0 and 255.
     * 
     * This filter is applied slice by slice in 2D.
     */
    public boolean dilateBoxSliceBySlice(ClearCLBuffer arg1, ClearCLBuffer arg2) {
        return Kernels.dilateBoxSliceBySlice(clij, arg1, arg2);
    }

    /**
     * Computes a binary image with pixel values 0 and 1 containing the binary dilation of a given input image.
     * The dilation takes the Moore-neighborhood (8 pixels in 2D and 26 pixels in 3d) into account.
     * The pixels in the input image with pixel value not equal to 0 will be interpreted as 1.
     * 
     * This method is comparable to the 'Dilate' menu in ImageJ in case it is applied to a 2D image. The only
     * difference is that the output image contains values 0 and 1 instead of 0 and 255.
     * 
     * This filter is applied slice by slice in 2D.
     */
    public boolean dilateBoxSliceBySlice(ClearCLImage arg1, ClearCLImage arg2) {
        return Kernels.dilateBoxSliceBySlice(clij, arg1, arg2);
    }

    /**
     * Computes the local maximum of a pixels ellipsoidal 2D neighborhood in an image stack 
     * slice by slice. The ellipses size is specified by its half-width and half-height (radius).
     * 
     * This filter is applied slice by slice in 2D.
     */
    public boolean maximumSliceBySliceSphere(ClearCLImage arg1, ClearCLImage arg2, Integer arg3, Integer arg4) {
        return Kernels.maximumSliceBySliceSphere(clij, arg1, arg2, arg3, arg4);
    }

    /**
     * Computes the local maximum of a pixels ellipsoidal 2D neighborhood in an image stack 
     * slice by slice. The ellipses size is specified by its half-width and half-height (radius).
     * 
     * This filter is applied slice by slice in 2D.
     */
    public boolean maximumSliceBySliceSphere(ClearCLBuffer arg1, ClearCLBuffer arg2, Integer arg3, Integer arg4) {
        return Kernels.maximumSliceBySliceSphere(clij, arg1, arg2, arg3, arg4);
    }

    /**
     * Computes the maximum of a constant scalar s and each pixel value x in a given image X.
     * 
     * <pre>f(x, s) = min(x, s)</pre>
     */
    public boolean minimumImageAndScalar(ClearCLImage arg1, ClearCLImage arg2, Float arg3) {
        return Kernels.minimumImageAndScalar(clij, arg1, arg2, arg3);
    }

    /**
     * Computes the maximum of a constant scalar s and each pixel value x in a given image X.
     * 
     * <pre>f(x, s) = min(x, s)</pre>
     */
    public boolean minimumImageAndScalar(ClearCLBuffer arg1, ClearCLBuffer arg2, Float arg3) {
        return Kernels.minimumImageAndScalar(clij, arg1, arg2, arg3);
    }

    /**
     * Determines the maximum projection of an image along a given dimension. Furthermore, the X and Y
     *  dimesions of the resulting image must be specified by the user according to its definition:
     * X = 0
     * Y = 1
     * Z = 2
     * 
     */
    public boolean maximumXYZProjection(ClearCLImage arg1, ClearCLImage arg2, Integer arg3, Integer arg4, Integer arg5) {
        return Kernels.maximumXYZProjection(clij, arg1, arg2, arg3, arg4, arg5);
    }

    /**
     * Determines the maximum projection of an image along a given dimension. Furthermore, the X and Y
     *  dimesions of the resulting image must be specified by the user according to its definition:
     * X = 0
     * Y = 1
     * Z = 2
     * 
     */
    public boolean maximumXYZProjection(ClearCLBuffer arg1, ClearCLBuffer arg2, Integer arg3, Integer arg4, Integer arg5) {
        return Kernels.maximumXYZProjection(clij, arg1, arg2, arg3, arg4, arg5);
    }

    /**
     * Computes the local median of a pixels rectangular neighborhood. This is done slice-by-slice in a 3D 
     * image stack. The rectangle is specified by its half-width and half-height (radius).
     * 
     * For technical reasons, the area of the rectangle must have less than 1000 pixels.
     */
    public boolean medianSliceBySliceBox(ClearCLImage arg1, ClearCLImage arg2, Integer arg3, Integer arg4) {
        return Kernels.medianSliceBySliceBox(clij, arg1, arg2, arg3, arg4);
    }

    /**
     * Computes the local median of a pixels rectangular neighborhood. This is done slice-by-slice in a 3D 
     * image stack. The rectangle is specified by its half-width and half-height (radius).
     * 
     * For technical reasons, the area of the rectangle must have less than 1000 pixels.
     */
    public boolean medianSliceBySliceBox(ClearCLBuffer arg1, ClearCLBuffer arg2, Integer arg3, Integer arg4) {
        return Kernels.medianSliceBySliceBox(clij, arg1, arg2, arg3, arg4);
    }

    /**
     * Computes the local minimum of a pixels ellipsoidal 2D neighborhood in an image stack 
     * slice by slice. The ellipses size is specified by its half-width and half-height (radius).
     * 
     * This filter is applied slice by slice in 2D.
     */
    public boolean minimumSliceBySliceSphere(ClearCLImage arg1, ClearCLImage arg2, Integer arg3, Integer arg4) {
        return Kernels.minimumSliceBySliceSphere(clij, arg1, arg2, arg3, arg4);
    }

    /**
     * Computes the local minimum of a pixels ellipsoidal 2D neighborhood in an image stack 
     * slice by slice. The ellipses size is specified by its half-width and half-height (radius).
     * 
     * This filter is applied slice by slice in 2D.
     */
    public boolean minimumSliceBySliceSphere(ClearCLBuffer arg1, ClearCLBuffer arg2, Integer arg3, Integer arg4) {
        return Kernels.minimumSliceBySliceSphere(clij, arg1, arg2, arg3, arg4);
    }

    /**
     * Computes a binary image with pixel values 0 and 1 containing the binary dilation of a given input image.
     * The dilation takes the von-Neumann-neighborhood (4 pixels in 2D and 6 pixels in 3d) into account.
     * The pixels in the input image with pixel value not equal to 0 will be interpreted as 1.
     * 
     * This filter is applied slice by slice in 2D.
     */
    public boolean dilateSphereSliceBySlice(ClearCLImage arg1, ClearCLImage arg2) {
        return Kernels.dilateSphereSliceBySlice(clij, arg1, arg2);
    }

    /**
     * Computes a binary image with pixel values 0 and 1 containing the binary dilation of a given input image.
     * The dilation takes the von-Neumann-neighborhood (4 pixels in 2D and 6 pixels in 3d) into account.
     * The pixels in the input image with pixel value not equal to 0 will be interpreted as 1.
     * 
     * This filter is applied slice by slice in 2D.
     */
    public boolean dilateSphereSliceBySlice(ClearCLBuffer arg1, ClearCLBuffer arg2) {
        return Kernels.dilateSphereSliceBySlice(clij, arg1, arg2);
    }

    /**
     * 
     */
    public boolean multiplySliceBySliceWithScalars(ClearCLBuffer arg1, ClearCLBuffer arg2, float[] arg3) {
        return Kernels.multiplySliceBySliceWithScalars(clij, arg1, arg2, arg3);
    }

    /**
     * 
     */
    public boolean multiplySliceBySliceWithScalars(ClearCLImage arg1, ClearCLImage arg2, float[] arg3) {
        return Kernels.multiplySliceBySliceWithScalars(clij, arg1, arg2, arg3);
    }

    /**
     * 
     */
    public boolean countNonZeroPixelsLocally(ClearCLImage arg1, ClearCLImage arg2, Integer arg3, Integer arg4) {
        return Kernels.countNonZeroPixelsLocally(clij, arg1, arg2, arg3, arg4);
    }

    /**
     * 
     */
    public boolean countNonZeroPixelsLocally(ClearCLBuffer arg1, ClearCLBuffer arg2, Integer arg3, Integer arg4) {
        return Kernels.countNonZeroPixelsLocally(clij, arg1, arg2, arg3, arg4);
    }

    /**
     * Computes a binary image with pixel values 0 and 1 containing the binary erosion of a given input image.
     * The erosion takes the von-Neumann-neighborhood (4 pixels in 2D and 6 pixels in 3d) into account.
     * The pixels in the input image with pixel value not equal to 0 will be interpreted as 1.
     * 
     * This filter is applied slice by slice in 2D.
     */
    public boolean erodeSphereSliceBySlice(ClearCLBuffer arg1, ClearCLBuffer arg2) {
        return Kernels.erodeSphereSliceBySlice(clij, arg1, arg2);
    }

    /**
     * Computes a binary image with pixel values 0 and 1 containing the binary erosion of a given input image.
     * The erosion takes the von-Neumann-neighborhood (4 pixels in 2D and 6 pixels in 3d) into account.
     * The pixels in the input image with pixel value not equal to 0 will be interpreted as 1.
     * 
     * This filter is applied slice by slice in 2D.
     */
    public boolean erodeSphereSliceBySlice(ClearCLImage arg1, ClearCLImage arg2) {
        return Kernels.erodeSphereSliceBySlice(clij, arg1, arg2);
    }

    /**
     * Multiplies all pairs of pixel values x and y from an image stack X and a 2D image Y. x and y are at 
     * the same spatial position within a plane.
     * 
     * <pre>f(x, y) = x * y</pre>
     */
    public boolean multiplyStackWithPlane(ClearCLImage arg1, ClearCLImage arg2, ClearCLImage arg3) {
        return Kernels.multiplyStackWithPlane(clij, arg1, arg2, arg3);
    }

    /**
     * Multiplies all pairs of pixel values x and y from an image stack X and a 2D image Y. x and y are at 
     * the same spatial position within a plane.
     * 
     * <pre>f(x, y) = x * y</pre>
     */
    public boolean multiplyStackWithPlane(ClearCLBuffer arg1, ClearCLBuffer arg2, ClearCLBuffer arg3) {
        return Kernels.multiplyStackWithPlane(clij, arg1, arg2, arg3);
    }

    /**
     * Flippes X, Y and Z axis of an image stack. This operation is similar to ImageJs 'Reslice [/]' method 
     *  but offers less flexibility such as interpolation.
     */
    public boolean resliceRight(ClearCLImage arg1, ClearCLImage arg2) {
        return Kernels.resliceRight(clij, arg1, arg2);
    }

    /**
     * Flippes X, Y and Z axis of an image stack. This operation is similar to ImageJs 'Reslice [/]' method 
     *  but offers less flexibility such as interpolation.
     */
    public boolean resliceRight(ClearCLBuffer arg1, ClearCLBuffer arg2) {
        return Kernels.resliceRight(clij, arg1, arg2);
    }

    /**
     * Determines the mean average projection of an image along Z.
     */
    public boolean meanZProjection(ClearCLBuffer arg1, ClearCLBuffer arg2) {
        return Kernels.meanZProjection(clij, arg1, arg2);
    }

    /**
     * Determines the mean average projection of an image along Z.
     */
    public boolean meanZProjection(ClearCLImage arg1, ClearCLImage arg2) {
        return Kernels.meanZProjection(clij, arg1, arg2);
    }

    /**
     * Computes the local mean average of a pixels rectangular neighborhood. The rectangles size is specified by 
     * its half-width and half-height (radius).
     */
    public boolean meanBox(ClearCLImage arg1, ClearCLImage arg2, int arg3, int arg4, int arg5) {
        return Kernels.meanBox(clij, arg1, arg2, arg3, arg4, arg5);
    }

    /**
     * Computes the local mean average of a pixels rectangular neighborhood. The rectangles size is specified by 
     * its half-width and half-height (radius).
     */
    public boolean meanBox(ClearCLBuffer arg1, ClearCLBuffer arg2, int arg3, int arg4, int arg5) {
        return Kernels.meanBox(clij, arg1, arg2, arg3, arg4, arg5);
    }

    /**
     * Multiplies all pairs of pixel values x and y from two image X and Y.
     * 
     * <pre>f(x, y) = x * y</pre>
     */
    public boolean multiplyImages(ClearCLImage arg1, ClearCLImage arg2, ClearCLImage arg3) {
        return Kernels.multiplyImages(clij, arg1, arg2, arg3);
    }

    /**
     * Multiplies all pairs of pixel values x and y from two image X and Y.
     * 
     * <pre>f(x, y) = x * y</pre>
     */
    public boolean multiplyImages(ClearCLBuffer arg1, ClearCLBuffer arg2, ClearCLBuffer arg3) {
        return Kernels.multiplyImages(clij, arg1, arg2, arg3);
    }

    /**
     * 
     */
    public boolean radialProjection(ClearCLBuffer arg1, ClearCLBuffer arg2, Float arg3) {
        return Kernels.radialProjection(clij, arg1, arg2, arg3);
    }

    /**
     * 
     */
    public boolean radialProjection(ClearCLImage arg1, ClearCLImage arg2, Float arg3) {
        return Kernels.radialProjection(clij, arg1, arg2, arg3);
    }

    /**
     * Flippes Y and Z axis of an image stack. This operation is similar to ImageJs 'Reslice [/]' method but
     * offers less flexibility such as interpolation.
     */
    public boolean resliceTop(ClearCLImage arg1, ClearCLImage arg2) {
        return Kernels.resliceTop(clij, arg1, arg2);
    }

    /**
     * Flippes Y and Z axis of an image stack. This operation is similar to ImageJs 'Reslice [/]' method but
     * offers less flexibility such as interpolation.
     */
    public boolean resliceTop(ClearCLBuffer arg1, ClearCLBuffer arg2) {
        return Kernels.resliceTop(clij, arg1, arg2);
    }

    /**
     * This method is deprecated. Consider using minimumBox or minimumSphere instead.
     */
    public boolean minimumIJ(ClearCLBuffer arg1, ClearCLBuffer arg2, Integer arg3) {
        return Kernels.minimumIJ(clij, arg1, arg2, arg3);
    }

    /**
     * This method is deprecated. Consider using minimumBox or minimumSphere instead.
     */
    public boolean minimumIJ(ClearCLImage arg1, ClearCLImage arg2, Integer arg3) {
        return Kernels.minimumIJ(clij, arg1, arg2, arg3);
    }

    /**
     * Determines the sum projection of an image along Z.
     */
    public boolean sumZProjection(ClearCLImage arg1, ClearCLImage arg2) {
        return Kernels.sumZProjection(clij, arg1, arg2);
    }

    /**
     * Determines the sum projection of an image along Z.
     */
    public boolean sumZProjection(ClearCLBuffer arg1, ClearCLBuffer arg2) {
        return Kernels.sumZProjection(clij, arg1, arg2);
    }

    /**
     * 
     */
    public boolean tenengradFusion(ClearCLImage arg1, float[] arg2, ClearCLImage[] arg3) {
        return Kernels.tenengradFusion(clij, arg1, arg2, arg3);
    }

    /**
     * 
     */
    public boolean tenengradFusion(ClearCLImage arg1, float[] arg2, float arg3, ClearCLImage[] arg4) {
        return Kernels.tenengradFusion(clij, arg1, arg2, arg3, arg4);
    }

    /**
     * Flippes Y and Z axis of an image stack. This operation is similar to ImageJs 'Reslice [/]' method but
     * offers less flexibility such as interpolation.
     */
    public boolean resliceBottom(ClearCLImage arg1, ClearCLImage arg2) {
        return Kernels.resliceBottom(clij, arg1, arg2);
    }

    /**
     * Flippes Y and Z axis of an image stack. This operation is similar to ImageJs 'Reslice [/]' method but
     * offers less flexibility such as interpolation.
     */
    public boolean resliceBottom(ClearCLBuffer arg1, ClearCLBuffer arg2) {
        return Kernels.resliceBottom(clij, arg1, arg2);
    }

    /**
     * Computes the local minimum of a pixels rectangular neighborhood. The rectangles size is specified by 
     * its half-width and half-height (radius).
     */
    public boolean minimumBox(ClearCLBuffer arg1, ClearCLBuffer arg2, int arg3, int arg4, int arg5) {
        return Kernels.minimumBox(clij, arg1, arg2, arg3, arg4, arg5);
    }

    /**
     * Computes the local minimum of a pixels rectangular neighborhood. The rectangles size is specified by 
     * its half-width and half-height (radius).
     */
    public boolean minimumBox(ClearCLImage arg1, ClearCLImage arg2, int arg3, int arg4, int arg5) {
        return Kernels.minimumBox(clij, arg1, arg2, arg3, arg4, arg5);
    }

    /**
     * Computes the minimum of a pair of pixel values x, y from two given images X and Y.
     * 
     * <pre>f(x, y) = min(x, y)</pre>
     */
    public boolean minimumImages(ClearCLImage arg1, ClearCLImage arg2, ClearCLImage arg3) {
        return Kernels.minimumImages(clij, arg1, arg2, arg3);
    }

    /**
     * Computes the minimum of a pair of pixel values x, y from two given images X and Y.
     * 
     * <pre>f(x, y) = min(x, y)</pre>
     */
    public boolean minimumImages(ClearCLBuffer arg1, ClearCLBuffer arg2, ClearCLBuffer arg3) {
        return Kernels.minimumImages(clij, arg1, arg2, arg3);
    }

    /**
     * Subtracts one image X from another image Y pixel wise.
     * 
     * <pre>f(x, y) = x - y</pre>
     */
    public boolean subtract(ClearCLImage arg1, ClearCLImage arg2, ClearCLImage arg3) {
        return Kernels.subtract(clij, arg1, arg2, arg3);
    }

    /**
     * Subtracts one image X from another image Y pixel wise.
     * 
     * <pre>f(x, y) = x - y</pre>
     */
    public boolean subtract(ClearCLBuffer arg1, ClearCLBuffer arg2, ClearCLBuffer arg3) {
        return Kernels.subtract(clij, arg1, arg2, arg3);
    }

    /**
     * Subtracts one image X from another image Y pixel wise.
     * 
     * <pre>f(x, y) = x - y</pre>
     */
    public boolean subtractImages(ClearCLImage arg1, ClearCLImage arg2, ClearCLImage arg3) {
        return Kernels.subtractImages(clij, arg1, arg2, arg3);
    }

    /**
     * Subtracts one image X from another image Y pixel wise.
     * 
     * <pre>f(x, y) = x - y</pre>
     */
    public boolean subtractImages(ClearCLBuffer arg1, ClearCLBuffer arg2, ClearCLBuffer arg3) {
        return Kernels.subtractImages(clij, arg1, arg2, arg3);
    }

    /**
     * Computes the local minimum of a pixels rectangular neighborhood. The rectangles size is specified by 
     * its half-width and half-height (radius).
     */
    public boolean minimumSphere(ClearCLImage arg1, ClearCLImage arg2, Integer arg3, Integer arg4) {
        return Kernels.minimumSphere(clij, arg1, arg2, arg3, arg4);
    }

    /**
     * Computes the local minimum of a pixels rectangular neighborhood. The rectangles size is specified by 
     * its half-width and half-height (radius).
     */
    public boolean minimumSphere(ClearCLBuffer arg1, ClearCLBuffer arg2, Integer arg3, Integer arg4, Integer arg5) {
        return Kernels.minimumSphere(clij, arg1, arg2, arg3, arg4, arg5);
    }

    /**
     * Computes the local minimum of a pixels rectangular neighborhood. The rectangles size is specified by 
     * its half-width and half-height (radius).
     */
    public boolean minimumSphere(ClearCLBuffer arg1, ClearCLBuffer arg2, Integer arg3, Integer arg4) {
        return Kernels.minimumSphere(clij, arg1, arg2, arg3, arg4);
    }

    /**
     * Computes the local minimum of a pixels rectangular neighborhood. The rectangles size is specified by 
     * its half-width and half-height (radius).
     */
    public boolean minimumSphere(ClearCLImage arg1, ClearCLImage arg2, Integer arg3, Integer arg4, Integer arg5) {
        return Kernels.minimumSphere(clij, arg1, arg2, arg3, arg4, arg5);
    }

    /**
     * Computes the local maximum of a pixels rectangular neighborhood. The rectangles size is specified by 
     * its half-width and half-height (radius).
     */
    public boolean maximumBox(ClearCLImage arg1, ClearCLImage arg2, int arg3, int arg4, int arg5) {
        return Kernels.maximumBox(clij, arg1, arg2, arg3, arg4, arg5);
    }

    /**
     * Computes the local maximum of a pixels rectangular neighborhood. The rectangles size is specified by 
     * its half-width and half-height (radius).
     */
    public boolean maximumBox(ClearCLBuffer arg1, ClearCLBuffer arg2, int arg3, int arg4, int arg5) {
        return Kernels.maximumBox(clij, arg1, arg2, arg3, arg4, arg5);
    }

    /**
     * Computes the local median of a pixels rectangular neighborhood. The rectangle is specified by 
     * its half-width and half-height (radius).
     * 
     * For technical reasons, the area of the rectangle must have less than 1000 pixels.
     */
    public boolean medianBox(ClearCLImage arg1, ClearCLImage arg2, Integer arg3, Integer arg4, Integer arg5) {
        return Kernels.medianBox(clij, arg1, arg2, arg3, arg4, arg5);
    }

    /**
     * Computes the local median of a pixels rectangular neighborhood. The rectangle is specified by 
     * its half-width and half-height (radius).
     * 
     * For technical reasons, the area of the rectangle must have less than 1000 pixels.
     */
    public boolean medianBox(ClearCLImage arg1, ClearCLImage arg2, Integer arg3, Integer arg4) {
        return Kernels.medianBox(clij, arg1, arg2, arg3, arg4);
    }

    /**
     * Computes the local median of a pixels rectangular neighborhood. The rectangle is specified by 
     * its half-width and half-height (radius).
     * 
     * For technical reasons, the area of the rectangle must have less than 1000 pixels.
     */
    public boolean medianBox(ClearCLBuffer arg1, ClearCLBuffer arg2, Integer arg3, Integer arg4, Integer arg5) {
        return Kernels.medianBox(clij, arg1, arg2, arg3, arg4, arg5);
    }

    /**
     * Computes the local median of a pixels rectangular neighborhood. The rectangle is specified by 
     * its half-width and half-height (radius).
     * 
     * For technical reasons, the area of the rectangle must have less than 1000 pixels.
     */
    public boolean medianBox(ClearCLBuffer arg1, ClearCLBuffer arg2, Integer arg3, Integer arg4) {
        return Kernels.medianBox(clij, arg1, arg2, arg3, arg4);
    }

    /**
     * Computes the local median of a pixels rectangular neighborhood. The rectangle is specified by 
     * its half-width and half-height (radius).
     * 
     * For technical reasons, the area of the rectangle must have less than 1000 pixels.
     */
    public boolean medianSphere(ClearCLBuffer arg1, ClearCLBuffer arg2, Integer arg3, Integer arg4, Integer arg5) {
        return Kernels.medianSphere(clij, arg1, arg2, arg3, arg4, arg5);
    }

    /**
     * Computes the local median of a pixels rectangular neighborhood. The rectangle is specified by 
     * its half-width and half-height (radius).
     * 
     * For technical reasons, the area of the rectangle must have less than 1000 pixels.
     */
    public boolean medianSphere(ClearCLImage arg1, ClearCLImage arg2, Integer arg3, Integer arg4) {
        return Kernels.medianSphere(clij, arg1, arg2, arg3, arg4);
    }

    /**
     * Computes the local median of a pixels rectangular neighborhood. The rectangle is specified by 
     * its half-width and half-height (radius).
     * 
     * For technical reasons, the area of the rectangle must have less than 1000 pixels.
     */
    public boolean medianSphere(ClearCLBuffer arg1, ClearCLBuffer arg2, Integer arg3, Integer arg4) {
        return Kernels.medianSphere(clij, arg1, arg2, arg3, arg4);
    }

    /**
     * Computes the local median of a pixels rectangular neighborhood. The rectangle is specified by 
     * its half-width and half-height (radius).
     * 
     * For technical reasons, the area of the rectangle must have less than 1000 pixels.
     */
    public boolean medianSphere(ClearCLImage arg1, ClearCLImage arg2, Integer arg3, Integer arg4, Integer arg5) {
        return Kernels.medianSphere(clij, arg1, arg2, arg3, arg4, arg5);
    }

    /**
     * Determines the sum of all pixels in a given image. It will be stored in a new row of ImageJs
     * Results table in the column 'Sum'.
     */
    public double sumPixels(ClearCLImage arg1) {
        return Kernels.sumPixels(clij, arg1);
    }

    /**
     * Determines the sum of all pixels in a given image. It will be stored in a new row of ImageJs
     * Results table in the column 'Sum'.
     */
    public double sumPixels(ClearCLBuffer arg1) {
        return Kernels.sumPixels(clij, arg1);
    }

    /**
     * Computes the local maximum of a pixels rectangular neighborhood. The rectangles size is specified by 
     * its half-width and half-height (radius).
     */
    public boolean maximumSphere(ClearCLImage arg1, ClearCLImage arg2, Integer arg3, Integer arg4) {
        return Kernels.maximumSphere(clij, arg1, arg2, arg3, arg4);
    }

    /**
     * Computes the local maximum of a pixels rectangular neighborhood. The rectangles size is specified by 
     * its half-width and half-height (radius).
     */
    public boolean maximumSphere(ClearCLBuffer arg1, ClearCLBuffer arg2, Integer arg3, Integer arg4) {
        return Kernels.maximumSphere(clij, arg1, arg2, arg3, arg4);
    }

    /**
     * Computes the local maximum of a pixels rectangular neighborhood. The rectangles size is specified by 
     * its half-width and half-height (radius).
     */
    public boolean maximumSphere(ClearCLImage arg1, ClearCLImage arg2, Integer arg3, Integer arg4, Integer arg5) {
        return Kernels.maximumSphere(clij, arg1, arg2, arg3, arg4, arg5);
    }

    /**
     * Computes the local maximum of a pixels rectangular neighborhood. The rectangles size is specified by 
     * its half-width and half-height (radius).
     */
    public boolean maximumSphere(ClearCLBuffer arg1, ClearCLBuffer arg2, Integer arg3, Integer arg4, Integer arg5) {
        return Kernels.maximumSphere(clij, arg1, arg2, arg3, arg4, arg5);
    }

    /**
     * Computes all pixels value x to the power of a given exponent a.
     * 
     * <pre>f(x, a) = x ^ a</pre>
     */
    public boolean power(ClearCLImage arg1, ClearCLImage arg2, Float arg3) {
        return Kernels.power(clij, arg1, arg2, arg3);
    }

    /**
     * Computes all pixels value x to the power of a given exponent a.
     * 
     * <pre>f(x, a) = x ^ a</pre>
     */
    public boolean power(ClearCLBuffer arg1, ClearCLBuffer arg2, Float arg3) {
        return Kernels.power(clij, arg1, arg2, arg3);
    }

    /**
     * This method is deprecated. Consider using maximumBox or maximumSphere instead.
     */
    public boolean maximumIJ(ClearCLBuffer arg1, ClearCLBuffer arg2, Integer arg3) {
        return Kernels.maximumIJ(clij, arg1, arg2, arg3);
    }

    /**
     * This method is deprecated. Consider using maximumBox or maximumSphere instead.
     */
    public boolean maximumIJ(ClearCLImage arg1, ClearCLImage arg2, Integer arg3) {
        return Kernels.maximumIJ(clij, arg1, arg2, arg3);
    }

    /**
     * 
     */
    public boolean splitStack(ClearCLImage arg1, ClearCLImage[] arg2) {
        return Kernels.splitStack(clij, arg1, arg2);
    }

    /**
     * 
     */
    public boolean splitStack(ClearCLBuffer arg1, ClearCLBuffer[] arg2) {
        return Kernels.splitStack(clij, arg1, arg2);
    }

    /**
     * Computes the maximum of a pair of pixel values x, y from two given images X and Y.
     * 
     * <pre>f(x, y) = max(x, y)</pre>
     */
    public boolean maximumImages(ClearCLImage arg1, ClearCLImage arg2, ClearCLImage arg3) {
        return Kernels.maximumImages(clij, arg1, arg2, arg3);
    }

    /**
     * Computes the maximum of a pair of pixel values x, y from two given images X and Y.
     * 
     * <pre>f(x, y) = max(x, y)</pre>
     */
    public boolean maximumImages(ClearCLBuffer arg1, ClearCLBuffer arg2, ClearCLBuffer arg3) {
        return Kernels.maximumImages(clij, arg1, arg2, arg3);
    }

    /**
     * Computes the local mean average of a pixels rectangular neighborhood. The rectangles size is specified by 
     * its half-width and half-height (radius).
     */
    public boolean meanSphere(ClearCLBuffer arg1, ClearCLBuffer arg2, Integer arg3, Integer arg4) {
        return Kernels.meanSphere(clij, arg1, arg2, arg3, arg4);
    }

    /**
     * Computes the local mean average of a pixels rectangular neighborhood. The rectangles size is specified by 
     * its half-width and half-height (radius).
     */
    public boolean meanSphere(ClearCLImage arg1, ClearCLImage arg2, Integer arg3, Integer arg4) {
        return Kernels.meanSphere(clij, arg1, arg2, arg3, arg4);
    }

    /**
     * Computes the local mean average of a pixels rectangular neighborhood. The rectangles size is specified by 
     * its half-width and half-height (radius).
     */
    public boolean meanSphere(ClearCLBuffer arg1, ClearCLBuffer arg2, Integer arg3, Integer arg4, Integer arg5) {
        return Kernels.meanSphere(clij, arg1, arg2, arg3, arg4, arg5);
    }

    /**
     * Computes the local mean average of a pixels rectangular neighborhood. The rectangles size is specified by 
     * its half-width and half-height (radius).
     */
    public boolean meanSphere(ClearCLImage arg1, ClearCLImage arg2, Integer arg3, Integer arg4, Integer arg5) {
        return Kernels.meanSphere(clij, arg1, arg2, arg3, arg4, arg5);
    }

    /**
     * This method is deprecated. Consider using meanBox or meanSphere instead.
     */
    public boolean meanIJ(ClearCLBuffer arg1, ClearCLBuffer arg2, Integer arg3) {
        return Kernels.meanIJ(clij, arg1, arg2, arg3);
    }

    /**
     * This method is deprecated. Consider using meanBox or meanSphere instead.
     */
    public boolean meanIJ(ClearCLImage arg1, ClearCLImage arg2, Integer arg3) {
        return Kernels.meanIJ(clij, arg1, arg2, arg3);
    }

    /**
     * Flippes X, Y and Z axis of an image stack. This operation is similar to ImageJs 'Reslice [/]' method 
     *  but offers less flexibility such as interpolation.
     */
    public boolean resliceLeft(ClearCLImage arg1, ClearCLImage arg2) {
        return Kernels.resliceLeft(clij, arg1, arg2);
    }

    /**
     * Flippes X, Y and Z axis of an image stack. This operation is similar to ImageJs 'Reslice [/]' method 
     *  but offers less flexibility such as interpolation.
     */
    public boolean resliceLeft(ClearCLBuffer arg1, ClearCLBuffer arg2) {
        return Kernels.resliceLeft(clij, arg1, arg2);
    }

    /**
     * Detects local minima in a given square/cubic neighborhood. Pixels in the resulting image are set to 1 if
     * there is no other pixel in a given radius which has a lower intensity, and to 0 otherwise.
     */
    public boolean detectMinimaBox(ClearCLBuffer arg1, ClearCLBuffer arg2, Integer arg3) {
        return Kernels.detectMinimaBox(clij, arg1, arg2, arg3);
    }

    /**
     * Detects local minima in a given square/cubic neighborhood. Pixels in the resulting image are set to 1 if
     * there is no other pixel in a given radius which has a lower intensity, and to 0 otherwise.
     */
    public boolean detectMinimaBox(ClearCLImage arg1, ClearCLImage arg2, Integer arg3) {
        return Kernels.detectMinimaBox(clij, arg1, arg2, arg3);
    }

    /**
     * Computes a binary image with pixel values 0 and 1 containing the binary dilation of a given input image.
     * The dilation takes the von-Neumann-neighborhood (4 pixels in 2D and 6 pixels in 3d) into account.
     * The pixels in the input image with pixel value not equal to 0 will be interpreted as 1.
     */
    public boolean dilateSphere(ClearCLBuffer arg1, ClearCLBuffer arg2) {
        return Kernels.dilateSphere(clij, arg1, arg2);
    }

    /**
     * Computes a binary image with pixel values 0 and 1 containing the binary dilation of a given input image.
     * The dilation takes the von-Neumann-neighborhood (4 pixels in 2D and 6 pixels in 3d) into account.
     * The pixels in the input image with pixel value not equal to 0 will be interpreted as 1.
     */
    public boolean dilateSphere(ClearCLImage arg1, ClearCLImage arg2) {
        return Kernels.dilateSphere(clij, arg1, arg2);
    }

    /**
     * Computes the gradient of gray values along Y. Assuming a, b and c are three adjacent
     *  pixels in Y direction. In the target image will be saved as: <pre>b' = c - a;</pre>
     */
    public boolean gradientY(ClearCLImage arg1, ClearCLImage arg2) {
        return Kernels.gradientY(clij, arg1, arg2);
    }

    /**
     * Computes the gradient of gray values along Y. Assuming a, b and c are three adjacent
     *  pixels in Y direction. In the target image will be saved as: <pre>b' = c - a;</pre>
     */
    public boolean gradientY(ClearCLBuffer arg1, ClearCLBuffer arg2) {
        return Kernels.gradientY(clij, arg1, arg2);
    }

    /**
     * 
     */
    public boolean detectOptima(ClearCLBuffer arg1, ClearCLBuffer arg2, Integer arg3, Boolean arg4) {
        return Kernels.detectOptima(clij, arg1, arg2, arg3, arg4);
    }

    /**
     * 
     */
    public boolean detectOptima(ClearCLImage arg1, ClearCLImage arg2, Integer arg3, Boolean arg4) {
        return Kernels.detectOptima(clij, arg1, arg2, arg3, arg4);
    }

    /**
     * Computes a binary image with pixel values 0 and 1 containing the binary erosion of a given input image.
     * The erosion takes the Moore-neighborhood (8 pixels in 2D and 26 pixels in 3d) into account.
     * The pixels in the input image with pixel value not equal to 0 will be interpreted as 1.
     * 
     * This method is comparable to the 'Erode' menu in ImageJ in case it is applied to a 2D image. The only
     * difference is that the output image contains values 0 and 1 instead of 0 and 255.
     */
    public boolean erodeBox(ClearCLImage arg1, ClearCLImage arg2) {
        return Kernels.erodeBox(clij, arg1, arg2);
    }

    /**
     * Computes a binary image with pixel values 0 and 1 containing the binary erosion of a given input image.
     * The erosion takes the Moore-neighborhood (8 pixels in 2D and 26 pixels in 3d) into account.
     * The pixels in the input image with pixel value not equal to 0 will be interpreted as 1.
     * 
     * This method is comparable to the 'Erode' menu in ImageJ in case it is applied to a 2D image. The only
     * difference is that the output image contains values 0 and 1 instead of 0 and 255.
     */
    public boolean erodeBox(ClearCLBuffer arg1, ClearCLBuffer arg2) {
        return Kernels.erodeBox(clij, arg1, arg2);
    }

    /**
     * Computes the Gaussian blurred image of an image given two sigma values in X and Y. Thus, the filterkernel can have non-isotropic shape.
     * 
     * The Gaussian blur is applied slice by slice in 2D.
     */
    public boolean blurSliceBySlice(ClearCLBuffer arg1, ClearCLBuffer arg2, int arg3, int arg4, float arg5, float arg6) {
        return Kernels.blurSliceBySlice(clij, arg1, arg2, arg3, arg4, arg5, arg6);
    }

    /**
     * Computes the Gaussian blurred image of an image given two sigma values in X and Y. Thus, the filterkernel can have non-isotropic shape.
     * 
     * The Gaussian blur is applied slice by slice in 2D.
     */
    public boolean blurSliceBySlice(ClearCLImage arg1, ClearCLImage arg2, Integer arg3, Integer arg4, Float arg5, Float arg6) {
        return Kernels.blurSliceBySlice(clij, arg1, arg2, arg3, arg4, arg5, arg6);
    }

    /**
     * Computes a binary image with pixel values 0 and 1 containing the binary erosion of a given input image.
     * The erosion takes the von-Neumann-neighborhood (4 pixels in 2D and 6 pixels in 3d) into account.
     * The pixels in the input image with pixel value not equal to 0 will be interpreted as 1.
     */
    public boolean erodeSphere(ClearCLBuffer arg1, ClearCLBuffer arg2) {
        return Kernels.erodeSphere(clij, arg1, arg2);
    }

    /**
     * Computes a binary image with pixel values 0 and 1 containing the binary erosion of a given input image.
     * The erosion takes the von-Neumann-neighborhood (4 pixels in 2D and 6 pixels in 3d) into account.
     * The pixels in the input image with pixel value not equal to 0 will be interpreted as 1.
     */
    public boolean erodeSphere(ClearCLImage arg1, ClearCLImage arg2) {
        return Kernels.erodeSphere(clij, arg1, arg2);
    }

    /**
     * Computes the gradient of gray values along Z. Assuming a, b and c are three adjacent
     *  pixels in Z direction. In the target image will be saved as: <pre>b' = c - a;</pre>
     */
    public boolean gradientZ(ClearCLBuffer arg1, ClearCLBuffer arg2) {
        return Kernels.gradientZ(clij, arg1, arg2);
    }

    /**
     * Computes the gradient of gray values along Z. Assuming a, b and c are three adjacent
     *  pixels in Z direction. In the target image will be saved as: <pre>b' = c - a;</pre>
     */
    public boolean gradientZ(ClearCLImage arg1, ClearCLImage arg2) {
        return Kernels.gradientZ(clij, arg1, arg2);
    }

    /**
     * Crops a given rectangle out of a given image.
     * 
     * Note: If the destination image pre-exists already, it will be overwritten and keep it's dimensions.
     */
    public boolean crop(ClearCLBuffer arg1, ClearCLBuffer arg2, Integer arg3, Integer arg4) {
        return Kernels.crop(clij, arg1, arg2, arg3, arg4);
    }

    /**
     * Crops a given rectangle out of a given image.
     * 
     * Note: If the destination image pre-exists already, it will be overwritten and keep it's dimensions.
     */
    public boolean crop(ClearCLImage arg1, ClearCLImage arg2, Integer arg3, Integer arg4, Integer arg5) {
        return Kernels.crop(clij, arg1, arg2, arg3, arg4, arg5);
    }

    /**
     * Crops a given rectangle out of a given image.
     * 
     * Note: If the destination image pre-exists already, it will be overwritten and keep it's dimensions.
     */
    public boolean crop(ClearCLBuffer arg1, ClearCLBuffer arg2, Integer arg3, Integer arg4, Integer arg5) {
        return Kernels.crop(clij, arg1, arg2, arg3, arg4, arg5);
    }

    /**
     * Crops a given rectangle out of a given image.
     * 
     * Note: If the destination image pre-exists already, it will be overwritten and keep it's dimensions.
     */
    public boolean crop(ClearCLImage arg1, ClearCLImage arg2, Integer arg3, Integer arg4) {
        return Kernels.crop(clij, arg1, arg2, arg3, arg4);
    }

    /**
     * Determines the center of mass of an image or image stack and writes the result in the results table
     * in the columns MassX, MassY and MassZ.
     */
    public double[] centerOfMass(ClearCLBuffer arg1) {
        return Kernels.centerOfMass(clij, arg1);
    }

    /**
     * Determines the center of mass of an image or image stack and writes the result in the results table
     * in the columns MassX, MassY and MassZ.
     */
    public double[] centerOfMass(ClearCLImage arg1) {
        return Kernels.centerOfMass(clij, arg1);
    }

    /**
     * Detects local maxima in a given square/cubic neighborhood. Pixels in the resulting image are set to 1 if
     * there is no other pixel in a given radius which has a higher intensity, and to 0 otherwise.
     */
    public boolean detectMaximaBox(ClearCLImage arg1, ClearCLImage arg2, Integer arg3) {
        return Kernels.detectMaximaBox(clij, arg1, arg2, arg3);
    }

    /**
     * Detects local maxima in a given square/cubic neighborhood. Pixels in the resulting image are set to 1 if
     * there is no other pixel in a given radius which has a higher intensity, and to 0 otherwise.
     */
    public boolean detectMaximaBox(ClearCLBuffer arg1, ClearCLBuffer arg2, Integer arg3) {
        return Kernels.detectMaximaBox(clij, arg1, arg2, arg3);
    }

    /**
     * Divides two images X and Y by each other pixel wise.
     * 
     * <pre>f(x, y) = x / y</pre>
     */
    public boolean divideImages(ClearCLImage arg1, ClearCLImage arg2, ClearCLImage arg3) {
        return Kernels.divideImages(clij, arg1, arg2, arg3);
    }

    /**
     * Divides two images X and Y by each other pixel wise.
     * 
     * <pre>f(x, y) = x / y</pre>
     */
    public boolean divideImages(ClearCLBuffer arg1, ClearCLBuffer arg2, ClearCLBuffer arg3) {
        return Kernels.divideImages(clij, arg1, arg2, arg3);
    }

    /**
     * This method has two purposes: 
     * It copies a 2D image to a given slice z position in a 3D image stack or 
     * It copies a given slice at position z in an image stack to a 2D image.
     * 
     * The first case is only available via ImageJ macro. If you are using it, it is recommended that the 
     * target 3D image already pre-exists in GPU memory before calling this method. Otherwise, CLIJ create 
     * the image stack with z planes.
     */
    public boolean copySlice(ClearCLImage arg1, ClearCLImage arg2, Integer arg3) {
        return Kernels.copySlice(clij, arg1, arg2, arg3);
    }

    /**
     * This method has two purposes: 
     * It copies a 2D image to a given slice z position in a 3D image stack or 
     * It copies a given slice at position z in an image stack to a 2D image.
     * 
     * The first case is only available via ImageJ macro. If you are using it, it is recommended that the 
     * target 3D image already pre-exists in GPU memory before calling this method. Otherwise, CLIJ create 
     * the image stack with z planes.
     */
    public boolean copySlice(ClearCLBuffer arg1, ClearCLBuffer arg2, Integer arg3) {
        return Kernels.copySlice(clij, arg1, arg2, arg3);
    }

    /**
     * Scales an image using given scaling factors for X and Y dimensions. The nearest-neighbor method
     * is applied. In ImageJ the method which is similar is called 'Interpolation method: none'.
     */
    public boolean downsample(ClearCLBuffer arg1, ClearCLBuffer arg2, Float arg3, Float arg4) {
        return Kernels.downsample(clij, arg1, arg2, arg3, arg4);
    }

    /**
     * Scales an image using given scaling factors for X and Y dimensions. The nearest-neighbor method
     * is applied. In ImageJ the method which is similar is called 'Interpolation method: none'.
     */
    public boolean downsample(ClearCLImage arg1, ClearCLImage arg2, Float arg3, Float arg4, Float arg5) {
        return Kernels.downsample(clij, arg1, arg2, arg3, arg4, arg5);
    }

    /**
     * Scales an image using given scaling factors for X and Y dimensions. The nearest-neighbor method
     * is applied. In ImageJ the method which is similar is called 'Interpolation method: none'.
     */
    public boolean downsample(ClearCLBuffer arg1, ClearCLBuffer arg2, Float arg3, Float arg4, Float arg5) {
        return Kernels.downsample(clij, arg1, arg2, arg3, arg4, arg5);
    }

    /**
     * Scales an image using given scaling factors for X and Y dimensions. The nearest-neighbor method
     * is applied. In ImageJ the method which is similar is called 'Interpolation method: none'.
     */
    public boolean downsample(ClearCLImage arg1, ClearCLImage arg2, Float arg3, Float arg4) {
        return Kernels.downsample(clij, arg1, arg2, arg3, arg4);
    }

    /**
     * Computes the negative value of all pixels in a given image. It is recommended to convert images to 
     * 32-bit float before applying this operation.
     * 
     * <pre>f(x) = - x</pre>
     * 
     * For binary images, use binaryNot.
     */
    public boolean invert(ClearCLBuffer arg1, ClearCLBuffer arg2) {
        return Kernels.invert(clij, arg1, arg2);
    }

    /**
     * Computes the negative value of all pixels in a given image. It is recommended to convert images to 
     * 32-bit float before applying this operation.
     * 
     * <pre>f(x) = - x</pre>
     * 
     * For binary images, use binaryNot.
     */
    public boolean invert(ClearCLImage arg1, ClearCLImage arg2) {
        return Kernels.invert(clij, arg1, arg2);
    }

    /**
     * 
     */
    public boolean fillHistogram(ClearCLBuffer arg1, ClearCLBuffer arg2, Float arg3, Float arg4) {
        return Kernels.fillHistogram(clij, arg1, arg2, arg3, arg4);
    }

    /**
     * Computes the gradient of gray values along X. Assuming a, b and c are three adjacent
     *  pixels in X direction. In the target image will be saved as: <pre>b' = c - a;</pre>
     */
    public boolean gradientX(ClearCLBuffer arg1, ClearCLBuffer arg2) {
        return Kernels.gradientX(clij, arg1, arg2);
    }

    /**
     * Computes the gradient of gray values along X. Assuming a, b and c are three adjacent
     *  pixels in X direction. In the target image will be saved as: <pre>b' = c - a;</pre>
     */
    public boolean gradientX(ClearCLImage arg1, ClearCLImage arg2) {
        return Kernels.gradientX(clij, arg1, arg2);
    }

    /**
     * Computes a binary image with pixel values 0 and 1 containing the binary dilation of a given input image.
     * The dilation takes the Moore-neighborhood (8 pixels in 2D and 26 pixels in 3d) into account.
     * The pixels in the input image with pixel value not equal to 0 will be interpreted as 1.
     * 
     * This method is comparable to the 'Dilate' menu in ImageJ in case it is applied to a 2D image. The only
     * difference is that the output image contains values 0 and 1 instead of 0 and 255.
     */
    public boolean dilateBox(ClearCLImage arg1, ClearCLImage arg2) {
        return Kernels.dilateBox(clij, arg1, arg2);
    }

    /**
     * Computes a binary image with pixel values 0 and 1 containing the binary dilation of a given input image.
     * The dilation takes the Moore-neighborhood (8 pixels in 2D and 26 pixels in 3d) into account.
     * The pixels in the input image with pixel value not equal to 0 will be interpreted as 1.
     * 
     * This method is comparable to the 'Dilate' menu in ImageJ in case it is applied to a 2D image. The only
     * difference is that the output image contains values 0 and 1 instead of 0 and 255.
     */
    public boolean dilateBox(ClearCLBuffer arg1, ClearCLBuffer arg2) {
        return Kernels.dilateBox(clij, arg1, arg2);
    }

    /**
     * Calculates the sum of pairs of pixels x and y of two images X and Y.
     * 
     * <pre>f(x, y) = x + y</pre>
     */
    public boolean addImages(ClearCLImage arg1, ClearCLImage arg2, ClearCLImage arg3) {
        return Kernels.addImages(clij, arg1, arg2, arg3);
    }

    /**
     * Calculates the sum of pairs of pixels x and y of two images X and Y.
     * 
     * <pre>f(x, y) = x + y</pre>
     */
    public boolean addImages(ClearCLBuffer arg1, ClearCLBuffer arg2, ClearCLBuffer arg3) {
        return Kernels.addImages(clij, arg1, arg2, arg3);
    }

    /**
     * Computes the absolute value of every individual pixel x in a given image.
     * 
     * <pre>f(x) = |x| </pre>
     */
    public boolean absolute(ClearCLBuffer arg1, ClearCLBuffer arg2) {
        return Kernels.absolute(clij, arg1, arg2);
    }

    /**
     * Computes the absolute value of every individual pixel x in a given image.
     * 
     * <pre>f(x) = |x| </pre>
     */
    public boolean absolute(ClearCLImage arg1, ClearCLImage arg2) {
        return Kernels.absolute(clij, arg1, arg2);
    }

    /**
     * CLIJ affineTransform is deprecated. Use affineTransform2D or affineTransform3D instead.
     * 
     * Applies an affine transform to a 3D image. Individual transforms must be separated by spaces.
     * 
     * Supported transforms:
     * * center: translate the coordinate origin to the center of the image
     * * -center: translate the coordinate origin back to the initial origin
     * * rotate=[angle]: rotate in X/Y plane (around Z-axis) by the given angle in degrees
     * * rotateX=[angle]: rotate in Y/Z plane (around X-axis) by the given angle in degrees
     * * rotateY=[angle]: rotate in X/Z plane (around Y-axis) by the given angle in degrees
     * * rotateZ=[angle]: rotate in X/Y plane (around Z-axis) by the given angle in degrees
     * * scale=[factor]: isotropic scaling according to given zoom factor
     * * scaleX=[factor]: scaling along X-axis according to given zoom factor
     * * scaleY=[factor]: scaling along Y-axis according to given zoom factor
     * * scaleZ=[factor]: scaling along Z-axis according to given zoom factor
     * * shearXY=[factor]: shearing along X-axis in XY plane according to given factor
     * * shearXZ=[factor]: shearing along X-axis in XZ plane according to given factor
     * * shearYX=[factor]: shearing along Y-axis in XY plane according to given factor
     * * shearYZ=[factor]: shearing along Y-axis in YZ plane according to given factor
     * * shearZX=[factor]: shearing along Z-axis in XZ plane according to given factor
     * * shearZY=[factor]: shearing along Z-axis in YZ plane according to given factor
     * * translateX=[distance]: translate along X-axis by distance given in pixels
     * * translateY=[distance]: translate along X-axis by distance given in pixels
     * * translateZ=[distance]: translate along X-axis by distance given in pixels
     * 
     * Example transform:
     * transform = "center scale=2 rotate=45 -center";
     */
    public boolean affineTransform(ClearCLImage arg1, ClearCLImage arg2, AffineTransform3D arg3) {
        return Kernels.affineTransform(clij, arg1, arg2, arg3);
    }

    /**
     * CLIJ affineTransform is deprecated. Use affineTransform2D or affineTransform3D instead.
     * 
     * Applies an affine transform to a 3D image. Individual transforms must be separated by spaces.
     * 
     * Supported transforms:
     * * center: translate the coordinate origin to the center of the image
     * * -center: translate the coordinate origin back to the initial origin
     * * rotate=[angle]: rotate in X/Y plane (around Z-axis) by the given angle in degrees
     * * rotateX=[angle]: rotate in Y/Z plane (around X-axis) by the given angle in degrees
     * * rotateY=[angle]: rotate in X/Z plane (around Y-axis) by the given angle in degrees
     * * rotateZ=[angle]: rotate in X/Y plane (around Z-axis) by the given angle in degrees
     * * scale=[factor]: isotropic scaling according to given zoom factor
     * * scaleX=[factor]: scaling along X-axis according to given zoom factor
     * * scaleY=[factor]: scaling along Y-axis according to given zoom factor
     * * scaleZ=[factor]: scaling along Z-axis according to given zoom factor
     * * shearXY=[factor]: shearing along X-axis in XY plane according to given factor
     * * shearXZ=[factor]: shearing along X-axis in XZ plane according to given factor
     * * shearYX=[factor]: shearing along Y-axis in XY plane according to given factor
     * * shearYZ=[factor]: shearing along Y-axis in YZ plane according to given factor
     * * shearZX=[factor]: shearing along Z-axis in XZ plane according to given factor
     * * shearZY=[factor]: shearing along Z-axis in YZ plane according to given factor
     * * translateX=[distance]: translate along X-axis by distance given in pixels
     * * translateY=[distance]: translate along X-axis by distance given in pixels
     * * translateZ=[distance]: translate along X-axis by distance given in pixels
     * 
     * Example transform:
     * transform = "center scale=2 rotate=45 -center";
     */
    public boolean affineTransform(ClearCLImage arg1, ClearCLImage arg2, float[] arg3) {
        return Kernels.affineTransform(clij, arg1, arg2, arg3);
    }

    /**
     * CLIJ affineTransform is deprecated. Use affineTransform2D or affineTransform3D instead.
     * 
     * Applies an affine transform to a 3D image. Individual transforms must be separated by spaces.
     * 
     * Supported transforms:
     * * center: translate the coordinate origin to the center of the image
     * * -center: translate the coordinate origin back to the initial origin
     * * rotate=[angle]: rotate in X/Y plane (around Z-axis) by the given angle in degrees
     * * rotateX=[angle]: rotate in Y/Z plane (around X-axis) by the given angle in degrees
     * * rotateY=[angle]: rotate in X/Z plane (around Y-axis) by the given angle in degrees
     * * rotateZ=[angle]: rotate in X/Y plane (around Z-axis) by the given angle in degrees
     * * scale=[factor]: isotropic scaling according to given zoom factor
     * * scaleX=[factor]: scaling along X-axis according to given zoom factor
     * * scaleY=[factor]: scaling along Y-axis according to given zoom factor
     * * scaleZ=[factor]: scaling along Z-axis according to given zoom factor
     * * shearXY=[factor]: shearing along X-axis in XY plane according to given factor
     * * shearXZ=[factor]: shearing along X-axis in XZ plane according to given factor
     * * shearYX=[factor]: shearing along Y-axis in XY plane according to given factor
     * * shearYZ=[factor]: shearing along Y-axis in YZ plane according to given factor
     * * shearZX=[factor]: shearing along Z-axis in XZ plane according to given factor
     * * shearZY=[factor]: shearing along Z-axis in YZ plane according to given factor
     * * translateX=[distance]: translate along X-axis by distance given in pixels
     * * translateY=[distance]: translate along X-axis by distance given in pixels
     * * translateZ=[distance]: translate along X-axis by distance given in pixels
     * 
     * Example transform:
     * transform = "center scale=2 rotate=45 -center";
     */
    public boolean affineTransform(ClearCLBuffer arg1, ClearCLBuffer arg2, AffineTransform3D arg3) {
        return Kernels.affineTransform(clij, arg1, arg2, arg3);
    }

    /**
     * CLIJ affineTransform is deprecated. Use affineTransform2D or affineTransform3D instead.
     * 
     * Applies an affine transform to a 3D image. Individual transforms must be separated by spaces.
     * 
     * Supported transforms:
     * * center: translate the coordinate origin to the center of the image
     * * -center: translate the coordinate origin back to the initial origin
     * * rotate=[angle]: rotate in X/Y plane (around Z-axis) by the given angle in degrees
     * * rotateX=[angle]: rotate in Y/Z plane (around X-axis) by the given angle in degrees
     * * rotateY=[angle]: rotate in X/Z plane (around Y-axis) by the given angle in degrees
     * * rotateZ=[angle]: rotate in X/Y plane (around Z-axis) by the given angle in degrees
     * * scale=[factor]: isotropic scaling according to given zoom factor
     * * scaleX=[factor]: scaling along X-axis according to given zoom factor
     * * scaleY=[factor]: scaling along Y-axis according to given zoom factor
     * * scaleZ=[factor]: scaling along Z-axis according to given zoom factor
     * * shearXY=[factor]: shearing along X-axis in XY plane according to given factor
     * * shearXZ=[factor]: shearing along X-axis in XZ plane according to given factor
     * * shearYX=[factor]: shearing along Y-axis in XY plane according to given factor
     * * shearYZ=[factor]: shearing along Y-axis in YZ plane according to given factor
     * * shearZX=[factor]: shearing along Z-axis in XZ plane according to given factor
     * * shearZY=[factor]: shearing along Z-axis in YZ plane according to given factor
     * * translateX=[distance]: translate along X-axis by distance given in pixels
     * * translateY=[distance]: translate along X-axis by distance given in pixels
     * * translateZ=[distance]: translate along X-axis by distance given in pixels
     * 
     * Example transform:
     * transform = "center scale=2 rotate=45 -center";
     */
    public boolean affineTransform(ClearCLBuffer arg1, ClearCLBuffer arg2, float[] arg3) {
        return Kernels.affineTransform(clij, arg1, arg2, arg3);
    }

    /**
     * Determines the histogram of a given image.
     */
    public float[] histogram(ClearCLBuffer arg1, Float arg2, Float arg3, Integer arg4) {
        return Kernels.histogram(clij, arg1, arg2, arg3, arg4);
    }

    /**
     * Computes a binary image (containing pixel values 0 and 1) from two images X and Y by connecting pairs of
     * pixels x and y with the binary AND operator &.
     * All pixel values except 0 in the input images are interpreted as 1.
     * 
     * <pre>f(x, y) = x & y</pre>
     */
    public boolean binaryAnd(ClearCLImage arg1, ClearCLImage arg2, ClearCLImage arg3) {
        return Kernels.binaryAnd(clij, arg1, arg2, arg3);
    }

    /**
     * Computes a binary image (containing pixel values 0 and 1) from two images X and Y by connecting pairs of
     * pixels x and y with the binary AND operator &.
     * All pixel values except 0 in the input images are interpreted as 1.
     * 
     * <pre>f(x, y) = x & y</pre>
     */
    public boolean binaryAnd(ClearCLBuffer arg1, ClearCLBuffer arg2, ClearCLBuffer arg3) {
        return Kernels.binaryAnd(clij, arg1, arg2, arg3);
    }

    /**
     * Computes a binary image (containing pixel values 0 and 1) from two images X and Y by connecting pairs of
     * pixels x and y with the binary operators AND &, OR | and NOT ! implementing the XOR operator.
     * All pixel values except 0 in the input images are interpreted as 1.
     * 
     * <pre>f(x, y) = (x & !y) | (!x & y)</pre>
     */
    public boolean binaryXOr(ClearCLBuffer arg1, ClearCLBuffer arg2, ClearCLBuffer arg3) {
        return Kernels.binaryXOr(clij, arg1, arg2, arg3);
    }

    /**
     * Computes a binary image (containing pixel values 0 and 1) from two images X and Y by connecting pairs of
     * pixels x and y with the binary operators AND &, OR | and NOT ! implementing the XOR operator.
     * All pixel values except 0 in the input images are interpreted as 1.
     * 
     * <pre>f(x, y) = (x & !y) | (!x & y)</pre>
     */
    public boolean binaryXOr(ClearCLImage arg1, ClearCLImage arg2, ClearCLImage arg3) {
        return Kernels.binaryXOr(clij, arg1, arg2, arg3);
    }

    /**
     * Computes a binary image (containing pixel values 0 and 1) from an image X by negating its pixel values
     * x using the binary NOT operator !
     * All pixel values except 0 in the input image are interpreted as 1.
     * 
     * <pre>f(x) = !x</pre>
     */
    public boolean binaryNot(ClearCLImage arg1, ClearCLImage arg2) {
        return Kernels.binaryNot(clij, arg1, arg2);
    }

    /**
     * Computes a binary image (containing pixel values 0 and 1) from an image X by negating its pixel values
     * x using the binary NOT operator !
     * All pixel values except 0 in the input image are interpreted as 1.
     * 
     * <pre>f(x) = !x</pre>
     */
    public boolean binaryNot(ClearCLBuffer arg1, ClearCLBuffer arg2) {
        return Kernels.binaryNot(clij, arg1, arg2);
    }

    /**
     * Computes a binary image (containing pixel values 0 and 1) from two images X and Y by connecting pairs of
     * pixels x and y with the binary OR operator |.
     * All pixel values except 0 in the input images are interpreted as 1.<pre>f(x, y) = x | y</pre>
     */
    public boolean binaryOr(ClearCLImage arg1, ClearCLImage arg2, ClearCLImage arg3) {
        return Kernels.binaryOr(clij, arg1, arg2, arg3);
    }

    /**
     * Computes a binary image (containing pixel values 0 and 1) from two images X and Y by connecting pairs of
     * pixels x and y with the binary OR operator |.
     * All pixel values except 0 in the input images are interpreted as 1.<pre>f(x, y) = x | y</pre>
     */
    public boolean binaryOr(ClearCLBuffer arg1, ClearCLBuffer arg2, ClearCLBuffer arg3) {
        return Kernels.binaryOr(clij, arg1, arg2, arg3);
    }

    /**
     * Computes the Gaussian blurred image of an image given two sigma values in X and Y. Thus, the filterkernel can have non-isotropic shape.
     * 
     * The implementation is done separable. In case a sigma equals zero, the direction is not blurred.
     */
    public boolean blur(ClearCLImage arg1, ClearCLBuffer arg2, Float arg3, Float arg4) {
        return Kernels.blur(clij, arg1, arg2, arg3, arg4);
    }

    /**
     * Computes the Gaussian blurred image of an image given two sigma values in X and Y. Thus, the filterkernel can have non-isotropic shape.
     * 
     * The implementation is done separable. In case a sigma equals zero, the direction is not blurred.
     */
    public boolean blur(ClearCLImage arg1, ClearCLImage arg2, Float arg3, Float arg4, Float arg5) {
        return Kernels.blur(clij, arg1, arg2, arg3, arg4, arg5);
    }

    /**
     * Computes the Gaussian blurred image of an image given two sigma values in X and Y. Thus, the filterkernel can have non-isotropic shape.
     * 
     * The implementation is done separable. In case a sigma equals zero, the direction is not blurred.
     */
    public boolean blur(ClearCLImage arg1, ClearCLBuffer arg2, Float arg3, Float arg4, Float arg5) {
        return Kernels.blur(clij, arg1, arg2, arg3, arg4, arg5);
    }

    /**
     * Computes the Gaussian blurred image of an image given two sigma values in X and Y. Thus, the filterkernel can have non-isotropic shape.
     * 
     * The implementation is done separable. In case a sigma equals zero, the direction is not blurred.
     */
    public boolean blur(ClearCLBuffer arg1, ClearCLBuffer arg2, Float arg3, Float arg4, Float arg5) {
        return Kernels.blur(clij, arg1, arg2, arg3, arg4, arg5);
    }

    /**
     * Computes the Gaussian blurred image of an image given two sigma values in X and Y. Thus, the filterkernel can have non-isotropic shape.
     * 
     * The implementation is done separable. In case a sigma equals zero, the direction is not blurred.
     */
    public boolean blur(ClearCLImage arg1, ClearCLImage arg2, Float arg3, Float arg4) {
        return Kernels.blur(clij, arg1, arg2, arg3, arg4);
    }

    /**
     * Computes the Gaussian blurred image of an image given two sigma values in X and Y. Thus, the filterkernel can have non-isotropic shape.
     * 
     * The implementation is done separable. In case a sigma equals zero, the direction is not blurred.
     */
    public boolean blur(ClearCLBuffer arg1, ClearCLBuffer arg2, Float arg3, Float arg4) {
        return Kernels.blur(clij, arg1, arg2, arg3, arg4);
    }

    /**
     * Deforms an image according to distances provided in the given vector images. It is recommended to use 32-bit images for input, output and vector images. 
     */
    public boolean applyVectorfield(ClearCLBuffer arg1, ClearCLBuffer arg2, ClearCLBuffer arg3, ClearCLBuffer arg4, ClearCLBuffer arg5) {
        return Kernels.applyVectorfield(clij, arg1, arg2, arg3, arg4, arg5);
    }

    /**
     * Deforms an image according to distances provided in the given vector images. It is recommended to use 32-bit images for input, output and vector images. 
     */
    public boolean applyVectorfield(ClearCLImage arg1, ClearCLImage arg2, ClearCLImage arg3, ClearCLImage arg4) {
        return Kernels.applyVectorfield(clij, arg1, arg2, arg3, arg4);
    }

    /**
     * Deforms an image according to distances provided in the given vector images. It is recommended to use 32-bit images for input, output and vector images. 
     */
    public boolean applyVectorfield(ClearCLBuffer arg1, ClearCLBuffer arg2, ClearCLBuffer arg3, ClearCLBuffer arg4) {
        return Kernels.applyVectorfield(clij, arg1, arg2, arg3, arg4);
    }

    /**
     * Deforms an image according to distances provided in the given vector images. It is recommended to use 32-bit images for input, output and vector images. 
     */
    public boolean applyVectorfield(ClearCLImage arg1, ClearCLImage arg2, ClearCLImage arg3, ClearCLImage arg4, ClearCLImage arg5) {
        return Kernels.applyVectorfield(clij, arg1, arg2, arg3, arg4, arg5);
    }


    // net.haesleinhuepf.clij.advancedfilters.BinaryUnion
    //----------------------------------------------------
    /**
     * Computes a binary image (containing pixel values 0 and 1) from two images X and Y by connecting pairs of
     * pixels x and y with the binary union operator |.
     * All pixel values except 0 in the input images are interpreted as 1.<pre>f(x, y) = x | y</pre>
     */
    public boolean binaryUnion(ClearCLBuffer arg1, ClearCLBuffer arg2, ClearCLBuffer arg3) {
        return BinaryUnion.binaryUnion(clij, arg1, arg2, arg3);
    }


    // net.haesleinhuepf.clij.advancedfilters.BinaryIntersection
    //----------------------------------------------------
    /**
     * Computes a binary image (containing pixel values 0 and 1) from two images X and Y by connecting pairs of
     * pixels x and y with the binary intersection operator &.
     * All pixel values except 0 in the input images are interpreted as 1.
     * 
     * <pre>f(x, y) = x & y</pre>
     */
    public boolean binaryIntersection(ClearCLBuffer arg1, ClearCLBuffer arg2, ClearCLBuffer arg3) {
        return BinaryIntersection.binaryIntersection(clij, arg1, arg2, arg3);
    }


    // net.haesleinhuepf.clij.advancedfilters.ConnectedComponentsLabeling
    //----------------------------------------------------
    /**
     * 
     */
    public boolean replace(ClearCLBuffer arg1, ClearCLBuffer arg2, ClearCLBuffer arg3) {
        return ConnectedComponentsLabeling.replace(clij, arg1, arg2, arg3);
    }

    /**
     * 
     */
    public boolean replace(ClearCLBuffer arg1, ClearCLBuffer arg2, Float arg3, Float arg4) {
        return ConnectedComponentsLabeling.replace(clij, arg1, arg2, arg3, arg4);
    }

    /**
     * 
     */
    public boolean shiftIntensitiesToCloseGaps(ClearCLBuffer arg1, ClearCLBuffer arg2) {
        return ConnectedComponentsLabeling.shiftIntensitiesToCloseGaps(clij, arg1, arg2);
    }

    /**
     * 
     */
    public boolean nonzeroMinimumBox(ClearCLBuffer arg1, ClearCLBuffer arg2, ClearCLBuffer arg3, int arg4, int arg5, int arg6) {
        return ConnectedComponentsLabeling.nonzeroMinimumBox(clij, arg1, arg2, arg3, arg4, arg5, arg6);
    }

    /**
     * 
     */
    public boolean setNonZeroPixelsToPixelIndex(ClearCLBuffer arg1, ClearCLBuffer arg2) {
        return ConnectedComponentsLabeling.setNonZeroPixelsToPixelIndex(clij, arg1, arg2);
    }

    /**
     * Performs connected components analysis to a binary image and generates a label map.
     */
    public boolean connectedComponentsLabeling(ClearCLBuffer arg1, ClearCLBuffer arg2) {
        return ConnectedComponentsLabeling.connectedComponentsLabeling(clij, arg1, arg2);
    }


    // net.haesleinhuepf.clij.advancedfilters.CountNonZeroPixels
    //----------------------------------------------------
    /**
     * Determines the number of all pixels in a given image which are not equal to 0. It will be stored in a new row of ImageJs
     * Results table in the column 'Count_non_zero'.
     */
    public double countNonZeroPixels(ClearCLBuffer arg1) {
        return CountNonZeroPixels.countNonZeroPixels(clij, arg1);
    }


    // net.haesleinhuepf.clij.advancedfilters.CrossCorrelation
    //----------------------------------------------------
    /**
     * 
     */
    public boolean crossCorrelation(ClearCLImage arg1, ClearCLImage arg2, ClearCLImage arg3, ClearCLImage arg4, ClearCLImage arg5, int arg6, int arg7, int arg8) {
        return CrossCorrelation.crossCorrelation(clij, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8);
    }

    /**
     * 
     */
    public boolean crossCorrelation(ClearCLBuffer arg1, ClearCLBuffer arg2, ClearCLBuffer arg3, ClearCLBuffer arg4, ClearCLBuffer arg5, int arg6, int arg7, int arg8) {
        return CrossCorrelation.crossCorrelation(clij, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8);
    }


    // net.haesleinhuepf.clij.advancedfilters.DifferenceOfGaussian2D
    //----------------------------------------------------
    /**
     * Applies Gaussian blur to the input image twice resulting in two images which are then subtracted from each other.
     */
    public boolean differenceOfGaussian(ClearCLBuffer arg1, ClearCLBuffer arg2, Float arg3, Float arg4, Float arg5, Float arg6) {
        return DifferenceOfGaussian2D.differenceOfGaussian(clij, arg1, arg2, arg3, arg4, arg5, arg6);
    }


    // net.haesleinhuepf.clij.advancedfilters.DifferenceOfGaussian3D
    //----------------------------------------------------
    /**
     * Applies Gaussian blur to the input image twice resulting in two images which are then subtracted from each other.
     */
    public boolean differenceOfGaussian(ClearCLBuffer arg1, ClearCLBuffer arg2, Float arg3, Float arg4, Float arg5, Float arg6, Float arg7, Float arg8) {
        return DifferenceOfGaussian3D.differenceOfGaussian(clij, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8);
    }


    // net.haesleinhuepf.clij.advancedfilters.Extrema
    //----------------------------------------------------
    /**
     * Returns an image with pixel values most distant from 0: f(x, y) = x if abs(x) > abs(y), y else.
     */
    public boolean extrema(ClearCLBuffer arg1, ClearCLBuffer arg2, ClearCLBuffer arg3) {
        return Extrema.extrema(clij, arg1, arg2, arg3);
    }


    // net.haesleinhuepf.clij.advancedfilters.LocalExtremaBox
    //----------------------------------------------------
    /**
     * 
     */
    public boolean localExtrema(ClearCLBuffer arg1, ClearCLBuffer arg2, Integer arg3, Integer arg4, Integer arg5) {
        return LocalExtremaBox.localExtrema(clij, arg1, arg2, arg3, arg4, arg5);
    }


    // net.haesleinhuepf.clij.advancedfilters.LocalID
    //----------------------------------------------------
    /**
     * local id
     */
    public boolean localID(ClearCLBuffer arg1, ClearCLBuffer arg2) {
        return LocalID.localID(clij, arg1, arg2);
    }


    // net.haesleinhuepf.clij.advancedfilters.MaskLabel
    //----------------------------------------------------
    /**
     * Computes a masked image by applying a label mask to an image. All pixel values x of image X will be copied
     * to the destination image in case pixel value m at the same position in the label_map image has the right index value i.
     * 
     * f(x,m,i) = (x if (m == i); (0 otherwise))
     */
    public boolean maskLabel(ClearCLBuffer arg1, ClearCLBuffer arg2, ClearCLBuffer arg3, Float arg4) {
        return MaskLabel.maskLabel(clij, arg1, arg2, arg3, arg4);
    }


    // net.haesleinhuepf.clij.advancedfilters.MeanClosestSpotDistance
    //----------------------------------------------------
    /**
     * 
     */
    public double meanClosestSpotDistances(ClearCLBuffer arg1, ClearCLBuffer arg2) {
        return MeanClosestSpotDistance.meanClosestSpotDistances(clij, arg1, arg2);
    }

    /**
     * 
     */
    public double[] meanClosestSpotDistances(ClearCLBuffer arg1, ClearCLBuffer arg2, Boolean arg3) {
        return MeanClosestSpotDistance.meanClosestSpotDistances(clij, arg1, arg2, arg3);
    }


    // net.haesleinhuepf.clij.advancedfilters.MeanSquaredError
    //----------------------------------------------------
    /**
     * Determines the mean squared error (MSE) between two images. The MSE will be stored in a new row of ImageJs
     * Results table in the column 'MSE'.
     */
    public double meanSquaredError(ClearCLBuffer arg1, ClearCLBuffer arg2) {
        return MeanSquaredError.meanSquaredError(clij, arg1, arg2);
    }


    // net.haesleinhuepf.clij.advancedfilters.MedianZProjection
    //----------------------------------------------------

    // net.haesleinhuepf.clij.advancedfilters.NonzeroMinimum3DDiamond
    //----------------------------------------------------
    /**
     * Apply a minimum-sphere filter to the input image. The radius is fixed to 1.
     */
    public boolean nonzeroMinimum3DDiamond(ClearCLBuffer arg1, ClearCLBuffer arg2, ClearCLBuffer arg3) {
        return NonzeroMinimum3DDiamond.nonzeroMinimum3DDiamond(clij, arg1, arg2, arg3);
    }


    // net.haesleinhuepf.clij.advancedfilters.Paste2D
    //----------------------------------------------------
    /**
     * Pastes an image into another image at a given position.
     */
    public boolean paste(ClearCLImage arg1, ClearCLImage arg2, Integer arg3, Integer arg4) {
        return Paste2D.paste(clij, arg1, arg2, arg3, arg4);
    }

    /**
     * Pastes an image into another image at a given position.
     */
    public boolean paste(ClearCLBuffer arg1, ClearCLBuffer arg2, Integer arg3, Integer arg4) {
        return Paste2D.paste(clij, arg1, arg2, arg3, arg4);
    }


    // net.haesleinhuepf.clij.advancedfilters.Paste3D
    //----------------------------------------------------
    /**
     * Pastes an image into another image at a given position.
     */
    public boolean paste(ClearCLImage arg1, ClearCLImage arg2, Integer arg3, Integer arg4, Integer arg5) {
        return Paste3D.paste(clij, arg1, arg2, arg3, arg4, arg5);
    }

    /**
     * Pastes an image into another image at a given position.
     */
    public boolean paste(ClearCLBuffer arg1, ClearCLBuffer arg2, Integer arg3, Integer arg4, Integer arg5) {
        return Paste3D.paste(clij, arg1, arg2, arg3, arg4, arg5);
    }


    // net.haesleinhuepf.clij.advancedfilters.Presign
    //----------------------------------------------------
    /**
     * Determines the extrema of pixel values: f(x) = x / abs(x).
     */
    public boolean presign(ClearCLBuffer arg1, ClearCLBuffer arg2) {
        return Presign.presign(clij, arg1, arg2);
    }


    // net.haesleinhuepf.clij.advancedfilters.SorensenDiceJaccardIndex
    //----------------------------------------------------
    /**
     * 
     */
    public double sorensenDiceCoefficient(ClearCLBuffer arg1, ClearCLBuffer arg2) {
        return SorensenDiceJaccardIndex.sorensenDiceCoefficient(clij, arg1, arg2);
    }

    /**
     * 
     */
    public double jaccardIndex(ClearCLBuffer arg1, ClearCLBuffer arg2) {
        return SorensenDiceJaccardIndex.jaccardIndex(clij, arg1, arg2);
    }


    // net.haesleinhuepf.clij.advancedfilters.StandardDeviationZProjection
    //----------------------------------------------------

    // net.haesleinhuepf.clij.advancedfilters.StackToTiles
    //----------------------------------------------------
    /**
     * Stack to tiles.
     */
    public boolean stackToTiles(ClearCLImage arg1, ClearCLImage arg2, Integer arg3, Integer arg4) {
        return StackToTiles.stackToTiles(clij, arg1, arg2, arg3, arg4);
    }

    /**
     * Stack to tiles.
     */
    public boolean stackToTiles(ClearCLBuffer arg1, ClearCLBuffer arg2, Integer arg3, Integer arg4) {
        return StackToTiles.stackToTiles(clij, arg1, arg2, arg3, arg4);
    }


    // net.haesleinhuepf.clij.advancedfilters.SubtractBackground2D
    //----------------------------------------------------
    /**
     * 
     */
    public boolean subtractBackground(ClearCLBuffer arg1, ClearCLBuffer arg2, Float arg3, Float arg4) {
        return SubtractBackground2D.subtractBackground(clij, arg1, arg2, arg3, arg4);
    }


    // net.haesleinhuepf.clij.advancedfilters.SubtractBackground3D
    //----------------------------------------------------
    /**
     * 
     */
    public boolean subtractBackground(ClearCLBuffer arg1, ClearCLBuffer arg2, Float arg3, Float arg4, Float arg5) {
        return SubtractBackground3D.subtractBackground(clij, arg1, arg2, arg3, arg4, arg5);
    }


    // net.haesleinhuepf.clij.advancedfilters.TopHatBox
    //----------------------------------------------------
    /**
     * Apply a top-hat filter to the input image.
     */
    public boolean topHatBox(ClearCLBuffer arg1, ClearCLBuffer arg2, Integer arg3, Integer arg4, Integer arg5) {
        return TopHatBox.topHatBox(clij, arg1, arg2, arg3, arg4, arg5);
    }


    // net.haesleinhuepf.clij.advancedfilters.TopHatSphere
    //----------------------------------------------------
    /**
     * Apply a top-hat filter to the input image.
     */
    public boolean topHatSphere(ClearCLBuffer arg1, ClearCLBuffer arg2, Integer arg3, Integer arg4, Integer arg5) {
        return TopHatSphere.topHatSphere(clij, arg1, arg2, arg3, arg4, arg5);
    }


    // net.haesleinhuepf.clij.advancedmath.Exponential
    //----------------------------------------------------
    /**
     * Computes base exponential of all pixels values.
     * 
     * f(x) = exp(x)
     */
    public boolean exponential(ClearCLImage arg1, ClearCLImage arg2) {
        return Exponential.exponential(clij, arg1, arg2);
    }

    /**
     * Computes base exponential of all pixels values.
     * 
     * f(x) = exp(x)
     */
    public boolean exponential(ClearCLBuffer arg1, ClearCLBuffer arg2) {
        return Exponential.exponential(clij, arg1, arg2);
    }


    // net.haesleinhuepf.clij.advancedmath.Logarithm
    //----------------------------------------------------
    /**
     * Computes baseE logarithm of all pixels values.
     * 
     * f(x) = logarithm(x)
     */
    public boolean logarithm(ClearCLImage arg1, ClearCLImage arg2) {
        return Logarithm.logarithm(clij, arg1, arg2);
    }

    /**
     * Computes baseE logarithm of all pixels values.
     * 
     * f(x) = logarithm(x)
     */
    public boolean logarithm(ClearCLBuffer arg1, ClearCLBuffer arg2) {
        return Logarithm.logarithm(clij, arg1, arg2);
    }


    // net.haesleinhuepf.clij.matrix.GenerateDistanceMatrix
    //----------------------------------------------------
    /**
     * Takes two images containing coordinates and builds up a matrix containing distance between the points. Convention: image width represents number of points, height represents dimensionality (2D, 3D, ... 10D). The result image has width the first input image and height equals to the width of the second input image.
     */
    public boolean generateDistanceMatrix(ClearCLBuffer arg1, ClearCLBuffer arg2, ClearCLBuffer arg3) {
        return GenerateDistanceMatrix.generateDistanceMatrix(clij, arg1, arg2, arg3);
    }


    // net.haesleinhuepf.clij.matrix.ShortestDistances
    //----------------------------------------------------
    /**
     * Determine the shortest distance from a distance matrix. This corresponds to the minimum in a matrix for each individial column.
     */
    public boolean shortestDistances(ClearCLBuffer arg1, ClearCLBuffer arg2) {
        return ShortestDistances.shortestDistances(clij, arg1, arg2);
    }


    // net.haesleinhuepf.clij.matrix.SpotsToPointList
    //----------------------------------------------------
    /**
     * Transforms a spots image as resulting from maxim detection in an image where every column cotains d 
     * pixels (with d = dimensionality of the original image) with the coordinates of the maxima.
     */
    public boolean spotsToPointList(ClearCLBuffer arg1, ClearCLBuffer arg2) {
        return SpotsToPointList.spotsToPointList(clij, arg1, arg2);
    }


    // net.haesleinhuepf.clij.matrix.TransposeXY
    //----------------------------------------------------
    /**
     * Transpose X and Y in an image.
     */
    public boolean transposeXY(ClearCLBuffer arg1, ClearCLBuffer arg2) {
        return TransposeXY.transposeXY(clij, arg1, arg2);
    }


    // net.haesleinhuepf.clij.matrix.TransposeXZ
    //----------------------------------------------------
    /**
     * Transpose X and Z in an image.
     */
    public boolean transposeXZ(ClearCLBuffer arg1, ClearCLBuffer arg2) {
        return TransposeXZ.transposeXZ(clij, arg1, arg2);
    }


    // net.haesleinhuepf.clij.matrix.TransposeYZ
    //----------------------------------------------------
    /**
     * Transpose Y and Z in an image.
     */
    public boolean transposeYZ(ClearCLBuffer arg1, ClearCLBuffer arg2) {
        return TransposeYZ.transposeYZ(clij, arg1, arg2);
    }


    // net.haesleinhuepf.clij.piv.FastParticleImageVelocimetry
    //----------------------------------------------------
    /**
     * 
     */
    public boolean particleImageVelocimetry2D(ClearCLImage arg1, ClearCLImage arg2, ClearCLImage arg3, ClearCLImage arg4, Integer arg5) {
        return FastParticleImageVelocimetry.particleImageVelocimetry2D(clij, arg1, arg2, arg3, arg4, arg5);
    }

    /**
     * 
     */
    public boolean particleImageVelocimetry2D(ClearCLBuffer arg1, ClearCLBuffer arg2, ClearCLBuffer arg3, ClearCLBuffer arg4, Integer arg5) {
        return FastParticleImageVelocimetry.particleImageVelocimetry2D(clij, arg1, arg2, arg3, arg4, arg5);
    }


    // net.haesleinhuepf.clij.piv.ParticleImageVelocimetry
    //----------------------------------------------------
    /**
     * For every pixel in source image 1, determine the pixel with the most similar intensity in 
     *  the local neighborhood with a given radius in source image 2. Write the distance in 
     * X and Y in the two corresponding destination images.
     */
    public boolean particleImageVelocimetry(ClearCLBuffer arg1, ClearCLBuffer arg2, ClearCLBuffer arg3, ClearCLBuffer arg4, ClearCLBuffer arg5, Integer arg6, Integer arg7, Integer arg8, boolean arg9) {
        return ParticleImageVelocimetry.particleImageVelocimetry(clij, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9);
    }


    // net.haesleinhuepf.clij.piv.ParticleImageVelocimetryTimelapse
    //----------------------------------------------------
    /**
     * Run particle image velocimetry on a 2D+t timelapse.
     */
    public boolean particleImageVelocimetryTimelapse(ClearCLBuffer arg1, ClearCLBuffer arg2, ClearCLBuffer arg3, ClearCLBuffer arg4, int arg5, int arg6, int arg7, boolean arg8) {
        return ParticleImageVelocimetryTimelapse.particleImageVelocimetryTimelapse(clij, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8);
    }


    // net.haesleinhuepf.clij.registration.DeformableRegistration2D
    //----------------------------------------------------
    /**
     * Applies particle image velocimetry to two images and registers them afterwards by warping input image 2 with a smoothed vector field.
     */
    public boolean deformableRegistration2D(ClearCLBuffer arg1, ClearCLBuffer arg2, ClearCLBuffer arg3, int arg4, int arg5) {
        return DeformableRegistration2D.deformableRegistration2D(clij, arg1, arg2, arg3, arg4, arg5);
    }


    // net.haesleinhuepf.clij.registration.TranslationRegistration
    //----------------------------------------------------
    /**
     * Measures center of mass of thresholded objects in the two input images and translates the second image so that it better fits to the first image.
     */
    public boolean translationRegistration(ClearCLBuffer arg1, ClearCLBuffer arg2, double[] arg3) {
        return TranslationRegistration.translationRegistration(clij, arg1, arg2, arg3);
    }

    /**
     * Measures center of mass of thresholded objects in the two input images and translates the second image so that it better fits to the first image.
     */
    public boolean translationRegistration(ClearCLBuffer arg1, ClearCLBuffer arg2, ClearCLBuffer arg3) {
        return TranslationRegistration.translationRegistration(clij, arg1, arg2, arg3);
    }


    // net.haesleinhuepf.clij.registration.TranslationTimelapseRegistration
    //----------------------------------------------------
    /**
     * Applies 2D translation registration to every pair of t, t+1 slices of a 2D+t image stack.
     */
    public boolean translationTimelapseRegistration(ClearCLBuffer arg1, ClearCLBuffer arg2) {
        return TranslationTimelapseRegistration.translationTimelapseRegistration(clij, arg1, arg2);
    }


    // net.haesleinhuepf.clij.advancedfilters.SetWhereXequalsY
    //----------------------------------------------------
    /**
     * Sets all pixel values a of a given image A to a constant value v in case its coordinates x == y. Otherwise the pixel is not overwritten.
     * If you want to initialize an identity transfrom matrix, set all pixels to 0 first.
     * 
     * <pre>f(a) = v</pre>
     */
    public boolean setWhereXequalsY(ClearCLImage arg1, Float arg2) {
        return SetWhereXequalsY.setWhereXequalsY(clij, arg1, arg2);
    }

    /**
     * Sets all pixel values a of a given image A to a constant value v in case its coordinates x == y. Otherwise the pixel is not overwritten.
     * If you want to initialize an identity transfrom matrix, set all pixels to 0 first.
     * 
     * <pre>f(a) = v</pre>
     */
    public boolean setWhereXequalsY(ClearCLBuffer arg1, Float arg2) {
        return SetWhereXequalsY.setWhereXequalsY(clij, arg1, arg2);
    }


    // net.haesleinhuepf.clij.advancedfilters.Laplace
    //----------------------------------------------------
    /**
     * Apply the Laplace operator (Diamond neighborhood) to an image.
     */
    public boolean laplace(ClearCLBuffer arg1, ClearCLBuffer arg2) {
        return Laplace.laplace(clij, arg1, arg2);
    }


    // net.haesleinhuepf.clij.advancedfilters.WriteValuesToPositions
    //----------------------------------------------------
    /**
     * Takes an image with three/four rows (2D: height = 3; 3D: height = 4): x, y [, z] and v and target image. The value v will be written at position x/y[/z] in the target image.
     */
    public boolean writeValuesToPositions(ClearCLBuffer arg1, ClearCLBuffer arg2) {
        return WriteValuesToPositions.writeValuesToPositions(clij, arg1, arg2);
    }


    // net.haesleinhuepf.clij.advancedfilters.GetSize
    //----------------------------------------------------
    /**
     * Reads out the size of an image [stack] and writes it to the results table in the columns 'Width', 'Height' and 'Depth'.
     */
    public long[] getSize(ClearCLBuffer arg1) {
        return GetSize.getSize(clij, arg1);
    }


    // net.haesleinhuepf.clij.matrix.MultiplyMatrix
    //----------------------------------------------------
    /**
     * Multiplies two matrices with each other.
     */
    public boolean multiplyMatrix(ClearCLBuffer arg1, ClearCLBuffer arg2, ClearCLBuffer arg3) {
        return MultiplyMatrix.multiplyMatrix(clij, arg1, arg2, arg3);
    }


    // net.haesleinhuepf.clij.matrix.MatrixEqual
    //----------------------------------------------------
    /**
     * Checks if all elements of a matrix are different by less than or equal to a given tolerance.
     * The result will be put in the results table as 1 if yes and 0 otherwise.
     */
    public boolean matrixEqual(ClearCLBuffer arg1, ClearCLBuffer arg2, Float arg3) {
        return MatrixEqual.matrixEqual(clij, arg1, arg2, arg3);
    }


    // net.haesleinhuepf.clij.advancedfilters.PowerImages
    //----------------------------------------------------
    /**
     * Calculates x to the power of y pixel wise of two images X and Y.
     */
    public boolean powerImages(ClearCLBuffer arg1, ClearCLBuffer arg2, ClearCLBuffer arg3) {
        return PowerImages.powerImages(clij, arg1, arg2, arg3);
    }


    // net.haesleinhuepf.clij.advancedmath.Equal
    //----------------------------------------------------
    /**
     * Determines if two images A and B equal pixel wise.
     * 
     * f(a, b) = 1 if a == b; 0 otherwise. 
     */
    public boolean equal(ClearCLBuffer arg1, ClearCLBuffer arg2, ClearCLBuffer arg3) {
        return Equal.equal(clij, arg1, arg2, arg3);
    }


    // net.haesleinhuepf.clij.advancedmath.GreaterOrEqual
    //----------------------------------------------------
    /**
     * Determines if two images A and B greater or equal pixel wise.
     * 
     * f(a, b) = 1 if a >= b; 0 otherwise. 
     */
    public boolean greaterOrEqual(ClearCLBuffer arg1, ClearCLBuffer arg2, ClearCLBuffer arg3) {
        return GreaterOrEqual.greaterOrEqual(clij, arg1, arg2, arg3);
    }


    // net.haesleinhuepf.clij.advancedmath.Greater
    //----------------------------------------------------
    /**
     * Determines if two images A and B greater pixel wise.
     * 
     * f(a, b) = 1 if a > b; 0 otherwise. 
     */
    public boolean greater(ClearCLBuffer arg1, ClearCLBuffer arg2, ClearCLBuffer arg3) {
        return Greater.greater(clij, arg1, arg2, arg3);
    }


    // net.haesleinhuepf.clij.advancedmath.Smaller
    //----------------------------------------------------
    /**
     * Determines if two images A and B smaller pixel wise.
     * 
     * f(a, b) = 1 if a < b; 0 otherwise. 
     */
    public boolean smaller(ClearCLBuffer arg1, ClearCLBuffer arg2, ClearCLBuffer arg3) {
        return Smaller.smaller(clij, arg1, arg2, arg3);
    }


    // net.haesleinhuepf.clij.advancedmath.SmallerOrEqual
    //----------------------------------------------------
    /**
     * Determines if two images A and B smaller or equal pixel wise.
     * 
     * f(a, b) = 1 if a <= b; 0 otherwise. 
     */
    public boolean smallerOrEqual(ClearCLBuffer arg1, ClearCLBuffer arg2, ClearCLBuffer arg3) {
        return SmallerOrEqual.smallerOrEqual(clij, arg1, arg2, arg3);
    }


    // net.haesleinhuepf.clij.advancedmath.NotEqual
    //----------------------------------------------------
    /**
     * Determines if two images A and B equal pixel wise.
     * 
     * f(a, b) = 1 if a != b; 0 otherwise. 
     */
    public boolean notEqual(ClearCLBuffer arg1, ClearCLBuffer arg2, ClearCLBuffer arg3) {
        return NotEqual.notEqual(clij, arg1, arg2, arg3);
    }


    // net.haesleinhuepf.clij.advancedfilters.ReadImageFromDisc
    //----------------------------------------------------
    /**
     * Read an image from disc.
     */
    public ClearCLBuffer readImageFromDisc(String arg1) {
        return ReadImageFromDisc.readImageFromDisc(clij, arg1);
    }


    // net.haesleinhuepf.clij.advancedfilters.ReadRawImageFromDisc
    //----------------------------------------------------
    /**
     * Reads a raw file from disc and pushes it immediately to the GPU.
     */
    public ClearCLBuffer readRawImageFromDisc(String arg1, Integer arg2, Integer arg3, Integer arg4, Integer arg5) {
        return ReadRawImageFromDisc.readRawImageFromDisc(clij, arg1, arg2, arg3, arg4, arg5);
    }

    /**
     * Reads a raw file from disc and pushes it immediately to the GPU.
     */
    public boolean readRawImageFromDisc(ClearCLBuffer arg1, String arg2) {
        return ReadRawImageFromDisc.readRawImageFromDisc(clij, arg1, arg2);
    }

}
