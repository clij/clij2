package net.haesleinhuepf.clijx.temp;

import ij.ImagePlus;
import ij.gui.NewImage;
import ij.measure.ResultsTable;
import net.haesleinhuepf.clij.CLIJ;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.clearcl.exceptions.OpenCLException;
import net.haesleinhuepf.clij.clearcl.interfaces.ClearCLImageInterface;
import net.haesleinhuepf.clij.coremem.enums.NativeTypeEnum;
import net.haesleinhuepf.clij.macro.AbstractCLIJPlugin;
import net.haesleinhuepf.clij.macro.CLIJMacroPlugin;
import net.haesleinhuepf.clij.macro.CLIJMacroPluginService;
import net.haesleinhuepf.clij.macro.CLIJOpenCLProcessor;
import net.haesleinhuepf.clij.macro.documentation.OffersDocumentation;
import net.haesleinhuepf.clij.macro.modules.Mean3DBox;
import net.haesleinhuepf.clij.test.TestUtilities;
import net.haesleinhuepf.clijx.CLIJx;
import net.haesleinhuepf.clijx.utilities.AbstractCLIJxPlugin;
import org.scijava.Context;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;

public class CLIJ1CLIJ2Benchmarking {

    static ResultsTable table;

    public static void main(String ... arg) throws IOException {
        CLIJ clij = CLIJ.getInstance();

        CLIJx clijx = CLIJx.getInstance();

        table = new ResultsTable();

        System.out.println("CLIJ: " + clij.getGPUName() + " " + clij.getClearCLContext().toString());
        System.out.println("CLIJx: " + clijx.getGPUName() + " " + clijx.getClij().getClearCLContext().toString());

        ImagePlus random = NewImage.createFloatImage("rand", 1024, 1024, 50, NewImage.FILL_RANDOM);
        ClearCLBuffer input = clij.push(random);

        ImagePlus random2d = NewImage.createFloatImage("rand", 1024, 1024, 1, NewImage.FILL_RANDOM);
        ClearCLBuffer input2d = clij.push(random2d);

        String foundParameterHashes = ";";

        // these operations must be tested individually
        String blackList = ";CLIJ_automaticThreshold;CLIJ_maskStackWithPlane;CLIJ_multiplyStackWithPlane;CLIJ_histogram;" +
                "CLIJ_clear;CLIJ_clInfo;CLIJ_convertFloat;CLIJ_convertUInt8;CLIJ_convertUInt16;CLIJ_create2D;CLIJ_create3D;" +
                "CLIJ_pull;CLIJ_pullBinary;" +
                "CLIJ_push;CLIJ_pushCurrentSlice;CLIJ_pushCurrentZStack;CLIJ_release;CLIJ_reportMemory;";



        CLIJMacroPluginService service = new Context(CLIJMacroPluginService.class).getService(CLIJMacroPluginService.class);
        for (String pluginName : service.getCLIJMethodNames()) {
            if (!new File(pluginName + ".txt").exists()) {
                System.out.println(pluginName);
                if (pluginName.startsWith("CLIJ") && !blackList.contains(";" + pluginName + ";")) {
                    CLIJMacroPlugin clijPlugin = service.getCLIJMacroPlugin(pluginName);
                    CLIJMacroPlugin clijxPlugin = service.getCLIJMacroPlugin(pluginName.replace("CLIJ_", "CLIJx_"));

                    System.out.println("----");
                    if (pluginName.startsWith("CLIJ_") && clijPlugin != null && clijxPlugin != null) {

                        System.out.println(clijPlugin + " <=> " + clijxPlugin);
                        String clijParameterTypeHash = getParameterTypeHash(clijPlugin);
                        String clijxParameterTypeHash = getParameterTypeHash(clijxPlugin);

                        if (clijParameterTypeHash.compareTo(clijxParameterTypeHash) == 0) {// the y take the same parameters
                            if (!foundParameterHashes.contains(";" + clijParameterTypeHash + ";")) {
                                foundParameterHashes = foundParameterHashes + clijParameterTypeHash + ";";
                            }
                        } else {
                            System.out.println("Error: Parameter hash differs for " + pluginName);
                            continue;
                        }


                        if (!clijPlugin.getName().contains("2D")) { // test 3D
                            if (clijxPlugin instanceof OffersDocumentation && ((OffersDocumentation) clijxPlugin).getAvailableForDimensions().contains("3D")) {
                                addTableLine(clijPlugin.getClass().getName(), clijxPlugin.getClass().getName(), "3D", 0 );
                                Object[] argsCLIJ = buildArgs(clijx, clijPlugin, clijParameterTypeHash, input);
                                Object[] argsCLIJx = buildArgs(clijx, clijxPlugin, clijxParameterTypeHash, input);

                                clijPlugin.setClij(clij);
                                clijxPlugin.setClij(clij);

                                clijPlugin.setArgs(argsCLIJ);
                                clijxPlugin.setArgs(argsCLIJx);

                                if (clijPlugin instanceof CLIJOpenCLProcessor) {
                                    System.out.print("executing clij...");
                                    long time = System.currentTimeMillis();
                                    ((CLIJOpenCLProcessor) clijPlugin).executeCL();
                                    System.out.println(" took " + (System.currentTimeMillis() - time) + " ms");
                                }
                                if (clijxPlugin instanceof CLIJOpenCLProcessor) {
                                    System.out.print("executing clijx...");
                                    long time = System.currentTimeMillis();
                                    ((CLIJOpenCLProcessor) clijxPlugin).executeCL();
                                    System.out.println(" took " + (System.currentTimeMillis() - time) + " ms");
                                }

                                System.out.println("comparing 3D ...");
                                compareResults(clijx, argsCLIJ, argsCLIJx, clijParameterTypeHash);

                                cleanUpArgs(clijx, argsCLIJ, input);
                                cleanUpArgs(clijx, argsCLIJx, input);
                            }
                        }

                        if ((!clijPlugin.getName().contains("3D")) && (!clijPlugin.getName().toLowerCase().contains("project"))) { // test 2D
                            if (clijxPlugin instanceof OffersDocumentation && ((OffersDocumentation) clijxPlugin).getAvailableForDimensions().contains("2D")) {
                                addTableLine(clijPlugin.getClass().getName(), clijxPlugin.getClass().getName(), "2D", 0 );


                                Object[] argsCLIJ = buildArgs(clijx, clijPlugin, clijParameterTypeHash, input2d);
                                Object[] argsCLIJx = buildArgs(clijx, clijxPlugin, clijxParameterTypeHash, input2d);

                                clijPlugin.setClij(clij);
                                clijxPlugin.setClij(clij);

                                clijPlugin.setArgs(argsCLIJ);
                                clijxPlugin.setArgs(argsCLIJx);

                                if (clijPlugin instanceof CLIJOpenCLProcessor) {
                                    System.out.print("executing clij...");
                                    long time = System.currentTimeMillis();
                                    ((CLIJOpenCLProcessor) clijPlugin).executeCL();
                                    long duration = (System.currentTimeMillis() - time);
                                    System.out.println(" took " + (duration) + " ms");
                                    table.addValue("CLIJ duration", (duration));
                                }
                                if (clijxPlugin instanceof CLIJOpenCLProcessor) {
                                    System.out.print("executing clijx...");
                                    long time = System.currentTimeMillis();
                                    ((CLIJOpenCLProcessor) clijxPlugin).executeCL();
                                    long duration = (System.currentTimeMillis() - time);
                                    System.out.println(" took " + (duration) + " ms");
                                    table.addValue("CLIJx duration", (duration));
                                }

                                System.out.println("comparing 2D...");
                                compareResults(clijx, argsCLIJ, argsCLIJx, clijParameterTypeHash);

                                cleanUpArgs(clijx, argsCLIJ, input2d);
                                cleanUpArgs(clijx, argsCLIJx, input2d);
                            }
                        }

                    } else if (clijxPlugin == null) {
                        System.out.println("Error: No successor found for " + pluginName);
                    } else if (clijPlugin == null) {
                        System.out.println("Error: No clij1 found for " + pluginName);

                        System.out.println(clijPlugin + " <=> " + clijxPlugin);
                        String clijxParameterTypeHash = getParameterTypeHash(clijxPlugin);

                        if (!foundParameterHashes.contains(";" + clijxParameterTypeHash + ";")) {
                            foundParameterHashes = foundParameterHashes + clijxParameterTypeHash + ";";
                        }

                        if (!clijxPlugin.getName().contains("2D")) { // test 3D
                            if (clijxPlugin instanceof OffersDocumentation && ((OffersDocumentation) clijxPlugin).getAvailableForDimensions().contains("3D")) {
                                addTableLine("", clijxPlugin.getClass().getName(), "3D", 0 );
                                Object[] argsCLIJx = buildArgs(clijx, clijxPlugin, clijxParameterTypeHash, input);

                                clijxPlugin.setClij(clij);

                                clijxPlugin.setArgs(argsCLIJx);

                                if (clijxPlugin instanceof CLIJOpenCLProcessor) {
                                    System.out.print("executing clijx...");
                                    long time = System.currentTimeMillis();
                                    ((CLIJOpenCLProcessor) clijxPlugin).executeCL();
                                    System.out.println(" took " + (System.currentTimeMillis() - time) + " ms");
                                }

                                cleanUpArgs(clijx, argsCLIJx, input);
                            }
                        }

                        if ((!clijxPlugin.getName().contains("3D")) && (!clijxPlugin.getName().toLowerCase().contains("project"))) { // test 2D
                            if (clijxPlugin instanceof OffersDocumentation && ((OffersDocumentation) clijxPlugin).getAvailableForDimensions().contains("2D")) {
                                addTableLine("", clijxPlugin.getClass().getName(), "2D", 0 );

                                Object[] argsCLIJx = buildArgs(clijx, clijxPlugin, clijxParameterTypeHash, input2d);

                                clijxPlugin.setClij(clij);

                                clijxPlugin.setArgs(argsCLIJx);

                                if (clijxPlugin instanceof CLIJOpenCLProcessor) {
                                    System.out.print("executing clijx...");
                                    long time = System.currentTimeMillis();
                                    ((CLIJOpenCLProcessor) clijxPlugin).executeCL();
                                    long duration = (System.currentTimeMillis() - time);
                                    System.out.println(" took " + (duration) + " ms");
                                    table.addValue("CLIJx duration", (duration));
                                }

                                cleanUpArgs(clijx, argsCLIJx, input2d);
                            }
                        }
                    }

                    Files.write(Paths.get(pluginName+ ".txt"), new byte[1]);
                } else {
                    System.out.println("ignoring " + pluginName);
                }
                table.show("Results");
            }
        }

        System.out.println("Found hashes: " + foundParameterHashes);

        table.saveAs("clij1clij2_comparison.csv");

        /*
        if (true) return;


        for (int i = 0; i < 10; i++) {

            //long time = System.currentTimeMillis();
            //clij.op().meanBox(input, output, 3, 3, 3);
            //System.out.println("CLIJ mean took " + (System.currentTimeMillis() - time) + " ms");

            long duration = benchmarkOp(clij, new Mean3DBox(), i2n3);
            System.out.println("CLIJ mean took " + (duration) + " ms");
        }


        for (int i = 0; i < 10; i++) {
            //long time = System.currentTimeMillis();
            //clijx.meanBox(input, output, 3, 3, 3);
            //System.out.println("CLIJx mean took " + (System.currentTimeMillis() - time) + " ms");
            long duration = benchmarkOp(clij, new net.haesleinhuepf.clijx.plugins.Mean3DBox(), i2n3);
            System.out.println("CLIJx mean took " + (duration) + " ms");

        }*/
    }

    private static void addTableLine(String comparisionA, String comparisonB, String dimension, int iteration) {
        table.incrementCounter();
        table.addValue("CLIJ plugin", comparisionA);
        table.addValue("CLIJx plugin", comparisionA);
        table.addValue("Image dimensions", dimension);
        table.addValue("iteration", iteration);
    }

    private static void compareResults(CLIJx clijx, Object[] argsCLIJ, Object[] argsCLIJx, String hash) {
        for (int i = 0; i < hash.length(); i++) {
            String typeHash = hash.substring(i, i+1);
            if (typeHash.compareTo("-") == 0) {
                if (argsCLIJ[i] instanceof ClearCLBuffer && argsCLIJx[i] instanceof ClearCLBuffer) {
                    ClearCLBuffer a = (ClearCLBuffer) argsCLIJ[i];
                    ClearCLBuffer b = (ClearCLBuffer) argsCLIJx[i];

                    double mean1 = clijx.meanOfAllPixels(a);
                    double mean2 = clijx.meanOfAllPixels(b);

                    double mse = clijx.meanSquaredError(a, b);

                    System.out.println("mean1: " + mean1);
                    System.out.println("mean2: " + mean2);
                    System.out.println("mse: " + mse);

                    table.addValue("Mean A", mean1);
                    table.addValue("Mean B", mean2);
                    table.addValue("Mean Squared Error", mse);

                    System.out.println("Equal results: " + TestUtilities.clBuffersEqual(clijx.getClij(), a, b, 0.001));

                } else {
                    System.out.println("No result found for one of both");
                }
            }
        }
    }

    private static void cleanUpArgs(CLIJx clijx, Object[] args, ClearCLBuffer input) {
        for (Object obj : args) {
            if (obj != input && obj instanceof ClearCLImageInterface) {
                clijx.release((ClearCLImageInterface) obj);
            }
        }
    }

    private static Object[] buildArgs(CLIJx clijx, CLIJMacroPlugin clijPlugin, String hash, ClearCLBuffer input) {
        Object[] args = new Object[hash.length()];

        ClearCLBuffer firstInput = null;

        for (int i = 0; i < args.length; i++) {
            String typeHash = hash.substring(i, i+1);
            if (typeHash.compareTo("i") == 0) {
                ClearCLBuffer copy = clijx.create(input);
                clijx.copy(input, copy);
                if (firstInput == null) {
                    firstInput = copy;
                }
                args[i] = copy;
            } else if (typeHash.compareTo("s") == 0) {
                args[i] = "";
            } else if (typeHash.compareTo("b") == 0) {
                args[i] = "1"; // true
            } else if (typeHash.compareTo("n") == 0) {
                args[i] = "3";
            } else if (typeHash.compareTo("a") == 0) {
                args[i] = "";
                System.out.println("Array requested");
            } else if (typeHash.compareTo("-") == 0) {
                args[i] = null;
            }
        }
        clijPlugin.setClij(clijx.getClij());
        clijPlugin.setArgs(args);

        for (int i = 0; i < args.length; i++) {
            String typeHash = hash.substring(i, i+1);
            if (typeHash.compareTo("-") == 0) {
                args[i] = clijPlugin.createOutputBufferFromSource(firstInput);
            }
        }

        System.out.println(Arrays.toString(args));
        return args;
    }

    private static String getParameterTypeHash(CLIJMacroPlugin clijPlugin) {
        String hash = "";
        String parameterHelpText = clijPlugin.getParameterHelpText();
        String[] parameterTypesAndNames = parameterHelpText.split(",");
        
        for (String parameterTypeAndName : parameterTypesAndNames) {
            String type = parameterTypeAndName.trim().split(" ")[0].toLowerCase();
            String name = parameterTypeAndName.trim().split(" ")[1].toLowerCase();
            if (name.contains("destination")) {
                hash = hash + "-";
            } else {
                if (type.compareTo("image") == 0) {
                    hash = hash + "i";
                } else if (type.compareTo("string") == 0) {
                    hash = hash + "s";
                } else if (type.compareTo("boolean") == 0) {
                    hash = hash + "b";
                } else if (type.compareTo("number") == 0) {
                    hash = hash + "n";
                } else if (type.compareTo("array") == 0) {
                    hash = hash + "a";
                }
            }
        }
        return hash;
    }

    private static long benchmarkOp(CLIJ clij, Object op, Object[] args) {
        if (op instanceof AbstractCLIJPlugin) {
            ((AbstractCLIJPlugin) op).setClij(clij);
            ((AbstractCLIJPlugin) op).setArgs(args);
        }
        if (op instanceof CLIJOpenCLProcessor) {
            long time = System.currentTimeMillis();
            ((CLIJOpenCLProcessor) op).executeCL();
            return (System.currentTimeMillis() - time);
        }
        return -1;
    }
}
