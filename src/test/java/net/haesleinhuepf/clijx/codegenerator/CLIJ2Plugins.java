package net.haesleinhuepf.clijx.codegenerator;

import net.haesleinhuepf.clijx.advancedfilters.*;
import net.haesleinhuepf.clijx.advancedmath.*;
import net.haesleinhuepf.clijx.gui.OrganiseWindows;
import net.haesleinhuepf.clijx.io.PreloadFromDisc;
import net.haesleinhuepf.clijx.io.ReadImageFromDisc;
import net.haesleinhuepf.clijx.io.ReadRawImageFromDisc;
import net.haesleinhuepf.clij.kernels.Kernels;
import net.haesleinhuepf.clijx.matrix.*;
import net.haesleinhuepf.clijx.painting.DrawBox;
import net.haesleinhuepf.clijx.painting.DrawLine;
import net.haesleinhuepf.clijx.painting.DrawSphere;
import net.haesleinhuepf.clijx.painting.DrawTwoValueLine;
import net.haesleinhuepf.clijx.piv.FastParticleImageVelocimetry;
import net.haesleinhuepf.clijx.piv.ParticleImageVelocimetry;
import net.haesleinhuepf.clijx.piv.ParticleImageVelocimetryTimelapse;
import net.haesleinhuepf.clijx.registration.DeformableRegistration2D;
import net.haesleinhuepf.clijx.registration.TranslationRegistration;
import net.haesleinhuepf.clijx.registration.TranslationTimelapseRegistration;
import net.haesleinhuepf.clijx.utilities.CLIJxAPIConsistencyWorkaround;
import net.haesleinhuepf.clijx.weka.ApplyWekaModel;
import net.haesleinhuepf.clijx.weka.TrainWekaModel;
import net.imagej.ops.Ops;

public interface CLIJ2Plugins {
    public Class[] classes = {
            Kernels.class,
            CLIJxAPIConsistencyWorkaround.class,
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
            SorensenDiceCoefficent.class,
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
            LaplaceSphere.class,
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
            ExcludeLabelsOnEdges.class,
            BinarySubtract.class,
            BinaryEdgeDetection.class,
            DistanceMap.class,
            PullAsROI.class,
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
            ProjectMaximumZBounded.class,
            ProjectMinimumZBounded.class,
            ProjectMeanZBounded.class,
            NonzeroMaximumBox.class,
            NonzeroMinimumBox.class,
            ProjectMinimumThresholdedZBounded.class,
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
            ApplyWekaModel.class,
            TrainWekaModel.class,
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
            DilateBoxSliceBySlice.class
    };

    public String blockList = ";" +
            "BinaryIntersection.binaryAnd;" +
            "BinaryUnion.binaryOr;" +
            "Kernels.absolute;" +
            "Kernels.addImagesWeighted;" +
            "Kernels.addImages;" +
            "Kernels.subtractImages;" +
            "Kernels.subtract;" +
            "Kernels.affineTransform;" +
            "Kernels.affineTransform2D;" +
            "Kernels.affineTransform3D;" +
            "Kernels.threshold;" +
            "Kernels.applyVectorfield;" +
            "Kernels.argMaximumZProjection;" +
            "Kernels.fillHistogram;" +
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
            "Kernels.dilateSphereSliceBySlice;";
}
