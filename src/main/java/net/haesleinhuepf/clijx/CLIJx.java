package net.haesleinhuepf.clijx;

import ij.IJ;
import ij.ImagePlus;
import net.haesleinhuepf.clij.CLIJ;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.clearcl.ClearCLImage;
import net.haesleinhuepf.clij.clearcl.ClearCLKernel;
import net.haesleinhuepf.clij.clearcl.enums.ImageChannelDataType;
import net.haesleinhuepf.clij.clearcl.interfaces.ClearCLImageInterface;
import net.haesleinhuepf.clij.clearcl.util.ElapsedTime;
import net.haesleinhuepf.clij.converters.implementations.ClearCLBufferToImagePlusConverter;
import net.haesleinhuepf.clij.coremem.enums.NativeTypeEnum;
import net.haesleinhuepf.clij.utilities.TypeFixer;
import net.haesleinhuepf.clijx.utilities.CLIJxOps;
import net.haesleinhuepf.clijx.utilities.CLKernelExecutor;
import net.imglib2.Cursor;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.type.numeric.RealType;
import net.imglib2.view.Views;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * The CLIJx gateway
 */
public class CLIJx extends CLIJxOps{
    private static CLIJx instance;

    @Deprecated // use clijx instead of clijx.op
    public final CLIJxOps op;

    private final CLKernelExecutor mCLKernelExecutor;

    /**
     * Marking this as deprecated as it will very likely go away before release.
     * Use CLIJx.getInstance() instead.
     * @param clij
     */
    @Deprecated
    public CLIJx(CLIJ clij) {
        this.clij = clij;
        this.clijx = this;
        op = this; //new CLIJxOps(this);
        mCLKernelExecutor = new CLKernelExecutor(clij.getClearCLContext());
    }

    public static CLIJx getInstance() {
        CLIJ clij = CLIJ.getInstance();
        if (instance == null || instance.clij != CLIJ.getInstance()) {
            instance = new CLIJx(clij);
        }
        return instance;
    }

    public static CLIJx getInstance(String id) {
        CLIJ clij = CLIJ.getInstance(id);
        if (instance == null || instance.clij != clij) {
            instance = new CLIJx(clij);
        }
        return instance;
    }

    public String getGPUName() {
        return clij.getGPUName();
    }

    public double getOpenCLVersion() {
        return clij.getOpenCLVersion();
    }

    public ClearCLBuffer push(Object object) {
        ClearCLBuffer buffer = clij.convert(object, ClearCLBuffer.class);
        registerReference(buffer);
        return buffer;
    }

    public ClearCLBuffer pushCurrentZStack(ImagePlus imp) {
        ClearCLBuffer buffer = clij.pushCurrentZStack(imp);
        registerReference(buffer);
        return buffer;
    }

    public ClearCLBuffer pushCurrentSlice(ImagePlus imp) {
        ClearCLBuffer buffer = clij.pushCurrentSlice(imp);
        registerReference(buffer);
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
        registerReference(result);
        return result;
    }

    public ClearCLImage create(ClearCLImage image) {
        ClearCLImage result = clij.create(image);
        registerReference(result);
        return result;
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
        registerReference(buffer);
        return buffer;
    }

    public ClearCLImage create(long[] dimensions, ImageChannelDataType typeEnum) {
        ClearCLImage image = clij.create(dimensions, typeEnum);
        registerReference(image);
        return image;
    }

    private static boolean notifiedDeprecated = false;
    @Deprecated // use clijx instead of clijx.op()
    public CLIJxOps op() {
        if (!notifiedDeprecated) {
            IJ.log("Warning: clijx.op().anyMethod() and clijx.op.anyMethod() are deprecated. Use clijx.anyMethod() instead.");
            notifiedDeprecated = true;
        }
        return op;
    }

    public void execute(Class anchorClass, String programFilename, String kernelname, long[] dimensions, long[] globalsizes, HashMap<String, Object> parameters) {
        ClearCLKernel kernel = executeSubsequently(anchorClass, programFilename, kernelname,  dimensions, globalsizes, parameters, null);
        kernel.close();
    }

    public ClearCLKernel executeSubsequently(Class anchorClass, String pProgramFilename, String pKernelname, long[] dimensions, long[] globalsizes, HashMap<String, Object> parameters, ClearCLKernel kernel) {
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
            mCLKernelExecutor.setGlobalSizes(globalsizes);

            result[0] = mCLKernelExecutor.enqueue(true, kernel);
        });

        return result[0];
    }

    @Deprecated
    public CLIJ getClij() {
        return clij;
    }

    private boolean keepReferences = true;

    /**
     * This method is for debugging purposes only
     * @param keepReferences
     */
    @Deprecated
    public void setKeepReferences(boolean keepReferences) {
        this.keepReferences = keepReferences;
    }

    ArrayList<ClearCLImageInterface> references = new ArrayList<>();
    private void registerReference(ClearCLImageInterface image) {
        if (keepReferences) {
            references.add(image);
            for (int i = references.size() - 1; i >= 0; i--) {
                ClearCLImageInterface buffer = references.get(i);
                if (buffer instanceof ClearCLImage && ((ClearCLImage) buffer).getPeerPointer() == null) {
                    references.remove(i);
                } else if (buffer instanceof ClearCLBuffer && ((ClearCLBuffer) buffer).getPeerPointer() == null) {
                    references.remove(i);
                }
            }
        }
    }

    public void release(ClearCLImageInterface image) {
        references.remove(image);
        if (image instanceof ClearCLImage) {
            ((ClearCLImage) image).close();
        } else if (image instanceof ClearCLBuffer) {
            ((ClearCLBuffer) image).close();
        }
    }

    public void clear() {
        for (ClearCLImageInterface image : references) {
            if (image instanceof ClearCLImage) {
                ((ClearCLImage) image).close();
            } else if (image instanceof ClearCLBuffer) {
                ((ClearCLBuffer) image).close();
            }
        }
        references.clear();
    }

    public String reportMemory() {
        StringBuilder stringBuilder = new StringBuilder();
        long bytesSum = 0;
        stringBuilder.append("GPU contains " + (references.size() )+ " images.\n");
        boolean wasClosedAlready = false;
        for (ClearCLImageInterface buffer : references) {
            String star = "";
            if (buffer instanceof ClearCLImage && ((ClearCLImage) buffer).getPeerPointer() == null) {
                star = "*";
                wasClosedAlready = true;
            } else if (buffer instanceof ClearCLBuffer && ((ClearCLBuffer) buffer).getPeerPointer() == null) {
                star = "*";
                wasClosedAlready = true;
            } else {
                bytesSum = bytesSum + buffer.getSizeInBytes();
            }

            stringBuilder.append("- " + buffer.getName() + star + " " + humanReadableBytes(buffer.getSizeInBytes()) + " [" + buffer.toString() + "] " + humanReadableBytes(buffer.getSizeInBytes()) + "\n");
        }
        stringBuilder.append("= " + humanReadableBytes(bytesSum) +"\n");
        if (wasClosedAlready) {
            stringBuilder.append("  * Some images/buffers were closed already.\n");
        }
        return stringBuilder.toString();
    }

    private String humanReadableBytes(double bytesSum) {
        if (bytesSum > 1024) {
            bytesSum = bytesSum / 1024;
            if (bytesSum > 1024) {
                bytesSum = bytesSum / 1024;
                if (bytesSum > 1024) {
                    bytesSum = bytesSum / 1024;
                    return (Math.round(bytesSum * 10.0) / 10.0 + " Gb");
                } else {
                    return (Math.round(bytesSum * 10.0) / 10.0 + " Mb");
                }
            } else {
                return (Math.round(bytesSum * 10.0) / 10.0 + " kb");
            }
        }
        return Math.round(bytesSum * 10.0) / 10.0 + " b";
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

    public CLIJx __enter__() {
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
}
