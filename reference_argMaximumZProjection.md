## argMaximumZProjection
![Image](images/mini_clij1_logo.png)![Image](images/mini_clij2_logo.png)![Image](images/mini_clijx_logo.png)

Determines the maximum projection of an image stack along Z.
Furthermore, another 2D image is generated with pixels containing the z-index where the maximum was found (zero based).

### Usage in ImageJ macro
```
Ext.CLIJx_argMaximumZProjection(Image source, Image destination_max, Image destination_arg_max);
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
clij2.argMaximumZProjection(clij, arg1, arg2, arg3);
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
