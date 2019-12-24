package net.haesleinhuepf.clijx.utilities;
import net.haesleinhuepf.clij.CLIJ;
import net.haesleinhuepf.clijx.CLIJx;
import net.haesleinhuepf.clij.clearcl.ClearCLKernel;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.clearcl.ClearCLImage;
import net.haesleinhuepf.clij.clearcl.interfaces.ClearCLImageInterface;
import net.imglib2.realtransform.AffineTransform2D;
import net.imglib2.realtransform.AffineTransform3D;
import ij.measure.ResultsTable;
import ij.gui.Roi;
import net.haesleinhuepf.clij.kernels.Kernels;
import net.haesleinhuepf.clijx.utilities.CLIJxAPIConsistencyWorkaround;
import net.haesleinhuepf.clijx.advancedfilters.BinaryUnion;
import net.haesleinhuepf.clijx.advancedfilters.BinaryIntersection;
import net.haesleinhuepf.clijx.advancedfilters.ConnectedComponentsLabeling;
import net.haesleinhuepf.clijx.advancedfilters.CountNonZeroPixels;
import net.haesleinhuepf.clijx.advancedfilters.CrossCorrelation;
import net.haesleinhuepf.clijx.advancedfilters.DifferenceOfGaussian2D;
import net.haesleinhuepf.clijx.advancedfilters.DifferenceOfGaussian3D;
import net.haesleinhuepf.clijx.advancedfilters.Extrema;
import net.haesleinhuepf.clijx.advancedfilters.LocalExtremaBox;
import net.haesleinhuepf.clijx.advancedfilters.LocalID;
import net.haesleinhuepf.clijx.advancedfilters.MaskLabel;
import net.haesleinhuepf.clijx.advancedfilters.MeanClosestSpotDistance;
import net.haesleinhuepf.clijx.advancedfilters.MeanSquaredError;
import net.haesleinhuepf.clijx.advancedfilters.MedianZProjection;
import net.haesleinhuepf.clijx.advancedfilters.NonzeroMinimumDiamond;
import net.haesleinhuepf.clijx.advancedfilters.Paste2D;
import net.haesleinhuepf.clijx.advancedfilters.Paste3D;
import net.haesleinhuepf.clijx.advancedfilters.Presign;
import net.haesleinhuepf.clijx.advancedfilters.JaccardIndex;
import net.haesleinhuepf.clijx.advancedfilters.SorensenDiceCoefficent;
import net.haesleinhuepf.clijx.advancedfilters.StandardDeviationZProjection;
import net.haesleinhuepf.clijx.advancedfilters.StackToTiles;
import net.haesleinhuepf.clijx.advancedfilters.SubtractBackground2D;
import net.haesleinhuepf.clijx.advancedfilters.SubtractBackground3D;
import net.haesleinhuepf.clijx.advancedfilters.TopHatBox;
import net.haesleinhuepf.clijx.advancedfilters.TopHatSphere;
import net.haesleinhuepf.clijx.advancedmath.Exponential;
import net.haesleinhuepf.clijx.advancedmath.Logarithm;
import net.haesleinhuepf.clijx.matrix.GenerateDistanceMatrix;
import net.haesleinhuepf.clijx.matrix.ShortestDistances;
import net.haesleinhuepf.clijx.matrix.SpotsToPointList;
import net.haesleinhuepf.clijx.matrix.TransposeXY;
import net.haesleinhuepf.clijx.matrix.TransposeXZ;
import net.haesleinhuepf.clijx.matrix.TransposeYZ;
import net.haesleinhuepf.clijx.piv.FastParticleImageVelocimetry;
import net.haesleinhuepf.clijx.piv.ParticleImageVelocimetry;
import net.haesleinhuepf.clijx.piv.ParticleImageVelocimetryTimelapse;
import net.haesleinhuepf.clijx.registration.DeformableRegistration2D;
import net.haesleinhuepf.clijx.registration.TranslationRegistration;
import net.haesleinhuepf.clijx.registration.TranslationTimelapseRegistration;
import net.haesleinhuepf.clijx.advancedfilters.SetWhereXequalsY;
import net.haesleinhuepf.clijx.advancedfilters.LaplaceSphere;
import net.haesleinhuepf.clijx.advancedfilters.Image2DToResultsTable;
import net.haesleinhuepf.clijx.advancedfilters.WriteValuesToPositions;
import net.haesleinhuepf.clijx.advancedfilters.GetSize;
import net.haesleinhuepf.clijx.matrix.MultiplyMatrix;
import net.haesleinhuepf.clijx.matrix.MatrixEqual;
import net.haesleinhuepf.clijx.advancedfilters.PowerImages;
import net.haesleinhuepf.clijx.advancedmath.Equal;
import net.haesleinhuepf.clijx.advancedmath.GreaterOrEqual;
import net.haesleinhuepf.clijx.advancedmath.Greater;
import net.haesleinhuepf.clijx.advancedmath.Smaller;
import net.haesleinhuepf.clijx.advancedmath.SmallerOrEqual;
import net.haesleinhuepf.clijx.advancedmath.NotEqual;
import net.haesleinhuepf.clijx.io.ReadImageFromDisc;
import net.haesleinhuepf.clijx.io.ReadRawImageFromDisc;
import net.haesleinhuepf.clijx.io.PreloadFromDisc;
import net.haesleinhuepf.clijx.advancedmath.EqualConstant;
import net.haesleinhuepf.clijx.advancedmath.GreaterOrEqualConstant;
import net.haesleinhuepf.clijx.advancedmath.GreaterConstant;
import net.haesleinhuepf.clijx.advancedmath.SmallerConstant;
import net.haesleinhuepf.clijx.advancedmath.SmallerOrEqualConstant;
import net.haesleinhuepf.clijx.advancedmath.NotEqualConstant;
import net.haesleinhuepf.clijx.painting.DrawBox;
import net.haesleinhuepf.clijx.painting.DrawLine;
import net.haesleinhuepf.clijx.painting.DrawSphere;
import net.haesleinhuepf.clijx.advancedfilters.ReplaceIntensity;
import net.haesleinhuepf.clijx.advancedfilters.BoundingBox;
import net.haesleinhuepf.clijx.advancedfilters.MinimumOfMaskedPixels;
import net.haesleinhuepf.clijx.advancedfilters.MaximumOfMaskedPixels;
import net.haesleinhuepf.clijx.advancedfilters.MeanOfMaskedPixels;
import net.haesleinhuepf.clijx.advancedfilters.LabelToMask;
import net.haesleinhuepf.clijx.matrix.NClosestPoints;
import net.haesleinhuepf.clijx.matrix.GaussJordan;
import net.haesleinhuepf.clijx.advancedfilters.StatisticsOfLabelledPixels;
import net.haesleinhuepf.clijx.advancedfilters.VarianceOfAllPixels;
import net.haesleinhuepf.clijx.advancedfilters.StandardDeviationOfAllPixels;
import net.haesleinhuepf.clijx.advancedfilters.ExcludeLabelsOnEdges;
import net.haesleinhuepf.clijx.advancedfilters.BinarySubtract;
import net.haesleinhuepf.clijx.advancedfilters.BinaryEdgeDetection;
import net.haesleinhuepf.clijx.advancedfilters.DistanceMap;
import net.haesleinhuepf.clijx.advancedfilters.PullAsROI;
import net.haesleinhuepf.clijx.advancedfilters.NonzeroMaximumDiamond;
import net.haesleinhuepf.clijx.advancedfilters.OnlyzeroOverwriteMaximumDiamond;
import net.haesleinhuepf.clijx.advancedfilters.OnlyzeroOverwriteMaximumBox;
import net.haesleinhuepf.clijx.matrix.GenerateTouchMatrix;
import net.haesleinhuepf.clijx.advancedfilters.DetectLabelEdges;
import net.haesleinhuepf.clijx.advancedfilters.StopWatch;
import net.haesleinhuepf.clijx.matrix.CountTouchingNeighbors;
import net.haesleinhuepf.clijx.advancedfilters.ReplaceIntensities;
import net.haesleinhuepf.clijx.painting.DrawTwoValueLine;
import net.haesleinhuepf.clijx.matrix.AverageDistanceOfNClosestPoints;
import net.haesleinhuepf.clijx.advancedfilters.SaveAsTIF;
import net.haesleinhuepf.clijx.advancedfilters.ConnectedComponentsLabelingInplace;
import net.haesleinhuepf.clijx.matrix.TouchMatrixToMesh;
import net.haesleinhuepf.clijx.advancedfilters.AutomaticThresholdInplace;
import net.haesleinhuepf.clijx.advancedfilters.DifferenceOfGaussianInplace3D;
import net.haesleinhuepf.clijx.advancedfilters.AbsoluteInplace;
import net.haesleinhuepf.clijx.advancedfilters.Resample;
import net.haesleinhuepf.clijx.advancedfilters.EqualizeMeanIntensitiesOfSlices;
import net.haesleinhuepf.clijx.advancedfilters.Watershed;
import net.haesleinhuepf.clijx.advancedfilters.ResliceRadial;
import net.haesleinhuepf.clijx.advancedfilters.ShowRGB;
import net.haesleinhuepf.clijx.advancedfilters.ShowGrey;
import net.haesleinhuepf.clijx.advancedfilters.Sobel;
import net.haesleinhuepf.clijx.advancedfilters.Absolute;
import net.haesleinhuepf.clijx.advancedfilters.LaplaceBox;
import net.haesleinhuepf.clijx.advancedfilters.BottomHatBox;
import net.haesleinhuepf.clijx.advancedfilters.BottomHatSphere;
import net.haesleinhuepf.clijx.advancedfilters.ClosingBox;
import net.haesleinhuepf.clijx.advancedfilters.ClosingDiamond;
import net.haesleinhuepf.clijx.advancedfilters.OpeningBox;
import net.haesleinhuepf.clijx.advancedfilters.OpeningDiamond;
import net.haesleinhuepf.clijx.advancedfilters.MaximumXProjection;
import net.haesleinhuepf.clijx.advancedfilters.MaximumYProjection;
import net.haesleinhuepf.clijx.advancedfilters.ProjectMaximumZBounded;
import net.haesleinhuepf.clijx.advancedfilters.ProjectMinimumZBounded;
import net.haesleinhuepf.clijx.advancedfilters.ProjectMeanZBounded;
import net.haesleinhuepf.clijx.advancedfilters.NonzeroMaximumBox;
import net.haesleinhuepf.clijx.advancedfilters.NonzeroMinimumBox;
import net.haesleinhuepf.clijx.advancedfilters.ProjectMinimumThresholdedZBounded;
import net.haesleinhuepf.clijx.advancedfilters.MeanOfPixelsAboveThreshold;
import net.haesleinhuepf.clijx.gui.OrganiseWindows;
import net.haesleinhuepf.clijx.matrix.DistanceMatrixToMesh;
import net.haesleinhuepf.clijx.matrix.PointIndexListToMesh;
import net.haesleinhuepf.clijx.advancedfilters.MinimumOctagon;
import net.haesleinhuepf.clijx.advancedfilters.MaximumOctagon;
import net.haesleinhuepf.clijx.advancedfilters.TopHatOctagon;
import net.haesleinhuepf.clijx.advancedfilters.AddImages;
import net.haesleinhuepf.clijx.advancedfilters.AddImagesWeighted;
import net.haesleinhuepf.clijx.advancedfilters.SubtractImages;
import net.haesleinhuepf.clijx.advancedfilters.ShowGlasbeyOnGrey;
import net.haesleinhuepf.clijx.weka.ApplyWekaModel;
import net.haesleinhuepf.clijx.weka.TrainWekaModel;
// this is generated code. See src/test/java/net/haesleinhuepf/clijx/codegenerator for details
public abstract class CLIJxOps {
   protected CLIJ clij;
   protected CLIJx clijx;
   

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
    public boolean threshold(ClearCLImage source, ClearCLImage destination, double threshold) {
        return Kernels.threshold(clij, source, destination, new Double (threshold).floatValue());
    }

    /**
     * Computes a binary image with pixel values 0 and 1. All pixel values x of a given input image with 
     * value larger or equal to a given threshold t will be set to 1.
     * 
     * f(x,t) = (1 if (x >= t); (0 otherwise))
     * 
     * This plugin is comparable to setting a raw threshold in ImageJ and using the 'Convert to Mask' menu.
     */
    public boolean threshold(ClearCLBuffer source, ClearCLBuffer destination, double threshold) {
        return Kernels.threshold(clij, source, destination, new Double (threshold).floatValue());
    }

    /**
     * Copies an image.
     * 
     * <pre>f(x) = x</pre>
     */
    public boolean copy(ClearCLImage source, ClearCLBuffer destination) {
        return Kernels.copy(clij, source, destination);
    }

    /**
     * Copies an image.
     * 
     * <pre>f(x) = x</pre>
     */
    public boolean copy(ClearCLBuffer source, ClearCLBuffer destination) {
        return Kernels.copy(clij, source, destination);
    }

    /**
     * Copies an image.
     * 
     * <pre>f(x) = x</pre>
     */
    public boolean copy(ClearCLImage source, ClearCLImage destination) {
        return Kernels.copy(clij, source, destination);
    }

    /**
     * Copies an image.
     * 
     * <pre>f(x) = x</pre>
     */
    public boolean copy(ClearCLBuffer source, ClearCLImage destination) {
        return Kernels.copy(clij, source, destination);
    }

    /**
     * Sets all pixel values x of a given image X to a constant value v.
     * 
     * <pre>f(x) = v</pre>
     */
    public boolean set(ClearCLBuffer source, double value) {
        return Kernels.set(clij, source, new Double (value).floatValue());
    }

    /**
     * Sets all pixel values x of a given image X to a constant value v.
     * 
     * <pre>f(x) = v</pre>
     */
    public boolean set(ClearCLImage source, double value) {
        return Kernels.set(clij, source, new Double (value).floatValue());
    }

    /**
     * Flips an image in X and/or Y direction depending on boolean flags.
     */
    public boolean flip(ClearCLBuffer arg1, ClearCLBuffer arg2, boolean arg3, boolean arg4, boolean arg5) {
        return Kernels.flip(clij, arg1, arg2, arg3, arg4, arg5);
    }

    /**
     * Flips an image in X and/or Y direction depending on boolean flags.
     */
    public boolean flip(ClearCLImage arg1, ClearCLImage arg2, boolean arg3, boolean arg4) {
        return Kernels.flip(clij, arg1, arg2, arg3, arg4);
    }

    /**
     * Flips an image in X and/or Y direction depending on boolean flags.
     */
    public boolean flip(ClearCLBuffer arg1, ClearCLBuffer arg2, boolean arg3, boolean arg4) {
        return Kernels.flip(clij, arg1, arg2, arg3, arg4);
    }

    /**
     * Flips an image in X and/or Y direction depending on boolean flags.
     */
    public boolean flip(ClearCLImage arg1, ClearCLImage arg2, boolean arg3, boolean arg4, boolean arg5) {
        return Kernels.flip(clij, arg1, arg2, arg3, arg4, arg5);
    }

    /**
     * Rotates a given input image by 90 degrees counter-clockwise. For that, X and Y axis of an image stack
     * are flipped. This operation is similar to ImageJs 'Reslice [/]' method but offers less flexibility 
     * such as interpolation.
     */
    public boolean rotateLeft(ClearCLBuffer source, ClearCLBuffer destination) {
        return Kernels.rotateLeft(clij, source, destination);
    }

    /**
     * Rotates a given input image by 90 degrees counter-clockwise. For that, X and Y axis of an image stack
     * are flipped. This operation is similar to ImageJs 'Reslice [/]' method but offers less flexibility 
     * such as interpolation.
     */
    public boolean rotateLeft(ClearCLImage source, ClearCLImage destination) {
        return Kernels.rotateLeft(clij, source, destination);
    }

    /**
     * Rotates a given input image by 90 degrees clockwise. For that, X and Y axis of an image stack
     * are flipped. This operation is similar to ImageJs 'Reslice [/]' method but offers less flexibility 
     * such as interpolation.
     */
    public boolean rotateRight(ClearCLBuffer source, ClearCLBuffer destination) {
        return Kernels.rotateRight(clij, source, destination);
    }

    /**
     * Rotates a given input image by 90 degrees clockwise. For that, X and Y axis of an image stack
     * are flipped. This operation is similar to ImageJs 'Reslice [/]' method but offers less flexibility 
     * such as interpolation.
     */
    public boolean rotateRight(ClearCLImage source, ClearCLImage destination) {
        return Kernels.rotateRight(clij, source, destination);
    }

    /**
     * Computes a masked image by applying a mask to an image. All pixel values x of image X will be copied
     * to the destination image in case pixel value m at the same position in the mask image is not equal to 
     * zero.
     * 
     * <pre>f(x,m) = (x if (m != 0); (0 otherwise))</pre>
     */
    public boolean mask(ClearCLImage source, ClearCLImage mask, ClearCLImage destination) {
        return Kernels.mask(clij, source, mask, destination);
    }

    /**
     * Computes a masked image by applying a mask to an image. All pixel values x of image X will be copied
     * to the destination image in case pixel value m at the same position in the mask image is not equal to 
     * zero.
     * 
     * <pre>f(x,m) = (x if (m != 0); (0 otherwise))</pre>
     */
    public boolean mask(ClearCLBuffer source, ClearCLBuffer mask, ClearCLBuffer destination) {
        return Kernels.mask(clij, source, mask, destination);
    }

    /**
     * Computes all pixels value x to the power of a given exponent a.
     * 
     * <pre>f(x, a) = x ^ a</pre>
     */
    public boolean power(ClearCLBuffer source, ClearCLBuffer destination, double exponent) {
        return Kernels.power(clij, source, destination, new Double (exponent).floatValue());
    }

    /**
     * Computes all pixels value x to the power of a given exponent a.
     * 
     * <pre>f(x, a) = x ^ a</pre>
     */
    public boolean power(ClearCLImage source, ClearCLImage destination, double exponent) {
        return Kernels.power(clij, source, destination, new Double (exponent).floatValue());
    }

    /**
     * Determines the histogram of a given image.
     */
    public float[] histogram(ClearCLBuffer arg1, double arg2, double arg3, double arg4) {
        return Kernels.histogram(clij, arg1, new Double (arg2).floatValue(), new Double (arg3).floatValue(), new Double (arg4).intValue());
    }

    /**
     * Computes the negative value of all pixels in a given image. It is recommended to convert images to 
     * 32-bit float before applying this operation.
     * 
     * <pre>f(x) = - x</pre>
     * 
     * For binary images, use binaryNot.
     */
    public boolean invert(ClearCLImage source, ClearCLImage destination) {
        return Kernels.invert(clij, source, destination);
    }

    /**
     * Computes the negative value of all pixels in a given image. It is recommended to convert images to 
     * 32-bit float before applying this operation.
     * 
     * <pre>f(x) = - x</pre>
     * 
     * For binary images, use binaryNot.
     */
    public boolean invert(ClearCLBuffer source, ClearCLBuffer destination) {
        return Kernels.invert(clij, source, destination);
    }

    /**
     * Crops a given rectangle out of a given image.
     * 
     * Note: If the destination image pre-exists already, it will be overwritten and keep it's dimensions.
     */
    public boolean crop(ClearCLImage arg1, ClearCLImage arg2, double arg3, double arg4, double arg5) {
        return Kernels.crop(clij, arg1, arg2, new Double (arg3).intValue(), new Double (arg4).intValue(), new Double (arg5).intValue());
    }

    /**
     * Crops a given rectangle out of a given image.
     * 
     * Note: If the destination image pre-exists already, it will be overwritten and keep it's dimensions.
     */
    public boolean crop(ClearCLBuffer arg1, ClearCLBuffer arg2, double arg3, double arg4, double arg5) {
        return Kernels.crop(clij, arg1, arg2, new Double (arg3).intValue(), new Double (arg4).intValue(), new Double (arg5).intValue());
    }

    /**
     * Crops a given rectangle out of a given image.
     * 
     * Note: If the destination image pre-exists already, it will be overwritten and keep it's dimensions.
     */
    public boolean crop(ClearCLBuffer arg1, ClearCLBuffer arg2, double arg3, double arg4) {
        return Kernels.crop(clij, arg1, arg2, new Double (arg3).intValue(), new Double (arg4).intValue());
    }

    /**
     * Crops a given rectangle out of a given image.
     * 
     * Note: If the destination image pre-exists already, it will be overwritten and keep it's dimensions.
     */
    public boolean crop(ClearCLImage arg1, ClearCLImage arg2, double arg3, double arg4) {
        return Kernels.crop(clij, arg1, arg2, new Double (arg3).intValue(), new Double (arg4).intValue());
    }

    /**
     * Deforms an image according to distances provided in the given vector images. It is recommended to use 32-bit images for input, output and vector images. 
     */
    public boolean applyVectorfield(ClearCLImage source, ClearCLImage vectorX, ClearCLImage vectorY, ClearCLImage destination) {
        return Kernels.applyVectorfield(clij, source, vectorX, vectorY, destination);
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
    public boolean applyVectorfield(ClearCLImage arg1, ClearCLImage arg2, ClearCLImage arg3, ClearCLImage arg4, ClearCLImage arg5) {
        return Kernels.applyVectorfield(clij, arg1, arg2, arg3, arg4, arg5);
    }

    /**
     * Deforms an image according to distances provided in the given vector images. It is recommended to use 32-bit images for input, output and vector images. 
     */
    public boolean applyVectorfield(ClearCLBuffer source, ClearCLBuffer vectorX, ClearCLBuffer vectorY, ClearCLBuffer destination) {
        return Kernels.applyVectorfield(clij, source, vectorX, vectorY, destination);
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
    public boolean affineTransform(ClearCLImage arg1, ClearCLImage arg2, AffineTransform3D arg3) {
        return Kernels.affineTransform(clij, arg1, arg2, arg3);
    }

    /**
     * Detects local minima in a given square/cubic neighborhood. Pixels in the resulting image are set to 1 if
     * there is no other pixel in a given radius which has a lower intensity, and to 0 otherwise.
     */
    public boolean detectMinimaBox(ClearCLImage source, ClearCLImage destination, double radius) {
        return Kernels.detectMinimaBox(clij, source, destination, new Double (radius).intValue());
    }

    /**
     * Detects local minima in a given square/cubic neighborhood. Pixels in the resulting image are set to 1 if
     * there is no other pixel in a given radius which has a lower intensity, and to 0 otherwise.
     */
    public boolean detectMinimaBox(ClearCLBuffer source, ClearCLBuffer destination, double radius) {
        return Kernels.detectMinimaBox(clij, source, destination, new Double (radius).intValue());
    }

    /**
     * Computes a binary image (containing pixel values 0 and 1) from two images X and Y by connecting pairs of
     * pixels x and y with the binary AND operator &.
     * All pixel values except 0 in the input images are interpreted as 1.
     * 
     * <pre>f(x, y) = x & y</pre>
     */
    public boolean binaryAnd(ClearCLImage operand1, ClearCLImage operand2, ClearCLImage destination) {
        return Kernels.binaryAnd(clij, operand1, operand2, destination);
    }

    /**
     * Computes a binary image (containing pixel values 0 and 1) from two images X and Y by connecting pairs of
     * pixels x and y with the binary AND operator &.
     * All pixel values except 0 in the input images are interpreted as 1.
     * 
     * <pre>f(x, y) = x & y</pre>
     */
    public boolean binaryAnd(ClearCLBuffer operand1, ClearCLBuffer operand2, ClearCLBuffer destination) {
        return Kernels.binaryAnd(clij, operand1, operand2, destination);
    }

    /**
     * 
     */
    public boolean detectOptima(ClearCLBuffer arg1, ClearCLBuffer arg2, double arg3, boolean arg4) {
        return Kernels.detectOptima(clij, arg1, arg2, new Double (arg3).intValue(), arg4);
    }

    /**
     * 
     */
    public boolean detectOptima(ClearCLImage arg1, ClearCLImage arg2, double arg3, boolean arg4) {
        return Kernels.detectOptima(clij, arg1, arg2, new Double (arg3).intValue(), arg4);
    }

    /**
     * 
     */
    public boolean fillHistogram(ClearCLBuffer arg1, ClearCLBuffer arg2, double arg3, double arg4) {
        return Kernels.fillHistogram(clij, arg1, arg2, new Double (arg3).floatValue(), new Double (arg4).floatValue());
    }

    /**
     * Computes a binary image (containing pixel values 0 and 1) from two images X and Y by connecting pairs of
     * pixels x and y with the binary OR operator |.
     * All pixel values except 0 in the input images are interpreted as 1.<pre>f(x, y) = x | y</pre>
     */
    public boolean binaryOr(ClearCLImage operand1, ClearCLImage operand2, ClearCLImage destination) {
        return Kernels.binaryOr(clij, operand1, operand2, destination);
    }

    /**
     * Computes a binary image (containing pixel values 0 and 1) from two images X and Y by connecting pairs of
     * pixels x and y with the binary OR operator |.
     * All pixel values except 0 in the input images are interpreted as 1.<pre>f(x, y) = x | y</pre>
     */
    public boolean binaryOr(ClearCLBuffer operand1, ClearCLBuffer operand2, ClearCLBuffer destination) {
        return Kernels.binaryOr(clij, operand1, operand2, destination);
    }

    /**
     * Scales an image using given scaling factors for X and Y dimensions. The nearest-neighbor method
     * is applied. In ImageJ the method which is similar is called 'Interpolation method: none'.
     */
    public boolean downsample(ClearCLBuffer arg1, ClearCLBuffer arg2, double arg3, double arg4, double arg5) {
        return Kernels.downsample(clij, arg1, arg2, new Double (arg3).floatValue(), new Double (arg4).floatValue(), new Double (arg5).floatValue());
    }

    /**
     * Scales an image using given scaling factors for X and Y dimensions. The nearest-neighbor method
     * is applied. In ImageJ the method which is similar is called 'Interpolation method: none'.
     */
    public boolean downsample(ClearCLImage source, ClearCLImage destination, double factorX, double factorY) {
        return Kernels.downsample(clij, source, destination, new Double (factorX).floatValue(), new Double (factorY).floatValue());
    }

    /**
     * Scales an image using given scaling factors for X and Y dimensions. The nearest-neighbor method
     * is applied. In ImageJ the method which is similar is called 'Interpolation method: none'.
     */
    public boolean downsample(ClearCLBuffer source, ClearCLBuffer destination, double factorX, double factorY) {
        return Kernels.downsample(clij, source, destination, new Double (factorX).floatValue(), new Double (factorY).floatValue());
    }

    /**
     * Scales an image using given scaling factors for X and Y dimensions. The nearest-neighbor method
     * is applied. In ImageJ the method which is similar is called 'Interpolation method: none'.
     */
    public boolean downsample(ClearCLImage arg1, ClearCLImage arg2, double arg3, double arg4, double arg5) {
        return Kernels.downsample(clij, arg1, arg2, new Double (arg3).floatValue(), new Double (arg4).floatValue(), new Double (arg5).floatValue());
    }

    /**
     * Computes a binary image (containing pixel values 0 and 1) from two images X and Y by connecting pairs of
     * pixels x and y with the binary operators AND &, OR | and NOT ! implementing the XOR operator.
     * All pixel values except 0 in the input images are interpreted as 1.
     * 
     * <pre>f(x, y) = (x & !y) | (!x & y)</pre>
     */
    public boolean binaryXOr(ClearCLImage operand1, ClearCLImage operand2, ClearCLImage destination) {
        return Kernels.binaryXOr(clij, operand1, operand2, destination);
    }

    /**
     * Computes a binary image (containing pixel values 0 and 1) from two images X and Y by connecting pairs of
     * pixels x and y with the binary operators AND &, OR | and NOT ! implementing the XOR operator.
     * All pixel values except 0 in the input images are interpreted as 1.
     * 
     * <pre>f(x, y) = (x & !y) | (!x & y)</pre>
     */
    public boolean binaryXOr(ClearCLBuffer operand1, ClearCLBuffer operand2, ClearCLBuffer destination) {
        return Kernels.binaryXOr(clij, operand1, operand2, destination);
    }

    /**
     * Computes the Gaussian blurred image of an image given two sigma values in X and Y. Thus, the filterkernel can have non-isotropic shape.
     * 
     * The implementation is done separable. In case a sigma equals zero, the direction is not blurred.
     */
    public boolean blur(ClearCLBuffer source, ClearCLBuffer destination, double sigmaX, double sigmaY) {
        return Kernels.blur(clij, source, destination, new Double (sigmaX).floatValue(), new Double (sigmaY).floatValue());
    }

    /**
     * Computes the Gaussian blurred image of an image given two sigma values in X and Y. Thus, the filterkernel can have non-isotropic shape.
     * 
     * The implementation is done separable. In case a sigma equals zero, the direction is not blurred.
     */
    public boolean blur(ClearCLImage source, ClearCLImage destination, double sigmaX, double sigmaY) {
        return Kernels.blur(clij, source, destination, new Double (sigmaX).floatValue(), new Double (sigmaY).floatValue());
    }

    /**
     * Computes the Gaussian blurred image of an image given two sigma values in X and Y. Thus, the filterkernel can have non-isotropic shape.
     * 
     * The implementation is done separable. In case a sigma equals zero, the direction is not blurred.
     */
    public boolean blur(ClearCLBuffer arg1, ClearCLBuffer arg2, double arg3, double arg4, double arg5) {
        return Kernels.blur(clij, arg1, arg2, new Double (arg3).floatValue(), new Double (arg4).floatValue(), new Double (arg5).floatValue());
    }

    /**
     * Computes the Gaussian blurred image of an image given two sigma values in X and Y. Thus, the filterkernel can have non-isotropic shape.
     * 
     * The implementation is done separable. In case a sigma equals zero, the direction is not blurred.
     */
    public boolean blur(ClearCLImage arg1, ClearCLBuffer arg2, double arg3, double arg4, double arg5) {
        return Kernels.blur(clij, arg1, arg2, new Double (arg3).floatValue(), new Double (arg4).floatValue(), new Double (arg5).floatValue());
    }

    /**
     * Computes the Gaussian blurred image of an image given two sigma values in X and Y. Thus, the filterkernel can have non-isotropic shape.
     * 
     * The implementation is done separable. In case a sigma equals zero, the direction is not blurred.
     */
    public boolean blur(ClearCLImage arg1, ClearCLImage arg2, double arg3, double arg4, double arg5) {
        return Kernels.blur(clij, arg1, arg2, new Double (arg3).floatValue(), new Double (arg4).floatValue(), new Double (arg5).floatValue());
    }

    /**
     * Computes the Gaussian blurred image of an image given two sigma values in X and Y. Thus, the filterkernel can have non-isotropic shape.
     * 
     * The implementation is done separable. In case a sigma equals zero, the direction is not blurred.
     */
    public boolean blur(ClearCLImage source, ClearCLBuffer destination, double sigmaX, double sigmaY) {
        return Kernels.blur(clij, source, destination, new Double (sigmaX).floatValue(), new Double (sigmaY).floatValue());
    }

    /**
     * Computes a binary image with pixel values 0 and 1 containing the binary erosion of a given input image.
     * The erosion takes the von-Neumann-neighborhood (4 pixels in 2D and 6 pixels in 3d) into account.
     * The pixels in the input image with pixel value not equal to 0 will be interpreted as 1.
     */
    public boolean erodeSphere(ClearCLImage source, ClearCLImage destination) {
        return Kernels.erodeSphere(clij, source, destination);
    }

    /**
     * Computes a binary image with pixel values 0 and 1 containing the binary erosion of a given input image.
     * The erosion takes the von-Neumann-neighborhood (4 pixels in 2D and 6 pixels in 3d) into account.
     * The pixels in the input image with pixel value not equal to 0 will be interpreted as 1.
     */
    public boolean erodeSphere(ClearCLBuffer source, ClearCLBuffer destination) {
        return Kernels.erodeSphere(clij, source, destination);
    }

    /**
     * Computes a binary image with pixel values 0 and 1 containing the binary erosion of a given input image.
     * The erosion takes the Moore-neighborhood (8 pixels in 2D and 26 pixels in 3d) into account.
     * The pixels in the input image with pixel value not equal to 0 will be interpreted as 1.
     * 
     * This method is comparable to the 'Erode' menu in ImageJ in case it is applied to a 2D image. The only
     * difference is that the output image contains values 0 and 1 instead of 0 and 255.
     */
    public boolean erodeBox(ClearCLBuffer source, ClearCLBuffer destination) {
        return Kernels.erodeBox(clij, source, destination);
    }

    /**
     * Computes a binary image with pixel values 0 and 1 containing the binary erosion of a given input image.
     * The erosion takes the Moore-neighborhood (8 pixels in 2D and 26 pixels in 3d) into account.
     * The pixels in the input image with pixel value not equal to 0 will be interpreted as 1.
     * 
     * This method is comparable to the 'Erode' menu in ImageJ in case it is applied to a 2D image. The only
     * difference is that the output image contains values 0 and 1 instead of 0 and 255.
     */
    public boolean erodeBox(ClearCLImage source, ClearCLImage destination) {
        return Kernels.erodeBox(clij, source, destination);
    }

    /**
     * Determines the center of mass of an image or image stack and writes the result in the results table
     * in the columns MassX, MassY and MassZ.
     */
    public double[] centerOfMass(ClearCLImage source) {
        return Kernels.centerOfMass(clij, source);
    }

    /**
     * Determines the center of mass of an image or image stack and writes the result in the results table
     * in the columns MassX, MassY and MassZ.
     */
    public double[] centerOfMass(ClearCLBuffer source) {
        return Kernels.centerOfMass(clij, source);
    }

    /**
     * Computes the gradient of gray values along X. Assuming a, b and c are three adjacent
     *  pixels in X direction. In the target image will be saved as: <pre>b' = c - a;</pre>
     */
    public boolean gradientX(ClearCLImage source, ClearCLImage destination) {
        return Kernels.gradientX(clij, source, destination);
    }

    /**
     * Computes the gradient of gray values along X. Assuming a, b and c are three adjacent
     *  pixels in X direction. In the target image will be saved as: <pre>b' = c - a;</pre>
     */
    public boolean gradientX(ClearCLBuffer source, ClearCLBuffer destination) {
        return Kernels.gradientX(clij, source, destination);
    }

    /**
     * Computes a binary image with pixel values 0 and 1 containing the binary dilation of a given input image.
     * The dilation takes the Moore-neighborhood (8 pixels in 2D and 26 pixels in 3d) into account.
     * The pixels in the input image with pixel value not equal to 0 will be interpreted as 1.
     * 
     * This method is comparable to the 'Dilate' menu in ImageJ in case it is applied to a 2D image. The only
     * difference is that the output image contains values 0 and 1 instead of 0 and 255.
     */
    public boolean dilateBox(ClearCLImage source, ClearCLImage destination) {
        return Kernels.dilateBox(clij, source, destination);
    }

    /**
     * Computes a binary image with pixel values 0 and 1 containing the binary dilation of a given input image.
     * The dilation takes the Moore-neighborhood (8 pixels in 2D and 26 pixels in 3d) into account.
     * The pixels in the input image with pixel value not equal to 0 will be interpreted as 1.
     * 
     * This method is comparable to the 'Dilate' menu in ImageJ in case it is applied to a 2D image. The only
     * difference is that the output image contains values 0 and 1 instead of 0 and 255.
     */
    public boolean dilateBox(ClearCLBuffer source, ClearCLBuffer destination) {
        return Kernels.dilateBox(clij, source, destination);
    }

    /**
     * Computes the gradient of gray values along Y. Assuming a, b and c are three adjacent
     *  pixels in Y direction. In the target image will be saved as: <pre>b' = c - a;</pre>
     */
    public boolean gradientY(ClearCLBuffer source, ClearCLBuffer destination) {
        return Kernels.gradientY(clij, source, destination);
    }

    /**
     * Computes the gradient of gray values along Y. Assuming a, b and c are three adjacent
     *  pixels in Y direction. In the target image will be saved as: <pre>b' = c - a;</pre>
     */
    public boolean gradientY(ClearCLImage source, ClearCLImage destination) {
        return Kernels.gradientY(clij, source, destination);
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
    public boolean copySlice(ClearCLImage source, ClearCLImage destination, double sliceIndex) {
        return Kernels.copySlice(clij, source, destination, new Double (sliceIndex).intValue());
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
    public boolean copySlice(ClearCLBuffer source, ClearCLBuffer destination, double sliceIndex) {
        return Kernels.copySlice(clij, source, destination, new Double (sliceIndex).intValue());
    }

    /**
     * Computes the gradient of gray values along Z. Assuming a, b and c are three adjacent
     *  pixels in Z direction. In the target image will be saved as: <pre>b' = c - a;</pre>
     */
    public boolean gradientZ(ClearCLBuffer source, ClearCLBuffer destination) {
        return Kernels.gradientZ(clij, source, destination);
    }

    /**
     * Computes the gradient of gray values along Z. Assuming a, b and c are three adjacent
     *  pixels in Z direction. In the target image will be saved as: <pre>b' = c - a;</pre>
     */
    public boolean gradientZ(ClearCLImage source, ClearCLImage destination) {
        return Kernels.gradientZ(clij, source, destination);
    }

    /**
     * Computes a binary image with pixel values 0 and 1 containing the binary dilation of a given input image.
     * The dilation takes the von-Neumann-neighborhood (4 pixels in 2D and 6 pixels in 3d) into account.
     * The pixels in the input image with pixel value not equal to 0 will be interpreted as 1.
     */
    public boolean dilateSphere(ClearCLImage source, ClearCLImage destination) {
        return Kernels.dilateSphere(clij, source, destination);
    }

    /**
     * Computes a binary image with pixel values 0 and 1 containing the binary dilation of a given input image.
     * The dilation takes the von-Neumann-neighborhood (4 pixels in 2D and 6 pixels in 3d) into account.
     * The pixels in the input image with pixel value not equal to 0 will be interpreted as 1.
     */
    public boolean dilateSphere(ClearCLBuffer source, ClearCLBuffer destination) {
        return Kernels.dilateSphere(clij, source, destination);
    }

    /**
     * Detects local maxima in a given square/cubic neighborhood. Pixels in the resulting image are set to 1 if
     * there is no other pixel in a given radius which has a higher intensity, and to 0 otherwise.
     */
    public boolean detectMaximaBox(ClearCLBuffer source, ClearCLBuffer destination, double radius) {
        return Kernels.detectMaximaBox(clij, source, destination, new Double (radius).intValue());
    }

    /**
     * Detects local maxima in a given square/cubic neighborhood. Pixels in the resulting image are set to 1 if
     * there is no other pixel in a given radius which has a higher intensity, and to 0 otherwise.
     */
    public boolean detectMaximaBox(ClearCLImage source, ClearCLImage destination, double radius) {
        return Kernels.detectMaximaBox(clij, source, destination, new Double (radius).intValue());
    }

    /**
     * Divides two images X and Y by each other pixel wise.
     * 
     * <pre>f(x, y) = x / y</pre>
     */
    public boolean divideImages(ClearCLBuffer divident, ClearCLBuffer divisor, ClearCLBuffer destination) {
        return Kernels.divideImages(clij, divident, divisor, destination);
    }

    /**
     * Divides two images X and Y by each other pixel wise.
     * 
     * <pre>f(x, y) = x / y</pre>
     */
    public boolean divideImages(ClearCLImage divident, ClearCLImage divisor, ClearCLImage destination) {
        return Kernels.divideImages(clij, divident, divisor, destination);
    }

    /**
     * Computes a binary image (containing pixel values 0 and 1) from an image X by negating its pixel values
     * x using the binary NOT operator !
     * All pixel values except 0 in the input image are interpreted as 1.
     * 
     * <pre>f(x) = !x</pre>
     */
    public boolean binaryNot(ClearCLImage source, ClearCLImage destination) {
        return Kernels.binaryNot(clij, source, destination);
    }

    /**
     * Computes a binary image (containing pixel values 0 and 1) from an image X by negating its pixel values
     * x using the binary NOT operator !
     * All pixel values except 0 in the input image are interpreted as 1.
     * 
     * <pre>f(x) = !x</pre>
     */
    public boolean binaryNot(ClearCLBuffer source, ClearCLBuffer destination) {
        return Kernels.binaryNot(clij, source, destination);
    }

    /**
     * Computes the Gaussian blurred image of an image given two sigma values in X and Y. Thus, the filterkernel can have non-isotropic shape.
     * 
     * The Gaussian blur is applied slice by slice in 2D.
     */
    public boolean blurSliceBySlice(ClearCLImage arg1, ClearCLImage arg2, double arg3, double arg4, double arg5, double arg6) {
        return Kernels.blurSliceBySlice(clij, arg1, arg2, new Double (arg3).intValue(), new Double (arg4).intValue(), new Double (arg5).floatValue(), new Double (arg6).floatValue());
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
     * This method is deprecated. Consider using maximumBox or maximumSphere instead.
     */
    public boolean maximumIJ(ClearCLImage arg1, ClearCLImage arg2, double arg3) {
        return Kernels.maximumIJ(clij, arg1, arg2, new Double (arg3).intValue());
    }

    /**
     * This method is deprecated. Consider using maximumBox or maximumSphere instead.
     */
    public boolean maximumIJ(ClearCLBuffer arg1, ClearCLBuffer arg2, double arg3) {
        return Kernels.maximumIJ(clij, arg1, arg2, new Double (arg3).intValue());
    }

    /**
     * Computes the local maximum of a pixels rectangular neighborhood. The rectangles size is specified by 
     * its half-width and half-height (radius).
     */
    public boolean maximumSphere(ClearCLImage source, ClearCLImage destination, double radiusX, double radiusY) {
        return Kernels.maximumSphere(clij, source, destination, new Double (radiusX).intValue(), new Double (radiusY).intValue());
    }

    /**
     * Computes the local maximum of a pixels rectangular neighborhood. The rectangles size is specified by 
     * its half-width and half-height (radius).
     */
    public boolean maximumSphere(ClearCLBuffer source, ClearCLBuffer destination, double radiusX, double radiusY) {
        return Kernels.maximumSphere(clij, source, destination, new Double (radiusX).intValue(), new Double (radiusY).intValue());
    }

    /**
     * Computes the local maximum of a pixels rectangular neighborhood. The rectangles size is specified by 
     * its half-width and half-height (radius).
     */
    public boolean maximumSphere(ClearCLBuffer arg1, ClearCLBuffer arg2, double arg3, double arg4, double arg5) {
        return Kernels.maximumSphere(clij, arg1, arg2, new Double (arg3).intValue(), new Double (arg4).intValue(), new Double (arg5).intValue());
    }

    /**
     * Computes the local maximum of a pixels rectangular neighborhood. The rectangles size is specified by 
     * its half-width and half-height (radius).
     */
    public boolean maximumSphere(ClearCLImage arg1, ClearCLImage arg2, double arg3, double arg4, double arg5) {
        return Kernels.maximumSphere(clij, arg1, arg2, new Double (arg3).intValue(), new Double (arg4).intValue(), new Double (arg5).intValue());
    }

    /**
     * Computes the local maximum of a pixels rectangular neighborhood. The rectangles size is specified by 
     * its half-width and half-height (radius).
     */
    public boolean maximumBox(ClearCLBuffer arg1, ClearCLBuffer arg2, int arg3, int arg4, int arg5) {
        return Kernels.maximumBox(clij, arg1, arg2, arg3, arg4, arg5);
    }

    /**
     * Computes the local maximum of a pixels rectangular neighborhood. The rectangles size is specified by 
     * its half-width and half-height (radius).
     */
    public boolean maximumBox(ClearCLImage arg1, ClearCLImage arg2, int arg3, int arg4, int arg5) {
        return Kernels.maximumBox(clij, arg1, arg2, arg3, arg4, arg5);
    }

    /**
     * Computes the maximum of a pair of pixel values x, y from two given images X and Y.
     * 
     * <pre>f(x, y) = max(x, y)</pre>
     */
    public boolean maximumImages(ClearCLImage source1, ClearCLImage source2, ClearCLImage destination) {
        return Kernels.maximumImages(clij, source1, source2, destination);
    }

    /**
     * Computes the maximum of a pair of pixel values x, y from two given images X and Y.
     * 
     * <pre>f(x, y) = max(x, y)</pre>
     */
    public boolean maximumImages(ClearCLBuffer source1, ClearCLBuffer source2, ClearCLBuffer destination) {
        return Kernels.maximumImages(clij, source1, source2, destination);
    }

    /**
     * Computes the minimum of a pair of pixel values x, y from two given images X and Y.
     * 
     * <pre>f(x, y) = min(x, y)</pre>
     */
    public boolean minimumImages(ClearCLBuffer source1, ClearCLBuffer source2, ClearCLBuffer destination) {
        return Kernels.minimumImages(clij, source1, source2, destination);
    }

    /**
     * Computes the minimum of a pair of pixel values x, y from two given images X and Y.
     * 
     * <pre>f(x, y) = min(x, y)</pre>
     */
    public boolean minimumImages(ClearCLImage source1, ClearCLImage source2, ClearCLImage destination) {
        return Kernels.minimumImages(clij, source1, source2, destination);
    }

    /**
     * Determines the mean average projection of an image along Z.
     */
    public boolean meanZProjection(ClearCLBuffer source, ClearCLBuffer destination) {
        return Kernels.meanZProjection(clij, source, destination);
    }

    /**
     * Determines the mean average projection of an image along Z.
     */
    public boolean meanZProjection(ClearCLImage source, ClearCLImage destination) {
        return Kernels.meanZProjection(clij, source, destination);
    }

    /**
     * Computes the local mean average of a pixels rectangular neighborhood. The rectangles size is specified by 
     * its half-width and half-height (radius).
     */
    public boolean meanSphere(ClearCLBuffer source, ClearCLBuffer destination, double radiusX, double radiusY) {
        return Kernels.meanSphere(clij, source, destination, new Double (radiusX).intValue(), new Double (radiusY).intValue());
    }

    /**
     * Computes the local mean average of a pixels rectangular neighborhood. The rectangles size is specified by 
     * its half-width and half-height (radius).
     */
    public boolean meanSphere(ClearCLImage arg1, ClearCLImage arg2, double arg3, double arg4, double arg5) {
        return Kernels.meanSphere(clij, arg1, arg2, new Double (arg3).intValue(), new Double (arg4).intValue(), new Double (arg5).intValue());
    }

    /**
     * Computes the local mean average of a pixels rectangular neighborhood. The rectangles size is specified by 
     * its half-width and half-height (radius).
     */
    public boolean meanSphere(ClearCLBuffer arg1, ClearCLBuffer arg2, double arg3, double arg4, double arg5) {
        return Kernels.meanSphere(clij, arg1, arg2, new Double (arg3).intValue(), new Double (arg4).intValue(), new Double (arg5).intValue());
    }

    /**
     * Computes the local mean average of a pixels rectangular neighborhood. The rectangles size is specified by 
     * its half-width and half-height (radius).
     */
    public boolean meanSphere(ClearCLImage source, ClearCLImage destination, double radiusX, double radiusY) {
        return Kernels.meanSphere(clij, source, destination, new Double (radiusX).intValue(), new Double (radiusY).intValue());
    }

    /**
     * Computes a binary image with pixel values 0 and 1 depending on if a pixel value x in image X 
     * was above of equal to the pixel value m in mask image M.
     * 
     * <pre>f(x) = (1 if (x >=  m)); (0 otherwise)</pre>
     */
    public boolean localThreshold(ClearCLImage source, ClearCLImage localThreshold, ClearCLImage destination) {
        return Kernels.localThreshold(clij, source, localThreshold, destination);
    }

    /**
     * Computes a binary image with pixel values 0 and 1 depending on if a pixel value x in image X 
     * was above of equal to the pixel value m in mask image M.
     * 
     * <pre>f(x) = (1 if (x >=  m)); (0 otherwise)</pre>
     */
    public boolean localThreshold(ClearCLBuffer source, ClearCLBuffer localThreshold, ClearCLBuffer destination) {
        return Kernels.localThreshold(clij, source, localThreshold, destination);
    }

    /**
     * Flippes X, Y and Z axis of an image stack. This operation is similar to ImageJs 'Reslice [/]' method 
     *  but offers less flexibility such as interpolation.
     */
    public boolean resliceRight(ClearCLImage source, ClearCLImage destination) {
        return Kernels.resliceRight(clij, source, destination);
    }

    /**
     * Flippes X, Y and Z axis of an image stack. This operation is similar to ImageJs 'Reslice [/]' method 
     *  but offers less flexibility such as interpolation.
     */
    public boolean resliceRight(ClearCLBuffer source, ClearCLBuffer destination) {
        return Kernels.resliceRight(clij, source, destination);
    }

    /**
     * Computes the local mean average of a pixels rectangular neighborhood. The rectangles size is specified by 
     * its half-width and half-height (radius).
     */
    public boolean meanBox(ClearCLBuffer arg1, ClearCLBuffer arg2, int arg3, int arg4, int arg5) {
        return Kernels.meanBox(clij, arg1, arg2, arg3, arg4, arg5);
    }

    /**
     * Computes the local mean average of a pixels rectangular neighborhood. The rectangles size is specified by 
     * its half-width and half-height (radius).
     */
    public boolean meanBox(ClearCLImage arg1, ClearCLImage arg2, int arg3, int arg4, int arg5) {
        return Kernels.meanBox(clij, arg1, arg2, arg3, arg4, arg5);
    }

    /**
     * Flippes Y and Z axis of an image stack. This operation is similar to ImageJs 'Reslice [/]' method but
     * offers less flexibility such as interpolation.
     */
    public boolean resliceBottom(ClearCLBuffer source, ClearCLBuffer destination) {
        return Kernels.resliceBottom(clij, source, destination);
    }

    /**
     * Flippes Y and Z axis of an image stack. This operation is similar to ImageJs 'Reslice [/]' method but
     * offers less flexibility such as interpolation.
     */
    public boolean resliceBottom(ClearCLImage source, ClearCLImage destination) {
        return Kernels.resliceBottom(clij, source, destination);
    }

    /**
     * This method is deprecated. Consider using minimumBox or minimumSphere instead.
     */
    public boolean minimumIJ(ClearCLImage arg1, ClearCLImage arg2, double arg3) {
        return Kernels.minimumIJ(clij, arg1, arg2, new Double (arg3).intValue());
    }

    /**
     * This method is deprecated. Consider using minimumBox or minimumSphere instead.
     */
    public boolean minimumIJ(ClearCLBuffer arg1, ClearCLBuffer arg2, double arg3) {
        return Kernels.minimumIJ(clij, arg1, arg2, new Double (arg3).intValue());
    }

    /**
     * This method is deprecated. Consider using meanBox or meanSphere instead.
     */
    public boolean meanIJ(ClearCLImage arg1, ClearCLImage arg2, double arg3) {
        return Kernels.meanIJ(clij, arg1, arg2, new Double (arg3).intValue());
    }

    /**
     * This method is deprecated. Consider using meanBox or meanSphere instead.
     */
    public boolean meanIJ(ClearCLBuffer arg1, ClearCLBuffer arg2, double arg3) {
        return Kernels.meanIJ(clij, arg1, arg2, new Double (arg3).intValue());
    }

    /**
     * Computes the local median of a pixels rectangular neighborhood. The rectangle is specified by 
     * its half-width and half-height (radius).
     * 
     * For technical reasons, the area of the rectangle must have less than 1000 pixels.
     */
    public boolean medianBox(ClearCLImage arg1, ClearCLImage arg2, double arg3, double arg4, double arg5) {
        return Kernels.medianBox(clij, arg1, arg2, new Double (arg3).intValue(), new Double (arg4).intValue(), new Double (arg5).intValue());
    }

    /**
     * Computes the local median of a pixels rectangular neighborhood. The rectangle is specified by 
     * its half-width and half-height (radius).
     * 
     * For technical reasons, the area of the rectangle must have less than 1000 pixels.
     */
    public boolean medianBox(ClearCLBuffer arg1, ClearCLBuffer arg2, double arg3, double arg4, double arg5) {
        return Kernels.medianBox(clij, arg1, arg2, new Double (arg3).intValue(), new Double (arg4).intValue(), new Double (arg5).intValue());
    }

    /**
     * Computes the local median of a pixels rectangular neighborhood. The rectangle is specified by 
     * its half-width and half-height (radius).
     * 
     * For technical reasons, the area of the rectangle must have less than 1000 pixels.
     */
    public boolean medianBox(ClearCLBuffer source, ClearCLBuffer destination, double radiusX, double radiusY) {
        return Kernels.medianBox(clij, source, destination, new Double (radiusX).intValue(), new Double (radiusY).intValue());
    }

    /**
     * Computes the local median of a pixels rectangular neighborhood. The rectangle is specified by 
     * its half-width and half-height (radius).
     * 
     * For technical reasons, the area of the rectangle must have less than 1000 pixels.
     */
    public boolean medianBox(ClearCLImage source, ClearCLImage destination, double radiusX, double radiusY) {
        return Kernels.medianBox(clij, source, destination, new Double (radiusX).intValue(), new Double (radiusY).intValue());
    }

    /**
     * Computes the local minimum of a pixels rectangular neighborhood. The rectangles size is specified by 
     * its half-width and half-height (radius).
     */
    public boolean minimumSphere(ClearCLBuffer arg1, ClearCLBuffer arg2, double arg3, double arg4, double arg5) {
        return Kernels.minimumSphere(clij, arg1, arg2, new Double (arg3).intValue(), new Double (arg4).intValue(), new Double (arg5).intValue());
    }

    /**
     * Computes the local minimum of a pixels rectangular neighborhood. The rectangles size is specified by 
     * its half-width and half-height (radius).
     */
    public boolean minimumSphere(ClearCLImage arg1, ClearCLImage arg2, double arg3, double arg4, double arg5) {
        return Kernels.minimumSphere(clij, arg1, arg2, new Double (arg3).intValue(), new Double (arg4).intValue(), new Double (arg5).intValue());
    }

    /**
     * Computes the local minimum of a pixels rectangular neighborhood. The rectangles size is specified by 
     * its half-width and half-height (radius).
     */
    public boolean minimumSphere(ClearCLBuffer source, ClearCLBuffer destination, double radiusX, double radiusY) {
        return Kernels.minimumSphere(clij, source, destination, new Double (radiusX).intValue(), new Double (radiusY).intValue());
    }

    /**
     * Computes the local minimum of a pixels rectangular neighborhood. The rectangles size is specified by 
     * its half-width and half-height (radius).
     */
    public boolean minimumSphere(ClearCLImage source, ClearCLImage destination, double radiusX, double radiusY) {
        return Kernels.minimumSphere(clij, source, destination, new Double (radiusX).intValue(), new Double (radiusY).intValue());
    }

    /**
     * Computes the local median of a pixels rectangular neighborhood. The rectangle is specified by 
     * its half-width and half-height (radius).
     * 
     * For technical reasons, the area of the rectangle must have less than 1000 pixels.
     */
    public boolean medianSphere(ClearCLBuffer source, ClearCLBuffer destination, double radiusX, double radiusY) {
        return Kernels.medianSphere(clij, source, destination, new Double (radiusX).intValue(), new Double (radiusY).intValue());
    }

    /**
     * Computes the local median of a pixels rectangular neighborhood. The rectangle is specified by 
     * its half-width and half-height (radius).
     * 
     * For technical reasons, the area of the rectangle must have less than 1000 pixels.
     */
    public boolean medianSphere(ClearCLBuffer arg1, ClearCLBuffer arg2, double arg3, double arg4, double arg5) {
        return Kernels.medianSphere(clij, arg1, arg2, new Double (arg3).intValue(), new Double (arg4).intValue(), new Double (arg5).intValue());
    }

    /**
     * Computes the local median of a pixels rectangular neighborhood. The rectangle is specified by 
     * its half-width and half-height (radius).
     * 
     * For technical reasons, the area of the rectangle must have less than 1000 pixels.
     */
    public boolean medianSphere(ClearCLImage arg1, ClearCLImage arg2, double arg3, double arg4, double arg5) {
        return Kernels.medianSphere(clij, arg1, arg2, new Double (arg3).intValue(), new Double (arg4).intValue(), new Double (arg5).intValue());
    }

    /**
     * Computes the local median of a pixels rectangular neighborhood. The rectangle is specified by 
     * its half-width and half-height (radius).
     * 
     * For technical reasons, the area of the rectangle must have less than 1000 pixels.
     */
    public boolean medianSphere(ClearCLImage source, ClearCLImage destination, double radiusX, double radiusY) {
        return Kernels.medianSphere(clij, source, destination, new Double (radiusX).intValue(), new Double (radiusY).intValue());
    }

    /**
     * Multiplies all pairs of pixel values x and y from two image X and Y.
     * 
     * <pre>f(x, y) = x * y</pre>
     */
    public boolean multiplyImages(ClearCLImage factor1, ClearCLImage factor2, ClearCLImage destination) {
        return Kernels.multiplyImages(clij, factor1, factor2, destination);
    }

    /**
     * Multiplies all pairs of pixel values x and y from two image X and Y.
     * 
     * <pre>f(x, y) = x * y</pre>
     */
    public boolean multiplyImages(ClearCLBuffer factor1, ClearCLBuffer factor2, ClearCLBuffer destination) {
        return Kernels.multiplyImages(clij, factor1, factor2, destination);
    }

    /**
     * 
     */
    public boolean radialProjection(ClearCLImage arg1, ClearCLImage arg2, double arg3) {
        return Kernels.radialProjection(clij, arg1, arg2, new Double (arg3).floatValue());
    }

    /**
     * 
     */
    public boolean radialProjection(ClearCLBuffer arg1, ClearCLBuffer arg2, double arg3) {
        return Kernels.radialProjection(clij, arg1, arg2, new Double (arg3).floatValue());
    }

    /**
     * 
     */
    public boolean splitStack(ClearCLBuffer arg1, ClearCLBuffer[] arg2) {
        return Kernels.splitStack(clij, arg1, arg2);
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
    public boolean resliceTop(ClearCLImage source, ClearCLImage destination) {
        return Kernels.resliceTop(clij, source, destination);
    }

    /**
     * Flippes Y and Z axis of an image stack. This operation is similar to ImageJs 'Reslice [/]' method but
     * offers less flexibility such as interpolation.
     */
    public boolean resliceTop(ClearCLBuffer source, ClearCLBuffer destination) {
        return Kernels.resliceTop(clij, source, destination);
    }

    /**
     * Determines the sum of all pixels in a given image. It will be stored in a new row of ImageJs
     * Results table in the column 'Sum'.
     */
    public double sumPixels(ClearCLBuffer source) {
        return Kernels.sumPixels(clij, source);
    }

    /**
     * Determines the sum of all pixels in a given image. It will be stored in a new row of ImageJs
     * Results table in the column 'Sum'.
     */
    public double sumPixels(ClearCLImage source) {
        return Kernels.sumPixels(clij, source);
    }

    /**
     * Flippes X, Y and Z axis of an image stack. This operation is similar to ImageJs 'Reslice [/]' method 
     *  but offers less flexibility such as interpolation.
     */
    public boolean resliceLeft(ClearCLBuffer source, ClearCLBuffer destination) {
        return Kernels.resliceLeft(clij, source, destination);
    }

    /**
     * Flippes X, Y and Z axis of an image stack. This operation is similar to ImageJs 'Reslice [/]' method 
     *  but offers less flexibility such as interpolation.
     */
    public boolean resliceLeft(ClearCLImage source, ClearCLImage destination) {
        return Kernels.resliceLeft(clij, source, destination);
    }

    /**
     * Determines the sum projection of an image along Z.
     */
    public boolean sumZProjection(ClearCLBuffer source, ClearCLBuffer destination_sum) {
        return Kernels.sumZProjection(clij, source, destination_sum);
    }

    /**
     * Determines the sum projection of an image along Z.
     */
    public boolean sumZProjection(ClearCLImage source, ClearCLImage destination_sum) {
        return Kernels.sumZProjection(clij, source, destination_sum);
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
     * 
     */
    public boolean countNonZeroPixelsLocallySliceBySlice(ClearCLBuffer arg1, ClearCLBuffer arg2, double arg3, double arg4) {
        return Kernels.countNonZeroPixelsLocallySliceBySlice(clij, arg1, arg2, new Double (arg3).intValue(), new Double (arg4).intValue());
    }

    /**
     * 
     */
    public boolean countNonZeroPixelsLocallySliceBySlice(ClearCLImage arg1, ClearCLImage arg2, double arg3, double arg4) {
        return Kernels.countNonZeroPixelsLocallySliceBySlice(clij, arg1, arg2, new Double (arg3).intValue(), new Double (arg4).intValue());
    }

    /**
     * 
     */
    public boolean differenceOfGaussianSliceBySlice(ClearCLImage arg1, ClearCLImage arg2, double arg3, double arg4, double arg5) {
        return Kernels.differenceOfGaussianSliceBySlice(clij, arg1, arg2, new Double (arg3).intValue(), new Double (arg4).floatValue(), new Double (arg5).floatValue());
    }

    /**
     * Computes the local maximum of a pixels ellipsoidal 2D neighborhood in an image stack 
     * slice by slice. The ellipses size is specified by its half-width and half-height (radius).
     * 
     * This filter is applied slice by slice in 2D.
     */
    public boolean maximumSliceBySliceSphere(ClearCLBuffer source, ClearCLBuffer destination, double radiusX, double radiusY) {
        return Kernels.maximumSliceBySliceSphere(clij, source, destination, new Double (radiusX).intValue(), new Double (radiusY).intValue());
    }

    /**
     * Computes the local maximum of a pixels ellipsoidal 2D neighborhood in an image stack 
     * slice by slice. The ellipses size is specified by its half-width and half-height (radius).
     * 
     * This filter is applied slice by slice in 2D.
     */
    public boolean maximumSliceBySliceSphere(ClearCLImage source, ClearCLImage destination, double radiusX, double radiusY) {
        return Kernels.maximumSliceBySliceSphere(clij, source, destination, new Double (radiusX).intValue(), new Double (radiusY).intValue());
    }

    /**
     * Computes the maximum of a constant scalar s and each pixel value x in a given image X.
     * 
     * <pre>f(x, s) = max(x, s)</pre>
     */
    public boolean maximumImageAndScalar(ClearCLImage source, ClearCLImage destination, double scalar) {
        return Kernels.maximumImageAndScalar(clij, source, destination, new Double (scalar).floatValue());
    }

    /**
     * Computes the maximum of a constant scalar s and each pixel value x in a given image X.
     * 
     * <pre>f(x, s) = max(x, s)</pre>
     */
    public boolean maximumImageAndScalar(ClearCLBuffer source, ClearCLBuffer destination, double scalar) {
        return Kernels.maximumImageAndScalar(clij, source, destination, new Double (scalar).floatValue());
    }

    /**
     * 
     */
    public boolean countNonZeroPixelsLocally(ClearCLImage arg1, ClearCLImage arg2, double arg3, double arg4) {
        return Kernels.countNonZeroPixelsLocally(clij, arg1, arg2, new Double (arg3).intValue(), new Double (arg4).intValue());
    }

    /**
     * 
     */
    public boolean countNonZeroPixelsLocally(ClearCLBuffer arg1, ClearCLBuffer arg2, double arg3, double arg4) {
        return Kernels.countNonZeroPixelsLocally(clij, arg1, arg2, new Double (arg3).intValue(), new Double (arg4).intValue());
    }

    /**
     * Determines the maximum projection of an image stack along Z.
     * Furthermore, another 2D image is generated with pixels containing the z-index where the maximum was found (zero based).
     */
    public boolean argMaximumZProjection(ClearCLImage source, ClearCLImage destination_max, ClearCLImage destination_arg_max) {
        return Kernels.argMaximumZProjection(clij, source, destination_max, destination_arg_max);
    }

    /**
     * Determines the maximum projection of an image stack along Z.
     * Furthermore, another 2D image is generated with pixels containing the z-index where the maximum was found (zero based).
     */
    public boolean argMaximumZProjection(ClearCLBuffer source, ClearCLBuffer destination_max, ClearCLBuffer destination_arg_max) {
        return Kernels.argMaximumZProjection(clij, source, destination_max, destination_arg_max);
    }

    /**
     * Detects local minima in a given square neighborhood of an input image stack. The input image stack is 
     * processed slice by slice. Pixels in the resulting image are set to 1 if there is no other pixel in a 
     * given radius which has a lower intensity, and to 0 otherwise.
     */
    public boolean detectMinimaSliceBySliceBox(ClearCLBuffer source, ClearCLBuffer destination, double radius) {
        return Kernels.detectMinimaSliceBySliceBox(clij, source, destination, new Double (radius).intValue());
    }

    /**
     * Detects local minima in a given square neighborhood of an input image stack. The input image stack is 
     * processed slice by slice. Pixels in the resulting image are set to 1 if there is no other pixel in a 
     * given radius which has a lower intensity, and to 0 otherwise.
     */
    public boolean detectMinimaSliceBySliceBox(ClearCLImage source, ClearCLImage destination, double radius) {
        return Kernels.detectMinimaSliceBySliceBox(clij, source, destination, new Double (radius).intValue());
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
    public boolean affineTransform2D(ClearCLBuffer arg1, ClearCLBuffer arg2, AffineTransform2D arg3) {
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
    public boolean affineTransform2D(ClearCLImage arg1, ClearCLImage arg2, float[] arg3) {
        return Kernels.affineTransform2D(clij, arg1, arg2, arg3);
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
    public boolean convertToImageJBinary(ClearCLImage arg1, ClearCLImage arg2) {
        return Kernels.convertToImageJBinary(clij, arg1, arg2);
    }

    /**
     * Scales an image using scaling factors 0.5 for X and Y dimensions. The Z dimension stays untouched. Thus, each slice is processed separately.
     * The median method is applied. Thus, each pixel value in the destination image equals to the median of
     * four corresponding pixels in the source image.
     */
    public boolean downsampleSliceBySliceHalfMedian(ClearCLImage source, ClearCLImage destination) {
        return Kernels.downsampleSliceBySliceHalfMedian(clij, source, destination);
    }

    /**
     * Scales an image using scaling factors 0.5 for X and Y dimensions. The Z dimension stays untouched. Thus, each slice is processed separately.
     * The median method is applied. Thus, each pixel value in the destination image equals to the median of
     * four corresponding pixels in the source image.
     */
    public boolean downsampleSliceBySliceHalfMedian(ClearCLBuffer source, ClearCLBuffer destination) {
        return Kernels.downsampleSliceBySliceHalfMedian(clij, source, destination);
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
    public boolean affineTransform3D(ClearCLBuffer arg1, ClearCLBuffer arg2, float[] arg3) {
        return Kernels.affineTransform3D(clij, arg1, arg2, arg3);
    }

    /**
     * Computes a binary image with pixel values 0 and 1 containing the binary erosion of a given input image.
     * The erosion takes the von-Neumann-neighborhood (4 pixels in 2D and 6 pixels in 3d) into account.
     * The pixels in the input image with pixel value not equal to 0 will be interpreted as 1.
     * 
     * This filter is applied slice by slice in 2D.
     */
    public boolean erodeSphereSliceBySlice(ClearCLBuffer source, ClearCLBuffer destination) {
        return Kernels.erodeSphereSliceBySlice(clij, source, destination);
    }

    /**
     * Computes a binary image with pixel values 0 and 1 containing the binary erosion of a given input image.
     * The erosion takes the von-Neumann-neighborhood (4 pixels in 2D and 6 pixels in 3d) into account.
     * The pixels in the input image with pixel value not equal to 0 will be interpreted as 1.
     * 
     * This filter is applied slice by slice in 2D.
     */
    public boolean erodeSphereSliceBySlice(ClearCLImage source, ClearCLImage destination) {
        return Kernels.erodeSphereSliceBySlice(clij, source, destination);
    }

    /**
     * Computes the maximum of a constant scalar s and each pixel value x in a given image X.
     * 
     * <pre>f(x, s) = min(x, s)</pre>
     */
    public boolean minimumImageAndScalar(ClearCLImage source, ClearCLImage destination, double scalar) {
        return Kernels.minimumImageAndScalar(clij, source, destination, new Double (scalar).floatValue());
    }

    /**
     * Computes the maximum of a constant scalar s and each pixel value x in a given image X.
     * 
     * <pre>f(x, s) = min(x, s)</pre>
     */
    public boolean minimumImageAndScalar(ClearCLBuffer source, ClearCLBuffer destination, double scalar) {
        return Kernels.minimumImageAndScalar(clij, source, destination, new Double (scalar).floatValue());
    }

    /**
     * Determines the maximum projection of an image along Z.
     */
    public boolean maximumZProjection(ClearCLImage source, ClearCLImage destination_max) {
        return Kernels.maximumZProjection(clij, source, destination_max);
    }

    /**
     * Determines the maximum projection of an image along Z.
     */
    public boolean maximumZProjection(ClearCLBuffer source, ClearCLBuffer destination_max) {
        return Kernels.maximumZProjection(clij, source, destination_max);
    }

    /**
     * Adds a scalar value s to all pixels x of a given image X.
     * 
     * <pre>f(x, s) = x + s</pre>
     */
    public boolean addImageAndScalar(ClearCLImage source, ClearCLImage destination, double scalar) {
        return Kernels.addImageAndScalar(clij, source, destination, new Double (scalar).floatValue());
    }

    /**
     * Adds a scalar value s to all pixels x of a given image X.
     * 
     * <pre>f(x, s) = x + s</pre>
     */
    public boolean addImageAndScalar(ClearCLBuffer source, ClearCLBuffer destination, double scalar) {
        return Kernels.addImageAndScalar(clij, source, destination, new Double (scalar).floatValue());
    }

    /**
     * Computes a binary image with pixel values 0 and 1 containing the binary dilation of a given input image.
     * The dilation takes the von-Neumann-neighborhood (4 pixels in 2D and 6 pixels in 3d) into account.
     * The pixels in the input image with pixel value not equal to 0 will be interpreted as 1.
     * 
     * This filter is applied slice by slice in 2D.
     */
    public boolean dilateSphereSliceBySlice(ClearCLBuffer source, ClearCLBuffer destination) {
        return Kernels.dilateSphereSliceBySlice(clij, source, destination);
    }

    /**
     * Computes a binary image with pixel values 0 and 1 containing the binary dilation of a given input image.
     * The dilation takes the von-Neumann-neighborhood (4 pixels in 2D and 6 pixels in 3d) into account.
     * The pixels in the input image with pixel value not equal to 0 will be interpreted as 1.
     * 
     * This filter is applied slice by slice in 2D.
     */
    public boolean dilateSphereSliceBySlice(ClearCLImage source, ClearCLImage destination) {
        return Kernels.dilateSphereSliceBySlice(clij, source, destination);
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
     * The automatic thresholder utilizes the threshold methods from ImageJ on a histogram determined on 
     * the GPU to create binary images as similar as possible to ImageJ 'Apply Threshold' method. Enter one 
     * of these methods in the method text field:
     * [Default, Huang, Intermodes, IsoData, IJ_IsoData, Li, MaxEntropy, Mean, MinError, Minimum, Moments, Otsu, Percentile, RenyiEntropy, Shanbhag, Triangle, Yen]
     */
    public boolean automaticThreshold(ClearCLBuffer arg1, ClearCLBuffer arg2, String arg3, double arg4, double arg5, double arg6) {
        return Kernels.automaticThreshold(clij, arg1, arg2, arg3, new Double (arg4).floatValue(), new Double (arg5).floatValue(), new Double (arg6).intValue());
    }

    /**
     * 
     */
    public boolean countNonZeroVoxelsLocally(ClearCLBuffer arg1, ClearCLBuffer arg2, double arg3, double arg4, double arg5) {
        return Kernels.countNonZeroVoxelsLocally(clij, arg1, arg2, new Double (arg3).intValue(), new Double (arg4).intValue(), new Double (arg5).intValue());
    }

    /**
     * 
     */
    public boolean countNonZeroVoxelsLocally(ClearCLImage arg1, ClearCLImage arg2, double arg3, double arg4, double arg5) {
        return Kernels.countNonZeroVoxelsLocally(clij, arg1, arg2, new Double (arg3).intValue(), new Double (arg4).intValue(), new Double (arg5).intValue());
    }

    /**
     * 
     */
    public boolean detectOptimaSliceBySlice(ClearCLImage arg1, ClearCLImage arg2, double arg3, boolean arg4) {
        return Kernels.detectOptimaSliceBySlice(clij, arg1, arg2, new Double (arg3).intValue(), arg4);
    }

    /**
     * 
     */
    public boolean detectOptimaSliceBySlice(ClearCLBuffer arg1, ClearCLBuffer arg2, double arg3, boolean arg4) {
        return Kernels.detectOptimaSliceBySlice(clij, arg1, arg2, new Double (arg3).intValue(), arg4);
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
    public boolean dilateBoxSliceBySlice(ClearCLBuffer source, ClearCLBuffer destination) {
        return Kernels.dilateBoxSliceBySlice(clij, source, destination);
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
    public boolean dilateBoxSliceBySlice(ClearCLImage source, ClearCLImage destination) {
        return Kernels.dilateBoxSliceBySlice(clij, source, destination);
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
    public boolean erodeBoxSliceBySlice(ClearCLBuffer source, ClearCLBuffer destination) {
        return Kernels.erodeBoxSliceBySlice(clij, source, destination);
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
    public boolean erodeBoxSliceBySlice(ClearCLImage source, ClearCLImage destination) {
        return Kernels.erodeBoxSliceBySlice(clij, source, destination);
    }

    /**
     * Applies Gaussian blur to the input image twice with different sigma values resulting in two images which are then subtracted from each other.
     * 
     * It is recommended to apply this operation to images of type Float (32 bit) as results might be negative.
     */
    public boolean differenceOfGaussian(ClearCLImage arg1, ClearCLImage arg2, double arg3, double arg4, double arg5) {
        return Kernels.differenceOfGaussian(clij, arg1, arg2, new Double (arg3).intValue(), new Double (arg4).floatValue(), new Double (arg5).floatValue());
    }

    /**
     * Computes a masked image by applying a 2D mask to an image stack. All pixel values x of image X will be copied
     * to the destination image in case pixel value m at the same spatial position in the mask image is not equal to 
     * zero.
     * 
     * <pre>f(x,m) = (x if (m != 0); (0 otherwise))</pre>
     */
    public boolean maskStackWithPlane(ClearCLBuffer source, ClearCLBuffer mask, ClearCLBuffer destination) {
        return Kernels.maskStackWithPlane(clij, source, mask, destination);
    }

    /**
     * Computes a masked image by applying a 2D mask to an image stack. All pixel values x of image X will be copied
     * to the destination image in case pixel value m at the same spatial position in the mask image is not equal to 
     * zero.
     * 
     * <pre>f(x,m) = (x if (m != 0); (0 otherwise))</pre>
     */
    public boolean maskStackWithPlane(ClearCLImage source, ClearCLImage mask, ClearCLImage destination) {
        return Kernels.maskStackWithPlane(clij, source, mask, destination);
    }

    /**
     * Detects local maxima in a given square neighborhood of an input image stack. The input image stack is 
     * processed slice by slice. Pixels in the resulting image are set to 1 if there is no other pixel in a 
     * given radius which has a higher intensity, and to 0 otherwise.
     */
    public boolean detectMaximaSliceBySliceBox(ClearCLImage source, ClearCLImage destination, double radius) {
        return Kernels.detectMaximaSliceBySliceBox(clij, source, destination, new Double (radius).intValue());
    }

    /**
     * Detects local maxima in a given square neighborhood of an input image stack. The input image stack is 
     * processed slice by slice. Pixels in the resulting image are set to 1 if there is no other pixel in a 
     * given radius which has a higher intensity, and to 0 otherwise.
     */
    public boolean detectMaximaSliceBySliceBox(ClearCLBuffer source, ClearCLBuffer destination, double radius) {
        return Kernels.detectMaximaSliceBySliceBox(clij, source, destination, new Double (radius).intValue());
    }

    /**
     * Multiplies all pairs of pixel values x and y from an image stack X and a 2D image Y. x and y are at 
     * the same spatial position within a plane.
     * 
     * <pre>f(x, y) = x * y</pre>
     */
    public boolean multiplyStackWithPlane(ClearCLBuffer sourceStack, ClearCLBuffer sourcePlane, ClearCLBuffer destination) {
        return Kernels.multiplyStackWithPlane(clij, sourceStack, sourcePlane, destination);
    }

    /**
     * Multiplies all pairs of pixel values x and y from an image stack X and a 2D image Y. x and y are at 
     * the same spatial position within a plane.
     * 
     * <pre>f(x, y) = x * y</pre>
     */
    public boolean multiplyStackWithPlane(ClearCLImage sourceStack, ClearCLImage sourcePlane, ClearCLImage destination) {
        return Kernels.multiplyStackWithPlane(clij, sourceStack, sourcePlane, destination);
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
     * Multiplies all pixels value x in a given image X with a constant scalar s.
     * 
     * <pre>f(x, s) = x * s</pre>
     */
    public boolean multiplyImageAndScalar(ClearCLImage source, ClearCLImage destination, double scalar) {
        return Kernels.multiplyImageAndScalar(clij, source, destination, new Double (scalar).floatValue());
    }

    /**
     * Multiplies all pixels value x in a given image X with a constant scalar s.
     * 
     * <pre>f(x, s) = x * s</pre>
     */
    public boolean multiplyImageAndScalar(ClearCLBuffer source, ClearCLBuffer destination, double scalar) {
        return Kernels.multiplyImageAndScalar(clij, source, destination, new Double (scalar).floatValue());
    }

    /**
     * Computes the local mean average of a pixels ellipsoidal 2D neighborhood in an image stack 
     * slice by slice. The ellipses size is specified by its half-width and half-height (radius).
     * 
     * This filter is applied slice by slice in 2D.
     */
    public boolean meanSliceBySliceSphere(ClearCLBuffer source, ClearCLBuffer destination, double radiusX, double radiusY) {
        return Kernels.meanSliceBySliceSphere(clij, source, destination, new Double (radiusX).intValue(), new Double (radiusY).intValue());
    }

    /**
     * Computes the local mean average of a pixels ellipsoidal 2D neighborhood in an image stack 
     * slice by slice. The ellipses size is specified by its half-width and half-height (radius).
     * 
     * This filter is applied slice by slice in 2D.
     */
    public boolean meanSliceBySliceSphere(ClearCLImage source, ClearCLImage destination, double radiusX, double radiusY) {
        return Kernels.meanSliceBySliceSphere(clij, source, destination, new Double (radiusX).intValue(), new Double (radiusY).intValue());
    }

    /**
     * Determines the minimum of all pixels in a given image. It will be stored in a new row of ImageJs
     * Results table in the column 'Min'.
     */
    public double minimumOfAllPixels(ClearCLBuffer source) {
        return Kernels.minimumOfAllPixels(clij, source);
    }

    /**
     * Determines the minimum of all pixels in a given image. It will be stored in a new row of ImageJs
     * Results table in the column 'Min'.
     */
    public double minimumOfAllPixels(ClearCLImage source) {
        return Kernels.minimumOfAllPixels(clij, source);
    }

    /**
     * 
     */
    public boolean multiplyImageAndCoordinate(ClearCLImage arg1, ClearCLImage arg2, double arg3) {
        return Kernels.multiplyImageAndCoordinate(clij, arg1, arg2, new Double (arg3).intValue());
    }

    /**
     * 
     */
    public boolean multiplyImageAndCoordinate(ClearCLBuffer arg1, ClearCLBuffer arg2, double arg3) {
        return Kernels.multiplyImageAndCoordinate(clij, arg1, arg2, new Double (arg3).intValue());
    }

    /**
     * Determines the maximum projection of an image along a given dimension. Furthermore, the X and Y
     *  dimesions of the resulting image must be specified by the user according to its definition:
     * X = 0
     * Y = 1
     * Z = 2
     * 
     */
    public boolean maximumXYZProjection(ClearCLImage source, ClearCLImage destination_max, double dimensionX, double dimensionY, double projectedDimension) {
        return Kernels.maximumXYZProjection(clij, source, destination_max, new Double (dimensionX).intValue(), new Double (dimensionY).intValue(), new Double (projectedDimension).intValue());
    }

    /**
     * Determines the maximum projection of an image along a given dimension. Furthermore, the X and Y
     *  dimesions of the resulting image must be specified by the user according to its definition:
     * X = 0
     * Y = 1
     * Z = 2
     * 
     */
    public boolean maximumXYZProjection(ClearCLBuffer source, ClearCLBuffer destination_max, double dimensionX, double dimensionY, double projectedDimension) {
        return Kernels.maximumXYZProjection(clij, source, destination_max, new Double (dimensionX).intValue(), new Double (dimensionY).intValue(), new Double (projectedDimension).intValue());
    }

    /**
     * Computes the local minimum of a pixels ellipsoidal 2D neighborhood in an image stack 
     * slice by slice. The ellipses size is specified by its half-width and half-height (radius).
     * 
     * This filter is applied slice by slice in 2D.
     */
    public boolean minimumSliceBySliceSphere(ClearCLBuffer source, ClearCLBuffer destination, double radiusX, double radiusY) {
        return Kernels.minimumSliceBySliceSphere(clij, source, destination, new Double (radiusX).intValue(), new Double (radiusY).intValue());
    }

    /**
     * Computes the local minimum of a pixels ellipsoidal 2D neighborhood in an image stack 
     * slice by slice. The ellipses size is specified by its half-width and half-height (radius).
     * 
     * This filter is applied slice by slice in 2D.
     */
    public boolean minimumSliceBySliceSphere(ClearCLImage source, ClearCLImage destination, double radiusX, double radiusY) {
        return Kernels.minimumSliceBySliceSphere(clij, source, destination, new Double (radiusX).intValue(), new Double (radiusY).intValue());
    }

    /**
     * Determines the maximum of all pixels in a given image. It will be stored in a new row of ImageJs
     * Results table in the column 'Max'.
     */
    public double maximumOfAllPixels(ClearCLImage source) {
        return Kernels.maximumOfAllPixels(clij, source);
    }

    /**
     * Determines the maximum of all pixels in a given image. It will be stored in a new row of ImageJs
     * Results table in the column 'Max'.
     */
    public double maximumOfAllPixels(ClearCLBuffer source) {
        return Kernels.maximumOfAllPixels(clij, source);
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
    public double[] sumPixelsSliceBySlice(ClearCLImage arg1) {
        return Kernels.sumPixelsSliceBySlice(clij, arg1);
    }

    /**
     * Computes the local median of a pixels rectangular neighborhood. This is done slice-by-slice in a 3D 
     * image stack. The rectangle is specified by its half-width and half-height (radius).
     * 
     * For technical reasons, the area of the rectangle must have less than 1000 pixels.
     */
    public boolean medianSliceBySliceBox(ClearCLImage source, ClearCLImage destination, double radiusX, double radiusY) {
        return Kernels.medianSliceBySliceBox(clij, source, destination, new Double (radiusX).intValue(), new Double (radiusY).intValue());
    }

    /**
     * Computes the local median of a pixels rectangular neighborhood. This is done slice-by-slice in a 3D 
     * image stack. The rectangle is specified by its half-width and half-height (radius).
     * 
     * For technical reasons, the area of the rectangle must have less than 1000 pixels.
     */
    public boolean medianSliceBySliceBox(ClearCLBuffer source, ClearCLBuffer destination, double radiusX, double radiusY) {
        return Kernels.medianSliceBySliceBox(clij, source, destination, new Double (radiusX).intValue(), new Double (radiusY).intValue());
    }

    /**
     * Determines the minimum projection of an image along Z.
     */
    public boolean minimumZProjection(ClearCLBuffer source, ClearCLBuffer destination_sum) {
        return Kernels.minimumZProjection(clij, source, destination_sum);
    }

    /**
     * Determines the minimum projection of an image along Z.
     */
    public boolean minimumZProjection(ClearCLImage source, ClearCLImage destination_sum) {
        return Kernels.minimumZProjection(clij, source, destination_sum);
    }

    /**
     * Computes the local median of a pixels ellipsoidal neighborhood. This is done slice-by-slice in a 3D 
     * image stack. The ellipses size is specified by its half-width and half-height (radius).
     * 
     * For technical reasons, the area of the ellipse must have less than 1000 pixels.
     */
    public boolean medianSliceBySliceSphere(ClearCLImage source, ClearCLImage destination, double radiusX, double radiusY) {
        return Kernels.medianSliceBySliceSphere(clij, source, destination, new Double (radiusX).intValue(), new Double (radiusY).intValue());
    }

    /**
     * Computes the local median of a pixels ellipsoidal neighborhood. This is done slice-by-slice in a 3D 
     * image stack. The ellipses size is specified by its half-width and half-height (radius).
     * 
     * For technical reasons, the area of the ellipse must have less than 1000 pixels.
     */
    public boolean medianSliceBySliceSphere(ClearCLBuffer source, ClearCLBuffer destination, double radiusX, double radiusY) {
        return Kernels.medianSliceBySliceSphere(clij, source, destination, new Double (radiusX).intValue(), new Double (radiusY).intValue());
    }

    /**
     * 
     */
    public boolean tenengradWeightsSliceBySlice(ClearCLImage arg1, ClearCLImage arg2) {
        return Kernels.tenengradWeightsSliceBySlice(clij, arg1, arg2);
    }


    // net.haesleinhuepf.clijx.utilities.CLIJxAPIConsistencyWorkaround
    //----------------------------------------------------
    /**
     * DEPRECATED: CLIJ scale() is deprecated. Use scale2D or scale3D instead!
     */
    public boolean scale(ClearCLBuffer arg1, ClearCLBuffer arg2, double arg3, double arg4) {
        return CLIJxAPIConsistencyWorkaround.scale(clij, arg1, arg2, new Double (arg3).floatValue(), new Double (arg4).floatValue());
    }

    /**
     * DEPRECATED: CLIJ scale() is deprecated. Use scale2D or scale3D instead!
     */
    public boolean scale(ClearCLBuffer arg1, ClearCLBuffer arg2, double arg3, double arg4, double arg5) {
        return CLIJxAPIConsistencyWorkaround.scale(clij, arg1, arg2, new Double (arg3).floatValue(), new Double (arg4).floatValue(), new Double (arg5).floatValue());
    }

    /**
     * Computes the local mean average of a pixels cube neighborhood. The cubes size is specified by 
     * its half-width, half-height and half-depth (radius).
     */
    public boolean mean3DBox(ClearCLBuffer source, ClearCLBuffer destination, double radiusX, double radiusY, double radiusZ) {
        return CLIJxAPIConsistencyWorkaround.mean3DBox(clij, source, destination, new Double (radiusX).intValue(), new Double (radiusY).intValue(), new Double (radiusZ).intValue());
    }

    /**
     * Crops a given rectangle out of a given image.
     * 
     * Note: If the destination image pre-exists already, it will be overwritten and keep it's dimensions.
     */
    public boolean crop2D(ClearCLBuffer source, ClearCLBuffer destination, double startX, double startY, double width, double height) {
        return CLIJxAPIConsistencyWorkaround.crop2D(clij, source, destination, new Double (startX).intValue(), new Double (startY).intValue(), new Double (width).intValue(), new Double (height).intValue());
    }

    /**
     * Computes the local mean average of a pixels rectangular neighborhood. The rectangles size is specified by 
     * its half-width and half-height (radius).
     */
    public boolean mean2DBox(ClearCLBuffer source, ClearCLBuffer destination, double radiusX, double radiusY) {
        return CLIJxAPIConsistencyWorkaround.mean2DBox(clij, source, destination, new Double (radiusX).intValue(), new Double (radiusY).intValue());
    }

    /**
     * Computes the local minimum of a pixels spherical neighborhood. The spheres size is specified by 
     * its half-width, half-height and half-depth (radius).
     */
    public boolean minimum3DSphere(ClearCLBuffer source, ClearCLBuffer destination, double radiusX, double radiusY, double radiusZ) {
        return CLIJxAPIConsistencyWorkaround.minimum3DSphere(clij, source, destination, new Double (radiusX).intValue(), new Double (radiusY).intValue(), new Double (radiusZ).intValue());
    }

    /**
     * Computes the Gaussian blurred image of an image given two sigma values in X and Y. Thus, the filterkernel can have non-isotropic shape.
     * 
     * The implementation is done separable. In case a sigma equals zero, the direction is not blurred.
     */
    public boolean blur2D(ClearCLBuffer source, ClearCLBuffer destination, double sigmaX, double sigmaY) {
        return CLIJxAPIConsistencyWorkaround.blur2D(clij, source, destination, new Double (sigmaX).floatValue(), new Double (sigmaY).floatValue());
    }

    /**
     * Rotates an image in plane. All angles are entered in degrees. If the image is not rotated around 
     * the center, it is rotated around the coordinate origin.
     * 
     * It is recommended to apply the rotation to an isotropic image.
     */
    public boolean rotate2D(ClearCLBuffer arg1, ClearCLBuffer arg2, double arg3, double arg4, boolean arg5) {
        return CLIJxAPIConsistencyWorkaround.rotate2D(clij, arg1, arg2, new Double (arg3).floatValue(), new Double (arg4).floatValue(), arg5);
    }

    /**
     * Scales an image using given scaling factors for X and Y dimensions. The nearest-neighbor method
     * is applied. In ImageJ the method which is similar is called 'Interpolation method: none'.
     */
    public boolean downsample2D(ClearCLBuffer source, ClearCLBuffer destination, double factorX, double factorY) {
        return CLIJxAPIConsistencyWorkaround.downsample2D(clij, source, destination, new Double (factorX).floatValue(), new Double (factorY).floatValue());
    }

    /**
     * Scales an image using given scaling factors for X and Y dimensions. The nearest-neighbor method
     * is applied. In ImageJ the method which is similar is called 'Interpolation method: none'.
     */
    public boolean downsample3D(ClearCLBuffer source, ClearCLBuffer destination, double factorX, double factorY, double factorZ) {
        return CLIJxAPIConsistencyWorkaround.downsample3D(clij, source, destination, new Double (factorX).floatValue(), new Double (factorY).floatValue(), new Double (factorZ).floatValue());
    }

    /**
     * Computes the local median of a pixels ellipsoidal neighborhood. The ellipses size is specified by 
     * its half-width and half-height (radius).
     * 
     * For technical reasons, the area of the ellipse must have less than 1000 pixels.
     */
    public boolean median2DSphere(ClearCLBuffer source, ClearCLBuffer destination, double radiusX, double radiusY) {
        return CLIJxAPIConsistencyWorkaround.median2DSphere(clij, source, destination, new Double (radiusX).intValue(), new Double (radiusY).intValue());
    }

    /**
     * Computes the local mean average of a pixels spherical neighborhood. The spheres size is specified by 
     * its half-width, half-height and half-depth (radius).
     */
    public boolean mean3DSphere(ClearCLBuffer source, ClearCLBuffer destination, double radiusX, double radiusY, double radiusZ) {
        return CLIJxAPIConsistencyWorkaround.mean3DSphere(clij, source, destination, new Double (radiusX).intValue(), new Double (radiusY).intValue(), new Double (radiusZ).intValue());
    }

    /**
     * Computes the local minimum of a pixels ellipsoidal neighborhood. The ellipses size is specified by 
     * its half-width and half-height (radius).
     */
    public boolean minimum2DSphere(ClearCLBuffer source, ClearCLBuffer destination, double radiusX, double radiusY) {
        return CLIJxAPIConsistencyWorkaround.minimum2DSphere(clij, source, destination, new Double (radiusX).intValue(), new Double (radiusY).intValue());
    }

    /**
     * Computes the local mean average of a pixels ellipsoidal neighborhood. The ellipses size is specified by 
     * its half-width and half-height (radius).
     */
    public boolean mean2DSphere(ClearCLBuffer source, ClearCLBuffer destination, double radiusX, double radiusY) {
        return CLIJxAPIConsistencyWorkaround.mean2DSphere(clij, source, destination, new Double (radiusX).intValue(), new Double (radiusY).intValue());
    }

    /**
     * Computes the local median of a pixels cuboid neighborhood. The cuboid size is specified by 
     * its half-width, half-height and half-depth (radius).
     * 
     * For technical reasons, the volume of the cuboid must contain less than 1000 voxels.
     */
    public boolean median3DBox(ClearCLBuffer source, ClearCLBuffer destination, double radiusX, double radiusY, double radiusZ) {
        return CLIJxAPIConsistencyWorkaround.median3DBox(clij, source, destination, new Double (radiusX).intValue(), new Double (radiusY).intValue(), new Double (radiusZ).intValue());
    }

    /**
     * Translate an image stack in X and Y.
     */
    public boolean translate2D(ClearCLBuffer source, ClearCLBuffer destination, double translateX, double translateY) {
        return CLIJxAPIConsistencyWorkaround.translate2D(clij, source, destination, new Double (translateX).floatValue(), new Double (translateY).floatValue());
    }

    /**
     * Computes the local median of a pixels rectangular neighborhood. The rectangle is specified by 
     * its half-width and half-height (radius).
     * 
     * For technical reasons, the area of the rectangle must have less than 1000 pixels.
     */
    public boolean median2DBox(ClearCLBuffer source, ClearCLBuffer destination, double radiusX, double radiusY) {
        return CLIJxAPIConsistencyWorkaround.median2DBox(clij, source, destination, new Double (radiusX).intValue(), new Double (radiusY).intValue());
    }

    /**
     * Computes the local maximum of a pixels cube neighborhood. The cubes size is specified by 
     * its half-width, half-height and half-depth (radius).
     */
    public boolean maximum3DBox(ClearCLBuffer source, ClearCLBuffer destination, double radiusX, double radiusY, double radiusZ) {
        return CLIJxAPIConsistencyWorkaround.maximum3DBox(clij, source, destination, new Double (radiusX).intValue(), new Double (radiusY).intValue(), new Double (radiusZ).intValue());
    }

    /**
     * Crops a given sub-stack out of a given image stack.
     * 
     * Note: If the destination image pre-exists already, it will be overwritten and keep it's dimensions.
     */
    public boolean crop3D(ClearCLBuffer source, ClearCLBuffer destination, double startX, double startY, double startZ, double width, double height, double depth) {
        return CLIJxAPIConsistencyWorkaround.crop3D(clij, source, destination, new Double (startX).intValue(), new Double (startY).intValue(), new Double (startZ).intValue(), new Double (width).intValue(), new Double (height).intValue(), new Double (depth).intValue());
    }

    /**
     * Scales an image with a given factor.
     */
    public boolean scale3D(ClearCLBuffer arg1, ClearCLBuffer arg2, double arg3, double arg4, double arg5) {
        return CLIJxAPIConsistencyWorkaround.scale3D(clij, arg1, arg2, new Double (arg3).floatValue(), new Double (arg4).floatValue(), new Double (arg5).floatValue());
    }

    /**
     * Computes the local maximum of a pixels spherical neighborhood. The spheres size is specified by 
     * its half-width, half-height and half-depth (radius).
     */
    public boolean maximum3DSphere(ClearCLBuffer source, ClearCLBuffer destination, double radiusX, double radiusY, double radiusZ) {
        return CLIJxAPIConsistencyWorkaround.maximum3DSphere(clij, source, destination, new Double (radiusX).intValue(), new Double (radiusY).intValue(), new Double (radiusZ).intValue());
    }

    /**
     * Flips an image in X, Y and/or Z direction depending on boolean flags.
     */
    public boolean flip3D(ClearCLBuffer arg1, ClearCLBuffer arg2, boolean arg3, boolean arg4, boolean arg5) {
        return CLIJxAPIConsistencyWorkaround.flip3D(clij, arg1, arg2, arg3, arg4, arg5);
    }

    /**
     * Computes the local minimum of a pixels cube neighborhood. The cubes size is specified by 
     * its half-width, half-height and half-depth (radius).
     */
    public boolean minimum3DBox(ClearCLBuffer source, ClearCLBuffer destination, double radiusX, double radiusY, double radiusZ) {
        return CLIJxAPIConsistencyWorkaround.minimum3DBox(clij, source, destination, new Double (radiusX).intValue(), new Double (radiusY).intValue(), new Double (radiusZ).intValue());
    }

    /**
     * Flips an image in X and/or Y direction depending on boolean flags.
     */
    public boolean flip2D(ClearCLBuffer arg1, ClearCLBuffer arg2, boolean arg3, boolean arg4) {
        return CLIJxAPIConsistencyWorkaround.flip2D(clij, arg1, arg2, arg3, arg4);
    }

    /**
     * Computes the local maximum of a pixels rectangular neighborhood. The rectangles size is specified by 
     * its half-width and half-height (radius).
     */
    public boolean maximum2DBox(ClearCLBuffer source, ClearCLBuffer destination, double radiusX, double radiusY) {
        return CLIJxAPIConsistencyWorkaround.maximum2DBox(clij, source, destination, new Double (radiusX).intValue(), new Double (radiusY).intValue());
    }

    /**
     * Computes the Gaussian blurred image of an image given two sigma values in X, Y and Z. Thus, the filterkernel can have non-isotropic shape.
     * 
     * The implementation is done separable. In case a sigma equals zero, the direction is not blurred.
     */
    public boolean blur3D(ClearCLBuffer source, ClearCLBuffer destination, double sigmaX, double sigmaY, double sigmaZ) {
        return CLIJxAPIConsistencyWorkaround.blur3D(clij, source, destination, new Double (sigmaX).floatValue(), new Double (sigmaY).floatValue(), new Double (sigmaZ).floatValue());
    }

    /**
     * Rotates an image stack in 3D. All angles are entered in degrees. If the image is not rotated around 
     * the center, it is rotated around the coordinate origin.
     * 
     * It is recommended to apply the rotation to an isotropic image stack.
     */
    public boolean rotate3D(ClearCLBuffer arg1, ClearCLBuffer arg2, double arg3, double arg4, double arg5, boolean arg6) {
        return CLIJxAPIConsistencyWorkaround.rotate3D(clij, arg1, arg2, new Double (arg3).floatValue(), new Double (arg4).floatValue(), new Double (arg5).floatValue(), arg6);
    }

    /**
     * Computes the local minimum of a pixels rectangular neighborhood. The rectangles size is specified by 
     * its half-width and half-height (radius).
     */
    public boolean minimum2DBox(ClearCLBuffer source, ClearCLBuffer destination, double radiusX, double radiusY) {
        return CLIJxAPIConsistencyWorkaround.minimum2DBox(clij, source, destination, new Double (radiusX).intValue(), new Double (radiusY).intValue());
    }

    /**
     * Computes the local median of a pixels spherical neighborhood. The spheres size is specified by 
     * its half-width, half-height and half-depth (radius).
     * 
     * For technical reasons, the volume of the sphere must contain less than 1000 voxels.
     */
    public boolean median3DSphere(ClearCLBuffer source, ClearCLBuffer destination, double radiusX, double radiusY, double radiusZ) {
        return CLIJxAPIConsistencyWorkaround.median3DSphere(clij, source, destination, new Double (radiusX).intValue(), new Double (radiusY).intValue(), new Double (radiusZ).intValue());
    }

    /**
     * Translate an image stack in X, Y and Z.
     */
    public boolean translate3D(ClearCLBuffer source, ClearCLBuffer destination, double translateX, double translateY, double translateZ) {
        return CLIJxAPIConsistencyWorkaround.translate3D(clij, source, destination, new Double (translateX).floatValue(), new Double (translateY).floatValue(), new Double (translateZ).floatValue());
    }

    /**
     * Scales an image with a given factor.
     */
    public boolean scale2D(ClearCLBuffer arg1, ClearCLBuffer arg2, double arg3, double arg4) {
        return CLIJxAPIConsistencyWorkaround.scale2D(clij, arg1, arg2, new Double (arg3).floatValue(), new Double (arg4).floatValue());
    }

    /**
     * Computes the local maximum of a pixels ellipsoidal neighborhood. The ellipses size is specified by 
     * its half-width and half-height (radius).
     */
    public boolean maximum2DSphere(ClearCLBuffer source, ClearCLBuffer destination, double radiusX, double radiusY) {
        return CLIJxAPIConsistencyWorkaround.maximum2DSphere(clij, source, destination, new Double (radiusX).intValue(), new Double (radiusY).intValue());
    }

    /**
     * Counts non-zero pixels in a sphere around every pixel slice by slice in a stack and puts the resulting number in the destination image stack.
     */
    public boolean countNonZeroPixelsSliceBySliceSphere(ClearCLBuffer source, ClearCLBuffer destination, double radiusX, double radiusY) {
        return CLIJxAPIConsistencyWorkaround.countNonZeroPixelsSliceBySliceSphere(clij, source, destination, new Double (radiusX).intValue(), new Double (radiusY).intValue());
    }

    /**
     * Deforms an image according to distances provided in the given vector images. It is recommended to use 32-bit images for input, output and vector images. 
     */
    public boolean applyVectorField2D(ClearCLBuffer source, ClearCLBuffer vectorX, ClearCLBuffer vectorY, ClearCLBuffer destination) {
        return CLIJxAPIConsistencyWorkaround.applyVectorField2D(clij, source, vectorX, vectorY, destination);
    }

    /**
     * Counts non-zero pixels in a sphere around every pixel.Put the number in the result image.
     */
    public boolean countNonZeroPixels2DSphere(ClearCLBuffer source, ClearCLBuffer destination, double radiusX, double radiusY) {
        return CLIJxAPIConsistencyWorkaround.countNonZeroPixels2DSphere(clij, source, destination, new Double (radiusX).intValue(), new Double (radiusY).intValue());
    }

    /**
     * Image source, Image vectorX, Image vectorY, Image vectorZ, Image destination
     */
    public boolean applyVectorField3D(ClearCLBuffer arg1, ClearCLBuffer arg2, ClearCLBuffer arg3, ClearCLBuffer arg4, ClearCLBuffer arg5) {
        return CLIJxAPIConsistencyWorkaround.applyVectorField3D(clij, arg1, arg2, arg3, arg4, arg5);
    }

    /**
     * Computes the Gaussian blurred image of an image given two sigma values in X and Y. Thus, the filterkernel can have non-isotropic shape.
     * 
     * The Gaussian blur is applied slice by slice in 2D.
     */
    public boolean blur3DSliceBySlice(ClearCLBuffer source, ClearCLBuffer destination, double sigmaX, double sigmaY) {
        return CLIJxAPIConsistencyWorkaround.blur3DSliceBySlice(clij, source, destination, new Double (sigmaX).floatValue(), new Double (sigmaY).floatValue());
    }

    /**
     * Counts non-zero voxels in a sphere around every voxel.Put the number in the result image.
     */
    public boolean countNonZeroVoxels3DSphere(ClearCLBuffer source, ClearCLBuffer destination, double radiusX, double radiusY, double radiusZ) {
        return CLIJxAPIConsistencyWorkaround.countNonZeroVoxels3DSphere(clij, source, destination, new Double (radiusX).intValue(), new Double (radiusY).intValue(), new Double (radiusZ).intValue());
    }


    // net.haesleinhuepf.clijx.advancedfilters.BinaryUnion
    //----------------------------------------------------
    /**
     * Computes a binary image (containing pixel values 0 and 1) from two images X and Y by connecting pairs of
     * pixels x and y with the binary union operator |.
     * All pixel values except 0 in the input images are interpreted as 1.<pre>f(x, y) = x | y</pre>
     */
    public boolean binaryUnion(ClearCLBuffer operand1, ClearCLBuffer operand2, ClearCLBuffer destination) {
        return BinaryUnion.binaryUnion(clij, operand1, operand2, destination);
    }


    // net.haesleinhuepf.clijx.advancedfilters.BinaryIntersection
    //----------------------------------------------------
    /**
     * Computes a binary image (containing pixel values 0 and 1) from two images X and Y by connecting pairs of
     * pixels x and y with the binary intersection operator &.
     * All pixel values except 0 in the input images are interpreted as 1.
     * 
     * <pre>f(x, y) = x & y</pre>
     */
    public boolean binaryIntersection(ClearCLBuffer operand1, ClearCLBuffer operand2, ClearCLBuffer destination) {
        return BinaryIntersection.binaryIntersection(clij, operand1, operand2, destination);
    }


    // net.haesleinhuepf.clijx.advancedfilters.ConnectedComponentsLabeling
    //----------------------------------------------------
    /**
     * Performs connected components analysis to a binary image and generates a label map.
     */
    public boolean connectedComponentsLabeling(ClearCLBuffer binary_input, ClearCLBuffer labeling_destination) {
        return ConnectedComponentsLabeling.connectedComponentsLabeling(clijx, binary_input, labeling_destination);
    }

    /**
     * 
     */
    public boolean shiftIntensitiesToCloseGaps(ClearCLImageInterface arg1, ClearCLImageInterface arg2) {
        return ConnectedComponentsLabeling.shiftIntensitiesToCloseGaps(clijx, arg1, arg2);
    }

    /**
     * 
     */
    public boolean setNonZeroPixelsToPixelIndex(ClearCLImageInterface arg1, ClearCLImageInterface arg2) {
        return ConnectedComponentsLabeling.setNonZeroPixelsToPixelIndex(clij, arg1, arg2);
    }


    // net.haesleinhuepf.clijx.advancedfilters.CountNonZeroPixels
    //----------------------------------------------------
    /**
     * Determines the number of all pixels in a given image which are not equal to 0. It will be stored in a new row of ImageJs
     * Results table in the column 'CountNonZero'.
     */
    public double countNonZeroPixels(ClearCLBuffer source) {
        return CountNonZeroPixels.countNonZeroPixels(clij, source);
    }


    // net.haesleinhuepf.clijx.advancedfilters.CrossCorrelation
    //----------------------------------------------------
    /**
     * Performs cross correlation analysis between two images. The second image is shifted by deltaPos in the given dimension. The cross correlation coefficient is calculated for each pixel in a range around the given pixel with given radius in the given dimension. Together with the original images it is recommended to hand over mean filtered images using the same radius.  
     */
    public boolean crossCorrelation(ClearCLImage arg1, ClearCLImage arg2, ClearCLImage arg3, ClearCLImage arg4, ClearCLImage arg5, int arg6, int arg7, int arg8) {
        return CrossCorrelation.crossCorrelation(clij, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8);
    }

    /**
     * Performs cross correlation analysis between two images. The second image is shifted by deltaPos in the given dimension. The cross correlation coefficient is calculated for each pixel in a range around the given pixel with given radius in the given dimension. Together with the original images it is recommended to hand over mean filtered images using the same radius.  
     */
    public boolean crossCorrelation(ClearCLBuffer arg1, ClearCLBuffer arg2, ClearCLBuffer arg3, ClearCLBuffer arg4, ClearCLBuffer arg5, int arg6, int arg7, int arg8) {
        return CrossCorrelation.crossCorrelation(clij, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8);
    }


    // net.haesleinhuepf.clijx.advancedfilters.DifferenceOfGaussian2D
    //----------------------------------------------------
    /**
     * Applies Gaussian blur to the input image twice with different sigma values resulting in two images which are then subtracted from each other.
     * 
     * It is recommended to apply this operation to images of type Float (32 bit) as results might be negative.
     */
    public boolean differenceOfGaussian(ClearCLBuffer input, ClearCLBuffer destination, double sigma1x, double sigma1y, double sigma2x, double sigma2y) {
        return DifferenceOfGaussian2D.differenceOfGaussian(clij, input, destination, new Double (sigma1x).floatValue(), new Double (sigma1y).floatValue(), new Double (sigma2x).floatValue(), new Double (sigma2y).floatValue());
    }

    /**
     * Applies Gaussian blur to the input image twice with different sigma values resulting in two images which are then subtracted from each other.
     * 
     * It is recommended to apply this operation to images of type Float (32 bit) as results might be negative.
     */
    public boolean differenceOfGaussian2D(ClearCLBuffer input, ClearCLBuffer destination, double sigma1x, double sigma1y, double sigma2x, double sigma2y) {
        return DifferenceOfGaussian2D.differenceOfGaussian2D(clij, input, destination, new Double (sigma1x).floatValue(), new Double (sigma1y).floatValue(), new Double (sigma2x).floatValue(), new Double (sigma2y).floatValue());
    }


    // net.haesleinhuepf.clijx.advancedfilters.DifferenceOfGaussian3D
    //----------------------------------------------------
    /**
     * Applies Gaussian blur to the input image twice with different sigma values resulting in two images which are then subtracted from each other.
     * 
     * It is recommended to apply this operation to images of type Float (32 bit) as results might be negative.
     */
    public boolean differenceOfGaussian(ClearCLBuffer arg1, ClearCLBuffer arg2, double arg3, double arg4, double arg5, double arg6, double arg7, double arg8) {
        return DifferenceOfGaussian3D.differenceOfGaussian(clij, arg1, arg2, new Double (arg3).floatValue(), new Double (arg4).floatValue(), new Double (arg5).floatValue(), new Double (arg6).floatValue(), new Double (arg7).floatValue(), new Double (arg8).floatValue());
    }

    /**
     * Applies Gaussian blur to the input image twice with different sigma values resulting in two images which are then subtracted from each other.
     * 
     * It is recommended to apply this operation to images of type Float (32 bit) as results might be negative.
     */
    public boolean differenceOfGaussian3D(ClearCLBuffer input, ClearCLBuffer destination, double sigma1x, double sigma1y, double sigma1z, double sigma2x, double sigma2y, double sigma2z) {
        return DifferenceOfGaussian3D.differenceOfGaussian3D(clij, input, destination, new Double (sigma1x).floatValue(), new Double (sigma1y).floatValue(), new Double (sigma1z).floatValue(), new Double (sigma2x).floatValue(), new Double (sigma2y).floatValue(), new Double (sigma2z).floatValue());
    }


    // net.haesleinhuepf.clijx.advancedfilters.Extrema
    //----------------------------------------------------
    /**
     * Returns an image with pixel values most distant from 0: f(x, y) = x if abs(x) > abs(y), y else.
     */
    public boolean extrema(ClearCLBuffer input1, ClearCLBuffer input2, ClearCLBuffer destination) {
        return Extrema.extrema(clij, input1, input2, destination);
    }


    // net.haesleinhuepf.clijx.advancedfilters.LocalExtremaBox
    //----------------------------------------------------
    /**
     * Applies a local minimum and maximum filter. Afterwards, the value is returned which is more far from zero.
     */
    public boolean localExtremaBox(ClearCLBuffer input, ClearCLBuffer destination, double radiusX, double radiusY, double radiusZ) {
        return LocalExtremaBox.localExtremaBox(clij, input, destination, new Double (radiusX).intValue(), new Double (radiusY).intValue(), new Double (radiusZ).intValue());
    }


    // net.haesleinhuepf.clijx.advancedfilters.LocalID
    //----------------------------------------------------
    /**
     * local id
     */
    public boolean localID(ClearCLBuffer input, ClearCLBuffer destination) {
        return LocalID.localID(clij, input, destination);
    }


    // net.haesleinhuepf.clijx.advancedfilters.MaskLabel
    //----------------------------------------------------
    /**
     * Computes a masked image by applying a label mask to an image. All pixel values x of image X will be copied
     * to the destination image in case pixel value m at the same position in the label_map image has the right index value i.
     * 
     * f(x,m,i) = (x if (m == i); (0 otherwise))
     */
    public boolean maskLabel(ClearCLBuffer source, ClearCLBuffer label_map, ClearCLBuffer destination, double label_index) {
        return MaskLabel.maskLabel(clij, source, label_map, destination, new Double (label_index).floatValue());
    }


    // net.haesleinhuepf.clijx.advancedfilters.MeanClosestSpotDistance
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
    public double[] meanClosestSpotDistances(ClearCLBuffer arg1, ClearCLBuffer arg2, boolean arg3) {
        return MeanClosestSpotDistance.meanClosestSpotDistances(clij, arg1, arg2, arg3);
    }


    // net.haesleinhuepf.clijx.advancedfilters.MeanSquaredError
    //----------------------------------------------------
    /**
     * Determines the mean squared error (MSE) between two images. The MSE will be stored in a new row of ImageJs
     * Results table in the column 'MSE'.
     */
    public double meanSquaredError(ClearCLBuffer source1, ClearCLBuffer source2) {
        return MeanSquaredError.meanSquaredError(clij, source1, source2);
    }


    // net.haesleinhuepf.clijx.advancedfilters.MedianZProjection
    //----------------------------------------------------
    /**
     * Determines the median projection of an image along Z.
     */
    public boolean medianZProjection(ClearCLBuffer source, ClearCLBuffer destination) {
        return MedianZProjection.medianZProjection(clij, source, destination);
    }

    /**
     * Determines the median projection of an image along Z.
     */
    public boolean medianZProjection(ClearCLImage source, ClearCLImage destination) {
        return MedianZProjection.medianZProjection(clij, source, destination);
    }


    // net.haesleinhuepf.clijx.advancedfilters.NonzeroMinimumDiamond
    //----------------------------------------------------
    /**
     * Apply a minimum-sphere filter to the input image. The radius is fixed to 1 and pixels with value 0 are ignored.
     */
    public boolean nonzeroMinimumDiamond(ClearCLImageInterface arg1, ClearCLImageInterface arg2, ClearCLImageInterface arg3) {
        return NonzeroMinimumDiamond.nonzeroMinimumDiamond(clij, arg1, arg2, arg3);
    }

    /**
     * Apply a minimum-sphere filter to the input image. The radius is fixed to 1 and pixels with value 0 are ignored.
     */
    public boolean nonzeroMinimumDiamond(ClearCLImageInterface arg1, ClearCLBuffer arg2, ClearCLImageInterface arg3) {
        return NonzeroMinimumDiamond.nonzeroMinimumDiamond(clijx, arg1, arg2, arg3);
    }

    /**
     * Apply a minimum-sphere filter to the input image. The radius is fixed to 1 and pixels with value 0 are ignored.
     */
    public ClearCLKernel nonzeroMinimumDiamond(ClearCLImageInterface arg1, ClearCLBuffer arg2, ClearCLImageInterface arg3, ClearCLKernel arg4) {
        return NonzeroMinimumDiamond.nonzeroMinimumDiamond(clijx, arg1, arg2, arg3, arg4);
    }


    // net.haesleinhuepf.clijx.advancedfilters.Paste2D
    //----------------------------------------------------
    /**
     * Pastes an image into another image at a given position.
     */
    public boolean paste2D(ClearCLBuffer source, ClearCLBuffer destination, double destinationX, double destinationY) {
        return Paste2D.paste2D(clij, source, destination, new Double (destinationX).intValue(), new Double (destinationY).intValue());
    }

    /**
     * Pastes an image into another image at a given position.
     */
    public boolean paste(ClearCLImage source, ClearCLImage destination, double destinationX, double destinationY) {
        return Paste2D.paste(clij, source, destination, new Double (destinationX).intValue(), new Double (destinationY).intValue());
    }

    /**
     * Pastes an image into another image at a given position.
     */
    public boolean paste(ClearCLBuffer source, ClearCLBuffer destination, double destinationX, double destinationY) {
        return Paste2D.paste(clij, source, destination, new Double (destinationX).intValue(), new Double (destinationY).intValue());
    }


    // net.haesleinhuepf.clijx.advancedfilters.Paste3D
    //----------------------------------------------------
    /**
     * Pastes an image into another image at a given position.
     */
    public boolean paste3D(ClearCLBuffer source, ClearCLBuffer destination, double destinationX, double destinationY, double destinationZ) {
        return Paste3D.paste3D(clij, source, destination, new Double (destinationX).intValue(), new Double (destinationY).intValue(), new Double (destinationZ).intValue());
    }

    /**
     * Pastes an image into another image at a given position.
     */
    public boolean paste(ClearCLImage arg1, ClearCLImage arg2, double arg3, double arg4, double arg5) {
        return Paste3D.paste(clij, arg1, arg2, new Double (arg3).intValue(), new Double (arg4).intValue(), new Double (arg5).intValue());
    }

    /**
     * Pastes an image into another image at a given position.
     */
    public boolean paste(ClearCLBuffer arg1, ClearCLBuffer arg2, double arg3, double arg4, double arg5) {
        return Paste3D.paste(clij, arg1, arg2, new Double (arg3).intValue(), new Double (arg4).intValue(), new Double (arg5).intValue());
    }


    // net.haesleinhuepf.clijx.advancedfilters.Presign
    //----------------------------------------------------
    /**
     * Determines the extrema of pixel values: f(x) = x / abs(x).
     */
    public boolean presign(ClearCLBuffer input, ClearCLBuffer destination) {
        return Presign.presign(clij, input, destination);
    }


    // net.haesleinhuepf.clijx.advancedfilters.JaccardIndex
    //----------------------------------------------------
    /**
     * Determines the overlap of two binary images using the Jaccard index.
     * A value of 0 suggests no overlap, 1 means perfect overlap.
     * The resulting Jaccard index is saved to the results table in the 'Jaccard_Index' column.
     * Note that the Sorensen-Dice coefficient can be calculated from the Jaccard index j using this formula:
     * <pre>s = f(j) = 2 j / (j + 1)</pre>
     */
    public double jaccardIndex(ClearCLBuffer source1, ClearCLBuffer source2) {
        return JaccardIndex.jaccardIndex(clij, source1, source2);
    }


    // net.haesleinhuepf.clijx.advancedfilters.SorensenDiceCoefficent
    //----------------------------------------------------
    /**
     * Determines the overlap of two binary images using the Sorensen-Dice coefficent.
     * A value of 0 suggests no overlap, 1 means perfect overlap.
     * The Sorensen-Dice coefficient is saved in the colum 'Sorensen_Dice_coefficient'.
     * Note that the Sorensen-Dice coefficient s can be calculated from the Jaccard index j using this formula:
     * <pre>s = f(j) = 2 j / (j + 1)</pre>
     */
    public double sorensenDiceCoefficient(ClearCLBuffer source1, ClearCLBuffer source2) {
        return SorensenDiceCoefficent.sorensenDiceCoefficient(clij, source1, source2);
    }


    // net.haesleinhuepf.clijx.advancedfilters.StandardDeviationZProjection
    //----------------------------------------------------
    /**
     * Determines the standard deviation projection of an image along Z.
     */
    public boolean standardDeviationZProjection(ClearCLImage source, ClearCLImage destination) {
        return StandardDeviationZProjection.standardDeviationZProjection(clij, source, destination);
    }

    /**
     * Determines the standard deviation projection of an image along Z.
     */
    public boolean standardDeviationZProjection(ClearCLBuffer source, ClearCLBuffer destination) {
        return StandardDeviationZProjection.standardDeviationZProjection(clij, source, destination);
    }


    // net.haesleinhuepf.clijx.advancedfilters.StackToTiles
    //----------------------------------------------------
    /**
     * Stack to tiles.
     */
    public boolean stackToTiles(ClearCLImage source, ClearCLImage destination, double tiles_x, double tiles_y) {
        return StackToTiles.stackToTiles(clij, source, destination, new Double (tiles_x).intValue(), new Double (tiles_y).intValue());
    }

    /**
     * Stack to tiles.
     */
    public boolean stackToTiles(ClearCLBuffer source, ClearCLBuffer destination, double tiles_x, double tiles_y) {
        return StackToTiles.stackToTiles(clij, source, destination, new Double (tiles_x).intValue(), new Double (tiles_y).intValue());
    }


    // net.haesleinhuepf.clijx.advancedfilters.SubtractBackground2D
    //----------------------------------------------------
    /**
     * Applies Gaussian blur to the input image and subtracts the result from the original image.
     */
    public boolean subtractBackground2D(ClearCLBuffer input, ClearCLBuffer destination, double sigmaX, double sigmaY) {
        return SubtractBackground2D.subtractBackground2D(clij, input, destination, new Double (sigmaX).floatValue(), new Double (sigmaY).floatValue());
    }

    /**
     * Applies Gaussian blur to the input image and subtracts the result from the original image.
     */
    public boolean subtractBackground(ClearCLBuffer input, ClearCLBuffer destination, double sigmaX, double sigmaY) {
        return SubtractBackground2D.subtractBackground(clij, input, destination, new Double (sigmaX).floatValue(), new Double (sigmaY).floatValue());
    }


    // net.haesleinhuepf.clijx.advancedfilters.SubtractBackground3D
    //----------------------------------------------------
    /**
     * Applies Gaussian blur to the input image and subtracts the result from the original image.
     */
    public boolean subtractBackground(ClearCLBuffer arg1, ClearCLBuffer arg2, double arg3, double arg4, double arg5) {
        return SubtractBackground3D.subtractBackground(clij, arg1, arg2, new Double (arg3).floatValue(), new Double (arg4).floatValue(), new Double (arg5).floatValue());
    }

    /**
     * Applies Gaussian blur to the input image and subtracts the result from the original image.
     */
    public boolean subtractBackground3D(ClearCLBuffer input, ClearCLBuffer destination, double sigmaX, double sigmaY, double sigmaZ) {
        return SubtractBackground3D.subtractBackground3D(clij, input, destination, new Double (sigmaX).floatValue(), new Double (sigmaY).floatValue(), new Double (sigmaZ).floatValue());
    }


    // net.haesleinhuepf.clijx.advancedfilters.TopHatBox
    //----------------------------------------------------
    /**
     * Applies a top-hat filter for background subtraction to the input image.
     */
    public boolean topHatBox(ClearCLBuffer input, ClearCLBuffer destination, double radiusX, double radiusY, double radiusZ) {
        return TopHatBox.topHatBox(clij, input, destination, new Double (radiusX).intValue(), new Double (radiusY).intValue(), new Double (radiusZ).intValue());
    }


    // net.haesleinhuepf.clijx.advancedfilters.TopHatSphere
    //----------------------------------------------------
    /**
     * Applies a top-hat filter for background subtraction to the input image.
     */
    public boolean topHatSphere(ClearCLBuffer input, ClearCLBuffer destination, double radiusX, double radiusY, double radiusZ) {
        return TopHatSphere.topHatSphere(clij, input, destination, new Double (radiusX).intValue(), new Double (radiusY).intValue(), new Double (radiusZ).intValue());
    }


    // net.haesleinhuepf.clijx.advancedmath.Exponential
    //----------------------------------------------------
    /**
     * Computes base exponential of all pixels values.
     * 
     * f(x) = exp(x)
     */
    public boolean exponential(ClearCLImage source, ClearCLImage destination) {
        return Exponential.exponential(clij, source, destination);
    }

    /**
     * Computes base exponential of all pixels values.
     * 
     * f(x) = exp(x)
     */
    public boolean exponential(ClearCLBuffer source, ClearCLBuffer destination) {
        return Exponential.exponential(clij, source, destination);
    }


    // net.haesleinhuepf.clijx.advancedmath.Logarithm
    //----------------------------------------------------
    /**
     * Computes baseE logarithm of all pixels values.
     * 
     * f(x) = logarithm(x)
     */
    public boolean logarithm(ClearCLImage source, ClearCLImage destination) {
        return Logarithm.logarithm(clij, source, destination);
    }

    /**
     * Computes baseE logarithm of all pixels values.
     * 
     * f(x) = logarithm(x)
     */
    public boolean logarithm(ClearCLBuffer source, ClearCLBuffer destination) {
        return Logarithm.logarithm(clij, source, destination);
    }


    // net.haesleinhuepf.clijx.matrix.GenerateDistanceMatrix
    //----------------------------------------------------
    /**
     * Takes two images containing coordinates and builds up a matrix containing distance between the points. Convention: image width represents number of points, height represents dimensionality (2D, 3D, ... 10D). The result image has width the first input image and height equals to the width of the second input image.
     */
    public boolean generateDistanceMatrix(ClearCLBuffer coordinate_list1, ClearCLBuffer coordinate_list2, ClearCLBuffer distance_matrix_destination) {
        return GenerateDistanceMatrix.generateDistanceMatrix(clij, coordinate_list1, coordinate_list2, distance_matrix_destination);
    }


    // net.haesleinhuepf.clijx.matrix.ShortestDistances
    //----------------------------------------------------
    /**
     * Determine the shortest distance from a distance matrix. This corresponds to the minimum in a matrix for each individial column.
     */
    public boolean shortestDistances(ClearCLBuffer distance_matrix, ClearCLBuffer destination_minimum_distances) {
        return ShortestDistances.shortestDistances(clij, distance_matrix, destination_minimum_distances);
    }


    // net.haesleinhuepf.clijx.matrix.SpotsToPointList
    //----------------------------------------------------
    /**
     * Transforms a spots image as resulting from maximum/minimum detection in an image where every column cotains d 
     * pixels (with d = dimensionality of the original image) with the coordinates of the maxima/minima.
     */
    public boolean spotsToPointList(ClearCLBuffer input_spots, ClearCLBuffer destination_pointlist) {
        return SpotsToPointList.spotsToPointList(clij, input_spots, destination_pointlist);
    }


    // net.haesleinhuepf.clijx.matrix.TransposeXY
    //----------------------------------------------------
    /**
     * Transpose X and Y axes of an image.
     */
    public boolean transposeXY(ClearCLBuffer input, ClearCLBuffer destination) {
        return TransposeXY.transposeXY(clij, input, destination);
    }


    // net.haesleinhuepf.clijx.matrix.TransposeXZ
    //----------------------------------------------------
    /**
     * Transpose X and Z axes of an image.
     */
    public boolean transposeXZ(ClearCLBuffer input, ClearCLBuffer destination) {
        return TransposeXZ.transposeXZ(clij, input, destination);
    }


    // net.haesleinhuepf.clijx.matrix.TransposeYZ
    //----------------------------------------------------
    /**
     * Transpose Y and Z axes of an image.
     */
    public boolean transposeYZ(ClearCLBuffer input, ClearCLBuffer destination) {
        return TransposeYZ.transposeYZ(clij, input, destination);
    }


    // net.haesleinhuepf.clijx.piv.FastParticleImageVelocimetry
    //----------------------------------------------------
    /**
     * 
     */
    public boolean particleImageVelocimetry2D(ClearCLImage arg1, ClearCLImage arg2, ClearCLImage arg3, ClearCLImage arg4, double arg5) {
        return FastParticleImageVelocimetry.particleImageVelocimetry2D(clij, arg1, arg2, arg3, arg4, new Double (arg5).intValue());
    }

    /**
     * 
     */
    public boolean particleImageVelocimetry2D(ClearCLBuffer arg1, ClearCLBuffer arg2, ClearCLBuffer arg3, ClearCLBuffer arg4, double arg5) {
        return FastParticleImageVelocimetry.particleImageVelocimetry2D(clij, arg1, arg2, arg3, arg4, new Double (arg5).intValue());
    }


    // net.haesleinhuepf.clijx.piv.ParticleImageVelocimetry
    //----------------------------------------------------
    /**
     * For every pixel in source image 1, determine the pixel with the most similar intensity in 
     *  the local neighborhood with a given radius in source image 2. Write the distance in 
     * X and Y in the two corresponding destination images.
     */
    public boolean particleImageVelocimetry(ClearCLBuffer arg1, ClearCLBuffer arg2, ClearCLBuffer arg3, ClearCLBuffer arg4, ClearCLBuffer arg5, double arg6, double arg7, double arg8, boolean arg9) {
        return ParticleImageVelocimetry.particleImageVelocimetry(clij, arg1, arg2, arg3, arg4, arg5, new Double (arg6).intValue(), new Double (arg7).intValue(), new Double (arg8).intValue(), arg9);
    }


    // net.haesleinhuepf.clijx.piv.ParticleImageVelocimetryTimelapse
    //----------------------------------------------------
    /**
     * Run particle image velocimetry on a 2D+t timelapse.
     */
    public boolean particleImageVelocimetryTimelapse(ClearCLBuffer arg1, ClearCLBuffer arg2, ClearCLBuffer arg3, ClearCLBuffer arg4, int arg5, int arg6, int arg7, boolean arg8) {
        return ParticleImageVelocimetryTimelapse.particleImageVelocimetryTimelapse(clij, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8);
    }


    // net.haesleinhuepf.clijx.registration.DeformableRegistration2D
    //----------------------------------------------------
    /**
     * Applies particle image velocimetry to two images and registers them afterwards by warping input image 2 with a smoothed vector field.
     */
    public boolean deformableRegistration2D(ClearCLBuffer arg1, ClearCLBuffer arg2, ClearCLBuffer arg3, int arg4, int arg5) {
        return DeformableRegistration2D.deformableRegistration2D(clij, arg1, arg2, arg3, arg4, arg5);
    }


    // net.haesleinhuepf.clijx.registration.TranslationRegistration
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
    public boolean translationRegistration(ClearCLBuffer input1, ClearCLBuffer input2, ClearCLBuffer destination) {
        return TranslationRegistration.translationRegistration(clij, input1, input2, destination);
    }


    // net.haesleinhuepf.clijx.registration.TranslationTimelapseRegistration
    //----------------------------------------------------
    /**
     * Applies 2D translation registration to every pair of t, t+1 slices of a 2D+t image stack.
     */
    public boolean translationTimelapseRegistration(ClearCLBuffer input, ClearCLBuffer output) {
        return TranslationTimelapseRegistration.translationTimelapseRegistration(clij, input, output);
    }


    // net.haesleinhuepf.clijx.advancedfilters.SetWhereXequalsY
    //----------------------------------------------------
    /**
     * Sets all pixel values a of a given image A to a constant value v in case its coordinates x == y. Otherwise the pixel is not overwritten.
     * If you want to initialize an identity transfrom matrix, set all pixels to 0 first.
     * 
     * <pre>f(a) = v</pre>
     */
    public boolean setWhereXequalsY(ClearCLImage source, double value) {
        return SetWhereXequalsY.setWhereXequalsY(clij, source, new Double (value).floatValue());
    }

    /**
     * Sets all pixel values a of a given image A to a constant value v in case its coordinates x == y. Otherwise the pixel is not overwritten.
     * If you want to initialize an identity transfrom matrix, set all pixels to 0 first.
     * 
     * <pre>f(a) = v</pre>
     */
    public boolean setWhereXequalsY(ClearCLBuffer source, double value) {
        return SetWhereXequalsY.setWhereXequalsY(clij, source, new Double (value).floatValue());
    }


    // net.haesleinhuepf.clijx.advancedfilters.LaplaceSphere
    //----------------------------------------------------
    /**
     * Applies the Laplace operator (Diamond neighborhood) to an image.
     */
    public boolean laplaceSphere(ClearCLBuffer input, ClearCLBuffer destination) {
        return LaplaceSphere.laplaceSphere(clij, input, destination);
    }


    // net.haesleinhuepf.clijx.advancedfilters.Image2DToResultsTable
    //----------------------------------------------------
    /**
     * Converts an image into a table.
     */
    public ResultsTable image2DToResultsTable(ClearCLImage arg1, ResultsTable arg2) {
        return Image2DToResultsTable.image2DToResultsTable(clij, arg1, arg2);
    }

    /**
     * Converts an image into a table.
     */
    public ResultsTable image2DToResultsTable(ClearCLBuffer arg1, ResultsTable arg2) {
        return Image2DToResultsTable.image2DToResultsTable(clij, arg1, arg2);
    }


    // net.haesleinhuepf.clijx.advancedfilters.WriteValuesToPositions
    //----------------------------------------------------
    /**
     * Takes an image with three/four rows (2D: height = 3; 3D: height = 4): x, y [, z] and v and target image. The value v will be written at position x/y[/z] in the target image.
     */
    public boolean writeValuesToPositions(ClearCLBuffer positionsAndValues, ClearCLBuffer destination) {
        return WriteValuesToPositions.writeValuesToPositions(clij, positionsAndValues, destination);
    }


    // net.haesleinhuepf.clijx.advancedfilters.GetSize
    //----------------------------------------------------
    /**
     * Reads out the size of an image [stack] and writes it to the results table in the columns 'Width', 'Height' and 'Depth'.
     */
    public long[] getSize(ClearCLBuffer source) {
        return GetSize.getSize(clij, source);
    }


    // net.haesleinhuepf.clijx.matrix.MultiplyMatrix
    //----------------------------------------------------
    /**
     * Multiplies two matrices with each other.
     */
    public boolean multiplyMatrix(ClearCLBuffer matrix1, ClearCLBuffer matrix2, ClearCLBuffer matrix_destination) {
        return MultiplyMatrix.multiplyMatrix(clij, matrix1, matrix2, matrix_destination);
    }


    // net.haesleinhuepf.clijx.matrix.MatrixEqual
    //----------------------------------------------------
    /**
     * Checks if all elements of a matrix are different by less than or equal to a given tolerance.
     * The result will be put in the results table as 1 if yes and 0 otherwise.
     */
    public boolean matrixEqual(ClearCLBuffer arg1, ClearCLBuffer arg2, double arg3) {
        return MatrixEqual.matrixEqual(clij, arg1, arg2, new Double (arg3).floatValue());
    }


    // net.haesleinhuepf.clijx.advancedfilters.PowerImages
    //----------------------------------------------------
    /**
     * Calculates x to the power of y pixel wise of two images X and Y.
     */
    public boolean powerImages(ClearCLBuffer input, ClearCLBuffer exponent, ClearCLBuffer destination) {
        return PowerImages.powerImages(clij, input, exponent, destination);
    }


    // net.haesleinhuepf.clijx.advancedmath.Equal
    //----------------------------------------------------
    /**
     * Determines if two images A and B equal pixel wise.
     * 
     * f(a, b) = 1 if a == b; 0 otherwise. 
     */
    public boolean equal(ClearCLBuffer source1, ClearCLBuffer source2, ClearCLBuffer destination) {
        return Equal.equal(clij, source1, source2, destination);
    }


    // net.haesleinhuepf.clijx.advancedmath.GreaterOrEqual
    //----------------------------------------------------
    /**
     * Determines if two images A and B greater or equal pixel wise.
     * 
     * f(a, b) = 1 if a >= b; 0 otherwise. 
     */
    public boolean greaterOrEqual(ClearCLBuffer source1, ClearCLBuffer source2, ClearCLBuffer destination) {
        return GreaterOrEqual.greaterOrEqual(clij, source1, source2, destination);
    }


    // net.haesleinhuepf.clijx.advancedmath.Greater
    //----------------------------------------------------
    /**
     * Determines if two images A and B greater pixel wise.
     * 
     * f(a, b) = 1 if a > b; 0 otherwise. 
     */
    public boolean greater(ClearCLBuffer source1, ClearCLBuffer source2, ClearCLBuffer destination) {
        return Greater.greater(clij, source1, source2, destination);
    }


    // net.haesleinhuepf.clijx.advancedmath.Smaller
    //----------------------------------------------------
    /**
     * Determines if two images A and B smaller pixel wise.
     * 
     * f(a, b) = 1 if a < b; 0 otherwise. 
     */
    public boolean smaller(ClearCLBuffer source1, ClearCLBuffer source2, ClearCLBuffer destination) {
        return Smaller.smaller(clij, source1, source2, destination);
    }


    // net.haesleinhuepf.clijx.advancedmath.SmallerOrEqual
    //----------------------------------------------------
    /**
     * Determines if two images A and B smaller or equal pixel wise.
     * 
     * f(a, b) = 1 if a <= b; 0 otherwise. 
     */
    public boolean smallerOrEqual(ClearCLBuffer source1, ClearCLBuffer source2, ClearCLBuffer destination) {
        return SmallerOrEqual.smallerOrEqual(clij, source1, source2, destination);
    }


    // net.haesleinhuepf.clijx.advancedmath.NotEqual
    //----------------------------------------------------
    /**
     * Determines if two images A and B equal pixel wise.
     * 
     * f(a, b) = 1 if a != b; 0 otherwise. 
     */
    public boolean notEqual(ClearCLBuffer source1, ClearCLBuffer source2, ClearCLBuffer destination) {
        return NotEqual.notEqual(clij, source1, source2, destination);
    }


    // net.haesleinhuepf.clijx.io.ReadImageFromDisc
    //----------------------------------------------------
    /**
     * Read an image from disc.
     */
    public ClearCLBuffer readImageFromDisc(String arg1) {
        return ReadImageFromDisc.readImageFromDisc(clij, arg1);
    }


    // net.haesleinhuepf.clijx.io.ReadRawImageFromDisc
    //----------------------------------------------------
    /**
     * Reads a raw file from disc and pushes it immediately to the GPU.
     */
    public boolean readRawImageFromDisc(ClearCLBuffer arg1, String arg2) {
        return ReadRawImageFromDisc.readRawImageFromDisc(clij, arg1, arg2);
    }

    /**
     * Reads a raw file from disc and pushes it immediately to the GPU.
     */
    public ClearCLBuffer readRawImageFromDisc(String arg1, double arg2, double arg3, double arg4, double arg5) {
        return ReadRawImageFromDisc.readRawImageFromDisc(clij, arg1, new Double (arg2).intValue(), new Double (arg3).intValue(), new Double (arg4).intValue(), new Double (arg5).intValue());
    }


    // net.haesleinhuepf.clijx.io.PreloadFromDisc
    //----------------------------------------------------
    /**
     * This plugin takes two image filenames and loads them into RAM. The first image is returned immediately, the second image is loaded in the background and  will be returned when the plugin is called again.
     * 
     *  It is assumed that all images have the same size. If this is not the case, call release(image) before  getting the second image.
     */
    public ClearCLBuffer preloadFromDisc(ClearCLBuffer arg1, String arg2, String arg3, String arg4) {
        return PreloadFromDisc.preloadFromDisc(clij, arg1, arg2, arg3, arg4);
    }


    // net.haesleinhuepf.clijx.advancedmath.EqualConstant
    //----------------------------------------------------
    /**
     * Determines if an image A and a constant b are equal.
     * 
     * f(a, b) = 1 if a == b; 0 otherwise. 
     */
    public boolean equalConstant(ClearCLBuffer source, ClearCLBuffer destination, double constant) {
        return EqualConstant.equalConstant(clij, source, destination, new Double (constant).floatValue());
    }


    // net.haesleinhuepf.clijx.advancedmath.GreaterOrEqualConstant
    //----------------------------------------------------
    /**
     * Determines if two images A and B greater or equal pixel wise.
     * 
     * f(a, b) = 1 if a >= b; 0 otherwise. 
     */
    public boolean greaterOrEqualConstant(ClearCLBuffer source, ClearCLBuffer destination, double constant) {
        return GreaterOrEqualConstant.greaterOrEqualConstant(clij, source, destination, new Double (constant).floatValue());
    }


    // net.haesleinhuepf.clijx.advancedmath.GreaterConstant
    //----------------------------------------------------
    /**
     * Determines if two images A and B greater pixel wise.
     * 
     * f(a, b) = 1 if a > b; 0 otherwise. 
     */
    public boolean greaterConstant(ClearCLBuffer source, ClearCLBuffer destination, double constant) {
        return GreaterConstant.greaterConstant(clij, source, destination, new Double (constant).floatValue());
    }


    // net.haesleinhuepf.clijx.advancedmath.SmallerConstant
    //----------------------------------------------------
    /**
     * Determines if two images A and B smaller pixel wise.
     * 
     * f(a, b) = 1 if a < b; 0 otherwise. 
     */
    public boolean smallerConstant(ClearCLBuffer source, ClearCLBuffer destination, double constant) {
        return SmallerConstant.smallerConstant(clij, source, destination, new Double (constant).floatValue());
    }


    // net.haesleinhuepf.clijx.advancedmath.SmallerOrEqualConstant
    //----------------------------------------------------
    /**
     * Determines if two images A and B smaller or equal pixel wise.
     * 
     * f(a, b) = 1 if a <= b; 0 otherwise. 
     */
    public boolean smallerOrEqualConstant(ClearCLBuffer source, ClearCLBuffer destination, double constant) {
        return SmallerOrEqualConstant.smallerOrEqualConstant(clij, source, destination, new Double (constant).floatValue());
    }


    // net.haesleinhuepf.clijx.advancedmath.NotEqualConstant
    //----------------------------------------------------
    /**
     * Determines if two images A and B equal pixel wise.
     * 
     * f(a, b) = 1 if a != b; 0 otherwise. 
     */
    public boolean notEqualConstant(ClearCLBuffer source, ClearCLBuffer destination, double constant) {
        return NotEqualConstant.notEqualConstant(clij, source, destination, new Double (constant).floatValue());
    }


    // net.haesleinhuepf.clijx.painting.DrawBox
    //----------------------------------------------------
    /**
     * Draws a box at a given start point with given size. All pixels other than in the box are untouched. Consider using clij.op.set(buffer, 0); in advance.
     */
    public boolean drawBox(ClearCLBuffer arg1, double arg2, double arg3, double arg4, double arg5) {
        return DrawBox.drawBox(clij, arg1, new Double (arg2).floatValue(), new Double (arg3).floatValue(), new Double (arg4).floatValue(), new Double (arg5).floatValue());
    }

    /**
     * Draws a box at a given start point with given size. All pixels other than in the box are untouched. Consider using clij.op.set(buffer, 0); in advance.
     */
    public boolean drawBox(ClearCLBuffer destination, double x, double y, double z, double width, double height, double depth) {
        return DrawBox.drawBox(clij, destination, new Double (x).floatValue(), new Double (y).floatValue(), new Double (z).floatValue(), new Double (width).floatValue(), new Double (height).floatValue(), new Double (depth).floatValue());
    }


    // net.haesleinhuepf.clijx.painting.DrawLine
    //----------------------------------------------------
    /**
     * Draws a line between two points with a given thickness. All pixels other than on the line are untouched. Consider using clij.op.set(buffer, 0); in advance.
     */
    public boolean drawLine(ClearCLBuffer destination, double x1, double y1, double z1, double x2, double y2, double z2, double thickness) {
        return DrawLine.drawLine(clijx, destination, new Double (x1).floatValue(), new Double (y1).floatValue(), new Double (z1).floatValue(), new Double (x2).floatValue(), new Double (y2).floatValue(), new Double (z2).floatValue(), new Double (thickness).floatValue());
    }


    // net.haesleinhuepf.clijx.painting.DrawSphere
    //----------------------------------------------------
    /**
     * Draws a sphere around a given point with given radii in x, y and z (if 3D). All pixels other than in the sphere are untouched. Consider using clij.op.set(buffer, 0); in advance.
     */
    public boolean drawSphere(ClearCLBuffer arg1, double arg2, double arg3, double arg4, double arg5) {
        return DrawSphere.drawSphere(clij, arg1, new Double (arg2).floatValue(), new Double (arg3).floatValue(), new Double (arg4).floatValue(), new Double (arg5).floatValue());
    }

    /**
     * Draws a sphere around a given point with given radii in x, y and z (if 3D). All pixels other than in the sphere are untouched. Consider using clij.op.set(buffer, 0); in advance.
     */
    public boolean drawSphere(ClearCLBuffer destination, double x, double y, double z, double radius_x, double radius_y, double radius_z) {
        return DrawSphere.drawSphere(clij, destination, new Double (x).floatValue(), new Double (y).floatValue(), new Double (z).floatValue(), new Double (radius_x).floatValue(), new Double (radius_y).floatValue(), new Double (radius_z).floatValue());
    }


    // net.haesleinhuepf.clijx.advancedfilters.ReplaceIntensity
    //----------------------------------------------------
    /**
     * Replaces a specific intensity in an image with a given new value.
     */
    public boolean replaceIntensity(ClearCLImageInterface arg1, ClearCLImageInterface arg2, double arg3, double arg4) {
        return ReplaceIntensity.replaceIntensity(clij, arg1, arg2, new Double (arg3).floatValue(), new Double (arg4).floatValue());
    }


    // net.haesleinhuepf.clijx.advancedfilters.BoundingBox
    //----------------------------------------------------
    /**
     * Determines the bounding box of all non-zero pixels in a binary image. The positions will be stored in a new row of ImageJs
     * Results table in the column 'BoundingBoxX', 'BoundingBoxY', 'BoundingBoxZ', 'BoundingBoxWidth', 'BoundingBoxHeight' 'BoundingBoxDepth'.In case of 2D images Z and depth will be zero.
     */
    public double[] boundingBox(ClearCLBuffer source) {
        return BoundingBox.boundingBox(clij, source);
    }


    // net.haesleinhuepf.clijx.advancedfilters.MinimumOfMaskedPixels
    //----------------------------------------------------
    /**
     * Determines the minimum intensity in an image, but only in pixels which have non-zero values in another mask image.
     */
    public double minimumOfMaskedPixels(ClearCLBuffer source, ClearCLBuffer mask) {
        return MinimumOfMaskedPixels.minimumOfMaskedPixels(clij, source, mask);
    }


    // net.haesleinhuepf.clijx.advancedfilters.MaximumOfMaskedPixels
    //----------------------------------------------------
    /**
     * Determines the maximum intensity in an image, but only in pixels which have non-zero values in another mask image.
     */
    public double maximumOfMaskedPixels(ClearCLBuffer source, ClearCLBuffer mask) {
        return MaximumOfMaskedPixels.maximumOfMaskedPixels(clij, source, mask);
    }


    // net.haesleinhuepf.clijx.advancedfilters.MeanOfMaskedPixels
    //----------------------------------------------------
    /**
     * Determines the mean intensity in an image, but only in pixels which have non-zero values in another binary mask image.
     */
    public double meanOfMaskedPixels(ClearCLBuffer source, ClearCLBuffer mask) {
        return MeanOfMaskedPixels.meanOfMaskedPixels(clij, source, mask);
    }


    // net.haesleinhuepf.clijx.advancedfilters.LabelToMask
    //----------------------------------------------------
    /**
     * Masks a single label in a label map: Sets all pixels in the target image to 1, where the given label index was present in the label map. Other pixels are set to 0.
     */
    public boolean labelToMask(ClearCLBuffer label_map_source, ClearCLBuffer mask_destination, double label_index) {
        return LabelToMask.labelToMask(clij, label_map_source, mask_destination, new Double (label_index).floatValue());
    }


    // net.haesleinhuepf.clijx.matrix.NClosestPoints
    //----------------------------------------------------
    /**
     * Determine the n point indices with shortest distance for all points in a distance matrix.
     * This corresponds to the n row indices with minimum values for each column of the distance matrix.
     */
    public boolean nClosestPoints(ClearCLBuffer arg1, ClearCLBuffer arg2) {
        return NClosestPoints.nClosestPoints(clij, arg1, arg2);
    }


    // net.haesleinhuepf.clijx.matrix.GaussJordan
    //----------------------------------------------------
    /**
     * Gauss Jordan elimination algorithm for solving linear equation systems. Ent the equation coefficients as an n*n sized image A and an n*1 sized image B:
     * <pre>a(1,1)*x + a(2,1)*y + a(3,1)+z = b(1)
     * a(2,1)*x + a(2,2)*y + a(3,2)+z = b(2)
     * a(3,1)*x + a(3,2)*y + a(3,3)+z = b(3)
     * </pre>
     * The results will then be given in an n*1 image with values [x, y, z].
     * 
     * Adapted from: 
     * https://github.com/qbunia/rodinia/blob/master/opencl/gaussian/gaussianElim_kernels.cl
     * L.G. Szafaryn, K. Skadron and J. Saucerman. "Experiences Accelerating MATLAB Systems
     * //Biology Applications." in Workshop on Biomedicine in Computing (BiC) at the International
     * //Symposium on Computer Architecture (ISCA), June 2009.
     */
    public boolean gaussJordan(ClearCLBuffer A_matrix, ClearCLBuffer B_result_vector, ClearCLBuffer solution_destination) {
        return GaussJordan.gaussJordan(clij, A_matrix, B_result_vector, solution_destination);
    }


    // net.haesleinhuepf.clijx.advancedfilters.StatisticsOfLabelledPixels
    //----------------------------------------------------
    /**
     * Determines bounding box, area (in pixels/voxels), min, max and mean intensity  of a labelled object in a label map and corresponding pixels in the original image.Instead of a label map, you can also use a binary image as a binary image is a label map with just one label.
     */
    public double[] statisticsOfLabelledPixels(ClearCLBuffer arg1, ClearCLBuffer arg2, int arg3) {
        return StatisticsOfLabelledPixels.statisticsOfLabelledPixels(clij, arg1, arg2, arg3);
    }

    /**
     * Determines bounding box, area (in pixels/voxels), min, max and mean intensity  of a labelled object in a label map and corresponding pixels in the original image.Instead of a label map, you can also use a binary image as a binary image is a label map with just one label.
     */
    public ResultsTable statisticsOfLabelledPixels(ClearCLBuffer arg1, ClearCLBuffer arg2, ResultsTable arg3) {
        return StatisticsOfLabelledPixels.statisticsOfLabelledPixels(clij, arg1, arg2, arg3);
    }

    /**
     * Determines bounding box, area (in pixels/voxels), min, max and mean intensity  of a labelled object in a label map and corresponding pixels in the original image.Instead of a label map, you can also use a binary image as a binary image is a label map with just one label.
     */
    public double[][] statisticsOfLabelledPixels(ClearCLBuffer arg1, ClearCLBuffer arg2, int arg3, int arg4) {
        return StatisticsOfLabelledPixels.statisticsOfLabelledPixels(clij, arg1, arg2, arg3, arg4);
    }

    /**
     * Determines bounding box, area (in pixels/voxels), min, max and mean intensity  of a labelled object in a label map and corresponding pixels in the original image.Instead of a label map, you can also use a binary image as a binary image is a label map with just one label.
     */
    public double[][] statisticsOfLabelledPixels(ClearCLBuffer input, ClearCLBuffer labelmap) {
        return StatisticsOfLabelledPixels.statisticsOfLabelledPixels(clij, input, labelmap);
    }


    // net.haesleinhuepf.clijx.advancedfilters.VarianceOfAllPixels
    //----------------------------------------------------
    /**
     * Determines the variance of all pixels in an image. The value will be stored in a new row of ImageJs
     * Results table in the column 'Variance'.
     */
    public double varianceOfAllPixels(ClearCLBuffer source) {
        return VarianceOfAllPixels.varianceOfAllPixels(clij, source);
    }

    /**
     * Determines the variance of all pixels in an image. The value will be stored in a new row of ImageJs
     * Results table in the column 'Variance'.
     */
    public double varianceOfAllPixels(ClearCLBuffer arg1, double arg2) {
        return VarianceOfAllPixels.varianceOfAllPixels(clij, arg1, new Double (arg2).floatValue());
    }


    // net.haesleinhuepf.clijx.advancedfilters.StandardDeviationOfAllPixels
    //----------------------------------------------------
    /**
     * Determines the standard deviation of all pixels in an image. The value will be stored in a new row of ImageJs
     * Results table in the column 'Standard_deviation'.
     */
    public double standardDeviationOfAllPixels(ClearCLBuffer source) {
        return StandardDeviationOfAllPixels.standardDeviationOfAllPixels(clij, source);
    }

    /**
     * Determines the standard deviation of all pixels in an image. The value will be stored in a new row of ImageJs
     * Results table in the column 'Standard_deviation'.
     */
    public double standardDeviationOfAllPixels(ClearCLBuffer arg1, double arg2) {
        return StandardDeviationOfAllPixels.standardDeviationOfAllPixels(clij, arg1, new Double (arg2).floatValue());
    }


    // net.haesleinhuepf.clijx.advancedfilters.ExcludeLabelsOnEdges
    //----------------------------------------------------
    /**
     * Removes all labels from a label map which touch the edges of the image (in X, Y and Z if the image is 3D). Remaining label elements are renumbered afterwards.
     */
    public boolean excludeLabelsOnEdges(ClearCLBuffer label_map_input, ClearCLBuffer label_map_destination) {
        return ExcludeLabelsOnEdges.excludeLabelsOnEdges(clij, label_map_input, label_map_destination);
    }


    // net.haesleinhuepf.clijx.advancedfilters.BinarySubtract
    //----------------------------------------------------
    /**
     * Subtracts one binary image from another.
     */
    public boolean binarySubtract(ClearCLImageInterface arg1, ClearCLImageInterface arg2, ClearCLImageInterface arg3) {
        return BinarySubtract.binarySubtract(clijx, arg1, arg2, arg3);
    }


    // net.haesleinhuepf.clijx.advancedfilters.BinaryEdgeDetection
    //----------------------------------------------------
    /**
     * Determines pixels/voxels which are on the surface of a binary objects and sets only them to 1 in the destination image. All other pixels are set to 0.
     */
    public boolean binaryEdgeDetection(ClearCLImageInterface arg1, ClearCLImageInterface arg2) {
        return BinaryEdgeDetection.binaryEdgeDetection(clijx, arg1, arg2);
    }


    // net.haesleinhuepf.clijx.advancedfilters.DistanceMap
    //----------------------------------------------------
    /**
     * Generates a distance map from a binary image. Pixels with non-zero value in the binary image are set to a number representing the distance to the closest zero-value pixel.
     */
    public boolean distanceMap(ClearCLBuffer source, ClearCLBuffer destination) {
        return DistanceMap.distanceMap(clijx, source, destination);
    }

    /**
     * 
     */
    public boolean localPositiveMinimum(ClearCLImageInterface arg1, ClearCLImageInterface arg2, ClearCLImageInterface arg3) {
        return DistanceMap.localPositiveMinimum(clijx, arg1, arg2, arg3);
    }


    // net.haesleinhuepf.clijx.advancedfilters.PullAsROI
    //----------------------------------------------------
    /**
     * Pulls a binary image from the GPU memory and puts it on the currently active ImageJ window.
     */
    public Roi pullAsROI(ClearCLBuffer binary_input) {
        return PullAsROI.pullAsROI(clij, binary_input);
    }


    // net.haesleinhuepf.clijx.advancedfilters.NonzeroMaximumDiamond
    //----------------------------------------------------
    /**
     * Apply a maximum-sphere filter to the input image. The radius is fixed to 1 and pixels with value 0 are ignored.
     */
    public boolean nonzeroMaximumDiamond(ClearCLImageInterface arg1, ClearCLImageInterface arg2, ClearCLImageInterface arg3) {
        return NonzeroMaximumDiamond.nonzeroMaximumDiamond(clij, arg1, arg2, arg3);
    }

    /**
     * Apply a maximum-sphere filter to the input image. The radius is fixed to 1 and pixels with value 0 are ignored.
     */
    public boolean nonzeroMaximumDiamond(ClearCLImageInterface arg1, ClearCLBuffer arg2, ClearCLImageInterface arg3) {
        return NonzeroMaximumDiamond.nonzeroMaximumDiamond(clijx, arg1, arg2, arg3);
    }

    /**
     * Apply a maximum-sphere filter to the input image. The radius is fixed to 1 and pixels with value 0 are ignored.
     */
    public ClearCLKernel nonzeroMaximumDiamond(ClearCLImageInterface arg1, ClearCLBuffer arg2, ClearCLImageInterface arg3, ClearCLKernel arg4) {
        return NonzeroMaximumDiamond.nonzeroMaximumDiamond(clijx, arg1, arg2, arg3, arg4);
    }


    // net.haesleinhuepf.clijx.advancedfilters.OnlyzeroOverwriteMaximumDiamond
    //----------------------------------------------------
    /**
     * TODO
     */
    public boolean onlyzeroOverwriteMaximumDiamond(ClearCLImageInterface arg1, ClearCLImageInterface arg2, ClearCLImageInterface arg3) {
        return OnlyzeroOverwriteMaximumDiamond.onlyzeroOverwriteMaximumDiamond(clij, arg1, arg2, arg3);
    }


    // net.haesleinhuepf.clijx.advancedfilters.OnlyzeroOverwriteMaximumBox
    //----------------------------------------------------
    /**
     * 
     */
    public boolean onlyzeroOverwriteMaximumBox(ClearCLImageInterface arg1, ClearCLImageInterface arg2, ClearCLImageInterface arg3) {
        return OnlyzeroOverwriteMaximumBox.onlyzeroOverwriteMaximumBox(clij, arg1, arg2, arg3);
    }


    // net.haesleinhuepf.clijx.matrix.GenerateTouchMatrix
    //----------------------------------------------------
    /**
     * Takes a labelmap with n labels and generates a (n+1)*(n+1) matrix where all pixels are set to 0 exept those where labels are touching.Only half of the matrix is filled (with x < y). For example, if labels 3 and 4 are touching then the pixel (3,4) in the matrix will be set to 1.
     */
    public boolean generateTouchMatrix(ClearCLBuffer label_map, ClearCLBuffer touch_matrix_destination) {
        return GenerateTouchMatrix.generateTouchMatrix(clij, label_map, touch_matrix_destination);
    }


    // net.haesleinhuepf.clijx.advancedfilters.DetectLabelEdges
    //----------------------------------------------------
    /**
     * Takes a labelmap and returns an image where all pixels on label edges are set to 1 and all other pixels to 0.
     */
    public boolean detectLabelEdges(ClearCLBuffer label_map, ClearCLBuffer edge_image_destination) {
        return DetectLabelEdges.detectLabelEdges(clijx, label_map, edge_image_destination);
    }


    // net.haesleinhuepf.clijx.advancedfilters.StopWatch
    //----------------------------------------------------
    /**
     * Measures time and outputs delay to last call.
     */
    public boolean stopWatch(String arg1) {
        return StopWatch.stopWatch(clij, arg1);
    }


    // net.haesleinhuepf.clijx.matrix.CountTouchingNeighbors
    //----------------------------------------------------
    /**
     * Takes a touching-neighbors-matrix as input and delivers a vector with number of touching neighbors per label as a vector.
     */
    public boolean countTouchingNeighbors(ClearCLBuffer touch_matrix, ClearCLBuffer touching_neighbors_count_destination) {
        return CountTouchingNeighbors.countTouchingNeighbors(clijx, touch_matrix, touching_neighbors_count_destination);
    }


    // net.haesleinhuepf.clijx.advancedfilters.ReplaceIntensities
    //----------------------------------------------------
    /**
     * Replaces integer intensities specified in a vector image. The vector image must be 3D with size (m, 1, 1) where m corresponds to the maximum intensity in the original image. Assuming the vector image contains values (0, 1, 0, 2) means: 
     *  * All pixels with value 0 (first entry in the vector image) get value 0
     *  * All pixels with value 1 get value 1
     *  * All pixels with value 2 get value 0
     *  * All pixels with value 3 get value 2
     * 
     */
    public boolean replaceIntensities(ClearCLImageInterface arg1, ClearCLImageInterface arg2, ClearCLImageInterface arg3) {
        return ReplaceIntensities.replaceIntensities(clijx, arg1, arg2, arg3);
    }


    // net.haesleinhuepf.clijx.painting.DrawTwoValueLine
    //----------------------------------------------------
    /**
     * Draws a line between two points with a given thickness. Pixels close to point 1 are set to value1. Pixels closer to point 2 are set to value2 All pixels other than on the line are untouched. Consider using clij.set(buffer, 0); in advance.
     */
    public boolean drawTwoValueLine(ClearCLBuffer destination, double x1, double y1, double z1, double x2, double y2, double z2, double thickness, double value1, double destination0) {
        return DrawTwoValueLine.drawTwoValueLine(clijx, destination, new Double (x1).floatValue(), new Double (y1).floatValue(), new Double (z1).floatValue(), new Double (x2).floatValue(), new Double (y2).floatValue(), new Double (z2).floatValue(), new Double (thickness).floatValue(), new Double (value1).floatValue(), new Double (destination0).floatValue());
    }


    // net.haesleinhuepf.clijx.matrix.AverageDistanceOfNClosestPoints
    //----------------------------------------------------
    /**
     * Determine the n point indices with shortest distance for all points in a distance matrix.
     * This corresponds to the n row indices with minimum values for each column of the distance matrix.
     */
    public boolean averageDistanceOfClosestPoints(ClearCLBuffer distance_matrix, ClearCLBuffer indexlist_destination, double nClosestPointsTofind) {
        return AverageDistanceOfNClosestPoints.averageDistanceOfClosestPoints(clijx, distance_matrix, indexlist_destination, new Double (nClosestPointsTofind).intValue());
    }


    // net.haesleinhuepf.clijx.advancedfilters.SaveAsTIF
    //----------------------------------------------------
    /**
     * Pulls an image from the GPU memory and saves it as TIF to disc.
     */
    public boolean saveAsTIF(ClearCLBuffer arg1, String arg2) {
        return SaveAsTIF.saveAsTIF(clij, arg1, arg2);
    }


    // net.haesleinhuepf.clijx.advancedfilters.ConnectedComponentsLabelingInplace
    //----------------------------------------------------
    /**
     * Performs connected components analysis to a binary image and generates a label map.
     */
    public boolean connectedComponentsLabelingInplace(ClearCLBuffer binary_source_labeling_destination) {
        return ConnectedComponentsLabelingInplace.connectedComponentsLabelingInplace(clijx, binary_source_labeling_destination);
    }


    // net.haesleinhuepf.clijx.matrix.TouchMatrixToMesh
    //----------------------------------------------------
    /**
     * Takes a pointlist with dimensions n*d with n point coordinates in d dimensions and a touch matrix of size n*n to draw lines from all points to points if the corresponding pixel in the touch matrix is 1.
     */
    public boolean touchMatrixToMesh(ClearCLBuffer pointlist, ClearCLBuffer touch_matrix, ClearCLBuffer mesh_destination) {
        return TouchMatrixToMesh.touchMatrixToMesh(clijx, pointlist, touch_matrix, mesh_destination);
    }


    // net.haesleinhuepf.clijx.advancedfilters.AutomaticThresholdInplace
    //----------------------------------------------------
    /**
     * The automatic thresholder utilizes the threshold methods from ImageJ on a histogram determined on 
     * the GPU to create binary images as similar as possible to ImageJ 'Apply Threshold' method. Enter one 
     * of these methods in the method text field:
     * [Default, Huang, Intermodes, IsoData, IJ_IsoData, Li, MaxEntropy, Mean, MinError, Minimum, Moments, Otsu, Percentile, RenyiEntropy, Shanbhag, Triangle, Yen]
     */
    public boolean automaticThresholdInplace(ClearCLBuffer arg1, String arg2) {
        return AutomaticThresholdInplace.automaticThresholdInplace(clij, arg1, arg2);
    }


    // net.haesleinhuepf.clijx.advancedfilters.DifferenceOfGaussianInplace3D
    //----------------------------------------------------
    /**
     * Applies Gaussian blur to the input image twice with different sigma values resulting in two images which are then subtracted from each other.
     * 
     * It is recommended to apply this operation to images of type Float (32 bit) as results might be negative.
     */
    public boolean differenceOfGaussianInplace3D(ClearCLBuffer input_and_destination, double sigma1x, double sigma1y, double sigma1z, double sigma2x, double sigma2y, double sigma2z) {
        return DifferenceOfGaussianInplace3D.differenceOfGaussianInplace3D(clij, input_and_destination, new Double (sigma1x).floatValue(), new Double (sigma1y).floatValue(), new Double (sigma1z).floatValue(), new Double (sigma2x).floatValue(), new Double (sigma2y).floatValue(), new Double (sigma2z).floatValue());
    }


    // net.haesleinhuepf.clijx.advancedfilters.AbsoluteInplace
    //----------------------------------------------------
    /**
     * Computes the absolute value of every individual pixel x in a given image.
     * 
     * <pre>f(x) = |x| </pre>
     */
    public boolean absoluteInplace(ClearCLBuffer arg1) {
        return AbsoluteInplace.absoluteInplace(clijx, arg1);
    }


    // net.haesleinhuepf.clijx.advancedfilters.Resample
    //----------------------------------------------------
    /**
     * Resamples an image with given size factors using an affine transform.
     */
    public boolean resample(ClearCLBuffer arg1, ClearCLBuffer arg2, float arg3, float arg4, float arg5, boolean arg6) {
        return Resample.resample(clij, arg1, arg2, arg3, arg4, arg5, arg6);
    }


    // net.haesleinhuepf.clijx.advancedfilters.EqualizeMeanIntensitiesOfSlices
    //----------------------------------------------------
    /**
     * Determines correction factors for each z-slice so that the average intensity in all slices can be made the same and multiplies these factors with the slices.
     * This functionality is similar to the 'Simple Ratio Bleaching Correction' in Fiji.
     */
    public boolean equalizeMeanIntensitiesOfSlices(ClearCLBuffer input, ClearCLBuffer destination, double referenceSlice) {
        return EqualizeMeanIntensitiesOfSlices.equalizeMeanIntensitiesOfSlices(clij, input, destination, new Double (referenceSlice).intValue());
    }


    // net.haesleinhuepf.clijx.advancedfilters.Watershed
    //----------------------------------------------------
    /**
     * Apply a binary watershed to a binary image and introduces black pixels between objects.
     */
    public boolean watershed(ClearCLBuffer binary_source, ClearCLBuffer destination) {
        return Watershed.watershed(clijx, binary_source, destination);
    }


    // net.haesleinhuepf.clijx.advancedfilters.ResliceRadial
    //----------------------------------------------------
    /**
     * Computes a radial projection of an image stack. Starting point for the line is the center in any 
     * X/Y-plane of a given input image stack. This operation is similar to ImageJs 'Radial Reslice' method but offers less flexibility.
     */
    public boolean resliceRadial(ClearCLBuffer arg1, ClearCLBuffer arg2, double arg3, double arg4, double arg5, double arg6, double arg7, double arg8) {
        return ResliceRadial.resliceRadial(clij, arg1, arg2, new Double (arg3).floatValue(), new Double (arg4).floatValue(), new Double (arg5).floatValue(), new Double (arg6).floatValue(), new Double (arg7).floatValue(), new Double (arg8).floatValue());
    }

    /**
     * Computes a radial projection of an image stack. Starting point for the line is the center in any 
     * X/Y-plane of a given input image stack. This operation is similar to ImageJs 'Radial Reslice' method but offers less flexibility.
     */
    public boolean resliceRadial(ClearCLBuffer arg1, ClearCLBuffer arg2, double arg3, double arg4, double arg5) {
        return ResliceRadial.resliceRadial(clij, arg1, arg2, new Double (arg3).floatValue(), new Double (arg4).floatValue(), new Double (arg5).floatValue());
    }

    /**
     * Computes a radial projection of an image stack. Starting point for the line is the center in any 
     * X/Y-plane of a given input image stack. This operation is similar to ImageJs 'Radial Reslice' method but offers less flexibility.
     */
    public boolean resliceRadial(ClearCLBuffer arg1, ClearCLBuffer arg2, double arg3) {
        return ResliceRadial.resliceRadial(clij, arg1, arg2, new Double (arg3).floatValue());
    }


    // net.haesleinhuepf.clijx.advancedfilters.ShowRGB
    //----------------------------------------------------
    /**
     * Visualises three 2D images as one RGB image
     */
    public boolean showRGB(ClearCLBuffer arg1, ClearCLBuffer arg2, ClearCLBuffer arg3, String arg4) {
        return ShowRGB.showRGB(clij, arg1, arg2, arg3, arg4);
    }


    // net.haesleinhuepf.clijx.advancedfilters.ShowGrey
    //----------------------------------------------------
    /**
     * Visualises a single 2D image.
     */
    public boolean showGrey(ClearCLBuffer arg1, String arg2) {
        return ShowGrey.showGrey(clij, arg1, arg2);
    }


    // net.haesleinhuepf.clijx.advancedfilters.Sobel
    //----------------------------------------------------
    /**
     * Convolve the image with the Sobel kernel.
     */
    public boolean sobel(ClearCLBuffer source, ClearCLBuffer destination) {
        return Sobel.sobel(clij, source, destination);
    }


    // net.haesleinhuepf.clijx.advancedfilters.Absolute
    //----------------------------------------------------
    /**
     * Computes the absolute value of every individual pixel x in a given image.
     * 
     * <pre>f(x) = |x| </pre>
     */
    public boolean absolute(ClearCLImageInterface arg1, ClearCLImageInterface arg2) {
        return Absolute.absolute(clijx, arg1, arg2);
    }


    // net.haesleinhuepf.clijx.advancedfilters.LaplaceBox
    //----------------------------------------------------
    /**
     * Applies the Laplace operator (Box neighborhood) to an image.
     */
    public boolean laplaceBox(ClearCLBuffer input, ClearCLBuffer destination) {
        return LaplaceBox.laplaceBox(clij, input, destination);
    }


    // net.haesleinhuepf.clijx.advancedfilters.BottomHatBox
    //----------------------------------------------------
    /**
     * Apply a bottom-hat filter for background subtraction to the input image.
     */
    public boolean bottomHatBox(ClearCLBuffer input, ClearCLBuffer destination, double radiusX, double radiusY, double radiusZ) {
        return BottomHatBox.bottomHatBox(clij, input, destination, new Double (radiusX).intValue(), new Double (radiusY).intValue(), new Double (radiusZ).intValue());
    }


    // net.haesleinhuepf.clijx.advancedfilters.BottomHatSphere
    //----------------------------------------------------
    /**
     * Applies a bottom-hat filter for background subtraction to the input image.
     */
    public boolean bottomHatSphere(ClearCLBuffer input, ClearCLBuffer destination, double radiusX, double radiusY, double radiusZ) {
        return BottomHatSphere.bottomHatSphere(clij, input, destination, new Double (radiusX).intValue(), new Double (radiusY).intValue(), new Double (radiusZ).intValue());
    }


    // net.haesleinhuepf.clijx.advancedfilters.ClosingBox
    //----------------------------------------------------
    /**
     * Apply a binary closing to the input image by calling n dilations and n erosions subsequenntly.
     */
    public boolean closingBox(ClearCLBuffer input, ClearCLBuffer destination, double number_of_dilations_and_erosions) {
        return ClosingBox.closingBox(clij, input, destination, new Double (number_of_dilations_and_erosions).intValue());
    }


    // net.haesleinhuepf.clijx.advancedfilters.ClosingDiamond
    //----------------------------------------------------
    /**
     * Apply a binary closing to the input image by calling n dilations and n erosions subsequenntly.
     */
    public boolean closingDiamond(ClearCLBuffer input, ClearCLBuffer destination, double number_of_dilations_and_erotions) {
        return ClosingDiamond.closingDiamond(clij, input, destination, new Double (number_of_dilations_and_erotions).intValue());
    }


    // net.haesleinhuepf.clijx.advancedfilters.OpeningBox
    //----------------------------------------------------
    /**
     * Apply a binary opening to the input image by calling n erosions and n dilations subsequenntly.
     */
    public boolean openingBox(ClearCLBuffer input, ClearCLBuffer destination, double number_of_erotions_and_dilations) {
        return OpeningBox.openingBox(clij, input, destination, new Double (number_of_erotions_and_dilations).intValue());
    }


    // net.haesleinhuepf.clijx.advancedfilters.OpeningDiamond
    //----------------------------------------------------
    /**
     * Apply a binary opening to the input image by calling n erosions and n dilations subsequenntly.
     */
    public boolean openingDiamond(ClearCLBuffer input, ClearCLBuffer destination, double number_of_erotions_and_dilations) {
        return OpeningDiamond.openingDiamond(clij, input, destination, new Double (number_of_erotions_and_dilations).intValue());
    }


    // net.haesleinhuepf.clijx.advancedfilters.MaximumXProjection
    //----------------------------------------------------
    /**
     * Determines the maximum projection of an image along X.
     */
    public boolean maximumXProjection(ClearCLBuffer source, ClearCLBuffer destination_max) {
        return MaximumXProjection.maximumXProjection(clij, source, destination_max);
    }


    // net.haesleinhuepf.clijx.advancedfilters.MaximumYProjection
    //----------------------------------------------------
    /**
     * Determines the maximum projection of an image along X.
     */
    public boolean maximumYProjection(ClearCLBuffer source, ClearCLBuffer destination_max) {
        return MaximumYProjection.maximumYProjection(clij, source, destination_max);
    }


    // net.haesleinhuepf.clijx.advancedfilters.ProjectMaximumZBounded
    //----------------------------------------------------
    /**
     * Determines the maximum projection of an image along Z within a given z range.
     */
    public boolean projectMaximumZBounded(ClearCLBuffer source, ClearCLBuffer destination_max, double min_z, double max_z) {
        return ProjectMaximumZBounded.projectMaximumZBounded(clij, source, destination_max, new Double (min_z).intValue(), new Double (max_z).intValue());
    }


    // net.haesleinhuepf.clijx.advancedfilters.ProjectMinimumZBounded
    //----------------------------------------------------
    /**
     * Determines the minimum projection of an image along Z within a given z range.
     */
    public boolean projectMinimumZBounded(ClearCLBuffer source, ClearCLBuffer destination_min, double min_z, double max_z) {
        return ProjectMinimumZBounded.projectMinimumZBounded(clij, source, destination_min, new Double (min_z).intValue(), new Double (max_z).intValue());
    }


    // net.haesleinhuepf.clijx.advancedfilters.ProjectMeanZBounded
    //----------------------------------------------------
    /**
     * Determines the mean projection of an image along Z within a given z range.
     */
    public boolean projectMeanZBounded(ClearCLBuffer source, ClearCLBuffer destination_mean, double min_z, double max_z) {
        return ProjectMeanZBounded.projectMeanZBounded(clij, source, destination_mean, new Double (min_z).intValue(), new Double (max_z).intValue());
    }


    // net.haesleinhuepf.clijx.advancedfilters.NonzeroMaximumBox
    //----------------------------------------------------
    /**
     * Apply a maximum-sphere filter to the input image. The radius is fixed to 1 and pixels with value 0 are ignored.
     */
    public ClearCLKernel nonzeroMaximumBox(ClearCLImageInterface arg1, ClearCLBuffer arg2, ClearCLImageInterface arg3, ClearCLKernel arg4) {
        return NonzeroMaximumBox.nonzeroMaximumBox(clijx, arg1, arg2, arg3, arg4);
    }

    /**
     * Apply a maximum-sphere filter to the input image. The radius is fixed to 1 and pixels with value 0 are ignored.
     */
    public boolean nonzeroMaximumBox(ClearCLImageInterface arg1, ClearCLBuffer arg2, ClearCLImageInterface arg3) {
        return NonzeroMaximumBox.nonzeroMaximumBox(clijx, arg1, arg2, arg3);
    }


    // net.haesleinhuepf.clijx.advancedfilters.NonzeroMinimumBox
    //----------------------------------------------------
    /**
     * 
     */
    public ClearCLKernel nonzeroMinimumBox(ClearCLImageInterface arg1, ClearCLBuffer arg2, ClearCLImageInterface arg3, ClearCLKernel arg4) {
        return NonzeroMinimumBox.nonzeroMinimumBox(clijx, arg1, arg2, arg3, arg4);
    }

    /**
     * 
     */
    public boolean nonzeroMinimumBox(ClearCLImageInterface arg1, ClearCLBuffer arg2, ClearCLImageInterface arg3) {
        return NonzeroMinimumBox.nonzeroMinimumBox(clijx, arg1, arg2, arg3);
    }


    // net.haesleinhuepf.clijx.advancedfilters.ProjectMinimumThresholdedZBounded
    //----------------------------------------------------
    /**
     * Determines the minimum projection of all pixels in an image above a given threshold along Z within a given z range.
     */
    public boolean projectMinimumThresholdedZBounded(ClearCLBuffer arg1, ClearCLBuffer arg2, double arg3, double arg4, double arg5) {
        return ProjectMinimumThresholdedZBounded.projectMinimumThresholdedZBounded(clij, arg1, arg2, new Double (arg3).floatValue(), new Double (arg4).intValue(), new Double (arg5).intValue());
    }


    // net.haesleinhuepf.clijx.advancedfilters.MeanOfPixelsAboveThreshold
    //----------------------------------------------------
    /**
     * Determines the mean intensity in an image, but only in pixels which are above a given threshold.
     */
    public double meanOfPixelsAboveThreshold(ClearCLBuffer source, double threshold) {
        return MeanOfPixelsAboveThreshold.meanOfPixelsAboveThreshold(clij, source, new Double (threshold).floatValue());
    }


    // net.haesleinhuepf.clijx.gui.OrganiseWindows
    //----------------------------------------------------
    /**
     * Organises windows on screen.
     */
    public boolean organiseWindows(int arg1, int arg2, int arg3, int arg4, int arg5, int arg6) {
        return OrganiseWindows.organiseWindows(clij, arg1, arg2, arg3, arg4, arg5, arg6);
    }


    // net.haesleinhuepf.clijx.matrix.DistanceMatrixToMesh
    //----------------------------------------------------
    /**
     * Takes a pointlist with dimensions n*d with n point coordinates in d dimensions and a distance matrix of size n*n to draw lines from all points to points if the corresponding pixel in the distance matrix is smaller than a given distance threshold.
     */
    public boolean distanceMatrixToMesh(ClearCLBuffer pointlist, ClearCLBuffer distance_matrix, ClearCLBuffer mesh_destination, double maximumDistance) {
        return DistanceMatrixToMesh.distanceMatrixToMesh(clijx, pointlist, distance_matrix, mesh_destination, new Double (maximumDistance).floatValue());
    }


    // net.haesleinhuepf.clijx.matrix.PointIndexListToMesh
    //----------------------------------------------------
    /**
     * Meshes all points in a given point list which are indiced in a corresponding index list. TODO: Explain better
     */
    public boolean pointIndexListToMesh(ClearCLBuffer pointlist, ClearCLBuffer indexList, ClearCLBuffer Mesh) {
        return PointIndexListToMesh.pointIndexListToMesh(clijx, pointlist, indexList, Mesh);
    }


    // net.haesleinhuepf.clijx.advancedfilters.MinimumOctagon
    //----------------------------------------------------
    /**
     * Applies a minimum filter with kernel size 3x3 n times to an image iteratively. Odd iterations are done with box neighborhood, even iterations with a diamond. Thus, with n > 2, the filter shape is an octagon. The given number of iterations makes the filter result very similar to minimum sphere. Approximately:radius = iterations - 2
     */
    public boolean minimumOctagon(ClearCLBuffer input, ClearCLBuffer destination, double iterations) {
        return MinimumOctagon.minimumOctagon(clijx, input, destination, new Double (iterations).intValue());
    }


    // net.haesleinhuepf.clijx.advancedfilters.MaximumOctagon
    //----------------------------------------------------
    /**
     * Applies a maximum filter with kernel size 3x3 n times to an image iteratively. Odd iterations are done with box neighborhood, even iterations with a diamond. Thus, with n > 2, the filter shape is an octagon. The given number of iterations makes the filter result very similar to minimum sphere. Approximately:radius = iterations - 2
     */
    public boolean maximumOctagon(ClearCLBuffer input, ClearCLBuffer destination, double iterations) {
        return MaximumOctagon.maximumOctagon(clijx, input, destination, new Double (iterations).intValue());
    }


    // net.haesleinhuepf.clijx.advancedfilters.TopHatOctagon
    //----------------------------------------------------
    /**
     * Applies a minimum filter with kernel size 3x3 n times to an image iteratively. Odd iterations are done with box neighborhood, even iterations with a diamond. Thus, with n > 2, the filter shape is an octagon. The given number of iterations - 2 makes the filter result very similar to minimum sphere.
     */
    public boolean topHatOctagon(ClearCLBuffer input, ClearCLBuffer destination, double iterations) {
        return TopHatOctagon.topHatOctagon(clijx, input, destination, new Double (iterations).intValue());
    }


    // net.haesleinhuepf.clijx.advancedfilters.AddImages
    //----------------------------------------------------
    /**
     * Calculates the sum of pairs of pixels x and y of two images X and Y.
     * 
     * <pre>f(x, y) = x + y</pre>
     */
    public boolean addImages(ClearCLImageInterface arg1, ClearCLImageInterface arg2, ClearCLImageInterface arg3) {
        return AddImages.addImages(clijx, arg1, arg2, arg3);
    }


    // net.haesleinhuepf.clijx.advancedfilters.AddImagesWeighted
    //----------------------------------------------------
    /**
     * Calculates the sum of pairs of pixels x and y from images X and Y weighted with factors a and b.
     * 
     * <pre>f(x, y, a, b) = x * a + y * b</pre>
     */
    public boolean addImagesWeighted(ClearCLImageInterface arg1, ClearCLImageInterface arg2, ClearCLImageInterface arg3, double arg4, double arg5) {
        return AddImagesWeighted.addImagesWeighted(clijx, arg1, arg2, arg3, new Double (arg4).floatValue(), new Double (arg5).floatValue());
    }


    // net.haesleinhuepf.clijx.advancedfilters.SubtractImages
    //----------------------------------------------------
    /**
     * Subtracts one image X from another image Y pixel wise.
     * 
     * <pre>f(x, y) = x - y</pre>
     */
    public boolean subtract(ClearCLImageInterface arg1, ClearCLImageInterface arg2, ClearCLImageInterface arg3) {
        return SubtractImages.subtract(clijx, arg1, arg2, arg3);
    }

    /**
     * Subtracts one image X from another image Y pixel wise.
     * 
     * <pre>f(x, y) = x - y</pre>
     */
    public boolean subtractImages(ClearCLImageInterface arg1, ClearCLImageInterface arg2, ClearCLImageInterface arg3) {
        return SubtractImages.subtractImages(clijx, arg1, arg2, arg3);
    }


    // net.haesleinhuepf.clijx.advancedfilters.ShowGlasbeyOnGrey
    //----------------------------------------------------
    /**
     * Visualises two 2D images as one RGB image. The first channel is shown in grey, the second with glasbey LUT.
     */
    public boolean showGlasbeyOnGrey(ClearCLBuffer arg1, ClearCLBuffer arg2, String arg3) {
        return ShowGlasbeyOnGrey.showGlasbeyOnGrey(clij, arg1, arg2, arg3);
    }


    // net.haesleinhuepf.clijx.weka.ApplyWekaModel
    //----------------------------------------------------
    /**
     * Applies a Weka model using functionality of Fijis Trainable Weka Segmentation plugin.
     * It takes a 3D feature stack (e.g. first plane original image, second plane blurred, third plane edge image)and applies a pre-trained a Weka model. Take care that the feature stack has been generated in the sameway as for training the model!
     */
    public boolean applyWekaModel(ClearCLBuffer arg1, ClearCLBuffer arg2, String arg3) {
        return ApplyWekaModel.applyWekaModel(clijx, arg1, arg2, arg3);
    }


    // net.haesleinhuepf.clijx.weka.TrainWekaModel
    //----------------------------------------------------
    /**
     * Trains a Weka model using functionality of Fijis Trainable Weka Segmentation plugin.
     * It takes a 3D feature stack (e.g. first plane original image, second plane blurred, third plane edge image)and trains a Weka model. This model will be saved to disc.
     */
    public boolean trainWekaModel(ClearCLBuffer arg1, ClearCLBuffer arg2, String arg3) {
        return TrainWekaModel.trainWekaModel(clijx, arg1, arg2, arg3);
    }

}
// 430 methods generated.
