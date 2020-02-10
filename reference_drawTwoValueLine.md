## drawTwoValueLine
<img src="images/mini_empty_logo.png"/><img src="images/mini_empty_logo.png"/><img src="images/mini_clijx_logo.png"/>

Draws a line between two points with a given thickness. Pixels close to point 1 are set to value1. Pixels closer to point 2 are set to value2 All pixels other than on the line are untouched. Consider using clij.set(buffer, 0); in advance.

### Usage in ImageJ macro
```
Ext.CLIJx_drawTwoValueLine(Image destination, Number x1, Number y1, Number z1, Number x2, Number y2, Number z2, Number thickness, Number value1, Number value2);
```


### Usage in Java
```
// init CLIJ and GPU
import net.haesleinhuepf.clij2.CLIJ;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
CLIJ2 clij2 = CLIJ2.getInstance();

// get input parameters
destination = clij.create();
float x1 = 1.0;
float y1 = 2.0;
float z1 = 3.0;
float x2 = 4.0;
float y2 = 5.0;
float z2 = 6.0;
float thickness = 7.0;
float value1 = 8.0;
float destination0 = 9.0;
```

```
// Execute operation on GPU
clij2.drawTwoValueLine(clij, destination, x1, y1, z1, x2, y2, z2, thickness, value1, destination0);
```

```
//show result
destinationImagePlus = clij2.pull(destination);
destinationImagePlus.show();

// cleanup memory on GPU
clij2.release(destination);
```




### Example scripts
<a href="https://github.com/clij/clij-advanced-filters/blob/master/src/main/jython/"><img src="images/language_jython.png" height="20"/></a> [countNeighbors3D.py](https://github.com/clij/clij-advanced-filters/blob/master/src/main/jython/countNeighbors3D.py)  


[Back to CLIJ documentation](https://clij.github.io/)

[Imprint](https://clij.github.io/imprint)
