## blur3D
![Image](images/mini_clij1_logo.png)![Image](images/mini_clij2_logo.png)![Image](images/mini_clijx_logo.png)

Computes the Gaussian blurred image of an image given two sigma values in X, Y and Z. Thus, the filterkernel can have non-isotropic shape.

The implementation is done separable. In case a sigma equals zero, the direction is not blurred.

### Usage in ImageJ macro
```
Ext.CLIJx_blur3D(Image source, Image destination, Number sigmaX, Number sigmaY, Number sigmaZ);
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
float arg4 = 2.0;
float arg5 = 3.0;
```

```
// Execute operation on GPU
clij2.blur3D(clij, arg1, arg2, arg3, arg4, arg5);
```

```
//show result

// cleanup memory on GPU
clij2.release(arg1);
clij2.release(arg2);
```




### Example scripts
<a href="https://github.com/clij/clij-advanced-filters/blob/master/src/main/macro/"><img src="images/language_macro.png" height="20"/></a> [benchmarkingGaussianBlurs_x.ijm](https://github.com/clij/clij-advanced-filters/blob/master/src/main/macro/benchmarkingGaussianBlurs_x.ijm)  
<a href="https://github.com/clij/clij-advanced-filters/blob/master/src/main/macro/"><img src="images/language_macro.png" height="20"/></a> [preloading.ijm](https://github.com/clij/clij-advanced-filters/blob/master/src/main/macro/preloading.ijm)  


[Back to CLIJ documentation](https://clij.github.io/)

[Imprint](https://clij.github.io/imprint)
