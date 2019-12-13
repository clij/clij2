## scale2D
![Image](images/mini_clijx_logo.png)

Scales an image with a given factor.

### Usage in ImageJ macro
```
Ext.CLIJx_scale2D(Image source, Image destination, Number scaling_factor, Boolean scale_to_center);
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
clijx.scale2D(clij, arg1, arg2, arg3, arg4);
```

```
//show result

// cleanup memory on GPU
arg1.close();
arg2.close();
```




### Example scripts
<a href="https://github.com/clij/clij-docs/blob/master/src/main/macro/"><img src="images/language_macro.png" height="20"/></a> [scaleFree.ijm](https://github.com/clij/clij-docs/blob/master/src/main/macro/scaleFree.ijm)  


[Back to CLIJ documentation](https://clij.github.io/)

[Imprint](https://clij.github.io/imprint)
