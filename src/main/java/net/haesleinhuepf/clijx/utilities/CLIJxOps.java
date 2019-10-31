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
import net.haesleinhuepf.clij.clij1wrappers.CLIJ1Absolute;
import net.haesleinhuepf.clij.clij1wrappers.CLIJ1Mask;
import net.haesleinhuepf.clij.clij1wrappers.CLIJ1AddImageAndScalar;
import net.haesleinhuepf.clij.clij1wrappers.CLIJ1MaskStackWithPlane;
import net.haesleinhuepf.clij.clij1wrappers.CLIJ1AddImages;
import net.haesleinhuepf.clij.clij1wrappers.CLIJ1MaximumBox;
import net.haesleinhuepf.clij.clij1wrappers.CLIJ1AddImagesWeighted;
import net.haesleinhuepf.clij.clij1wrappers.CLIJ1MaximumIJ;
import net.haesleinhuepf.clij.clij1wrappers.CLIJ1AffineTransform2D;
import net.haesleinhuepf.clij.clij1wrappers.CLIJ1MaximumImageAndScalar;
import net.haesleinhuepf.clij.clij1wrappers.CLIJ1AffineTransform3D;
import net.haesleinhuepf.clij.clij1wrappers.CLIJ1MaximumImages;
import net.haesleinhuepf.clij.clij1wrappers.CLIJ1AffineTransform;
import net.haesleinhuepf.clij.clij1wrappers.CLIJ1MaximumOfAllPixels;
import net.haesleinhuepf.clij.clij1wrappers.CLIJ1ApplyVectorfield;
import net.haesleinhuepf.clij.clij1wrappers.CLIJ1MaximumSliceBySliceSphere;
import net.haesleinhuepf.clij.clij1wrappers.CLIJ1ArgMaximumZProjection;
import net.haesleinhuepf.clij.clij1wrappers.CLIJ1MaximumSphere;
import net.haesleinhuepf.clij.clij1wrappers.CLIJ1AutomaticThreshold;
import net.haesleinhuepf.clij.clij1wrappers.CLIJ1MaximumXYZProjection;
import net.haesleinhuepf.clij.clij1wrappers.CLIJ1BinaryAnd;
import net.haesleinhuepf.clij.clij1wrappers.CLIJ1MaximumZProjection;
import net.haesleinhuepf.clij.clij1wrappers.CLIJ1BinaryNot;
import net.haesleinhuepf.clij.clij1wrappers.CLIJ1MeanBox;
import net.haesleinhuepf.clij.clij1wrappers.CLIJ1BinaryOr;
import net.haesleinhuepf.clij.clij1wrappers.CLIJ1MeanIJ;
import net.haesleinhuepf.clij.clij1wrappers.CLIJ1BinaryXOr;
import net.haesleinhuepf.clij.clij1wrappers.CLIJ1MeanSliceBySliceSphere;
import net.haesleinhuepf.clij.clij1wrappers.CLIJ1Blur;
import net.haesleinhuepf.clij.clij1wrappers.CLIJ1MeanSphere;
import net.haesleinhuepf.clij.clij1wrappers.CLIJ1BlurSliceBySlice;
import net.haesleinhuepf.clij.clij1wrappers.CLIJ1MeanZProjection;
import net.haesleinhuepf.clij.clij1wrappers.CLIJ1CenterOfMass;
import net.haesleinhuepf.clij.clij1wrappers.CLIJ1MedianBox;
import net.haesleinhuepf.clij.clij1wrappers.CLIJ1ConvertToImageJBinary;
import net.haesleinhuepf.clij.clij1wrappers.CLIJ1MedianSliceBySliceBox;
import net.haesleinhuepf.clij.clij1wrappers.CLIJ1Copy;
import net.haesleinhuepf.clij.clij1wrappers.CLIJ1MedianSliceBySliceSphere;
import net.haesleinhuepf.clij.clij1wrappers.CLIJ1CopySlice;
import net.haesleinhuepf.clij.clij1wrappers.CLIJ1MedianSphere;
import net.haesleinhuepf.clij.clij1wrappers.CLIJ1CountNonZeroPixelsLocally;
import net.haesleinhuepf.clij.clij1wrappers.CLIJ1MinimumBox;
import net.haesleinhuepf.clij.clij1wrappers.CLIJ1CountNonZeroPixelsLocallySliceBySlice;
import net.haesleinhuepf.clij.clij1wrappers.CLIJ1MinimumIJ;
import net.haesleinhuepf.clij.clij1wrappers.CLIJ1CountNonZeroVoxelsLocally;
import net.haesleinhuepf.clij.clij1wrappers.CLIJ1MinimumImageAndScalar;
import net.haesleinhuepf.clij.clij1wrappers.CLIJ1Crop;
import net.haesleinhuepf.clij.clij1wrappers.CLIJ1MinimumImages;
import net.haesleinhuepf.clij.clij1wrappers.CLIJ1DetectMaximaBox;
import net.haesleinhuepf.clij.clij1wrappers.CLIJ1MinimumOfAllPixels;
import net.haesleinhuepf.clij.clij1wrappers.CLIJ1DetectMaximaSliceBySliceBox;
import net.haesleinhuepf.clij.clij1wrappers.CLIJ1MinimumSliceBySliceSphere;
import net.haesleinhuepf.clij.clij1wrappers.CLIJ1DetectMinimaBox;
import net.haesleinhuepf.clij.clij1wrappers.CLIJ1MinimumSphere;
import net.haesleinhuepf.clij.clij1wrappers.CLIJ1DetectMinimaSliceBySliceBox;
import net.haesleinhuepf.clij.clij1wrappers.CLIJ1MinimumZProjection;
import net.haesleinhuepf.clij.clij1wrappers.CLIJ1DetectOptima;
import net.haesleinhuepf.clij.clij1wrappers.CLIJ1MultiplyImageAndCoordinate;
import net.haesleinhuepf.clij.clij1wrappers.CLIJ1DetectOptimaSliceBySlice;
import net.haesleinhuepf.clij.clij1wrappers.CLIJ1MultiplyImageAndScalar;
import net.haesleinhuepf.clij.clij1wrappers.CLIJ1DifferenceOfGaussian;
import net.haesleinhuepf.clij.clij1wrappers.CLIJ1MultiplyImages;
import net.haesleinhuepf.clij.clij1wrappers.CLIJ1DifferenceOfGaussianSliceBySlice;
import net.haesleinhuepf.clij.clij1wrappers.CLIJ1MultiplySliceBySliceWithScalars;
import net.haesleinhuepf.clij.clij1wrappers.CLIJ1DilateBox;
import net.haesleinhuepf.clij.clij1wrappers.CLIJ1MultiplyStackWithPlane;
import net.haesleinhuepf.clij.clij1wrappers.CLIJ1DilateBoxSliceBySlice;
import net.haesleinhuepf.clij.clij1wrappers.CLIJ1Power;
import net.haesleinhuepf.clij.clij1wrappers.CLIJ1DilateSphere;
import net.haesleinhuepf.clij.clij1wrappers.CLIJ1RadialProjection;
import net.haesleinhuepf.clij.clij1wrappers.CLIJ1DilateSphereSliceBySlice;
import net.haesleinhuepf.clij.clij1wrappers.CLIJ1ResliceBottom;
import net.haesleinhuepf.clij.clij1wrappers.CLIJ1DivideImages;
import net.haesleinhuepf.clij.clij1wrappers.CLIJ1ResliceLeft;
import net.haesleinhuepf.clij.clij1wrappers.CLIJ1Downsample;
import net.haesleinhuepf.clij.clij1wrappers.CLIJ1ResliceRight;
import net.haesleinhuepf.clij.clij1wrappers.CLIJ1DownsampleSliceBySliceHalfMedian;
import net.haesleinhuepf.clij.clij1wrappers.CLIJ1ResliceTop;
import net.haesleinhuepf.clij.clij1wrappers.CLIJ1ErodeBox;
import net.haesleinhuepf.clij.clij1wrappers.CLIJ1RotateLeft;
import net.haesleinhuepf.clij.clij1wrappers.CLIJ1ErodeBoxSliceBySlice;
import net.haesleinhuepf.clij.clij1wrappers.CLIJ1RotateRight;
import net.haesleinhuepf.clij.clij1wrappers.CLIJ1ErodeSphere;
import net.haesleinhuepf.clij.clij1wrappers.CLIJ1Set;
import net.haesleinhuepf.clij.clij1wrappers.CLIJ1ErodeSphereSliceBySlice;
import net.haesleinhuepf.clij.clij1wrappers.CLIJ1SplitStack;
import net.haesleinhuepf.clij.clij1wrappers.CLIJ1FillHistogram;
import net.haesleinhuepf.clij.clij1wrappers.CLIJ1SubtractImages;
import net.haesleinhuepf.clij.clij1wrappers.CLIJ1Flip;
import net.haesleinhuepf.clij.clij1wrappers.CLIJ1Subtract;
import net.haesleinhuepf.clij.clij1wrappers.CLIJ1GradientX;
import net.haesleinhuepf.clij.clij1wrappers.CLIJ1SumPixels;
import net.haesleinhuepf.clij.clij1wrappers.CLIJ1GradientY;
import net.haesleinhuepf.clij.clij1wrappers.CLIJ1SumPixelsSliceBySlice;
import net.haesleinhuepf.clij.clij1wrappers.CLIJ1GradientZ;
import net.haesleinhuepf.clij.clij1wrappers.CLIJ1SumZProjection;
import net.haesleinhuepf.clij.clij1wrappers.CLIJ1Histogram;
import net.haesleinhuepf.clij.clij1wrappers.CLIJ1TenengradFusion;
import net.haesleinhuepf.clij.clij1wrappers.CLIJ1Invert;
import net.haesleinhuepf.clij.clij1wrappers.CLIJ1TenengradWeightsSliceBySlice;
import net.haesleinhuepf.clij.clij1wrappers.CLIJ1LocalThreshold;
import net.haesleinhuepf.clij.clij1wrappers.CLIJ1Threshold;
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
import net.haesleinhuepf.clij.advancedfilters.NonzeroMinimumDiamond;
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
import net.haesleinhuepf.clij.advancedfilters.Image2DToResultsTable;
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
import net.haesleinhuepf.clij.io.ReadImageFromDisc;
import net.haesleinhuepf.clij.io.ReadRawImageFromDisc;
import net.haesleinhuepf.clij.io.PreloadFromDisc;
import net.haesleinhuepf.clij.advancedmath.EqualConstant;
import net.haesleinhuepf.clij.advancedmath.GreaterOrEqualConstant;
import net.haesleinhuepf.clij.advancedmath.GreaterConstant;
import net.haesleinhuepf.clij.advancedmath.SmallerConstant;
import net.haesleinhuepf.clij.advancedmath.SmallerOrEqualConstant;
import net.haesleinhuepf.clij.advancedmath.NotEqualConstant;
import net.haesleinhuepf.clij.painting.DrawBox;
import net.haesleinhuepf.clij.painting.DrawLine;
import net.haesleinhuepf.clij.painting.DrawSphere;
import net.haesleinhuepf.clij.advancedfilters.ReplaceIntensity;
import net.haesleinhuepf.clij.advancedfilters.BoundingBox;
import net.haesleinhuepf.clij.advancedfilters.MinimumOfMaskedPixels;
import net.haesleinhuepf.clij.advancedfilters.MaximumOfMaskedPixels;
import net.haesleinhuepf.clij.advancedfilters.MeanOfMaskedPixels;
import net.haesleinhuepf.clij.advancedfilters.LabelToMask;
import net.haesleinhuepf.clij.matrix.NClosestPoints;
import net.haesleinhuepf.clij.matrix.GaussJordan;
import net.haesleinhuepf.clij.advancedfilters.StatisticsOfLabelledPixels;
import net.haesleinhuepf.clij.advancedfilters.VarianceOfAllPixels;
import net.haesleinhuepf.clij.advancedfilters.StandardDeviationOfAllPixels;
import net.haesleinhuepf.clij.advancedfilters.ExcludeLabelsOnEdges;
import net.haesleinhuepf.clij.advancedfilters.BinarySubtract;
import net.haesleinhuepf.clij.advancedfilters.BinaryEdgeDetection;
import net.haesleinhuepf.clij.advancedfilters.DistanceMap;
import net.haesleinhuepf.clij.advancedfilters.PullAsROI;
import net.haesleinhuepf.clij.advancedfilters.NonzeroMaximumDiamond;
import net.haesleinhuepf.clij.advancedfilters.OnlyzeroOverwriteMaximumDiamond;
import net.haesleinhuepf.clij.advancedfilters.OnlyzeroOverwriteMaximumBox;
import net.haesleinhuepf.clij.matrix.GenerateTouchMatrix;
import net.haesleinhuepf.clij.advancedfilters.DetectLabelEdges;
import net.haesleinhuepf.clij.advancedfilters.StopWatch;
import net.haesleinhuepf.clij.matrix.CountTouchingNeighbors;
import net.haesleinhuepf.clij.advancedfilters.ReplaceIntensities;
import net.haesleinhuepf.clij.painting.DrawTwoValueLine;
import net.haesleinhuepf.clij.matrix.AverageDistanceOfNClosestPoints;
import net.haesleinhuepf.clij.advancedfilters.SaveAsTIF;
// this is generated code. See src/test/java/net/haesleinhuepf/clijx/codegenerator for details
public abstract class CLIJxOps {
   protected CLIJ clij;
   protected CLIJx clijx;
   

    // net.haesleinhuepf.clij.clij1wrappers.CLIJ1Absolute
    //----------------------------------------------------

    // net.haesleinhuepf.clij.clij1wrappers.CLIJ1Mask
    //----------------------------------------------------

    // net.haesleinhuepf.clij.clij1wrappers.CLIJ1AddImageAndScalar
    //----------------------------------------------------

    // net.haesleinhuepf.clij.clij1wrappers.CLIJ1MaskStackWithPlane
    //----------------------------------------------------

    // net.haesleinhuepf.clij.clij1wrappers.CLIJ1AddImages
    //----------------------------------------------------

    // net.haesleinhuepf.clij.clij1wrappers.CLIJ1MaximumBox
    //----------------------------------------------------

    // net.haesleinhuepf.clij.clij1wrappers.CLIJ1AddImagesWeighted
    //----------------------------------------------------

    // net.haesleinhuepf.clij.clij1wrappers.CLIJ1MaximumIJ
    //----------------------------------------------------

    // net.haesleinhuepf.clij.clij1wrappers.CLIJ1AffineTransform2D
    //----------------------------------------------------

    // net.haesleinhuepf.clij.clij1wrappers.CLIJ1MaximumImageAndScalar
    //----------------------------------------------------

    // net.haesleinhuepf.clij.clij1wrappers.CLIJ1AffineTransform3D
    //----------------------------------------------------

    // net.haesleinhuepf.clij.clij1wrappers.CLIJ1MaximumImages
    //----------------------------------------------------

    // net.haesleinhuepf.clij.clij1wrappers.CLIJ1AffineTransform
    //----------------------------------------------------

    // net.haesleinhuepf.clij.clij1wrappers.CLIJ1MaximumOfAllPixels
    //----------------------------------------------------

    // net.haesleinhuepf.clij.clij1wrappers.CLIJ1ApplyVectorfield
    //----------------------------------------------------

    // net.haesleinhuepf.clij.clij1wrappers.CLIJ1MaximumSliceBySliceSphere
    //----------------------------------------------------

    // net.haesleinhuepf.clij.clij1wrappers.CLIJ1ArgMaximumZProjection
    //----------------------------------------------------

    // net.haesleinhuepf.clij.clij1wrappers.CLIJ1MaximumSphere
    //----------------------------------------------------

    // net.haesleinhuepf.clij.clij1wrappers.CLIJ1AutomaticThreshold
    //----------------------------------------------------

    // net.haesleinhuepf.clij.clij1wrappers.CLIJ1MaximumXYZProjection
    //----------------------------------------------------

    // net.haesleinhuepf.clij.clij1wrappers.CLIJ1BinaryAnd
    //----------------------------------------------------

    // net.haesleinhuepf.clij.clij1wrappers.CLIJ1MaximumZProjection
    //----------------------------------------------------

    // net.haesleinhuepf.clij.clij1wrappers.CLIJ1BinaryNot
    //----------------------------------------------------

    // net.haesleinhuepf.clij.clij1wrappers.CLIJ1MeanBox
    //----------------------------------------------------

    // net.haesleinhuepf.clij.clij1wrappers.CLIJ1BinaryOr
    //----------------------------------------------------

    // net.haesleinhuepf.clij.clij1wrappers.CLIJ1MeanIJ
    //----------------------------------------------------

    // net.haesleinhuepf.clij.clij1wrappers.CLIJ1BinaryXOr
    //----------------------------------------------------

    // net.haesleinhuepf.clij.clij1wrappers.CLIJ1MeanSliceBySliceSphere
    //----------------------------------------------------

    // net.haesleinhuepf.clij.clij1wrappers.CLIJ1Blur
    //----------------------------------------------------

    // net.haesleinhuepf.clij.clij1wrappers.CLIJ1MeanSphere
    //----------------------------------------------------

    // net.haesleinhuepf.clij.clij1wrappers.CLIJ1BlurSliceBySlice
    //----------------------------------------------------

    // net.haesleinhuepf.clij.clij1wrappers.CLIJ1MeanZProjection
    //----------------------------------------------------

    // net.haesleinhuepf.clij.clij1wrappers.CLIJ1CenterOfMass
    //----------------------------------------------------

    // net.haesleinhuepf.clij.clij1wrappers.CLIJ1MedianBox
    //----------------------------------------------------

    // net.haesleinhuepf.clij.clij1wrappers.CLIJ1ConvertToImageJBinary
    //----------------------------------------------------

    // net.haesleinhuepf.clij.clij1wrappers.CLIJ1MedianSliceBySliceBox
    //----------------------------------------------------

    // net.haesleinhuepf.clij.clij1wrappers.CLIJ1Copy
    //----------------------------------------------------

    // net.haesleinhuepf.clij.clij1wrappers.CLIJ1MedianSliceBySliceSphere
    //----------------------------------------------------

    // net.haesleinhuepf.clij.clij1wrappers.CLIJ1CopySlice
    //----------------------------------------------------

    // net.haesleinhuepf.clij.clij1wrappers.CLIJ1MedianSphere
    //----------------------------------------------------

    // net.haesleinhuepf.clij.clij1wrappers.CLIJ1CountNonZeroPixelsLocally
    //----------------------------------------------------

    // net.haesleinhuepf.clij.clij1wrappers.CLIJ1MinimumBox
    //----------------------------------------------------

    // net.haesleinhuepf.clij.clij1wrappers.CLIJ1CountNonZeroPixelsLocallySliceBySlice
    //----------------------------------------------------

    // net.haesleinhuepf.clij.clij1wrappers.CLIJ1MinimumIJ
    //----------------------------------------------------

    // net.haesleinhuepf.clij.clij1wrappers.CLIJ1CountNonZeroVoxelsLocally
    //----------------------------------------------------

    // net.haesleinhuepf.clij.clij1wrappers.CLIJ1MinimumImageAndScalar
    //----------------------------------------------------

    // net.haesleinhuepf.clij.clij1wrappers.CLIJ1Crop
    //----------------------------------------------------

    // net.haesleinhuepf.clij.clij1wrappers.CLIJ1MinimumImages
    //----------------------------------------------------

    // net.haesleinhuepf.clij.clij1wrappers.CLIJ1DetectMaximaBox
    //----------------------------------------------------

    // net.haesleinhuepf.clij.clij1wrappers.CLIJ1MinimumOfAllPixels
    //----------------------------------------------------

    // net.haesleinhuepf.clij.clij1wrappers.CLIJ1DetectMaximaSliceBySliceBox
    //----------------------------------------------------

    // net.haesleinhuepf.clij.clij1wrappers.CLIJ1MinimumSliceBySliceSphere
    //----------------------------------------------------

    // net.haesleinhuepf.clij.clij1wrappers.CLIJ1DetectMinimaBox
    //----------------------------------------------------

    // net.haesleinhuepf.clij.clij1wrappers.CLIJ1MinimumSphere
    //----------------------------------------------------

    // net.haesleinhuepf.clij.clij1wrappers.CLIJ1DetectMinimaSliceBySliceBox
    //----------------------------------------------------

    // net.haesleinhuepf.clij.clij1wrappers.CLIJ1MinimumZProjection
    //----------------------------------------------------

    // net.haesleinhuepf.clij.clij1wrappers.CLIJ1DetectOptima
    //----------------------------------------------------

    // net.haesleinhuepf.clij.clij1wrappers.CLIJ1MultiplyImageAndCoordinate
    //----------------------------------------------------

    // net.haesleinhuepf.clij.clij1wrappers.CLIJ1DetectOptimaSliceBySlice
    //----------------------------------------------------

    // net.haesleinhuepf.clij.clij1wrappers.CLIJ1MultiplyImageAndScalar
    //----------------------------------------------------

    // net.haesleinhuepf.clij.clij1wrappers.CLIJ1DifferenceOfGaussian
    //----------------------------------------------------

    // net.haesleinhuepf.clij.clij1wrappers.CLIJ1MultiplyImages
    //----------------------------------------------------

    // net.haesleinhuepf.clij.clij1wrappers.CLIJ1DifferenceOfGaussianSliceBySlice
    //----------------------------------------------------

    // net.haesleinhuepf.clij.clij1wrappers.CLIJ1MultiplySliceBySliceWithScalars
    //----------------------------------------------------

    // net.haesleinhuepf.clij.clij1wrappers.CLIJ1DilateBox
    //----------------------------------------------------

    // net.haesleinhuepf.clij.clij1wrappers.CLIJ1MultiplyStackWithPlane
    //----------------------------------------------------

    // net.haesleinhuepf.clij.clij1wrappers.CLIJ1DilateBoxSliceBySlice
    //----------------------------------------------------

    // net.haesleinhuepf.clij.clij1wrappers.CLIJ1Power
    //----------------------------------------------------

    // net.haesleinhuepf.clij.clij1wrappers.CLIJ1DilateSphere
    //----------------------------------------------------

    // net.haesleinhuepf.clij.clij1wrappers.CLIJ1RadialProjection
    //----------------------------------------------------

    // net.haesleinhuepf.clij.clij1wrappers.CLIJ1DilateSphereSliceBySlice
    //----------------------------------------------------

    // net.haesleinhuepf.clij.clij1wrappers.CLIJ1ResliceBottom
    //----------------------------------------------------

    // net.haesleinhuepf.clij.clij1wrappers.CLIJ1DivideImages
    //----------------------------------------------------

    // net.haesleinhuepf.clij.clij1wrappers.CLIJ1ResliceLeft
    //----------------------------------------------------

    // net.haesleinhuepf.clij.clij1wrappers.CLIJ1Downsample
    //----------------------------------------------------

    // net.haesleinhuepf.clij.clij1wrappers.CLIJ1ResliceRight
    //----------------------------------------------------

    // net.haesleinhuepf.clij.clij1wrappers.CLIJ1DownsampleSliceBySliceHalfMedian
    //----------------------------------------------------

    // net.haesleinhuepf.clij.clij1wrappers.CLIJ1ResliceTop
    //----------------------------------------------------

    // net.haesleinhuepf.clij.clij1wrappers.CLIJ1ErodeBox
    //----------------------------------------------------

    // net.haesleinhuepf.clij.clij1wrappers.CLIJ1RotateLeft
    //----------------------------------------------------

    // net.haesleinhuepf.clij.clij1wrappers.CLIJ1ErodeBoxSliceBySlice
    //----------------------------------------------------

    // net.haesleinhuepf.clij.clij1wrappers.CLIJ1RotateRight
    //----------------------------------------------------

    // net.haesleinhuepf.clij.clij1wrappers.CLIJ1ErodeSphere
    //----------------------------------------------------

    // net.haesleinhuepf.clij.clij1wrappers.CLIJ1Set
    //----------------------------------------------------

    // net.haesleinhuepf.clij.clij1wrappers.CLIJ1ErodeSphereSliceBySlice
    //----------------------------------------------------

    // net.haesleinhuepf.clij.clij1wrappers.CLIJ1SplitStack
    //----------------------------------------------------

    // net.haesleinhuepf.clij.clij1wrappers.CLIJ1FillHistogram
    //----------------------------------------------------

    // net.haesleinhuepf.clij.clij1wrappers.CLIJ1SubtractImages
    //----------------------------------------------------

    // net.haesleinhuepf.clij.clij1wrappers.CLIJ1Flip
    //----------------------------------------------------

    // net.haesleinhuepf.clij.clij1wrappers.CLIJ1Subtract
    //----------------------------------------------------

    // net.haesleinhuepf.clij.clij1wrappers.CLIJ1GradientX
    //----------------------------------------------------

    // net.haesleinhuepf.clij.clij1wrappers.CLIJ1SumPixels
    //----------------------------------------------------

    // net.haesleinhuepf.clij.clij1wrappers.CLIJ1GradientY
    //----------------------------------------------------

    // net.haesleinhuepf.clij.clij1wrappers.CLIJ1SumPixelsSliceBySlice
    //----------------------------------------------------

    // net.haesleinhuepf.clij.clij1wrappers.CLIJ1GradientZ
    //----------------------------------------------------

    // net.haesleinhuepf.clij.clij1wrappers.CLIJ1SumZProjection
    //----------------------------------------------------

    // net.haesleinhuepf.clij.clij1wrappers.CLIJ1Histogram
    //----------------------------------------------------

    // net.haesleinhuepf.clij.clij1wrappers.CLIJ1TenengradFusion
    //----------------------------------------------------

    // net.haesleinhuepf.clij.clij1wrappers.CLIJ1Invert
    //----------------------------------------------------

    // net.haesleinhuepf.clij.clij1wrappers.CLIJ1TenengradWeightsSliceBySlice
    //----------------------------------------------------

    // net.haesleinhuepf.clij.clij1wrappers.CLIJ1LocalThreshold
    //----------------------------------------------------

    // net.haesleinhuepf.clij.clij1wrappers.CLIJ1Threshold
    //----------------------------------------------------

    // net.haesleinhuepf.clij.advancedfilters.BinaryUnion
    //----------------------------------------------------
    /**
     * Computes a binary image (containing pixel values 0 and 1) from two images X and Y by connecting pairs of
     * pixels x and y with the binary union operator |.
     * All pixel values except 0 in the input images are interpreted as 1.<pre>f(x, y) = x | y</pre>
     */
    public boolean binaryUnion(ClearCLBuffer operand1, ClearCLBuffer operand2, ClearCLBuffer destination) {
        return BinaryUnion.binaryUnion(clij, operand1, operand2, destination);
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
    public boolean binaryIntersection(ClearCLBuffer operand1, ClearCLBuffer operand2, ClearCLBuffer destination) {
        return BinaryIntersection.binaryIntersection(clij, operand1, operand2, destination);
    }


    // net.haesleinhuepf.clij.advancedfilters.ConnectedComponentsLabeling
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
    public boolean setNonZeroPixelsToPixelIndex(ClearCLImageInterface arg1, ClearCLImageInterface arg2) {
        return ConnectedComponentsLabeling.setNonZeroPixelsToPixelIndex(clij, arg1, arg2);
    }

    /**
     * 
     */
    public boolean shiftIntensitiesToCloseGaps(ClearCLImageInterface arg1, ClearCLImageInterface arg2) {
        return ConnectedComponentsLabeling.shiftIntensitiesToCloseGaps(clijx, arg1, arg2);
    }


    // net.haesleinhuepf.clij.advancedfilters.CountNonZeroPixels
    //----------------------------------------------------
    /**
     * Determines the number of all pixels in a given image which are not equal to 0. It will be stored in a new row of ImageJs
     * Results table in the column 'Count_non_zero'.
     */
    public double countNonZeroPixels(ClearCLBuffer source) {
        return CountNonZeroPixels.countNonZeroPixels(clij, source);
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
    public boolean differenceOfGaussian(ClearCLBuffer input, ClearCLBuffer destination, double sigma1x, double sigma1y, double sigma2x, double sigma2y) {
        return DifferenceOfGaussian2D.differenceOfGaussian(clij, input, destination, new Double (sigma1x).floatValue(), new Double (sigma1y).floatValue(), new Double (sigma2x).floatValue(), new Double (sigma2y).floatValue());
    }


    // net.haesleinhuepf.clij.advancedfilters.DifferenceOfGaussian3D
    //----------------------------------------------------
    /**
     * Applies Gaussian blur to the input image twice resulting in two images which are then subtracted from each other.
     */
    public boolean differenceOfGaussian(ClearCLBuffer arg1, ClearCLBuffer arg2, double arg3, double arg4, double arg5, double arg6, double arg7, double arg8) {
        return DifferenceOfGaussian3D.differenceOfGaussian(clij, arg1, arg2, new Double (arg3).floatValue(), new Double (arg4).floatValue(), new Double (arg5).floatValue(), new Double (arg6).floatValue(), new Double (arg7).floatValue(), new Double (arg8).floatValue());
    }


    // net.haesleinhuepf.clij.advancedfilters.Extrema
    //----------------------------------------------------
    /**
     * Returns an image with pixel values most distant from 0: f(x, y) = x if abs(x) > abs(y), y else.
     */
    public boolean extrema(ClearCLBuffer input1, ClearCLBuffer input2, ClearCLBuffer destination) {
        return Extrema.extrema(clij, input1, input2, destination);
    }


    // net.haesleinhuepf.clij.advancedfilters.LocalExtremaBox
    //----------------------------------------------------
    /**
     * Applies a local minimum and maximum filter. Afterwards, the value is returned which is more far from zero.
     */
    public boolean localExtremaBox(ClearCLBuffer input, ClearCLBuffer destination, double radiusX, double radiusY, double radiusZ) {
        return LocalExtremaBox.localExtremaBox(clij, input, destination, new Double (radiusX).intValue(), new Double (radiusY).intValue(), new Double (radiusZ).intValue());
    }


    // net.haesleinhuepf.clij.advancedfilters.LocalID
    //----------------------------------------------------
    /**
     * local id
     */
    public boolean localID(ClearCLBuffer input, ClearCLBuffer destination) {
        return LocalID.localID(clij, input, destination);
    }


    // net.haesleinhuepf.clij.advancedfilters.MaskLabel
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
    public double[] meanClosestSpotDistances(ClearCLBuffer arg1, ClearCLBuffer arg2, boolean arg3) {
        return MeanClosestSpotDistance.meanClosestSpotDistances(clij, arg1, arg2, arg3);
    }


    // net.haesleinhuepf.clij.advancedfilters.MeanSquaredError
    //----------------------------------------------------
    /**
     * Determines the mean squared error (MSE) between two images. The MSE will be stored in a new row of ImageJs
     * Results table in the column 'MSE'.
     */
    public double meanSquaredError(ClearCLBuffer source1, ClearCLBuffer source2) {
        return MeanSquaredError.meanSquaredError(clij, source1, source2);
    }


    // net.haesleinhuepf.clij.advancedfilters.MedianZProjection
    //----------------------------------------------------

    // net.haesleinhuepf.clij.advancedfilters.NonzeroMinimumDiamond
    //----------------------------------------------------
    /**
     * Apply a minimum-sphere filter to the input image. The radius is fixed to 1 and pixels with value 0 are ignored.
     */
    public ClearCLKernel nonzeroMinimumDiamond(ClearCLImageInterface arg1, ClearCLBuffer arg2, ClearCLImageInterface arg3, ClearCLKernel arg4) {
        return NonzeroMinimumDiamond.nonzeroMinimumDiamond(clijx, arg1, arg2, arg3, arg4);
    }

    /**
     * Apply a minimum-sphere filter to the input image. The radius is fixed to 1 and pixels with value 0 are ignored.
     */
    public boolean nonzeroMinimumDiamond(ClearCLImageInterface arg1, ClearCLImageInterface arg2, ClearCLImageInterface arg3) {
        return NonzeroMinimumDiamond.nonzeroMinimumDiamond(clij, arg1, arg2, arg3);
    }


    // net.haesleinhuepf.clij.advancedfilters.Paste2D
    //----------------------------------------------------
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


    // net.haesleinhuepf.clij.advancedfilters.Paste3D
    //----------------------------------------------------
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


    // net.haesleinhuepf.clij.advancedfilters.Presign
    //----------------------------------------------------
    /**
     * Determines the extrema of pixel values: f(x) = x / abs(x).
     */
    public boolean presign(ClearCLBuffer input, ClearCLBuffer destination) {
        return Presign.presign(clij, input, destination);
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
    public boolean stackToTiles(ClearCLImage source, ClearCLImage destination, double tiles_x, double tiles_y) {
        return StackToTiles.stackToTiles(clij, source, destination, new Double (tiles_x).intValue(), new Double (tiles_y).intValue());
    }

    /**
     * Stack to tiles.
     */
    public boolean stackToTiles(ClearCLBuffer source, ClearCLBuffer destination, double tiles_x, double tiles_y) {
        return StackToTiles.stackToTiles(clij, source, destination, new Double (tiles_x).intValue(), new Double (tiles_y).intValue());
    }


    // net.haesleinhuepf.clij.advancedfilters.SubtractBackground2D
    //----------------------------------------------------
    /**
     * Applies Gaussian blur to the input image and subtracts the result from the original image.
     */
    public boolean subtractBackground(ClearCLBuffer input, ClearCLBuffer destination, double sigmaX, double sigmaY) {
        return SubtractBackground2D.subtractBackground(clij, input, destination, new Double (sigmaX).floatValue(), new Double (sigmaY).floatValue());
    }


    // net.haesleinhuepf.clij.advancedfilters.SubtractBackground3D
    //----------------------------------------------------
    /**
     * Applies Gaussian blur to the input image and subtracts the result from the original image.
     */
    public boolean subtractBackground(ClearCLBuffer arg1, ClearCLBuffer arg2, double arg3, double arg4, double arg5) {
        return SubtractBackground3D.subtractBackground(clij, arg1, arg2, new Double (arg3).floatValue(), new Double (arg4).floatValue(), new Double (arg5).floatValue());
    }


    // net.haesleinhuepf.clij.advancedfilters.TopHatBox
    //----------------------------------------------------
    /**
     * Apply a top-hat filter to the input image.
     */
    public boolean topHatBox(ClearCLBuffer input, ClearCLBuffer destination, double radiusX, double radiusY, double radiusZ) {
        return TopHatBox.topHatBox(clij, input, destination, new Double (radiusX).intValue(), new Double (radiusY).intValue(), new Double (radiusZ).intValue());
    }


    // net.haesleinhuepf.clij.advancedfilters.TopHatSphere
    //----------------------------------------------------
    /**
     * Apply a top-hat filter to the input image.
     */
    public boolean topHatSphere(ClearCLBuffer input, ClearCLBuffer destination, double radiusX, double radiusY, double radiusZ) {
        return TopHatSphere.topHatSphere(clij, input, destination, new Double (radiusX).intValue(), new Double (radiusY).intValue(), new Double (radiusZ).intValue());
    }


    // net.haesleinhuepf.clij.advancedmath.Exponential
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


    // net.haesleinhuepf.clij.advancedmath.Logarithm
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


    // net.haesleinhuepf.clij.matrix.GenerateDistanceMatrix
    //----------------------------------------------------
    /**
     * Takes two images containing coordinates and builds up a matrix containing distance between the points. Convention: image width represents number of points, height represents dimensionality (2D, 3D, ... 10D). The result image has width the first input image and height equals to the width of the second input image.
     */
    public boolean generateDistanceMatrix(ClearCLBuffer coordinate_list1, ClearCLBuffer coordinate_list2, ClearCLBuffer distance_matrix_destination) {
        return GenerateDistanceMatrix.generateDistanceMatrix(clij, coordinate_list1, coordinate_list2, distance_matrix_destination);
    }


    // net.haesleinhuepf.clij.matrix.ShortestDistances
    //----------------------------------------------------
    /**
     * Determine the shortest distance from a distance matrix. This corresponds to the minimum in a matrix for each individial column.
     */
    public boolean shortestDistances(ClearCLBuffer distance_matrix, ClearCLBuffer destination_minimum_distances) {
        return ShortestDistances.shortestDistances(clij, distance_matrix, destination_minimum_distances);
    }


    // net.haesleinhuepf.clij.matrix.SpotsToPointList
    //----------------------------------------------------
    /**
     * Transforms a spots image as resulting from maxim detection in an image where every column cotains d 
     * pixels (with d = dimensionality of the original image) with the coordinates of the maxima.
     */
    public boolean spotsToPointList(ClearCLBuffer input_spots, ClearCLBuffer destination_pointlist) {
        return SpotsToPointList.spotsToPointList(clij, input_spots, destination_pointlist);
    }


    // net.haesleinhuepf.clij.matrix.TransposeXY
    //----------------------------------------------------
    /**
     * Transpose X and Y in an image.
     */
    public boolean transposeXY(ClearCLBuffer input, ClearCLBuffer destination) {
        return TransposeXY.transposeXY(clij, input, destination);
    }


    // net.haesleinhuepf.clij.matrix.TransposeXZ
    //----------------------------------------------------
    /**
     * Transpose X and Z in an image.
     */
    public boolean transposeXZ(ClearCLBuffer input, ClearCLBuffer destination) {
        return TransposeXZ.transposeXZ(clij, input, destination);
    }


    // net.haesleinhuepf.clij.matrix.TransposeYZ
    //----------------------------------------------------
    /**
     * Transpose Y and Z in an image.
     */
    public boolean transposeYZ(ClearCLBuffer input, ClearCLBuffer destination) {
        return TransposeYZ.transposeYZ(clij, input, destination);
    }


    // net.haesleinhuepf.clij.piv.FastParticleImageVelocimetry
    //----------------------------------------------------
    /**
     * 
     */
    public boolean particleImageVelocimetry2D(ClearCLBuffer arg1, ClearCLBuffer arg2, ClearCLBuffer arg3, ClearCLBuffer arg4, double arg5) {
        return FastParticleImageVelocimetry.particleImageVelocimetry2D(clij, arg1, arg2, arg3, arg4, new Double (arg5).intValue());
    }

    /**
     * 
     */
    public boolean particleImageVelocimetry2D(ClearCLImage arg1, ClearCLImage arg2, ClearCLImage arg3, ClearCLImage arg4, double arg5) {
        return FastParticleImageVelocimetry.particleImageVelocimetry2D(clij, arg1, arg2, arg3, arg4, new Double (arg5).intValue());
    }


    // net.haesleinhuepf.clij.piv.ParticleImageVelocimetry
    //----------------------------------------------------
    /**
     * For every pixel in source image 1, determine the pixel with the most similar intensity in 
     *  the local neighborhood with a given radius in source image 2. Write the distance in 
     * X and Y in the two corresponding destination images.
     */
    public boolean particleImageVelocimetry(ClearCLBuffer arg1, ClearCLBuffer arg2, ClearCLBuffer arg3, ClearCLBuffer arg4, ClearCLBuffer arg5, double arg6, double arg7, double arg8, boolean arg9) {
        return ParticleImageVelocimetry.particleImageVelocimetry(clij, arg1, arg2, arg3, arg4, arg5, new Double (arg6).intValue(), new Double (arg7).intValue(), new Double (arg8).intValue(), arg9);
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
    public boolean translationRegistration(ClearCLBuffer input1, ClearCLBuffer input2, ClearCLBuffer destination) {
        return TranslationRegistration.translationRegistration(clij, input1, input2, destination);
    }

    /**
     * Measures center of mass of thresholded objects in the two input images and translates the second image so that it better fits to the first image.
     */
    public boolean translationRegistration(ClearCLBuffer arg1, ClearCLBuffer arg2, double[] arg3) {
        return TranslationRegistration.translationRegistration(clij, arg1, arg2, arg3);
    }


    // net.haesleinhuepf.clij.registration.TranslationTimelapseRegistration
    //----------------------------------------------------
    /**
     * Applies 2D translation registration to every pair of t, t+1 slices of a 2D+t image stack.
     */
    public boolean translationTimelapseRegistration(ClearCLBuffer input, ClearCLBuffer output) {
        return TranslationTimelapseRegistration.translationTimelapseRegistration(clij, input, output);
    }


    // net.haesleinhuepf.clij.advancedfilters.SetWhereXequalsY
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


    // net.haesleinhuepf.clij.advancedfilters.Laplace
    //----------------------------------------------------
    /**
     * Apply the Laplace operator (Diamond neighborhood) to an image.
     */
    public boolean laplace(ClearCLBuffer input, ClearCLBuffer destination) {
        return Laplace.laplace(clij, input, destination);
    }


    // net.haesleinhuepf.clij.advancedfilters.Image2DToResultsTable
    //----------------------------------------------------
    /**
     * Converts an image into a table.
     */
    public ResultsTable image2DToResultsTable(ClearCLBuffer arg1, ResultsTable arg2) {
        return Image2DToResultsTable.image2DToResultsTable(clij, arg1, arg2);
    }

    /**
     * Converts an image into a table.
     */
    public ResultsTable image2DToResultsTable(ClearCLImage arg1, ResultsTable arg2) {
        return Image2DToResultsTable.image2DToResultsTable(clij, arg1, arg2);
    }


    // net.haesleinhuepf.clij.advancedfilters.WriteValuesToPositions
    //----------------------------------------------------
    /**
     * Takes an image with three/four rows (2D: height = 3; 3D: height = 4): x, y [, z] and v and target image. The value v will be written at position x/y[/z] in the target image.
     */
    public boolean writeValuesToPositions(ClearCLBuffer positionsAndValues, ClearCLBuffer destination) {
        return WriteValuesToPositions.writeValuesToPositions(clij, positionsAndValues, destination);
    }


    // net.haesleinhuepf.clij.advancedfilters.GetSize
    //----------------------------------------------------
    /**
     * Reads out the size of an image [stack] and writes it to the results table in the columns 'Width', 'Height' and 'Depth'.
     */
    public long[] getSize(ClearCLBuffer source) {
        return GetSize.getSize(clij, source);
    }


    // net.haesleinhuepf.clij.matrix.MultiplyMatrix
    //----------------------------------------------------
    /**
     * Multiplies two matrices with each other.
     */
    public boolean multiplyMatrix(ClearCLBuffer matrix1, ClearCLBuffer matrix2, ClearCLBuffer matrix_destination) {
        return MultiplyMatrix.multiplyMatrix(clij, matrix1, matrix2, matrix_destination);
    }


    // net.haesleinhuepf.clij.matrix.MatrixEqual
    //----------------------------------------------------
    /**
     * Checks if all elements of a matrix are different by less than or equal to a given tolerance.
     * The result will be put in the results table as 1 if yes and 0 otherwise.
     */
    public boolean matrixEqual(ClearCLBuffer arg1, ClearCLBuffer arg2, double arg3) {
        return MatrixEqual.matrixEqual(clij, arg1, arg2, new Double (arg3).floatValue());
    }


    // net.haesleinhuepf.clij.advancedfilters.PowerImages
    //----------------------------------------------------
    /**
     * Calculates x to the power of y pixel wise of two images X and Y.
     */
    public boolean powerImages(ClearCLBuffer input, ClearCLBuffer exponent, ClearCLBuffer destination) {
        return PowerImages.powerImages(clij, input, exponent, destination);
    }


    // net.haesleinhuepf.clij.advancedmath.Equal
    //----------------------------------------------------
    /**
     * Determines if two images A and B equal pixel wise.
     * 
     * f(a, b) = 1 if a == b; 0 otherwise. 
     */
    public boolean equal(ClearCLBuffer source1, ClearCLBuffer source2, ClearCLBuffer destination) {
        return Equal.equal(clij, source1, source2, destination);
    }


    // net.haesleinhuepf.clij.advancedmath.GreaterOrEqual
    //----------------------------------------------------
    /**
     * Determines if two images A and B greater or equal pixel wise.
     * 
     * f(a, b) = 1 if a >= b; 0 otherwise. 
     */
    public boolean greaterOrEqual(ClearCLBuffer source1, ClearCLBuffer source2, ClearCLBuffer destination) {
        return GreaterOrEqual.greaterOrEqual(clij, source1, source2, destination);
    }


    // net.haesleinhuepf.clij.advancedmath.Greater
    //----------------------------------------------------
    /**
     * Determines if two images A and B greater pixel wise.
     * 
     * f(a, b) = 1 if a > b; 0 otherwise. 
     */
    public boolean greater(ClearCLBuffer source1, ClearCLBuffer source2, ClearCLBuffer destination) {
        return Greater.greater(clij, source1, source2, destination);
    }


    // net.haesleinhuepf.clij.advancedmath.Smaller
    //----------------------------------------------------
    /**
     * Determines if two images A and B smaller pixel wise.
     * 
     * f(a, b) = 1 if a < b; 0 otherwise. 
     */
    public boolean smaller(ClearCLBuffer source1, ClearCLBuffer source2, ClearCLBuffer destination) {
        return Smaller.smaller(clij, source1, source2, destination);
    }


    // net.haesleinhuepf.clij.advancedmath.SmallerOrEqual
    //----------------------------------------------------
    /**
     * Determines if two images A and B smaller or equal pixel wise.
     * 
     * f(a, b) = 1 if a <= b; 0 otherwise. 
     */
    public boolean smallerOrEqual(ClearCLBuffer source1, ClearCLBuffer source2, ClearCLBuffer destination) {
        return SmallerOrEqual.smallerOrEqual(clij, source1, source2, destination);
    }


    // net.haesleinhuepf.clij.advancedmath.NotEqual
    //----------------------------------------------------
    /**
     * Determines if two images A and B equal pixel wise.
     * 
     * f(a, b) = 1 if a != b; 0 otherwise. 
     */
    public boolean notEqual(ClearCLBuffer source1, ClearCLBuffer source2, ClearCLBuffer destination) {
        return NotEqual.notEqual(clij, source1, source2, destination);
    }


    // net.haesleinhuepf.clij.io.ReadImageFromDisc
    //----------------------------------------------------
    /**
     * Read an image from disc.
     */
    public ClearCLBuffer readImageFromDisc(String arg1) {
        return ReadImageFromDisc.readImageFromDisc(clij, arg1);
    }


    // net.haesleinhuepf.clij.io.ReadRawImageFromDisc
    //----------------------------------------------------
    /**
     * Reads a raw file from disc and pushes it immediately to the GPU.
     */
    public ClearCLBuffer readRawImageFromDisc(String arg1, double arg2, double arg3, double arg4, double arg5) {
        return ReadRawImageFromDisc.readRawImageFromDisc(clij, arg1, new Double (arg2).intValue(), new Double (arg3).intValue(), new Double (arg4).intValue(), new Double (arg5).intValue());
    }

    /**
     * Reads a raw file from disc and pushes it immediately to the GPU.
     */
    public boolean readRawImageFromDisc(ClearCLBuffer arg1, String arg2) {
        return ReadRawImageFromDisc.readRawImageFromDisc(clij, arg1, arg2);
    }


    // net.haesleinhuepf.clij.io.PreloadFromDisc
    //----------------------------------------------------
    /**
     * This plugin takes two image filenames and loads them into RAM. The first image is returned immediately, the second image is loaded in the background and  will be returned when the plugin is called again.
     * 
     *  It is assumed that all images have the same size. If this is not the case, call release(image) before  getting the second image.
     */
    public ClearCLBuffer preloadFromDisc(ClearCLBuffer arg1, String arg2, String arg3, String arg4) {
        return PreloadFromDisc.preloadFromDisc(clij, arg1, arg2, arg3, arg4);
    }


    // net.haesleinhuepf.clij.advancedmath.EqualConstant
    //----------------------------------------------------
    /**
     * Determines if an image A and a constant b are equal.
     * 
     * f(a, b) = 1 if a == b; 0 otherwise. 
     */
    public boolean equalConstant(ClearCLBuffer source, ClearCLBuffer destination, double constant) {
        return EqualConstant.equalConstant(clij, source, destination, new Double (constant).floatValue());
    }


    // net.haesleinhuepf.clij.advancedmath.GreaterOrEqualConstant
    //----------------------------------------------------
    /**
     * Determines if two images A and B greater or equal pixel wise.
     * 
     * f(a, b) = 1 if a >= b; 0 otherwise. 
     */
    public boolean greaterOrEqualConstant(ClearCLBuffer source, ClearCLBuffer destination, double constant) {
        return GreaterOrEqualConstant.greaterOrEqualConstant(clij, source, destination, new Double (constant).floatValue());
    }


    // net.haesleinhuepf.clij.advancedmath.GreaterConstant
    //----------------------------------------------------
    /**
     * Determines if two images A and B greater pixel wise.
     * 
     * f(a, b) = 1 if a > b; 0 otherwise. 
     */
    public boolean greaterConstant(ClearCLBuffer source, ClearCLBuffer destination, double constant) {
        return GreaterConstant.greaterConstant(clij, source, destination, new Double (constant).floatValue());
    }


    // net.haesleinhuepf.clij.advancedmath.SmallerConstant
    //----------------------------------------------------
    /**
     * Determines if two images A and B smaller pixel wise.
     * 
     * f(a, b) = 1 if a < b; 0 otherwise. 
     */
    public boolean smallerConstant(ClearCLBuffer source, ClearCLBuffer destination, double constant) {
        return SmallerConstant.smallerConstant(clij, source, destination, new Double (constant).floatValue());
    }


    // net.haesleinhuepf.clij.advancedmath.SmallerOrEqualConstant
    //----------------------------------------------------
    /**
     * Determines if two images A and B smaller or equal pixel wise.
     * 
     * f(a, b) = 1 if a <= b; 0 otherwise. 
     */
    public boolean smallerOrEqualConstant(ClearCLBuffer source, ClearCLBuffer destination, double constant) {
        return SmallerOrEqualConstant.smallerOrEqualConstant(clij, source, destination, new Double (constant).floatValue());
    }


    // net.haesleinhuepf.clij.advancedmath.NotEqualConstant
    //----------------------------------------------------
    /**
     * Determines if two images A and B equal pixel wise.
     * 
     * f(a, b) = 1 if a != b; 0 otherwise. 
     */
    public boolean notEqualConstant(ClearCLBuffer source, ClearCLBuffer destination, double constant) {
        return NotEqualConstant.notEqualConstant(clij, source, destination, new Double (constant).floatValue());
    }


    // net.haesleinhuepf.clij.painting.DrawBox
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


    // net.haesleinhuepf.clij.painting.DrawLine
    //----------------------------------------------------
    /**
     * Draws a line between two points with a given thickness. All pixels other than on the line are untouched. Consider using clij.op.set(buffer, 0); in advance.
     */
    public boolean drawLine(ClearCLBuffer destination, double x1, double y1, double z1, double x2, double y2, double z2, double thickness) {
        return DrawLine.drawLine(clijx, destination, new Double (x1).floatValue(), new Double (y1).floatValue(), new Double (z1).floatValue(), new Double (x2).floatValue(), new Double (y2).floatValue(), new Double (z2).floatValue(), new Double (thickness).floatValue());
    }


    // net.haesleinhuepf.clij.painting.DrawSphere
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


    // net.haesleinhuepf.clij.advancedfilters.ReplaceIntensity
    //----------------------------------------------------
    /**
     * Replaces a specific intensity in an image with a given new value.
     */
    public boolean replaceIntensity(ClearCLImageInterface arg1, ClearCLImageInterface arg2, double arg3, double arg4) {
        return ReplaceIntensity.replaceIntensity(clij, arg1, arg2, new Double (arg3).floatValue(), new Double (arg4).floatValue());
    }


    // net.haesleinhuepf.clij.advancedfilters.BoundingBox
    //----------------------------------------------------
    /**
     * Determines the bounding box of all non-zero pixels in a binary image. The positions will be stored in a new row of ImageJs
     * Results table in the column 'BoundingBoxX', 'BoundingBoxY', 'BoundingBoxZ', 'BoundingBoxWidth', 'BoundingBoxHeight' 'BoundingBoxDepth'.In case of 2D images Z and depth will be zero.
     */
    public double[] boundingBox(ClearCLBuffer source) {
        return BoundingBox.boundingBox(clij, source);
    }


    // net.haesleinhuepf.clij.advancedfilters.MinimumOfMaskedPixels
    //----------------------------------------------------
    /**
     * Determines the minimum intensity in an image, but only in pixels which have non-zero values in another mask image.
     */
    public double minimumOfMaskedPixels(ClearCLBuffer source, ClearCLBuffer mask) {
        return MinimumOfMaskedPixels.minimumOfMaskedPixels(clij, source, mask);
    }


    // net.haesleinhuepf.clij.advancedfilters.MaximumOfMaskedPixels
    //----------------------------------------------------
    /**
     * Determines the maximum intensity in an image, but only in pixels which have non-zero values in another mask image.
     */
    public double maximumOfMaskedPixels(ClearCLBuffer source, ClearCLBuffer mask) {
        return MaximumOfMaskedPixels.maximumOfMaskedPixels(clij, source, mask);
    }


    // net.haesleinhuepf.clij.advancedfilters.MeanOfMaskedPixels
    //----------------------------------------------------
    /**
     * Determines the mean intensity in an image, but only in pixels which have non-zero values in another binary mask image.
     */
    public double meanOfMaskedPixels(ClearCLBuffer source, ClearCLBuffer mask) {
        return MeanOfMaskedPixels.meanOfMaskedPixels(clij, source, mask);
    }


    // net.haesleinhuepf.clij.advancedfilters.LabelToMask
    //----------------------------------------------------
    /**
     * Masks a single label in a label map: Sets all pixels in the target image to 1, where the given label index was present in the label map.
     */
    public boolean labelToMask(ClearCLBuffer label_map_source, ClearCLBuffer mask_destination, double label_index) {
        return LabelToMask.labelToMask(clij, label_map_source, mask_destination, new Double (label_index).floatValue());
    }


    // net.haesleinhuepf.clij.matrix.NClosestPoints
    //----------------------------------------------------
    /**
     * Determine the n point indices with shortest distance for all points in a distance matrix.
     * This corresponds to the n row indices with minimum values for each column of the distance matrix.
     */
    public boolean nClosestPoints(ClearCLBuffer arg1, ClearCLBuffer arg2) {
        return NClosestPoints.nClosestPoints(clij, arg1, arg2);
    }


    // net.haesleinhuepf.clij.matrix.GaussJordan
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


    // net.haesleinhuepf.clij.advancedfilters.StatisticsOfLabelledPixels
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


    // net.haesleinhuepf.clij.advancedfilters.VarianceOfAllPixels
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


    // net.haesleinhuepf.clij.advancedfilters.StandardDeviationOfAllPixels
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


    // net.haesleinhuepf.clij.advancedfilters.ExcludeLabelsOnEdges
    //----------------------------------------------------
    /**
     * Removes all labels from a label map which touch the edges. Remaining label elements are renumbered afterwards.
     */
    public boolean excludeLabelsOnEdges(ClearCLBuffer label_map_input, ClearCLBuffer label_map_destination) {
        return ExcludeLabelsOnEdges.excludeLabelsOnEdges(clij, label_map_input, label_map_destination);
    }


    // net.haesleinhuepf.clij.advancedfilters.BinarySubtract
    //----------------------------------------------------
    /**
     * Subtracts one binary image from another.
     */
    public boolean binarySubtract(ClearCLImageInterface arg1, ClearCLImageInterface arg2, ClearCLImageInterface arg3) {
        return BinarySubtract.binarySubtract(clijx, arg1, arg2, arg3);
    }


    // net.haesleinhuepf.clij.advancedfilters.BinaryEdgeDetection
    //----------------------------------------------------
    /**
     * Determines pixels/voxels which are on the surface of a binary objects and sets only them to 1 in the destination image.
     */
    public boolean binaryEdgeDetection(ClearCLImageInterface arg1, ClearCLImageInterface arg2) {
        return BinaryEdgeDetection.binaryEdgeDetection(clijx, arg1, arg2);
    }


    // net.haesleinhuepf.clij.advancedfilters.DistanceMap
    //----------------------------------------------------
    /**
     * 
     */
    public boolean localPositiveMinimum(ClearCLImageInterface arg1, ClearCLImageInterface arg2, ClearCLImageInterface arg3) {
        return DistanceMap.localPositiveMinimum(clijx, arg1, arg2, arg3);
    }

    /**
     * Generates a distance map from a binary image.
     */
    public boolean distanceMap(ClearCLBuffer source, ClearCLBuffer destination) {
        return DistanceMap.distanceMap(clijx, source, destination);
    }


    // net.haesleinhuepf.clij.advancedfilters.PullAsROI
    //----------------------------------------------------
    /**
     * Pulls a binary image from the GPU memory and puts it on the currently active ImageJ window.
     */
    public Roi pullAsROI(ClearCLBuffer binary_input) {
        return PullAsROI.pullAsROI(clij, binary_input);
    }


    // net.haesleinhuepf.clij.advancedfilters.NonzeroMaximumDiamond
    //----------------------------------------------------
    /**
     * Apply a maximum-sphere filter to the input image. The radius is fixed to 1 and pixels with value 0 are ignored.
     */
    public ClearCLKernel nonzeroMaximumDiamond(ClearCLImageInterface arg1, ClearCLBuffer arg2, ClearCLImageInterface arg3, ClearCLKernel arg4) {
        return NonzeroMaximumDiamond.nonzeroMaximumDiamond(clijx, arg1, arg2, arg3, arg4);
    }

    /**
     * Apply a maximum-sphere filter to the input image. The radius is fixed to 1 and pixels with value 0 are ignored.
     */
    public boolean nonzeroMaximumDiamond(ClearCLImageInterface arg1, ClearCLImageInterface arg2, ClearCLImageInterface arg3) {
        return NonzeroMaximumDiamond.nonzeroMaximumDiamond(clij, arg1, arg2, arg3);
    }


    // net.haesleinhuepf.clij.advancedfilters.OnlyzeroOverwriteMaximumDiamond
    //----------------------------------------------------
    /**
     * TODO
     */
    public boolean onlyzeroOverwriteMaximumDiamond(ClearCLImageInterface arg1, ClearCLImageInterface arg2, ClearCLImageInterface arg3) {
        return OnlyzeroOverwriteMaximumDiamond.onlyzeroOverwriteMaximumDiamond(clij, arg1, arg2, arg3);
    }


    // net.haesleinhuepf.clij.advancedfilters.OnlyzeroOverwriteMaximumBox
    //----------------------------------------------------
    /**
     * 
     */
    public boolean onlyzeroOverwriteMaximumBox(ClearCLImageInterface arg1, ClearCLImageInterface arg2, ClearCLImageInterface arg3) {
        return OnlyzeroOverwriteMaximumBox.onlyzeroOverwriteMaximumBox(clij, arg1, arg2, arg3);
    }


    // net.haesleinhuepf.clij.matrix.GenerateTouchMatrix
    //----------------------------------------------------
    /**
     * Takes a labelmap with n labels and generates a (n+1)*(n+1) matrix where all pixels are set to 0 exept those where labels are touching.Only half of the matrix is filled (with x < y). For example, if labels 3 and 4 are touching then the pixel (3,4) in the matrix will be set to 1.
     */
    public boolean generateTouchMatrix(ClearCLBuffer label_map, ClearCLBuffer touch_matrix_destination) {
        return GenerateTouchMatrix.generateTouchMatrix(clij, label_map, touch_matrix_destination);
    }


    // net.haesleinhuepf.clij.advancedfilters.DetectLabelEdges
    //----------------------------------------------------
    /**
     * Takes a labelmap with n labels and generates a (n+1)*(n+1) matrix where all pixels are set to 0 exept those where labels are touching.Only half of the matrix is filled (with x < y). For example, if labels 3 and 4 are touching then the pixel (3,4) in the matrix will be set to 1.
     */
    public boolean detectLabelEdges(ClearCLBuffer label_map, ClearCLBuffer touch_matrix_destination) {
        return DetectLabelEdges.detectLabelEdges(clijx, label_map, touch_matrix_destination);
    }


    // net.haesleinhuepf.clij.advancedfilters.StopWatch
    //----------------------------------------------------
    /**
     * Measures time and outputs delay to last call.
     */
    public boolean stopWatch(String arg1) {
        return StopWatch.stopWatch(clij, arg1);
    }


    // net.haesleinhuepf.clij.matrix.CountTouchingNeighbors
    //----------------------------------------------------
    /**
     * Takes a touching-neighbors-matrix as input and delivers a vector with number of touching neighbors per label as a vector.
     */
    public boolean countTouchingNeighbors(ClearCLBuffer touch_matrix, ClearCLBuffer touching_neighbors_count_destination) {
        return CountTouchingNeighbors.countTouchingNeighbors(clijx, touch_matrix, touching_neighbors_count_destination);
    }


    // net.haesleinhuepf.clij.advancedfilters.ReplaceIntensities
    //----------------------------------------------------
    /**
     * Replaces specific intensities specified in a vector image with given new values specified in a vector image.
     */
    public boolean replaceIntensities(ClearCLImageInterface arg1, ClearCLImageInterface arg2, ClearCLImageInterface arg3) {
        return ReplaceIntensities.replaceIntensities(clijx, arg1, arg2, arg3);
    }


    // net.haesleinhuepf.clij.painting.DrawTwoValueLine
    //----------------------------------------------------
    /**
     * Draws a line between two points with a given thickness. Pixels close to point 1 are set to value1. Pixels closer to point 2 are set to value2 All pixels other than on the line are untouched. Consider using clij.set(buffer, 0); in advance.
     */
    public boolean drawTwoValueLine(ClearCLBuffer destination, double x1, double y1, double z1, double x2, double y2, double z2, double thickness, double value1, double destination0) {
        return DrawTwoValueLine.drawTwoValueLine(clijx, destination, new Double (x1).floatValue(), new Double (y1).floatValue(), new Double (z1).floatValue(), new Double (x2).floatValue(), new Double (y2).floatValue(), new Double (z2).floatValue(), new Double (thickness).floatValue(), new Double (value1).floatValue(), new Double (destination0).floatValue());
    }


    // net.haesleinhuepf.clij.matrix.AverageDistanceOfNClosestPoints
    //----------------------------------------------------
    /**
     * Determine the n point indices with shortest distance for all points in a distance matrix.
     * This corresponds to the n row indices with minimum values for each column of the distance matrix.
     */
    public boolean averageDistanceOfClosestPoints(ClearCLBuffer distance_matrix, ClearCLBuffer indexlist_destination, double nClosestPointsTofind) {
        return AverageDistanceOfNClosestPoints.averageDistanceOfClosestPoints(clijx, distance_matrix, indexlist_destination, new Double (nClosestPointsTofind).intValue());
    }


    // net.haesleinhuepf.clij.advancedfilters.SaveAsTIF
    //----------------------------------------------------
    /**
     * Pulls an image from the GPU memory and saves it as TIF to disc.
     */
    public boolean saveAsTIF(ClearCLBuffer arg1, String arg2) {
        return SaveAsTIF.saveAsTIF(clij, arg1, arg2);
    }

}
// 115 methods generated.
