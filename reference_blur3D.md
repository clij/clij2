## blur3D
<img src="images/mini_clij1_logo.png"/><img src="images/mini_clij2_logo.png"/><img src="images/mini_clijx_logo.png"/>

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
ClearCLBuffer source = clij2.push(sourceImagePlus);
destination = clij.create(source);
float sigmaX = 1.0;
float sigmaY = 2.0;
float sigmaZ = 3.0;
```

```
// Execute operation on GPU
clij2.blur3D(clij, source, destination, sigmaX, sigmaY, sigmaZ);
```

```
//show result
destinationImagePlus = clij2.pull(destination);
destinationImagePlus.show();

// cleanup memory on GPU
clij2.release(source);
clij2.release(destination);
```




### Example scripts
<a href="https://github.com/clij/clij-advanced-filters/blob/master/src/main/macro/"><img src="images/language_macro.png" height="20"/></a> [benchmarkingGaussianBlurs_x.ijm](https://github.com/clij/clij-advanced-filters/blob/master/src/main/macro/benchmarkingGaussianBlurs_x.ijm)  
<a href="https://github.com/clij/clij-advanced-filters/blob/master/src/main/macro/"><img src="images/language_macro.png" height="20"/></a> [preloading.ijm](https://github.com/clij/clij-advanced-filters/blob/master/src/main/macro/preloading.ijm)  


[Back to CLIJ documentation](https://clij.github.io/)

[Imprint](https://clij.github.io/imprint)
