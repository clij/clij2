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
import java.util.HashMap;
import ij.ImagePlus;
import java.util.List;
import java.util.ArrayList;
import net.haesleinhuepf.clij2.plugins.BinaryUnion;
import net.haesleinhuepf.clij2.plugins.BinaryIntersection;
import net.haesleinhuepf.clij2.plugins.ConnectedComponentsLabeling;
import net.haesleinhuepf.clij2.plugins.CountNonZeroPixels;
import net.haesleinhuepf.clij2.plugins.DifferenceOfGaussian2D;
import net.haesleinhuepf.clij2.plugins.DifferenceOfGaussian3D;
import net.haesleinhuepf.clij2.plugins.MaskLabel;
import net.haesleinhuepf.clij2.plugins.MeanClosestSpotDistance;
import net.haesleinhuepf.clij2.plugins.MeanSquaredError;
import net.haesleinhuepf.clij2.plugins.MedianZProjection;
import net.haesleinhuepf.clij2.plugins.NonzeroMinimumDiamond;
import net.haesleinhuepf.clij2.plugins.Paste2D;
import net.haesleinhuepf.clij2.plugins.Paste3D;
import net.haesleinhuepf.clij2.plugins.JaccardIndex;
import net.haesleinhuepf.clij2.plugins.SorensenDiceCoefficient;
import net.haesleinhuepf.clij2.plugins.StandardDeviationZProjection;
import net.haesleinhuepf.clij2.plugins.TopHatBox;
import net.haesleinhuepf.clij2.plugins.TopHatSphere;
import net.haesleinhuepf.clij2.plugins.Exponential;
import net.haesleinhuepf.clij2.plugins.Logarithm;
import net.haesleinhuepf.clij2.plugins.GenerateDistanceMatrix;
import net.haesleinhuepf.clij2.plugins.ShortestDistances;
import net.haesleinhuepf.clij2.plugins.SpotsToPointList;
import net.haesleinhuepf.clij2.plugins.TransposeXY;
import net.haesleinhuepf.clij2.plugins.TransposeXZ;
import net.haesleinhuepf.clij2.plugins.TransposeYZ;
import net.haesleinhuepf.clij2.plugins.SetWhereXequalsY;
import net.haesleinhuepf.clij2.plugins.LaplaceDiamond;
import net.haesleinhuepf.clij2.plugins.Image2DToResultsTable;
import net.haesleinhuepf.clij2.plugins.WriteValuesToPositions;
import net.haesleinhuepf.clij2.plugins.GetSize;
import net.haesleinhuepf.clij2.plugins.MultiplyMatrix;
import net.haesleinhuepf.clij2.plugins.MatrixEqual;
import net.haesleinhuepf.clij2.plugins.PowerImages;
import net.haesleinhuepf.clij2.plugins.Equal;
import net.haesleinhuepf.clij2.plugins.GreaterOrEqual;
import net.haesleinhuepf.clij2.plugins.Greater;
import net.haesleinhuepf.clij2.plugins.Smaller;
import net.haesleinhuepf.clij2.plugins.SmallerOrEqual;
import net.haesleinhuepf.clij2.plugins.NotEqual;
import net.haesleinhuepf.clij2.plugins.EqualConstant;
import net.haesleinhuepf.clij2.plugins.GreaterOrEqualConstant;
import net.haesleinhuepf.clij2.plugins.GreaterConstant;
import net.haesleinhuepf.clij2.plugins.SmallerConstant;
import net.haesleinhuepf.clij2.plugins.SmallerOrEqualConstant;
import net.haesleinhuepf.clij2.plugins.NotEqualConstant;
import net.haesleinhuepf.clij2.plugins.DrawBox;
import net.haesleinhuepf.clij2.plugins.DrawLine;
import net.haesleinhuepf.clij2.plugins.DrawSphere;
import net.haesleinhuepf.clij2.plugins.ReplaceIntensity;
import net.haesleinhuepf.clij2.plugins.BoundingBox;
import net.haesleinhuepf.clij2.plugins.MinimumOfMaskedPixels;
import net.haesleinhuepf.clij2.plugins.MaximumOfMaskedPixels;
import net.haesleinhuepf.clij2.plugins.MeanOfMaskedPixels;
import net.haesleinhuepf.clij2.plugins.LabelToMask;
import net.haesleinhuepf.clij2.plugins.NClosestPoints;
import net.haesleinhuepf.clij2.plugins.StatisticsOfLabelledPixels;
import net.haesleinhuepf.clij2.plugins.VarianceOfAllPixels;
import net.haesleinhuepf.clij2.plugins.StandardDeviationOfAllPixels;
import net.haesleinhuepf.clij2.plugins.VarianceOfMaskedPixels;
import net.haesleinhuepf.clij2.plugins.StandardDeviationOfMaskedPixels;
import net.haesleinhuepf.clij2.plugins.ExcludeLabelsOnEdges;
import net.haesleinhuepf.clij2.plugins.BinarySubtract;
import net.haesleinhuepf.clij2.plugins.BinaryEdgeDetection;
import net.haesleinhuepf.clij2.plugins.DistanceMap;
import net.haesleinhuepf.clij2.plugins.PullAsROI;
import net.haesleinhuepf.clij2.plugins.PullLabelsToROIManager;
import net.haesleinhuepf.clij2.plugins.NonzeroMaximumDiamond;
import net.haesleinhuepf.clij2.plugins.OnlyzeroOverwriteMaximumDiamond;
import net.haesleinhuepf.clij2.plugins.OnlyzeroOverwriteMaximumBox;
import net.haesleinhuepf.clij2.plugins.GenerateTouchMatrix;
import net.haesleinhuepf.clij2.plugins.DetectLabelEdges;
import net.haesleinhuepf.clij2.plugins.CountTouchingNeighbors;
import net.haesleinhuepf.clij2.plugins.ReplaceIntensities;
import net.haesleinhuepf.clij2.plugins.AverageDistanceOfNClosestPoints;
import net.haesleinhuepf.clij2.plugins.SaveAsTIF;
import net.haesleinhuepf.clij2.plugins.TouchMatrixToMesh;
import net.haesleinhuepf.clij2.plugins.Resample;
import net.haesleinhuepf.clij2.plugins.EqualizeMeanIntensitiesOfSlices;
import net.haesleinhuepf.clij2.plugins.Watershed;
import net.haesleinhuepf.clij2.plugins.ResliceRadial;
import net.haesleinhuepf.clij2.plugins.Sobel;
import net.haesleinhuepf.clij2.plugins.Absolute;
import net.haesleinhuepf.clij2.plugins.LaplaceBox;
import net.haesleinhuepf.clij2.plugins.BottomHatBox;
import net.haesleinhuepf.clij2.plugins.BottomHatSphere;
import net.haesleinhuepf.clij2.plugins.ClosingBox;
import net.haesleinhuepf.clij2.plugins.ClosingDiamond;
import net.haesleinhuepf.clij2.plugins.OpeningBox;
import net.haesleinhuepf.clij2.plugins.OpeningDiamond;
import net.haesleinhuepf.clij2.plugins.MaximumXProjection;
import net.haesleinhuepf.clij2.plugins.MaximumYProjection;
import net.haesleinhuepf.clij2.plugins.MaximumZProjectionBounded;
import net.haesleinhuepf.clij2.plugins.MinimumZProjectionBounded;
import net.haesleinhuepf.clij2.plugins.MeanZProjectionBounded;
import net.haesleinhuepf.clij2.plugins.NonzeroMaximumBox;
import net.haesleinhuepf.clij2.plugins.NonzeroMinimumBox;
import net.haesleinhuepf.clij2.plugins.MinimumZProjectionThresholdedBounded;
import net.haesleinhuepf.clij2.plugins.MeanOfPixelsAboveThreshold;
import net.haesleinhuepf.clij2.plugins.DistanceMatrixToMesh;
import net.haesleinhuepf.clij2.plugins.PointIndexListToMesh;
import net.haesleinhuepf.clij2.plugins.MinimumOctagon;
import net.haesleinhuepf.clij2.plugins.MaximumOctagon;
import net.haesleinhuepf.clij2.plugins.AddImages;
import net.haesleinhuepf.clij2.plugins.AddImagesWeighted;
import net.haesleinhuepf.clij2.plugins.SubtractImages;
import net.haesleinhuepf.clij2.plugins.AffineTransform2D;
import net.haesleinhuepf.clij2.plugins.AffineTransform3D;
import net.haesleinhuepf.clij2.plugins.ApplyVectorField2D;
import net.haesleinhuepf.clij2.plugins.ApplyVectorField3D;
import net.haesleinhuepf.clij2.plugins.ArgMaximumZProjection;
import net.haesleinhuepf.clij2.plugins.Histogram;
import net.haesleinhuepf.clij2.plugins.AutomaticThreshold;
import net.haesleinhuepf.clij2.plugins.Threshold;
import net.haesleinhuepf.clij2.plugins.BinaryOr;
import net.haesleinhuepf.clij2.plugins.BinaryAnd;
import net.haesleinhuepf.clij2.plugins.BinaryXOr;
import net.haesleinhuepf.clij2.plugins.BinaryNot;
import net.haesleinhuepf.clij2.plugins.ErodeSphere;
import net.haesleinhuepf.clij2.plugins.ErodeBox;
import net.haesleinhuepf.clij2.plugins.ErodeSphereSliceBySlice;
import net.haesleinhuepf.clij2.plugins.ErodeBoxSliceBySlice;
import net.haesleinhuepf.clij2.plugins.DilateSphere;
import net.haesleinhuepf.clij2.plugins.DilateBox;
import net.haesleinhuepf.clij2.plugins.DilateSphereSliceBySlice;
import net.haesleinhuepf.clij2.plugins.DilateBoxSliceBySlice;
import net.haesleinhuepf.clij2.plugins.Copy;
import net.haesleinhuepf.clij2.plugins.CopySlice;
import net.haesleinhuepf.clij2.plugins.Crop2D;
import net.haesleinhuepf.clij2.plugins.Crop3D;
import net.haesleinhuepf.clij2.plugins.Set;
import net.haesleinhuepf.clij2.plugins.Flip2D;
import net.haesleinhuepf.clij2.plugins.Flip3D;
import net.haesleinhuepf.clij2.plugins.RotateCounterClockwise;
import net.haesleinhuepf.clij2.plugins.RotateClockwise;
import net.haesleinhuepf.clij2.plugins.Mask;
import net.haesleinhuepf.clij2.plugins.MaskStackWithPlane;
import net.haesleinhuepf.clij2.plugins.MaximumZProjection;
import net.haesleinhuepf.clij2.plugins.MeanZProjection;
import net.haesleinhuepf.clij2.plugins.MinimumZProjection;
import net.haesleinhuepf.clij2.plugins.Power;
import net.haesleinhuepf.clij2.plugins.DivideImages;
import net.haesleinhuepf.clij2.plugins.MaximumImages;
import net.haesleinhuepf.clij2.plugins.MaximumImageAndScalar;
import net.haesleinhuepf.clij2.plugins.MinimumImages;
import net.haesleinhuepf.clij2.plugins.MinimumImageAndScalar;
import net.haesleinhuepf.clij2.plugins.MultiplyImageAndScalar;
import net.haesleinhuepf.clij2.plugins.MultiplyStackWithPlane;
import net.haesleinhuepf.clij2.plugins.CountNonZeroPixels2DSphere;
import net.haesleinhuepf.clij2.plugins.CountNonZeroPixelsSliceBySliceSphere;
import net.haesleinhuepf.clij2.plugins.CountNonZeroVoxels3DSphere;
import net.haesleinhuepf.clij2.plugins.SumZProjection;
import net.haesleinhuepf.clij2.plugins.SumOfAllPixels;
import net.haesleinhuepf.clij2.plugins.CenterOfMass;
import net.haesleinhuepf.clij2.plugins.Invert;
import net.haesleinhuepf.clij2.plugins.Downsample2D;
import net.haesleinhuepf.clij2.plugins.Downsample3D;
import net.haesleinhuepf.clij2.plugins.DownsampleSliceBySliceHalfMedian;
import net.haesleinhuepf.clij2.plugins.LocalThreshold;
import net.haesleinhuepf.clij2.plugins.GradientX;
import net.haesleinhuepf.clij2.plugins.GradientY;
import net.haesleinhuepf.clij2.plugins.GradientZ;
import net.haesleinhuepf.clij2.plugins.MultiplyImageAndCoordinate;
import net.haesleinhuepf.clij2.plugins.Mean2DBox;
import net.haesleinhuepf.clij2.plugins.Mean2DSphere;
import net.haesleinhuepf.clij2.plugins.Mean3DBox;
import net.haesleinhuepf.clij2.plugins.Mean3DSphere;
import net.haesleinhuepf.clij2.plugins.MeanSliceBySliceSphere;
import net.haesleinhuepf.clij2.plugins.MeanOfAllPixels;
import net.haesleinhuepf.clij2.plugins.Median2DBox;
import net.haesleinhuepf.clij2.plugins.Median2DSphere;
import net.haesleinhuepf.clij2.plugins.Median3DBox;
import net.haesleinhuepf.clij2.plugins.Median3DSphere;
import net.haesleinhuepf.clij2.plugins.MedianSliceBySliceBox;
import net.haesleinhuepf.clij2.plugins.MedianSliceBySliceSphere;
import net.haesleinhuepf.clij2.plugins.Maximum2DSphere;
import net.haesleinhuepf.clij2.plugins.Maximum3DSphere;
import net.haesleinhuepf.clij2.plugins.Maximum2DBox;
import net.haesleinhuepf.clij2.plugins.Maximum3DBox;
import net.haesleinhuepf.clij2.plugins.MaximumSliceBySliceSphere;
import net.haesleinhuepf.clij2.plugins.Minimum2DSphere;
import net.haesleinhuepf.clij2.plugins.Minimum3DSphere;
import net.haesleinhuepf.clij2.plugins.Minimum2DBox;
import net.haesleinhuepf.clij2.plugins.Minimum3DBox;
import net.haesleinhuepf.clij2.plugins.MinimumSliceBySliceSphere;
import net.haesleinhuepf.clij2.plugins.MultiplyImages;
import net.haesleinhuepf.clij2.plugins.GaussianBlur2D;
import net.haesleinhuepf.clij2.plugins.GaussianBlur3D;
import net.haesleinhuepf.clij2.plugins.ResliceBottom;
import net.haesleinhuepf.clij2.plugins.ResliceTop;
import net.haesleinhuepf.clij2.plugins.ResliceLeft;
import net.haesleinhuepf.clij2.plugins.ResliceRight;
import net.haesleinhuepf.clij2.plugins.Rotate2D;
import net.haesleinhuepf.clij2.plugins.Rotate3D;
import net.haesleinhuepf.clij2.plugins.Scale2D;
import net.haesleinhuepf.clij2.plugins.Scale3D;
import net.haesleinhuepf.clij2.plugins.Translate2D;
import net.haesleinhuepf.clij2.plugins.Translate3D;
import net.haesleinhuepf.clij2.plugins.Clear;
import net.haesleinhuepf.clij2.plugins.ClInfo;
import net.haesleinhuepf.clij2.plugins.ConvertFloat;
import net.haesleinhuepf.clij2.plugins.ConvertUInt8;
import net.haesleinhuepf.clij2.plugins.ConvertUInt16;
import net.haesleinhuepf.clij2.plugins.Create2D;
import net.haesleinhuepf.clij2.plugins.Create3D;
import net.haesleinhuepf.clij2.plugins.Pull;
import net.haesleinhuepf.clij2.plugins.PullBinary;
import net.haesleinhuepf.clij2.plugins.Push;
import net.haesleinhuepf.clij2.plugins.PushCurrentSlice;
import net.haesleinhuepf.clij2.plugins.PushCurrentZStack;
import net.haesleinhuepf.clij2.plugins.PushCurrentSelection;
import net.haesleinhuepf.clij2.plugins.PushCurrentSliceSelection;
import net.haesleinhuepf.clij2.plugins.Release;
import net.haesleinhuepf.clij2.plugins.AddImageAndScalar;
import net.haesleinhuepf.clij2.plugins.DetectMinimaBox;
import net.haesleinhuepf.clij2.plugins.DetectMaximaBox;
import net.haesleinhuepf.clij2.plugins.DetectMaximaSliceBySliceBox;
import net.haesleinhuepf.clij2.plugins.DetectMinimaSliceBySliceBox;
import net.haesleinhuepf.clij2.plugins.MaximumOfAllPixels;
import net.haesleinhuepf.clij2.plugins.MinimumOfAllPixels;
import net.haesleinhuepf.clij2.plugins.ReportMemory;
import net.haesleinhuepf.clij2.plugins.SetColumn;
import net.haesleinhuepf.clij2.plugins.SetRow;
import net.haesleinhuepf.clij2.plugins.SumYProjection;
import net.haesleinhuepf.clij2.plugins.AverageDistanceOfTouchingNeighbors;
import net.haesleinhuepf.clij2.plugins.LabelledSpotsToPointList;
import net.haesleinhuepf.clij2.plugins.LabelSpots;
import net.haesleinhuepf.clij2.plugins.MinimumDistanceOfTouchingNeighbors;
import net.haesleinhuepf.clij2.plugins.SetWhereXgreaterThanY;
import net.haesleinhuepf.clij2.plugins.SetWhereXsmallerThanY;
import net.haesleinhuepf.clij2.plugins.SetNonZeroPixelsToPixelIndex;
import net.haesleinhuepf.clij2.plugins.CloseIndexGapsInLabelMap;
import net.haesleinhuepf.clij2.plugins.AffineTransform;
import net.haesleinhuepf.clij2.plugins.Scale;
import net.haesleinhuepf.clij2.plugins.CentroidsOfLabels;
import net.haesleinhuepf.clij2.plugins.SetRampX;
import net.haesleinhuepf.clij2.plugins.SetRampY;
import net.haesleinhuepf.clij2.plugins.SetRampZ;
import net.haesleinhuepf.clij2.plugins.SubtractImageFromScalar;
import net.haesleinhuepf.clij2.plugins.ThresholdDefault;
import net.haesleinhuepf.clij2.plugins.ThresholdOtsu;
import net.haesleinhuepf.clij2.plugins.ThresholdHuang;
import net.haesleinhuepf.clij2.plugins.ThresholdIntermodes;
import net.haesleinhuepf.clij2.plugins.ThresholdIsoData;
import net.haesleinhuepf.clij2.plugins.ThresholdIJ_IsoData;
import net.haesleinhuepf.clij2.plugins.ThresholdLi;
import net.haesleinhuepf.clij2.plugins.ThresholdMaxEntropy;
import net.haesleinhuepf.clij2.plugins.ThresholdMean;
import net.haesleinhuepf.clij2.plugins.ThresholdMinError;
import net.haesleinhuepf.clij2.plugins.ThresholdMinimum;
import net.haesleinhuepf.clij2.plugins.ThresholdMoments;
import net.haesleinhuepf.clij2.plugins.ThresholdPercentile;
import net.haesleinhuepf.clij2.plugins.ThresholdRenyiEntropy;
import net.haesleinhuepf.clij2.plugins.ThresholdShanbhag;
import net.haesleinhuepf.clij2.plugins.ThresholdTriangle;
import net.haesleinhuepf.clij2.plugins.ThresholdYen;
import net.haesleinhuepf.clij2.plugins.ExcludeLabelsSubSurface;
import net.haesleinhuepf.clij2.plugins.ExcludeLabelsOnSurface;
import net.haesleinhuepf.clij2.plugins.SetPlane;
import net.haesleinhuepf.clij2.plugins.ImageToStack;
import net.haesleinhuepf.clij2.plugins.SumXProjection;
import net.haesleinhuepf.clij2.plugins.SumImageSliceBySlice;
import net.haesleinhuepf.clij2.plugins.MultiplyImageStackWithScalars;
import net.haesleinhuepf.clij2.plugins.Print;
import net.haesleinhuepf.clij2.plugins.VoronoiOctagon;
import net.haesleinhuepf.clij2.plugins.SetImageBorders;
import net.haesleinhuepf.clij2.plugins.FloodFillDiamond;
import net.haesleinhuepf.clij2.plugins.BinaryFillHoles;
import net.haesleinhuepf.clij2.plugins.ConnectedComponentsLabelingDiamond;
import net.haesleinhuepf.clij2.plugins.ConnectedComponentsLabelingBox;
import net.haesleinhuepf.clij2.plugins.SetRandom;
import net.haesleinhuepf.clij2.plugins.InvalidateKernelCache;
import net.haesleinhuepf.clij2.plugins.EntropyBox;
import net.haesleinhuepf.clij2.plugins.ConcatenateStacks;
import net.haesleinhuepf.clij2.plugins.ResultsTableToImage2D;
import net.haesleinhuepf.clij2.plugins.GetAutomaticThreshold;
import net.haesleinhuepf.clij2.plugins.GetDimensions;
import net.haesleinhuepf.clij2.plugins.CustomOperation;
import net.haesleinhuepf.clij2.plugins.PullLabelsToROIList;
import net.haesleinhuepf.clij2.plugins.MeanOfTouchingNeighbors;
import net.haesleinhuepf.clij2.plugins.MinimumOfTouchingNeighbors;
import net.haesleinhuepf.clij2.plugins.MaximumOfTouchingNeighbors;
import net.haesleinhuepf.clij2.plugins.ResultsTableColumnToImage;
import net.haesleinhuepf.clij2.plugins.StatisticsOfBackgroundAndLabelledPixels;
import net.haesleinhuepf.clij2.plugins.GetGPUProperties;
import net.haesleinhuepf.clij2.plugins.GetSumOfAllPixels;
import net.haesleinhuepf.clij2.plugins.GetSorensenDiceCoefficient;
import net.haesleinhuepf.clij2.plugins.GetMinimumOfAllPixels;
import net.haesleinhuepf.clij2.plugins.GetMaximumOfAllPixels;
import net.haesleinhuepf.clij2.plugins.GetMeanOfAllPixels;
import net.haesleinhuepf.clij2.plugins.GetJaccardIndex;
import net.haesleinhuepf.clij2.plugins.GetCenterOfMass;
import net.haesleinhuepf.clij2.plugins.GetBoundingBox;
import net.haesleinhuepf.clij2.plugins.PushArray;
import net.haesleinhuepf.clij2.plugins.PullString;
import net.haesleinhuepf.clij2.plugins.PushString;
import net.haesleinhuepf.clij2.plugins.MedianOfTouchingNeighbors;
import net.haesleinhuepf.clij2.plugins.PushResultsTableColumn;
import net.haesleinhuepf.clij2.plugins.PushResultsTable;
import net.haesleinhuepf.clij2.plugins.PullToResultsTable;
import net.haesleinhuepf.clij2.plugins.LabelVoronoiOctagon;
import net.haesleinhuepf.clij2.plugins.TouchMatrixToAdjacencyMatrix;
import net.haesleinhuepf.clij2.plugins.AdjacencyMatrixToTouchMatrix;
import net.haesleinhuepf.clij2.plugins.PointlistToLabelledSpots;
import net.haesleinhuepf.clij2.plugins.StatisticsOfImage;
import net.haesleinhuepf.clij2.plugins.NClosestDistances;
import net.haesleinhuepf.clij2.plugins.ExcludeLabels;
import net.haesleinhuepf.clij2.plugins.AverageDistanceOfNFarOffPoints;
import net.haesleinhuepf.clij2.plugins.StandardDeviationOfTouchingNeighbors;
import net.haesleinhuepf.clij2.plugins.NeighborsOfNeighbors;
import net.haesleinhuepf.clij2.plugins.GenerateParametricImage;
import net.haesleinhuepf.clij2.plugins.GenerateParametricImageFromResultsTableColumn;
import net.haesleinhuepf.clij2.plugins.ExcludeLabelsWithValuesOutOfRange;
import net.haesleinhuepf.clij2.plugins.ExcludeLabelsWithValuesWithinRange;
import net.haesleinhuepf.clij2.plugins.CombineVertically;
import net.haesleinhuepf.clij2.plugins.CombineHorizontally;
import net.haesleinhuepf.clij2.plugins.ReduceStack;
import net.haesleinhuepf.clij2.plugins.DetectMinima2DBox;
import net.haesleinhuepf.clij2.plugins.DetectMaxima2DBox;
import net.haesleinhuepf.clij2.plugins.DetectMinima3DBox;
import net.haesleinhuepf.clij2.plugins.DetectMaxima3DBox;
import net.haesleinhuepf.clij2.plugins.DepthColorProjection;
import net.haesleinhuepf.clij2.plugins.GenerateBinaryOverlapMatrix;
import net.haesleinhuepf.clij2.plugins.ResliceRadialTop;
import net.haesleinhuepf.clij2.plugins.Convolve;
import net.haesleinhuepf.clij2.plugins.UndefinedToZero;
import net.haesleinhuepf.clij2.plugins.GenerateJaccardIndexMatrix;
import net.haesleinhuepf.clij2.plugins.GenerateTouchCountMatrix;
import net.haesleinhuepf.clij2.plugins.MinimumXProjection;
import net.haesleinhuepf.clij2.plugins.MinimumYProjection;
import net.haesleinhuepf.clij2.plugins.MeanXProjection;
import net.haesleinhuepf.clij2.plugins.MeanYProjection;
import net.haesleinhuepf.clij2.plugins.SquaredDifference;
import net.haesleinhuepf.clij2.plugins.AbsoluteDifference;
import net.haesleinhuepf.clij2.plugins.ReplacePixelsIfZero;
import net.haesleinhuepf.clij2.plugins.VoronoiLabeling;
import net.haesleinhuepf.clij2.plugins.ExtendLabelingViaVoronoi;
import net.haesleinhuepf.clij2.plugins.CentroidsOfBackgroundAndLabels;
import net.haesleinhuepf.clij2.plugins.GetMeanOfMaskedPixels;
// this is generated code. See src/test/java/net/haesleinhuepf/clijx/codegenerator for details
public abstract interface CLIJ2Ops {
   CLIJ getCLIJ();
   CLIJ2 getCLIJ2();
   boolean doTimeTracing();
   void recordMethodStart(String method);
   void recordMethodEnd(String method);
   

    // net.haesleinhuepf.clij2.plugins.BinaryUnion
    //----------------------------------------------------
    /**
     * Computes a binary image (containing pixel values 0 and 1) from two images X and Y by connecting pairs of
     * pixels x and y with the binary union operator |.
     * 
     * All pixel values except 0 in the input images are interpreted as 1.<pre>f(x, y) = x | y</pre>
     * 
     * Parameters
     * ----------
     * operand1 : Image
     *     The first binary input image to be processed.
     * operand2 : Image
     *     The second binary input image to be processed.
     * destination : Image
     *     The output image where results are written into.
     * 
     */
    default boolean binaryUnion(ClearCLBuffer operand1, ClearCLBuffer operand2, ClearCLBuffer destination) {
        if (doTimeTracing()) {recordMethodStart("BinaryUnion");}
        boolean result = BinaryUnion.binaryUnion(getCLIJ2(), operand1, operand2, destination);
        if (doTimeTracing()) {recordMethodEnd("BinaryUnion");}
        return result;
    }


    // net.haesleinhuepf.clij2.plugins.BinaryIntersection
    //----------------------------------------------------
    /**
     * Computes a binary image (containing pixel values 0 and 1) from two images X and Y by connecting pairs of
     * pixels x and y with the binary intersection operator &.
     * All pixel values except 0 in the input images are interpreted as 1.
     * 
     * <pre>f(x, y) = x & y</pre>
     * 
     * Parameters
     * ----------
     * operand1 : Image
     *     The first binary input image to be processed.
     * operand2 : Image
     *     The second binary input image to be processed.
     * destination : Image
     *     The output image where results are written into.
     * 
     */
    default boolean binaryIntersection(ClearCLBuffer operand1, ClearCLBuffer operand2, ClearCLBuffer destination) {
        if (doTimeTracing()) {recordMethodStart("BinaryIntersection");}
        boolean result = BinaryIntersection.binaryIntersection(getCLIJ2(), operand1, operand2, destination);
        if (doTimeTracing()) {recordMethodEnd("BinaryIntersection");}
        return result;
    }


    // net.haesleinhuepf.clij2.plugins.ConnectedComponentsLabeling
    //----------------------------------------------------
    /**
     * Performs connected components analysis to a binary image and generates a label map.
     * 
     * DEPRECATED: This method is deprecated. Use ConnectedComponentsLabellingBox (or Diamond) instead.
     */
    @Deprecated
    default boolean connectedComponentsLabeling(ClearCLImageInterface binary_input, ClearCLImageInterface labeling_destination) {
        System.out.println("connectedComponentsLabeling is deprecated. Check the documentation for a replacement. https://clij.github.io/clij2-doccs/reference");
        if (doTimeTracing()) {recordMethodStart("ConnectedComponentsLabeling");}
        boolean result = ConnectedComponentsLabeling.connectedComponentsLabeling(getCLIJ2(), binary_input, labeling_destination);
        if (doTimeTracing()) {recordMethodEnd("ConnectedComponentsLabeling");}
        return result;
    }


    // net.haesleinhuepf.clij2.plugins.CountNonZeroPixels
    //----------------------------------------------------
    /**
     * Determines the number of all pixels in a given image which are not equal to 0. 
     * 
     * It will be stored in a new row of ImageJs
     * Results table in the column 'CountNonZero'.
     */
    default double countNonZeroPixels(ClearCLBuffer source) {
        if (doTimeTracing()) {recordMethodStart("CountNonZeroPixels");}
        double result = CountNonZeroPixels.countNonZeroPixels(getCLIJ2(), source);
        if (doTimeTracing()) {recordMethodEnd("CountNonZeroPixels");}
        return result;
    }


    // net.haesleinhuepf.clij2.plugins.DifferenceOfGaussian2D
    //----------------------------------------------------
    /**
     * Applies Gaussian blur to the input image twice with different sigma values resulting in two images which are then subtracted from each other.
     * 
     * It is recommended to apply this operation to images of type Float (32 bit) as results might be negative.
     * 
     * Parameters
     * ----------
     * input : Image
     *     The input image to be processed.
     * destination : Image
     *     The output image where results are written into.
     * sigma1_x : float
     *     Sigma of the first Gaussian filter in x
     * sigma1_y : float
     *     Sigma of the first Gaussian filter in y
     * sigma2_x : float
     *     Sigma of the second Gaussian filter in x
     * sigma2_y : float
     *     Sigma of the second Gaussian filter in y
     */
    default boolean differenceOfGaussian(ClearCLBuffer arg1, ClearCLBuffer arg2, double arg3, double arg4, double arg5, double arg6) {
        if (doTimeTracing()) {recordMethodStart("DifferenceOfGaussian2D");}
        boolean result = DifferenceOfGaussian2D.differenceOfGaussian(getCLIJ2(), arg1, arg2, new Double (arg3).floatValue(), new Double (arg4).floatValue(), new Double (arg5).floatValue(), new Double (arg6).floatValue());
        if (doTimeTracing()) {recordMethodEnd("DifferenceOfGaussian2D");}
        return result;
    }

    /**
     * Applies Gaussian blur to the input image twice with different sigma values resulting in two images which are then subtracted from each other.
     * 
     * It is recommended to apply this operation to images of type Float (32 bit) as results might be negative.
     * 
     * Parameters
     * ----------
     * input : Image
     *     The input image to be processed.
     * destination : Image
     *     The output image where results are written into.
     * sigma1_x : float
     *     Sigma of the first Gaussian filter in x
     * sigma1_y : float
     *     Sigma of the first Gaussian filter in y
     * sigma2_x : float
     *     Sigma of the second Gaussian filter in x
     * sigma2_y : float
     *     Sigma of the second Gaussian filter in y
     */
    default boolean differenceOfGaussian2D(ClearCLBuffer arg1, ClearCLBuffer arg2, double arg3, double arg4, double arg5, double arg6) {
        if (doTimeTracing()) {recordMethodStart("DifferenceOfGaussian2D");}
        boolean result = DifferenceOfGaussian2D.differenceOfGaussian2D(getCLIJ2(), arg1, arg2, new Double (arg3).floatValue(), new Double (arg4).floatValue(), new Double (arg5).floatValue(), new Double (arg6).floatValue());
        if (doTimeTracing()) {recordMethodEnd("DifferenceOfGaussian2D");}
        return result;
    }


    // net.haesleinhuepf.clij2.plugins.DifferenceOfGaussian3D
    //----------------------------------------------------
    /**
     * Applies Gaussian blur to the input image twice with different sigma values resulting in two images which are then subtracted from each other.
     * 
     * It is recommended to apply this operation to images of type Float (32 bit) as results might be negative.
     * 
     * Parameters
     * ----------
     * input : Image
     *     The input image to be processed.
     * destination : Image
     *     The output image where results are written into.
     * sigma1_x : float
     *     Sigma of the first Gaussian filter in x
     * sigma1_y : float
     *     Sigma of the first Gaussian filter in y
     * sigma2_x : float
     *     Sigma of the second Gaussian filter in x
     * sigma2_y : float
     *     Sigma of the second Gaussian filter in y
     */
    default boolean differenceOfGaussian(ClearCLBuffer arg1, ClearCLBuffer arg2, double arg3, double arg4, double arg5, double arg6, double arg7, double arg8) {
        if (doTimeTracing()) {recordMethodStart("DifferenceOfGaussian3D");}
        boolean result = DifferenceOfGaussian3D.differenceOfGaussian(getCLIJ2(), arg1, arg2, new Double (arg3).floatValue(), new Double (arg4).floatValue(), new Double (arg5).floatValue(), new Double (arg6).floatValue(), new Double (arg7).floatValue(), new Double (arg8).floatValue());
        if (doTimeTracing()) {recordMethodEnd("DifferenceOfGaussian3D");}
        return result;
    }

    /**
     * Applies Gaussian blur to the input image twice with different sigma values resulting in two images which are then subtracted from each other.
     * 
     * It is recommended to apply this operation to images of type Float (32 bit) as results might be negative.
     * 
     * Parameters
     * ----------
     * input : Image
     *     The input image to be processed.
     * destination : Image
     *     The output image where results are written into.
     * sigma1_x : float
     *     Sigma of the first Gaussian filter in x
     * sigma1_y : float
     *     Sigma of the first Gaussian filter in y
     * sigma1_z : float
     *     Sigma of the first Gaussian filter in z
     * sigma2_x : float
     *     Sigma of the second Gaussian filter in x
     * sigma2_y : float
     *     Sigma of the second Gaussian filter in y
     * sigma2_z : float
     *     Sigma of the second Gaussian filter in z
     */
    default boolean differenceOfGaussian3D(ClearCLBuffer arg1, ClearCLBuffer arg2, double arg3, double arg4, double arg5, double arg6, double arg7, double arg8) {
        if (doTimeTracing()) {recordMethodStart("DifferenceOfGaussian3D");}
        boolean result = DifferenceOfGaussian3D.differenceOfGaussian3D(getCLIJ2(), arg1, arg2, new Double (arg3).floatValue(), new Double (arg4).floatValue(), new Double (arg5).floatValue(), new Double (arg6).floatValue(), new Double (arg7).floatValue(), new Double (arg8).floatValue());
        if (doTimeTracing()) {recordMethodEnd("DifferenceOfGaussian3D");}
        return result;
    }


    // net.haesleinhuepf.clij2.plugins.MaskLabel
    //----------------------------------------------------
    /**
     * Computes a masked image by applying a label mask to an image. 
     * 
     * All pixel values x of image X will be copied
     * to the destination image in case pixel value m at the same position in the label_map image has the right index value i.
     * 
     * f(x,m,i) = (x if (m == i); (0 otherwise))
     */
    default boolean maskLabel(ClearCLBuffer arg1, ClearCLBuffer arg2, ClearCLBuffer arg3, double arg4) {
        if (doTimeTracing()) {recordMethodStart("MaskLabel");}
        boolean result = MaskLabel.maskLabel(getCLIJ2(), arg1, arg2, arg3, new Double (arg4).floatValue());
        if (doTimeTracing()) {recordMethodEnd("MaskLabel");}
        return result;
    }


    // net.haesleinhuepf.clij2.plugins.MeanClosestSpotDistance
    //----------------------------------------------------
    /**
     * Determines the distance between pairs of closest spots in two binary images. 
     * 
     * Takes two binary images A and B with marked spots and determines for each spot in image A the closest spot in image B. Afterwards, it saves the average shortest distances from image A to image B as 'mean_closest_spot_distance_A_B' and from image B to image A as 'mean_closest_spot_distance_B_A' to the results table. The distance between B and A is only determined if the `bidirectional` checkbox is checked.
     */
    default double meanClosestSpotDistance(ClearCLBuffer arg1, ClearCLBuffer arg2) {
        if (doTimeTracing()) {recordMethodStart("MeanClosestSpotDistance");}
        double result = MeanClosestSpotDistance.meanClosestSpotDistance(getCLIJ2(), arg1, arg2);
        if (doTimeTracing()) {recordMethodEnd("MeanClosestSpotDistance");}
        return result;
    }

    /**
     * Determines the distance between pairs of closest spots in two binary images. 
     * 
     * Takes two binary images A and B with marked spots and determines for each spot in image A the closest spot in image B. Afterwards, it saves the average shortest distances from image A to image B as 'mean_closest_spot_distance_A_B' and from image B to image A as 'mean_closest_spot_distance_B_A' to the results table. The distance between B and A is only determined if the `bidirectional` checkbox is checked.
     */
    default double[] meanClosestSpotDistance(ClearCLBuffer arg1, ClearCLBuffer arg2, boolean arg3) {
        if (doTimeTracing()) {recordMethodStart("MeanClosestSpotDistance");}
        double[] result = MeanClosestSpotDistance.meanClosestSpotDistance(getCLIJ2(), arg1, arg2, arg3);
        if (doTimeTracing()) {recordMethodEnd("MeanClosestSpotDistance");}
        return result;
    }


    // net.haesleinhuepf.clij2.plugins.MeanSquaredError
    //----------------------------------------------------
    /**
     * Determines the mean squared error (MSE) between two images. 
     * 
     * The MSE will be stored in a new row of ImageJs
     * Results table in the column 'MSE'.
     */
    default double meanSquaredError(ClearCLBuffer source1, ClearCLBuffer source2) {
        if (doTimeTracing()) {recordMethodStart("MeanSquaredError");}
        double result = MeanSquaredError.meanSquaredError(getCLIJ2(), source1, source2);
        if (doTimeTracing()) {recordMethodEnd("MeanSquaredError");}
        return result;
    }


    // net.haesleinhuepf.clij2.plugins.MedianZProjection
    //----------------------------------------------------
    /**
     * Determines the median intensity projection of an image stack along Z.
     */
    default boolean medianZProjection(ClearCLImageInterface source, ClearCLImageInterface destination) {
        if (doTimeTracing()) {recordMethodStart("MedianZProjection");}
        boolean result = MedianZProjection.medianZProjection(getCLIJ2(), source, destination);
        if (doTimeTracing()) {recordMethodEnd("MedianZProjection");}
        return result;
    }


    // net.haesleinhuepf.clij2.plugins.NonzeroMinimumDiamond
    //----------------------------------------------------
    /**
     * Apply a minimum filter (diamond shape) to the input image. 
     * 
     * The radius is fixed to 1 and pixels with value 0 are ignored.Note: Pixels with 0 value in the input image will not be overwritten in the output image.
     * Thus, the result image should be initialized by copying the original image in advance.
     */
    default boolean nonzeroMinimumDiamond(ClearCLImageInterface input, ClearCLImageInterface destination) {
        if (doTimeTracing()) {recordMethodStart("NonzeroMinimumDiamond");}
        boolean result = NonzeroMinimumDiamond.nonzeroMinimumDiamond(getCLIJ2(), input, destination);
        if (doTimeTracing()) {recordMethodEnd("NonzeroMinimumDiamond");}
        return result;
    }

    /**
     * Apply a minimum filter (diamond shape) to the input image. 
     * 
     * The radius is fixed to 1 and pixels with value 0 are ignored.Note: Pixels with 0 value in the input image will not be overwritten in the output image.
     * Thus, the result image should be initialized by copying the original image in advance.
     */
    default boolean nonzeroMinimumDiamond(ClearCLImageInterface arg1, ClearCLImageInterface arg2, ClearCLImageInterface arg3) {
        if (doTimeTracing()) {recordMethodStart("NonzeroMinimumDiamond");}
        boolean result = NonzeroMinimumDiamond.nonzeroMinimumDiamond(getCLIJ2(), arg1, arg2, arg3);
        if (doTimeTracing()) {recordMethodEnd("NonzeroMinimumDiamond");}
        return result;
    }

    /**
     * Apply a minimum filter (diamond shape) to the input image. 
     * 
     * The radius is fixed to 1 and pixels with value 0 are ignored.Note: Pixels with 0 value in the input image will not be overwritten in the output image.
     * Thus, the result image should be initialized by copying the original image in advance.
     */
    default ClearCLKernel nonzeroMinimumDiamond(ClearCLImageInterface arg1, ClearCLImageInterface arg2, ClearCLImageInterface arg3, ClearCLKernel arg4) {
        if (doTimeTracing()) {recordMethodStart("NonzeroMinimumDiamond");}
        ClearCLKernel result = NonzeroMinimumDiamond.nonzeroMinimumDiamond(getCLIJ2(), arg1, arg2, arg3, arg4);
        if (doTimeTracing()) {recordMethodEnd("NonzeroMinimumDiamond");}
        return result;
    }


    // net.haesleinhuepf.clij2.plugins.Paste2D
    //----------------------------------------------------
    /**
     * Pastes an image into another image at a given position.
     */
    default boolean paste(ClearCLImageInterface arg1, ClearCLImageInterface arg2, double arg3, double arg4) {
        if (doTimeTracing()) {recordMethodStart("Paste2D");}
        boolean result = Paste2D.paste(getCLIJ2(), arg1, arg2, new Double (arg3).intValue(), new Double (arg4).intValue());
        if (doTimeTracing()) {recordMethodEnd("Paste2D");}
        return result;
    }

    /**
     * Pastes an image into another image at a given position.
     */
    default boolean paste2D(ClearCLImageInterface arg1, ClearCLImageInterface arg2, double arg3, double arg4) {
        if (doTimeTracing()) {recordMethodStart("Paste2D");}
        boolean result = Paste2D.paste2D(getCLIJ2(), arg1, arg2, new Double (arg3).intValue(), new Double (arg4).intValue());
        if (doTimeTracing()) {recordMethodEnd("Paste2D");}
        return result;
    }


    // net.haesleinhuepf.clij2.plugins.Paste3D
    //----------------------------------------------------
    /**
     * Pastes an image into another image at a given position.
     */
    default boolean paste(ClearCLImageInterface arg1, ClearCLImageInterface arg2, double arg3, double arg4, double arg5) {
        if (doTimeTracing()) {recordMethodStart("Paste3D");}
        boolean result = Paste3D.paste(getCLIJ2(), arg1, arg2, new Double (arg3).intValue(), new Double (arg4).intValue(), new Double (arg5).intValue());
        if (doTimeTracing()) {recordMethodEnd("Paste3D");}
        return result;
    }

    /**
     * Pastes an image into another image at a given position.
     */
    default boolean paste3D(ClearCLImageInterface arg1, ClearCLImageInterface arg2, double arg3, double arg4, double arg5) {
        if (doTimeTracing()) {recordMethodStart("Paste3D");}
        boolean result = Paste3D.paste3D(getCLIJ2(), arg1, arg2, new Double (arg3).intValue(), new Double (arg4).intValue(), new Double (arg5).intValue());
        if (doTimeTracing()) {recordMethodEnd("Paste3D");}
        return result;
    }


    // net.haesleinhuepf.clij2.plugins.JaccardIndex
    //----------------------------------------------------
    /**
     * Determines the overlap of two binary images using the Jaccard index. 
     * 
     * A value of 0 suggests no overlap, 1 means perfect overlap.
     * The resulting Jaccard index is saved to the results table in the 'Jaccard_Index' column.
     * Note that the Sorensen-Dice coefficient can be calculated from the Jaccard index j using this formula:
     * <pre>s = f(j) = 2 j / (j + 1)</pre>
     */
    default double jaccardIndex(ClearCLBuffer source1, ClearCLBuffer source2) {
        if (doTimeTracing()) {recordMethodStart("JaccardIndex");}
        double result = JaccardIndex.jaccardIndex(getCLIJ2(), source1, source2);
        if (doTimeTracing()) {recordMethodEnd("JaccardIndex");}
        return result;
    }


    // net.haesleinhuepf.clij2.plugins.SorensenDiceCoefficient
    //----------------------------------------------------
    /**
     * Determines the overlap of two binary images using the Sorensen-Dice coefficent. 
     * 
     * A value of 0 suggests no overlap, 1 means perfect overlap.
     * The Sorensen-Dice coefficient is saved in the colum 'Sorensen_Dice_coefficient'.
     * Note that the Sorensen-Dice coefficient s can be calculated from the Jaccard index j using this formula:
     * <pre>s = f(j) = 2 j / (j + 1)</pre>
     */
    default double sorensenDiceCoefficient(ClearCLBuffer source1, ClearCLBuffer source2) {
        if (doTimeTracing()) {recordMethodStart("SorensenDiceCoefficient");}
        double result = SorensenDiceCoefficient.sorensenDiceCoefficient(getCLIJ2(), source1, source2);
        if (doTimeTracing()) {recordMethodEnd("SorensenDiceCoefficient");}
        return result;
    }


    // net.haesleinhuepf.clij2.plugins.StandardDeviationZProjection
    //----------------------------------------------------
    /**
     * Determines the standard deviation intensity projection of an image stack along Z.
     */
    default boolean standardDeviationZProjection(ClearCLImageInterface source, ClearCLImageInterface destination) {
        if (doTimeTracing()) {recordMethodStart("StandardDeviationZProjection");}
        boolean result = StandardDeviationZProjection.standardDeviationZProjection(getCLIJ2(), source, destination);
        if (doTimeTracing()) {recordMethodEnd("StandardDeviationZProjection");}
        return result;
    }


    // net.haesleinhuepf.clij2.plugins.TopHatBox
    //----------------------------------------------------
    /**
     * Applies a top-hat filter for background subtraction to the input image.
     * 
     * Parameters
     * ----------
     * input : Image
     *     The input image where the background is subtracted from.
     * destination : Image
     *     The output image where results are written into.
     * radius_x : Image
     *     Radius of the background determination region in X.
     * radius_y : Image
     *     Radius of the background determination region in Y.
     * radius_z : Image
     *     Radius of the background determination region in Z.
     * 
     */
    default boolean topHatBox(ClearCLBuffer arg1, ClearCLBuffer arg2, double arg3, double arg4, double arg5) {
        if (doTimeTracing()) {recordMethodStart("TopHatBox");}
        boolean result = TopHatBox.topHatBox(getCLIJ2(), arg1, arg2, new Double (arg3).intValue(), new Double (arg4).intValue(), new Double (arg5).intValue());
        if (doTimeTracing()) {recordMethodEnd("TopHatBox");}
        return result;
    }


    // net.haesleinhuepf.clij2.plugins.TopHatSphere
    //----------------------------------------------------
    /**
     * Applies a top-hat filter for background subtraction to the input image.
     * 
     * Parameters
     * ----------
     * input : Image
     *     The input image where the background is subtracted from.
     * destination : Image
     *     The output image where results are written into.
     * radius_x : Image
     *     Radius of the background determination region in X.
     * radius_y : Image
     *     Radius of the background determination region in Y.
     * radius_z : Image
     *     Radius of the background determination region in Z.
     * 
     */
    default boolean topHatSphere(ClearCLBuffer arg1, ClearCLBuffer arg2, double arg3, double arg4, double arg5) {
        if (doTimeTracing()) {recordMethodStart("TopHatSphere");}
        boolean result = TopHatSphere.topHatSphere(getCLIJ2(), arg1, arg2, new Double (arg3).intValue(), new Double (arg4).intValue(), new Double (arg5).intValue());
        if (doTimeTracing()) {recordMethodEnd("TopHatSphere");}
        return result;
    }


    // net.haesleinhuepf.clij2.plugins.Exponential
    //----------------------------------------------------
    /**
     * Computes base exponential of all pixels values.
     * 
     * f(x) = exp(x)
     */
    default boolean exponential(ClearCLImageInterface source, ClearCLImageInterface destination) {
        if (doTimeTracing()) {recordMethodStart("Exponential");}
        boolean result = Exponential.exponential(getCLIJ2(), source, destination);
        if (doTimeTracing()) {recordMethodEnd("Exponential");}
        return result;
    }


    // net.haesleinhuepf.clij2.plugins.Logarithm
    //----------------------------------------------------
    /**
     * Computes base e logarithm of all pixels values.
     * 
     * f(x) = log(x)
     */
    default boolean logarithm(ClearCLImageInterface source, ClearCLImageInterface destination) {
        if (doTimeTracing()) {recordMethodStart("Logarithm");}
        boolean result = Logarithm.logarithm(getCLIJ2(), source, destination);
        if (doTimeTracing()) {recordMethodEnd("Logarithm");}
        return result;
    }


    // net.haesleinhuepf.clij2.plugins.GenerateDistanceMatrix
    //----------------------------------------------------
    /**
     * Computes the distance between all point coordinates given in two point lists.
     * 
     * Takes two images containing pointlists (dimensionality n * d, n: number of points and d: dimensionality) and builds up a matrix containing the distances between these points. 
     * 
     * Convention: Given two point lists with dimensionality n * d and m * d, the distance matrix will be of size(n + 1) * (m + 1). The first row and column contain zeros. They represent the distance of the objects to a theoretical background object. In that way, distance matrices are of the same size as touch matrices (see generateTouchMatrix). Thus, one can threshold a distance matrix to generate a touch matrix out of it for drawing meshes.
     */
    default boolean generateDistanceMatrix(ClearCLBuffer coordinate_list1, ClearCLBuffer coordinate_list2, ClearCLBuffer distance_matrix_destination) {
        if (doTimeTracing()) {recordMethodStart("GenerateDistanceMatrix");}
        boolean result = GenerateDistanceMatrix.generateDistanceMatrix(getCLIJ2(), coordinate_list1, coordinate_list2, distance_matrix_destination);
        if (doTimeTracing()) {recordMethodEnd("GenerateDistanceMatrix");}
        return result;
    }


    // net.haesleinhuepf.clij2.plugins.ShortestDistances
    //----------------------------------------------------
    /**
     * Determine the shortest distance from a distance matrix. 
     * 
     * This corresponds to the minimum for each individial column.
     */
    default boolean shortestDistances(ClearCLBuffer distance_matrix, ClearCLBuffer destination_minimum_distances) {
        if (doTimeTracing()) {recordMethodStart("ShortestDistances");}
        boolean result = ShortestDistances.shortestDistances(getCLIJ2(), distance_matrix, destination_minimum_distances);
        if (doTimeTracing()) {recordMethodEnd("ShortestDistances");}
        return result;
    }


    // net.haesleinhuepf.clij2.plugins.SpotsToPointList
    //----------------------------------------------------
    /**
     * Transforms a spots image as resulting from maximum/minimum detection in an image where every column contains d 
     * pixels (with d = dimensionality of the original image) with the coordinates of the maxima/minima.
     */
    default boolean spotsToPointList(ClearCLBuffer input_spots, ClearCLBuffer destination_pointlist) {
        if (doTimeTracing()) {recordMethodStart("SpotsToPointList");}
        boolean result = SpotsToPointList.spotsToPointList(getCLIJ2(), input_spots, destination_pointlist);
        if (doTimeTracing()) {recordMethodEnd("SpotsToPointList");}
        return result;
    }


    // net.haesleinhuepf.clij2.plugins.TransposeXY
    //----------------------------------------------------
    /**
     * Transpose X and Y axes of an image.
     * 
     * Parameters
     * ----------
     * input : Image
     *     The input image.
     * destination : Image
     *     The output image where results are written into.
     * 
     */
    default boolean transposeXY(ClearCLBuffer input, ClearCLBuffer destination) {
        if (doTimeTracing()) {recordMethodStart("TransposeXY");}
        boolean result = TransposeXY.transposeXY(getCLIJ2(), input, destination);
        if (doTimeTracing()) {recordMethodEnd("TransposeXY");}
        return result;
    }


    // net.haesleinhuepf.clij2.plugins.TransposeXZ
    //----------------------------------------------------
    /**
     * Transpose X and Z axes of an image.
     * 
     * Parameters
     * ----------
     * input : Image
     *     The input image.
     * destination : Image
     *     The output image where results are written into.
     * 
     */
    default boolean transposeXZ(ClearCLBuffer input, ClearCLBuffer destination) {
        if (doTimeTracing()) {recordMethodStart("TransposeXZ");}
        boolean result = TransposeXZ.transposeXZ(getCLIJ2(), input, destination);
        if (doTimeTracing()) {recordMethodEnd("TransposeXZ");}
        return result;
    }


    // net.haesleinhuepf.clij2.plugins.TransposeYZ
    //----------------------------------------------------
    /**
     * Transpose Y and Z axes of an image.
     * 
     * Parameters
     * ----------
     * input : Image
     *     The input image.
     * destination : Image
     *     The output image where results are written into.
     * 
     */
    default boolean transposeYZ(ClearCLBuffer input, ClearCLBuffer destination) {
        if (doTimeTracing()) {recordMethodStart("TransposeYZ");}
        boolean result = TransposeYZ.transposeYZ(getCLIJ2(), input, destination);
        if (doTimeTracing()) {recordMethodEnd("TransposeYZ");}
        return result;
    }


    // net.haesleinhuepf.clij2.plugins.SetWhereXequalsY
    //----------------------------------------------------
    /**
     * Sets all pixel values a of a given image A to a constant value v in case its coordinates x == y. 
     * 
     * Otherwise the pixel is not overwritten.
     * If you want to initialize an identity transfrom matrix, set all pixels to 0 first.
     */
    default boolean setWhereXequalsY(ClearCLImageInterface arg1, double arg2) {
        if (doTimeTracing()) {recordMethodStart("SetWhereXequalsY");}
        boolean result = SetWhereXequalsY.setWhereXequalsY(getCLIJ2(), arg1, new Double (arg2).floatValue());
        if (doTimeTracing()) {recordMethodEnd("SetWhereXequalsY");}
        return result;
    }


    // net.haesleinhuepf.clij2.plugins.LaplaceDiamond
    //----------------------------------------------------
    /**
     * Applies the Laplace operator (Diamond neighborhood) to an image.
     */
    default boolean laplaceSphere(ClearCLBuffer input, ClearCLBuffer destination) {
        if (doTimeTracing()) {recordMethodStart("LaplaceDiamond");}
        boolean result = LaplaceDiamond.laplaceSphere(getCLIJ2(), input, destination);
        if (doTimeTracing()) {recordMethodEnd("LaplaceDiamond");}
        return result;
    }


    // net.haesleinhuepf.clij2.plugins.Image2DToResultsTable
    //----------------------------------------------------
    /**
     * Converts an image into a table.
     */
    @Deprecated
    default ResultsTable image2DToResultsTable(ClearCLBuffer arg1, ResultsTable arg2) {
        System.out.println("image2DToResultsTable is deprecated. Check the documentation for a replacement. https://clij.github.io/clij2-doccs/reference");
        if (doTimeTracing()) {recordMethodStart("Image2DToResultsTable");}
        ResultsTable result = Image2DToResultsTable.image2DToResultsTable(getCLIJ2(), arg1, arg2);
        if (doTimeTracing()) {recordMethodEnd("Image2DToResultsTable");}
        return result;
    }

    /**
     * Converts an image into a table.
     */
    @Deprecated
    default ResultsTable image2DToResultsTable(ClearCLImage arg1, ResultsTable arg2) {
        System.out.println("image2DToResultsTable is deprecated. Check the documentation for a replacement. https://clij.github.io/clij2-doccs/reference");
        if (doTimeTracing()) {recordMethodStart("Image2DToResultsTable");}
        ResultsTable result = Image2DToResultsTable.image2DToResultsTable(getCLIJ2(), arg1, arg2);
        if (doTimeTracing()) {recordMethodEnd("Image2DToResultsTable");}
        return result;
    }


    // net.haesleinhuepf.clij2.plugins.WriteValuesToPositions
    //----------------------------------------------------
    /**
     * Takes an image with three/four rows (2D: height = 3; 3D: height = 4): x, y [, z] and v and target image. 
     * 
     * The value v will be written at position x/y[/z] in the target image.
     */
    default boolean writeValuesToPositions(ClearCLBuffer positions_and_values, ClearCLBuffer destination) {
        if (doTimeTracing()) {recordMethodStart("WriteValuesToPositions");}
        boolean result = WriteValuesToPositions.writeValuesToPositions(getCLIJ2(), positions_and_values, destination);
        if (doTimeTracing()) {recordMethodEnd("WriteValuesToPositions");}
        return result;
    }


    // net.haesleinhuepf.clij2.plugins.GetSize
    //----------------------------------------------------
    /**
     * Reads out the size of an image [stack] and writes it to the results table in the columns 'Width', 'Height' and 'Depth'.
     * 
     * DEPRECATED: Thie method is deprecated. Use getDimensions instead.
     */
    @Deprecated
    default long[] getSize(ClearCLBuffer source) {
        System.out.println("getSize is deprecated. Check the documentation for a replacement. https://clij.github.io/clij2-doccs/reference");
        if (doTimeTracing()) {recordMethodStart("GetSize");}
        long[] result = GetSize.getSize(getCLIJ2(), source);
        if (doTimeTracing()) {recordMethodEnd("GetSize");}
        return result;
    }


    // net.haesleinhuepf.clij2.plugins.MultiplyMatrix
    //----------------------------------------------------
    /**
     * Multiplies two matrices with each other.
     */
    default boolean multiplyMatrix(ClearCLBuffer matrix1, ClearCLBuffer matrix2, ClearCLBuffer matrix_destination) {
        if (doTimeTracing()) {recordMethodStart("MultiplyMatrix");}
        boolean result = MultiplyMatrix.multiplyMatrix(getCLIJ2(), matrix1, matrix2, matrix_destination);
        if (doTimeTracing()) {recordMethodEnd("MultiplyMatrix");}
        return result;
    }


    // net.haesleinhuepf.clij2.plugins.MatrixEqual
    //----------------------------------------------------
    /**
     * Checks if all elements of a matrix are different by less than or equal to a given tolerance. 
     * 
     * The result will be put in the results table in column "MatrixEqual" as 1 if yes and 0 otherwise.
     */
    default boolean matrixEqual(ClearCLBuffer arg1, ClearCLBuffer arg2, double arg3) {
        if (doTimeTracing()) {recordMethodStart("MatrixEqual");}
        boolean result = MatrixEqual.matrixEqual(getCLIJ2(), arg1, arg2, new Double (arg3).floatValue());
        if (doTimeTracing()) {recordMethodEnd("MatrixEqual");}
        return result;
    }


    // net.haesleinhuepf.clij2.plugins.PowerImages
    //----------------------------------------------------
    /**
     * Calculates x to the power of y pixel wise of two images X and Y.
     */
    default boolean powerImages(ClearCLBuffer input, ClearCLBuffer exponent, ClearCLBuffer destination) {
        if (doTimeTracing()) {recordMethodStart("PowerImages");}
        boolean result = PowerImages.powerImages(getCLIJ2(), input, exponent, destination);
        if (doTimeTracing()) {recordMethodEnd("PowerImages");}
        return result;
    }


    // net.haesleinhuepf.clij2.plugins.Equal
    //----------------------------------------------------
    /**
     * Determines if two images A and B equal pixel wise.
     * 
     * <pre>f(a, b) = 1 if a == b; 0 otherwise.</pre>
     * 
     * Parameters
     * ----------
     * source1 : Image
     *     The first image to be compared with.
     * source2 : Image
     *     The second image to be compared with the first.
     * destination : Image
     *     The resulting binary image where pixels will be 1 only if source1 and source2 equal in the given pixel.
     * 
     */
    default boolean equal(ClearCLImageInterface source1, ClearCLImageInterface source2, ClearCLImageInterface destination) {
        if (doTimeTracing()) {recordMethodStart("Equal");}
        boolean result = Equal.equal(getCLIJ2(), source1, source2, destination);
        if (doTimeTracing()) {recordMethodEnd("Equal");}
        return result;
    }


    // net.haesleinhuepf.clij2.plugins.GreaterOrEqual
    //----------------------------------------------------
    /**
     * Determines if two images A and B greater or equal pixel wise. 
     * 
     * f(a, b) = 1 if a >= b; 0 otherwise. 
     */
    default boolean greaterOrEqual(ClearCLImageInterface source1, ClearCLImageInterface source2, ClearCLImageInterface destination) {
        if (doTimeTracing()) {recordMethodStart("GreaterOrEqual");}
        boolean result = GreaterOrEqual.greaterOrEqual(getCLIJ2(), source1, source2, destination);
        if (doTimeTracing()) {recordMethodEnd("GreaterOrEqual");}
        return result;
    }


    // net.haesleinhuepf.clij2.plugins.Greater
    //----------------------------------------------------
    /**
     * Determines if two images A and B greater pixel wise.
     * 
     * f(a, b) = 1 if a > b; 0 otherwise. 
     */
    default boolean greater(ClearCLImageInterface source1, ClearCLImageInterface source2, ClearCLImageInterface destination) {
        if (doTimeTracing()) {recordMethodStart("Greater");}
        boolean result = Greater.greater(getCLIJ2(), source1, source2, destination);
        if (doTimeTracing()) {recordMethodEnd("Greater");}
        return result;
    }


    // net.haesleinhuepf.clij2.plugins.Smaller
    //----------------------------------------------------
    /**
     * Determines if two images A and B smaller pixel wise.
     * 
     * f(a, b) = 1 if a < b; 0 otherwise. 
     */
    default boolean smaller(ClearCLBuffer source1, ClearCLBuffer source2, ClearCLBuffer destination) {
        if (doTimeTracing()) {recordMethodStart("Smaller");}
        boolean result = Smaller.smaller(getCLIJ2(), source1, source2, destination);
        if (doTimeTracing()) {recordMethodEnd("Smaller");}
        return result;
    }


    // net.haesleinhuepf.clij2.plugins.SmallerOrEqual
    //----------------------------------------------------
    /**
     * Determines if two images A and B smaller or equal pixel wise.
     * 
     * f(a, b) = 1 if a <= b; 0 otherwise. 
     */
    default boolean smallerOrEqual(ClearCLBuffer source1, ClearCLBuffer source2, ClearCLBuffer destination) {
        if (doTimeTracing()) {recordMethodStart("SmallerOrEqual");}
        boolean result = SmallerOrEqual.smallerOrEqual(getCLIJ2(), source1, source2, destination);
        if (doTimeTracing()) {recordMethodEnd("SmallerOrEqual");}
        return result;
    }


    // net.haesleinhuepf.clij2.plugins.NotEqual
    //----------------------------------------------------
    /**
     * Determines if two images A and B equal pixel wise.
     * 
     * f(a, b) = 1 if a != b; 0 otherwise.
     * 
     * Parameters
     * ----------
     * source1 : Image
     *     The first image to be compared with.
     * source2 : Image
     *     The second image to be compared with the first.
     * destination : Image
     *     The resulting binary image where pixels will be 1 only if source1 and source2 are not equal in the given pixel.
     * 
     */
    default boolean notEqual(ClearCLImageInterface source1, ClearCLImageInterface source2, ClearCLBuffer destination) {
        if (doTimeTracing()) {recordMethodStart("NotEqual");}
        boolean result = NotEqual.notEqual(getCLIJ2(), source1, source2, destination);
        if (doTimeTracing()) {recordMethodEnd("NotEqual");}
        return result;
    }


    // net.haesleinhuepf.clij2.plugins.EqualConstant
    //----------------------------------------------------
    /**
     * Determines if an image A and a constant b are equal.
     * 
     * <pre>f(a, b) = 1 if a == b; 0 otherwise.</pre>
     * 
     * Parameters
     * ----------
     * source : Image
     *     The image where every pixel is compared to the constant.
     * destination : Image
     *     The resulting binary image where pixels will be 1 only if source1 and source2 equal in the given pixel.
     * constant : float
     *     The constant where every pixel is compared to.
     * 
     */
    default boolean equalConstant(ClearCLImageInterface arg1, ClearCLImageInterface arg2, double arg3) {
        if (doTimeTracing()) {recordMethodStart("EqualConstant");}
        boolean result = EqualConstant.equalConstant(getCLIJ2(), arg1, arg2, new Double (arg3).floatValue());
        if (doTimeTracing()) {recordMethodEnd("EqualConstant");}
        return result;
    }


    // net.haesleinhuepf.clij2.plugins.GreaterOrEqualConstant
    //----------------------------------------------------
    /**
     * Determines if two images A and B greater or equal pixel wise. 
     * 
     * f(a, b) = 1 if a >= b; 0 otherwise. 
     */
    default boolean greaterOrEqualConstant(ClearCLImageInterface arg1, ClearCLImageInterface arg2, double arg3) {
        if (doTimeTracing()) {recordMethodStart("GreaterOrEqualConstant");}
        boolean result = GreaterOrEqualConstant.greaterOrEqualConstant(getCLIJ2(), arg1, arg2, new Double (arg3).floatValue());
        if (doTimeTracing()) {recordMethodEnd("GreaterOrEqualConstant");}
        return result;
    }


    // net.haesleinhuepf.clij2.plugins.GreaterConstant
    //----------------------------------------------------
    /**
     * Determines if two images A and B greater pixel wise. 
     * 
     * f(a, b) = 1 if a > b; 0 otherwise. 
     */
    default boolean greaterConstant(ClearCLImageInterface arg1, ClearCLImageInterface arg2, double arg3) {
        if (doTimeTracing()) {recordMethodStart("GreaterConstant");}
        boolean result = GreaterConstant.greaterConstant(getCLIJ2(), arg1, arg2, new Double (arg3).floatValue());
        if (doTimeTracing()) {recordMethodEnd("GreaterConstant");}
        return result;
    }


    // net.haesleinhuepf.clij2.plugins.SmallerConstant
    //----------------------------------------------------
    /**
     * Determines if two images A and B smaller pixel wise.
     * 
     * f(a, b) = 1 if a < b; 0 otherwise. 
     */
    default boolean smallerConstant(ClearCLBuffer arg1, ClearCLBuffer arg2, double arg3) {
        if (doTimeTracing()) {recordMethodStart("SmallerConstant");}
        boolean result = SmallerConstant.smallerConstant(getCLIJ2(), arg1, arg2, new Double (arg3).floatValue());
        if (doTimeTracing()) {recordMethodEnd("SmallerConstant");}
        return result;
    }


    // net.haesleinhuepf.clij2.plugins.SmallerOrEqualConstant
    //----------------------------------------------------
    /**
     * Determines if two images A and B smaller or equal pixel wise.
     * 
     * f(a, b) = 1 if a <= b; 0 otherwise. 
     */
    default boolean smallerOrEqualConstant(ClearCLBuffer arg1, ClearCLBuffer arg2, double arg3) {
        if (doTimeTracing()) {recordMethodStart("SmallerOrEqualConstant");}
        boolean result = SmallerOrEqualConstant.smallerOrEqualConstant(getCLIJ2(), arg1, arg2, new Double (arg3).floatValue());
        if (doTimeTracing()) {recordMethodEnd("SmallerOrEqualConstant");}
        return result;
    }


    // net.haesleinhuepf.clij2.plugins.NotEqualConstant
    //----------------------------------------------------
    /**
     * Determines if two images A and B equal pixel wise.
     * 
     * f(a, b) = 1 if a != b; 0 otherwise.Parameters
     * ----------
     * source : Image
     *     The image where every pixel is compared to the constant.
     * destination : Image
     *     The resulting binary image where pixels will be 1 only if source1 and source2 equal in the given pixel.
     * constant : float
     *     The constant where every pixel is compared to.
     * 
     */
    default boolean notEqualConstant(ClearCLBuffer arg1, ClearCLBuffer arg2, double arg3) {
        if (doTimeTracing()) {recordMethodStart("NotEqualConstant");}
        boolean result = NotEqualConstant.notEqualConstant(getCLIJ2(), arg1, arg2, new Double (arg3).floatValue());
        if (doTimeTracing()) {recordMethodEnd("NotEqualConstant");}
        return result;
    }


    // net.haesleinhuepf.clij2.plugins.DrawBox
    //----------------------------------------------------
    /**
     * Draws a box at a given start point with given size. 
     * All pixels other than in the box are untouched. Consider using `set(buffer, 0);` in advance.
     */
    default boolean drawBox(ClearCLImageInterface arg1, double arg2, double arg3, double arg4, double arg5) {
        if (doTimeTracing()) {recordMethodStart("DrawBox");}
        boolean result = DrawBox.drawBox(getCLIJ2(), arg1, new Double (arg2).floatValue(), new Double (arg3).floatValue(), new Double (arg4).floatValue(), new Double (arg5).floatValue());
        if (doTimeTracing()) {recordMethodEnd("DrawBox");}
        return result;
    }

    /**
     * Draws a box at a given start point with given size. 
     * All pixels other than in the box are untouched. Consider using `set(buffer, 0);` in advance.
     */
    default boolean drawBox(ClearCLImageInterface arg1, double arg2, double arg3, double arg4, double arg5, double arg6, double arg7) {
        if (doTimeTracing()) {recordMethodStart("DrawBox");}
        boolean result = DrawBox.drawBox(getCLIJ2(), arg1, new Double (arg2).floatValue(), new Double (arg3).floatValue(), new Double (arg4).floatValue(), new Double (arg5).floatValue(), new Double (arg6).floatValue(), new Double (arg7).floatValue());
        if (doTimeTracing()) {recordMethodEnd("DrawBox");}
        return result;
    }

    /**
     * Draws a box at a given start point with given size. 
     * All pixels other than in the box are untouched. Consider using `set(buffer, 0);` in advance.
     */
    default boolean drawBox(ClearCLImageInterface arg1, double arg2, double arg3, double arg4, double arg5, double arg6, double arg7, double arg8) {
        if (doTimeTracing()) {recordMethodStart("DrawBox");}
        boolean result = DrawBox.drawBox(getCLIJ2(), arg1, new Double (arg2).floatValue(), new Double (arg3).floatValue(), new Double (arg4).floatValue(), new Double (arg5).floatValue(), new Double (arg6).floatValue(), new Double (arg7).floatValue(), new Double (arg8).floatValue());
        if (doTimeTracing()) {recordMethodEnd("DrawBox");}
        return result;
    }


    // net.haesleinhuepf.clij2.plugins.DrawLine
    //----------------------------------------------------
    /**
     * Draws a line between two points with a given thickness. 
     * 
     * All pixels other than on the line are untouched. Consider using `set(buffer, 0);` in advance.
     */
    default boolean drawLine(ClearCLImageInterface arg1, double arg2, double arg3, double arg4, double arg5, double arg6, double arg7, double arg8) {
        if (doTimeTracing()) {recordMethodStart("DrawLine");}
        boolean result = DrawLine.drawLine(getCLIJ2(), arg1, new Double (arg2).floatValue(), new Double (arg3).floatValue(), new Double (arg4).floatValue(), new Double (arg5).floatValue(), new Double (arg6).floatValue(), new Double (arg7).floatValue(), new Double (arg8).floatValue());
        if (doTimeTracing()) {recordMethodEnd("DrawLine");}
        return result;
    }

    /**
     * Draws a line between two points with a given thickness. 
     * 
     * All pixels other than on the line are untouched. Consider using `set(buffer, 0);` in advance.
     */
    default boolean drawLine(ClearCLImageInterface arg1, double arg2, double arg3, double arg4, double arg5, double arg6, double arg7, double arg8, double arg9) {
        if (doTimeTracing()) {recordMethodStart("DrawLine");}
        boolean result = DrawLine.drawLine(getCLIJ2(), arg1, new Double (arg2).floatValue(), new Double (arg3).floatValue(), new Double (arg4).floatValue(), new Double (arg5).floatValue(), new Double (arg6).floatValue(), new Double (arg7).floatValue(), new Double (arg8).floatValue(), new Double (arg9).floatValue());
        if (doTimeTracing()) {recordMethodEnd("DrawLine");}
        return result;
    }


    // net.haesleinhuepf.clij2.plugins.DrawSphere
    //----------------------------------------------------
    /**
     * Draws a sphere around a given point with given radii in x, y and z (if 3D). 
     * 
     *  All pixels other than in the sphere are untouched. Consider using `set(buffer, 0);` in advance.
     */
    default boolean drawSphere(ClearCLImageInterface arg1, double arg2, double arg3, double arg4, double arg5) {
        if (doTimeTracing()) {recordMethodStart("DrawSphere");}
        boolean result = DrawSphere.drawSphere(getCLIJ2(), arg1, new Double (arg2).floatValue(), new Double (arg3).floatValue(), new Double (arg4).floatValue(), new Double (arg5).floatValue());
        if (doTimeTracing()) {recordMethodEnd("DrawSphere");}
        return result;
    }

    /**
     * Draws a sphere around a given point with given radii in x, y and z (if 3D). 
     * 
     *  All pixels other than in the sphere are untouched. Consider using `set(buffer, 0);` in advance.
     */
    default boolean drawSphere(ClearCLImageInterface arg1, double arg2, double arg3, double arg4, double arg5, double arg6) {
        if (doTimeTracing()) {recordMethodStart("DrawSphere");}
        boolean result = DrawSphere.drawSphere(getCLIJ2(), arg1, new Double (arg2).floatValue(), new Double (arg3).floatValue(), new Double (arg4).floatValue(), new Double (arg5).floatValue(), new Double (arg6).floatValue());
        if (doTimeTracing()) {recordMethodEnd("DrawSphere");}
        return result;
    }

    /**
     * Draws a sphere around a given point with given radii in x, y and z (if 3D). 
     * 
     *  All pixels other than in the sphere are untouched. Consider using `set(buffer, 0);` in advance.
     */
    default boolean drawSphere(ClearCLImageInterface arg1, double arg2, double arg3, double arg4, double arg5, double arg6, double arg7) {
        if (doTimeTracing()) {recordMethodStart("DrawSphere");}
        boolean result = DrawSphere.drawSphere(getCLIJ2(), arg1, new Double (arg2).floatValue(), new Double (arg3).floatValue(), new Double (arg4).floatValue(), new Double (arg5).floatValue(), new Double (arg6).floatValue(), new Double (arg7).floatValue());
        if (doTimeTracing()) {recordMethodEnd("DrawSphere");}
        return result;
    }

    /**
     * Draws a sphere around a given point with given radii in x, y and z (if 3D). 
     * 
     *  All pixels other than in the sphere are untouched. Consider using `set(buffer, 0);` in advance.
     */
    default boolean drawSphere(ClearCLImageInterface arg1, double arg2, double arg3, double arg4, double arg5, double arg6, double arg7, double arg8) {
        if (doTimeTracing()) {recordMethodStart("DrawSphere");}
        boolean result = DrawSphere.drawSphere(getCLIJ2(), arg1, new Double (arg2).floatValue(), new Double (arg3).floatValue(), new Double (arg4).floatValue(), new Double (arg5).floatValue(), new Double (arg6).floatValue(), new Double (arg7).floatValue(), new Double (arg8).floatValue());
        if (doTimeTracing()) {recordMethodEnd("DrawSphere");}
        return result;
    }


    // net.haesleinhuepf.clij2.plugins.ReplaceIntensity
    //----------------------------------------------------
    /**
     * Replaces a specific intensity in an image with a given new value.
     */
    default boolean replaceIntensity(ClearCLImageInterface arg1, ClearCLImageInterface arg2, double arg3, double arg4) {
        if (doTimeTracing()) {recordMethodStart("ReplaceIntensity");}
        boolean result = ReplaceIntensity.replaceIntensity(getCLIJ2(), arg1, arg2, new Double (arg3).floatValue(), new Double (arg4).floatValue());
        if (doTimeTracing()) {recordMethodEnd("ReplaceIntensity");}
        return result;
    }


    // net.haesleinhuepf.clij2.plugins.BoundingBox
    //----------------------------------------------------
    /**
     * Determines the bounding box of all non-zero pixels in a binary image. 
     * 
     * If called from macro, the positions will be stored in a new row of ImageJs Results table in the columns 'BoundingBoxX', 'BoundingBoxY', 'BoundingBoxZ', 'BoundingBoxWidth', 'BoundingBoxHeight' 'BoundingBoxDepth'.In case of 2D images Z and depth will be zero.
     */
    default double[] boundingBox(ClearCLBuffer source) {
        if (doTimeTracing()) {recordMethodStart("BoundingBox");}
        double[] result = BoundingBox.boundingBox(getCLIJ2(), source);
        if (doTimeTracing()) {recordMethodEnd("BoundingBox");}
        return result;
    }


    // net.haesleinhuepf.clij2.plugins.MinimumOfMaskedPixels
    //----------------------------------------------------
    /**
     * Determines the minimum intensity in a masked image. 
     * 
     * But only in pixels which have non-zero values in another mask image.
     */
    default double minimumOfMaskedPixels(ClearCLBuffer source, ClearCLBuffer mask) {
        if (doTimeTracing()) {recordMethodStart("MinimumOfMaskedPixels");}
        double result = MinimumOfMaskedPixels.minimumOfMaskedPixels(getCLIJ2(), source, mask);
        if (doTimeTracing()) {recordMethodEnd("MinimumOfMaskedPixels");}
        return result;
    }


    // net.haesleinhuepf.clij2.plugins.MaximumOfMaskedPixels
    //----------------------------------------------------
    /**
     * Determines the maximum intensity in an image, but only in pixels which have non-zero values in another mask image.
     * 
     * Parameters
     * ----------
     * source : Image
     *     The image of which the minimum of all pixels or voxels where mask=1 will be determined.
     * mask : Image
     *     A binary image marking all pixels with 1 which should be taken into accout.
     * 
     */
    default double maximumOfMaskedPixels(ClearCLBuffer source, ClearCLBuffer mask) {
        if (doTimeTracing()) {recordMethodStart("MaximumOfMaskedPixels");}
        double result = MaximumOfMaskedPixels.maximumOfMaskedPixels(getCLIJ2(), source, mask);
        if (doTimeTracing()) {recordMethodEnd("MaximumOfMaskedPixels");}
        return result;
    }


    // net.haesleinhuepf.clij2.plugins.MeanOfMaskedPixels
    //----------------------------------------------------
    /**
     * Determines the mean intensity in a masked image. 
     * 
     * Only in pixels which have non-zero values in another binary mask image.
     */
    default double meanOfMaskedPixels(ClearCLBuffer source, ClearCLBuffer mask) {
        if (doTimeTracing()) {recordMethodStart("MeanOfMaskedPixels");}
        double result = MeanOfMaskedPixels.meanOfMaskedPixels(getCLIJ2(), source, mask);
        if (doTimeTracing()) {recordMethodEnd("MeanOfMaskedPixels");}
        return result;
    }


    // net.haesleinhuepf.clij2.plugins.LabelToMask
    //----------------------------------------------------
    /**
     * Masks a single label in a label map. 
     * 
     * Sets all pixels in the target image to 1, where the given label index was present in the label map. Other pixels are set to 0.
     */
    default boolean labelToMask(ClearCLBuffer arg1, ClearCLBuffer arg2, double arg3) {
        if (doTimeTracing()) {recordMethodStart("LabelToMask");}
        boolean result = LabelToMask.labelToMask(getCLIJ2(), arg1, arg2, new Double (arg3).floatValue());
        if (doTimeTracing()) {recordMethodEnd("LabelToMask");}
        return result;
    }


    // net.haesleinhuepf.clij2.plugins.NClosestPoints
    //----------------------------------------------------
    /**
     * Determine the n point indices with shortest distance for all points in a distance matrix. 
     * 
     * This corresponds to the n row indices with minimum values for each column of the distance matrix.
     */
    default boolean nClosestPoints(ClearCLBuffer arg1, ClearCLBuffer arg2) {
        if (doTimeTracing()) {recordMethodStart("NClosestPoints");}
        boolean result = NClosestPoints.nClosestPoints(getCLIJ2(), arg1, arg2);
        if (doTimeTracing()) {recordMethodEnd("NClosestPoints");}
        return result;
    }


    // net.haesleinhuepf.clij2.plugins.StatisticsOfLabelledPixels
    //----------------------------------------------------
    /**
     * Determines bounding box, area (in pixels/voxels), min, max and mean intensity 
     *  of labelled objects in a label map and corresponding pixels in the original image. 
     * 
     * Instead of a label map, you can also use a binary image as a binary image is a label map with just one label.
     * 
     * This method is executed on the CPU and not on the GPU/OpenCL device.
     */
    default double[] statisticsOfLabelledPixels(ClearCLBuffer arg1, ClearCLBuffer arg2, double arg3) {
        if (doTimeTracing()) {recordMethodStart("StatisticsOfLabelledPixels");}
        double[] result = StatisticsOfLabelledPixels.statisticsOfLabelledPixels(getCLIJ2(), arg1, arg2, new Double (arg3).intValue());
        if (doTimeTracing()) {recordMethodEnd("StatisticsOfLabelledPixels");}
        return result;
    }

    /**
     * Determines bounding box, area (in pixels/voxels), min, max and mean intensity 
     *  of labelled objects in a label map and corresponding pixels in the original image. 
     * 
     * Instead of a label map, you can also use a binary image as a binary image is a label map with just one label.
     * 
     * This method is executed on the CPU and not on the GPU/OpenCL device.
     */
    default double[][] statisticsOfLabelledPixels(ClearCLBuffer input, ClearCLBuffer labelmap) {
        if (doTimeTracing()) {recordMethodStart("StatisticsOfLabelledPixels");}
        double[][] result = StatisticsOfLabelledPixels.statisticsOfLabelledPixels(getCLIJ2(), input, labelmap);
        if (doTimeTracing()) {recordMethodEnd("StatisticsOfLabelledPixels");}
        return result;
    }

    /**
     * Determines bounding box, area (in pixels/voxels), min, max and mean intensity 
     *  of labelled objects in a label map and corresponding pixels in the original image. 
     * 
     * Instead of a label map, you can also use a binary image as a binary image is a label map with just one label.
     * 
     * This method is executed on the CPU and not on the GPU/OpenCL device.
     */
    default double[][] statisticsOfLabelledPixels(ClearCLBuffer arg1, ClearCLBuffer arg2, double arg3, double arg4) {
        if (doTimeTracing()) {recordMethodStart("StatisticsOfLabelledPixels");}
        double[][] result = StatisticsOfLabelledPixels.statisticsOfLabelledPixels(getCLIJ2(), arg1, arg2, new Double (arg3).intValue(), new Double (arg4).intValue());
        if (doTimeTracing()) {recordMethodEnd("StatisticsOfLabelledPixels");}
        return result;
    }

    /**
     * 
     */
    default double[][] statisticsOfLabelledPixels_single_threaded(ClearCLBuffer arg1, ClearCLBuffer arg2, double arg3, double arg4) {
        if (doTimeTracing()) {recordMethodStart("StatisticsOfLabelledPixels");}
        double[][] result = StatisticsOfLabelledPixels.statisticsOfLabelledPixels_single_threaded(getCLIJ2(), arg1, arg2, new Double (arg3).intValue(), new Double (arg4).intValue());
        if (doTimeTracing()) {recordMethodEnd("StatisticsOfLabelledPixels");}
        return result;
    }

    /**
     * Determines bounding box, area (in pixels/voxels), min, max and mean intensity 
     *  of labelled objects in a label map and corresponding pixels in the original image. 
     * 
     * Instead of a label map, you can also use a binary image as a binary image is a label map with just one label.
     * 
     * This method is executed on the CPU and not on the GPU/OpenCL device.
     */
    default ResultsTable statisticsOfLabelledPixels(ClearCLBuffer arg1, ClearCLBuffer arg2, ResultsTable arg3) {
        if (doTimeTracing()) {recordMethodStart("StatisticsOfLabelledPixels");}
        ResultsTable result = StatisticsOfLabelledPixels.statisticsOfLabelledPixels(getCLIJ2(), arg1, arg2, arg3);
        if (doTimeTracing()) {recordMethodEnd("StatisticsOfLabelledPixels");}
        return result;
    }


    // net.haesleinhuepf.clij2.plugins.VarianceOfAllPixels
    //----------------------------------------------------
    /**
     * Determines the variance of all pixels in an image. 
     * 
     * The value will be stored in a new row of ImageJs
     * Results table in the column 'Variance'.
     */
    default double varianceOfAllPixels(ClearCLBuffer source) {
        if (doTimeTracing()) {recordMethodStart("VarianceOfAllPixels");}
        double result = VarianceOfAllPixels.varianceOfAllPixels(getCLIJ2(), source);
        if (doTimeTracing()) {recordMethodEnd("VarianceOfAllPixels");}
        return result;
    }

    /**
     * Determines the variance of all pixels in an image. 
     * 
     * The value will be stored in a new row of ImageJs
     * Results table in the column 'Variance'.
     */
    default double varianceOfAllPixels(ClearCLImageInterface arg1, double arg2) {
        if (doTimeTracing()) {recordMethodStart("VarianceOfAllPixels");}
        double result = VarianceOfAllPixels.varianceOfAllPixels(getCLIJ2(), arg1, new Double (arg2).floatValue());
        if (doTimeTracing()) {recordMethodEnd("VarianceOfAllPixels");}
        return result;
    }


    // net.haesleinhuepf.clij2.plugins.StandardDeviationOfAllPixels
    //----------------------------------------------------
    /**
     * Determines the standard deviation of all pixels in an image. 
     * 
     * The value will be stored in a new row of ImageJs
     * Results table in the column 'Standard_deviation'.
     */
    default double standardDeviationOfAllPixels(ClearCLImageInterface source) {
        if (doTimeTracing()) {recordMethodStart("StandardDeviationOfAllPixels");}
        double result = StandardDeviationOfAllPixels.standardDeviationOfAllPixels(getCLIJ2(), source);
        if (doTimeTracing()) {recordMethodEnd("StandardDeviationOfAllPixels");}
        return result;
    }

    /**
     * Determines the standard deviation of all pixels in an image. 
     * 
     * The value will be stored in a new row of ImageJs
     * Results table in the column 'Standard_deviation'.
     */
    default double standardDeviationOfAllPixels(ClearCLImageInterface arg1, double arg2) {
        if (doTimeTracing()) {recordMethodStart("StandardDeviationOfAllPixels");}
        double result = StandardDeviationOfAllPixels.standardDeviationOfAllPixels(getCLIJ2(), arg1, new Double (arg2).floatValue());
        if (doTimeTracing()) {recordMethodEnd("StandardDeviationOfAllPixels");}
        return result;
    }


    // net.haesleinhuepf.clij2.plugins.VarianceOfMaskedPixels
    //----------------------------------------------------
    /**
     * Determines the variance in an image, but only in pixels which have non-zero values in another binary mask image. 
     * 
     * The result is put in the results table as new column named 'Masked_variance'.
     */
    default double varianceOfMaskedPixels(ClearCLBuffer source, ClearCLBuffer mask) {
        if (doTimeTracing()) {recordMethodStart("VarianceOfMaskedPixels");}
        double result = VarianceOfMaskedPixels.varianceOfMaskedPixels(getCLIJ2(), source, mask);
        if (doTimeTracing()) {recordMethodEnd("VarianceOfMaskedPixels");}
        return result;
    }

    /**
     * Determines the variance in an image, but only in pixels which have non-zero values in another binary mask image. 
     * 
     * The result is put in the results table as new column named 'Masked_variance'.
     */
    default double varianceOfMaskedPixels(ClearCLBuffer arg1, ClearCLBuffer arg2, double arg3) {
        if (doTimeTracing()) {recordMethodStart("VarianceOfMaskedPixels");}
        double result = VarianceOfMaskedPixels.varianceOfMaskedPixels(getCLIJ2(), arg1, arg2, new Double (arg3).floatValue());
        if (doTimeTracing()) {recordMethodEnd("VarianceOfMaskedPixels");}
        return result;
    }


    // net.haesleinhuepf.clij2.plugins.StandardDeviationOfMaskedPixels
    //----------------------------------------------------
    /**
     * Determines the standard deviation of all pixels in an image which have non-zero value in a corresponding mask image. 
     * 
     * The value will be stored in a new row of ImageJs
     * Results table in the column 'Masked_standard_deviation'.
     */
    default double standardDeviationOfMaskedPixels(ClearCLBuffer source, ClearCLBuffer mask) {
        if (doTimeTracing()) {recordMethodStart("StandardDeviationOfMaskedPixels");}
        double result = StandardDeviationOfMaskedPixels.standardDeviationOfMaskedPixels(getCLIJ2(), source, mask);
        if (doTimeTracing()) {recordMethodEnd("StandardDeviationOfMaskedPixels");}
        return result;
    }

    /**
     * Determines the standard deviation of all pixels in an image which have non-zero value in a corresponding mask image. 
     * 
     * The value will be stored in a new row of ImageJs
     * Results table in the column 'Masked_standard_deviation'.
     */
    default double standardDeviationOfMaskedPixels(ClearCLBuffer arg1, ClearCLBuffer arg2, double arg3) {
        if (doTimeTracing()) {recordMethodStart("StandardDeviationOfMaskedPixels");}
        double result = StandardDeviationOfMaskedPixels.standardDeviationOfMaskedPixels(getCLIJ2(), arg1, arg2, new Double (arg3).floatValue());
        if (doTimeTracing()) {recordMethodEnd("StandardDeviationOfMaskedPixels");}
        return result;
    }


    // net.haesleinhuepf.clij2.plugins.ExcludeLabelsOnEdges
    //----------------------------------------------------
    /**
     * Removes all labels from a label map which touch the edges of the image (in X, Y and Z if the image is 3D). 
     * 
     * Remaining label elements are renumbered afterwards.
     */
    default boolean excludeLabelsOnEdges(ClearCLBuffer label_map_input, ClearCLBuffer label_map_destination) {
        if (doTimeTracing()) {recordMethodStart("ExcludeLabelsOnEdges");}
        boolean result = ExcludeLabelsOnEdges.excludeLabelsOnEdges(getCLIJ2(), label_map_input, label_map_destination);
        if (doTimeTracing()) {recordMethodEnd("ExcludeLabelsOnEdges");}
        return result;
    }


    // net.haesleinhuepf.clij2.plugins.BinarySubtract
    //----------------------------------------------------
    /**
     * Subtracts one binary image from another.
     * 
     * Parameters
     * ----------
     * minuend : Image
     *     The first binary input image to be processed.
     * suubtrahend : Image
     *     The second binary input image to be subtracted from the first.
     * destination : Image
     *     The output image where results are written into.
     * 
     */
    default boolean binarySubtract(ClearCLImageInterface minuend, ClearCLImageInterface subtrahend, ClearCLImageInterface destination) {
        if (doTimeTracing()) {recordMethodStart("BinarySubtract");}
        boolean result = BinarySubtract.binarySubtract(getCLIJ2(), minuend, subtrahend, destination);
        if (doTimeTracing()) {recordMethodEnd("BinarySubtract");}
        return result;
    }


    // net.haesleinhuepf.clij2.plugins.BinaryEdgeDetection
    //----------------------------------------------------
    /**
     * Determines pixels/voxels which are on the surface of binary objects and sets only them to 1 in the 
     * destination image. All other pixels are set to 0.
     * 
     * Parameters
     * ----------
     * source : Image
     *     The binary input image where edges will be searched.
     * destination : Image
     *     The output image where edge pixels will be 1.
     * 
     */
    default boolean binaryEdgeDetection(ClearCLImageInterface source, ClearCLImageInterface destination) {
        if (doTimeTracing()) {recordMethodStart("BinaryEdgeDetection");}
        boolean result = BinaryEdgeDetection.binaryEdgeDetection(getCLIJ2(), source, destination);
        if (doTimeTracing()) {recordMethodEnd("BinaryEdgeDetection");}
        return result;
    }


    // net.haesleinhuepf.clij2.plugins.DistanceMap
    //----------------------------------------------------
    /**
     * Generates a distance map from a binary image. 
     * 
     * Pixels with non-zero value in the binary image are set to a number representing the distance to the closest zero-value pixel.
     * 
     * Note: This is not a distance matrix. See generateDistanceMatrix for details.
     */
    default boolean distanceMap(ClearCLBuffer source, ClearCLBuffer destination) {
        if (doTimeTracing()) {recordMethodStart("DistanceMap");}
        boolean result = DistanceMap.distanceMap(getCLIJ2(), source, destination);
        if (doTimeTracing()) {recordMethodEnd("DistanceMap");}
        return result;
    }


    // net.haesleinhuepf.clij2.plugins.PullAsROI
    //----------------------------------------------------
    /**
     * Pulls a binary image from the GPU memory and puts it on the currently active ImageJ window as region of interest.
     */
    default Roi pullAsROI(ClearCLBuffer binary_input) {
        if (doTimeTracing()) {recordMethodStart("PullAsROI");}
        Roi result = PullAsROI.pullAsROI(getCLIJ2(), binary_input);
        if (doTimeTracing()) {recordMethodEnd("PullAsROI");}
        return result;
    }


    // net.haesleinhuepf.clij2.plugins.PullLabelsToROIManager
    //----------------------------------------------------
    /**
     * Pulls all labels in a label map as ROIs to the ROI manager.
     */
    default boolean pullLabelsToROIManager(ClearCLBuffer labelmap_input) {
        if (doTimeTracing()) {recordMethodStart("PullLabelsToROIManager");}
        boolean result = PullLabelsToROIManager.pullLabelsToROIManager(getCLIJ2(), labelmap_input);
        if (doTimeTracing()) {recordMethodEnd("PullLabelsToROIManager");}
        return result;
    }

    /**
     * Pulls all labels in a label map as ROIs to the ROI manager.
     */
    default boolean pullLabelsToROIManager(ClearCLBuffer arg1, RoiManager arg2) {
        if (doTimeTracing()) {recordMethodStart("PullLabelsToROIManager");}
        boolean result = PullLabelsToROIManager.pullLabelsToROIManager(getCLIJ2(), arg1, arg2);
        if (doTimeTracing()) {recordMethodEnd("PullLabelsToROIManager");}
        return result;
    }


    // net.haesleinhuepf.clij2.plugins.NonzeroMaximumDiamond
    //----------------------------------------------------
    /**
     * Apply a maximum filter (diamond shape) to the input image. 
     * 
     * The radius is fixed to 1 and pixels with value 0 are ignored.
     * Note: Pixels with 0 value in the input image will not be overwritten in the output image.
     * Thus, the result image should be initialized by copying the original image in advance.
     */
    default boolean nonzeroMaximumDiamond(ClearCLImageInterface input, ClearCLImageInterface destination) {
        if (doTimeTracing()) {recordMethodStart("NonzeroMaximumDiamond");}
        boolean result = NonzeroMaximumDiamond.nonzeroMaximumDiamond(getCLIJ2(), input, destination);
        if (doTimeTracing()) {recordMethodEnd("NonzeroMaximumDiamond");}
        return result;
    }

    /**
     * Apply a maximum filter (diamond shape) to the input image. 
     * 
     * The radius is fixed to 1 and pixels with value 0 are ignored.
     * Note: Pixels with 0 value in the input image will not be overwritten in the output image.
     * Thus, the result image should be initialized by copying the original image in advance.
     */
    default boolean nonzeroMaximumDiamond(ClearCLImageInterface arg1, ClearCLImageInterface arg2, ClearCLImageInterface arg3) {
        if (doTimeTracing()) {recordMethodStart("NonzeroMaximumDiamond");}
        boolean result = NonzeroMaximumDiamond.nonzeroMaximumDiamond(getCLIJ2(), arg1, arg2, arg3);
        if (doTimeTracing()) {recordMethodEnd("NonzeroMaximumDiamond");}
        return result;
    }

    /**
     * Apply a maximum filter (diamond shape) to the input image. 
     * 
     * The radius is fixed to 1 and pixels with value 0 are ignored.
     * Note: Pixels with 0 value in the input image will not be overwritten in the output image.
     * Thus, the result image should be initialized by copying the original image in advance.
     */
    default ClearCLKernel nonzeroMaximumDiamond(ClearCLImageInterface arg1, ClearCLImageInterface arg2, ClearCLImageInterface arg3, ClearCLKernel arg4) {
        if (doTimeTracing()) {recordMethodStart("NonzeroMaximumDiamond");}
        ClearCLKernel result = NonzeroMaximumDiamond.nonzeroMaximumDiamond(getCLIJ2(), arg1, arg2, arg3, arg4);
        if (doTimeTracing()) {recordMethodEnd("NonzeroMaximumDiamond");}
        return result;
    }


    // net.haesleinhuepf.clij2.plugins.OnlyzeroOverwriteMaximumDiamond
    //----------------------------------------------------
    /**
     * Apply a local maximum filter to an image which only overwrites pixels with value 0.
     */
    default boolean onlyzeroOverwriteMaximumDiamond(ClearCLImageInterface input, ClearCLImageInterface destination) {
        if (doTimeTracing()) {recordMethodStart("OnlyzeroOverwriteMaximumDiamond");}
        boolean result = OnlyzeroOverwriteMaximumDiamond.onlyzeroOverwriteMaximumDiamond(getCLIJ2(), input, destination);
        if (doTimeTracing()) {recordMethodEnd("OnlyzeroOverwriteMaximumDiamond");}
        return result;
    }

    /**
     * Apply a local maximum filter to an image which only overwrites pixels with value 0.
     */
    default boolean onlyzeroOverwriteMaximumDiamond(ClearCLImageInterface arg1, ClearCLImageInterface arg2, ClearCLImageInterface arg3) {
        if (doTimeTracing()) {recordMethodStart("OnlyzeroOverwriteMaximumDiamond");}
        boolean result = OnlyzeroOverwriteMaximumDiamond.onlyzeroOverwriteMaximumDiamond(getCLIJ2(), arg1, arg2, arg3);
        if (doTimeTracing()) {recordMethodEnd("OnlyzeroOverwriteMaximumDiamond");}
        return result;
    }

    /**
     * Apply a local maximum filter to an image which only overwrites pixels with value 0.
     */
    default ClearCLKernel onlyzeroOverwriteMaximumDiamond(ClearCLImageInterface arg1, ClearCLImageInterface arg2, ClearCLImageInterface arg3, ClearCLKernel arg4) {
        if (doTimeTracing()) {recordMethodStart("OnlyzeroOverwriteMaximumDiamond");}
        ClearCLKernel result = OnlyzeroOverwriteMaximumDiamond.onlyzeroOverwriteMaximumDiamond(getCLIJ2(), arg1, arg2, arg3, arg4);
        if (doTimeTracing()) {recordMethodEnd("OnlyzeroOverwriteMaximumDiamond");}
        return result;
    }


    // net.haesleinhuepf.clij2.plugins.OnlyzeroOverwriteMaximumBox
    //----------------------------------------------------
    /**
     * Apply a local maximum filter to an image which only overwrites pixels with value 0.
     */
    default boolean onlyzeroOverwriteMaximumBox(ClearCLImageInterface input, ClearCLImageInterface destination) {
        if (doTimeTracing()) {recordMethodStart("OnlyzeroOverwriteMaximumBox");}
        boolean result = OnlyzeroOverwriteMaximumBox.onlyzeroOverwriteMaximumBox(getCLIJ2(), input, destination);
        if (doTimeTracing()) {recordMethodEnd("OnlyzeroOverwriteMaximumBox");}
        return result;
    }

    /**
     * Apply a local maximum filter to an image which only overwrites pixels with value 0.
     */
    default boolean onlyzeroOverwriteMaximumBox(ClearCLImageInterface arg1, ClearCLImageInterface arg2, ClearCLImageInterface arg3) {
        if (doTimeTracing()) {recordMethodStart("OnlyzeroOverwriteMaximumBox");}
        boolean result = OnlyzeroOverwriteMaximumBox.onlyzeroOverwriteMaximumBox(getCLIJ2(), arg1, arg2, arg3);
        if (doTimeTracing()) {recordMethodEnd("OnlyzeroOverwriteMaximumBox");}
        return result;
    }

    /**
     * Apply a local maximum filter to an image which only overwrites pixels with value 0.
     */
    default ClearCLKernel onlyzeroOverwriteMaximumBox(ClearCLImageInterface arg1, ClearCLImageInterface arg2, ClearCLImageInterface arg3, ClearCLKernel arg4) {
        if (doTimeTracing()) {recordMethodStart("OnlyzeroOverwriteMaximumBox");}
        ClearCLKernel result = OnlyzeroOverwriteMaximumBox.onlyzeroOverwriteMaximumBox(getCLIJ2(), arg1, arg2, arg3, arg4);
        if (doTimeTracing()) {recordMethodEnd("OnlyzeroOverwriteMaximumBox");}
        return result;
    }


    // net.haesleinhuepf.clij2.plugins.GenerateTouchMatrix
    //----------------------------------------------------
    /**
     * Takes a labelmap with n labels and generates a (n+1)*(n+1) matrix where all pixels are set to 0 exept those where labels are touching. 
     * 
     * Only half of the matrix is filled (with x < y). For example, if labels 3 and 4 are touching then the pixel (3,4) in the matrix will be set to 1.
     * The touch matrix is a representation of a region adjacency graph
     * 
     */
    default boolean generateTouchMatrix(ClearCLBuffer label_map, ClearCLBuffer touch_matrix_destination) {
        if (doTimeTracing()) {recordMethodStart("GenerateTouchMatrix");}
        boolean result = GenerateTouchMatrix.generateTouchMatrix(getCLIJ2(), label_map, touch_matrix_destination);
        if (doTimeTracing()) {recordMethodEnd("GenerateTouchMatrix");}
        return result;
    }


    // net.haesleinhuepf.clij2.plugins.DetectLabelEdges
    //----------------------------------------------------
    /**
     * Takes a labelmap and returns an image where all pixels on label edges are set to 1 and all other pixels to 0.
     */
    default boolean detectLabelEdges(ClearCLImageInterface label_map, ClearCLBuffer edge_image_destination) {
        if (doTimeTracing()) {recordMethodStart("DetectLabelEdges");}
        boolean result = DetectLabelEdges.detectLabelEdges(getCLIJ2(), label_map, edge_image_destination);
        if (doTimeTracing()) {recordMethodEnd("DetectLabelEdges");}
        return result;
    }


    // net.haesleinhuepf.clij2.plugins.CountTouchingNeighbors
    //----------------------------------------------------
    /**
     * Takes a touch matrix as input and delivers a vector with number of touching neighbors per label as a vector.
     */
    default boolean countTouchingNeighbors(ClearCLBuffer touch_matrix, ClearCLBuffer touching_neighbors_count_destination) {
        if (doTimeTracing()) {recordMethodStart("CountTouchingNeighbors");}
        boolean result = CountTouchingNeighbors.countTouchingNeighbors(getCLIJ2(), touch_matrix, touching_neighbors_count_destination);
        if (doTimeTracing()) {recordMethodEnd("CountTouchingNeighbors");}
        return result;
    }


    // net.haesleinhuepf.clij2.plugins.ReplaceIntensities
    //----------------------------------------------------
    /**
     * Replaces integer intensities specified in a vector image. 
     * 
     * The vector image must be 3D with size (m, 1, 1) where m corresponds to the maximum intensity in the original image. Assuming the vector image contains values (0, 1, 0, 2) means: 
     *  * All pixels with value 0 (first entry in the vector image) get value 0
     *  * All pixels with value 1 get value 1
     *  * All pixels with value 2 get value 0
     *  * All pixels with value 3 get value 2
     * 
     */
    default boolean replaceIntensities(ClearCLImageInterface input, ClearCLImageInterface new_values_vector, ClearCLImageInterface destination) {
        if (doTimeTracing()) {recordMethodStart("ReplaceIntensities");}
        boolean result = ReplaceIntensities.replaceIntensities(getCLIJ2(), input, new_values_vector, destination);
        if (doTimeTracing()) {recordMethodEnd("ReplaceIntensities");}
        return result;
    }


    // net.haesleinhuepf.clij2.plugins.AverageDistanceOfNClosestPoints
    //----------------------------------------------------
    /**
     * 
     */
    @Deprecated
    default boolean averageDistanceOfClosestPoints(ClearCLBuffer arg1, ClearCLBuffer arg2, double arg3) {
        System.out.println("averageDistanceOfClosestPoints is deprecated. Check the documentation for a replacement. https://clij.github.io/clij2-doccs/reference");
        if (doTimeTracing()) {recordMethodStart("AverageDistanceOfNClosestPoints");}
        boolean result = AverageDistanceOfNClosestPoints.averageDistanceOfClosestPoints(getCLIJ2(), arg1, arg2, new Double (arg3).intValue());
        if (doTimeTracing()) {recordMethodEnd("AverageDistanceOfNClosestPoints");}
        return result;
    }

    /**
     * Determines the average of the n closest points for every point in a distance matrix.
     * 
     * This corresponds to the average of the n minimum values (rows) for each column of the distance matrix.
     */
    default boolean averageDistanceOfNClosestPoints(ClearCLBuffer arg1, ClearCLBuffer arg2, double arg3) {
        if (doTimeTracing()) {recordMethodStart("AverageDistanceOfNClosestPoints");}
        boolean result = AverageDistanceOfNClosestPoints.averageDistanceOfNClosestPoints(getCLIJ2(), arg1, arg2, new Double (arg3).intValue());
        if (doTimeTracing()) {recordMethodEnd("AverageDistanceOfNClosestPoints");}
        return result;
    }


    // net.haesleinhuepf.clij2.plugins.SaveAsTIF
    //----------------------------------------------------
    /**
     * Pulls an image from the GPU memory and saves it as TIF to disc.
     */
    default boolean saveAsTIF(ClearCLBuffer input, String filename) {
        if (doTimeTracing()) {recordMethodStart("SaveAsTIF");}
        boolean result = SaveAsTIF.saveAsTIF(getCLIJ2(), input, filename);
        if (doTimeTracing()) {recordMethodEnd("SaveAsTIF");}
        return result;
    }


    // net.haesleinhuepf.clij2.plugins.TouchMatrixToMesh
    //----------------------------------------------------
    /**
     * Takes a pointlist with dimensions n*d with n point coordinates in d dimensions and a touch matrix of 
     * size n*n to draw lines from all points to points if the corresponding pixel in the touch matrix is 1.
     * 
     * Parameters
     * ----------
     * pointlist : Image
     *     n*d matrix representing n coordinates with d dimensions.
     * touch_matrix : Image
     *     A 2D binary matrix with 1 in pixels (i,j) where label i touches label j.
     * mesh_destination : Image
     *     The output image where results are written into.
     * 
     */
    default boolean touchMatrixToMesh(ClearCLBuffer pointlist, ClearCLBuffer touch_matrix, ClearCLBuffer mesh_destination) {
        if (doTimeTracing()) {recordMethodStart("TouchMatrixToMesh");}
        boolean result = TouchMatrixToMesh.touchMatrixToMesh(getCLIJ2(), pointlist, touch_matrix, mesh_destination);
        if (doTimeTracing()) {recordMethodEnd("TouchMatrixToMesh");}
        return result;
    }


    // net.haesleinhuepf.clij2.plugins.Resample
    //----------------------------------------------------
    /**
     * Resamples an image with given size factors using an affine transform.
     */
    @Deprecated
    default boolean resample(ClearCLImageInterface arg1, ClearCLImageInterface arg2, double arg3, double arg4, double arg5, boolean arg6) {
        System.out.println("resample is deprecated. Check the documentation for a replacement. https://clij.github.io/clij2-doccs/reference");
        if (doTimeTracing()) {recordMethodStart("Resample");}
        boolean result = Resample.resample(getCLIJ2(), arg1, arg2, new Double (arg3).floatValue(), new Double (arg4).floatValue(), new Double (arg5).floatValue(), arg6);
        if (doTimeTracing()) {recordMethodEnd("Resample");}
        return result;
    }

    /**
     * 
     */
    default boolean resample2D(ClearCLImageInterface arg1, ClearCLImageInterface arg2, double arg3, double arg4, boolean arg5) {
        if (doTimeTracing()) {recordMethodStart("Resample");}
        boolean result = Resample.resample2D(getCLIJ2(), arg1, arg2, new Double (arg3).floatValue(), new Double (arg4).floatValue(), arg5);
        if (doTimeTracing()) {recordMethodEnd("Resample");}
        return result;
    }

    /**
     * 
     */
    default boolean resample3D(ClearCLImageInterface arg1, ClearCLImageInterface arg2, double arg3, double arg4, double arg5, boolean arg6) {
        if (doTimeTracing()) {recordMethodStart("Resample");}
        boolean result = Resample.resample3D(getCLIJ2(), arg1, arg2, new Double (arg3).floatValue(), new Double (arg4).floatValue(), new Double (arg5).floatValue(), arg6);
        if (doTimeTracing()) {recordMethodEnd("Resample");}
        return result;
    }


    // net.haesleinhuepf.clij2.plugins.EqualizeMeanIntensitiesOfSlices
    //----------------------------------------------------
    /**
     * Determines correction factors for each z-slice so that the average intensity in all slices can be made the same and multiplies these factors with the slices. 
     * 
     * This functionality is similar to the 'Simple Ratio Bleaching Correction' in Fiji.
     */
    default boolean equalizeMeanIntensitiesOfSlices(ClearCLBuffer arg1, ClearCLBuffer arg2, double arg3) {
        if (doTimeTracing()) {recordMethodStart("EqualizeMeanIntensitiesOfSlices");}
        boolean result = EqualizeMeanIntensitiesOfSlices.equalizeMeanIntensitiesOfSlices(getCLIJ2(), arg1, arg2, new Double (arg3).intValue());
        if (doTimeTracing()) {recordMethodEnd("EqualizeMeanIntensitiesOfSlices");}
        return result;
    }


    // net.haesleinhuepf.clij2.plugins.Watershed
    //----------------------------------------------------
    /**
     * Apply a binary watershed to a binary image and introduces black pixels between objects.
     */
    default boolean watershed(ClearCLBuffer binary_source, ClearCLBuffer destination) {
        if (doTimeTracing()) {recordMethodStart("Watershed");}
        boolean result = Watershed.watershed(getCLIJ2(), binary_source, destination);
        if (doTimeTracing()) {recordMethodEnd("Watershed");}
        return result;
    }


    // net.haesleinhuepf.clij2.plugins.ResliceRadial
    //----------------------------------------------------
    /**
     * 
     */
    @Deprecated
    default boolean radialProjection(ClearCLBuffer arg1, ClearCLBuffer arg2, double arg3) {
        System.out.println("radialProjection is deprecated. Check the documentation for a replacement. https://clij.github.io/clij2-doccs/reference");
        if (doTimeTracing()) {recordMethodStart("ResliceRadial");}
        boolean result = ResliceRadial.radialProjection(getCLIJ2(), arg1, arg2, new Double (arg3).floatValue());
        if (doTimeTracing()) {recordMethodEnd("ResliceRadial");}
        return result;
    }

    /**
     * Computes a radial projection of an image stack. 
     * 
     * Starting point for the line is the given point in any 
     * X/Y-plane of a given input image stack. Furthermore, radius of the resulting projection must be given and scaling factors in X and Y in case pixels are not isotropic.This operation is similar to ImageJs 'Radial Reslice' method but offers less flexibility.
     */
    default boolean resliceRadial(ClearCLBuffer arg1, ClearCLBuffer arg2, double arg3) {
        if (doTimeTracing()) {recordMethodStart("ResliceRadial");}
        boolean result = ResliceRadial.resliceRadial(getCLIJ2(), arg1, arg2, new Double (arg3).floatValue());
        if (doTimeTracing()) {recordMethodEnd("ResliceRadial");}
        return result;
    }

    /**
     * Computes a radial projection of an image stack. 
     * 
     * Starting point for the line is the given point in any 
     * X/Y-plane of a given input image stack. Furthermore, radius of the resulting projection must be given and scaling factors in X and Y in case pixels are not isotropic.This operation is similar to ImageJs 'Radial Reslice' method but offers less flexibility.
     */
    default boolean resliceRadial(ClearCLBuffer arg1, ClearCLBuffer arg2, double arg3, double arg4, double arg5) {
        if (doTimeTracing()) {recordMethodStart("ResliceRadial");}
        boolean result = ResliceRadial.resliceRadial(getCLIJ2(), arg1, arg2, new Double (arg3).floatValue(), new Double (arg4).floatValue(), new Double (arg5).floatValue());
        if (doTimeTracing()) {recordMethodEnd("ResliceRadial");}
        return result;
    }

    /**
     * Computes a radial projection of an image stack. 
     * 
     * Starting point for the line is the given point in any 
     * X/Y-plane of a given input image stack. Furthermore, radius of the resulting projection must be given and scaling factors in X and Y in case pixels are not isotropic.This operation is similar to ImageJs 'Radial Reslice' method but offers less flexibility.
     */
    default boolean resliceRadial(ClearCLBuffer arg1, ClearCLBuffer arg2, double arg3, double arg4, double arg5, double arg6, double arg7, double arg8) {
        if (doTimeTracing()) {recordMethodStart("ResliceRadial");}
        boolean result = ResliceRadial.resliceRadial(getCLIJ2(), arg1, arg2, new Double (arg3).floatValue(), new Double (arg4).floatValue(), new Double (arg5).floatValue(), new Double (arg6).floatValue(), new Double (arg7).floatValue(), new Double (arg8).floatValue());
        if (doTimeTracing()) {recordMethodEnd("ResliceRadial");}
        return result;
    }


    // net.haesleinhuepf.clij2.plugins.Sobel
    //----------------------------------------------------
    /**
     * Convolve the image with the Sobel kernel.
     */
    default boolean sobel(ClearCLBuffer source, ClearCLBuffer destination) {
        if (doTimeTracing()) {recordMethodStart("Sobel");}
        boolean result = Sobel.sobel(getCLIJ2(), source, destination);
        if (doTimeTracing()) {recordMethodEnd("Sobel");}
        return result;
    }


    // net.haesleinhuepf.clij2.plugins.Absolute
    //----------------------------------------------------
    /**
     * Computes the absolute value of every individual pixel x in a given image.
     * 
     * <pre>f(x) = |x| </pre>
     * 
     * Parameters
     * ----------
     * source : Image
     *     The input image to be processed.
     * destination : Image
     *     The output image where results are written into.
     * 
     */
    default boolean absolute(ClearCLImageInterface source, ClearCLImageInterface destination) {
        if (doTimeTracing()) {recordMethodStart("Absolute");}
        boolean result = Absolute.absolute(getCLIJ2(), source, destination);
        if (doTimeTracing()) {recordMethodEnd("Absolute");}
        return result;
    }


    // net.haesleinhuepf.clij2.plugins.LaplaceBox
    //----------------------------------------------------
    /**
     * Applies the Laplace operator (Box neighborhood) to an image.
     */
    default boolean laplaceBox(ClearCLBuffer input, ClearCLBuffer destination) {
        if (doTimeTracing()) {recordMethodStart("LaplaceBox");}
        boolean result = LaplaceBox.laplaceBox(getCLIJ2(), input, destination);
        if (doTimeTracing()) {recordMethodEnd("LaplaceBox");}
        return result;
    }


    // net.haesleinhuepf.clij2.plugins.BottomHatBox
    //----------------------------------------------------
    /**
     * Apply a bottom-hat filter for background subtraction to the input image.
     * 
     * Parameters
     * ----------
     * input : Image
     *     The input image where the background is subtracted from.
     * destination : Image
     *     The output image where results are written into.
     * radius_x : Image
     *     Radius of the background determination region in X.
     * radius_y : Image
     *     Radius of the background determination region in Y.
     * radius_z : Image
     *     Radius of the background determination region in Z.
     * 
     */
    default boolean bottomHatBox(ClearCLBuffer arg1, ClearCLBuffer arg2, double arg3, double arg4, double arg5) {
        if (doTimeTracing()) {recordMethodStart("BottomHatBox");}
        boolean result = BottomHatBox.bottomHatBox(getCLIJ2(), arg1, arg2, new Double (arg3).intValue(), new Double (arg4).intValue(), new Double (arg5).intValue());
        if (doTimeTracing()) {recordMethodEnd("BottomHatBox");}
        return result;
    }


    // net.haesleinhuepf.clij2.plugins.BottomHatSphere
    //----------------------------------------------------
    /**
     * Applies a bottom-hat filter for background subtraction to the input image.
     * 
     * Parameters
     * ----------
     * input : Image
     *     The input image where the background is subtracted from.
     * destination : Image
     *     The output image where results are written into.
     * radius_x : Image
     *     Radius of the background determination region in X.
     * radius_y : Image
     *     Radius of the background determination region in Y.
     * radius_z : Image
     *     Radius of the background determination region in Z.
     * 
     */
    default boolean bottomHatSphere(ClearCLBuffer arg1, ClearCLBuffer arg2, double arg3, double arg4, double arg5) {
        if (doTimeTracing()) {recordMethodStart("BottomHatSphere");}
        boolean result = BottomHatSphere.bottomHatSphere(getCLIJ2(), arg1, arg2, new Double (arg3).intValue(), new Double (arg4).intValue(), new Double (arg5).intValue());
        if (doTimeTracing()) {recordMethodEnd("BottomHatSphere");}
        return result;
    }


    // net.haesleinhuepf.clij2.plugins.ClosingBox
    //----------------------------------------------------
    /**
     * Apply a binary closing to the input image by calling n dilations and n erosions subsequenntly.
     */
    default boolean closingBox(ClearCLBuffer arg1, ClearCLBuffer arg2, double arg3) {
        if (doTimeTracing()) {recordMethodStart("ClosingBox");}
        boolean result = ClosingBox.closingBox(getCLIJ2(), arg1, arg2, new Double (arg3).intValue());
        if (doTimeTracing()) {recordMethodEnd("ClosingBox");}
        return result;
    }


    // net.haesleinhuepf.clij2.plugins.ClosingDiamond
    //----------------------------------------------------
    /**
     * Apply a binary closing to the input image by calling n dilations and n erosions subsequently.
     */
    default boolean closingDiamond(ClearCLBuffer arg1, ClearCLBuffer arg2, double arg3) {
        if (doTimeTracing()) {recordMethodStart("ClosingDiamond");}
        boolean result = ClosingDiamond.closingDiamond(getCLIJ2(), arg1, arg2, new Double (arg3).intValue());
        if (doTimeTracing()) {recordMethodEnd("ClosingDiamond");}
        return result;
    }


    // net.haesleinhuepf.clij2.plugins.OpeningBox
    //----------------------------------------------------
    /**
     * Apply a binary opening to the input image by calling n erosions and n dilations subsequenntly.
     */
    default boolean openingBox(ClearCLBuffer arg1, ClearCLBuffer arg2, double arg3) {
        if (doTimeTracing()) {recordMethodStart("OpeningBox");}
        boolean result = OpeningBox.openingBox(getCLIJ2(), arg1, arg2, new Double (arg3).intValue());
        if (doTimeTracing()) {recordMethodEnd("OpeningBox");}
        return result;
    }


    // net.haesleinhuepf.clij2.plugins.OpeningDiamond
    //----------------------------------------------------
    /**
     * Apply a binary opening to the input image by calling n erosions and n dilations subsequenntly.
     */
    default boolean openingDiamond(ClearCLBuffer arg1, ClearCLBuffer arg2, double arg3) {
        if (doTimeTracing()) {recordMethodStart("OpeningDiamond");}
        boolean result = OpeningDiamond.openingDiamond(getCLIJ2(), arg1, arg2, new Double (arg3).intValue());
        if (doTimeTracing()) {recordMethodEnd("OpeningDiamond");}
        return result;
    }


    // net.haesleinhuepf.clij2.plugins.MaximumXProjection
    //----------------------------------------------------
    /**
     * Determines the maximum intensity projection of an image along X.
     */
    default boolean maximumXProjection(ClearCLImageInterface source, ClearCLImageInterface destination_max) {
        if (doTimeTracing()) {recordMethodStart("MaximumXProjection");}
        boolean result = MaximumXProjection.maximumXProjection(getCLIJ2(), source, destination_max);
        if (doTimeTracing()) {recordMethodEnd("MaximumXProjection");}
        return result;
    }


    // net.haesleinhuepf.clij2.plugins.MaximumYProjection
    //----------------------------------------------------
    /**
     * Determines the maximum intensity projection of an image along X.
     */
    default boolean maximumYProjection(ClearCLImageInterface source, ClearCLImageInterface destination_max) {
        if (doTimeTracing()) {recordMethodStart("MaximumYProjection");}
        boolean result = MaximumYProjection.maximumYProjection(getCLIJ2(), source, destination_max);
        if (doTimeTracing()) {recordMethodEnd("MaximumYProjection");}
        return result;
    }


    // net.haesleinhuepf.clij2.plugins.MaximumZProjectionBounded
    //----------------------------------------------------
    /**
     * Determines the maximum intensity projection of an image along Z within a given z range.
     */
    default boolean maximumZProjectionBounded(ClearCLImageInterface arg1, ClearCLImageInterface arg2, double arg3, double arg4) {
        if (doTimeTracing()) {recordMethodStart("MaximumZProjectionBounded");}
        boolean result = MaximumZProjectionBounded.maximumZProjectionBounded(getCLIJ2(), arg1, arg2, new Double (arg3).intValue(), new Double (arg4).intValue());
        if (doTimeTracing()) {recordMethodEnd("MaximumZProjectionBounded");}
        return result;
    }


    // net.haesleinhuepf.clij2.plugins.MinimumZProjectionBounded
    //----------------------------------------------------
    /**
     * Determines the minimum intensity projection of an image along Z within a given z range.
     */
    default boolean minimumZProjectionBounded(ClearCLImageInterface arg1, ClearCLImageInterface arg2, double arg3, double arg4) {
        if (doTimeTracing()) {recordMethodStart("MinimumZProjectionBounded");}
        boolean result = MinimumZProjectionBounded.minimumZProjectionBounded(getCLIJ2(), arg1, arg2, new Double (arg3).intValue(), new Double (arg4).intValue());
        if (doTimeTracing()) {recordMethodEnd("MinimumZProjectionBounded");}
        return result;
    }


    // net.haesleinhuepf.clij2.plugins.MeanZProjectionBounded
    //----------------------------------------------------
    /**
     * Determines the mean average intensity projection of an image along Z within a given z range.
     */
    default boolean meanZProjectionBounded(ClearCLImageInterface arg1, ClearCLImageInterface arg2, double arg3, double arg4) {
        if (doTimeTracing()) {recordMethodStart("MeanZProjectionBounded");}
        boolean result = MeanZProjectionBounded.meanZProjectionBounded(getCLIJ(), arg1, arg2, new Double (arg3).intValue(), new Double (arg4).intValue());
        if (doTimeTracing()) {recordMethodEnd("MeanZProjectionBounded");}
        return result;
    }


    // net.haesleinhuepf.clij2.plugins.NonzeroMaximumBox
    //----------------------------------------------------
    /**
     * Apply a maximum filter (box shape) to the input image. 
     * 
     * The radius is fixed to 1 and pixels with value 0 are ignored.
     * Note: Pixels with 0 value in the input image will not be overwritten in the output image.
     * Thus, the result image should be initialized by copying the original image in advance.
     */
    default boolean nonzeroMaximumBox(ClearCLImageInterface input, ClearCLImageInterface destination) {
        if (doTimeTracing()) {recordMethodStart("NonzeroMaximumBox");}
        boolean result = NonzeroMaximumBox.nonzeroMaximumBox(getCLIJ2(), input, destination);
        if (doTimeTracing()) {recordMethodEnd("NonzeroMaximumBox");}
        return result;
    }

    /**
     * Apply a maximum filter (box shape) to the input image. 
     * 
     * The radius is fixed to 1 and pixels with value 0 are ignored.
     * Note: Pixels with 0 value in the input image will not be overwritten in the output image.
     * Thus, the result image should be initialized by copying the original image in advance.
     */
    default boolean nonzeroMaximumBox(ClearCLImageInterface arg1, ClearCLImageInterface arg2, ClearCLImageInterface arg3) {
        if (doTimeTracing()) {recordMethodStart("NonzeroMaximumBox");}
        boolean result = NonzeroMaximumBox.nonzeroMaximumBox(getCLIJ2(), arg1, arg2, arg3);
        if (doTimeTracing()) {recordMethodEnd("NonzeroMaximumBox");}
        return result;
    }

    /**
     * Apply a maximum filter (box shape) to the input image. 
     * 
     * The radius is fixed to 1 and pixels with value 0 are ignored.
     * Note: Pixels with 0 value in the input image will not be overwritten in the output image.
     * Thus, the result image should be initialized by copying the original image in advance.
     */
    default ClearCLKernel nonzeroMaximumBox(ClearCLImageInterface arg1, ClearCLImageInterface arg2, ClearCLImageInterface arg3, ClearCLKernel arg4) {
        if (doTimeTracing()) {recordMethodStart("NonzeroMaximumBox");}
        ClearCLKernel result = NonzeroMaximumBox.nonzeroMaximumBox(getCLIJ2(), arg1, arg2, arg3, arg4);
        if (doTimeTracing()) {recordMethodEnd("NonzeroMaximumBox");}
        return result;
    }


    // net.haesleinhuepf.clij2.plugins.NonzeroMinimumBox
    //----------------------------------------------------
    /**
     * Apply a minimum filter (box shape) to the input image. 
     * 
     * The radius is fixed to 1 and pixels with value 0 are ignored.
     * Note: Pixels with 0 value in the input image will not be overwritten in the output image.
     * Thus, the result image should be initialized by copying the original image in advance.
     */
    default boolean nonzeroMinimumBox(ClearCLImageInterface input, ClearCLImageInterface destination) {
        if (doTimeTracing()) {recordMethodStart("NonzeroMinimumBox");}
        boolean result = NonzeroMinimumBox.nonzeroMinimumBox(getCLIJ2(), input, destination);
        if (doTimeTracing()) {recordMethodEnd("NonzeroMinimumBox");}
        return result;
    }

    /**
     * Apply a minimum filter (box shape) to the input image. 
     * 
     * The radius is fixed to 1 and pixels with value 0 are ignored.
     * Note: Pixels with 0 value in the input image will not be overwritten in the output image.
     * Thus, the result image should be initialized by copying the original image in advance.
     */
    default boolean nonzeroMinimumBox(ClearCLImageInterface arg1, ClearCLImageInterface arg2, ClearCLImageInterface arg3) {
        if (doTimeTracing()) {recordMethodStart("NonzeroMinimumBox");}
        boolean result = NonzeroMinimumBox.nonzeroMinimumBox(getCLIJ2(), arg1, arg2, arg3);
        if (doTimeTracing()) {recordMethodEnd("NonzeroMinimumBox");}
        return result;
    }

    /**
     * Apply a minimum filter (box shape) to the input image. 
     * 
     * The radius is fixed to 1 and pixels with value 0 are ignored.
     * Note: Pixels with 0 value in the input image will not be overwritten in the output image.
     * Thus, the result image should be initialized by copying the original image in advance.
     */
    default ClearCLKernel nonzeroMinimumBox(ClearCLImageInterface arg1, ClearCLImageInterface arg2, ClearCLImageInterface arg3, ClearCLKernel arg4) {
        if (doTimeTracing()) {recordMethodStart("NonzeroMinimumBox");}
        ClearCLKernel result = NonzeroMinimumBox.nonzeroMinimumBox(getCLIJ2(), arg1, arg2, arg3, arg4);
        if (doTimeTracing()) {recordMethodEnd("NonzeroMinimumBox");}
        return result;
    }


    // net.haesleinhuepf.clij2.plugins.MinimumZProjectionThresholdedBounded
    //----------------------------------------------------
    /**
     * Determines the minimum intensity projection of all pixels in an image above a given threshold along Z within a given z range.
     */
    default boolean minimumZProjectionThresholdedBounded(ClearCLBuffer arg1, ClearCLBuffer arg2, double arg3, double arg4, double arg5) {
        if (doTimeTracing()) {recordMethodStart("MinimumZProjectionThresholdedBounded");}
        boolean result = MinimumZProjectionThresholdedBounded.minimumZProjectionThresholdedBounded(getCLIJ2(), arg1, arg2, new Double (arg3).floatValue(), new Double (arg4).intValue(), new Double (arg5).intValue());
        if (doTimeTracing()) {recordMethodEnd("MinimumZProjectionThresholdedBounded");}
        return result;
    }


    // net.haesleinhuepf.clij2.plugins.MeanOfPixelsAboveThreshold
    //----------------------------------------------------
    /**
     * Determines the mean intensity in a threshleded image. 
     * 
     * But only in pixels which are above a given threshold.
     */
    default double meanOfPixelsAboveThreshold(ClearCLBuffer arg1, double arg2) {
        if (doTimeTracing()) {recordMethodStart("MeanOfPixelsAboveThreshold");}
        double result = MeanOfPixelsAboveThreshold.meanOfPixelsAboveThreshold(getCLIJ2(), arg1, new Double (arg2).floatValue());
        if (doTimeTracing()) {recordMethodEnd("MeanOfPixelsAboveThreshold");}
        return result;
    }


    // net.haesleinhuepf.clij2.plugins.DistanceMatrixToMesh
    //----------------------------------------------------
    /**
     * Generates a mesh from a distance matric and a list of point coordinates.
     * 
     * Takes a pointlist with dimensions n*d with n point coordinates in d dimensions and a distance matrix of size n*n to draw lines from all points to points if the corresponding pixel in the distance matrix is smaller than a given distance threshold.
     */
    default boolean distanceMatrixToMesh(ClearCLBuffer arg1, ClearCLBuffer arg2, ClearCLBuffer arg3, double arg4) {
        if (doTimeTracing()) {recordMethodStart("DistanceMatrixToMesh");}
        boolean result = DistanceMatrixToMesh.distanceMatrixToMesh(getCLIJ2(), arg1, arg2, arg3, new Double (arg4).floatValue());
        if (doTimeTracing()) {recordMethodEnd("DistanceMatrixToMesh");}
        return result;
    }


    // net.haesleinhuepf.clij2.plugins.PointIndexListToMesh
    //----------------------------------------------------
    /**
     * Meshes all points in a given point list which are indiced in a corresponding index list.
     */
    default boolean pointIndexListToMesh(ClearCLBuffer pointlist, ClearCLBuffer indexlist, ClearCLBuffer mesh_destination) {
        if (doTimeTracing()) {recordMethodStart("PointIndexListToMesh");}
        boolean result = PointIndexListToMesh.pointIndexListToMesh(getCLIJ2(), pointlist, indexlist, mesh_destination);
        if (doTimeTracing()) {recordMethodEnd("PointIndexListToMesh");}
        return result;
    }


    // net.haesleinhuepf.clij2.plugins.MinimumOctagon
    //----------------------------------------------------
    /**
     * Applies a minimum filter with kernel size 3x3 n times to an image iteratively. 
     * 
     * Odd iterations are done with box neighborhood, even iterations with a diamond. Thus, with n > 2, the filter shape is an octagon. The given number of iterations makes the filter result very similar to minimum sphere. Approximately:radius = iterations - 2
     */
    default boolean minimumOctagon(ClearCLBuffer arg1, ClearCLBuffer arg2, double arg3) {
        if (doTimeTracing()) {recordMethodStart("MinimumOctagon");}
        boolean result = MinimumOctagon.minimumOctagon(getCLIJ2(), arg1, arg2, new Double (arg3).intValue());
        if (doTimeTracing()) {recordMethodEnd("MinimumOctagon");}
        return result;
    }

    /**
     * Computes the local minimum of a pixels rectangular neighborhood. 
     * 
     * The rectangles size is specified by 
     * its half-width and half-height (radius).
     */
    @Deprecated
    default ClearCLKernel minimumBox(ClearCLImageInterface arg1, ClearCLImageInterface arg2, ClearCLKernel arg3) {
        System.out.println("minimumBox is deprecated. Check the documentation for a replacement. https://clij.github.io/clij2-doccs/reference");
        if (doTimeTracing()) {recordMethodStart("MinimumOctagon");}
        ClearCLKernel result = MinimumOctagon.minimumBox(getCLIJ2(), arg1, arg2, arg3);
        if (doTimeTracing()) {recordMethodEnd("MinimumOctagon");}
        return result;
    }

    /**
     * 
     */
    @Deprecated
    default ClearCLKernel minimumDiamond(ClearCLImageInterface arg1, ClearCLImageInterface arg2, ClearCLKernel arg3) {
        System.out.println("minimumDiamond is deprecated. Check the documentation for a replacement. https://clij.github.io/clij2-doccs/reference");
        if (doTimeTracing()) {recordMethodStart("MinimumOctagon");}
        ClearCLKernel result = MinimumOctagon.minimumDiamond(getCLIJ2(), arg1, arg2, arg3);
        if (doTimeTracing()) {recordMethodEnd("MinimumOctagon");}
        return result;
    }


    // net.haesleinhuepf.clij2.plugins.MaximumOctagon
    //----------------------------------------------------
    /**
     * Applies a maximum filter with kernel size 3x3 n times to an image iteratively. 
     * 
     * Odd iterations are done with box neighborhood, even iterations with a diamond. 
     * Thus, with n > 2, the filter shape is an octagon. The given number of iterations makes the filter 
     * result very similar to minimum sphere. Approximately:radius = iterations - 2
     */
    default boolean maximumOctagon(ClearCLBuffer arg1, ClearCLBuffer arg2, double arg3) {
        if (doTimeTracing()) {recordMethodStart("MaximumOctagon");}
        boolean result = MaximumOctagon.maximumOctagon(getCLIJ2(), arg1, arg2, new Double (arg3).intValue());
        if (doTimeTracing()) {recordMethodEnd("MaximumOctagon");}
        return result;
    }

    /**
     * Computes the local maximum of a pixels rectangular neighborhood. 
     * 
     * The rectangles size is specified by 
     * its half-width and half-height (radius).
     */
    @Deprecated
    default ClearCLKernel maximumBox(ClearCLImageInterface arg1, ClearCLImageInterface arg2, ClearCLKernel arg3) {
        System.out.println("maximumBox is deprecated. Check the documentation for a replacement. https://clij.github.io/clij2-doccs/reference");
        if (doTimeTracing()) {recordMethodStart("MaximumOctagon");}
        ClearCLKernel result = MaximumOctagon.maximumBox(getCLIJ2(), arg1, arg2, arg3);
        if (doTimeTracing()) {recordMethodEnd("MaximumOctagon");}
        return result;
    }

    /**
     * 
     */
    @Deprecated
    default ClearCLKernel maximumDiamond(ClearCLImageInterface arg1, ClearCLImageInterface arg2, ClearCLKernel arg3) {
        System.out.println("maximumDiamond is deprecated. Check the documentation for a replacement. https://clij.github.io/clij2-doccs/reference");
        if (doTimeTracing()) {recordMethodStart("MaximumOctagon");}
        ClearCLKernel result = MaximumOctagon.maximumDiamond(getCLIJ2(), arg1, arg2, arg3);
        if (doTimeTracing()) {recordMethodEnd("MaximumOctagon");}
        return result;
    }


    // net.haesleinhuepf.clij2.plugins.AddImages
    //----------------------------------------------------
    /**
     * Calculates the sum of pairs of pixels x and y of two images X and Y.
     * 
     * <pre>f(x, y) = x + y</pre>
     * 
     * Parameters
     * ----------
     * summand1 : Image
     *     The first input image to added.
     * summand2 : Image
     *     The second image to be added.
     * destination : Image
     *     The output image where results are written into.
     * 
     */
    default boolean addImages(ClearCLImageInterface summand1, ClearCLImageInterface summand2, ClearCLImageInterface destination) {
        if (doTimeTracing()) {recordMethodStart("AddImages");}
        boolean result = AddImages.addImages(getCLIJ2(), summand1, summand2, destination);
        if (doTimeTracing()) {recordMethodEnd("AddImages");}
        return result;
    }


    // net.haesleinhuepf.clij2.plugins.AddImagesWeighted
    //----------------------------------------------------
    /**
     * Calculates the sum of pairs of pixels x and y from images X and Y weighted with factors a and b.
     * 
     * <pre>f(x, y, a, b) = x * a + y * b</pre>
     * 
     * Parameters
     * ----------
     * summand1 : Image
     *     The first input image to added.
     * summand2 : Image
     *     The second image to be added.
     * destination : Image
     *     The output image where results are written into.
     * factor1 : float
     *     The constant number which will be multiplied with each pixel of summand1 before adding it.
     * factor2 : float
     *     The constant number which will be multiplied with each pixel of summand2 before adding it.
     * 
     */
    default boolean addImagesWeighted(ClearCLImageInterface arg1, ClearCLImageInterface arg2, ClearCLImageInterface arg3, double arg4, double arg5) {
        if (doTimeTracing()) {recordMethodStart("AddImagesWeighted");}
        boolean result = AddImagesWeighted.addImagesWeighted(getCLIJ2(), arg1, arg2, arg3, new Double (arg4).floatValue(), new Double (arg5).floatValue());
        if (doTimeTracing()) {recordMethodEnd("AddImagesWeighted");}
        return result;
    }


    // net.haesleinhuepf.clij2.plugins.SubtractImages
    //----------------------------------------------------
    /**
     * Subtracts one image X from another image Y pixel wise.
     * 
     * <pre>f(x, y) = x - y</pre>
     */
    @Deprecated
    default boolean subtract(ClearCLImageInterface subtrahend, ClearCLImageInterface minuend, ClearCLImageInterface destination) {
        System.out.println("subtract is deprecated. Check the documentation for a replacement. https://clij.github.io/clij2-doccs/reference");
        if (doTimeTracing()) {recordMethodStart("SubtractImages");}
        boolean result = SubtractImages.subtract(getCLIJ2(), subtrahend, minuend, destination);
        if (doTimeTracing()) {recordMethodEnd("SubtractImages");}
        return result;
    }

    /**
     * Subtracts one image X from another image Y pixel wise.
     * 
     * <pre>f(x, y) = x - y</pre>
     */
    default boolean subtractImages(ClearCLImageInterface subtrahend, ClearCLImageInterface minuend, ClearCLImageInterface destination) {
        if (doTimeTracing()) {recordMethodStart("SubtractImages");}
        boolean result = SubtractImages.subtractImages(getCLIJ2(), subtrahend, minuend, destination);
        if (doTimeTracing()) {recordMethodEnd("SubtractImages");}
        return result;
    }


    // net.haesleinhuepf.clij2.plugins.AffineTransform2D
    //----------------------------------------------------
    /**
     * Applies an affine transform to a 2D image.
     * 
     * The transform describes how coordinates in the target image are transformed to coordinates in the source image.
     * This may appear unintuitive and will be changed in the next major release. The replacement 
     * affineTransform (currently part of CLIJx) will apply inverted transforms compared to this operation.
     * Individual transforms must be separated by spaces.
     * Supported transforms:
     * 
     * * -center: translate the coordinate origin to the center of the image
     * * center: translate the coordinate origin back to the initial origin
     * * rotate=[angle]: rotate in X/Y plane (around Z-axis) by the given angle in degrees
     * * scale=[factor]: isotropic scaling according to given zoom factor
     * * scaleX=[factor]: scaling along X-axis according to given zoom factor
     * * scaleY=[factor]: scaling along Y-axis according to given zoom factor
     * * shearXY=[factor]: shearing along X-axis in XY plane according to given factor
     * * translateX=[distance]: translate along X-axis by distance given in pixels
     * * translateY=[distance]: translate along X-axis by distance given in pixels
     * 
     * Example transform:
     * transform = "-center scale=2 rotate=45 center";
     */
    default boolean affineTransform2D(ClearCLBuffer arg1, ClearCLImageInterface arg2, float[] arg3) {
        if (doTimeTracing()) {recordMethodStart("AffineTransform2D");}
        boolean result = AffineTransform2D.affineTransform2D(getCLIJ2(), arg1, arg2, arg3);
        if (doTimeTracing()) {recordMethodEnd("AffineTransform2D");}
        return result;
    }

    /**
     * Applies an affine transform to a 2D image.
     * 
     * The transform describes how coordinates in the target image are transformed to coordinates in the source image.
     * This may appear unintuitive and will be changed in the next major release. The replacement 
     * affineTransform (currently part of CLIJx) will apply inverted transforms compared to this operation.
     * Individual transforms must be separated by spaces.
     * Supported transforms:
     * 
     * * -center: translate the coordinate origin to the center of the image
     * * center: translate the coordinate origin back to the initial origin
     * * rotate=[angle]: rotate in X/Y plane (around Z-axis) by the given angle in degrees
     * * scale=[factor]: isotropic scaling according to given zoom factor
     * * scaleX=[factor]: scaling along X-axis according to given zoom factor
     * * scaleY=[factor]: scaling along Y-axis according to given zoom factor
     * * shearXY=[factor]: shearing along X-axis in XY plane according to given factor
     * * translateX=[distance]: translate along X-axis by distance given in pixels
     * * translateY=[distance]: translate along X-axis by distance given in pixels
     * 
     * Example transform:
     * transform = "-center scale=2 rotate=45 center";
     */
    default boolean affineTransform2D(ClearCLBuffer source, ClearCLImageInterface destination, String transform) {
        if (doTimeTracing()) {recordMethodStart("AffineTransform2D");}
        boolean result = AffineTransform2D.affineTransform2D(getCLIJ2(), source, destination, transform);
        if (doTimeTracing()) {recordMethodEnd("AffineTransform2D");}
        return result;
    }

    /**
     * Applies an affine transform to a 2D image.
     * 
     * The transform describes how coordinates in the target image are transformed to coordinates in the source image.
     * This may appear unintuitive and will be changed in the next major release. The replacement 
     * affineTransform (currently part of CLIJx) will apply inverted transforms compared to this operation.
     * Individual transforms must be separated by spaces.
     * Supported transforms:
     * 
     * * -center: translate the coordinate origin to the center of the image
     * * center: translate the coordinate origin back to the initial origin
     * * rotate=[angle]: rotate in X/Y plane (around Z-axis) by the given angle in degrees
     * * scale=[factor]: isotropic scaling according to given zoom factor
     * * scaleX=[factor]: scaling along X-axis according to given zoom factor
     * * scaleY=[factor]: scaling along Y-axis according to given zoom factor
     * * shearXY=[factor]: shearing along X-axis in XY plane according to given factor
     * * translateX=[distance]: translate along X-axis by distance given in pixels
     * * translateY=[distance]: translate along X-axis by distance given in pixels
     * 
     * Example transform:
     * transform = "-center scale=2 rotate=45 center";
     */
    default boolean affineTransform2D(ClearCLBuffer arg1, ClearCLImageInterface arg2, net.imglib2.realtransform.AffineTransform2D arg3) {
        if (doTimeTracing()) {recordMethodStart("AffineTransform2D");}
        boolean result = AffineTransform2D.affineTransform2D(getCLIJ2(), arg1, arg2, arg3);
        if (doTimeTracing()) {recordMethodEnd("AffineTransform2D");}
        return result;
    }

    /**
     * Applies an affine transform to a 2D image.
     * 
     * The transform describes how coordinates in the target image are transformed to coordinates in the source image.
     * This may appear unintuitive and will be changed in the next major release. The replacement 
     * affineTransform (currently part of CLIJx) will apply inverted transforms compared to this operation.
     * Individual transforms must be separated by spaces.
     * Supported transforms:
     * 
     * * -center: translate the coordinate origin to the center of the image
     * * center: translate the coordinate origin back to the initial origin
     * * rotate=[angle]: rotate in X/Y plane (around Z-axis) by the given angle in degrees
     * * scale=[factor]: isotropic scaling according to given zoom factor
     * * scaleX=[factor]: scaling along X-axis according to given zoom factor
     * * scaleY=[factor]: scaling along Y-axis according to given zoom factor
     * * shearXY=[factor]: shearing along X-axis in XY plane according to given factor
     * * translateX=[distance]: translate along X-axis by distance given in pixels
     * * translateY=[distance]: translate along X-axis by distance given in pixels
     * 
     * Example transform:
     * transform = "-center scale=2 rotate=45 center";
     */
    default boolean affineTransform2D(ClearCLImage arg1, ClearCLImageInterface arg2, float[] arg3) {
        if (doTimeTracing()) {recordMethodStart("AffineTransform2D");}
        boolean result = AffineTransform2D.affineTransform2D(getCLIJ2(), arg1, arg2, arg3);
        if (doTimeTracing()) {recordMethodEnd("AffineTransform2D");}
        return result;
    }

    /**
     * Applies an affine transform to a 2D image.
     * 
     * The transform describes how coordinates in the target image are transformed to coordinates in the source image.
     * This may appear unintuitive and will be changed in the next major release. The replacement 
     * affineTransform (currently part of CLIJx) will apply inverted transforms compared to this operation.
     * Individual transforms must be separated by spaces.
     * Supported transforms:
     * 
     * * -center: translate the coordinate origin to the center of the image
     * * center: translate the coordinate origin back to the initial origin
     * * rotate=[angle]: rotate in X/Y plane (around Z-axis) by the given angle in degrees
     * * scale=[factor]: isotropic scaling according to given zoom factor
     * * scaleX=[factor]: scaling along X-axis according to given zoom factor
     * * scaleY=[factor]: scaling along Y-axis according to given zoom factor
     * * shearXY=[factor]: shearing along X-axis in XY plane according to given factor
     * * translateX=[distance]: translate along X-axis by distance given in pixels
     * * translateY=[distance]: translate along X-axis by distance given in pixels
     * 
     * Example transform:
     * transform = "-center scale=2 rotate=45 center";
     */
    default boolean affineTransform2D(ClearCLImage arg1, ClearCLImageInterface arg2, net.imglib2.realtransform.AffineTransform2D arg3) {
        if (doTimeTracing()) {recordMethodStart("AffineTransform2D");}
        boolean result = AffineTransform2D.affineTransform2D(getCLIJ2(), arg1, arg2, arg3);
        if (doTimeTracing()) {recordMethodEnd("AffineTransform2D");}
        return result;
    }


    // net.haesleinhuepf.clij2.plugins.AffineTransform3D
    //----------------------------------------------------
    /**
     * Applies an affine transform to a 3D image.
     * 
     * The transform describes how coordinates in the target image are transformed to coordinates in the source image.
     * This may appear unintuitive and will be changed in the next major release. The replacement 
     * affineTransform (currently part of CLIJx) will apply inverted transforms compared to this operation.
     * Individual transforms must be separated by spaces.
     * Supported transforms:
     * 
     * * -center: translate the coordinate origin to the center of the image
     * * center: translate the coordinate origin back to the initial origin
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
     * transform = "-center scale=2 rotate=45 center";
     */
    default boolean affineTransform3D(ClearCLBuffer arg1, ClearCLImageInterface arg2, float[] arg3) {
        if (doTimeTracing()) {recordMethodStart("AffineTransform3D");}
        boolean result = AffineTransform3D.affineTransform3D(getCLIJ2(), arg1, arg2, arg3);
        if (doTimeTracing()) {recordMethodEnd("AffineTransform3D");}
        return result;
    }

    /**
     * Applies an affine transform to a 3D image.
     * 
     * The transform describes how coordinates in the target image are transformed to coordinates in the source image.
     * This may appear unintuitive and will be changed in the next major release. The replacement 
     * affineTransform (currently part of CLIJx) will apply inverted transforms compared to this operation.
     * Individual transforms must be separated by spaces.
     * Supported transforms:
     * 
     * * -center: translate the coordinate origin to the center of the image
     * * center: translate the coordinate origin back to the initial origin
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
     * transform = "-center scale=2 rotate=45 center";
     */
    default boolean affineTransform3D(ClearCLBuffer source, ClearCLImageInterface destination, String transform) {
        if (doTimeTracing()) {recordMethodStart("AffineTransform3D");}
        boolean result = AffineTransform3D.affineTransform3D(getCLIJ2(), source, destination, transform);
        if (doTimeTracing()) {recordMethodEnd("AffineTransform3D");}
        return result;
    }

    /**
     * Applies an affine transform to a 3D image.
     * 
     * The transform describes how coordinates in the target image are transformed to coordinates in the source image.
     * This may appear unintuitive and will be changed in the next major release. The replacement 
     * affineTransform (currently part of CLIJx) will apply inverted transforms compared to this operation.
     * Individual transforms must be separated by spaces.
     * Supported transforms:
     * 
     * * -center: translate the coordinate origin to the center of the image
     * * center: translate the coordinate origin back to the initial origin
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
     * transform = "-center scale=2 rotate=45 center";
     */
    default boolean affineTransform3D(ClearCLBuffer arg1, ClearCLImageInterface arg2, net.imglib2.realtransform.AffineTransform3D arg3) {
        if (doTimeTracing()) {recordMethodStart("AffineTransform3D");}
        boolean result = AffineTransform3D.affineTransform3D(getCLIJ2(), arg1, arg2, arg3);
        if (doTimeTracing()) {recordMethodEnd("AffineTransform3D");}
        return result;
    }

    /**
     * Applies an affine transform to a 3D image.
     * 
     * The transform describes how coordinates in the target image are transformed to coordinates in the source image.
     * This may appear unintuitive and will be changed in the next major release. The replacement 
     * affineTransform (currently part of CLIJx) will apply inverted transforms compared to this operation.
     * Individual transforms must be separated by spaces.
     * Supported transforms:
     * 
     * * -center: translate the coordinate origin to the center of the image
     * * center: translate the coordinate origin back to the initial origin
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
     * transform = "-center scale=2 rotate=45 center";
     */
    default boolean affineTransform3D(ClearCLImage arg1, ClearCLImageInterface arg2, float[] arg3) {
        if (doTimeTracing()) {recordMethodStart("AffineTransform3D");}
        boolean result = AffineTransform3D.affineTransform3D(getCLIJ2(), arg1, arg2, arg3);
        if (doTimeTracing()) {recordMethodEnd("AffineTransform3D");}
        return result;
    }

    /**
     * Applies an affine transform to a 3D image.
     * 
     * The transform describes how coordinates in the target image are transformed to coordinates in the source image.
     * This may appear unintuitive and will be changed in the next major release. The replacement 
     * affineTransform (currently part of CLIJx) will apply inverted transforms compared to this operation.
     * Individual transforms must be separated by spaces.
     * Supported transforms:
     * 
     * * -center: translate the coordinate origin to the center of the image
     * * center: translate the coordinate origin back to the initial origin
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
     * transform = "-center scale=2 rotate=45 center";
     */
    default boolean affineTransform3D(ClearCLImage arg1, ClearCLImageInterface arg2, net.imglib2.realtransform.AffineTransform3D arg3) {
        if (doTimeTracing()) {recordMethodStart("AffineTransform3D");}
        boolean result = AffineTransform3D.affineTransform3D(getCLIJ2(), arg1, arg2, arg3);
        if (doTimeTracing()) {recordMethodEnd("AffineTransform3D");}
        return result;
    }


    // net.haesleinhuepf.clij2.plugins.ApplyVectorField2D
    //----------------------------------------------------
    /**
     * Deforms an image according to distances provided in the given vector images.
     * 
     *  It is recommended to use 32-bit images for input, output and vector images. 
     */
    default boolean applyVectorField(ClearCLImageInterface source, ClearCLImageInterface vectorX, ClearCLImageInterface vectorY, ClearCLImageInterface destination) {
        if (doTimeTracing()) {recordMethodStart("ApplyVectorField2D");}
        boolean result = ApplyVectorField2D.applyVectorField(getCLIJ2(), source, vectorX, vectorY, destination);
        if (doTimeTracing()) {recordMethodEnd("ApplyVectorField2D");}
        return result;
    }

    /**
     * Deforms an image according to distances provided in the given vector images.
     * 
     *  It is recommended to use 32-bit images for input, output and vector images. 
     */
    default boolean applyVectorField2D(ClearCLImageInterface source, ClearCLImageInterface vectorX, ClearCLImageInterface vectorY, ClearCLImageInterface destination) {
        if (doTimeTracing()) {recordMethodStart("ApplyVectorField2D");}
        boolean result = ApplyVectorField2D.applyVectorField2D(getCLIJ2(), source, vectorX, vectorY, destination);
        if (doTimeTracing()) {recordMethodEnd("ApplyVectorField2D");}
        return result;
    }


    // net.haesleinhuepf.clij2.plugins.ApplyVectorField3D
    //----------------------------------------------------
    /**
     * Deforms an image according to distances provided in the given vector images.
     * 
     *  It is recommended to use 32-bit images for input, output and vector images. 
     */
    default boolean applyVectorField(ClearCLImageInterface arg1, ClearCLImageInterface arg2, ClearCLImageInterface arg3, ClearCLImageInterface arg4, ClearCLImageInterface arg5) {
        if (doTimeTracing()) {recordMethodStart("ApplyVectorField3D");}
        boolean result = ApplyVectorField3D.applyVectorField(getCLIJ2(), arg1, arg2, arg3, arg4, arg5);
        if (doTimeTracing()) {recordMethodEnd("ApplyVectorField3D");}
        return result;
    }

    /**
     * Deforms an image stack according to distances provided in the given vector image stacks.
     * 
     * It is recommended to use 32-bit image stacks for input, output and vector image stacks. 
     */
    default boolean applyVectorField3D(ClearCLImageInterface source, ClearCLImageInterface vectorX, ClearCLImageInterface vectorY, ClearCLImageInterface vectorZ, ClearCLImageInterface destination) {
        if (doTimeTracing()) {recordMethodStart("ApplyVectorField3D");}
        boolean result = ApplyVectorField3D.applyVectorField3D(getCLIJ2(), source, vectorX, vectorY, vectorZ, destination);
        if (doTimeTracing()) {recordMethodEnd("ApplyVectorField3D");}
        return result;
    }


    // net.haesleinhuepf.clij2.plugins.ArgMaximumZProjection
    //----------------------------------------------------
    /**
     * Determines the maximum projection of an image stack along Z.
     * 
     * Furthermore, another 2D image is generated with pixels containing the z-index where the maximum was found (zero based).
     */
    default boolean argMaximumZProjection(ClearCLImageInterface source, ClearCLImageInterface destination_max, ClearCLImageInterface destination_arg_max) {
        if (doTimeTracing()) {recordMethodStart("ArgMaximumZProjection");}
        boolean result = ArgMaximumZProjection.argMaximumZProjection(getCLIJ2(), source, destination_max, destination_arg_max);
        if (doTimeTracing()) {recordMethodEnd("ArgMaximumZProjection");}
        return result;
    }


    // net.haesleinhuepf.clij2.plugins.Histogram
    //----------------------------------------------------
    /**
     * 
     */
    @Deprecated
    default boolean fillHistogram(ClearCLBuffer arg1, ClearCLBuffer arg2, double arg3, double arg4) {
        System.out.println("fillHistogram is deprecated. Check the documentation for a replacement. https://clij.github.io/clij2-doccs/reference");
        if (doTimeTracing()) {recordMethodStart("Histogram");}
        boolean result = Histogram.fillHistogram(getCLIJ2(), arg1, arg2, new Double (arg3).floatValue(), new Double (arg4).floatValue());
        if (doTimeTracing()) {recordMethodEnd("Histogram");}
        return result;
    }

    /**
     * Determines the histogram of a given image.
     * 
     * The histogram image is of dimensions number_of_bins/1/1; a 3D image with height=1 and depth=1. 
     * Histogram bins contain the number of pixels with intensity in this corresponding bin. 
     * The histogram bins are uniformly distributed between given minimum and maximum grey value intensity. 
     * If the flag determine_min_max is set, minimum and maximum intensity will be determined. 
     * When calling this operation many times, it is recommended to determine minimum and maximum intensity 
     * once at the beginning and handing over these values.
     */
    default boolean histogram(ClearCLBuffer arg1, ClearCLBuffer arg2) {
        if (doTimeTracing()) {recordMethodStart("Histogram");}
        boolean result = Histogram.histogram(getCLIJ2(), arg1, arg2);
        if (doTimeTracing()) {recordMethodEnd("Histogram");}
        return result;
    }

    /**
     * Determines the histogram of a given image.
     * 
     * The histogram image is of dimensions number_of_bins/1/1; a 3D image with height=1 and depth=1. 
     * Histogram bins contain the number of pixels with intensity in this corresponding bin. 
     * The histogram bins are uniformly distributed between given minimum and maximum grey value intensity. 
     * If the flag determine_min_max is set, minimum and maximum intensity will be determined. 
     * When calling this operation many times, it is recommended to determine minimum and maximum intensity 
     * once at the beginning and handing over these values.
     */
    default boolean histogram(ClearCLBuffer arg1, ClearCLBuffer arg2, double arg3, double arg4, double arg5, boolean arg6) {
        if (doTimeTracing()) {recordMethodStart("Histogram");}
        boolean result = Histogram.histogram(getCLIJ2(), arg1, arg2, new Double (arg3).intValue(), new Double (arg4).floatValue(), new Double (arg5).floatValue(), arg6);
        if (doTimeTracing()) {recordMethodEnd("Histogram");}
        return result;
    }

    /**
     * Determines the histogram of a given image.
     * 
     * The histogram image is of dimensions number_of_bins/1/1; a 3D image with height=1 and depth=1. 
     * Histogram bins contain the number of pixels with intensity in this corresponding bin. 
     * The histogram bins are uniformly distributed between given minimum and maximum grey value intensity. 
     * If the flag determine_min_max is set, minimum and maximum intensity will be determined. 
     * When calling this operation many times, it is recommended to determine minimum and maximum intensity 
     * once at the beginning and handing over these values.
     */
    default boolean histogram(ClearCLBuffer arg1, ClearCLBuffer arg2, double arg3, double arg4, double arg5, boolean arg6, boolean arg7) {
        if (doTimeTracing()) {recordMethodStart("Histogram");}
        boolean result = Histogram.histogram(getCLIJ2(), arg1, arg2, new Double (arg3).intValue(), new Double (arg4).floatValue(), new Double (arg5).floatValue(), arg6, arg7);
        if (doTimeTracing()) {recordMethodEnd("Histogram");}
        return result;
    }

    /**
     * Determines the histogram of a given image.
     * 
     * The histogram image is of dimensions number_of_bins/1/1; a 3D image with height=1 and depth=1. 
     * Histogram bins contain the number of pixels with intensity in this corresponding bin. 
     * The histogram bins are uniformly distributed between given minimum and maximum grey value intensity. 
     * If the flag determine_min_max is set, minimum and maximum intensity will be determined. 
     * When calling this operation many times, it is recommended to determine minimum and maximum intensity 
     * once at the beginning and handing over these values.
     */
    default float[] histogram(ClearCLBuffer arg1, double arg2, double arg3, double arg4) {
        if (doTimeTracing()) {recordMethodStart("Histogram");}
        float[] result = Histogram.histogram(getCLIJ2(), arg1, new Double (arg2).floatValue(), new Double (arg3).floatValue(), new Double (arg4).intValue());
        if (doTimeTracing()) {recordMethodEnd("Histogram");}
        return result;
    }

    /**
     * Determines the histogram of a given image.
     * 
     * The histogram image is of dimensions number_of_bins/1/1; a 3D image with height=1 and depth=1. 
     * Histogram bins contain the number of pixels with intensity in this corresponding bin. 
     * The histogram bins are uniformly distributed between given minimum and maximum grey value intensity. 
     * If the flag determine_min_max is set, minimum and maximum intensity will be determined. 
     * When calling this operation many times, it is recommended to determine minimum and maximum intensity 
     * once at the beginning and handing over these values.
     */
    default ClearCLBuffer histogram(ClearCLBuffer arg1) {
        if (doTimeTracing()) {recordMethodStart("Histogram");}
        ClearCLBuffer result = Histogram.histogram(getCLIJ2(), arg1);
        if (doTimeTracing()) {recordMethodEnd("Histogram");}
        return result;
    }


    // net.haesleinhuepf.clij2.plugins.AutomaticThreshold
    //----------------------------------------------------
    /**
     * The automatic thresholder utilizes the threshold methods from ImageJ on a histogram determined on 
     * the GPU to create binary images as similar as possible to ImageJ 'Apply Threshold' method.
     * 
     *  Enter one 
     * of these methods in the method text field:
     * [Default, Huang, Intermodes, IsoData, IJ_IsoData, Li, MaxEntropy, Mean, MinError, Minimum, Moments, Otsu, Percentile, RenyiEntropy, Shanbhag, Triangle, Yen]
     */
    default boolean automaticThreshold(ClearCLBuffer input, ClearCLBuffer destination, String method) {
        if (doTimeTracing()) {recordMethodStart("AutomaticThreshold");}
        boolean result = AutomaticThreshold.automaticThreshold(getCLIJ2(), input, destination, method);
        if (doTimeTracing()) {recordMethodEnd("AutomaticThreshold");}
        return result;
    }

    /**
     * The automatic thresholder utilizes the threshold methods from ImageJ on a histogram determined on 
     * the GPU to create binary images as similar as possible to ImageJ 'Apply Threshold' method.
     * 
     *  Enter one 
     * of these methods in the method text field:
     * [Default, Huang, Intermodes, IsoData, IJ_IsoData, Li, MaxEntropy, Mean, MinError, Minimum, Moments, Otsu, Percentile, RenyiEntropy, Shanbhag, Triangle, Yen]
     */
    default boolean automaticThreshold(ClearCLBuffer arg1, ClearCLBuffer arg2, String arg3, double arg4, double arg5, double arg6) {
        if (doTimeTracing()) {recordMethodStart("AutomaticThreshold");}
        boolean result = AutomaticThreshold.automaticThreshold(getCLIJ2(), arg1, arg2, arg3, new Double (arg4).floatValue(), new Double (arg5).floatValue(), new Double (arg6).intValue());
        if (doTimeTracing()) {recordMethodEnd("AutomaticThreshold");}
        return result;
    }


    // net.haesleinhuepf.clij2.plugins.Threshold
    //----------------------------------------------------
    /**
     * Computes a binary image with pixel values 0 and 1. 
     * 
     * All pixel values x of a given input image with 
     * value larger or equal to a given threshold t will be set to 1.
     * 
     * f(x,t) = (1 if (x >= t); (0 otherwise))
     * 
     * This plugin is comparable to setting a raw threshold in ImageJ and using the 'Convert to Mask' menu.
     */
    default boolean threshold(ClearCLImageInterface arg1, ClearCLImageInterface arg2, double arg3) {
        if (doTimeTracing()) {recordMethodStart("Threshold");}
        boolean result = Threshold.threshold(getCLIJ2(), arg1, arg2, new Double (arg3).floatValue());
        if (doTimeTracing()) {recordMethodEnd("Threshold");}
        return result;
    }


    // net.haesleinhuepf.clij2.plugins.BinaryOr
    //----------------------------------------------------
    /**
     * Computes a binary image (containing pixel values 0 and 1) from two images X and Y by connecting pairs of
     * pixels x and y with the binary OR operator |.
     * 
     * All pixel values except 0 in the input images are interpreted as 1.<pre>f(x, y) = x | y</pre>
     * 
     * Parameters
     * ----------
     * operand1 : Image
     *     The first binary input image to be processed.
     * operand2 : Image
     *     The second binary input image to be processed.
     * destination : Image
     *     The output image where results are written into.
     * 
     */
    default boolean binaryOr(ClearCLImageInterface operand1, ClearCLImageInterface operand2, ClearCLImageInterface destination) {
        if (doTimeTracing()) {recordMethodStart("BinaryOr");}
        boolean result = BinaryOr.binaryOr(getCLIJ2(), operand1, operand2, destination);
        if (doTimeTracing()) {recordMethodEnd("BinaryOr");}
        return result;
    }


    // net.haesleinhuepf.clij2.plugins.BinaryAnd
    //----------------------------------------------------
    /**
     * Computes a binary image (containing pixel values 0 and 1) from two images X and Y by connecting pairs of
     * pixels x and y with the binary AND operator &.
     * All pixel values except 0 in the input images are interpreted as 1.
     * 
     * <pre>f(x, y) = x & y</pre>
     * 
     * Parameters
     * ----------
     * operand1 : Image
     *     The first binary input image to be processed.
     * operand2 : Image
     *     The second binary input image to be processed.
     * destination : Image
     *     The output image where results are written into.
     * 
     */
    default boolean binaryAnd(ClearCLImageInterface operand1, ClearCLImageInterface operand2, ClearCLImageInterface destination) {
        if (doTimeTracing()) {recordMethodStart("BinaryAnd");}
        boolean result = BinaryAnd.binaryAnd(getCLIJ2(), operand1, operand2, destination);
        if (doTimeTracing()) {recordMethodEnd("BinaryAnd");}
        return result;
    }


    // net.haesleinhuepf.clij2.plugins.BinaryXOr
    //----------------------------------------------------
    /**
     * Computes a binary image (containing pixel values 0 and 1) from two images X and Y by connecting pairs of
     * pixels x and y with the binary operators AND &, OR | and NOT ! implementing the XOR operator.
     * 
     * All pixel values except 0 in the input images are interpreted as 1.
     * 
     * <pre>f(x, y) = (x & !y) | (!x & y)</pre>
     * 
     * Parameters
     * ----------
     * operand1 : Image
     *     The first binary input image to be processed.
     * operand2 : Image
     *     The second binary input image to be processed.
     * destination : Image
     *     The output image where results are written into.
     * 
     */
    default boolean binaryXOr(ClearCLImageInterface operand1, ClearCLImageInterface operand2, ClearCLImageInterface destination) {
        if (doTimeTracing()) {recordMethodStart("BinaryXOr");}
        boolean result = BinaryXOr.binaryXOr(getCLIJ2(), operand1, operand2, destination);
        if (doTimeTracing()) {recordMethodEnd("BinaryXOr");}
        return result;
    }


    // net.haesleinhuepf.clij2.plugins.BinaryNot
    //----------------------------------------------------
    /**
     * Computes a binary image (containing pixel values 0 and 1) from an image X by negating its pixel values
     * x using the binary NOT operator !
     * 
     * All pixel values except 0 in the input image are interpreted as 1.
     * 
     * <pre>f(x) = !x</pre>
     * 
     * Parameters
     * ----------
     * source : Image
     *     The binary input image to be inverted.
     * destination : Image
     *     The output image where results are written into.
     * 
     */
    default boolean binaryNot(ClearCLImageInterface source, ClearCLImageInterface destination) {
        if (doTimeTracing()) {recordMethodStart("BinaryNot");}
        boolean result = BinaryNot.binaryNot(getCLIJ2(), source, destination);
        if (doTimeTracing()) {recordMethodEnd("BinaryNot");}
        return result;
    }


    // net.haesleinhuepf.clij2.plugins.ErodeSphere
    //----------------------------------------------------
    /**
     * Computes a binary image with pixel values 0 and 1 containing the binary erosion of a given input image. 
     * 
     * The erosion takes the von-Neumann-neighborhood (4 pixels in 2D and 6 pixels in 3d) into account.
     * The pixels in the input image with pixel value not equal to 0 will be interpreted as 1.
     */
    default boolean erodeSphere(ClearCLImageInterface source, ClearCLImageInterface destination) {
        if (doTimeTracing()) {recordMethodStart("ErodeSphere");}
        boolean result = ErodeSphere.erodeSphere(getCLIJ2(), source, destination);
        if (doTimeTracing()) {recordMethodEnd("ErodeSphere");}
        return result;
    }


    // net.haesleinhuepf.clij2.plugins.ErodeBox
    //----------------------------------------------------
    /**
     * Computes a binary image with pixel values 0 and 1 containing the binary erosion of a given input image. 
     * 
     * The erosion takes the Moore-neighborhood (8 pixels in 2D and 26 pixels in 3d) into account.
     * The pixels in the input image with pixel value not equal to 0 will be interpreted as 1.
     * 
     * This method is comparable to the 'Erode' menu in ImageJ in case it is applied to a 2D image. The only
     * difference is that the output image contains values 0 and 1 instead of 0 and 255.
     */
    default boolean erodeBox(ClearCLImageInterface source, ClearCLImageInterface destination) {
        if (doTimeTracing()) {recordMethodStart("ErodeBox");}
        boolean result = ErodeBox.erodeBox(getCLIJ2(), source, destination);
        if (doTimeTracing()) {recordMethodEnd("ErodeBox");}
        return result;
    }


    // net.haesleinhuepf.clij2.plugins.ErodeSphereSliceBySlice
    //----------------------------------------------------
    /**
     * Computes a binary image with pixel values 0 and 1 containing the binary erosion of a given input image. 
     * 
     * The erosion takes the von-Neumann-neighborhood (4 pixels in 2D and 6 pixels in 3d) into account.
     * The pixels in the input image with pixel value not equal to 0 will be interpreted as 1.
     * 
     * This filter is applied slice by slice in 2D.
     */
    default boolean erodeSphereSliceBySlice(ClearCLImageInterface source, ClearCLImageInterface destination) {
        if (doTimeTracing()) {recordMethodStart("ErodeSphereSliceBySlice");}
        boolean result = ErodeSphereSliceBySlice.erodeSphereSliceBySlice(getCLIJ2(), source, destination);
        if (doTimeTracing()) {recordMethodEnd("ErodeSphereSliceBySlice");}
        return result;
    }


    // net.haesleinhuepf.clij2.plugins.ErodeBoxSliceBySlice
    //----------------------------------------------------
    /**
     * Computes a binary image with pixel values 0 and 1 containing the binary erosion of a given input image. 
     * 
     * The erosion takes the Moore-neighborhood (8 pixels in 2D and 26 pixels in 3d) into account.
     * The pixels in the input image with pixel value not equal to 0 will be interpreted as 1.
     * 
     * This method is comparable to the 'Erode' menu in ImageJ in case it is applied to a 2D image. The only
     * difference is that the output image contains values 0 and 1 instead of 0 and 255.
     * 
     * This filter is applied slice by slice in 2D.
     */
    default boolean erodeBoxSliceBySlice(ClearCLImageInterface source, ClearCLImageInterface destination) {
        if (doTimeTracing()) {recordMethodStart("ErodeBoxSliceBySlice");}
        boolean result = ErodeBoxSliceBySlice.erodeBoxSliceBySlice(getCLIJ2(), source, destination);
        if (doTimeTracing()) {recordMethodEnd("ErodeBoxSliceBySlice");}
        return result;
    }


    // net.haesleinhuepf.clij2.plugins.DilateSphere
    //----------------------------------------------------
    /**
     * Computes a binary image with pixel values 0 and 1 containing the binary dilation of a given input image.
     * 
     * The dilation takes the von-Neumann-neighborhood (4 pixels in 2D and 6 pixels in 3d) into account.
     * The pixels in the input image with pixel value not equal to 0 will be interpreted as 1.
     */
    default boolean dilateSphere(ClearCLImageInterface source, ClearCLImageInterface destination) {
        if (doTimeTracing()) {recordMethodStart("DilateSphere");}
        boolean result = DilateSphere.dilateSphere(getCLIJ2(), source, destination);
        if (doTimeTracing()) {recordMethodEnd("DilateSphere");}
        return result;
    }


    // net.haesleinhuepf.clij2.plugins.DilateBox
    //----------------------------------------------------
    /**
     * Computes a binary image with pixel values 0 and 1 containing the binary dilation of a given input image.
     * 
     * The dilation takes the Moore-neighborhood (8 pixels in 2D and 26 pixels in 3d) into account.
     * The pixels in the input image with pixel value not equal to 0 will be interpreted as 1.
     * 
     * This method is comparable to the 'Dilate' menu in ImageJ in case it is applied to a 2D image. The only
     * difference is that the output image contains values 0 and 1 instead of 0 and 255.
     */
    default boolean dilateBox(ClearCLImageInterface source, ClearCLImageInterface destination) {
        if (doTimeTracing()) {recordMethodStart("DilateBox");}
        boolean result = DilateBox.dilateBox(getCLIJ2(), source, destination);
        if (doTimeTracing()) {recordMethodEnd("DilateBox");}
        return result;
    }


    // net.haesleinhuepf.clij2.plugins.DilateSphereSliceBySlice
    //----------------------------------------------------
    /**
     * Computes a binary image with pixel values 0 and 1 containing the binary dilation of a given input image.
     * 
     * The dilation takes the von-Neumann-neighborhood (4 pixels in 2D and 6 pixels in 3d) into account.
     * The pixels in the input image with pixel value not equal to 0 will be interpreted as 1.
     * 
     * This filter is applied slice by slice in 2D.
     */
    default boolean dilateSphereSliceBySlice(ClearCLImageInterface source, ClearCLImageInterface destination) {
        if (doTimeTracing()) {recordMethodStart("DilateSphereSliceBySlice");}
        boolean result = DilateSphereSliceBySlice.dilateSphereSliceBySlice(getCLIJ2(), source, destination);
        if (doTimeTracing()) {recordMethodEnd("DilateSphereSliceBySlice");}
        return result;
    }


    // net.haesleinhuepf.clij2.plugins.DilateBoxSliceBySlice
    //----------------------------------------------------
    /**
     * Computes a binary image with pixel values 0 and 1 containing the binary dilation of a given input image.
     * 
     * The dilation takes the Moore-neighborhood (8 pixels in 2D and 26 pixels in 3d) into account.
     * The pixels in the input image with pixel value not equal to 0 will be interpreted as 1.
     * 
     * This method is comparable to the 'Dilate' menu in ImageJ in case it is applied to a 2D image. The only
     * difference is that the output image contains values 0 and 1 instead of 0 and 255.
     * 
     * This filter is applied slice by slice in 2D.
     */
    default boolean dilateBoxSliceBySlice(ClearCLImageInterface source, ClearCLImageInterface destination) {
        if (doTimeTracing()) {recordMethodStart("DilateBoxSliceBySlice");}
        boolean result = DilateBoxSliceBySlice.dilateBoxSliceBySlice(getCLIJ2(), source, destination);
        if (doTimeTracing()) {recordMethodEnd("DilateBoxSliceBySlice");}
        return result;
    }


    // net.haesleinhuepf.clij2.plugins.Copy
    //----------------------------------------------------
    /**
     * Copies an image.
     * 
     * <pre>f(x) = x</pre>
     */
    default boolean copy(ClearCLImageInterface source, ClearCLImageInterface destination) {
        if (doTimeTracing()) {recordMethodStart("Copy");}
        boolean result = Copy.copy(getCLIJ2(), source, destination);
        if (doTimeTracing()) {recordMethodEnd("Copy");}
        return result;
    }


    // net.haesleinhuepf.clij2.plugins.CopySlice
    //----------------------------------------------------
    /**
     * This method has two purposes: 
     * It copies a 2D image to a given slice z position in a 3D image stack or 
     * It copies a given slice at position z in an image stack to a 2D image.
     * 
     * The first case is only available via ImageJ macro. If you are using it, it is recommended that the 
     * target 3D image already pre-exists in GPU memory before calling this method. Otherwise, CLIJ create 
     * the image stack with z planes.
     */
    default boolean copySlice(ClearCLImageInterface arg1, ClearCLImageInterface arg2, double arg3) {
        if (doTimeTracing()) {recordMethodStart("CopySlice");}
        boolean result = CopySlice.copySlice(getCLIJ2(), arg1, arg2, new Double (arg3).intValue());
        if (doTimeTracing()) {recordMethodEnd("CopySlice");}
        return result;
    }


    // net.haesleinhuepf.clij2.plugins.Crop2D
    //----------------------------------------------------
    /**
     * Crops a given rectangle out of a given image. 
     * 
     * Note: If the destination image pre-exists already, it will be overwritten and keep it's dimensions.
     */
    default boolean crop(ClearCLImageInterface arg1, ClearCLImageInterface arg2, double arg3, double arg4) {
        if (doTimeTracing()) {recordMethodStart("Crop2D");}
        boolean result = Crop2D.crop(getCLIJ2(), arg1, arg2, new Double (arg3).intValue(), new Double (arg4).intValue());
        if (doTimeTracing()) {recordMethodEnd("Crop2D");}
        return result;
    }

    /**
     * Crops a given rectangle out of a given image. 
     * 
     * Note: If the destination image pre-exists already, it will be overwritten and keep it's dimensions.
     */
    default boolean crop2D(ClearCLImageInterface arg1, ClearCLImageInterface arg2, double arg3, double arg4) {
        if (doTimeTracing()) {recordMethodStart("Crop2D");}
        boolean result = Crop2D.crop2D(getCLIJ2(), arg1, arg2, new Double (arg3).intValue(), new Double (arg4).intValue());
        if (doTimeTracing()) {recordMethodEnd("Crop2D");}
        return result;
    }


    // net.haesleinhuepf.clij2.plugins.Crop3D
    //----------------------------------------------------
    /**
     * Crops a given rectangle out of a given image. 
     * 
     * Note: If the destination image pre-exists already, it will be overwritten and keep it's dimensions.
     */
    default boolean crop(ClearCLImageInterface arg1, ClearCLImageInterface arg2, double arg3, double arg4, double arg5) {
        if (doTimeTracing()) {recordMethodStart("Crop3D");}
        boolean result = Crop3D.crop(getCLIJ2(), arg1, arg2, new Double (arg3).intValue(), new Double (arg4).intValue(), new Double (arg5).intValue());
        if (doTimeTracing()) {recordMethodEnd("Crop3D");}
        return result;
    }

    /**
     * Crops a given sub-stack out of a given image stack. 
     * 
     * Note: If the destination image pre-exists already, it will be overwritten and keep it's dimensions.
     */
    default boolean crop3D(ClearCLImageInterface arg1, ClearCLImageInterface arg2, double arg3, double arg4, double arg5) {
        if (doTimeTracing()) {recordMethodStart("Crop3D");}
        boolean result = Crop3D.crop3D(getCLIJ2(), arg1, arg2, new Double (arg3).intValue(), new Double (arg4).intValue(), new Double (arg5).intValue());
        if (doTimeTracing()) {recordMethodEnd("Crop3D");}
        return result;
    }


    // net.haesleinhuepf.clij2.plugins.Set
    //----------------------------------------------------
    /**
     * Sets all pixel values x of a given image X to a constant value v.
     * 
     * <pre>f(x) = v</pre>
     */
    default boolean set(ClearCLImageInterface arg1, double arg2) {
        if (doTimeTracing()) {recordMethodStart("Set");}
        boolean result = Set.set(getCLIJ2(), arg1, new Double (arg2).floatValue());
        if (doTimeTracing()) {recordMethodEnd("Set");}
        return result;
    }


    // net.haesleinhuepf.clij2.plugins.Flip2D
    //----------------------------------------------------
    /**
     * Flips an image in X and/or Y direction depending on boolean flags.
     */
    default boolean flip(ClearCLImageInterface arg1, ClearCLImageInterface arg2, boolean arg3, boolean arg4) {
        if (doTimeTracing()) {recordMethodStart("Flip2D");}
        boolean result = Flip2D.flip(getCLIJ2(), arg1, arg2, arg3, arg4);
        if (doTimeTracing()) {recordMethodEnd("Flip2D");}
        return result;
    }

    /**
     * Flips an image in X and/or Y direction depending on boolean flags.
     */
    default boolean flip2D(ClearCLImageInterface arg1, ClearCLImageInterface arg2, boolean arg3, boolean arg4) {
        if (doTimeTracing()) {recordMethodStart("Flip2D");}
        boolean result = Flip2D.flip2D(getCLIJ2(), arg1, arg2, arg3, arg4);
        if (doTimeTracing()) {recordMethodEnd("Flip2D");}
        return result;
    }


    // net.haesleinhuepf.clij2.plugins.Flip3D
    //----------------------------------------------------
    /**
     * Flips an image in X and/or Y direction depending on boolean flags.
     */
    default boolean flip(ClearCLImageInterface arg1, ClearCLImageInterface arg2, boolean arg3, boolean arg4, boolean arg5) {
        if (doTimeTracing()) {recordMethodStart("Flip3D");}
        boolean result = Flip3D.flip(getCLIJ2(), arg1, arg2, arg3, arg4, arg5);
        if (doTimeTracing()) {recordMethodEnd("Flip3D");}
        return result;
    }

    /**
     * Flips an image in X, Y and/or Z direction depending on boolean flags.
     */
    default boolean flip3D(ClearCLImageInterface arg1, ClearCLImageInterface arg2, boolean arg3, boolean arg4, boolean arg5) {
        if (doTimeTracing()) {recordMethodStart("Flip3D");}
        boolean result = Flip3D.flip3D(getCLIJ2(), arg1, arg2, arg3, arg4, arg5);
        if (doTimeTracing()) {recordMethodEnd("Flip3D");}
        return result;
    }


    // net.haesleinhuepf.clij2.plugins.RotateCounterClockwise
    //----------------------------------------------------
    /**
     * Rotates a given input image by 90 degrees counter-clockwise. 
     * 
     * For that, X and Y axis of an image stack
     * are flipped. This operation is similar to ImageJs 'Reslice [/]' method but offers less flexibility 
     * such as interpolation.
     */
    default boolean rotateCounterClockwise(ClearCLImageInterface source, ClearCLImageInterface destination) {
        if (doTimeTracing()) {recordMethodStart("RotateCounterClockwise");}
        boolean result = RotateCounterClockwise.rotateCounterClockwise(getCLIJ2(), source, destination);
        if (doTimeTracing()) {recordMethodEnd("RotateCounterClockwise");}
        return result;
    }

    /**
     * Rotates a given input image by 90 degrees counter-clockwise. 
     * 
     * For that, X and Y axis of an image stack
     * are flipped. This operation is similar to ImageJs 'Reslice [/]' method but offers less flexibility 
     * such as interpolation.
     */
    @Deprecated
    default boolean rotateLeft(ClearCLImageInterface source, ClearCLImageInterface destination) {
        System.out.println("rotateLeft is deprecated. Check the documentation for a replacement. https://clij.github.io/clij2-doccs/reference");
        if (doTimeTracing()) {recordMethodStart("RotateCounterClockwise");}
        boolean result = RotateCounterClockwise.rotateLeft(getCLIJ2(), source, destination);
        if (doTimeTracing()) {recordMethodEnd("RotateCounterClockwise");}
        return result;
    }


    // net.haesleinhuepf.clij2.plugins.RotateClockwise
    //----------------------------------------------------
    /**
     * Rotates a given input image by 90 degrees clockwise. 
     * 
     * For that, X and Y axis of an image stack
     * are flipped. This operation is similar to ImageJs 'Reslice [/]' method but offers less flexibility 
     * such as interpolation.
     */
    default boolean rotateClockwise(ClearCLImageInterface source, ClearCLImageInterface destination) {
        if (doTimeTracing()) {recordMethodStart("RotateClockwise");}
        boolean result = RotateClockwise.rotateClockwise(getCLIJ2(), source, destination);
        if (doTimeTracing()) {recordMethodEnd("RotateClockwise");}
        return result;
    }

    /**
     * Rotates a given input image by 90 degrees counter-clockwise. 
     * 
     * For that, X and Y axis of an image stack
     * are flipped. This operation is similar to ImageJs 'Reslice [/]' method but offers less flexibility 
     * such as interpolation.
     */
    @Deprecated
    default boolean rotateRight(ClearCLImageInterface source, ClearCLImageInterface destination) {
        System.out.println("rotateRight is deprecated. Check the documentation for a replacement. https://clij.github.io/clij2-doccs/reference");
        if (doTimeTracing()) {recordMethodStart("RotateClockwise");}
        boolean result = RotateClockwise.rotateRight(getCLIJ2(), source, destination);
        if (doTimeTracing()) {recordMethodEnd("RotateClockwise");}
        return result;
    }


    // net.haesleinhuepf.clij2.plugins.Mask
    //----------------------------------------------------
    /**
     * Computes a masked image by applying a binary mask to an image. 
     * 
     * All pixel values x of image X will be copied
     * to the destination image in case pixel value m at the same position in the mask image is not equal to 
     * zero.
     * 
     * <pre>f(x,m) = (x if (m != 0); (0 otherwise))</pre>
     */
    default boolean mask(ClearCLImageInterface source, ClearCLImageInterface mask, ClearCLImageInterface destination) {
        if (doTimeTracing()) {recordMethodStart("Mask");}
        boolean result = Mask.mask(getCLIJ2(), source, mask, destination);
        if (doTimeTracing()) {recordMethodEnd("Mask");}
        return result;
    }


    // net.haesleinhuepf.clij2.plugins.MaskStackWithPlane
    //----------------------------------------------------
    /**
     * Computes a masked image by applying a binary 2D mask to an image stack. 
     * 
     * All pixel values x of image X will be copied
     * to the destination image in case pixel value m at the same spatial position in the mask image is not equal to 
     * zero.
     * 
     * <pre>f(x,m) = (x if (m != 0); (0 otherwise))</pre>
     */
    default boolean maskStackWithPlane(ClearCLImageInterface source, ClearCLImageInterface mask, ClearCLImageInterface destination) {
        if (doTimeTracing()) {recordMethodStart("MaskStackWithPlane");}
        boolean result = MaskStackWithPlane.maskStackWithPlane(getCLIJ2(), source, mask, destination);
        if (doTimeTracing()) {recordMethodEnd("MaskStackWithPlane");}
        return result;
    }


    // net.haesleinhuepf.clij2.plugins.MaximumZProjection
    //----------------------------------------------------
    /**
     * Determines the maximum intensity projection of an image along Z.
     */
    default boolean maximumZProjection(ClearCLImageInterface source, ClearCLImageInterface destination_max) {
        if (doTimeTracing()) {recordMethodStart("MaximumZProjection");}
        boolean result = MaximumZProjection.maximumZProjection(getCLIJ2(), source, destination_max);
        if (doTimeTracing()) {recordMethodEnd("MaximumZProjection");}
        return result;
    }


    // net.haesleinhuepf.clij2.plugins.MeanZProjection
    //----------------------------------------------------
    /**
     * Determines the mean average intensity projection of an image along Z.
     */
    default boolean meanZProjection(ClearCLImageInterface source, ClearCLImageInterface destination) {
        if (doTimeTracing()) {recordMethodStart("MeanZProjection");}
        boolean result = MeanZProjection.meanZProjection(getCLIJ2(), source, destination);
        if (doTimeTracing()) {recordMethodEnd("MeanZProjection");}
        return result;
    }


    // net.haesleinhuepf.clij2.plugins.MinimumZProjection
    //----------------------------------------------------
    /**
     * Determines the minimum intensity projection of an image along Z.
     */
    default boolean minimumZProjection(ClearCLImageInterface source, ClearCLImageInterface destination_min) {
        if (doTimeTracing()) {recordMethodStart("MinimumZProjection");}
        boolean result = MinimumZProjection.minimumZProjection(getCLIJ2(), source, destination_min);
        if (doTimeTracing()) {recordMethodEnd("MinimumZProjection");}
        return result;
    }


    // net.haesleinhuepf.clij2.plugins.Power
    //----------------------------------------------------
    /**
     * Computes all pixels value x to the power of a given exponent a.
     * 
     * <pre>f(x, a) = x ^ a</pre>
     */
    default boolean power(ClearCLImageInterface arg1, ClearCLImageInterface arg2, double arg3) {
        if (doTimeTracing()) {recordMethodStart("Power");}
        boolean result = Power.power(getCLIJ2(), arg1, arg2, new Double (arg3).floatValue());
        if (doTimeTracing()) {recordMethodEnd("Power");}
        return result;
    }


    // net.haesleinhuepf.clij2.plugins.DivideImages
    //----------------------------------------------------
    /**
     * Divides two images X and Y by each other pixel wise. 
     * 
     * <pre>f(x, y) = x / y</pre>
     */
    default boolean divideImages(ClearCLImageInterface divident, ClearCLImageInterface divisor, ClearCLImageInterface destination) {
        if (doTimeTracing()) {recordMethodStart("DivideImages");}
        boolean result = DivideImages.divideImages(getCLIJ2(), divident, divisor, destination);
        if (doTimeTracing()) {recordMethodEnd("DivideImages");}
        return result;
    }


    // net.haesleinhuepf.clij2.plugins.MaximumImages
    //----------------------------------------------------
    /**
     * Computes the maximum of a pair of pixel values x, y from two given images X and Y. 
     * 
     * <pre>f(x, y) = max(x, y)</pre>
     */
    default boolean maximumImages(ClearCLImageInterface source1, ClearCLImageInterface source2, ClearCLImageInterface destination) {
        if (doTimeTracing()) {recordMethodStart("MaximumImages");}
        boolean result = MaximumImages.maximumImages(getCLIJ2(), source1, source2, destination);
        if (doTimeTracing()) {recordMethodEnd("MaximumImages");}
        return result;
    }


    // net.haesleinhuepf.clij2.plugins.MaximumImageAndScalar
    //----------------------------------------------------
    /**
     * Computes the maximum of a constant scalar s and each pixel value x in a given image X. 
     * 
     * <pre>f(x, s) = max(x, s)</pre>
     */
    default boolean maximumImageAndScalar(ClearCLImageInterface arg1, ClearCLImageInterface arg2, double arg3) {
        if (doTimeTracing()) {recordMethodStart("MaximumImageAndScalar");}
        boolean result = MaximumImageAndScalar.maximumImageAndScalar(getCLIJ2(), arg1, arg2, new Double (arg3).floatValue());
        if (doTimeTracing()) {recordMethodEnd("MaximumImageAndScalar");}
        return result;
    }


    // net.haesleinhuepf.clij2.plugins.MinimumImages
    //----------------------------------------------------
    /**
     * Computes the minimum of a pair of pixel values x, y from two given images X and Y.
     * 
     * <pre>f(x, y) = min(x, y)</pre>
     */
    default boolean minimumImages(ClearCLImageInterface source1, ClearCLImageInterface source2, ClearCLImageInterface destination) {
        if (doTimeTracing()) {recordMethodStart("MinimumImages");}
        boolean result = MinimumImages.minimumImages(getCLIJ2(), source1, source2, destination);
        if (doTimeTracing()) {recordMethodEnd("MinimumImages");}
        return result;
    }


    // net.haesleinhuepf.clij2.plugins.MinimumImageAndScalar
    //----------------------------------------------------
    /**
     * Computes the minimum of a constant scalar s and each pixel value x in a given image X.
     * 
     * <pre>f(x, s) = min(x, s)</pre>
     */
    default boolean minimumImageAndScalar(ClearCLImageInterface arg1, ClearCLImageInterface arg2, double arg3) {
        if (doTimeTracing()) {recordMethodStart("MinimumImageAndScalar");}
        boolean result = MinimumImageAndScalar.minimumImageAndScalar(getCLIJ2(), arg1, arg2, new Double (arg3).floatValue());
        if (doTimeTracing()) {recordMethodEnd("MinimumImageAndScalar");}
        return result;
    }


    // net.haesleinhuepf.clij2.plugins.MultiplyImageAndScalar
    //----------------------------------------------------
    /**
     * Multiplies all pixels value x in a given image X with a constant scalar s.
     * 
     * <pre>f(x, s) = x * s</pre>
     * 
     * Parameters
     * ----------
     * source : Image
     *     The input image to be multiplied with a constant.
     * destination : Image
     *     The output image where results are written into.
     * scalar : float
     *     The number with which every pixel will be multiplied with.
     * 
     */
    default boolean multiplyImageAndScalar(ClearCLImageInterface arg1, ClearCLImageInterface arg2, double arg3) {
        if (doTimeTracing()) {recordMethodStart("MultiplyImageAndScalar");}
        boolean result = MultiplyImageAndScalar.multiplyImageAndScalar(getCLIJ2(), arg1, arg2, new Double (arg3).floatValue());
        if (doTimeTracing()) {recordMethodEnd("MultiplyImageAndScalar");}
        return result;
    }


    // net.haesleinhuepf.clij2.plugins.MultiplyStackWithPlane
    //----------------------------------------------------
    /**
     * Multiplies all pairs of pixel values x and y from an image stack X and a 2D image Y. 
     * 
     * x and y are at 
     * the same spatial position within a plane.
     * 
     * <pre>f(x, y) = x * y</pre>
     */
    default boolean multiplyStackWithPlane(ClearCLImageInterface sourceStack, ClearCLImageInterface sourcePlane, ClearCLImageInterface destination) {
        if (doTimeTracing()) {recordMethodStart("MultiplyStackWithPlane");}
        boolean result = MultiplyStackWithPlane.multiplyStackWithPlane(getCLIJ2(), sourceStack, sourcePlane, destination);
        if (doTimeTracing()) {recordMethodEnd("MultiplyStackWithPlane");}
        return result;
    }


    // net.haesleinhuepf.clij2.plugins.CountNonZeroPixels2DSphere
    //----------------------------------------------------
    /**
     * Counts non-zero pixels in a sphere around every pixel. 
     * 
     * Put the number in the result image.
     */
    default boolean countNonZeroPixels2DSphere(ClearCLBuffer arg1, ClearCLBuffer arg2, double arg3, double arg4) {
        if (doTimeTracing()) {recordMethodStart("CountNonZeroPixels2DSphere");}
        boolean result = CountNonZeroPixels2DSphere.countNonZeroPixels2DSphere(getCLIJ2(), arg1, arg2, new Double (arg3).intValue(), new Double (arg4).intValue());
        if (doTimeTracing()) {recordMethodEnd("CountNonZeroPixels2DSphere");}
        return result;
    }

    /**
     * 
     */
    @Deprecated
    default boolean countNonZeroPixelsLocally(ClearCLBuffer arg1, ClearCLBuffer arg2, double arg3, double arg4) {
        System.out.println("countNonZeroPixelsLocally is deprecated. Check the documentation for a replacement. https://clij.github.io/clij2-doccs/reference");
        if (doTimeTracing()) {recordMethodStart("CountNonZeroPixels2DSphere");}
        boolean result = CountNonZeroPixels2DSphere.countNonZeroPixelsLocally(getCLIJ2(), arg1, arg2, new Double (arg3).intValue(), new Double (arg4).intValue());
        if (doTimeTracing()) {recordMethodEnd("CountNonZeroPixels2DSphere");}
        return result;
    }


    // net.haesleinhuepf.clij2.plugins.CountNonZeroPixelsSliceBySliceSphere
    //----------------------------------------------------
    /**
     * 
     */
    @Deprecated
    default boolean countNonZeroPixelsLocallySliceBySlice(ClearCLBuffer arg1, ClearCLBuffer arg2, double arg3, double arg4) {
        System.out.println("countNonZeroPixelsLocallySliceBySlice is deprecated. Check the documentation for a replacement. https://clij.github.io/clij2-doccs/reference");
        if (doTimeTracing()) {recordMethodStart("CountNonZeroPixelsSliceBySliceSphere");}
        boolean result = CountNonZeroPixelsSliceBySliceSphere.countNonZeroPixelsLocallySliceBySlice(getCLIJ2(), arg1, arg2, new Double (arg3).intValue(), new Double (arg4).intValue());
        if (doTimeTracing()) {recordMethodEnd("CountNonZeroPixelsSliceBySliceSphere");}
        return result;
    }

    /**
     * Counts non-zero pixels in a sphere around every pixel slice by slice in a stack. 
     * 
     *  It puts the resulting number in the destination image stack.
     */
    default boolean countNonZeroPixelsSliceBySliceSphere(ClearCLBuffer arg1, ClearCLBuffer arg2, double arg3, double arg4) {
        if (doTimeTracing()) {recordMethodStart("CountNonZeroPixelsSliceBySliceSphere");}
        boolean result = CountNonZeroPixelsSliceBySliceSphere.countNonZeroPixelsSliceBySliceSphere(getCLIJ2(), arg1, arg2, new Double (arg3).intValue(), new Double (arg4).intValue());
        if (doTimeTracing()) {recordMethodEnd("CountNonZeroPixelsSliceBySliceSphere");}
        return result;
    }


    // net.haesleinhuepf.clij2.plugins.CountNonZeroVoxels3DSphere
    //----------------------------------------------------
    /**
     * Counts non-zero voxels in a sphere around every voxel. 
     * 
     * Put the number in the result image.
     */
    default boolean countNonZeroVoxels3DSphere(ClearCLBuffer arg1, ClearCLBuffer arg2, double arg3, double arg4, double arg5) {
        if (doTimeTracing()) {recordMethodStart("CountNonZeroVoxels3DSphere");}
        boolean result = CountNonZeroVoxels3DSphere.countNonZeroVoxels3DSphere(getCLIJ2(), arg1, arg2, new Double (arg3).intValue(), new Double (arg4).intValue(), new Double (arg5).intValue());
        if (doTimeTracing()) {recordMethodEnd("CountNonZeroVoxels3DSphere");}
        return result;
    }

    /**
     * 
     */
    @Deprecated
    default boolean countNonZeroVoxelsLocally(ClearCLBuffer arg1, ClearCLBuffer arg2, double arg3, double arg4, double arg5) {
        System.out.println("countNonZeroVoxelsLocally is deprecated. Check the documentation for a replacement. https://clij.github.io/clij2-doccs/reference");
        if (doTimeTracing()) {recordMethodStart("CountNonZeroVoxels3DSphere");}
        boolean result = CountNonZeroVoxels3DSphere.countNonZeroVoxelsLocally(getCLIJ2(), arg1, arg2, new Double (arg3).intValue(), new Double (arg4).intValue(), new Double (arg5).intValue());
        if (doTimeTracing()) {recordMethodEnd("CountNonZeroVoxels3DSphere");}
        return result;
    }


    // net.haesleinhuepf.clij2.plugins.SumZProjection
    //----------------------------------------------------
    /**
     * Determines the sum intensity projection of an image along Z.
     */
    default boolean sumZProjection(ClearCLImageInterface source, ClearCLImageInterface destination_sum) {
        if (doTimeTracing()) {recordMethodStart("SumZProjection");}
        boolean result = SumZProjection.sumZProjection(getCLIJ2(), source, destination_sum);
        if (doTimeTracing()) {recordMethodEnd("SumZProjection");}
        return result;
    }


    // net.haesleinhuepf.clij2.plugins.SumOfAllPixels
    //----------------------------------------------------
    /**
     * Determines the sum of all pixels in a given image. 
     * 
     * It will be stored in a new row of ImageJs
     * Results table in the column 'Sum'.
     * 
     * Parameters
     * ----------
     * source : Image
     *     The image of which all pixels or voxels will be summed.
     * 
     */
    default double sumOfAllPixels(ClearCLImageInterface source) {
        if (doTimeTracing()) {recordMethodStart("SumOfAllPixels");}
        double result = SumOfAllPixels.sumOfAllPixels(getCLIJ2(), source);
        if (doTimeTracing()) {recordMethodEnd("SumOfAllPixels");}
        return result;
    }

    /**
     * Determines the sum of all pixels in a given image. 
     * 
     * It will be stored in a new row of ImageJs
     * Results table in the column 'Sum'.
     * 
     * Parameters
     * ----------
     * source : Image
     *     The image of which all pixels or voxels will be summed.
     * 
     */
    @Deprecated
    default double sumPixels(ClearCLImageInterface source) {
        System.out.println("sumPixels is deprecated. Check the documentation for a replacement. https://clij.github.io/clij2-doccs/reference");
        if (doTimeTracing()) {recordMethodStart("SumOfAllPixels");}
        double result = SumOfAllPixels.sumPixels(getCLIJ2(), source);
        if (doTimeTracing()) {recordMethodEnd("SumOfAllPixels");}
        return result;
    }


    // net.haesleinhuepf.clij2.plugins.CenterOfMass
    //----------------------------------------------------
    /**
     * Determines the center of mass of an image or image stack. 
     * 
     * It writes the result in the results table
     * in the columns MassX, MassY and MassZ.
     */
    default double[] centerOfMass(ClearCLBuffer source) {
        if (doTimeTracing()) {recordMethodStart("CenterOfMass");}
        double[] result = CenterOfMass.centerOfMass(getCLIJ2(), source);
        if (doTimeTracing()) {recordMethodEnd("CenterOfMass");}
        return result;
    }


    // net.haesleinhuepf.clij2.plugins.Invert
    //----------------------------------------------------
    /**
     * Computes the negative value of all pixels in a given image. 
     * 
     * It is recommended to convert images to 
     * 32-bit float before applying this operation.
     * 
     * <pre>f(x) = - x</pre>
     * 
     * For binary images, use binaryNot.
     */
    default boolean invert(ClearCLImageInterface source, ClearCLImageInterface destination) {
        if (doTimeTracing()) {recordMethodStart("Invert");}
        boolean result = Invert.invert(getCLIJ2(), source, destination);
        if (doTimeTracing()) {recordMethodEnd("Invert");}
        return result;
    }


    // net.haesleinhuepf.clij2.plugins.Downsample2D
    //----------------------------------------------------
    /**
     * Scales an image using given scaling factors for X and Y dimensions. 
     * 
     * The nearest-neighbor method
     * is applied. In ImageJ the method which is similar is called 'Interpolation method: none'.
     */
    @Deprecated
    default boolean downsample(ClearCLImageInterface arg1, ClearCLImageInterface arg2, double arg3, double arg4) {
        System.out.println("downsample is deprecated. Check the documentation for a replacement. https://clij.github.io/clij2-doccs/reference");
        if (doTimeTracing()) {recordMethodStart("Downsample2D");}
        boolean result = Downsample2D.downsample(getCLIJ2(), arg1, arg2, new Double (arg3).floatValue(), new Double (arg4).floatValue());
        if (doTimeTracing()) {recordMethodEnd("Downsample2D");}
        return result;
    }

    /**
     * Scales an image using given scaling factors for X and Y dimensions. 
     * 
     * The nearest-neighbor method
     * is applied. In ImageJ the method which is similar is called 'Interpolation method: none'.
     */
    @Deprecated
    default boolean downsample2D(ClearCLImageInterface arg1, ClearCLImageInterface arg2, double arg3, double arg4) {
        System.out.println("downsample2D is deprecated. Check the documentation for a replacement. https://clij.github.io/clij2-doccs/reference");
        if (doTimeTracing()) {recordMethodStart("Downsample2D");}
        boolean result = Downsample2D.downsample2D(getCLIJ2(), arg1, arg2, new Double (arg3).floatValue(), new Double (arg4).floatValue());
        if (doTimeTracing()) {recordMethodEnd("Downsample2D");}
        return result;
    }


    // net.haesleinhuepf.clij2.plugins.Downsample3D
    //----------------------------------------------------
    /**
     * Scales an image using given scaling factors for X and Y dimensions. 
     * 
     * The nearest-neighbor method
     * is applied. In ImageJ the method which is similar is called 'Interpolation method: none'.
     */
    @Deprecated
    default boolean downsample(ClearCLImageInterface arg1, ClearCLImageInterface arg2, double arg3, double arg4, double arg5) {
        System.out.println("downsample is deprecated. Check the documentation for a replacement. https://clij.github.io/clij2-doccs/reference");
        if (doTimeTracing()) {recordMethodStart("Downsample3D");}
        boolean result = Downsample3D.downsample(getCLIJ2(), arg1, arg2, new Double (arg3).floatValue(), new Double (arg4).floatValue(), new Double (arg5).floatValue());
        if (doTimeTracing()) {recordMethodEnd("Downsample3D");}
        return result;
    }

    /**
     * Scales an image using given scaling factors for X and Y dimensions. 
     * 
     * The nearest-neighbor method
     * is applied. In ImageJ the method which is similar is called 'Interpolation method: none'.
     */
    @Deprecated
    default boolean downsample3D(ClearCLImageInterface arg1, ClearCLImageInterface arg2, double arg3, double arg4, double arg5) {
        System.out.println("downsample3D is deprecated. Check the documentation for a replacement. https://clij.github.io/clij2-doccs/reference");
        if (doTimeTracing()) {recordMethodStart("Downsample3D");}
        boolean result = Downsample3D.downsample3D(getCLIJ2(), arg1, arg2, new Double (arg3).floatValue(), new Double (arg4).floatValue(), new Double (arg5).floatValue());
        if (doTimeTracing()) {recordMethodEnd("Downsample3D");}
        return result;
    }


    // net.haesleinhuepf.clij2.plugins.DownsampleSliceBySliceHalfMedian
    //----------------------------------------------------
    /**
     * Scales an image using scaling factors 0.5 for X and Y dimensions. The Z dimension stays untouched. 
     * 
     * Thus, each slice is processed separately.
     * The median method is applied. Thus, each pixel value in the destination image equals to the median of
     * four corresponding pixels in the source image.
     */
    default boolean downsampleSliceBySliceHalfMedian(ClearCLImageInterface source, ClearCLImageInterface destination) {
        if (doTimeTracing()) {recordMethodStart("DownsampleSliceBySliceHalfMedian");}
        boolean result = DownsampleSliceBySliceHalfMedian.downsampleSliceBySliceHalfMedian(getCLIJ2(), source, destination);
        if (doTimeTracing()) {recordMethodEnd("DownsampleSliceBySliceHalfMedian");}
        return result;
    }


    // net.haesleinhuepf.clij2.plugins.LocalThreshold
    //----------------------------------------------------
    /**
     * Computes a binary image with pixel values 0 and 1 depending on if a pixel value x in image X 
     * was above of equal to the pixel value m in mask image M.
     * 
     * <pre>f(x) = (1 if (x >=  m)); (0 otherwise)</pre>
     */
    default boolean localThreshold(ClearCLImageInterface source, ClearCLImageInterface localThreshold, ClearCLImageInterface destination) {
        if (doTimeTracing()) {recordMethodStart("LocalThreshold");}
        boolean result = LocalThreshold.localThreshold(getCLIJ2(), source, localThreshold, destination);
        if (doTimeTracing()) {recordMethodEnd("LocalThreshold");}
        return result;
    }


    // net.haesleinhuepf.clij2.plugins.GradientX
    //----------------------------------------------------
    /**
     * Computes the gradient of gray values along X. 
     * 
     * Assuming a, b and c are three adjacent
     *  pixels in X direction. In the target image will be saved as: <pre>b' = c - a;</pre>
     */
    default boolean gradientX(ClearCLBuffer source, ClearCLBuffer destination) {
        if (doTimeTracing()) {recordMethodStart("GradientX");}
        boolean result = GradientX.gradientX(getCLIJ2(), source, destination);
        if (doTimeTracing()) {recordMethodEnd("GradientX");}
        return result;
    }


    // net.haesleinhuepf.clij2.plugins.GradientY
    //----------------------------------------------------
    /**
     * Computes the gradient of gray values along Y. 
     * 
     * Assuming a, b and c are three adjacent
     *  pixels in Y direction. In the target image will be saved as: <pre>b' = c - a;</pre>
     */
    default boolean gradientY(ClearCLBuffer source, ClearCLBuffer destination) {
        if (doTimeTracing()) {recordMethodStart("GradientY");}
        boolean result = GradientY.gradientY(getCLIJ2(), source, destination);
        if (doTimeTracing()) {recordMethodEnd("GradientY");}
        return result;
    }


    // net.haesleinhuepf.clij2.plugins.GradientZ
    //----------------------------------------------------
    /**
     * Computes the gradient of gray values along Z. 
     * 
     * Assuming a, b and c are three adjacent
     *  pixels in Z direction. In the target image will be saved as: <pre>b' = c - a;</pre>
     */
    default boolean gradientZ(ClearCLBuffer source, ClearCLBuffer destination) {
        if (doTimeTracing()) {recordMethodStart("GradientZ");}
        boolean result = GradientZ.gradientZ(getCLIJ2(), source, destination);
        if (doTimeTracing()) {recordMethodEnd("GradientZ");}
        return result;
    }


    // net.haesleinhuepf.clij2.plugins.MultiplyImageAndCoordinate
    //----------------------------------------------------
    /**
     * Multiplies all pixel intensities with the x, y or z coordinate, depending on specified dimension.
     */
    default boolean multiplyImageAndCoordinate(ClearCLImageInterface arg1, ClearCLImageInterface arg2, double arg3) {
        if (doTimeTracing()) {recordMethodStart("MultiplyImageAndCoordinate");}
        boolean result = MultiplyImageAndCoordinate.multiplyImageAndCoordinate(getCLIJ2(), arg1, arg2, new Double (arg3).intValue());
        if (doTimeTracing()) {recordMethodEnd("MultiplyImageAndCoordinate");}
        return result;
    }


    // net.haesleinhuepf.clij2.plugins.Mean2DBox
    //----------------------------------------------------
    /**
     * Computes the local mean average of a pixels rectangular neighborhood. 
     * 
     * The rectangles size is specified by 
     * its half-width and half-height (radius).
     */
    default boolean mean2DBox(ClearCLImageInterface arg1, ClearCLImageInterface arg2, double arg3, double arg4) {
        if (doTimeTracing()) {recordMethodStart("Mean2DBox");}
        boolean result = Mean2DBox.mean2DBox(getCLIJ2(), arg1, arg2, new Double (arg3).intValue(), new Double (arg4).intValue());
        if (doTimeTracing()) {recordMethodEnd("Mean2DBox");}
        return result;
    }


    // net.haesleinhuepf.clij2.plugins.Mean2DSphere
    //----------------------------------------------------
    /**
     * Computes the local mean average of a pixels ellipsoidal neighborhood. 
     * 
     * The ellipses size is specified by 
     * its half-width and half-height (radius).
     */
    default boolean mean2DSphere(ClearCLImageInterface arg1, ClearCLImageInterface arg2, double arg3, double arg4) {
        if (doTimeTracing()) {recordMethodStart("Mean2DSphere");}
        boolean result = Mean2DSphere.mean2DSphere(getCLIJ2(), arg1, arg2, new Double (arg3).intValue(), new Double (arg4).intValue());
        if (doTimeTracing()) {recordMethodEnd("Mean2DSphere");}
        return result;
    }


    // net.haesleinhuepf.clij2.plugins.Mean3DBox
    //----------------------------------------------------
    /**
     * Computes the local mean average of a pixels cube neighborhood. 
     * 
     * The cubes size is specified by 
     * its half-width, half-height and half-depth (radius).
     */
    default boolean mean3DBox(ClearCLImageInterface arg1, ClearCLImageInterface arg2, double arg3, double arg4, double arg5) {
        if (doTimeTracing()) {recordMethodStart("Mean3DBox");}
        boolean result = Mean3DBox.mean3DBox(getCLIJ2(), arg1, arg2, new Double (arg3).intValue(), new Double (arg4).intValue(), new Double (arg5).intValue());
        if (doTimeTracing()) {recordMethodEnd("Mean3DBox");}
        return result;
    }

    /**
     * Computes the local mean average of a pixels rectangular neighborhood. 
     * 
     * The rectangles size is specified by 
     * its half-width and half-height (radius).
     */
    default boolean meanBox(ClearCLImageInterface arg1, ClearCLImageInterface arg2, double arg3, double arg4, double arg5) {
        if (doTimeTracing()) {recordMethodStart("Mean3DBox");}
        boolean result = Mean3DBox.meanBox(getCLIJ2(), arg1, arg2, new Double (arg3).intValue(), new Double (arg4).intValue(), new Double (arg5).intValue());
        if (doTimeTracing()) {recordMethodEnd("Mean3DBox");}
        return result;
    }


    // net.haesleinhuepf.clij2.plugins.Mean3DSphere
    //----------------------------------------------------
    /**
     * Computes the local mean average of a pixels spherical neighborhood. 
     * 
     * The spheres size is specified by 
     * its half-width, half-height and half-depth (radius).
     */
    default boolean mean3DSphere(ClearCLImageInterface arg1, ClearCLImageInterface arg2, double arg3, double arg4, double arg5) {
        if (doTimeTracing()) {recordMethodStart("Mean3DSphere");}
        boolean result = Mean3DSphere.mean3DSphere(getCLIJ2(), arg1, arg2, new Double (arg3).intValue(), new Double (arg4).intValue(), new Double (arg5).intValue());
        if (doTimeTracing()) {recordMethodEnd("Mean3DSphere");}
        return result;
    }


    // net.haesleinhuepf.clij2.plugins.MeanSliceBySliceSphere
    //----------------------------------------------------
    /**
     * Computes the local mean average of a pixels ellipsoidal 2D neighborhood in an image stack 
     * slice by slice. 
     * 
     * The ellipses size is specified by its half-width and half-height (radius).
     * 
     * This filter is applied slice by slice in 2D.
     */
    default boolean meanSliceBySliceSphere(ClearCLImageInterface arg1, ClearCLImageInterface arg2, double arg3, double arg4) {
        if (doTimeTracing()) {recordMethodStart("MeanSliceBySliceSphere");}
        boolean result = MeanSliceBySliceSphere.meanSliceBySliceSphere(getCLIJ2(), arg1, arg2, new Double (arg3).intValue(), new Double (arg4).intValue());
        if (doTimeTracing()) {recordMethodEnd("MeanSliceBySliceSphere");}
        return result;
    }


    // net.haesleinhuepf.clij2.plugins.MeanOfAllPixels
    //----------------------------------------------------
    /**
     * Determines the mean average of all pixels in a given image. 
     * 
     * It will be stored in a new row of ImageJs
     * Results table in the column 'Mean'.Parameters
     * ----------
     * source : Image
     *     The image of which the mean average of all pixels or voxels will be determined.
     * 
     */
    default double meanOfAllPixels(ClearCLImageInterface source) {
        if (doTimeTracing()) {recordMethodStart("MeanOfAllPixels");}
        double result = MeanOfAllPixels.meanOfAllPixels(getCLIJ2(), source);
        if (doTimeTracing()) {recordMethodEnd("MeanOfAllPixels");}
        return result;
    }


    // net.haesleinhuepf.clij2.plugins.Median2DBox
    //----------------------------------------------------
    /**
     * Computes the local median of a pixels rectangular neighborhood. 
     * 
     * The rectangle is specified by 
     * its half-width and half-height (radius).
     * 
     * For technical reasons, the area of the rectangle must have less than 1000 pixels.
     */
    default boolean median2DBox(ClearCLImageInterface arg1, ClearCLImageInterface arg2, double arg3, double arg4) {
        if (doTimeTracing()) {recordMethodStart("Median2DBox");}
        boolean result = Median2DBox.median2DBox(getCLIJ2(), arg1, arg2, new Double (arg3).intValue(), new Double (arg4).intValue());
        if (doTimeTracing()) {recordMethodEnd("Median2DBox");}
        return result;
    }


    // net.haesleinhuepf.clij2.plugins.Median2DSphere
    //----------------------------------------------------
    /**
     * Computes the local median of a pixels ellipsoidal neighborhood. 
     * 
     * The ellipses size is specified by 
     * its half-width and half-height (radius).
     * 
     * For technical reasons, the area of the ellipse must have less than 1000 pixels.
     */
    default boolean median2DSphere(ClearCLImageInterface arg1, ClearCLImageInterface arg2, double arg3, double arg4) {
        if (doTimeTracing()) {recordMethodStart("Median2DSphere");}
        boolean result = Median2DSphere.median2DSphere(getCLIJ2(), arg1, arg2, new Double (arg3).intValue(), new Double (arg4).intValue());
        if (doTimeTracing()) {recordMethodEnd("Median2DSphere");}
        return result;
    }


    // net.haesleinhuepf.clij2.plugins.Median3DBox
    //----------------------------------------------------
    /**
     * Computes the local median of a pixels cuboid neighborhood. 
     * 
     * The cuboid size is specified by 
     * its half-width, half-height and half-depth (radius).
     * 
     * For technical reasons, the volume of the cuboid must contain less than 1000 voxels.
     */
    default boolean median3DBox(ClearCLImageInterface arg1, ClearCLImageInterface arg2, double arg3, double arg4, double arg5) {
        if (doTimeTracing()) {recordMethodStart("Median3DBox");}
        boolean result = Median3DBox.median3DBox(getCLIJ2(), arg1, arg2, new Double (arg3).intValue(), new Double (arg4).intValue(), new Double (arg5).intValue());
        if (doTimeTracing()) {recordMethodEnd("Median3DBox");}
        return result;
    }


    // net.haesleinhuepf.clij2.plugins.Median3DSphere
    //----------------------------------------------------
    /**
     * Computes the local median of a pixels spherical neighborhood. 
     * 
     * The spheres size is specified by 
     * its half-width, half-height and half-depth (radius).
     * 
     * For technical reasons, the volume of the sphere must contain less than 1000 voxels.
     */
    default boolean median3DSphere(ClearCLImageInterface arg1, ClearCLImageInterface arg2, double arg3, double arg4, double arg5) {
        if (doTimeTracing()) {recordMethodStart("Median3DSphere");}
        boolean result = Median3DSphere.median3DSphere(getCLIJ2(), arg1, arg2, new Double (arg3).intValue(), new Double (arg4).intValue(), new Double (arg5).intValue());
        if (doTimeTracing()) {recordMethodEnd("Median3DSphere");}
        return result;
    }


    // net.haesleinhuepf.clij2.plugins.MedianSliceBySliceBox
    //----------------------------------------------------
    /**
     * Computes the local median of a pixels rectangular neighborhood. 
     * 
     * This is done slice-by-slice in a 3D 
     * image stack. The rectangle is specified by its half-width and half-height (radius).
     * 
     * For technical reasons, the area of the rectangle must have less than 1000 pixels.
     */
    default boolean median3DSliceBySliceBox(ClearCLImageInterface arg1, ClearCLImageInterface arg2, double arg3, double arg4) {
        if (doTimeTracing()) {recordMethodStart("MedianSliceBySliceBox");}
        boolean result = MedianSliceBySliceBox.median3DSliceBySliceBox(getCLIJ2(), arg1, arg2, new Double (arg3).intValue(), new Double (arg4).intValue());
        if (doTimeTracing()) {recordMethodEnd("MedianSliceBySliceBox");}
        return result;
    }


    // net.haesleinhuepf.clij2.plugins.MedianSliceBySliceSphere
    //----------------------------------------------------
    /**
     * Computes the local median of a pixels ellipsoidal neighborhood. 
     * 
     * This is done slice-by-slice in a 3D 
     * image stack. The ellipses size is specified by its half-width and half-height (radius).
     * 
     * For technical reasons, the area of the ellipse must have less than 1000 pixels.
     */
    default boolean median3DSliceBySliceSphere(ClearCLImageInterface arg1, ClearCLImageInterface arg2, double arg3, double arg4) {
        if (doTimeTracing()) {recordMethodStart("MedianSliceBySliceSphere");}
        boolean result = MedianSliceBySliceSphere.median3DSliceBySliceSphere(getCLIJ2(), arg1, arg2, new Double (arg3).intValue(), new Double (arg4).intValue());
        if (doTimeTracing()) {recordMethodEnd("MedianSliceBySliceSphere");}
        return result;
    }


    // net.haesleinhuepf.clij2.plugins.Maximum2DSphere
    //----------------------------------------------------
    /**
     * Computes the local maximum of a pixels ellipsoidal neighborhood. 
     * 
     * The ellipses size is specified by 
     * its half-width and half-height (radius).
     */
    default boolean maximum2DSphere(ClearCLImageInterface arg1, ClearCLImageInterface arg2, double arg3, double arg4) {
        if (doTimeTracing()) {recordMethodStart("Maximum2DSphere");}
        boolean result = Maximum2DSphere.maximum2DSphere(getCLIJ2(), arg1, arg2, new Double (arg3).intValue(), new Double (arg4).intValue());
        if (doTimeTracing()) {recordMethodEnd("Maximum2DSphere");}
        return result;
    }


    // net.haesleinhuepf.clij2.plugins.Maximum3DSphere
    //----------------------------------------------------
    /**
     * Computes the local maximum of a pixels spherical neighborhood. 
     * 
     * The spheres size is specified by 
     * its half-width, half-height and half-depth (radius).
     */
    default boolean maximum3DSphere(ClearCLImageInterface arg1, ClearCLImageInterface arg2, double arg3, double arg4, double arg5) {
        if (doTimeTracing()) {recordMethodStart("Maximum3DSphere");}
        boolean result = Maximum3DSphere.maximum3DSphere(getCLIJ2(), arg1, arg2, new Double (arg3).intValue(), new Double (arg4).intValue(), new Double (arg5).intValue());
        if (doTimeTracing()) {recordMethodEnd("Maximum3DSphere");}
        return result;
    }


    // net.haesleinhuepf.clij2.plugins.Maximum2DBox
    //----------------------------------------------------
    /**
     * Computes the local maximum of a pixels rectangular neighborhood. 
     * 
     * The rectangles size is specified by 
     * its half-width and half-height (radius).
     */
    default boolean maximum2DBox(ClearCLImageInterface arg1, ClearCLImageInterface arg2, double arg3, double arg4) {
        if (doTimeTracing()) {recordMethodStart("Maximum2DBox");}
        boolean result = Maximum2DBox.maximum2DBox(getCLIJ2(), arg1, arg2, new Double (arg3).intValue(), new Double (arg4).intValue());
        if (doTimeTracing()) {recordMethodEnd("Maximum2DBox");}
        return result;
    }

    /**
     * Computes the local maximum of a pixels rectangular neighborhood. 
     * 
     * The rectangles size is specified by 
     * its half-width and half-height (radius).
     */
    default boolean maximumBox(ClearCLImageInterface arg1, ClearCLImageInterface arg2, double arg3, double arg4) {
        if (doTimeTracing()) {recordMethodStart("Maximum2DBox");}
        boolean result = Maximum2DBox.maximumBox(getCLIJ2(), arg1, arg2, new Double (arg3).intValue(), new Double (arg4).intValue());
        if (doTimeTracing()) {recordMethodEnd("Maximum2DBox");}
        return result;
    }


    // net.haesleinhuepf.clij2.plugins.Maximum3DBox
    //----------------------------------------------------
    /**
     * Computes the local maximum of a pixels cube neighborhood. 
     * 
     * The cubes size is specified by 
     * its half-width, half-height and half-depth (radius).
     */
    default boolean maximum3DBox(ClearCLImageInterface arg1, ClearCLImageInterface arg2, double arg3, double arg4, double arg5) {
        if (doTimeTracing()) {recordMethodStart("Maximum3DBox");}
        boolean result = Maximum3DBox.maximum3DBox(getCLIJ2(), arg1, arg2, new Double (arg3).intValue(), new Double (arg4).intValue(), new Double (arg5).intValue());
        if (doTimeTracing()) {recordMethodEnd("Maximum3DBox");}
        return result;
    }

    /**
     * Computes the local maximum of a pixels rectangular neighborhood. 
     * 
     * The rectangles size is specified by 
     * its half-width and half-height (radius).
     */
    default boolean maximumBox(ClearCLImageInterface arg1, ClearCLImageInterface arg2, double arg3, double arg4, double arg5) {
        if (doTimeTracing()) {recordMethodStart("Maximum3DBox");}
        boolean result = Maximum3DBox.maximumBox(getCLIJ2(), arg1, arg2, new Double (arg3).intValue(), new Double (arg4).intValue(), new Double (arg5).intValue());
        if (doTimeTracing()) {recordMethodEnd("Maximum3DBox");}
        return result;
    }


    // net.haesleinhuepf.clij2.plugins.MaximumSliceBySliceSphere
    //----------------------------------------------------
    /**
     * Computes the local maximum of a pixels ellipsoidal 2D neighborhood in an image stack slice by slice. 
     * 
     * The ellipses size is specified by its half-width and half-height (radius).
     * 
     * This filter is applied slice by slice in 2D.
     */
    default boolean maximum3DSliceBySliceSphere(ClearCLImageInterface arg1, ClearCLImageInterface arg2, double arg3, double arg4) {
        if (doTimeTracing()) {recordMethodStart("MaximumSliceBySliceSphere");}
        boolean result = MaximumSliceBySliceSphere.maximum3DSliceBySliceSphere(getCLIJ2(), arg1, arg2, new Double (arg3).intValue(), new Double (arg4).intValue());
        if (doTimeTracing()) {recordMethodEnd("MaximumSliceBySliceSphere");}
        return result;
    }


    // net.haesleinhuepf.clij2.plugins.Minimum2DSphere
    //----------------------------------------------------
    /**
     * Computes the local minimum of a pixels ellipsoidal neighborhood. 
     * 
     * The ellipses size is specified by 
     * its half-width and half-height (radius).
     */
    default boolean minimum2DSphere(ClearCLImageInterface arg1, ClearCLImageInterface arg2, double arg3, double arg4) {
        if (doTimeTracing()) {recordMethodStart("Minimum2DSphere");}
        boolean result = Minimum2DSphere.minimum2DSphere(getCLIJ2(), arg1, arg2, new Double (arg3).intValue(), new Double (arg4).intValue());
        if (doTimeTracing()) {recordMethodEnd("Minimum2DSphere");}
        return result;
    }


    // net.haesleinhuepf.clij2.plugins.Minimum3DSphere
    //----------------------------------------------------
    /**
     * Computes the local minimum of a pixels spherical neighborhood. 
     * 
     * The spheres size is specified by 
     * its half-width, half-height and half-depth (radius).
     */
    default boolean minimum3DSphere(ClearCLImageInterface arg1, ClearCLImageInterface arg2, double arg3, double arg4, double arg5) {
        if (doTimeTracing()) {recordMethodStart("Minimum3DSphere");}
        boolean result = Minimum3DSphere.minimum3DSphere(getCLIJ2(), arg1, arg2, new Double (arg3).intValue(), new Double (arg4).intValue(), new Double (arg5).intValue());
        if (doTimeTracing()) {recordMethodEnd("Minimum3DSphere");}
        return result;
    }


    // net.haesleinhuepf.clij2.plugins.Minimum2DBox
    //----------------------------------------------------
    /**
     * Computes the local minimum of a pixels rectangular neighborhood. 
     * 
     * The rectangles size is specified by 
     * its half-width and half-height (radius).
     */
    default boolean minimum2DBox(ClearCLImageInterface arg1, ClearCLImageInterface arg2, double arg3, double arg4) {
        if (doTimeTracing()) {recordMethodStart("Minimum2DBox");}
        boolean result = Minimum2DBox.minimum2DBox(getCLIJ2(), arg1, arg2, new Double (arg3).intValue(), new Double (arg4).intValue());
        if (doTimeTracing()) {recordMethodEnd("Minimum2DBox");}
        return result;
    }

    /**
     * Computes the local minimum of a pixels rectangular neighborhood. 
     * 
     * The rectangles size is specified by 
     * its half-width and half-height (radius).
     */
    default boolean minimumBox(ClearCLImageInterface arg1, ClearCLImageInterface arg2, double arg3, double arg4) {
        if (doTimeTracing()) {recordMethodStart("Minimum2DBox");}
        boolean result = Minimum2DBox.minimumBox(getCLIJ2(), arg1, arg2, new Double (arg3).intValue(), new Double (arg4).intValue());
        if (doTimeTracing()) {recordMethodEnd("Minimum2DBox");}
        return result;
    }


    // net.haesleinhuepf.clij2.plugins.Minimum3DBox
    //----------------------------------------------------
    /**
     * Computes the local minimum of a pixels cube neighborhood. 
     * 
     * The cubes size is specified by 
     * its half-width, half-height and half-depth (radius).
     */
    default boolean minimum3DBox(ClearCLImageInterface arg1, ClearCLImageInterface arg2, double arg3, double arg4, double arg5) {
        if (doTimeTracing()) {recordMethodStart("Minimum3DBox");}
        boolean result = Minimum3DBox.minimum3DBox(getCLIJ2(), arg1, arg2, new Double (arg3).intValue(), new Double (arg4).intValue(), new Double (arg5).intValue());
        if (doTimeTracing()) {recordMethodEnd("Minimum3DBox");}
        return result;
    }

    /**
     * Computes the local minimum of a pixels rectangular neighborhood. 
     * 
     * The rectangles size is specified by 
     * its half-width and half-height (radius).
     */
    default boolean minimumBox(ClearCLImageInterface arg1, ClearCLImageInterface arg2, double arg3, double arg4, double arg5) {
        if (doTimeTracing()) {recordMethodStart("Minimum3DBox");}
        boolean result = Minimum3DBox.minimumBox(getCLIJ2(), arg1, arg2, new Double (arg3).intValue(), new Double (arg4).intValue(), new Double (arg5).intValue());
        if (doTimeTracing()) {recordMethodEnd("Minimum3DBox");}
        return result;
    }


    // net.haesleinhuepf.clij2.plugins.MinimumSliceBySliceSphere
    //----------------------------------------------------
    /**
     * Computes the local minimum of a pixels ellipsoidal 2D neighborhood in an image stack 
     * slice by slice. 
     * 
     * The ellipses size is specified by its half-width and half-height (radius).
     * 
     * This filter is applied slice by slice in 2D.
     */
    default boolean minimum3DSliceBySliceSphere(ClearCLImageInterface arg1, ClearCLImageInterface arg2, double arg3, double arg4) {
        if (doTimeTracing()) {recordMethodStart("MinimumSliceBySliceSphere");}
        boolean result = MinimumSliceBySliceSphere.minimum3DSliceBySliceSphere(getCLIJ2(), arg1, arg2, new Double (arg3).intValue(), new Double (arg4).intValue());
        if (doTimeTracing()) {recordMethodEnd("MinimumSliceBySliceSphere");}
        return result;
    }


    // net.haesleinhuepf.clij2.plugins.MultiplyImages
    //----------------------------------------------------
    /**
     * Multiplies all pairs of pixel values x and y from two images X and Y.
     * 
     * <pre>f(x, y) = x * y</pre>
     * 
     * Parameters
     * ----------
     * factor1 : Image
     *     The first input image to be multiplied.
     * factor2 : Image
     *     The second image to be multiplied.
     * destination : Image
     *     The output image where results are written into.
     * 
     */
    default boolean multiplyImages(ClearCLImageInterface factor1, ClearCLImageInterface factor2, ClearCLImageInterface destination) {
        if (doTimeTracing()) {recordMethodStart("MultiplyImages");}
        boolean result = MultiplyImages.multiplyImages(getCLIJ2(), factor1, factor2, destination);
        if (doTimeTracing()) {recordMethodEnd("MultiplyImages");}
        return result;
    }


    // net.haesleinhuepf.clij2.plugins.GaussianBlur2D
    //----------------------------------------------------
    /**
     * Computes the Gaussian blurred image of an image given two sigma values in X and Y. 
     * 
     * Thus, the filterkernel can have non-isotropic shape.
     * 
     * The implementation is done separable. In case a sigma equals zero, the direction is not blurred.
     * 
     * DEPRECATED: This method is deprecated. Use gaussianBlur2D instead.
     */
    @Deprecated
    default boolean blur(ClearCLImageInterface arg1, ClearCLImageInterface arg2, double arg3, double arg4) {
        System.out.println("blur is deprecated. Check the documentation for a replacement. https://clij.github.io/clij2-doccs/reference");
        if (doTimeTracing()) {recordMethodStart("GaussianBlur2D");}
        boolean result = GaussianBlur2D.blur(getCLIJ2(), arg1, arg2, new Double (arg3).floatValue(), new Double (arg4).floatValue());
        if (doTimeTracing()) {recordMethodEnd("GaussianBlur2D");}
        return result;
    }

    /**
     * Computes the Gaussian blurred image of an image given two sigma values in X and Y. 
     * 
     * Thus, the filterkernel can have non-isotropic shape.
     * 
     * The implementation is done separable. In case a sigma equals zero, the direction is not blurred.
     * 
     * DEPRECATED: This method is deprecated. Use gaussianBlur2D instead.
     */
    @Deprecated
    default boolean blur2D(ClearCLImageInterface arg1, ClearCLImageInterface arg2, double arg3, double arg4) {
        System.out.println("blur2D is deprecated. Check the documentation for a replacement. https://clij.github.io/clij2-doccs/reference");
        if (doTimeTracing()) {recordMethodStart("GaussianBlur2D");}
        boolean result = GaussianBlur2D.blur2D(getCLIJ2(), arg1, arg2, new Double (arg3).floatValue(), new Double (arg4).floatValue());
        if (doTimeTracing()) {recordMethodEnd("GaussianBlur2D");}
        return result;
    }

    /**
     * Computes the Gaussian blurred image of an image given two sigma values in X and Y. 
     * 
     * Thus, the filterkernel can have non-isotropic shape.
     * 
     * The implementation is done separable. In case a sigma equals zero, the direction is not blurred.
     */
    default boolean gaussianBlur(ClearCLImageInterface arg1, ClearCLImageInterface arg2, double arg3, double arg4) {
        if (doTimeTracing()) {recordMethodStart("GaussianBlur2D");}
        boolean result = GaussianBlur2D.gaussianBlur(getCLIJ2(), arg1, arg2, new Double (arg3).floatValue(), new Double (arg4).floatValue());
        if (doTimeTracing()) {recordMethodEnd("GaussianBlur2D");}
        return result;
    }

    /**
     * Computes the Gaussian blurred image of an image given two sigma values in X and Y. 
     * 
     * Thus, the filterkernel can have non-isotropic shape.
     * 
     * The implementation is done separable. In case a sigma equals zero, the direction is not blurred.
     */
    default boolean gaussianBlur2D(ClearCLImageInterface arg1, ClearCLImageInterface arg2, double arg3, double arg4) {
        if (doTimeTracing()) {recordMethodStart("GaussianBlur2D");}
        boolean result = GaussianBlur2D.gaussianBlur2D(getCLIJ2(), arg1, arg2, new Double (arg3).floatValue(), new Double (arg4).floatValue());
        if (doTimeTracing()) {recordMethodEnd("GaussianBlur2D");}
        return result;
    }


    // net.haesleinhuepf.clij2.plugins.GaussianBlur3D
    //----------------------------------------------------
    /**
     * Computes the Gaussian blurred image of an image given two sigma values in X and Y. 
     * 
     * Thus, the filterkernel can have non-isotropic shape.
     * 
     * The implementation is done separable. In case a sigma equals zero, the direction is not blurred.
     * 
     * DEPRECATED: This method is deprecated. Use gaussianBlur2D instead.
     */
    @Deprecated
    default boolean blur(ClearCLImageInterface arg1, ClearCLImageInterface arg2, double arg3, double arg4, double arg5) {
        System.out.println("blur is deprecated. Check the documentation for a replacement. https://clij.github.io/clij2-doccs/reference");
        if (doTimeTracing()) {recordMethodStart("GaussianBlur3D");}
        boolean result = GaussianBlur3D.blur(getCLIJ2(), arg1, arg2, new Double (arg3).floatValue(), new Double (arg4).floatValue(), new Double (arg5).floatValue());
        if (doTimeTracing()) {recordMethodEnd("GaussianBlur3D");}
        return result;
    }

    /**
     * Computes the Gaussian blurred image of an image given two sigma values in X, Y and Z. 
     * 
     * Thus, the filterkernel can have non-isotropic shape.
     * 
     * The implementation is done separable. In case a sigma equals zero, the direction is not blurred.
     * 
     * DEPRECATED: This method is deprecated. Use gaussianBlur3D instead.
     */
    @Deprecated
    default boolean blur3D(ClearCLImageInterface arg1, ClearCLImageInterface arg2, double arg3, double arg4, double arg5) {
        System.out.println("blur3D is deprecated. Check the documentation for a replacement. https://clij.github.io/clij2-doccs/reference");
        if (doTimeTracing()) {recordMethodStart("GaussianBlur3D");}
        boolean result = GaussianBlur3D.blur3D(getCLIJ2(), arg1, arg2, new Double (arg3).floatValue(), new Double (arg4).floatValue(), new Double (arg5).floatValue());
        if (doTimeTracing()) {recordMethodEnd("GaussianBlur3D");}
        return result;
    }

    /**
     * Computes the Gaussian blurred image of an image given two sigma values in X and Y. 
     * 
     * Thus, the filterkernel can have non-isotropic shape.
     * 
     * The implementation is done separable. In case a sigma equals zero, the direction is not blurred.
     */
    default boolean gaussianBlur(ClearCLImageInterface arg1, ClearCLImageInterface arg2, double arg3, double arg4, double arg5) {
        if (doTimeTracing()) {recordMethodStart("GaussianBlur3D");}
        boolean result = GaussianBlur3D.gaussianBlur(getCLIJ2(), arg1, arg2, new Double (arg3).floatValue(), new Double (arg4).floatValue(), new Double (arg5).floatValue());
        if (doTimeTracing()) {recordMethodEnd("GaussianBlur3D");}
        return result;
    }

    /**
     * Computes the Gaussian blurred image of an image given two sigma values in X, Y and Z. 
     * 
     * Thus, the filterkernel can have non-isotropic shape.
     * 
     * The implementation is done separable. In case a sigma equals zero, the direction is not blurred.
     */
    default boolean gaussianBlur3D(ClearCLImageInterface arg1, ClearCLImageInterface arg2, double arg3, double arg4, double arg5) {
        if (doTimeTracing()) {recordMethodStart("GaussianBlur3D");}
        boolean result = GaussianBlur3D.gaussianBlur3D(getCLIJ2(), arg1, arg2, new Double (arg3).floatValue(), new Double (arg4).floatValue(), new Double (arg5).floatValue());
        if (doTimeTracing()) {recordMethodEnd("GaussianBlur3D");}
        return result;
    }


    // net.haesleinhuepf.clij2.plugins.ResliceBottom
    //----------------------------------------------------
    /**
     * Flippes Y and Z axis of an image stack. This operation is similar to ImageJs 'Reslice [/]' method but
     * offers less flexibility such as interpolation.
     */
    default boolean resliceBottom(ClearCLImageInterface source, ClearCLImageInterface destination) {
        if (doTimeTracing()) {recordMethodStart("ResliceBottom");}
        boolean result = ResliceBottom.resliceBottom(getCLIJ2(), source, destination);
        if (doTimeTracing()) {recordMethodEnd("ResliceBottom");}
        return result;
    }


    // net.haesleinhuepf.clij2.plugins.ResliceTop
    //----------------------------------------------------
    /**
     * Flippes Y and Z axis of an image stack. This operation is similar to ImageJs 'Reslice [/]' method but
     * offers less flexibility such as interpolation.
     */
    default boolean resliceTop(ClearCLImageInterface source, ClearCLImageInterface destination) {
        if (doTimeTracing()) {recordMethodStart("ResliceTop");}
        boolean result = ResliceTop.resliceTop(getCLIJ2(), source, destination);
        if (doTimeTracing()) {recordMethodEnd("ResliceTop");}
        return result;
    }


    // net.haesleinhuepf.clij2.plugins.ResliceLeft
    //----------------------------------------------------
    /**
     * Flippes X, Y and Z axis of an image stack. This operation is similar to ImageJs 'Reslice [/]' method 
     *  but offers less flexibility such as interpolation.
     */
    default boolean resliceLeft(ClearCLImageInterface source, ClearCLImageInterface destination) {
        if (doTimeTracing()) {recordMethodStart("ResliceLeft");}
        boolean result = ResliceLeft.resliceLeft(getCLIJ2(), source, destination);
        if (doTimeTracing()) {recordMethodEnd("ResliceLeft");}
        return result;
    }


    // net.haesleinhuepf.clij2.plugins.ResliceRight
    //----------------------------------------------------
    /**
     * Flippes X, Y and Z axis of an image stack. This operation is similar to ImageJs 'Reslice [/]' method 
     *  but offers less flexibility such as interpolation.
     */
    default boolean resliceRight(ClearCLImageInterface source, ClearCLImageInterface destination) {
        if (doTimeTracing()) {recordMethodStart("ResliceRight");}
        boolean result = ResliceRight.resliceRight(getCLIJ2(), source, destination);
        if (doTimeTracing()) {recordMethodEnd("ResliceRight");}
        return result;
    }


    // net.haesleinhuepf.clij2.plugins.Rotate2D
    //----------------------------------------------------
    /**
     * Rotates an image in plane. 
     * 
     * All angles are entered in degrees. If the image is not rotated around 
     * the center, it is rotated around the coordinate origin.
     * 
     * It is recommended to apply the rotation to an isotropic image.
     */
    default boolean rotate2D(ClearCLBuffer arg1, ClearCLBuffer arg2, double arg3, boolean arg4) {
        if (doTimeTracing()) {recordMethodStart("Rotate2D");}
        boolean result = Rotate2D.rotate2D(getCLIJ2(), arg1, arg2, new Double (arg3).floatValue(), arg4);
        if (doTimeTracing()) {recordMethodEnd("Rotate2D");}
        return result;
    }


    // net.haesleinhuepf.clij2.plugins.Rotate3D
    //----------------------------------------------------
    /**
     * Rotates an image stack in 3D. 
     * 
     * All angles are entered in degrees. If the image is not rotated around 
     * the center, it is rotated around the coordinate origin.
     * 
     * It is recommended to apply the rotation to an isotropic image stack.
     */
    default boolean rotate3D(ClearCLBuffer arg1, ClearCLBuffer arg2, double arg3, double arg4, double arg5, boolean arg6) {
        if (doTimeTracing()) {recordMethodStart("Rotate3D");}
        boolean result = Rotate3D.rotate3D(getCLIJ2(), arg1, arg2, new Double (arg3).floatValue(), new Double (arg4).floatValue(), new Double (arg5).floatValue(), arg6);
        if (doTimeTracing()) {recordMethodEnd("Rotate3D");}
        return result;
    }


    // net.haesleinhuepf.clij2.plugins.Scale2D
    //----------------------------------------------------
    /**
     * Scales an image with a given factor.
     */
    default boolean scale(ClearCLBuffer arg1, ClearCLBuffer arg2, double arg3) {
        if (doTimeTracing()) {recordMethodStart("Scale2D");}
        boolean result = Scale2D.scale(getCLIJ2(), arg1, arg2, new Double (arg3).floatValue());
        if (doTimeTracing()) {recordMethodEnd("Scale2D");}
        return result;
    }

    /**
     * Scales an image with a given factor.
     */
    default boolean scale(ClearCLBuffer arg1, ClearCLBuffer arg2, double arg3, double arg4) {
        if (doTimeTracing()) {recordMethodStart("Scale2D");}
        boolean result = Scale2D.scale(getCLIJ2(), arg1, arg2, new Double (arg3).floatValue(), new Double (arg4).floatValue());
        if (doTimeTracing()) {recordMethodEnd("Scale2D");}
        return result;
    }

    /**
     * Scales an image with a given factor.
     */
    default boolean scale2D(ClearCLBuffer arg1, ClearCLBuffer arg2, double arg3, double arg4) {
        if (doTimeTracing()) {recordMethodStart("Scale2D");}
        boolean result = Scale2D.scale2D(getCLIJ2(), arg1, arg2, new Double (arg3).floatValue(), new Double (arg4).floatValue());
        if (doTimeTracing()) {recordMethodEnd("Scale2D");}
        return result;
    }

    /**
     * Scales an image with a given factor.
     */
    default boolean scale2D(ClearCLBuffer arg1, ClearCLBuffer arg2, double arg3, double arg4, boolean arg5) {
        if (doTimeTracing()) {recordMethodStart("Scale2D");}
        boolean result = Scale2D.scale2D(getCLIJ2(), arg1, arg2, new Double (arg3).floatValue(), new Double (arg4).floatValue(), arg5);
        if (doTimeTracing()) {recordMethodEnd("Scale2D");}
        return result;
    }


    // net.haesleinhuepf.clij2.plugins.Scale3D
    //----------------------------------------------------
    /**
     * Scales an image with a given factor.
     */
    default boolean scale3D(ClearCLBuffer arg1, ClearCLBuffer arg2, double arg3, double arg4, double arg5) {
        if (doTimeTracing()) {recordMethodStart("Scale3D");}
        boolean result = Scale3D.scale3D(getCLIJ2(), arg1, arg2, new Double (arg3).floatValue(), new Double (arg4).floatValue(), new Double (arg5).floatValue());
        if (doTimeTracing()) {recordMethodEnd("Scale3D");}
        return result;
    }

    /**
     * Scales an image with a given factor.
     */
    default boolean scale3D(ClearCLBuffer arg1, ClearCLBuffer arg2, double arg3, double arg4, double arg5, boolean arg6) {
        if (doTimeTracing()) {recordMethodStart("Scale3D");}
        boolean result = Scale3D.scale3D(getCLIJ2(), arg1, arg2, new Double (arg3).floatValue(), new Double (arg4).floatValue(), new Double (arg5).floatValue(), arg6);
        if (doTimeTracing()) {recordMethodEnd("Scale3D");}
        return result;
    }


    // net.haesleinhuepf.clij2.plugins.Translate2D
    //----------------------------------------------------
    /**
     * Translate an image stack in X and Y.
     */
    default boolean translate2D(ClearCLBuffer arg1, ClearCLBuffer arg2, double arg3, double arg4) {
        if (doTimeTracing()) {recordMethodStart("Translate2D");}
        boolean result = Translate2D.translate2D(getCLIJ2(), arg1, arg2, new Double (arg3).floatValue(), new Double (arg4).floatValue());
        if (doTimeTracing()) {recordMethodEnd("Translate2D");}
        return result;
    }


    // net.haesleinhuepf.clij2.plugins.Translate3D
    //----------------------------------------------------
    /**
     * Translate an image stack in X, Y and Z.
     */
    default boolean translate3D(ClearCLBuffer arg1, ClearCLBuffer arg2, double arg3, double arg4, double arg5) {
        if (doTimeTracing()) {recordMethodStart("Translate3D");}
        boolean result = Translate3D.translate3D(getCLIJ2(), arg1, arg2, new Double (arg3).floatValue(), new Double (arg4).floatValue(), new Double (arg5).floatValue());
        if (doTimeTracing()) {recordMethodEnd("Translate3D");}
        return result;
    }


    // net.haesleinhuepf.clij2.plugins.Clear
    //----------------------------------------------------

    // net.haesleinhuepf.clij2.plugins.ClInfo
    //----------------------------------------------------

    // net.haesleinhuepf.clij2.plugins.ConvertFloat
    //----------------------------------------------------

    // net.haesleinhuepf.clij2.plugins.ConvertUInt8
    //----------------------------------------------------

    // net.haesleinhuepf.clij2.plugins.ConvertUInt16
    //----------------------------------------------------

    // net.haesleinhuepf.clij2.plugins.Create2D
    //----------------------------------------------------

    // net.haesleinhuepf.clij2.plugins.Create3D
    //----------------------------------------------------

    // net.haesleinhuepf.clij2.plugins.Pull
    //----------------------------------------------------

    // net.haesleinhuepf.clij2.plugins.PullBinary
    //----------------------------------------------------

    // net.haesleinhuepf.clij2.plugins.Push
    //----------------------------------------------------

    // net.haesleinhuepf.clij2.plugins.PushCurrentSlice
    //----------------------------------------------------

    // net.haesleinhuepf.clij2.plugins.PushCurrentZStack
    //----------------------------------------------------

    // net.haesleinhuepf.clij2.plugins.PushCurrentSelection
    //----------------------------------------------------

    // net.haesleinhuepf.clij2.plugins.PushCurrentSliceSelection
    //----------------------------------------------------

    // net.haesleinhuepf.clij2.plugins.Release
    //----------------------------------------------------

    // net.haesleinhuepf.clij2.plugins.AddImageAndScalar
    //----------------------------------------------------
    /**
     * Adds a scalar value s to all pixels x of a given image X.
     * 
     * <pre>f(x, s) = x + s</pre>
     * 
     * Parameters
     * ----------
     * source : Image
     *     The input image where scalare should be added.
     * destination : Image
     *     The output image where results are written into.
     * scalar : float
     *     The constant number which will be added to all pixels.
     * 
     */
    default boolean addImageAndScalar(ClearCLImageInterface arg1, ClearCLImageInterface arg2, double arg3) {
        if (doTimeTracing()) {recordMethodStart("AddImageAndScalar");}
        boolean result = AddImageAndScalar.addImageAndScalar(getCLIJ2(), arg1, arg2, new Double (arg3).floatValue());
        if (doTimeTracing()) {recordMethodEnd("AddImageAndScalar");}
        return result;
    }


    // net.haesleinhuepf.clij2.plugins.DetectMinimaBox
    //----------------------------------------------------
    /**
     * Detects local minima in a given square/cubic neighborhood. 
     * 
     * Pixels in the resulting image are set to 1 if there is no other pixel in a given radius which has a 
     * lower intensity, and to 0 otherwise.
     */
    @Deprecated
    default boolean detectMinimaBox(ClearCLImageInterface arg1, ClearCLImageInterface arg2, double arg3) {
        System.out.println("detectMinimaBox is deprecated. Check the documentation for a replacement. https://clij.github.io/clij2-doccs/reference");
        if (doTimeTracing()) {recordMethodStart("DetectMinimaBox");}
        boolean result = DetectMinimaBox.detectMinimaBox(getCLIJ2(), arg1, arg2, new Double (arg3).intValue());
        if (doTimeTracing()) {recordMethodEnd("DetectMinimaBox");}
        return result;
    }

    /**
     * Detects local minima in a given square/cubic neighborhood. 
     * 
     * Pixels in the resulting image are set to 1 if there is no other pixel in a given radius which has a 
     * lower intensity, and to 0 otherwise.
     */
    default boolean detectMinimaBox(ClearCLImageInterface arg1, ClearCLImageInterface arg2, double arg3, double arg4, double arg5) {
        if (doTimeTracing()) {recordMethodStart("DetectMinimaBox");}
        boolean result = DetectMinimaBox.detectMinimaBox(getCLIJ2(), arg1, arg2, new Double (arg3).intValue(), new Double (arg4).intValue(), new Double (arg5).intValue());
        if (doTimeTracing()) {recordMethodEnd("DetectMinimaBox");}
        return result;
    }


    // net.haesleinhuepf.clij2.plugins.DetectMaximaBox
    //----------------------------------------------------
    /**
     * Detects local maxima in a given square/cubic neighborhood. 
     * 
     * Pixels in the resulting image are set to 1 if there is no other pixel in a given radius which has a 
     * higher intensity, and to 0 otherwise.
     */
    @Deprecated
    default boolean detectMaximaBox(ClearCLImageInterface arg1, ClearCLImageInterface arg2, double arg3) {
        System.out.println("detectMaximaBox is deprecated. Check the documentation for a replacement. https://clij.github.io/clij2-doccs/reference");
        if (doTimeTracing()) {recordMethodStart("DetectMaximaBox");}
        boolean result = DetectMaximaBox.detectMaximaBox(getCLIJ2(), arg1, arg2, new Double (arg3).intValue());
        if (doTimeTracing()) {recordMethodEnd("DetectMaximaBox");}
        return result;
    }

    /**
     * Detects local maxima in a given square/cubic neighborhood. 
     * 
     * Pixels in the resulting image are set to 1 if there is no other pixel in a given radius which has a 
     * higher intensity, and to 0 otherwise.
     */
    default boolean detectMaximaBox(ClearCLImageInterface arg1, ClearCLImageInterface arg2, double arg3, double arg4, double arg5) {
        if (doTimeTracing()) {recordMethodStart("DetectMaximaBox");}
        boolean result = DetectMaximaBox.detectMaximaBox(getCLIJ2(), arg1, arg2, new Double (arg3).intValue(), new Double (arg4).intValue(), new Double (arg5).intValue());
        if (doTimeTracing()) {recordMethodEnd("DetectMaximaBox");}
        return result;
    }


    // net.haesleinhuepf.clij2.plugins.DetectMaximaSliceBySliceBox
    //----------------------------------------------------
    /**
     * Detects local maxima in a given square neighborhood of an input image stack. 
     * 
     * The input image stack is processed slice by slice. Pixels in the resulting image are set to 1 if 
     * there is no other pixel in a given radius which has a higher intensity, and to 0 otherwise.
     */
    default boolean detectMaximaSliceBySliceBox(ClearCLBuffer arg1, ClearCLBuffer arg2, double arg3, double arg4) {
        if (doTimeTracing()) {recordMethodStart("DetectMaximaSliceBySliceBox");}
        boolean result = DetectMaximaSliceBySliceBox.detectMaximaSliceBySliceBox(getCLIJ2(), arg1, arg2, new Double (arg3).intValue(), new Double (arg4).intValue());
        if (doTimeTracing()) {recordMethodEnd("DetectMaximaSliceBySliceBox");}
        return result;
    }


    // net.haesleinhuepf.clij2.plugins.DetectMinimaSliceBySliceBox
    //----------------------------------------------------
    /**
     * Detects local minima in a given square neighborhood of an input image stack. 
     * 
     * The input image stack is processed slice by slice. Pixels in the resulting image are set to 1 if 
     * there is no other pixel in a given radius which has a lower intensity, and to 0 otherwise.
     */
    default boolean detectMinimaSliceBySliceBox(ClearCLBuffer arg1, ClearCLBuffer arg2, double arg3, double arg4) {
        if (doTimeTracing()) {recordMethodStart("DetectMinimaSliceBySliceBox");}
        boolean result = DetectMinimaSliceBySliceBox.detectMinimaSliceBySliceBox(getCLIJ2(), arg1, arg2, new Double (arg3).intValue(), new Double (arg4).intValue());
        if (doTimeTracing()) {recordMethodEnd("DetectMinimaSliceBySliceBox");}
        return result;
    }


    // net.haesleinhuepf.clij2.plugins.MaximumOfAllPixels
    //----------------------------------------------------
    /**
     * Determines the maximum of all pixels in a given image. 
     * 
     * It will be stored in a new row of ImageJs
     * Results table in the column 'Max'.
     * 
     * Parameters
     * ----------
     * source : Image
     *     The image of which the maximum of all pixels or voxels will be determined.
     * 
     */
    default double maximumOfAllPixels(ClearCLImageInterface source) {
        if (doTimeTracing()) {recordMethodStart("MaximumOfAllPixels");}
        double result = MaximumOfAllPixels.maximumOfAllPixels(getCLIJ2(), source);
        if (doTimeTracing()) {recordMethodEnd("MaximumOfAllPixels");}
        return result;
    }


    // net.haesleinhuepf.clij2.plugins.MinimumOfAllPixels
    //----------------------------------------------------
    /**
     * Determines the minimum of all pixels in a given image. 
     * 
     * It will be stored in a new row of ImageJs
     * Results table in the column 'Min'.
     * 
     * Parameters
     * ----------
     * source : Image
     *     The image of which the minimum of all pixels or voxels will be determined.
     * 
     */
    default double minimumOfAllPixels(ClearCLImageInterface source) {
        if (doTimeTracing()) {recordMethodStart("MinimumOfAllPixels");}
        double result = MinimumOfAllPixels.minimumOfAllPixels(getCLIJ2(), source);
        if (doTimeTracing()) {recordMethodEnd("MinimumOfAllPixels");}
        return result;
    }


    // net.haesleinhuepf.clij2.plugins.ReportMemory
    //----------------------------------------------------

    // net.haesleinhuepf.clij2.plugins.SetColumn
    //----------------------------------------------------
    /**
     * Sets all pixel values x of a given column in X to a constant value v.
     */
    default boolean setColumn(ClearCLImageInterface arg1, double arg2, double arg3) {
        if (doTimeTracing()) {recordMethodStart("SetColumn");}
        boolean result = SetColumn.setColumn(getCLIJ2(), arg1, new Double (arg2).intValue(), new Double (arg3).floatValue());
        if (doTimeTracing()) {recordMethodEnd("SetColumn");}
        return result;
    }


    // net.haesleinhuepf.clij2.plugins.SetRow
    //----------------------------------------------------
    /**
     * Sets all pixel values x of a given row in X to a constant value v.
     */
    default boolean setRow(ClearCLImageInterface arg1, double arg2, double arg3) {
        if (doTimeTracing()) {recordMethodStart("SetRow");}
        boolean result = SetRow.setRow(getCLIJ2(), arg1, new Double (arg2).intValue(), new Double (arg3).floatValue());
        if (doTimeTracing()) {recordMethodEnd("SetRow");}
        return result;
    }


    // net.haesleinhuepf.clij2.plugins.SumYProjection
    //----------------------------------------------------
    /**
     * Determines the sum intensity projection of an image along Z.
     */
    default boolean sumYProjection(ClearCLImageInterface source, ClearCLImageInterface destination) {
        if (doTimeTracing()) {recordMethodStart("SumYProjection");}
        boolean result = SumYProjection.sumYProjection(getCLIJ2(), source, destination);
        if (doTimeTracing()) {recordMethodEnd("SumYProjection");}
        return result;
    }


    // net.haesleinhuepf.clij2.plugins.AverageDistanceOfTouchingNeighbors
    //----------------------------------------------------
    /**
     * Takes a touch matrix and a distance matrix to determine the average distance of touching neighbors 
     *  for every object.
     */
    default boolean averageDistanceOfTouchingNeighbors(ClearCLBuffer distance_matrix, ClearCLBuffer touch_matrix, ClearCLBuffer average_distancelist_destination) {
        if (doTimeTracing()) {recordMethodStart("AverageDistanceOfTouchingNeighbors");}
        boolean result = AverageDistanceOfTouchingNeighbors.averageDistanceOfTouchingNeighbors(getCLIJ2(), distance_matrix, touch_matrix, average_distancelist_destination);
        if (doTimeTracing()) {recordMethodEnd("AverageDistanceOfTouchingNeighbors");}
        return result;
    }


    // net.haesleinhuepf.clij2.plugins.LabelledSpotsToPointList
    //----------------------------------------------------
    /**
     * Generates a coordinate list of points in a labelled spot image. 
     * 
     * Transforms a labelmap of spots (single pixels with values 1, 2, ..., n for n spots) as resulting 
     * from connected components analysis in an image where every column contains d 
     * pixels (with d = dimensionality of the original image) with the coordinates of the maxima/minima.
     */
    default boolean labelledSpotsToPointList(ClearCLBuffer input_labelled_spots, ClearCLBuffer destination_pointlist) {
        if (doTimeTracing()) {recordMethodStart("LabelledSpotsToPointList");}
        boolean result = LabelledSpotsToPointList.labelledSpotsToPointList(getCLIJ2(), input_labelled_spots, destination_pointlist);
        if (doTimeTracing()) {recordMethodEnd("LabelledSpotsToPointList");}
        return result;
    }


    // net.haesleinhuepf.clij2.plugins.LabelSpots
    //----------------------------------------------------
    /**
     * Transforms a binary image with single pixles set to 1 to a labelled spots image. 
     * 
     * Transforms a spots image as resulting from maximum/minimum detection in an image of the same size where every spot has a number 1, 2, ... n.
     */
    default boolean labelSpots(ClearCLBuffer input_spots, ClearCLBuffer labelled_spots_destination) {
        if (doTimeTracing()) {recordMethodStart("LabelSpots");}
        boolean result = LabelSpots.labelSpots(getCLIJ2(), input_spots, labelled_spots_destination);
        if (doTimeTracing()) {recordMethodEnd("LabelSpots");}
        return result;
    }


    // net.haesleinhuepf.clij2.plugins.MinimumDistanceOfTouchingNeighbors
    //----------------------------------------------------
    /**
     * Takes a touch matrix and a distance matrix to determine the shortest distance of touching neighbors for every object.
     */
    default boolean minimumDistanceOfTouchingNeighbors(ClearCLBuffer distance_matrix, ClearCLBuffer touch_matrix, ClearCLBuffer minimum_distancelist_destination) {
        if (doTimeTracing()) {recordMethodStart("MinimumDistanceOfTouchingNeighbors");}
        boolean result = MinimumDistanceOfTouchingNeighbors.minimumDistanceOfTouchingNeighbors(getCLIJ2(), distance_matrix, touch_matrix, minimum_distancelist_destination);
        if (doTimeTracing()) {recordMethodEnd("MinimumDistanceOfTouchingNeighbors");}
        return result;
    }


    // net.haesleinhuepf.clij2.plugins.SetWhereXgreaterThanY
    //----------------------------------------------------
    /**
     * Sets all pixel values a of a given image A to a constant value v in case its coordinates x > y. 
     * 
     * Otherwise the pixel is not overwritten.
     * If you want to initialize an identity transfrom matrix, set all pixels to 0 first.
     */
    default boolean setWhereXgreaterThanY(ClearCLImageInterface arg1, double arg2) {
        if (doTimeTracing()) {recordMethodStart("SetWhereXgreaterThanY");}
        boolean result = SetWhereXgreaterThanY.setWhereXgreaterThanY(getCLIJ2(), arg1, new Double (arg2).floatValue());
        if (doTimeTracing()) {recordMethodEnd("SetWhereXgreaterThanY");}
        return result;
    }


    // net.haesleinhuepf.clij2.plugins.SetWhereXsmallerThanY
    //----------------------------------------------------
    /**
     * Sets all pixel values a of a given image A to a constant value v in case its coordinates x < y. 
     * 
     * Otherwise the pixel is not overwritten.
     * If you want to initialize an identity transfrom matrix, set all pixels to 0 first.
     */
    default boolean setWhereXsmallerThanY(ClearCLImageInterface arg1, double arg2) {
        if (doTimeTracing()) {recordMethodStart("SetWhereXsmallerThanY");}
        boolean result = SetWhereXsmallerThanY.setWhereXsmallerThanY(getCLIJ2(), arg1, new Double (arg2).floatValue());
        if (doTimeTracing()) {recordMethodEnd("SetWhereXsmallerThanY");}
        return result;
    }


    // net.haesleinhuepf.clij2.plugins.SetNonZeroPixelsToPixelIndex
    //----------------------------------------------------
    /**
     * Sets all pixels in an image which are not zero to the index of the pixel. 
     * 
     * This can be used for Connected Components Analysis.
     */
    default boolean setNonZeroPixelsToPixelIndex(ClearCLImageInterface source, ClearCLImageInterface destination) {
        if (doTimeTracing()) {recordMethodStart("SetNonZeroPixelsToPixelIndex");}
        boolean result = SetNonZeroPixelsToPixelIndex.setNonZeroPixelsToPixelIndex(getCLIJ2(), source, destination);
        if (doTimeTracing()) {recordMethodEnd("SetNonZeroPixelsToPixelIndex");}
        return result;
    }


    // net.haesleinhuepf.clij2.plugins.CloseIndexGapsInLabelMap
    //----------------------------------------------------
    /**
     * Analyses a label map and if there are gaps in the indexing (e.g. label 5 is not present) all 
     * subsequent labels will be relabelled. 
     * 
     * Thus, afterwards number of labels and maximum label index are equal.
     * This operation is mostly performed on the CPU.
     */
    default boolean closeIndexGapsInLabelMap(ClearCLBuffer labeling_input, ClearCLImageInterface labeling_destination) {
        if (doTimeTracing()) {recordMethodStart("CloseIndexGapsInLabelMap");}
        boolean result = CloseIndexGapsInLabelMap.closeIndexGapsInLabelMap(getCLIJ2(), labeling_input, labeling_destination);
        if (doTimeTracing()) {recordMethodEnd("CloseIndexGapsInLabelMap");}
        return result;
    }

    /**
     * 
     */
    @Deprecated
    default boolean shiftIntensitiesToCloseGaps(ClearCLBuffer arg1, ClearCLBuffer arg2) {
        System.out.println("shiftIntensitiesToCloseGaps is deprecated. Check the documentation for a replacement. https://clij.github.io/clij2-doccs/reference");
        if (doTimeTracing()) {recordMethodStart("CloseIndexGapsInLabelMap");}
        boolean result = CloseIndexGapsInLabelMap.shiftIntensitiesToCloseGaps(getCLIJ2(), arg1, arg2);
        if (doTimeTracing()) {recordMethodEnd("CloseIndexGapsInLabelMap");}
        return result;
    }


    // net.haesleinhuepf.clij2.plugins.AffineTransform
    //----------------------------------------------------

    // net.haesleinhuepf.clij2.plugins.Scale
    //----------------------------------------------------
    /**
     * Scales an image with a given factor.
     */
    @Deprecated
    default boolean scale(ClearCLBuffer arg1, ClearCLBuffer arg2, double arg3, double arg4, double arg5) {
        System.out.println("scale is deprecated. Check the documentation for a replacement. https://clij.github.io/clij2-doccs/reference");
        if (doTimeTracing()) {recordMethodStart("Scale");}
        boolean result = Scale.scale(getCLIJ2(), arg1, arg2, new Double (arg3).floatValue(), new Double (arg4).floatValue(), new Double (arg5).floatValue());
        if (doTimeTracing()) {recordMethodEnd("Scale");}
        return result;
    }


    // net.haesleinhuepf.clij2.plugins.CentroidsOfLabels
    //----------------------------------------------------
    /**
     * Determines the centroids of all labels in a label image or image stack. 
     * 
     * It writes the resulting  coordinates in a pointlist image. Depending on the dimensionality d of the labelmap and the number  of labels n, the pointlist image will have n*d pixels.
     */
    default boolean centroidsOfLabels(ClearCLBuffer source, ClearCLBuffer pointlist_destination) {
        if (doTimeTracing()) {recordMethodStart("CentroidsOfLabels");}
        boolean result = CentroidsOfLabels.centroidsOfLabels(getCLIJ2(), source, pointlist_destination);
        if (doTimeTracing()) {recordMethodEnd("CentroidsOfLabels");}
        return result;
    }


    // net.haesleinhuepf.clij2.plugins.SetRampX
    //----------------------------------------------------
    /**
     * Sets all pixel values to their X coordinate
     */
    default boolean setRampX(ClearCLImageInterface source) {
        if (doTimeTracing()) {recordMethodStart("SetRampX");}
        boolean result = SetRampX.setRampX(getCLIJ2(), source);
        if (doTimeTracing()) {recordMethodEnd("SetRampX");}
        return result;
    }


    // net.haesleinhuepf.clij2.plugins.SetRampY
    //----------------------------------------------------
    /**
     * Sets all pixel values to their Y coordinate
     */
    default boolean setRampY(ClearCLImageInterface source) {
        if (doTimeTracing()) {recordMethodStart("SetRampY");}
        boolean result = SetRampY.setRampY(getCLIJ2(), source);
        if (doTimeTracing()) {recordMethodEnd("SetRampY");}
        return result;
    }


    // net.haesleinhuepf.clij2.plugins.SetRampZ
    //----------------------------------------------------
    /**
     * Sets all pixel values to their Z coordinate
     */
    default boolean setRampZ(ClearCLImageInterface source) {
        if (doTimeTracing()) {recordMethodStart("SetRampZ");}
        boolean result = SetRampZ.setRampZ(getCLIJ2(), source);
        if (doTimeTracing()) {recordMethodEnd("SetRampZ");}
        return result;
    }


    // net.haesleinhuepf.clij2.plugins.SubtractImageFromScalar
    //----------------------------------------------------
    /**
     * Subtracts one image X from a scalar s pixel wise.
     * 
     * <pre>f(x, s) = s - x</pre>
     */
    default boolean subtractImageFromScalar(ClearCLImageInterface arg1, ClearCLImageInterface arg2, double arg3) {
        if (doTimeTracing()) {recordMethodStart("SubtractImageFromScalar");}
        boolean result = SubtractImageFromScalar.subtractImageFromScalar(getCLIJ2(), arg1, arg2, new Double (arg3).floatValue());
        if (doTimeTracing()) {recordMethodEnd("SubtractImageFromScalar");}
        return result;
    }


    // net.haesleinhuepf.clij2.plugins.ThresholdDefault
    //----------------------------------------------------
    /**
     * The automatic thresholder utilizes the Default threshold method implemented in ImageJ using a histogram determined on 
     * the GPU to create binary images as similar as possible to ImageJ 'Apply Threshold' method.
     */
    default boolean thresholdDefault(ClearCLBuffer input, ClearCLBuffer destination) {
        if (doTimeTracing()) {recordMethodStart("ThresholdDefault");}
        boolean result = ThresholdDefault.thresholdDefault(getCLIJ2(), input, destination);
        if (doTimeTracing()) {recordMethodEnd("ThresholdDefault");}
        return result;
    }


    // net.haesleinhuepf.clij2.plugins.ThresholdOtsu
    //----------------------------------------------------
    /**
     * The automatic thresholder utilizes the Otsu threshold method implemented in ImageJ using a histogram determined on 
     * the GPU to create binary images as similar as possible to ImageJ 'Apply Threshold' method.
     */
    default boolean thresholdOtsu(ClearCLBuffer input, ClearCLBuffer destination) {
        if (doTimeTracing()) {recordMethodStart("ThresholdOtsu");}
        boolean result = ThresholdOtsu.thresholdOtsu(getCLIJ2(), input, destination);
        if (doTimeTracing()) {recordMethodEnd("ThresholdOtsu");}
        return result;
    }


    // net.haesleinhuepf.clij2.plugins.ThresholdHuang
    //----------------------------------------------------
    /**
     * The automatic thresholder utilizes the Huang threshold method implemented in ImageJ using a histogram determined on 
     * the GPU to create binary images as similar as possible to ImageJ 'Apply Threshold' method.
     */
    default boolean thresholdHuang(ClearCLBuffer input, ClearCLBuffer destination) {
        if (doTimeTracing()) {recordMethodStart("ThresholdHuang");}
        boolean result = ThresholdHuang.thresholdHuang(getCLIJ2(), input, destination);
        if (doTimeTracing()) {recordMethodEnd("ThresholdHuang");}
        return result;
    }


    // net.haesleinhuepf.clij2.plugins.ThresholdIntermodes
    //----------------------------------------------------
    /**
     * The automatic thresholder utilizes the Intermodes threshold method implemented in ImageJ using a histogram determined on 
     * the GPU to create binary images as similar as possible to ImageJ 'Apply Threshold' method.
     */
    default boolean thresholdIntermodes(ClearCLBuffer input, ClearCLBuffer destination) {
        if (doTimeTracing()) {recordMethodStart("ThresholdIntermodes");}
        boolean result = ThresholdIntermodes.thresholdIntermodes(getCLIJ2(), input, destination);
        if (doTimeTracing()) {recordMethodEnd("ThresholdIntermodes");}
        return result;
    }


    // net.haesleinhuepf.clij2.plugins.ThresholdIsoData
    //----------------------------------------------------
    /**
     * The automatic thresholder utilizes the IsoData threshold method implemented in ImageJ using a histogram determined on 
     * the GPU to create binary images as similar as possible to ImageJ 'Apply Threshold' method.
     */
    default boolean thresholdIsoData(ClearCLBuffer input, ClearCLBuffer destination) {
        if (doTimeTracing()) {recordMethodStart("ThresholdIsoData");}
        boolean result = ThresholdIsoData.thresholdIsoData(getCLIJ2(), input, destination);
        if (doTimeTracing()) {recordMethodEnd("ThresholdIsoData");}
        return result;
    }


    // net.haesleinhuepf.clij2.plugins.ThresholdIJ_IsoData
    //----------------------------------------------------
    /**
     * The automatic thresholder utilizes the IJ_IsoData threshold method implemented in ImageJ using a histogram determined on 
     * the GPU to create binary images as similar as possible to ImageJ 'Apply Threshold' method.
     */
    default boolean thresholdIJ_IsoData(ClearCLBuffer input, ClearCLBuffer destination) {
        if (doTimeTracing()) {recordMethodStart("ThresholdIJ_IsoData");}
        boolean result = ThresholdIJ_IsoData.thresholdIJ_IsoData(getCLIJ2(), input, destination);
        if (doTimeTracing()) {recordMethodEnd("ThresholdIJ_IsoData");}
        return result;
    }


    // net.haesleinhuepf.clij2.plugins.ThresholdLi
    //----------------------------------------------------
    /**
     * The automatic thresholder utilizes the Li threshold method implemented in ImageJ using a histogram determined on 
     * the GPU to create binary images as similar as possible to ImageJ 'Apply Threshold' method.
     */
    default boolean thresholdLi(ClearCLBuffer input, ClearCLBuffer destination) {
        if (doTimeTracing()) {recordMethodStart("ThresholdLi");}
        boolean result = ThresholdLi.thresholdLi(getCLIJ2(), input, destination);
        if (doTimeTracing()) {recordMethodEnd("ThresholdLi");}
        return result;
    }


    // net.haesleinhuepf.clij2.plugins.ThresholdMaxEntropy
    //----------------------------------------------------
    /**
     * The automatic thresholder utilizes the MaxEntropy threshold method implemented in ImageJ using a histogram determined on 
     * the GPU to create binary images as similar as possible to ImageJ 'Apply Threshold' method.
     */
    default boolean thresholdMaxEntropy(ClearCLBuffer input, ClearCLBuffer destination) {
        if (doTimeTracing()) {recordMethodStart("ThresholdMaxEntropy");}
        boolean result = ThresholdMaxEntropy.thresholdMaxEntropy(getCLIJ2(), input, destination);
        if (doTimeTracing()) {recordMethodEnd("ThresholdMaxEntropy");}
        return result;
    }


    // net.haesleinhuepf.clij2.plugins.ThresholdMean
    //----------------------------------------------------
    /**
     * The automatic thresholder utilizes the Mean threshold method implemented in ImageJ using a histogram determined on 
     * the GPU to create binary images as similar as possible to ImageJ 'Apply Threshold' method.
     */
    default boolean thresholdMean(ClearCLBuffer input, ClearCLBuffer destination) {
        if (doTimeTracing()) {recordMethodStart("ThresholdMean");}
        boolean result = ThresholdMean.thresholdMean(getCLIJ2(), input, destination);
        if (doTimeTracing()) {recordMethodEnd("ThresholdMean");}
        return result;
    }


    // net.haesleinhuepf.clij2.plugins.ThresholdMinError
    //----------------------------------------------------
    /**
     * The automatic thresholder utilizes the MinError threshold method implemented in ImageJ using a histogram determined on 
     * the GPU to create binary images as similar as possible to ImageJ 'Apply Threshold' method.
     */
    default boolean thresholdMinError(ClearCLBuffer input, ClearCLBuffer destination) {
        if (doTimeTracing()) {recordMethodStart("ThresholdMinError");}
        boolean result = ThresholdMinError.thresholdMinError(getCLIJ2(), input, destination);
        if (doTimeTracing()) {recordMethodEnd("ThresholdMinError");}
        return result;
    }


    // net.haesleinhuepf.clij2.plugins.ThresholdMinimum
    //----------------------------------------------------
    /**
     * The automatic thresholder utilizes the Minimum threshold method implemented in ImageJ using a histogram determined on 
     * the GPU to create binary images as similar as possible to ImageJ 'Apply Threshold' method.
     */
    default boolean thresholdMinimum(ClearCLBuffer input, ClearCLBuffer destination) {
        if (doTimeTracing()) {recordMethodStart("ThresholdMinimum");}
        boolean result = ThresholdMinimum.thresholdMinimum(getCLIJ2(), input, destination);
        if (doTimeTracing()) {recordMethodEnd("ThresholdMinimum");}
        return result;
    }


    // net.haesleinhuepf.clij2.plugins.ThresholdMoments
    //----------------------------------------------------
    /**
     * The automatic thresholder utilizes the Moments threshold method implemented in ImageJ using a histogram determined on 
     * the GPU to create binary images as similar as possible to ImageJ 'Apply Threshold' method.
     */
    default boolean thresholdMoments(ClearCLBuffer input, ClearCLBuffer destination) {
        if (doTimeTracing()) {recordMethodStart("ThresholdMoments");}
        boolean result = ThresholdMoments.thresholdMoments(getCLIJ2(), input, destination);
        if (doTimeTracing()) {recordMethodEnd("ThresholdMoments");}
        return result;
    }


    // net.haesleinhuepf.clij2.plugins.ThresholdPercentile
    //----------------------------------------------------
    /**
     * The automatic thresholder utilizes the Percentile threshold method implemented in ImageJ using a histogram determined on 
     * the GPU to create binary images as similar as possible to ImageJ 'Apply Threshold' method.
     */
    default boolean thresholdPercentile(ClearCLBuffer input, ClearCLBuffer destination) {
        if (doTimeTracing()) {recordMethodStart("ThresholdPercentile");}
        boolean result = ThresholdPercentile.thresholdPercentile(getCLIJ2(), input, destination);
        if (doTimeTracing()) {recordMethodEnd("ThresholdPercentile");}
        return result;
    }


    // net.haesleinhuepf.clij2.plugins.ThresholdRenyiEntropy
    //----------------------------------------------------
    /**
     * The automatic thresholder utilizes the RenyiEntropy threshold method implemented in ImageJ using a histogram determined on 
     * the GPU to create binary images as similar as possible to ImageJ 'Apply Threshold' method.
     */
    default boolean thresholdRenyiEntropy(ClearCLBuffer input, ClearCLBuffer destination) {
        if (doTimeTracing()) {recordMethodStart("ThresholdRenyiEntropy");}
        boolean result = ThresholdRenyiEntropy.thresholdRenyiEntropy(getCLIJ2(), input, destination);
        if (doTimeTracing()) {recordMethodEnd("ThresholdRenyiEntropy");}
        return result;
    }


    // net.haesleinhuepf.clij2.plugins.ThresholdShanbhag
    //----------------------------------------------------
    /**
     * The automatic thresholder utilizes the Shanbhag threshold method implemented in ImageJ using a histogram determined on 
     * the GPU to create binary images as similar as possible to ImageJ 'Apply Threshold' method.
     */
    default boolean thresholdShanbhag(ClearCLBuffer input, ClearCLBuffer destination) {
        if (doTimeTracing()) {recordMethodStart("ThresholdShanbhag");}
        boolean result = ThresholdShanbhag.thresholdShanbhag(getCLIJ2(), input, destination);
        if (doTimeTracing()) {recordMethodEnd("ThresholdShanbhag");}
        return result;
    }


    // net.haesleinhuepf.clij2.plugins.ThresholdTriangle
    //----------------------------------------------------
    /**
     * The automatic thresholder utilizes the Triangle threshold method implemented in ImageJ using a histogram determined on 
     * the GPU to create binary images as similar as possible to ImageJ 'Apply Threshold' method.
     */
    default boolean thresholdTriangle(ClearCLBuffer input, ClearCLBuffer destination) {
        if (doTimeTracing()) {recordMethodStart("ThresholdTriangle");}
        boolean result = ThresholdTriangle.thresholdTriangle(getCLIJ2(), input, destination);
        if (doTimeTracing()) {recordMethodEnd("ThresholdTriangle");}
        return result;
    }


    // net.haesleinhuepf.clij2.plugins.ThresholdYen
    //----------------------------------------------------
    /**
     * The automatic thresholder utilizes the Yen threshold method implemented in ImageJ using a histogram determined on 
     * the GPU to create binary images as similar as possible to ImageJ 'Apply Threshold' method.
     */
    default boolean thresholdYen(ClearCLBuffer input, ClearCLBuffer destination) {
        if (doTimeTracing()) {recordMethodStart("ThresholdYen");}
        boolean result = ThresholdYen.thresholdYen(getCLIJ2(), input, destination);
        if (doTimeTracing()) {recordMethodEnd("ThresholdYen");}
        return result;
    }


    // net.haesleinhuepf.clij2.plugins.ExcludeLabelsSubSurface
    //----------------------------------------------------
    /**
     * This operation follows a ray from a given position towards a label (or opposite direction) and checks if  there is another label between the label an the image border. 
     * 
     * If yes, this label is eliminated from the label map.
     */
    default boolean excludeLabelsSubSurface(ClearCLBuffer arg1, ClearCLBuffer arg2, ClearCLBuffer arg3, double arg4, double arg5, double arg6) {
        if (doTimeTracing()) {recordMethodStart("ExcludeLabelsSubSurface");}
        boolean result = ExcludeLabelsSubSurface.excludeLabelsSubSurface(getCLIJ2(), arg1, arg2, arg3, new Double (arg4).floatValue(), new Double (arg5).floatValue(), new Double (arg6).floatValue());
        if (doTimeTracing()) {recordMethodEnd("ExcludeLabelsSubSurface");}
        return result;
    }


    // net.haesleinhuepf.clij2.plugins.ExcludeLabelsOnSurface
    //----------------------------------------------------
    /**
     * This operation follows a ray from a given position towards a label (or opposite direction) and checks if  there is another label between the label an the image border. 
     * 
     * If yes, this label is eliminated from the label map.
     */
    default boolean excludeLabelsOnSurface(ClearCLBuffer arg1, ClearCLBuffer arg2, ClearCLBuffer arg3, double arg4, double arg5, double arg6) {
        if (doTimeTracing()) {recordMethodStart("ExcludeLabelsOnSurface");}
        boolean result = ExcludeLabelsOnSurface.excludeLabelsOnSurface(getCLIJ2(), arg1, arg2, arg3, new Double (arg4).floatValue(), new Double (arg5).floatValue(), new Double (arg6).floatValue());
        if (doTimeTracing()) {recordMethodEnd("ExcludeLabelsOnSurface");}
        return result;
    }


    // net.haesleinhuepf.clij2.plugins.SetPlane
    //----------------------------------------------------
    /**
     * Sets all pixel values x of a given plane in X to a constant value v.
     */
    default boolean setPlane(ClearCLImageInterface arg1, double arg2, double arg3) {
        if (doTimeTracing()) {recordMethodStart("SetPlane");}
        boolean result = SetPlane.setPlane(getCLIJ2(), arg1, new Double (arg2).intValue(), new Double (arg3).floatValue());
        if (doTimeTracing()) {recordMethodEnd("SetPlane");}
        return result;
    }


    // net.haesleinhuepf.clij2.plugins.ImageToStack
    //----------------------------------------------------
    /**
     * Copies a single slice into a stack a given number of times.
     */
    default boolean imageToStack(ClearCLBuffer arg1, ClearCLBuffer arg2, double arg3) {
        if (doTimeTracing()) {recordMethodStart("ImageToStack");}
        boolean result = ImageToStack.imageToStack(getCLIJ2(), arg1, arg2, new Double (arg3).intValue());
        if (doTimeTracing()) {recordMethodEnd("ImageToStack");}
        return result;
    }


    // net.haesleinhuepf.clij2.plugins.SumXProjection
    //----------------------------------------------------
    /**
     * Determines the sum intensity projection of an image along Z.
     */
    default boolean sumXProjection(ClearCLImageInterface source, ClearCLImageInterface destination) {
        if (doTimeTracing()) {recordMethodStart("SumXProjection");}
        boolean result = SumXProjection.sumXProjection(getCLIJ2(), source, destination);
        if (doTimeTracing()) {recordMethodEnd("SumXProjection");}
        return result;
    }


    // net.haesleinhuepf.clij2.plugins.SumImageSliceBySlice
    //----------------------------------------------------
    /**
     * Sums all pixels slice by slice and returns the sums in a vector.
     */
    default boolean sumImageSliceBySlice(ClearCLImageInterface source, ClearCLImageInterface destination) {
        if (doTimeTracing()) {recordMethodStart("SumImageSliceBySlice");}
        boolean result = SumImageSliceBySlice.sumImageSliceBySlice(getCLIJ2(), source, destination);
        if (doTimeTracing()) {recordMethodEnd("SumImageSliceBySlice");}
        return result;
    }

    /**
     * Sums all pixels slice by slice and returns the sums in a vector.
     */
    default double[] sumImageSliceBySlice(ClearCLImageInterface arg1) {
        if (doTimeTracing()) {recordMethodStart("SumImageSliceBySlice");}
        double[] result = SumImageSliceBySlice.sumImageSliceBySlice(getCLIJ2(), arg1);
        if (doTimeTracing()) {recordMethodEnd("SumImageSliceBySlice");}
        return result;
    }

    /**
     * 
     */
    @Deprecated
    default double[] sumPixelsSliceByslice(ClearCLImageInterface arg1) {
        System.out.println("sumPixelsSliceByslice is deprecated. Check the documentation for a replacement. https://clij.github.io/clij2-doccs/reference");
        if (doTimeTracing()) {recordMethodStart("SumImageSliceBySlice");}
        double[] result = SumImageSliceBySlice.sumPixelsSliceByslice(getCLIJ2(), arg1);
        if (doTimeTracing()) {recordMethodEnd("SumImageSliceBySlice");}
        return result;
    }


    // net.haesleinhuepf.clij2.plugins.MultiplyImageStackWithScalars
    //----------------------------------------------------
    /**
     * Multiplies all pixels value x in a given image X with a constant scalar s from a list of scalars.
     * 
     * <pre>f(x, s) = x * s</pre>
     */
    default boolean multiplyImageStackWithScalars(ClearCLImageInterface arg1, ClearCLImageInterface arg2, float[] arg3) {
        if (doTimeTracing()) {recordMethodStart("MultiplyImageStackWithScalars");}
        boolean result = MultiplyImageStackWithScalars.multiplyImageStackWithScalars(getCLIJ2(), arg1, arg2, arg3);
        if (doTimeTracing()) {recordMethodEnd("MultiplyImageStackWithScalars");}
        return result;
    }

    /**
     * Multiplies all pixels value x in a given image X with a constant scalar s from a list of scalars.
     * 
     * <pre>f(x, s) = x * s</pre>
     */
    default boolean multiplyImageStackWithScalars(ClearCLImageInterface arg1, ClearCLImageInterface arg2, ClearCLBuffer arg3) {
        if (doTimeTracing()) {recordMethodStart("MultiplyImageStackWithScalars");}
        boolean result = MultiplyImageStackWithScalars.multiplyImageStackWithScalars(getCLIJ2(), arg1, arg2, arg3);
        if (doTimeTracing()) {recordMethodEnd("MultiplyImageStackWithScalars");}
        return result;
    }

    /**
     * 
     */
    @Deprecated
    default boolean multiplySliceBySliceWithScalars(ClearCLImageInterface arg1, ClearCLImageInterface arg2, float[] arg3) {
        System.out.println("multiplySliceBySliceWithScalars is deprecated. Check the documentation for a replacement. https://clij.github.io/clij2-doccs/reference");
        if (doTimeTracing()) {recordMethodStart("MultiplyImageStackWithScalars");}
        boolean result = MultiplyImageStackWithScalars.multiplySliceBySliceWithScalars(getCLIJ2(), arg1, arg2, arg3);
        if (doTimeTracing()) {recordMethodEnd("MultiplyImageStackWithScalars");}
        return result;
    }


    // net.haesleinhuepf.clij2.plugins.Print
    //----------------------------------------------------
    /**
     * Visualises an image on standard out (console).
     */
    default boolean print(ClearCLImageInterface input) {
        if (doTimeTracing()) {recordMethodStart("Print");}
        boolean result = Print.print(getCLIJ2(), input);
        if (doTimeTracing()) {recordMethodEnd("Print");}
        return result;
    }


    // net.haesleinhuepf.clij2.plugins.VoronoiOctagon
    //----------------------------------------------------
    /**
     * Takes a binary image and dilates the regions using a octagon shape until they touch. 
     * 
     * The pixels where  the regions touched are afterwards returned as binary image which corresponds to the Voronoi diagram.
     */
    default boolean voronoiOctagon(ClearCLBuffer input, ClearCLBuffer destination) {
        if (doTimeTracing()) {recordMethodStart("VoronoiOctagon");}
        boolean result = VoronoiOctagon.voronoiOctagon(getCLIJ2(), input, destination);
        if (doTimeTracing()) {recordMethodEnd("VoronoiOctagon");}
        return result;
    }


    // net.haesleinhuepf.clij2.plugins.SetImageBorders
    //----------------------------------------------------
    /**
     * Sets all pixel values at the image border to a given value.
     */
    default boolean setImageBorders(ClearCLImageInterface arg1, double arg2) {
        if (doTimeTracing()) {recordMethodStart("SetImageBorders");}
        boolean result = SetImageBorders.setImageBorders(getCLIJ2(), arg1, new Double (arg2).floatValue());
        if (doTimeTracing()) {recordMethodEnd("SetImageBorders");}
        return result;
    }


    // net.haesleinhuepf.clij2.plugins.FloodFillDiamond
    //----------------------------------------------------
    /**
     * Replaces recursively all pixels of value a with value b if the pixels have a neighbor with value b.
     */
    default boolean floodFillDiamond(ClearCLBuffer arg1, ClearCLBuffer arg2, double arg3, double arg4) {
        if (doTimeTracing()) {recordMethodStart("FloodFillDiamond");}
        boolean result = FloodFillDiamond.floodFillDiamond(getCLIJ2(), arg1, arg2, new Double (arg3).floatValue(), new Double (arg4).floatValue());
        if (doTimeTracing()) {recordMethodEnd("FloodFillDiamond");}
        return result;
    }


    // net.haesleinhuepf.clij2.plugins.BinaryFillHoles
    //----------------------------------------------------
    /**
     * Fills holes (pixels with value 0 surrounded by pixels with value 1) in a binary image.
     */
    default boolean binaryFillHoles(ClearCLImageInterface source, ClearCLImageInterface destination) {
        if (doTimeTracing()) {recordMethodStart("BinaryFillHoles");}
        boolean result = BinaryFillHoles.binaryFillHoles(getCLIJ2(), source, destination);
        if (doTimeTracing()) {recordMethodEnd("BinaryFillHoles");}
        return result;
    }


    // net.haesleinhuepf.clij2.plugins.ConnectedComponentsLabelingDiamond
    //----------------------------------------------------
    /**
     * Performs connected components analysis inspecting the diamond neighborhood of every pixel to a binary image and generates a label map.
     */
    default boolean connectedComponentsLabelingDiamond(ClearCLImageInterface binary_input, ClearCLImageInterface labeling_destination) {
        if (doTimeTracing()) {recordMethodStart("ConnectedComponentsLabelingDiamond");}
        boolean result = ConnectedComponentsLabelingDiamond.connectedComponentsLabelingDiamond(getCLIJ2(), binary_input, labeling_destination);
        if (doTimeTracing()) {recordMethodEnd("ConnectedComponentsLabelingDiamond");}
        return result;
    }

    /**
     * Performs connected components analysis inspecting the diamond neighborhood of every pixel to a binary image and generates a label map.
     */
    default boolean connectedComponentsLabelingDiamond(ClearCLImageInterface arg1, ClearCLImageInterface arg2, boolean arg3) {
        if (doTimeTracing()) {recordMethodStart("ConnectedComponentsLabelingDiamond");}
        boolean result = ConnectedComponentsLabelingDiamond.connectedComponentsLabelingDiamond(getCLIJ2(), arg1, arg2, arg3);
        if (doTimeTracing()) {recordMethodEnd("ConnectedComponentsLabelingDiamond");}
        return result;
    }


    // net.haesleinhuepf.clij2.plugins.ConnectedComponentsLabelingBox
    //----------------------------------------------------
    /**
     * Performs connected components analysis inspecting the box neighborhood of every pixel to a binary image and generates a label map.
     */
    default boolean connectedComponentsLabelingBox(ClearCLImageInterface binary_input, ClearCLImageInterface labeling_destination) {
        if (doTimeTracing()) {recordMethodStart("ConnectedComponentsLabelingBox");}
        boolean result = ConnectedComponentsLabelingBox.connectedComponentsLabelingBox(getCLIJ2(), binary_input, labeling_destination);
        if (doTimeTracing()) {recordMethodEnd("ConnectedComponentsLabelingBox");}
        return result;
    }

    /**
     * Performs connected components analysis inspecting the box neighborhood of every pixel to a binary image and generates a label map.
     */
    default boolean connectedComponentsLabelingBox(ClearCLImageInterface arg1, ClearCLImageInterface arg2, boolean arg3) {
        if (doTimeTracing()) {recordMethodStart("ConnectedComponentsLabelingBox");}
        boolean result = ConnectedComponentsLabelingBox.connectedComponentsLabelingBox(getCLIJ2(), arg1, arg2, arg3);
        if (doTimeTracing()) {recordMethodEnd("ConnectedComponentsLabelingBox");}
        return result;
    }


    // net.haesleinhuepf.clij2.plugins.SetRandom
    //----------------------------------------------------
    /**
     * Fills an image or image stack with uniformly distributed random numbers between given minimum and maximum values. 
     * 
     * Recommendation: For the seed, use getTime().
     */
    default boolean setRandom(ClearCLBuffer arg1, double arg2, double arg3) {
        if (doTimeTracing()) {recordMethodStart("SetRandom");}
        boolean result = SetRandom.setRandom(getCLIJ2(), arg1, new Double (arg2).floatValue(), new Double (arg3).floatValue());
        if (doTimeTracing()) {recordMethodEnd("SetRandom");}
        return result;
    }

    /**
     * Fills an image or image stack with uniformly distributed random numbers between given minimum and maximum values. 
     * 
     * Recommendation: For the seed, use getTime().
     */
    default boolean setRandom(ClearCLBuffer arg1, double arg2, double arg3, double arg4) {
        if (doTimeTracing()) {recordMethodStart("SetRandom");}
        boolean result = SetRandom.setRandom(getCLIJ2(), arg1, new Double (arg2).floatValue(), new Double (arg3).floatValue(), new Double (arg4).floatValue());
        if (doTimeTracing()) {recordMethodEnd("SetRandom");}
        return result;
    }


    // net.haesleinhuepf.clij2.plugins.InvalidateKernelCache
    //----------------------------------------------------

    // net.haesleinhuepf.clij2.plugins.EntropyBox
    //----------------------------------------------------
    /**
     * Determines the local entropy in a box with a given radius around every pixel.
     */
    default boolean entropyBox(ClearCLBuffer arg1, ClearCLBuffer arg2, double arg3, double arg4, double arg5) {
        if (doTimeTracing()) {recordMethodStart("EntropyBox");}
        boolean result = EntropyBox.entropyBox(getCLIJ2(), arg1, arg2, new Double (arg3).intValue(), new Double (arg4).intValue(), new Double (arg5).intValue());
        if (doTimeTracing()) {recordMethodEnd("EntropyBox");}
        return result;
    }

    /**
     * Determines the local entropy in a box with a given radius around every pixel.
     */
    default boolean entropyBox(ClearCLBuffer arg1, ClearCLBuffer arg2, double arg3, double arg4, double arg5, double arg6, double arg7) {
        if (doTimeTracing()) {recordMethodStart("EntropyBox");}
        boolean result = EntropyBox.entropyBox(getCLIJ2(), arg1, arg2, new Double (arg3).intValue(), new Double (arg4).intValue(), new Double (arg5).intValue(), new Double (arg6).floatValue(), new Double (arg7).floatValue());
        if (doTimeTracing()) {recordMethodEnd("EntropyBox");}
        return result;
    }


    // net.haesleinhuepf.clij2.plugins.ConcatenateStacks
    //----------------------------------------------------
    /**
     * Concatenates two stacks in Z.
     */
    default boolean concatenateStacks(ClearCLImageInterface stack1, ClearCLImageInterface stack2, ClearCLImageInterface destination) {
        if (doTimeTracing()) {recordMethodStart("ConcatenateStacks");}
        boolean result = ConcatenateStacks.concatenateStacks(getCLIJ2(), stack1, stack2, destination);
        if (doTimeTracing()) {recordMethodEnd("ConcatenateStacks");}
        return result;
    }


    // net.haesleinhuepf.clij2.plugins.ResultsTableToImage2D
    //----------------------------------------------------
    /**
     * Converts a table to an image. 
     * 
     * Rows stay rows, columns stay columns.
     */
    @Deprecated
    default boolean resultsTableToImage2D(ClearCLBuffer arg1, ResultsTable arg2) {
        System.out.println("resultsTableToImage2D is deprecated. Check the documentation for a replacement. https://clij.github.io/clij2-doccs/reference");
        if (doTimeTracing()) {recordMethodStart("ResultsTableToImage2D");}
        boolean result = ResultsTableToImage2D.resultsTableToImage2D(getCLIJ2(), arg1, arg2);
        if (doTimeTracing()) {recordMethodEnd("ResultsTableToImage2D");}
        return result;
    }


    // net.haesleinhuepf.clij2.plugins.GetAutomaticThreshold
    //----------------------------------------------------
    /**
     * The automatic thresholder utilizes the threshold methods from ImageJ on a histogram determined on 
     * the GPU to determine a threshold value as similar as possible to ImageJ 'Apply Threshold' method. 
     * 
     * Enter one 
     * of these methods in the method text field:
     * [Default, Huang, Intermodes, IsoData, IJ_IsoData, Li, MaxEntropy, Mean, MinError, Minimum, Moments, Otsu, Percentile, RenyiEntropy, Shanbhag, Triangle, Yen]
     */
    default double getAutomaticThreshold(ClearCLBuffer arg1, String arg2) {
        if (doTimeTracing()) {recordMethodStart("GetAutomaticThreshold");}
        double result = GetAutomaticThreshold.getAutomaticThreshold(getCLIJ2(), arg1, arg2);
        if (doTimeTracing()) {recordMethodEnd("GetAutomaticThreshold");}
        return result;
    }

    /**
     * The automatic thresholder utilizes the threshold methods from ImageJ on a histogram determined on 
     * the GPU to determine a threshold value as similar as possible to ImageJ 'Apply Threshold' method. 
     * 
     * Enter one 
     * of these methods in the method text field:
     * [Default, Huang, Intermodes, IsoData, IJ_IsoData, Li, MaxEntropy, Mean, MinError, Minimum, Moments, Otsu, Percentile, RenyiEntropy, Shanbhag, Triangle, Yen]
     */
    default double getAutomaticThreshold(ClearCLBuffer arg1, String arg2, double arg3, double arg4, double arg5) {
        if (doTimeTracing()) {recordMethodStart("GetAutomaticThreshold");}
        double result = GetAutomaticThreshold.getAutomaticThreshold(getCLIJ2(), arg1, arg2, new Double (arg3).floatValue(), new Double (arg4).floatValue(), new Double (arg5).intValue());
        if (doTimeTracing()) {recordMethodEnd("GetAutomaticThreshold");}
        return result;
    }


    // net.haesleinhuepf.clij2.plugins.GetDimensions
    //----------------------------------------------------
    /**
     * Reads out the size of an image [stack] and writes it to the variables 'width', 'height' and 'depth'.
     */
    default long[] getDimensions(ClearCLBuffer arg1) {
        if (doTimeTracing()) {recordMethodStart("GetDimensions");}
        long[] result = GetDimensions.getDimensions(getCLIJ2(), arg1);
        if (doTimeTracing()) {recordMethodEnd("GetDimensions");}
        return result;
    }


    // net.haesleinhuepf.clij2.plugins.CustomOperation
    //----------------------------------------------------
    /**
     * Executes a custom operation wirtten in OpenCL on a custom list of images. 
     * 
     * All images must be created before calling this method. Image parameters should be handed over as an array with parameter names and image names alternating, e.g.
     * 
     * Ext.CLIJ2_customOperation(..., ..., newArray("image1", "blobs.gif", "image2", "Processed_blobs.gif"))
     * 
     * In the custom code, you can use the predefined variables x, y and z to deal with coordinates.
     * You can for example use it to access pixel intensities like this:
     * 
     * float value = READ_IMAGE(image, sampler, POS_image_INSTANCE(x, y, z, 0)).x;
     * WRITE_IMAGE(image, POS_image_INSTANCE(x, y, z, 0), CONVERT_image_PIXEL_TYPE(value));
     * 
     * Note: replace `image` with the given image parameter name. You can furthermore use custom function to organise code in the global_code parameter. In OpenCL they may look like this:
     * 
     * inline float sum(float a, float b) {
     *     return a + b;
     * }
     * 
     */
    default boolean customOperation(String arg1, String arg2, HashMap arg3) {
        if (doTimeTracing()) {recordMethodStart("CustomOperation");}
        boolean result = CustomOperation.customOperation(getCLIJ2(), arg1, arg2, arg3);
        if (doTimeTracing()) {recordMethodEnd("CustomOperation");}
        return result;
    }


    // net.haesleinhuepf.clij2.plugins.PullLabelsToROIList
    //----------------------------------------------------
    /**
     * Pulls all labels in a label map as ROIs to a list. 
     * 
     * From ImageJ macro this list is written to the log 
     * window. From ImageJ macro conside using pullLabelsToROIManager.
     */
    default boolean pullLabelsToROIList(ClearCLBuffer arg1, List arg2) {
        if (doTimeTracing()) {recordMethodStart("PullLabelsToROIList");}
        boolean result = PullLabelsToROIList.pullLabelsToROIList(getCLIJ2(), arg1, arg2);
        if (doTimeTracing()) {recordMethodEnd("PullLabelsToROIList");}
        return result;
    }

    /**
     * Pulls all labels in a label map as ROIs to a list. 
     * 
     * From ImageJ macro this list is written to the log 
     * window. From ImageJ macro conside using pullLabelsToROIManager.
     */
    default ArrayList pullLabelsToROIList(ClearCLBuffer labelmap_input) {
        if (doTimeTracing()) {recordMethodStart("PullLabelsToROIList");}
        ArrayList result = PullLabelsToROIList.pullLabelsToROIList(getCLIJ2(), labelmap_input);
        if (doTimeTracing()) {recordMethodEnd("PullLabelsToROIList");}
        return result;
    }


    // net.haesleinhuepf.clij2.plugins.MeanOfTouchingNeighbors
    //----------------------------------------------------
    /**
     * Takes a touch matrix and a vector of values to determine the mean value among touching neighbors for every object. 
     * 
     * Parameters
     * ----------
     * values : Image
     *     A vector of values corresponding to the labels of which the mean average should be determined.
     * touch_matrix : Image
     *     A touch_matrix specifying which labels are taken into account for neighborhood relationships.
     * mean_values_destination : Image
     *     A the resulting vector of mean average values in the neighborhood.
     * 
     */
    default boolean meanOfTouchingNeighbors(ClearCLBuffer values, ClearCLBuffer touch_matrix, ClearCLBuffer mean_values_destination) {
        if (doTimeTracing()) {recordMethodStart("MeanOfTouchingNeighbors");}
        boolean result = MeanOfTouchingNeighbors.meanOfTouchingNeighbors(getCLIJ2(), values, touch_matrix, mean_values_destination);
        if (doTimeTracing()) {recordMethodEnd("MeanOfTouchingNeighbors");}
        return result;
    }


    // net.haesleinhuepf.clij2.plugins.MinimumOfTouchingNeighbors
    //----------------------------------------------------
    /**
     * Takes a touch matrix and a vector of values to determine the minimum value among touching neighbors for every object. 
     * 
     * Parameters
     * ----------
     * values : Image
     *     A vector of values corresponding to the labels of which the minimum should be determined.
     * touch_matrix : Image
     *     A touch_matrix specifying which labels are taken into account for neighborhood relationships.
     * minimum_values_destination : Image
     *     A the resulting vector of minimum values in the neighborhood.
     * 
     */
    default boolean minimumOfTouchingNeighbors(ClearCLBuffer values, ClearCLBuffer touch_matrix, ClearCLBuffer minimum_values_destination) {
        if (doTimeTracing()) {recordMethodStart("MinimumOfTouchingNeighbors");}
        boolean result = MinimumOfTouchingNeighbors.minimumOfTouchingNeighbors(getCLIJ2(), values, touch_matrix, minimum_values_destination);
        if (doTimeTracing()) {recordMethodEnd("MinimumOfTouchingNeighbors");}
        return result;
    }


    // net.haesleinhuepf.clij2.plugins.MaximumOfTouchingNeighbors
    //----------------------------------------------------
    /**
     * Takes a touch matrix and a vector of values to determine the maximum value among touching neighbors for every object. 
     * 
     * Parameters
     * ----------
     * values : Image
     *     A vector of values corresponding to the labels of which the maximum should be determined.
     * touch_matrix : Image
     *     A touch_matrix specifying which labels are taken into account for neighborhood relationships.
     * maximum_values_destination : Image
     *     A the resulting vector of maximum values in the neighborhood.
     * 
     */
    default boolean maximumOfTouchingNeighbors(ClearCLBuffer values, ClearCLBuffer touch_matrix, ClearCLBuffer maximum_values_destination) {
        if (doTimeTracing()) {recordMethodStart("MaximumOfTouchingNeighbors");}
        boolean result = MaximumOfTouchingNeighbors.maximumOfTouchingNeighbors(getCLIJ2(), values, touch_matrix, maximum_values_destination);
        if (doTimeTracing()) {recordMethodEnd("MaximumOfTouchingNeighbors");}
        return result;
    }


    // net.haesleinhuepf.clij2.plugins.ResultsTableColumnToImage
    //----------------------------------------------------
    /**
     * Converts a table column to an image. 
     * 
     * The values are stored in x dimension.
     */
    default boolean resultsTableColumnToImage(ClearCLBuffer arg1, ResultsTable arg2, String arg3) {
        if (doTimeTracing()) {recordMethodStart("ResultsTableColumnToImage");}
        boolean result = ResultsTableColumnToImage.resultsTableColumnToImage(getCLIJ2(), arg1, arg2, arg3);
        if (doTimeTracing()) {recordMethodEnd("ResultsTableColumnToImage");}
        return result;
    }


    // net.haesleinhuepf.clij2.plugins.StatisticsOfBackgroundAndLabelledPixels
    //----------------------------------------------------
    /**
     * Determines bounding box, area (in pixels/voxels), min, max and mean intensity 
     *  of background and labelled objects in a label map and corresponding pixels in the original image.
     * 
     * Instead of a label map, you can also use a binary image as a binary image is a label map with just one label.
     * 
     * This method is executed on the CPU and not on the GPU/OpenCL device.
     */
    default double[][] statisticsOfBackgroundAndLabelledPixels(ClearCLBuffer input, ClearCLBuffer labelmap) {
        if (doTimeTracing()) {recordMethodStart("StatisticsOfBackgroundAndLabelledPixels");}
        double[][] result = StatisticsOfBackgroundAndLabelledPixels.statisticsOfBackgroundAndLabelledPixels(getCLIJ2(), input, labelmap);
        if (doTimeTracing()) {recordMethodEnd("StatisticsOfBackgroundAndLabelledPixels");}
        return result;
    }

    /**
     * Determines bounding box, area (in pixels/voxels), min, max and mean intensity 
     *  of background and labelled objects in a label map and corresponding pixels in the original image.
     * 
     * Instead of a label map, you can also use a binary image as a binary image is a label map with just one label.
     * 
     * This method is executed on the CPU and not on the GPU/OpenCL device.
     */
    default ResultsTable statisticsOfBackgroundAndLabelledPixels(ClearCLBuffer arg1, ClearCLBuffer arg2, ResultsTable arg3) {
        if (doTimeTracing()) {recordMethodStart("StatisticsOfBackgroundAndLabelledPixels");}
        ResultsTable result = StatisticsOfBackgroundAndLabelledPixels.statisticsOfBackgroundAndLabelledPixels(getCLIJ2(), arg1, arg2, arg3);
        if (doTimeTracing()) {recordMethodEnd("StatisticsOfBackgroundAndLabelledPixels");}
        return result;
    }


    // net.haesleinhuepf.clij2.plugins.GetGPUProperties
    //----------------------------------------------------

    // net.haesleinhuepf.clij2.plugins.GetSumOfAllPixels
    //----------------------------------------------------
    /**
     * Determines the sum of all pixels in a given image. 
     * 
     * It will be stored in the variable sum_of_all_pixels.
     */
    default double getSumOfAllPixels(ClearCLImageInterface arg1) {
        if (doTimeTracing()) {recordMethodStart("GetSumOfAllPixels");}
        double result = GetSumOfAllPixels.getSumOfAllPixels(getCLIJ2(), arg1);
        if (doTimeTracing()) {recordMethodEnd("GetSumOfAllPixels");}
        return result;
    }


    // net.haesleinhuepf.clij2.plugins.GetSorensenDiceCoefficient
    //----------------------------------------------------
    /**
     * Determines the overlap of two binary images using the Sorensen-Dice coefficent. 
     * 
     * A value of 0 suggests no overlap, 1 means perfect overlap.
     * The Sorensen-Dice coefficient is saved in the colum 'Sorensen_Dice_coefficient'.
     * Note that the Sorensen-Dice coefficient s can be calculated from the Jaccard index j using this formula:
     * <pre>s = f(j) = 2 j / (j + 1)</pre>
     */
    default double getSorensenDiceCoefficient(ClearCLBuffer arg1, ClearCLBuffer arg2) {
        if (doTimeTracing()) {recordMethodStart("GetSorensenDiceCoefficient");}
        double result = GetSorensenDiceCoefficient.getSorensenDiceCoefficient(getCLIJ2(), arg1, arg2);
        if (doTimeTracing()) {recordMethodEnd("GetSorensenDiceCoefficient");}
        return result;
    }


    // net.haesleinhuepf.clij2.plugins.GetMinimumOfAllPixels
    //----------------------------------------------------
    /**
     * Determines the minimum of all pixels in a given image. 
     * 
     * It will be stored in the variable minimum_of_all_pixels.
     */
    default double getMinimumOfAllPixels(ClearCLImageInterface arg1) {
        if (doTimeTracing()) {recordMethodStart("GetMinimumOfAllPixels");}
        double result = GetMinimumOfAllPixels.getMinimumOfAllPixels(getCLIJ2(), arg1);
        if (doTimeTracing()) {recordMethodEnd("GetMinimumOfAllPixels");}
        return result;
    }


    // net.haesleinhuepf.clij2.plugins.GetMaximumOfAllPixels
    //----------------------------------------------------
    /**
     * Determines the maximum of all pixels in a given image. 
     * 
     * It will be stored in the variable maximum_of_all_pixels.
     */
    default double getMaximumOfAllPixels(ClearCLImageInterface arg1) {
        if (doTimeTracing()) {recordMethodStart("GetMaximumOfAllPixels");}
        double result = GetMaximumOfAllPixels.getMaximumOfAllPixels(getCLIJ2(), arg1);
        if (doTimeTracing()) {recordMethodEnd("GetMaximumOfAllPixels");}
        return result;
    }


    // net.haesleinhuepf.clij2.plugins.GetMeanOfAllPixels
    //----------------------------------------------------
    /**
     * Determines the mean of all pixels in a given image. 
     * 
     * It will be stored in the variable mean_of_all_pixels.
     */
    default double getMeanOfAllPixels(ClearCLImageInterface arg1) {
        if (doTimeTracing()) {recordMethodStart("GetMeanOfAllPixels");}
        double result = GetMeanOfAllPixels.getMeanOfAllPixels(getCLIJ2(), arg1);
        if (doTimeTracing()) {recordMethodEnd("GetMeanOfAllPixels");}
        return result;
    }


    // net.haesleinhuepf.clij2.plugins.GetJaccardIndex
    //----------------------------------------------------
    /**
     * Determines the overlap of two binary images using the Jaccard index. 
     * 
     * A value of 0 suggests no overlap, 1 means perfect overlap.
     * The resulting Jaccard index is saved to the results table in the 'Jaccard_Index' column.
     * Note that the Sorensen-Dice coefficient can be calculated from the Jaccard index j using this formula:
     * <pre>s = f(j) = 2 j / (j + 1)</pre>
     */
    default double getJaccardIndex(ClearCLBuffer arg1, ClearCLBuffer arg2) {
        if (doTimeTracing()) {recordMethodStart("GetJaccardIndex");}
        double result = GetJaccardIndex.getJaccardIndex(getCLIJ2(), arg1, arg2);
        if (doTimeTracing()) {recordMethodEnd("GetJaccardIndex");}
        return result;
    }


    // net.haesleinhuepf.clij2.plugins.GetCenterOfMass
    //----------------------------------------------------
    /**
     * Determines the center of mass of an image or image stack.
     * 
     *  It writes the result in the variables
     *  centerOfMassX, centerOfMassY and centerOfMassZ.
     */
    default double[] getCenterOfMass(ClearCLBuffer arg1) {
        if (doTimeTracing()) {recordMethodStart("GetCenterOfMass");}
        double[] result = GetCenterOfMass.getCenterOfMass(getCLIJ2(), arg1);
        if (doTimeTracing()) {recordMethodEnd("GetCenterOfMass");}
        return result;
    }


    // net.haesleinhuepf.clij2.plugins.GetBoundingBox
    //----------------------------------------------------
    /**
     * Determines the bounding box of all non-zero pixels in a binary image. 
     * 
     * If called from macro, the positions will be stored in the variables 'boundingBoxX', 'boundingBoxY', 'boundingBoxZ', 'boundingBoxWidth', 'boundingBoxHeight' and 'boundingBoxDepth'.In case of 2D images Z and depth will be zero.
     */
    default double[] getBoundingBox(ClearCLBuffer arg1) {
        if (doTimeTracing()) {recordMethodStart("GetBoundingBox");}
        double[] result = GetBoundingBox.getBoundingBox(getCLIJ2(), arg1);
        if (doTimeTracing()) {recordMethodEnd("GetBoundingBox");}
        return result;
    }


    // net.haesleinhuepf.clij2.plugins.PushArray
    //----------------------------------------------------
    /**
     * Converts an array to an image.
     */
    default boolean pushArray(ClearCLBuffer arg1, Object arg2) {
        if (doTimeTracing()) {recordMethodStart("PushArray");}
        boolean result = PushArray.pushArray(getCLIJ2(), arg1, arg2);
        if (doTimeTracing()) {recordMethodEnd("PushArray");}
        return result;
    }

    /**
     * Converts an array to an image.
     */
    default ClearCLBuffer pushArray(float[] arg1, double arg2, double arg3, double arg4) {
        if (doTimeTracing()) {recordMethodStart("PushArray");}
        ClearCLBuffer result = PushArray.pushArray(getCLIJ2(), arg1, new Double (arg2).intValue(), new Double (arg3).intValue(), new Double (arg4).intValue());
        if (doTimeTracing()) {recordMethodEnd("PushArray");}
        return result;
    }


    // net.haesleinhuepf.clij2.plugins.PullString
    //----------------------------------------------------
    /**
     * Writes an image into a string.
     */
    default String pullString(ClearCLImageInterface arg1) {
        if (doTimeTracing()) {recordMethodStart("PullString");}
        String result = PullString.pullString(getCLIJ2(), arg1);
        if (doTimeTracing()) {recordMethodEnd("PullString");}
        return result;
    }


    // net.haesleinhuepf.clij2.plugins.PushString
    //----------------------------------------------------
    /**
     * Converts an string to an image. 
     * 
     * The formatting works with double line breaks for slice switches, single line breaks for y swithces and 
     * spaces for x. For example this string is converted to an image with width=4, height=3 and depth=2:
     * 
     * 1 2 3 4
     * 5 6 7 8
     * 9 0 1 2
     * 
     * 3 4 5 6
     * 7 8 9 0
     * 1 2 3 4
     * 
     */
    default boolean pushString(ClearCLBuffer arg1, String arg2) {
        if (doTimeTracing()) {recordMethodStart("PushString");}
        boolean result = PushString.pushString(getCLIJ2(), arg1, arg2);
        if (doTimeTracing()) {recordMethodEnd("PushString");}
        return result;
    }

    /**
     * Converts an string to an image. 
     * 
     * The formatting works with double line breaks for slice switches, single line breaks for y swithces and 
     * spaces for x. For example this string is converted to an image with width=4, height=3 and depth=2:
     * 
     * 1 2 3 4
     * 5 6 7 8
     * 9 0 1 2
     * 
     * 3 4 5 6
     * 7 8 9 0
     * 1 2 3 4
     * 
     */
    default ClearCLBuffer pushString(String arg1) {
        if (doTimeTracing()) {recordMethodStart("PushString");}
        ClearCLBuffer result = PushString.pushString(getCLIJ2(), arg1);
        if (doTimeTracing()) {recordMethodEnd("PushString");}
        return result;
    }


    // net.haesleinhuepf.clij2.plugins.MedianOfTouchingNeighbors
    //----------------------------------------------------
    /**
     * Takes a touch matrix and a vector of values to determine the median value among touching neighbors for every object. 
     * 
     * 
     */
    default boolean medianOfTouchingNeighbors(ClearCLBuffer values, ClearCLBuffer touch_matrix, ClearCLBuffer median_values_destination) {
        if (doTimeTracing()) {recordMethodStart("MedianOfTouchingNeighbors");}
        boolean result = MedianOfTouchingNeighbors.medianOfTouchingNeighbors(getCLIJ2(), values, touch_matrix, median_values_destination);
        if (doTimeTracing()) {recordMethodEnd("MedianOfTouchingNeighbors");}
        return result;
    }


    // net.haesleinhuepf.clij2.plugins.PushResultsTableColumn
    //----------------------------------------------------
    /**
     * Converts a table column to an image. 
     * 
     * The values are stored in x dimension.
     */
    default boolean pushResultsTableColumn(ClearCLBuffer arg1, ResultsTable arg2, String arg3) {
        if (doTimeTracing()) {recordMethodStart("PushResultsTableColumn");}
        boolean result = PushResultsTableColumn.pushResultsTableColumn(getCLIJ2(), arg1, arg2, arg3);
        if (doTimeTracing()) {recordMethodEnd("PushResultsTableColumn");}
        return result;
    }


    // net.haesleinhuepf.clij2.plugins.PushResultsTable
    //----------------------------------------------------
    /**
     * Converts a table to an image. 
     * 
     * Rows stay rows, columns stay columns.
     */
    default boolean pushResultsTable(ClearCLBuffer arg1, ResultsTable arg2) {
        if (doTimeTracing()) {recordMethodStart("PushResultsTable");}
        boolean result = PushResultsTable.pushResultsTable(getCLIJ2(), arg1, arg2);
        if (doTimeTracing()) {recordMethodEnd("PushResultsTable");}
        return result;
    }


    // net.haesleinhuepf.clij2.plugins.PullToResultsTable
    //----------------------------------------------------
    /**
     * Converts an image into a table.
     */
    default ResultsTable pullToResultsTable(ClearCLBuffer arg1, ResultsTable arg2) {
        if (doTimeTracing()) {recordMethodStart("PullToResultsTable");}
        ResultsTable result = PullToResultsTable.pullToResultsTable(getCLIJ2(), arg1, arg2);
        if (doTimeTracing()) {recordMethodEnd("PullToResultsTable");}
        return result;
    }

    /**
     * Converts an image into a table.
     */
    default ResultsTable pullToResultsTable(ClearCLImage arg1, ResultsTable arg2) {
        if (doTimeTracing()) {recordMethodStart("PullToResultsTable");}
        ResultsTable result = PullToResultsTable.pullToResultsTable(getCLIJ2(), arg1, arg2);
        if (doTimeTracing()) {recordMethodEnd("PullToResultsTable");}
        return result;
    }


    // net.haesleinhuepf.clij2.plugins.LabelVoronoiOctagon
    //----------------------------------------------------
    /**
     * Takes a labelled image and dilates the labels using a octagon shape until they touch. 
     * 
     * The pixels where  the regions touched are afterwards returned as binary image which corresponds to the Voronoi diagram.
     */
    default boolean labelVoronoiOctagon(ClearCLBuffer label_map, ClearCLBuffer label_voronoi_destination) {
        if (doTimeTracing()) {recordMethodStart("LabelVoronoiOctagon");}
        boolean result = LabelVoronoiOctagon.labelVoronoiOctagon(getCLIJ2(), label_map, label_voronoi_destination);
        if (doTimeTracing()) {recordMethodEnd("LabelVoronoiOctagon");}
        return result;
    }


    // net.haesleinhuepf.clij2.plugins.TouchMatrixToAdjacencyMatrix
    //----------------------------------------------------
    /**
     * Converts a touch matrix in an adjacency matrix
     */
    default boolean touchMatrixToAdjacencyMatrix(ClearCLBuffer touch_matrix, ClearCLBuffer adjacency_matrix) {
        if (doTimeTracing()) {recordMethodStart("TouchMatrixToAdjacencyMatrix");}
        boolean result = TouchMatrixToAdjacencyMatrix.touchMatrixToAdjacencyMatrix(getCLIJ2(), touch_matrix, adjacency_matrix);
        if (doTimeTracing()) {recordMethodEnd("TouchMatrixToAdjacencyMatrix");}
        return result;
    }


    // net.haesleinhuepf.clij2.plugins.AdjacencyMatrixToTouchMatrix
    //----------------------------------------------------
    /**
     * Converts a adjacency matrix in a touch matrix.
     * 
     * An adjacency matrix is symmetric while a touch matrix is typically not.
     * 
     * Parameters
     * ----------
     * adjacency_matrix : Image
     *     The input adjacency matrix to be read from.
     * touch_matrix : Image
     *     The output touch matrix to be written into.
     * 
     */
    default boolean adjacencyMatrixToTouchMatrix(ClearCLBuffer adjacency_matrix, ClearCLBuffer touch_matrix) {
        if (doTimeTracing()) {recordMethodStart("AdjacencyMatrixToTouchMatrix");}
        boolean result = AdjacencyMatrixToTouchMatrix.adjacencyMatrixToTouchMatrix(getCLIJ2(), adjacency_matrix, touch_matrix);
        if (doTimeTracing()) {recordMethodEnd("AdjacencyMatrixToTouchMatrix");}
        return result;
    }


    // net.haesleinhuepf.clij2.plugins.PointlistToLabelledSpots
    //----------------------------------------------------
    /**
     * Takes a pointlist with dimensions n times d with n point coordinates in d dimensions and labels corresponding pixels.
     */
    default boolean pointlistToLabelledSpots(ClearCLBuffer pointlist, ClearCLBuffer spots_destination) {
        if (doTimeTracing()) {recordMethodStart("PointlistToLabelledSpots");}
        boolean result = PointlistToLabelledSpots.pointlistToLabelledSpots(getCLIJ2(), pointlist, spots_destination);
        if (doTimeTracing()) {recordMethodEnd("PointlistToLabelledSpots");}
        return result;
    }


    // net.haesleinhuepf.clij2.plugins.StatisticsOfImage
    //----------------------------------------------------
    /**
     * Determines image size (bounding box), area (in pixels/voxels), min, max and mean intensity 
     *  of all pixels in the original image.
     * 
     * This method is executed on the CPU and not on the GPU/OpenCL device.
     */
    default ResultsTable statisticsOfImage(ClearCLBuffer arg1, ResultsTable arg2) {
        if (doTimeTracing()) {recordMethodStart("StatisticsOfImage");}
        ResultsTable result = StatisticsOfImage.statisticsOfImage(getCLIJ2(), arg1, arg2);
        if (doTimeTracing()) {recordMethodEnd("StatisticsOfImage");}
        return result;
    }


    // net.haesleinhuepf.clij2.plugins.NClosestDistances
    //----------------------------------------------------
    /**
     * Determine the n point indices with shortest distance for all points in a distance matrix. 
     * 
     * This corresponds to the n row indices with minimum values for each column of the distance matrix.Returns the n shortest distances in one image and the point indices in another image.
     */
    default boolean nClosestDistances(ClearCLBuffer arg1, ClearCLBuffer arg2, ClearCLBuffer arg3) {
        if (doTimeTracing()) {recordMethodStart("NClosestDistances");}
        boolean result = NClosestDistances.nClosestDistances(getCLIJ2(), arg1, arg2, arg3);
        if (doTimeTracing()) {recordMethodEnd("NClosestDistances");}
        return result;
    }


    // net.haesleinhuepf.clij2.plugins.ExcludeLabels
    //----------------------------------------------------
    /**
     * This operation removes labels from a labelmap and renumbers the remaining labels. 
     * 
     * Hand over a binary flag list vector starting with a flag for the background, continuing with label1, label2, ...
     * 
     * For example if you pass 0,1,0,0,1: Labels 1 and 4 will be removed (those with a 1 in the vector will be excluded). Labels 2 and 3 will be kept and renumbered to 1 and 2.
     */
    default boolean excludeLabels(ClearCLBuffer binary_flaglist, ClearCLBuffer label_map_input, ClearCLBuffer label_map_destination) {
        if (doTimeTracing()) {recordMethodStart("ExcludeLabels");}
        boolean result = ExcludeLabels.excludeLabels(getCLIJ2(), binary_flaglist, label_map_input, label_map_destination);
        if (doTimeTracing()) {recordMethodEnd("ExcludeLabels");}
        return result;
    }


    // net.haesleinhuepf.clij2.plugins.AverageDistanceOfNFarOffPoints
    //----------------------------------------------------
    /**
     * Determines the average of the n far off (most distant) points for every point in a distance matrix.
     * 
     * This corresponds to the average of the n maximum values (rows) for each column of the distance matrix.
     */
    default boolean averageDistanceOfNFarOffPoints(ClearCLBuffer arg1, ClearCLBuffer arg2, double arg3) {
        if (doTimeTracing()) {recordMethodStart("AverageDistanceOfNFarOffPoints");}
        boolean result = AverageDistanceOfNFarOffPoints.averageDistanceOfNFarOffPoints(getCLIJ2(), arg1, arg2, new Double (arg3).intValue());
        if (doTimeTracing()) {recordMethodEnd("AverageDistanceOfNFarOffPoints");}
        return result;
    }


    // net.haesleinhuepf.clij2.plugins.StandardDeviationOfTouchingNeighbors
    //----------------------------------------------------
    /**
     * Takes a touch matrix and a vector of values to determine the standard deviation value among touching neighbors for every object. 
     * 
     * 
     */
    default boolean standardDeviationOfTouchingNeighbors(ClearCLBuffer values, ClearCLBuffer touch_matrix, ClearCLBuffer standard_deviation_values_destination) {
        if (doTimeTracing()) {recordMethodStart("StandardDeviationOfTouchingNeighbors");}
        boolean result = StandardDeviationOfTouchingNeighbors.standardDeviationOfTouchingNeighbors(getCLIJ2(), values, touch_matrix, standard_deviation_values_destination);
        if (doTimeTracing()) {recordMethodEnd("StandardDeviationOfTouchingNeighbors");}
        return result;
    }


    // net.haesleinhuepf.clij2.plugins.NeighborsOfNeighbors
    //----------------------------------------------------
    /**
     * Determines neighbors of neigbors from touch matrix and saves the result as a new touch matrix.
     */
    default boolean neighborsOfNeighbors(ClearCLBuffer touch_matrix, ClearCLBuffer neighbor_matrix_destination) {
        if (doTimeTracing()) {recordMethodStart("NeighborsOfNeighbors");}
        boolean result = NeighborsOfNeighbors.neighborsOfNeighbors(getCLIJ2(), touch_matrix, neighbor_matrix_destination);
        if (doTimeTracing()) {recordMethodEnd("NeighborsOfNeighbors");}
        return result;
    }


    // net.haesleinhuepf.clij2.plugins.GenerateParametricImage
    //----------------------------------------------------
    /**
     * Take a labelmap and a vector of values to replace label 1 with the 1st value in the vector. 
     * 
     * Note that indexing in the vector starts at zero. The 0th entry corresponds to background in the label map.Internally this method just calls ReplaceIntensities.
     * 
     */
    default boolean generateParametricImage(ClearCLImageInterface label_map, ClearCLImageInterface parameter_value_vector, ClearCLImageInterface parametric_image_destination) {
        if (doTimeTracing()) {recordMethodStart("GenerateParametricImage");}
        boolean result = GenerateParametricImage.generateParametricImage(getCLIJ2(), label_map, parameter_value_vector, parametric_image_destination);
        if (doTimeTracing()) {recordMethodEnd("GenerateParametricImage");}
        return result;
    }


    // net.haesleinhuepf.clij2.plugins.GenerateParametricImageFromResultsTableColumn
    //----------------------------------------------------
    /**
     * Take a labelmap and a column from the results table to replace label 1 with the 1st value in the vector. 
     * 
     * Note that indexing in the table column starts at zero. The results table should contain a line at the beginningrepresenting the background.
     * 
     */
    default boolean generateParametricImageFromResultsTableColumn(ClearCLImageInterface arg1, ClearCLImageInterface arg2, ResultsTable arg3, String arg4) {
        if (doTimeTracing()) {recordMethodStart("GenerateParametricImageFromResultsTableColumn");}
        boolean result = GenerateParametricImageFromResultsTableColumn.generateParametricImageFromResultsTableColumn(getCLIJ2(), arg1, arg2, arg3, arg4);
        if (doTimeTracing()) {recordMethodEnd("GenerateParametricImageFromResultsTableColumn");}
        return result;
    }


    // net.haesleinhuepf.clij2.plugins.ExcludeLabelsWithValuesOutOfRange
    //----------------------------------------------------
    /**
     * This operation removes labels from a labelmap and renumbers the remaining labels. 
     * 
     * Hand over a vector of values and a range specifying which labels with which values are eliminated.
     */
    default boolean excludeLabelsWithValuesOutOfRange(ClearCLBuffer arg1, ClearCLBuffer arg2, ClearCLBuffer arg3, double arg4, double arg5) {
        if (doTimeTracing()) {recordMethodStart("ExcludeLabelsWithValuesOutOfRange");}
        boolean result = ExcludeLabelsWithValuesOutOfRange.excludeLabelsWithValuesOutOfRange(getCLIJ2(), arg1, arg2, arg3, new Double (arg4).floatValue(), new Double (arg5).floatValue());
        if (doTimeTracing()) {recordMethodEnd("ExcludeLabelsWithValuesOutOfRange");}
        return result;
    }


    // net.haesleinhuepf.clij2.plugins.ExcludeLabelsWithValuesWithinRange
    //----------------------------------------------------
    /**
     * This operation removes labels from a labelmap and renumbers the remaining labels. 
     * 
     * Hand over a vector of values and a range specifying which labels with which values are eliminated.
     */
    default boolean excludeLabelsWithValuesWithinRange(ClearCLBuffer arg1, ClearCLBuffer arg2, ClearCLBuffer arg3, double arg4, double arg5) {
        if (doTimeTracing()) {recordMethodStart("ExcludeLabelsWithValuesWithinRange");}
        boolean result = ExcludeLabelsWithValuesWithinRange.excludeLabelsWithValuesWithinRange(getCLIJ2(), arg1, arg2, arg3, new Double (arg4).floatValue(), new Double (arg5).floatValue());
        if (doTimeTracing()) {recordMethodEnd("ExcludeLabelsWithValuesWithinRange");}
        return result;
    }


    // net.haesleinhuepf.clij2.plugins.CombineVertically
    //----------------------------------------------------
    /**
     * Combines two images or stacks in Y.
     */
    default boolean combineVertically(ClearCLImageInterface stack1, ClearCLImageInterface stack2, ClearCLImageInterface destination) {
        if (doTimeTracing()) {recordMethodStart("CombineVertically");}
        boolean result = CombineVertically.combineVertically(getCLIJ2(), stack1, stack2, destination);
        if (doTimeTracing()) {recordMethodEnd("CombineVertically");}
        return result;
    }


    // net.haesleinhuepf.clij2.plugins.CombineHorizontally
    //----------------------------------------------------
    /**
     * Combines two images or stacks in X.
     */
    default boolean combineHorizontally(ClearCLImageInterface stack1, ClearCLImageInterface stack2, ClearCLImageInterface destination) {
        if (doTimeTracing()) {recordMethodStart("CombineHorizontally");}
        boolean result = CombineHorizontally.combineHorizontally(getCLIJ2(), stack1, stack2, destination);
        if (doTimeTracing()) {recordMethodEnd("CombineHorizontally");}
        return result;
    }


    // net.haesleinhuepf.clij2.plugins.ReduceStack
    //----------------------------------------------------
    /**
     * Reduces the number of slices in a stack by a given factor.
     * With the offset you have control which slices stay: 
     * * With factor 3 and offset 0, slices 0, 3, 6,... are kept. * With factor 4 and offset 1, slices 1, 5, 9,... are kept.
     */
    default boolean reduceStack(ClearCLImageInterface arg1, ClearCLImageInterface arg2, double arg3, double arg4) {
        if (doTimeTracing()) {recordMethodStart("ReduceStack");}
        boolean result = ReduceStack.reduceStack(getCLIJ2(), arg1, arg2, new Double (arg3).intValue(), new Double (arg4).intValue());
        if (doTimeTracing()) {recordMethodEnd("ReduceStack");}
        return result;
    }


    // net.haesleinhuepf.clij2.plugins.DetectMinima2DBox
    //----------------------------------------------------
    /**
     * Detects local minima in a given square/cubic neighborhood. 
     * 
     * Pixels in the resulting image are set to 1 if there is no other pixel in a given radius which has a 
     * lower intensity, and to 0 otherwise.
     */
    default boolean detectMinima2DBox(ClearCLBuffer arg1, ClearCLBuffer arg2, double arg3, double arg4) {
        if (doTimeTracing()) {recordMethodStart("DetectMinima2DBox");}
        boolean result = DetectMinima2DBox.detectMinima2DBox(getCLIJ2(), arg1, arg2, new Double (arg3).intValue(), new Double (arg4).intValue());
        if (doTimeTracing()) {recordMethodEnd("DetectMinima2DBox");}
        return result;
    }


    // net.haesleinhuepf.clij2.plugins.DetectMaxima2DBox
    //----------------------------------------------------
    /**
     * Detects local maxima in a given square/cubic neighborhood. 
     * 
     * Pixels in the resulting image are set to 1 if there is no other pixel in a given radius which has a 
     * higher intensity, and to 0 otherwise.
     */
    default boolean detectMaxima2DBox(ClearCLBuffer arg1, ClearCLBuffer arg2, double arg3, double arg4) {
        if (doTimeTracing()) {recordMethodStart("DetectMaxima2DBox");}
        boolean result = DetectMaxima2DBox.detectMaxima2DBox(getCLIJ2(), arg1, arg2, new Double (arg3).intValue(), new Double (arg4).intValue());
        if (doTimeTracing()) {recordMethodEnd("DetectMaxima2DBox");}
        return result;
    }


    // net.haesleinhuepf.clij2.plugins.DetectMinima3DBox
    //----------------------------------------------------
    /**
     * Detects local minima in a given square/cubic neighborhood. 
     * 
     * Pixels in the resulting image are set to 1 if there is no other pixel in a given radius which has a 
     * lower intensity, and to 0 otherwise.
     */
    default boolean detectMinima3DBox(ClearCLBuffer arg1, ClearCLBuffer arg2, double arg3, double arg4, double arg5) {
        if (doTimeTracing()) {recordMethodStart("DetectMinima3DBox");}
        boolean result = DetectMinima3DBox.detectMinima3DBox(getCLIJ2(), arg1, arg2, new Double (arg3).intValue(), new Double (arg4).intValue(), new Double (arg5).intValue());
        if (doTimeTracing()) {recordMethodEnd("DetectMinima3DBox");}
        return result;
    }


    // net.haesleinhuepf.clij2.plugins.DetectMaxima3DBox
    //----------------------------------------------------
    /**
     * Detects local maxima in a given square/cubic neighborhood. 
     * 
     * Pixels in the resulting image are set to 1 if there is no other pixel in a given radius which has a 
     * higher intensity, and to 0 otherwise.
     */
    default boolean detectMaxima3DBox(ClearCLBuffer arg1, ClearCLBuffer arg2, double arg3, double arg4, double arg5) {
        if (doTimeTracing()) {recordMethodStart("DetectMaxima3DBox");}
        boolean result = DetectMaxima3DBox.detectMaxima3DBox(getCLIJ2(), arg1, arg2, new Double (arg3).intValue(), new Double (arg4).intValue(), new Double (arg5).intValue());
        if (doTimeTracing()) {recordMethodEnd("DetectMaxima3DBox");}
        return result;
    }


    // net.haesleinhuepf.clij2.plugins.DepthColorProjection
    //----------------------------------------------------
    /**
     * Determines a maximum projection of an image stack and does a color coding of the determined arg Z (position of the found maximum). 
     * 
     * Second parameter is a Lookup-Table in the form of an 8-bit image stack 255 pixels wide, 1 pixel high with 3 planes representing red, green and blue intensities.
     * Resulting image is a 3D image with three Z-planes representing red, green and blue channels.
     */
    default boolean depthColorProjection(ClearCLImageInterface arg1, ClearCLBuffer arg2, ClearCLBuffer arg3, double arg4, double arg5) {
        if (doTimeTracing()) {recordMethodStart("DepthColorProjection");}
        boolean result = DepthColorProjection.depthColorProjection(getCLIJ2(), arg1, arg2, arg3, new Double (arg4).floatValue(), new Double (arg5).floatValue());
        if (doTimeTracing()) {recordMethodEnd("DepthColorProjection");}
        return result;
    }


    // net.haesleinhuepf.clij2.plugins.GenerateBinaryOverlapMatrix
    //----------------------------------------------------
    /**
     * Takes two labelmaps with n and m labels and generates a (n+1)*(m+1) matrix where all pixels are set to 0 exept those where labels overlap between the label maps. 
     * 
     * For example, if labels 3 in labelmap1 and 4 in labelmap2 are touching then the pixel (3,4) in the matrix will be set to 1.
     */
    default boolean generateBinaryOverlapMatrix(ClearCLBuffer label_map1, ClearCLBuffer label_map2, ClearCLBuffer binary_overlap_matrix_destination) {
        if (doTimeTracing()) {recordMethodStart("GenerateBinaryOverlapMatrix");}
        boolean result = GenerateBinaryOverlapMatrix.generateBinaryOverlapMatrix(getCLIJ2(), label_map1, label_map2, binary_overlap_matrix_destination);
        if (doTimeTracing()) {recordMethodEnd("GenerateBinaryOverlapMatrix");}
        return result;
    }


    // net.haesleinhuepf.clij2.plugins.ResliceRadialTop
    //----------------------------------------------------

    // net.haesleinhuepf.clij2.plugins.Convolve
    //----------------------------------------------------
    /**
     * Convolve the image with a given kernel image.
     * 
     * It is recommended that the kernel image has an odd size in X, Y and Z.
     */
    default boolean convolve(ClearCLBuffer source, ClearCLBuffer convolution_kernel, ClearCLBuffer destination) {
        if (doTimeTracing()) {recordMethodStart("Convolve");}
        boolean result = Convolve.convolve(getCLIJ2(), source, convolution_kernel, destination);
        if (doTimeTracing()) {recordMethodEnd("Convolve");}
        return result;
    }


    // net.haesleinhuepf.clij2.plugins.UndefinedToZero
    //----------------------------------------------------
    /**
     * Copies all pixels instead those which are not a number (NaN) or infinity (inf), which are replaced by 0.
     */
    default boolean undefinedToZero(ClearCLBuffer source, ClearCLBuffer destination) {
        if (doTimeTracing()) {recordMethodStart("UndefinedToZero");}
        boolean result = UndefinedToZero.undefinedToZero(getCLIJ2(), source, destination);
        if (doTimeTracing()) {recordMethodEnd("UndefinedToZero");}
        return result;
    }


    // net.haesleinhuepf.clij2.plugins.GenerateJaccardIndexMatrix
    //----------------------------------------------------
    /**
     * Takes two labelmaps with n and m labels_2 and generates a (n+1)*(m+1) matrix where all labels_1 are set to 0 exept those where labels_2 overlap between the label maps. 
     * 
     * For the remaining labels_1, the value will be between 0 and 1 indicating the overlap as measured by the Jaccard Index.
     * Major parts of this operation run on the CPU.
     */
    default boolean generateJaccardIndexMatrix(ClearCLBuffer label_map1, ClearCLBuffer label_map2, ClearCLBuffer jaccard_index_matrix_destination) {
        if (doTimeTracing()) {recordMethodStart("GenerateJaccardIndexMatrix");}
        boolean result = GenerateJaccardIndexMatrix.generateJaccardIndexMatrix(getCLIJ2(), label_map1, label_map2, jaccard_index_matrix_destination);
        if (doTimeTracing()) {recordMethodEnd("GenerateJaccardIndexMatrix");}
        return result;
    }


    // net.haesleinhuepf.clij2.plugins.GenerateTouchCountMatrix
    //----------------------------------------------------
    /**
     * Takes a label map with n labels and generates a (n+1)*(n+1) matrix where all pixels are set the number of pixels where labels touch (diamond neighborhood). 
     * 
     * Major parts of this operation run on the CPU.
     */
    default boolean generateTouchCountMatrix(ClearCLBuffer label_map, ClearCLBuffer touch_count_matrix_destination) {
        if (doTimeTracing()) {recordMethodStart("GenerateTouchCountMatrix");}
        boolean result = GenerateTouchCountMatrix.generateTouchCountMatrix(getCLIJ2(), label_map, touch_count_matrix_destination);
        if (doTimeTracing()) {recordMethodEnd("GenerateTouchCountMatrix");}
        return result;
    }


    // net.haesleinhuepf.clij2.plugins.MinimumXProjection
    //----------------------------------------------------
    /**
     * Determines the minimum intensity projection of an image along Y.
     */
    default boolean minimumXProjection(ClearCLImageInterface source, ClearCLImageInterface destination_min) {
        if (doTimeTracing()) {recordMethodStart("MinimumXProjection");}
        boolean result = MinimumXProjection.minimumXProjection(getCLIJ2(), source, destination_min);
        if (doTimeTracing()) {recordMethodEnd("MinimumXProjection");}
        return result;
    }


    // net.haesleinhuepf.clij2.plugins.MinimumYProjection
    //----------------------------------------------------
    /**
     * Determines the minimum intensity projection of an image along Y.
     */
    default boolean minimumYProjection(ClearCLImageInterface source, ClearCLImageInterface destination_min) {
        if (doTimeTracing()) {recordMethodStart("MinimumYProjection");}
        boolean result = MinimumYProjection.minimumYProjection(getCLIJ2(), source, destination_min);
        if (doTimeTracing()) {recordMethodEnd("MinimumYProjection");}
        return result;
    }


    // net.haesleinhuepf.clij2.plugins.MeanXProjection
    //----------------------------------------------------
    /**
     * Determines the mean average intensity projection of an image along X.
     */
    default boolean meanXProjection(ClearCLImageInterface source, ClearCLImageInterface destination) {
        if (doTimeTracing()) {recordMethodStart("MeanXProjection");}
        boolean result = MeanXProjection.meanXProjection(getCLIJ2(), source, destination);
        if (doTimeTracing()) {recordMethodEnd("MeanXProjection");}
        return result;
    }


    // net.haesleinhuepf.clij2.plugins.MeanYProjection
    //----------------------------------------------------
    /**
     * Determines the mean average intensity projection of an image along Y.
     */
    default boolean meanYProjection(ClearCLImageInterface source, ClearCLImageInterface destination) {
        if (doTimeTracing()) {recordMethodStart("MeanYProjection");}
        boolean result = MeanYProjection.meanYProjection(getCLIJ2(), source, destination);
        if (doTimeTracing()) {recordMethodEnd("MeanYProjection");}
        return result;
    }


    // net.haesleinhuepf.clij2.plugins.SquaredDifference
    //----------------------------------------------------
    /**
     * Determines the squared difference pixel by pixel between two images.
     */
    default boolean squaredDifference(ClearCLBuffer source1, ClearCLBuffer source2, ClearCLBuffer destination) {
        if (doTimeTracing()) {recordMethodStart("SquaredDifference");}
        boolean result = SquaredDifference.squaredDifference(getCLIJ2(), source1, source2, destination);
        if (doTimeTracing()) {recordMethodEnd("SquaredDifference");}
        return result;
    }


    // net.haesleinhuepf.clij2.plugins.AbsoluteDifference
    //----------------------------------------------------
    /**
     * Determines the absolute difference pixel by pixel between two images.
     * 
     * <pre>f(x, y) = |x - y| </pre>
     * 
     * Parameters
     * ----------
     * source1 : Image
     *     The input image to be subtracted from.
     * source2 : Image
     *     The input image which is subtracted.
     * destination : Image
     *     The output image  where results are written into.
     * 
     */
    default boolean absoluteDifference(ClearCLBuffer source1, ClearCLBuffer source2, ClearCLBuffer destination) {
        if (doTimeTracing()) {recordMethodStart("AbsoluteDifference");}
        boolean result = AbsoluteDifference.absoluteDifference(getCLIJ2(), source1, source2, destination);
        if (doTimeTracing()) {recordMethodEnd("AbsoluteDifference");}
        return result;
    }


    // net.haesleinhuepf.clij2.plugins.ReplacePixelsIfZero
    //----------------------------------------------------
    /**
     * Replaces pixel values x with y in case x is zero.
     * 
     * This functionality is comparable to ImageJs image calculator operator 'transparent zero'.
     */
    default boolean replacePixelsIfZero(ClearCLImageInterface input1, ClearCLImageInterface input2, ClearCLImageInterface destination) {
        if (doTimeTracing()) {recordMethodStart("ReplacePixelsIfZero");}
        boolean result = ReplacePixelsIfZero.replacePixelsIfZero(getCLIJ2(), input1, input2, destination);
        if (doTimeTracing()) {recordMethodEnd("ReplacePixelsIfZero");}
        return result;
    }


    // net.haesleinhuepf.clij2.plugins.VoronoiLabeling
    //----------------------------------------------------
    /**
     * Takes a binary image, labels connected components and dilates the regions using a octagon shape until they touch. 
     * 
     * The resulting label map is written to the output.
     */
    default boolean voronoiLabeling(ClearCLBuffer input, ClearCLImageInterface destination) {
        if (doTimeTracing()) {recordMethodStart("VoronoiLabeling");}
        boolean result = VoronoiLabeling.voronoiLabeling(getCLIJ2(), input, destination);
        if (doTimeTracing()) {recordMethodEnd("VoronoiLabeling");}
        return result;
    }


    // net.haesleinhuepf.clij2.plugins.ExtendLabelingViaVoronoi
    //----------------------------------------------------
    /**
     * Takes a label map image and dilates the regions using a octagon shape until they touch. 
     * 
     * The resulting label map is written to the output.
     */
    default boolean extendLabelingViaVoronoi(ClearCLBuffer input, ClearCLImageInterface destination) {
        if (doTimeTracing()) {recordMethodStart("ExtendLabelingViaVoronoi");}
        boolean result = ExtendLabelingViaVoronoi.extendLabelingViaVoronoi(getCLIJ2(), input, destination);
        if (doTimeTracing()) {recordMethodEnd("ExtendLabelingViaVoronoi");}
        return result;
    }


    // net.haesleinhuepf.clij2.plugins.CentroidsOfBackgroundAndLabels
    //----------------------------------------------------
    /**
     * Determines the centroids of the background and all labels in a label image or image stack. 
     * 
     * It writes the resulting  coordinates in a pointlist image. Depending on the dimensionality d of the labelmap and the number  of labels n, the pointlist image will have n*d pixels.
     */
    default boolean centroidsOfBackgroundAndLabels(ClearCLBuffer source, ClearCLBuffer pointlist_destination) {
        if (doTimeTracing()) {recordMethodStart("CentroidsOfBackgroundAndLabels");}
        boolean result = CentroidsOfBackgroundAndLabels.centroidsOfBackgroundAndLabels(getCLIJ2(), source, pointlist_destination);
        if (doTimeTracing()) {recordMethodEnd("CentroidsOfBackgroundAndLabels");}
        return result;
    }


    // net.haesleinhuepf.clij2.plugins.GetMeanOfMaskedPixels
    //----------------------------------------------------
    /**
     * Determines the mean of all pixels in a given image which have non-zero value in a corresponding mask image. 
     * 
     * It will be stored in the variable mean_of_masked_pixels.
     */
    default double getMeanOfMaskedPixels(ClearCLBuffer arg1, ClearCLBuffer arg2) {
        if (doTimeTracing()) {recordMethodStart("GetMeanOfMaskedPixels");}
        double result = GetMeanOfMaskedPixels.getMeanOfMaskedPixels(getCLIJ2(), arg1, arg2);
        if (doTimeTracing()) {recordMethodEnd("GetMeanOfMaskedPixels");}
        return result;
    }

}
// 423 methods generated.
