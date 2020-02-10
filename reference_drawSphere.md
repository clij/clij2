## drawSphere
<img src="images/mini_empty_logo.png"/><img src="images/mini_clij2_logo.png"/><img src="images/mini_clijx_logo.png"/>

Draws a sphere around a given point with given radii in x, y and z (if 3D). All pixels other than in the sphere are untouched. Consider using clij.op.set(buffer, 0); in advance.

### Usage in ImageJ macro
```
Ext.CLIJx_drawSphere(Image destination, Number x, Number y, Number z, Number radius_x, Number radius_y, Number radius_z, Number value);
```


### Usage in Java
```
// init CLIJ and GPU
import net.haesleinhuepf.clij2.CLIJ;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
CLIJ2 clij2 = CLIJ2.getInstance();

// get input parameters
destination = clij.create();
float x = 1.0;
float y = 2.0;
float z = 3.0;
float radius_x = 4.0;
float radius_y = 5.0;
float radius_z = 6.0;
float value = 7.0;
```

```
// Execute operation on GPU
clij2.drawSphere(clij, destination, x, y, z, radius_x, radius_y, radius_z, value);
```

```
//show result
destinationImagePlus = clij2.pull(destination);
destinationImagePlus.show();

// cleanup memory on GPU
clij2.release(destination);
```




### Example scripts
<a href="https://github.com/clij/clij-advanced-filters/blob/master/src/main/jython/"><img src="images/language_jython.png" height="20"/></a> [jaccardIndex.py](https://github.com/clij/clij-advanced-filters/blob/master/src/main/jython/jaccardIndex.py)  


[Back to CLIJ documentation](https://clij.github.io/)

[Imprint](https://clij.github.io/imprint)
