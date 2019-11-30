## deformableRegistration2D
![Image](images/mini_clijx_logo.png)

Applies particle image velocimetry to two images and registers them afterwards by warping input image 2 with a smoothed vector field.

### Usage in ImageJ macro
```
Ext.CLIJx_deformableRegistration2D(Image input1, Image input2, Image destination, Number maxDeltaX, Number maxDeltaY);
```


### Usage in Java
```
// init CLIJ and GPU
import net.haesleinhuepf.clijx.CLIJ;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
CLIJx clijx = CLIJx.getInstance();

// get input parameters
ClearCLBuffer arg1 = clijx.push(arg1ImagePlus);
ClearCLBuffer arg2 = clijx.push(arg2ImagePlus);
ClearCLBuffer arg3 = clijx.push(arg3ImagePlus);
```

```
// Execute operation on GPU
clijx.deformableRegistration2D(clij, arg1, arg2, arg3, arg4, arg5);
```

```
//show result

// cleanup memory on GPU
arg1.close();
arg2.close();
arg3.close();
```


[Back to CLIJ documentation](https://clij.github.io/)

[Imprint](https://clij.github.io/imprint)
