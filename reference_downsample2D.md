## downsample2D
![Image](images/mini_clijx_logo.png)

Scales an image using given scaling factors for X and Y dimensions. The nearest-neighbor method
is applied. In ImageJ the method which is similar is called 'Interpolation method: none'.

### Usage in ImageJ macro
```
Ext.CLIJx_downsample2D(Image source, Image destination, Number factorX, Number factorY);
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
clijx.downsample2D(clij, arg1, arg2, arg3, arg4);
```

```
//show result

// cleanup memory on GPU
arg1.close();
arg2.close();
```




### Example scripts
<a href="https://github.com/clij/clij-advanced-filters/blob/master/src/main/macro/"><img src="images/language_macro.png" height="20"/></a> [workflow.ijm](https://github.com/clij/clij-advanced-filters/blob/master/src/main/macro/workflow.ijm)  


[Back to CLIJ documentation](https://clij.github.io/)

[Imprint](https://clij.github.io/imprint)
