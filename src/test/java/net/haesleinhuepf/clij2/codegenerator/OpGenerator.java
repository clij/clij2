package net.haesleinhuepf.clij2.codegenerator;

import net.haesleinhuepf.clij.CLIJ;
import net.haesleinhuepf.clij.advancedfilters.*;
import net.haesleinhuepf.clij.advancedmath.*;
import net.haesleinhuepf.clij.io.PreloadFromDisc;
import net.haesleinhuepf.clij.io.ReadImageFromDisc;
import net.haesleinhuepf.clij.io.ReadRawImageFromDisc;
import net.haesleinhuepf.clij.kernels.Kernels;
import net.haesleinhuepf.clij.macro.CLIJMacroPlugin;
import net.haesleinhuepf.clij.macro.CLIJMacroPluginService;
import net.haesleinhuepf.clij.macro.documentation.OffersDocumentation;
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
import org.scijava.Context;

import java.io.*;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Parameter;

public class OpGenerator {
    public static void main(String ... args) throws IOException {

        Class[] classes = {
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
                ReplaceIntensity.class
        };


        CLIJMacroPluginService service = new Context(CLIJMacroPluginService.class).getService(CLIJMacroPluginService.class);

        StringBuilder builder = new StringBuilder();
        builder.append("package net.haesleinhuepf.clij2.utilities;\n");
        builder.append("import net.haesleinhuepf.clij.CLIJ;\n");
        builder.append("import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;\n");
        builder.append("import net.haesleinhuepf.clij.clearcl.ClearCLImage;\n");
        builder.append("import net.imglib2.realtransform.AffineTransform2D;\n");
        builder.append("import net.imglib2.realtransform.AffineTransform3D;\n");
        //builder.append("import ij.measure.ResultsTable;\n");

        for (Class klass : classes) {
            builder.append("import " + klass.getName() + ";\n");
        }

        builder.append("// this is generated code. See src/test/java/net/haesleinhuepf/clij2/codegenerator for details\n");
        builder.append("public class CLIJ2Ops {\n");
        builder.append("   private CLIJ clij;\n");
        builder.append("   public CLIJ2Ops(CLIJ clij) {\n");
        builder.append("       this.clij = clij;\n");
        builder.append("   }\n");

        for (Class klass : classes) {
            builder.append("\n    // " + klass.getName() + "\n");
            builder.append("    //----------------------------------------------------\n");
            for (Method method : klass.getMethods()) {
                if (Modifier.isStatic(method.getModifiers()) &&
                        Modifier.isPublic(method.getModifiers()) &&
                        method.getParameterCount() > 0 &&
                        method.getParameters()[0].getType() == CLIJ.class) {

                    String methodName = method.getName();
                    String returnType = typeToString(method.getReturnType());
                    String parametersHeader = "";
                    String parametersCall = "";
                    for (Parameter parameter : method.getParameters()) {
                        if (parametersCall.length() == 0) { // first parameter
                            parametersCall = "clij";
                            continue;
                        }

                        if (parametersHeader.length() > 0) {
                            parametersHeader = parametersHeader + ", ";
                        }
                        parametersHeader = parametersHeader + parameter.getType().getSimpleName() + " " + parameter.getName();
                        parametersCall = parametersCall + ", " + parameter.getName();
                    }

                    String documentation = findDocumentation(service, methodName);
                    //System.out.println(documentation);

                    builder.append("    /**\n");
                    builder.append("     * " + documentation.replace("\n", "\n     * ") + "\n");
                    builder.append("     */\n");

                    builder.append("    public " + returnType + " " + methodName + "(");
                    builder.append(parametersHeader);
                    builder.append(") {\n");
                    builder.append("        return " + klass.getSimpleName() + "." + methodName + "(" + parametersCall + ");\n");
                    builder.append("    }\n\n");
                }
            }
        }
        builder.append("}\n");

        File outputTarget = new File("src/main/java/net/haesleinhuepf/clij2/utilities/CLIJ2Ops.java");

        FileWriter writer = new FileWriter(outputTarget);
        writer.write(builder.toString());
        writer.close();

    }

    private static String typeToString(Class klass) {
        String result = "" + klass.getSimpleName();
        if (result.compareTo("[F") == 0) {
            return "float[]";
        }
        return result;
    }

    protected static String findDocumentation(CLIJMacroPluginService service, String methodName) {
        if (methodName.endsWith("IJ")) {
            return "This method is deprecated. Consider using " + methodName.replace("IJ", "Box") + " or " + methodName.replace("IJ", "Sphere") + " instead.";
        }

        String[] potentialMethodNames = {
                "CLIJ_" + methodName,
                "CLIJ_" + methodName + "2D",
                "CLIJ_" + methodName + "3D",
                "CLIJ_" + methodName + "Images",
                "CLIJ_" + methodName.replace( "Sphere", "2DBox"),
                "CLIJ_" + methodName.replace( "Sphere", "3DBox"),
                "CLIJ_" + methodName.replace( "Box", "2DBox"),
                "CLIJ_" + methodName.replace( "Box", "3DBox"),
                "CLIJ_" + methodName.replace( "Pixels", "OfAllPixels"),
                "CLIJ_" + methodName.replace( "SliceBySlice", "3DSliceBySlice")
        };

        for (String name : potentialMethodNames) {
            name = findName(service, name);
            CLIJMacroPlugin plugin = service.getCLIJMacroPlugin(name);
            if (plugin != null) {
                if (plugin instanceof OffersDocumentation) {
                    return ((OffersDocumentation) plugin).getDescription();
                } else {
                    return plugin.getParameterHelpText();
                }
            }
        }

        System.out.println("No documentation found for " + methodName);
        return "";
    }

    protected static String findName(CLIJMacroPluginService service, String name) {
        for (String potentialName : service.getCLIJMethodNames()) {
            if (potentialName.toLowerCase().compareTo(name.toLowerCase()) == 0) {
                return potentialName;
            }
        }
        return name;
    }

}
