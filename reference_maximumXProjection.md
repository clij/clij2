## maximumXProjection
![Image](images/mini_clijx_logo.png)

Determines the maximum projection of an image along X.

### Usage in ImageJ macro
```
Ext.CLIJx_maximumXProjection(Image source, Image destination_max);
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
```

```
// Execute operation on GPU
clijx.maximumXProjection(clij, arg1, arg2);
```

```
//show result

// cleanup memory on GPU
arg1.close();
arg2.close();
```




### Example scripts
<a href="https://github.com/clij/clij-advanced-filters/blob/master/src/main/macro/"><img src="images/language_macro.png" height="20"/></a> [orthogonalMaximumProjections.ijm](https://github.com/clij/clij-advanced-filters/blob/master/src/main/macro/orthogonalMaximumProjections.ijm)  


[Back to CLIJ documentation](https://clij.github.io/)

[Imprint](https://clij.github.io/imprint)
