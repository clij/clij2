package net.haesleinhuepf.clij2;
import net.haesleinhuepf.clij2.CLIJ2;
import net.haesleinhuepf.clij.CLIJ;
import net.haesleinhuepf.clij.clearcl.ClearCLKernel;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.clearcl.ClearCLImage;
import net.haesleinhuepf.clij.clearcl.interfaces.ClearCLImageInterface;
import ij.measure.ResultsTable;
import ij.gui.Roi;
import ij.plugin.frame.RoiManager;
import net.haesleinhuepf.clij.kernels.Kernels;
import net.haesleinhuepf.clijx.plugins.BinaryUnion;
import net.haesleinhuepf.clijx.plugins.BinaryIntersection;
import net.haesleinhuepf.clijx.plugins.ConnectedComponentsLabeling;
import net.haesleinhuepf.clijx.plugins.CountNonZeroPixels;
import net.haesleinhuepf.clijx.plugins.CrossCorrelation;
import net.haesleinhuepf.clijx.plugins.DifferenceOfGaussian2D;
import net.haesleinhuepf.clijx.plugins.DifferenceOfGaussian3D;
import net.haesleinhuepf.clijx.plugins.Extrema;
import net.haesleinhuepf.clijx.plugins.LocalExtremaBox;
import net.haesleinhuepf.clijx.plugins.LocalID;
import net.haesleinhuepf.clijx.plugins.MaskLabel;
import net.haesleinhuepf.clijx.plugins.MeanClosestSpotDistance;
import net.haesleinhuepf.clijx.plugins.MeanSquaredError;
import net.haesleinhuepf.clijx.plugins.MedianZProjection;
import net.haesleinhuepf.clijx.plugins.NonzeroMinimumDiamond;
import net.haesleinhuepf.clijx.plugins.Paste2D;
import net.haesleinhuepf.clijx.plugins.Paste3D;
import net.haesleinhuepf.clijx.plugins.Presign;
import net.haesleinhuepf.clijx.plugins.JaccardIndex;
import net.haesleinhuepf.clijx.plugins.SorensenDiceCoefficent;
import net.haesleinhuepf.clijx.plugins.StandardDeviationZProjection;
import net.haesleinhuepf.clijx.plugins.StackToTiles;
import net.haesleinhuepf.clijx.plugins.SubtractBackground2D;
import net.haesleinhuepf.clijx.plugins.SubtractBackground3D;
import net.haesleinhuepf.clijx.plugins.TopHatBox;
import net.haesleinhuepf.clijx.plugins.TopHatSphere;
import net.haesleinhuepf.clijx.plugins.Exponential;
import net.haesleinhuepf.clijx.plugins.Logarithm;
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
import net.haesleinhuepf.clijx.plugins.SetWhereXequalsY;
import net.haesleinhuepf.clijx.plugins.LaplaceSphere;
import net.haesleinhuepf.clijx.plugins.Image2DToResultsTable;
import net.haesleinhuepf.clijx.plugins.WriteValuesToPositions;
import net.haesleinhuepf.clijx.plugins.GetSize;
import net.haesleinhuepf.clijx.matrix.MultiplyMatrix;
import net.haesleinhuepf.clijx.matrix.MatrixEqual;
import net.haesleinhuepf.clijx.plugins.PowerImages;
import net.haesleinhuepf.clijx.plugins.Equal;
import net.haesleinhuepf.clijx.plugins.GreaterOrEqual;
import net.haesleinhuepf.clijx.plugins.Greater;
import net.haesleinhuepf.clijx.plugins.Smaller;
import net.haesleinhuepf.clijx.plugins.SmallerOrEqual;
import net.haesleinhuepf.clijx.plugins.NotEqual;
import net.haesleinhuepf.clijx.io.ReadImageFromDisc;
import net.haesleinhuepf.clijx.io.ReadRawImageFromDisc;
import net.haesleinhuepf.clijx.io.PreloadFromDisc;
import net.haesleinhuepf.clijx.plugins.EqualConstant;
import net.haesleinhuepf.clijx.plugins.GreaterOrEqualConstant;
import net.haesleinhuepf.clijx.plugins.GreaterConstant;
import net.haesleinhuepf.clijx.plugins.SmallerConstant;
import net.haesleinhuepf.clijx.plugins.SmallerOrEqualConstant;
import net.haesleinhuepf.clijx.plugins.NotEqualConstant;
import net.haesleinhuepf.clijx.painting.DrawBox;
import net.haesleinhuepf.clijx.painting.DrawLine;
import net.haesleinhuepf.clijx.painting.DrawSphere;
import net.haesleinhuepf.clijx.plugins.ReplaceIntensity;
import net.haesleinhuepf.clijx.plugins.BoundingBox;
import net.haesleinhuepf.clijx.plugins.MinimumOfMaskedPixels;
import net.haesleinhuepf.clijx.plugins.MaximumOfMaskedPixels;
import net.haesleinhuepf.clijx.plugins.MeanOfMaskedPixels;
import net.haesleinhuepf.clijx.plugins.LabelToMask;
import net.haesleinhuepf.clijx.matrix.NClosestPoints;
import net.haesleinhuepf.clijx.matrix.GaussJordan;
import net.haesleinhuepf.clijx.plugins.StatisticsOfLabelledPixels;
import net.haesleinhuepf.clijx.plugins.VarianceOfAllPixels;
import net.haesleinhuepf.clijx.plugins.StandardDeviationOfAllPixels;
import net.haesleinhuepf.clijx.plugins.VarianceOfMaskedPixels;
import net.haesleinhuepf.clijx.plugins.StandardDeviationOfMaskedPixels;
import net.haesleinhuepf.clijx.plugins.ExcludeLabelsOnEdges;
import net.haesleinhuepf.clijx.plugins.BinarySubtract;
import net.haesleinhuepf.clijx.plugins.BinaryEdgeDetection;
import net.haesleinhuepf.clijx.plugins.DistanceMap;
import net.haesleinhuepf.clijx.plugins.PullAsROI;
import net.haesleinhuepf.clijx.plugins.PullLabelsToROIManager;
import net.haesleinhuepf.clijx.plugins.NonzeroMaximumDiamond;
import net.haesleinhuepf.clijx.plugins.OnlyzeroOverwriteMaximumDiamond;
import net.haesleinhuepf.clijx.plugins.OnlyzeroOverwriteMaximumBox;
import net.haesleinhuepf.clijx.matrix.GenerateTouchMatrix;
import net.haesleinhuepf.clijx.plugins.DetectLabelEdges;
import net.haesleinhuepf.clijx.plugins.StopWatch;
import net.haesleinhuepf.clijx.matrix.CountTouchingNeighbors;
import net.haesleinhuepf.clijx.plugins.ReplaceIntensities;
import net.haesleinhuepf.clijx.painting.DrawTwoValueLine;
import net.haesleinhuepf.clijx.matrix.AverageDistanceOfNClosestPoints;
import net.haesleinhuepf.clijx.plugins.SaveAsTIF;
import net.haesleinhuepf.clijx.plugins.ConnectedComponentsLabelingInplace;
import net.haesleinhuepf.clijx.matrix.TouchMatrixToMesh;
import net.haesleinhuepf.clijx.plugins.AutomaticThresholdInplace;
import net.haesleinhuepf.clijx.plugins.DifferenceOfGaussianInplace3D;
import net.haesleinhuepf.clijx.plugins.AbsoluteInplace;
import net.haesleinhuepf.clijx.plugins.Resample;
import net.haesleinhuepf.clijx.plugins.EqualizeMeanIntensitiesOfSlices;
import net.haesleinhuepf.clijx.plugins.Watershed;
import net.haesleinhuepf.clijx.plugins.ResliceRadial;
import net.haesleinhuepf.clijx.plugins.ShowRGB;
import net.haesleinhuepf.clijx.plugins.ShowGrey;
import net.haesleinhuepf.clijx.plugins.Sobel;
import net.haesleinhuepf.clij2.plugins.Absolute;
import net.haesleinhuepf.clijx.plugins.LaplaceBox;
import net.haesleinhuepf.clijx.plugins.BottomHatBox;
import net.haesleinhuepf.clijx.plugins.BottomHatSphere;
import net.haesleinhuepf.clijx.plugins.ClosingBox;
import net.haesleinhuepf.clijx.plugins.ClosingDiamond;
import net.haesleinhuepf.clijx.plugins.OpeningBox;
import net.haesleinhuepf.clijx.plugins.OpeningDiamond;
import net.haesleinhuepf.clijx.plugins.MaximumXProjection;
import net.haesleinhuepf.clijx.plugins.MaximumYProjection;
import net.haesleinhuepf.clijx.plugins.ProjectMaximumZBounded;
import net.haesleinhuepf.clijx.plugins.ProjectMinimumZBounded;
import net.haesleinhuepf.clijx.plugins.ProjectMeanZBounded;
import net.haesleinhuepf.clijx.plugins.NonzeroMaximumBox;
import net.haesleinhuepf.clijx.plugins.NonzeroMinimumBox;
import net.haesleinhuepf.clijx.plugins.ProjectMinimumThresholdedZBounded;
import net.haesleinhuepf.clijx.plugins.MeanOfPixelsAboveThreshold;
import net.haesleinhuepf.clijx.gui.OrganiseWindows;
import net.haesleinhuepf.clijx.matrix.DistanceMatrixToMesh;
import net.haesleinhuepf.clijx.matrix.PointIndexListToMesh;
import net.haesleinhuepf.clijx.plugins.MinimumOctagon;
import net.haesleinhuepf.clijx.plugins.MaximumOctagon;
import net.haesleinhuepf.clijx.plugins.TopHatOctagon;
import net.haesleinhuepf.clij2.plugins.AddImages;
import net.haesleinhuepf.clij2.plugins.AddImagesWeighted;
import net.haesleinhuepf.clij2.plugins.SubtractImages;
import net.haesleinhuepf.clijx.plugins.ShowGlasbeyOnGrey;
import net.haesleinhuepf.clijx.weka.ApplyWekaModel;
import net.haesleinhuepf.clijx.weka.TrainWekaModel;
import net.haesleinhuepf.clijx.plugins.AffineTransform2D;
import net.haesleinhuepf.clijx.plugins.AffineTransform3D;
import net.haesleinhuepf.clijx.plugins.ApplyVectorField2D;
import net.haesleinhuepf.clijx.plugins.ApplyVectorField3D;
import net.haesleinhuepf.clijx.plugins.ArgMaximumZProjection;
import net.haesleinhuepf.clijx.plugins.Histogram;
import net.haesleinhuepf.clijx.plugins.AutomaticThreshold;
import net.haesleinhuepf.clijx.plugins.Threshold;
import net.haesleinhuepf.clijx.plugins.BinaryOr;
import net.haesleinhuepf.clijx.plugins.BinaryAnd;
import net.haesleinhuepf.clijx.plugins.BinaryXOr;
import net.haesleinhuepf.clijx.plugins.BinaryNot;
import net.haesleinhuepf.clijx.plugins.ErodeSphere;
import net.haesleinhuepf.clijx.plugins.ErodeBox;
import net.haesleinhuepf.clijx.plugins.ErodeSphereSliceBySlice;
import net.haesleinhuepf.clijx.plugins.ErodeBoxSliceBySlice;
import net.haesleinhuepf.clijx.plugins.DilateSphere;
import net.haesleinhuepf.clijx.plugins.DilateBox;
import net.haesleinhuepf.clijx.plugins.DilateSphereSliceBySlice;
import net.haesleinhuepf.clijx.plugins.DilateBoxSliceBySlice;
import net.haesleinhuepf.clijx.plugins.Copy;
import net.haesleinhuepf.clijx.plugins.CopySlice;
import net.haesleinhuepf.clijx.plugins.Crop2D;
import net.haesleinhuepf.clijx.plugins.Crop3D;
import net.haesleinhuepf.clijx.plugins.Set;
import net.haesleinhuepf.clijx.plugins.Flip2D;
import net.haesleinhuepf.clijx.plugins.Flip3D;
import net.haesleinhuepf.clijx.plugins.RotateLeft;
import net.haesleinhuepf.clijx.plugins.RotateRight;
import net.haesleinhuepf.clijx.plugins.Mask;
import net.haesleinhuepf.clijx.plugins.MaskStackWithPlane;
import net.haesleinhuepf.clijx.plugins.MaximumZProjection;
import net.haesleinhuepf.clijx.plugins.MeanZProjection;
import net.haesleinhuepf.clijx.plugins.MinimumZProjection;
import net.haesleinhuepf.clijx.plugins.Power;
import net.haesleinhuepf.clijx.plugins.tenengradfusion.AbstractTenengradFusion;
import net.haesleinhuepf.clijx.plugins.DivideImages;
import net.haesleinhuepf.clijx.plugins.MaximumImages;
import net.haesleinhuepf.clijx.plugins.MaximumImageAndScalar;
import net.haesleinhuepf.clijx.plugins.MinimumImages;
import net.haesleinhuepf.clijx.plugins.MinimumImageAndScalar;
import net.haesleinhuepf.clijx.plugins.MultiplyImageAndScalar;
import net.haesleinhuepf.clijx.plugins.MultiplyStackWithPlane;
import net.haesleinhuepf.clijx.plugins.CountNonZeroPixels2DSphere;
import net.haesleinhuepf.clijx.plugins.CountNonZeroPixelsSliceBySliceSphere;
import net.haesleinhuepf.clijx.plugins.CountNonZeroVoxels3DSphere;
import net.haesleinhuepf.clijx.plugins.SumZProjection;
import net.haesleinhuepf.clijx.plugins.SumOfAllPixels;
import net.haesleinhuepf.clijx.plugins.CenterOfMass;
import net.haesleinhuepf.clijx.plugins.Invert;
import net.haesleinhuepf.clijx.plugins.Downsample2D;
import net.haesleinhuepf.clijx.plugins.Downsample3D;
import net.haesleinhuepf.clijx.plugins.DownsampleSliceBySliceHalfMedian;
import net.haesleinhuepf.clijx.plugins.LocalThreshold;
import net.haesleinhuepf.clijx.plugins.GradientX;
import net.haesleinhuepf.clijx.plugins.GradientY;
import net.haesleinhuepf.clijx.plugins.GradientZ;
import net.haesleinhuepf.clijx.plugins.MultiplyImageAndCoordinate;
import net.haesleinhuepf.clijx.plugins.Mean2DBox;
import net.haesleinhuepf.clijx.plugins.Mean2DSphere;
import net.haesleinhuepf.clijx.plugins.Mean3DBox;
import net.haesleinhuepf.clijx.plugins.Mean3DSphere;
import net.haesleinhuepf.clijx.plugins.MeanSliceBySliceSphere;
import net.haesleinhuepf.clijx.plugins.MeanOfAllPixels;
import net.haesleinhuepf.clijx.plugins.Median2DBox;
import net.haesleinhuepf.clijx.plugins.Median2DSphere;
import net.haesleinhuepf.clijx.plugins.Median3DBox;
import net.haesleinhuepf.clijx.plugins.Median3DSphere;
import net.haesleinhuepf.clijx.plugins.MedianSliceBySliceBox;
import net.haesleinhuepf.clijx.plugins.MedianSliceBySliceSphere;
import net.haesleinhuepf.clijx.plugins.Maximum2DSphere;
import net.haesleinhuepf.clijx.plugins.Maximum3DSphere;
import net.haesleinhuepf.clijx.plugins.Maximum2DBox;
import net.haesleinhuepf.clijx.plugins.Maximum3DBox;
import net.haesleinhuepf.clijx.plugins.MaximumSliceBySliceSphere;
import net.haesleinhuepf.clijx.plugins.Minimum2DSphere;
import net.haesleinhuepf.clijx.plugins.Minimum3DSphere;
import net.haesleinhuepf.clijx.plugins.Minimum2DBox;
import net.haesleinhuepf.clijx.plugins.Minimum3DBox;
import net.haesleinhuepf.clijx.plugins.MinimumSliceBySliceSphere;
import net.haesleinhuepf.clijx.plugins.MultiplyImages;
import net.haesleinhuepf.clijx.plugins.Blur2D;
import net.haesleinhuepf.clijx.plugins.Blur3D;
import net.haesleinhuepf.clijx.plugins.Blur3DSliceBySlice;
import net.haesleinhuepf.clijx.plugins.ResliceBottom;
import net.haesleinhuepf.clijx.plugins.ResliceTop;
import net.haesleinhuepf.clijx.plugins.ResliceLeft;
import net.haesleinhuepf.clijx.plugins.ResliceRight;
import net.haesleinhuepf.clijx.plugins.Rotate2D;
import net.haesleinhuepf.clijx.plugins.Rotate3D;
import net.haesleinhuepf.clijx.plugins.Scale2D;
import net.haesleinhuepf.clijx.plugins.Scale3D;
import net.haesleinhuepf.clijx.plugins.Translate2D;
import net.haesleinhuepf.clijx.plugins.Translate3D;
import net.haesleinhuepf.clijx.base.Clear;
import net.haesleinhuepf.clijx.base.ClInfo;
import net.haesleinhuepf.clijx.base.ConvertFloat;
import net.haesleinhuepf.clijx.base.ConvertUInt8;
import net.haesleinhuepf.clijx.base.ConvertUInt16;
import net.haesleinhuepf.clijx.base.Create2D;
import net.haesleinhuepf.clijx.base.Create3D;
import net.haesleinhuepf.clijx.base.Pull;
import net.haesleinhuepf.clijx.base.PullBinary;
import net.haesleinhuepf.clijx.base.Push;
import net.haesleinhuepf.clijx.base.PushCurrentSlice;
import net.haesleinhuepf.clijx.base.PushCurrentZStack;
import net.haesleinhuepf.clijx.base.Release;
import net.haesleinhuepf.clij2.plugins.AddImageAndScalar;
import net.haesleinhuepf.clijx.plugins.DetectMinimaBox;
import net.haesleinhuepf.clijx.plugins.DetectMaximaBox;
import net.haesleinhuepf.clijx.plugins.DetectMaximaSliceBySliceBox;
import net.haesleinhuepf.clijx.plugins.DetectMinimaSliceBySliceBox;
import net.haesleinhuepf.clijx.plugins.MaximumOfAllPixels;
import net.haesleinhuepf.clijx.plugins.MinimumOfAllPixels;
import net.haesleinhuepf.clijx.plugins.ReportMemory;
import net.haesleinhuepf.clijx.plugins.splitstack.AbstractSplitStack;
import net.haesleinhuepf.clijx.plugins.TopHatOctagonSliceBySlice;
import net.haesleinhuepf.clijx.plugins.SetColumn;
import net.haesleinhuepf.clijx.plugins.SetRow;
import net.haesleinhuepf.clijx.plugins.SumYProjection;
import net.haesleinhuepf.clijx.matrix.AverageDistanceOfTouchingNeighbors;
import net.haesleinhuepf.clijx.matrix.LabelledSpotsToPointList;
import net.haesleinhuepf.clijx.matrix.LabelSpots;
import net.haesleinhuepf.clijx.matrix.MinimumDistanceOfTouchingNeighbors;
import net.haesleinhuepf.clijx.io.WriteVTKLineListToDisc;
import net.haesleinhuepf.clijx.io.WriteXYZPointListToDisc;
// this is generated code. See src/test/java/net/haesleinhuepf/clijx/codegenerator for details
public abstract interface CLIJ2Ops {
   CLIJ getCLIJ();
   CLIJ2 getCLIJ2();
   

    // net.haesleinhuepf.clij2.plugins.Absolute
    //----------------------------------------------------
    /**
     * Computes the absolute value of every individual pixel x in a given image.
     * 
     * <pre>f(x) = |x| </pre>
     */
    default boolean absolute(ClearCLImageInterface arg1, ClearCLImageInterface arg2) {
        return Absolute.absolute(getCLIJ2(), arg1, arg2);
    }


    // net.haesleinhuepf.clij2.plugins.AddImages
    //----------------------------------------------------
    /**
     * Calculates the sum of pairs of pixels x and y of two images X and Y.
     * 
     * <pre>f(x, y) = x + y</pre>
     */
    default boolean addImages(ClearCLImageInterface arg1, ClearCLImageInterface arg2, ClearCLImageInterface arg3) {
        return AddImages.addImages(getCLIJ2(), arg1, arg2, arg3);
    }


    // net.haesleinhuepf.clij2.plugins.AddImagesWeighted
    //----------------------------------------------------
    /**
     * Calculates the sum of pairs of pixels x and y from images X and Y weighted with factors a and b.
     * 
     * <pre>f(x, y, a, b) = x * a + y * b</pre>
     */
    default boolean addImagesWeighted(ClearCLImageInterface arg1, ClearCLImageInterface arg2, ClearCLImageInterface arg3, double arg4, double arg5) {
        return AddImagesWeighted.addImagesWeighted(getCLIJ2(), arg1, arg2, arg3, new Double (arg4).floatValue(), new Double (arg5).floatValue());
    }


    // net.haesleinhuepf.clij2.plugins.SubtractImages
    //----------------------------------------------------
    /**
     * Subtracts one image X from another image Y pixel wise.
     * 
     * <pre>f(x, y) = x - y</pre>
     */
    @Deprecated
    default boolean subtract(ClearCLImageInterface arg1, ClearCLImageInterface arg2, ClearCLImageInterface arg3) {
        return SubtractImages.subtract(getCLIJ2(), arg1, arg2, arg3);
    }

    /**
     * Subtracts one image X from another image Y pixel wise.
     * 
     * <pre>f(x, y) = x - y</pre>
     */
    default boolean subtractImages(ClearCLImageInterface arg1, ClearCLImageInterface arg2, ClearCLImageInterface arg3) {
        return SubtractImages.subtractImages(getCLIJ2(), arg1, arg2, arg3);
    }


    // net.haesleinhuepf.clij2.plugins.AddImageAndScalar
    //----------------------------------------------------
    /**
     * Adds a scalar value s to all pixels x of a given image X.
     * 
     * <pre>f(x, s) = x + s</pre>
     */
    default boolean addImageAndScalar(ClearCLImageInterface arg1, ClearCLImageInterface arg2, double arg3) {
        return AddImageAndScalar.addImageAndScalar(getCLIJ2(), arg1, arg2, new Double (arg3).floatValue());
    }

}
// 6 methods generated.
