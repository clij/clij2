package net.haesleinhuepf.clij2;

import ij.ImagePlus;
import ij.plugin.Duplicator;
import net.haesleinhuepf.clij.CLIJ;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.clearcl.ClearCLImage;
import net.haesleinhuepf.clij.clearcl.ClearCLKernel;
import net.haesleinhuepf.clij.clearcl.enums.ImageChannelDataType;
import net.haesleinhuepf.clij.clearcl.interfaces.ClearCLImageInterface;
import net.haesleinhuepf.clij.clearcl.util.ElapsedTime;
import net.haesleinhuepf.clij.coremem.enums.NativeTypeEnum;
import net.haesleinhuepf.clij.clearcl.util.CLKernelExecutor;
import net.imglib2.Cursor;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.type.numeric.RealType;
import net.imglib2.view.Views;

import java.nio.ByteBuffer;
import java.util.HashMap;


/**
 * The CLIJ2 gateway
 *
 * Author: @haesleinhuepf
 * December 2019
 */
public class CLIJ2 implements CLIJ2Ops {
    private static CLIJ2 instance;

    protected CLIJ clij;

    protected boolean waitForKernelFinish = true;


    public CLIJ getCLIJ() {
        return clij;
    }
    public CLIJ2 getCLIJ2() {
        return this;
    }

    protected final CLKernelExecutor mCLKernelExecutor;

    /**
     * Marking this as deprecated as it will very likely go away before release.
     * Use CLIJx.getInstance() instead.
     * @param clij
     */
    @Deprecated
    public CLIJ2(CLIJ clij) {
        this.clij = clij;
        mCLKernelExecutor = new CLKernelExecutor(clij.getClearCLContext());
    }

    public static CLIJ2 getInstance() {
        CLIJ clij = CLIJ.getInstance();
        if (instance == null || instance.clij != CLIJ.getInstance()) {
            instance = new CLIJ2(clij);
        }
        return instance;
    }

    public static CLIJ2 getInstance(String id) {
        CLIJ clij = CLIJ.getInstance(id);
        if (instance == null || instance.clij != clij) {
            instance = new CLIJ2(clij);
        }
        return instance;
    }

    public static String clinfo() {
        return CLIJ.clinfo();
    }

    public String getGPUName() {
        return clij.getGPUName();
    }

    public double getOpenCLVersion() {
        return clij.getOpenCLVersion();
    }

    public ClearCLBuffer push(Object object) {
        ClearCLBuffer buffer = clij.convert(object, ClearCLBuffer.class);
        return buffer;
    }

    public ClearCLBuffer pushCurrentZStack(ImagePlus imp) {
        ClearCLBuffer buffer = clij.pushCurrentZStack(imp);
        return buffer;
    }

    public ClearCLBuffer pushCurrentSlice(ImagePlus imp) {
        ClearCLBuffer buffer = clij.pushCurrentSlice(imp);
        return buffer;
    }

    public ClearCLBuffer pushCurrentSelection(ImagePlus imp) {
        imp = new Duplicator().run(imp);
        ClearCLBuffer buffer = clij.pushCurrentSlice(imp);
        return buffer;
    }

    public ImagePlus pull(Object object) {
        return clij.convert(object, ImagePlus.class);
    }

    public RandomAccessibleInterval pullRAI(Object object) {
        return clij.convert(object, RandomAccessibleInterval.class);
    }

    public void pullToRAI(Object object, RandomAccessibleInterval target) {
        RandomAccessibleInterval rai = pullRAI(object);

        Cursor<RealType> cursor = Views.iterable(rai).cursor();
        Cursor<RealType> target_cursor = Views.iterable(target).cursor();

        while(cursor.hasNext() && target_cursor.hasNext()) {
            target_cursor.next().set(cursor.next());
        }
    }

    public void show(Object object, String title) {
        ImagePlus imp = clij.convert(object, ImagePlus.class);
        clij.show(imp, title);
    }

    public <T> T convert(Object object, Class<T> klass) {
        return clij.convert(object, klass);
    }

    public ClearCLBuffer create(ClearCLBuffer buffer) {
        ClearCLBuffer result = clij.create(buffer);
        return result;
    }

    public ClearCLImage create(ClearCLImage image) {
        ClearCLImage result = clij.create(image);
        return result;
    }

    public ClearCLBuffer create(long dimensionX, long dimensionY, long dimensionZ) {
        return create(new long[]{dimensionX, dimensionY, dimensionZ}, NativeTypeEnum.Float);
    }

    public ClearCLBuffer create(long dimensionX, long dimensionY) {
        return create(new long[]{dimensionX, dimensionY}, NativeTypeEnum.Float);
    }

    public ClearCLBuffer create(long[] dimensions) {
        ClearCLBuffer buffer = create(dimensions, NativeTypeEnum.Float);
        return buffer;
    }

    public ClearCLBuffer create(double[] dblDimensions) {
        long[] dimensions = new long[dblDimensions.length];
        for (int i = 0; i < dimensions.length; i++) {
            dimensions[i] = (long)dblDimensions[i];
        }
        return create(dimensions, NativeTypeEnum.Float);
    }


    public ClearCLBuffer create(long[] dimensions, NativeTypeEnum typeEnum) {
        ClearCLBuffer buffer = clij.create(dimensions, typeEnum);
        return buffer;
    }

    public ClearCLImage create(long[] dimensions, ImageChannelDataType typeEnum) {
        ClearCLImage image = clij.create(dimensions, typeEnum);
        return image;
    }

    public void execute(String programFilename, String kernelname, long[] dimensions, long[] globalsizes, HashMap<String, Object> parameters, HashMap<String, Object> constants) {
        ClearCLKernel kernel = executeSubsequently(null, programFilename, kernelname,  dimensions, globalsizes, parameters, constants, null);
        kernel.close();
    }

    public void execute(Class anchorClass, String programFilename, String kernelname, long[] dimensions, long[] globalsizes, HashMap<String, Object> parameters, HashMap<String, Object> constants) {
        ClearCLKernel kernel = executeSubsequently(anchorClass, programFilename, kernelname,  dimensions, globalsizes, parameters, constants, null);
        kernel.close();
    }

    public void execute(Class anchorClass, String programFilename, String kernelname, long[] dimensions, long[] globalsizes, HashMap<String, Object> parameters) {
        ClearCLKernel kernel = executeSubsequently(anchorClass, programFilename, kernelname,  dimensions, globalsizes, parameters, null);
        kernel.close();
    }

    public void execute(Class anchorClass, String programFilename, String kernelname, long[] dimensions, long[] globalsizes, long[] localSizes, HashMap<String, Object> parameters) {
        ClearCLKernel kernel = executeSubsequently(anchorClass, programFilename, kernelname,  dimensions, globalsizes, localSizes, parameters, null,null);
        kernel.close();
    }

    public void execute(Class anchorClass, String programFilename, String kernelname, long[] dimensions, long[] globalsizes, long[] localSizes, HashMap<String, Object> parameters, HashMap<String, Object> constants) {
        ClearCLKernel kernel = executeSubsequently(anchorClass, programFilename, kernelname,  dimensions, globalsizes, localSizes, parameters, constants,null);
        kernel.close();
    }

    public ClearCLKernel executeSubsequently(Class anchorClass, String pProgramFilename, String pKernelname, long[] dimensions, long[] globalsizes, HashMap<String, Object> parameters, ClearCLKernel kernel) {
        return executeSubsequently(anchorClass, pProgramFilename, pKernelname, dimensions, globalsizes, parameters, null, kernel);
    }


    public ClearCLKernel executeSubsequently(Class anchorClass, String pProgramFilename, String pKernelname, long[] dimensions, long[] globalsizes, HashMap<String, Object> parameters, HashMap<String, Object> constants, ClearCLKernel kernel) {
        return executeSubsequently(anchorClass, pProgramFilename, pKernelname, dimensions, globalsizes, null, parameters, constants, kernel);
    }

    public synchronized ClearCLKernel executeSubsequently(Class anchorClass, String pProgramFilename, String pKernelname, long[] dimensions, long[] globalsizes, long[] localSizes, HashMap<String, Object> parameters, HashMap<String, Object> constants, ClearCLKernel kernel) {

        final ClearCLKernel[] result = {kernel};

        if (CLIJ.debug) {
            for (String key : parameters.keySet()) {
                System.out.println(key + " = " + parameters.get(key));
            }
        }

        ElapsedTime.measure("kernel + build " + pKernelname, () -> {
            mCLKernelExecutor.setProgramFilename(pProgramFilename);
            mCLKernelExecutor.setKernelName(pKernelname);
            mCLKernelExecutor.setAnchorClass(anchorClass);
            mCLKernelExecutor.setParameterMap(parameters);
            mCLKernelExecutor.setConstantsMap(constants);
            mCLKernelExecutor.setGlobalSizes(globalsizes);
            mCLKernelExecutor.setLocalSizes(localSizes);

            result[0] = mCLKernelExecutor.enqueue(waitForKernelFinish, kernel);

            mCLKernelExecutor.setImageSizeIndependentCompilation(false);
        });

        return result[0];
    }



    public void executeCode(String sourceCode, String kernelname, long[] dimensions, long[] globalsizes, HashMap<String, Object> parameters, HashMap<String, Object> constants) {
        ClearCLKernel kernel = executeCodeSubsequently(sourceCode, kernelname,  dimensions, globalsizes, parameters, constants, null);
        kernel.close();
    }

    public void executeCode(String sourceCode, String kernelname, long[] dimensions, long[] globalsizes, HashMap<String, Object> parameters) {
        ClearCLKernel kernel = executeCodeSubsequently(sourceCode, kernelname,  dimensions, globalsizes, parameters, null);
        kernel.close();
    }

    public void executeCode(String sourceCode, String kernelname, long[] dimensions, long[] globalsizes, long[] localSizes, HashMap<String, Object> parameters) {
        ClearCLKernel kernel = executeCodeSubsequently(sourceCode, kernelname,  dimensions, globalsizes, localSizes, parameters, null,null);
        kernel.close();
    }

    public ClearCLKernel executeCodeSubsequently(String sourceCode, String pKernelname, long[] dimensions, long[] globalsizes, HashMap<String, Object> parameters, ClearCLKernel kernel) {
        return executeCodeSubsequently(sourceCode, pKernelname, dimensions, globalsizes, parameters, null, kernel);
    }


    public ClearCLKernel executeCodeSubsequently(String sourceCode, String pKernelname, long[] dimensions, long[] globalsizes, HashMap<String, Object> parameters, HashMap<String, Object> constants, ClearCLKernel kernel) {
        return executeCodeSubsequently(sourceCode, pKernelname, dimensions, globalsizes, null, parameters, constants, kernel);
    }


    public ClearCLKernel executeCodeSubsequently(String sourceCode, String pKernelname, long[] dimensions, long[] globalsizes, long[] localSizes, HashMap<String, Object> parameters, HashMap<String, Object> constants, ClearCLKernel kernel) {

        final ClearCLKernel[] result = {kernel};

        if (CLIJ.debug) {
            for (String key : parameters.keySet()) {
                System.out.println(key + " = " + parameters.get(key));
            }
        }

        ElapsedTime.measure("kernel + build " + pKernelname, () -> {
            mCLKernelExecutor.setProgramSourceCode(sourceCode);
            mCLKernelExecutor.setKernelName(pKernelname);
            mCLKernelExecutor.setAnchorClass(Object.class);
            mCLKernelExecutor.setParameterMap(parameters);
            mCLKernelExecutor.setConstantsMap(constants);
            mCLKernelExecutor.setGlobalSizes(globalsizes);
            mCLKernelExecutor.setLocalSizes(localSizes);

            result[0] = mCLKernelExecutor.enqueue(waitForKernelFinish, kernel);

            mCLKernelExecutor.setImageSizeIndependentCompilation(false);
        });

        return result[0];
    }

    public boolean isSizeIndependentKernelCompilation() {
        return mCLKernelExecutor.isImageSizeIndependentCompilation();
    }

    public void activateSizeIndependentKernelCompilation() {
        mCLKernelExecutor.setImageSizeIndependentCompilation(true);
    }
    public void setWaitForKernelFinish(boolean waitForKernelFinish) {
        this.waitForKernelFinish = waitForKernelFinish;
    }


    @Deprecated
    public CLIJ getClij() {
        return clij;
    }

    /**
     * This method is for debugging purposes only
     * @param keepReferences
     */
    @Deprecated
    public void setKeepReferences(boolean keepReferences) {
        System.out.println("CLIJ2.setKeepReferences is obsolete.");
    }

    public void release(ClearCLImageInterface image) {
        if (image != null) {
            image.close();
        }
    }

    public void clear() {
        getCLIJ().getClearCLContext().releaseImages();
    }

    public String reportMemory() {
        return getCLIJ().getClearCLContext().reportAboutAllocatedImages();
    }


    public void close() {
        clear();

        if (this == instance) {
            instance = null;
        }

        clij.close();
    }

    public final NativeTypeEnum Float = NativeTypeEnum.Float;
    public final NativeTypeEnum UnsignedShort = NativeTypeEnum.UnsignedShort;
    public final NativeTypeEnum UnsignedByte = NativeTypeEnum.UnsignedByte;

    public CLIJ2 __enter__() {
        clear();
        return this;
    }

    public void __exit__(Object... args) {
        clear();
    }

    public boolean hasImageSupport() {
        return clij.hasImageSupport();
    }

    public ImagePlus pullBinary(ClearCLBuffer input) {
        return clij.pullBinary(input);
    }

    public void invalidateKernelCahe() {
        mCLKernelExecutor.close();
    }

    /**
     * Transfer a buffer from a different OpenCLDevice
     * @param input
     * @return
     */
    public ClearCLBuffer transfer(ClearCLBuffer input) {
        ClearCLBuffer output = create(input);
        //System.out.println("Transfer from: " + input);
        //System.out.println("Transfer to: " + output);
        ByteBuffer buffer = ByteBuffer.allocate((int) input.getSizeInBytes());
        input.writeTo(buffer, true);
        output.readFrom(buffer, true);
        return output;
    }
}
