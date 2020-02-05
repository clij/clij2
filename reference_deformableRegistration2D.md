## deformableRegistration2D
![Image](images/mini_clij2_logo.png)![Image](images/mini_clijx_logo.png)![Image](images/mini_clijx_logo.png)

Applies particle image velocimetry to two images and registers them afterwards by warping input image 2 with a smoothed vector field.

### Usage in ImageJ macro
```
Ext.CLIJx_deformableRegistration2D(Image input1, Image input2, Image destination, Number maxDeltaX, Number maxDeltaY);
```


### Usage in Java
```
// init CLIJ and GPU
import net.haesleinhuepf.clij2.CLIJ;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
CLIJ2 clij2 = CLIJ2.getInstance();

// get input parameters
ClearCLBuffer arg1 = clij2.push(arg1ImagePlus);
ClearCLBuffer arg2 = clij2.push(arg2ImagePlus);
ClearCLBuffer arg3 = clij2.push(arg3ImagePlus);
```

```
// Execute operation on GPU
clij2.deformableRegistration2D(clij, arg1, arg2, arg3, arg4, arg5);
```

```
//show result

// cleanup memory on GPU
clij2.release(arg1);
clij2.release(arg2);
clij2.release(arg3);
```


[Back to CLIJ documentation](https://clij.github.io/)

[Imprint](https://clij.github.io/imprint)
