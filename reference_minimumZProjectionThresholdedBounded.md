## minimumZProjectionThresholdedBounded
![Image](images/mini_clij2_logo.png)![Image](images/mini_clijx_logo.png)

Determines the minimum projection of all pixels in an image above a given threshold along Z within a given z range.

### Usage in ImageJ macro
```
Ext.CLIJx_minimumZProjectionThresholdedBounded(Image source, Image destination_min, Number min_z, Number max_z);
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
float arg3 = 1.0;
int arg4 = 10;
int arg5 = 20;
```

```
// Execute operation on GPU
clij2.minimumZProjectionThresholdedBounded(clij, arg1, arg2, arg3, arg4, arg5);
```

```
//show result

// cleanup memory on GPU
clij2.release(arg1);
clij2.release(arg2);
```


[Back to CLIJ documentation](https://clij.github.io/)

[Imprint](https://clij.github.io/imprint)
