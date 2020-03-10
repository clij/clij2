package net.haesleinhuepf.clijx.codegenerator;

import net.haesleinhuepf.clij2.plugins.AffineTransform;
import net.haesleinhuepf.clij2.plugins.Scale;
import net.haesleinhuepf.clij2.plugins.*;
import net.haesleinhuepf.clij2.plugins.Equal;
import net.haesleinhuepf.clij2.plugins.EqualConstant;
import net.haesleinhuepf.clij2.plugins.NotEqual;
import net.haesleinhuepf.clij2.plugins.NotEqualConstant;
import net.haesleinhuepf.clij2.plugins.Copy;
import net.haesleinhuepf.clij2.plugins.CopySlice;
import net.haesleinhuepf.clij2.plugins.AutomaticThreshold;
import net.haesleinhuepf.clij2.plugins.Histogram;
import net.haesleinhuepf.clij2.plugins.Threshold;
import net.haesleinhuepf.clij2.plugins.Set;
import net.haesleinhuepf.clij2.plugins.SetColumn;
import net.haesleinhuepf.clij2.plugins.SetRow;
import net.haesleinhuepf.clij2.plugins.AffineTransform2D;
import net.haesleinhuepf.clij2.plugins.AffineTransform3D;
import net.haesleinhuepf.clij2.plugins.LabelToMask;
import net.haesleinhuepf.clij2.plugins.MinimumDistanceOfTouchingNeighbors;
import net.haesleinhuepf.clij2.plugins.SpotsToPointList;
import net.haesleinhuepf.clijx.plugins.*;
import net.haesleinhuepf.clijx.plugins.splitstack.AbstractSplitStack;
import net.haesleinhuepf.clijx.gui.OrganiseWindows;
import net.haesleinhuepf.clijx.io.*;
import net.haesleinhuepf.clij.kernels.Kernels;
import net.haesleinhuepf.clij2.plugins.DrawBox;
import net.haesleinhuepf.clij2.plugins.DrawLine;
import net.haesleinhuepf.clij2.plugins.DrawSphere;
import net.haesleinhuepf.clijx.plugins.DrawTwoValueLine;
import net.haesleinhuepf.clijx.piv.FastParticleImageVelocimetry;
import net.haesleinhuepf.clijx.piv.ParticleImageVelocimetry;
import net.haesleinhuepf.clijx.piv.ParticleImageVelocimetryTimelapse;
import net.haesleinhuepf.clijx.plugins.tenengradfusion.TenengradFusion;
import net.haesleinhuepf.clijx.registration.DeformableRegistration2D;
import net.haesleinhuepf.clijx.registration.TranslationRegistration;
import net.haesleinhuepf.clijx.registration.TranslationTimelapseRegistration;
import net.haesleinhuepf.clij2.plugins.Downsample2D;
import net.haesleinhuepf.clij2.plugins.Downsample3D;
import net.haesleinhuepf.clij2.plugins.DownsampleSliceBySliceHalfMedian;
import net.haesleinhuepf.clij2.plugins.GradientX;
import net.haesleinhuepf.clij2.plugins.GradientY;
import net.haesleinhuepf.clij2.plugins.GradientZ;
import net.haesleinhuepf.clij2.plugins.LocalThreshold;
import net.haesleinhuepf.clij2.plugins.GaussianBlur2D;
import net.haesleinhuepf.clij2.plugins.GaussianBlur3D;
import net.haesleinhuepf.clijx.plugins.BlurSliceBySlice;

public interface CLIJxPlugins {
    public Class[] classes = {
            Kernels.class,
            BinaryUnion.class,
            BinaryIntersection.class,
            ConnectedComponentsLabeling.class,
            CountNonZeroPixels.class,
            CrossCorrelation.class,
            DifferenceOfGaussian2D.class,
            DifferenceOfGaussian3D.class,
            Extrema.class,
            LocalExtremaBox.class,
            LocalID.class,
            MaskLabel.class,
            MeanClosestSpotDistance.class,
            MeanSquaredError.class,
            MedianZProjection.class,
            NonzeroMinimumDiamond.class,
            Paste2D.class,
            Paste3D.class,
            Presign.class,
            JaccardIndex.class,
            SorensenDiceCoefficient.class,
            StandardDeviationZProjection.class,
            StackToTiles.class,
            SubtractBackground2D.class,
            SubtractBackground3D.class,
            TopHatBox.class,
            TopHatSphere.class,
            Exponential.class,
            Logarithm.class,
            GenerateDistanceMatrix.class,
            ShortestDistances.class,
            SpotsToPointList.class,
            TransposeXY.class,
            TransposeXZ.class,
            TransposeYZ.class,
            FastParticleImageVelocimetry.class,
            ParticleImageVelocimetry.class,
            ParticleImageVelocimetryTimelapse.class,
            DeformableRegistration2D.class,
            TranslationRegistration.class,
            TranslationTimelapseRegistration.class,
            SetWhereXequalsY.class,
            LaplaceDiamond.class,
            Image2DToResultsTable.class,
            WriteValuesToPositions.class,
            GetSize.class,
            MultiplyMatrix.class,
            MatrixEqual.class,
            PowerImages.class,
            Equal.class,
            GreaterOrEqual.class,
            Greater.class,
            Smaller.class,
            SmallerOrEqual.class,
            NotEqual.class,
            ReadImageFromDisc.class,
            ReadRawImageFromDisc.class,
            PreloadFromDisc.class,
            EqualConstant.class,
            GreaterOrEqualConstant.class,
            GreaterConstant.class,
            SmallerConstant.class,
            SmallerOrEqualConstant.class,
            NotEqualConstant.class,
            DrawBox.class,
            DrawLine.class,
            DrawSphere.class,
            ReplaceIntensity.class,
            BoundingBox.class,
            MinimumOfMaskedPixels.class,
            MaximumOfMaskedPixels.class,
            MeanOfMaskedPixels.class,
            LabelToMask.class,
            NClosestPoints.class,
            GaussJordan.class,
            StatisticsOfLabelledPixels.class,
            VarianceOfAllPixels.class,
            StandardDeviationOfAllPixels.class,
            VarianceOfMaskedPixels.class,
            StandardDeviationOfMaskedPixels.class,
            ExcludeLabelsOnEdges.class,
            BinarySubtract.class,
            BinaryEdgeDetection.class,
            DistanceMap.class,
            PullAsROI.class,
            PullLabelsToROIManager.class,
            NonzeroMaximumDiamond.class,
            OnlyzeroOverwriteMaximumDiamond.class,
            OnlyzeroOverwriteMaximumBox.class,
            GenerateTouchMatrix.class,
            DetectLabelEdges.class,
            StopWatch.class,
            CountTouchingNeighbors.class,
            ReplaceIntensities.class,
            DrawTwoValueLine.class,
            AverageDistanceOfNClosestPoints.class,
            SaveAsTIF.class,
            ConnectedComponentsLabelingInplace.class,
            TouchMatrixToMesh.class,
            AutomaticThresholdInplace.class,
            DifferenceOfGaussianInplace3D.class,
            AbsoluteInplace.class,
            Resample.class,
            EqualizeMeanIntensitiesOfSlices.class,
            Watershed.class,
            ResliceRadial.class,
            ShowRGB.class,
            ShowGrey.class,
            Sobel.class,
            Absolute.class,
            LaplaceBox.class,
            BottomHatBox.class,
            BottomHatSphere.class,
            ClosingBox.class,
            ClosingDiamond.class,
            OpeningBox.class,
            OpeningDiamond.class,
            MaximumXProjection.class,
            MaximumYProjection.class,
            MaximumZProjectionBounded.class,
            MinimumZProjectionBounded.class,
            MeanZProjectionBounded.class,
            NonzeroMaximumBox.class,
            NonzeroMinimumBox.class,
            MinimumZProjectionThresholdedBounded.class,
            MeanOfPixelsAboveThreshold.class,
            OrganiseWindows.class,
            DistanceMatrixToMesh.class,
            PointIndexListToMesh.class,
            MinimumOctagon.class,
            MaximumOctagon.class,
            TopHatOctagon.class,
            AddImages.class,
            AddImagesWeighted.class,
            SubtractImages.class,
            ShowGlasbeyOnGrey.class,
            AffineTransform2D.class,
            AffineTransform3D.class,
            ApplyVectorField2D.class,
            ApplyVectorField3D.class,
            ArgMaximumZProjection.class,
            Histogram.class,
            AutomaticThreshold.class,
            Threshold.class,
            BinaryOr.class,
            BinaryAnd.class,
            BinaryXOr.class,
            BinaryNot.class,
            ErodeSphere.class,
            ErodeBox.class,
            ErodeSphereSliceBySlice.class,
            ErodeBoxSliceBySlice.class,
            DilateSphere.class,
            DilateBox.class,
            DilateSphereSliceBySlice.class,
            DilateBoxSliceBySlice.class,
            Copy.class,
            CopySlice.class,
            Crop2D.class,
            Crop3D.class,
            Set.class,
            Flip2D.class,
            Flip3D.class,
            RotateCounterClockwise.class,
            RotateClockwise.class,
            Mask.class,
            MaskStackWithPlane.class,
            MaximumZProjection.class,
            MeanZProjection.class,
            MinimumZProjection.class,
            Power.class,
            DivideImages.class,
            MaximumImages.class,
            MaximumImageAndScalar.class,
            MinimumImages.class,
            MinimumImageAndScalar.class,
            MultiplyImageAndScalar.class,
            MultiplyStackWithPlane.class,
            CountNonZeroPixels2DSphere.class,
            CountNonZeroPixelsSliceBySliceSphere.class,
            CountNonZeroVoxels3DSphere.class,
            SumZProjection.class,
            SumOfAllPixels.class,
            CenterOfMass.class,
            Invert.class,
            Downsample2D.class,
            Downsample3D.class,
            DownsampleSliceBySliceHalfMedian.class,
            LocalThreshold.class,
            GradientX.class,
            GradientY.class,
            GradientZ.class,
            MultiplyImageAndCoordinate.class,
            Mean2DBox.class,
            Mean2DSphere.class,
            Mean3DBox.class,
            Mean3DSphere.class,
            MeanSliceBySliceSphere.class,
            MeanOfAllPixels.class,
            Median2DBox.class,
            Median2DSphere.class,
            Median3DBox.class,
            Median3DSphere.class,
            MedianSliceBySliceBox.class,
            MedianSliceBySliceSphere.class,
            Maximum2DSphere.class,
            Maximum3DSphere.class,
            Maximum2DBox.class,
            Maximum3DBox.class,
            MaximumSliceBySliceSphere.class,
            Minimum2DSphere.class,
            Minimum3DSphere.class,
            Minimum2DBox.class,
            Minimum3DBox.class,
            MinimumSliceBySliceSphere.class,
            MultiplyImages.class,
            GaussianBlur2D.class,
            GaussianBlur3D.class,
            BlurSliceBySlice.class,
            ResliceBottom.class,
            ResliceTop.class,
            ResliceLeft.class,
            ResliceRight.class,
            Rotate2D.class,
            Rotate3D.class,
            Scale2D.class,
            Scale3D.class,
            Translate2D.class,
            Translate3D.class,
            Clear.class,
            ClInfo.class,
            ConvertFloat.class,
            ConvertUInt8.class,
            ConvertUInt16.class,
            Create2D.class,
            Create3D.class,
            Pull.class,
            PullBinary.class,
            Push.class,
            PushCurrentSlice.class,
            PushCurrentZStack.class,
            Release.class,
            AddImageAndScalar.class,
            DetectMinimaBox.class,
            DetectMaximaBox.class,
            DetectMaximaSliceBySliceBox.class,
            DetectMinimaSliceBySliceBox.class,
            MaximumOfAllPixels.class,
            MinimumOfAllPixels.class,
            ReportMemory.class,
            AbstractSplitStack.class,
            TopHatOctagonSliceBySlice.class,
            SetColumn.class,
            SetRow.class,
            SumYProjection.class,
            AverageDistanceOfTouchingNeighbors.class,
            LabelledSpotsToPointList.class,
            LabelSpots.class,
            MinimumDistanceOfTouchingNeighbors.class,
            WriteVTKLineListToDisc.class,
            WriteXYZPointListToDisc.class,
            SetWhereXgreaterThanY.class,
            SetWhereXsmallerThanY.class,
            SetNonZeroPixelsToPixelIndex.class,
            CloseIndexGapsInLabelMap.class,
            AffineTransform.class,
            Scale.class,
            AverageAngleBetweenAdjacentTriangles.class,
            CentroidsOfLabels.class,
            SetRampX.class,
            SetRampY.class,
            SetRampZ.class,
            SubtractImageFromScalar.class,
            ThresholdDefault.class,
            ThresholdOtsu.class,
            ThresholdHuang.class,
            ThresholdIntermodes.class,
            ThresholdIsoData.class,
            ThresholdIJ_IsoData.class,
            ThresholdLi.class,
            ThresholdMaxEntropy.class,
            ThresholdMean.class,
            ThresholdMinError.class,
            ThresholdMinimum.class,
            ThresholdMoments.class,
            ThresholdPercentile.class,
            ThresholdRenyiEntropy.class,
            ThresholdShanbhag.class,
            ThresholdTriangle.class,
            ThresholdYen.class,
            ExcludeLabelsSubSurface.class,
            ExcludeLabelsOnSurface.class,
            SetPlane.class,
            TenengradFusion.class,
            ImageToStack.class,
            SumXProjection.class,
            SumImageSliceBySlice.class,
            MultiplyImageStackWithScalars.class,
            Print.class,
            VoronoiOctagon.class,
            SetImageBorders.class,
            Skeletonize.class,
            FloodFillDiamond.class,
            BinaryFillHoles.class,
            ConnectedComponentsLabelingDiamond.class,
            ConnectedComponentsLabelingBox.class
    };

    public String blockList = ";" +
            "BinaryIntersection.binaryAnd;" +
            "BinaryUnion.binaryOr;" +
            "AffineTransform.affineTransform3D;"+
            "Scale.scale3D;"+
            "ConnectedComponentsLabelingBox.connectedComponentsLabeling;" +
            "ConnectedComponentsLabelingDiamond.connectedComponentsLabeling;" +
            "Kernels.absolute;" +
            "Kernels.addImagesWeighted;" +
            "Kernels.addImages;" +
            "Kernels.subtractImages;" +
            "Kernels.subtract;" +
            "Kernels.affineTransform;" +
            "Kernels.affineTransform2D;" +
            "Kernels.affineTransform3D;" +
            "Kernels.threshold;" +
            "Kernels.applyVectorField;" +
            "Kernels.argMaximumZProjection;" +
            "Kernels.fillHistogram;" +
            "Kernels.histogram;" +
            "Kernels.automaticThreshold;" +
            "Kernels.binaryAnd;" +
            "Kernels.binaryOr;" +
            "Kernels.binaryXOr;" +
            "Kernels.binaryNot;" +
            "Kernels.erodeBox;" +
            "Kernels.erodeSphere;" +
            "Kernels.erodeBoxSliceBySlice;" +
            "Kernels.erodeSphereSliceBySlice;" +
            "Kernels.dilateBox;" +
            "Kernels.dilateSphere;" +
            "Kernels.dilateBoxSliceBySlice;" +
            "Kernels.dilateSphereSliceBySlice;" +
            "Kernels.copy;" +
            "Kernels.crop;" +
            "Kernels.copySlice;" +
            "Kernels.set;" +
            "Kernels.flip;" +
            "Kernels.rotateLeft;" +
            "Kernels.rotateRight;" +
            "Kernels.mask;" +
            "Kernels.maskStackWithPlane;" +
            "Kernels.maximumZProjection;" +
            "Kernels.meanZProjection;" +
            "Kernels.minimumZProjection;" +
            "Kernels.power;" +
            "Kernels.tenengradWeightsSliceBySlice;" +
            "Kernels.tenengradFusion;" +
            "Kernels.divideImages;" +
            "Kernels.maximumImages;" +
            "Kernels.maximumImageAndScalar;" +
            "Kernels.minimumImages;" +
            "Kernels.minimumImageAndScalar;" +
            "Kernels.multiplyImageAndScalar;" +
            "Kernels.multiplyStackWithPlane;" +
            "Kernels.countNonZeroPixelsLocallySliceBySlice;" +
            "Kernels.countNonZeroVoxelsLocally;" +
            "Kernels.countNonZeroPixelsLocally;" +
            "Kernels.sumPixels;" +
            "Kernels.sumZProjection;" +
            "Kernels.centerOfMass;" +
            "Kernels.invert;" +
            "Kernels.downsample;" +
            "Kernels.downsampleSliceBySliceHalfMedian;" +
            "Kernels.localThreshold;" +
            "Kernels.gradientX;" +
            "Kernels.gradientY;" +
            "Kernels.gradientZ;" +
            "Kernels.multiplyImageAndCoordinate;" +
            "Kernels.meanSliceBySliceSphere;" +
            "Kernels.meanBox;" +
            "Kernels.meanSphere;" +
            "Kernels.meanIJ;" +
            "Kernels.medianBox;" +
            "Kernels.medianSphere;" +
            "Kernels.medianSliceBySliceSphere;" +
            "Kernels.medianSliceBySliceBox;" +
            "Kernels.minimumSliceBySliceSphere;" +
            "Kernels.minimumSphere;" +
            "Kernels.minimumBox;" +
            "Kernels.maximumSliceBySliceSphere;" +
            "Kernels.maximumSphere;" +
            "Kernels.maximumBox;" +
            "Kernels.minimumIJ;" +
            "Kernels.maximumIJ;" +
            "Kernels.multiplyImages;" +
            "Kernels.blur;" +
            "Kernels.blurSliceBySlice;" +
            "Kernels.resliceTop;" +
            "Kernels.resliceBottom;" +
            "Kernels.resliceLeft;" +
            "Kernels.resliceRight;" +
            "Kernels.translate2D;" +
            "Kernels.translate3D;" +
            "Kernels.translate;" +
            "Kernels.scale2D;" +
            "Kernels.scale3D;" +
            "Kernels.scale;" +
            "Kernels.rotate2D;" +
            "Kernels.rotate3D;" +
            "Kernels.radialProjection;" +
            "Kernels.maximumOfAllPixels;" +
            "Kernels.minimumOfAllPixels;" +
            "Kernels.detectMinimaBox;" +
            "Kernels.detectMinimaSliceBySliceBox;" +
            "Kernels.detectMaximaBox;" +
            "Kernels.detectMaximaSliceBySliceBox;" +
            "Kernels.addImageAndScalar;" +
            "Kernels.splitStack;";

}
