## subtractBackground2D
![Image](images/mini_clijx_logo.png)

Applies Gaussian blur to the input image and subtracts the result from the original image.

### Usage in ImageJ macro
```
Ext.CLIJx_subtractBackground2D(Image input, Image destination, Number sigmaX, Number sigmaY);
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
float arg3 = 1.0;
float arg4 = 2.0;
```

```
// Execute operation on GPU
clijx.subtractBackground2D(clij, arg1, arg2, arg3, arg4);
```

```
//show result

// cleanup memory on GPU
arg1.close();
arg2.close();
```


[Back to CLIJ documentation](https://clij.github.io/)

[Imprint](https://clij.github.io/imprint)
