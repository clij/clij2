## blur2D
![Image](images/mini_clijx_logo.png)

Computes the Gaussian blurred image of an image given two sigma values in X and Y. Thus, the filterkernel can have non-isotropic shape.

The implementation is done separable. In case a sigma equals zero, the direction is not blurred.

### Usage in ImageJ macro
```
Ext.CLIJx_blur2D(Image source, Image destination, Number sigmaX, Number sigmaY);
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
clijx.blur2D(clij, arg1, arg2, arg3, arg4);
```

```
//show result

// cleanup memory on GPU
arg1.close();
arg2.close();
```




### Example scripts
<a href="https://github.com/clij/clij-advanced-filters/blob/master/src/main/macro/"><img src="images/language_macro.png" height="20"/></a> [mesh_closest_points.ijm](https://github.com/clij/clij-advanced-filters/blob/master/src/main/macro/mesh_closest_points.ijm)  
<a href="https://github.com/clij/clij-advanced-filters/blob/master/src/main/macro/"><img src="images/language_macro.png" height="20"/></a> [spot_distance_measurement.ijm](https://github.com/clij/clij-advanced-filters/blob/master/src/main/macro/spot_distance_measurement.ijm)  


[Back to CLIJ documentation](https://clij.github.io/)

[Imprint](https://clij.github.io/imprint)
