package net.haesleinhuepf.clij2.codegenerator;

import net.haesleinhuepf.clij.advancedfilters.*;
import net.haesleinhuepf.clij.advancedmath.*;
import net.haesleinhuepf.clij.io.PreloadFromDisc;
import net.haesleinhuepf.clij.io.ReadImageFromDisc;
import net.haesleinhuepf.clij.io.ReadRawImageFromDisc;
import net.haesleinhuepf.clij.kernels.Kernels;
import net.haesleinhuepf.clij.matrix.*;
import net.haesleinhuepf.clij.painting.DrawBox;
import net.haesleinhuepf.clij.painting.DrawLine;
import net.haesleinhuepf.clij.painting.DrawSphere;
import net.haesleinhuepf.clij.piv.FastParticleImageVelocimetry;
import net.haesleinhuepf.clij.piv.ParticleImageVelocimetry;
import net.haesleinhuepf.clij.piv.ParticleImageVelocimetryTimelapse;
import net.haesleinhuepf.clij.registration.DeformableRegistration2D;
import net.haesleinhuepf.clij.registration.TranslationRegistration;
import net.haesleinhuepf.clij.registration.TranslationTimelapseRegistration;

public interface CLIJ2Plugins {
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
            NonzeroMinimum3DDiamond.class,
            Paste2D.class,
            Paste3D.class,
            Presign.class,
            SorensenDiceJaccardIndex.class,
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
            Laplace.class,
            //Image2DToResultsTable.class,
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
            NClosestPoints.class
    };

}
