## drawLine
<img src="images/mini_empty_logo.png"/><img src="images/mini_clij2_logo.png"/><img src="images/mini_clijx_logo.png"/>

Draws a line between two points with a given thickness. All pixels other than on the line are untouched. Consider using clij.op.set(buffer, 0); in advance.

### Usage in ImageJ macro
```
Ext.CLIJx_drawLine(Image destination, Number x1, Number y1, Number z1, Number x2, Number y2, Number z2, Number thickness, Number value);
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
float value = 8.0;
```

```
// Execute operation on GPU
clij2.drawLine(clij, destination, x1, y1, z1, x2, y2, z2, thickness, value);
```

```
//show result
destinationImagePlus = clij2.pull(destination);
destinationImagePlus.show();

// cleanup memory on GPU
clij2.release(destination);
```




### Example scripts
<a href="https://github.com/clij/clij-advanced-filters/blob/master/src/main/macro/"><img src="images/language_macro.png" height="20"/></a> [drawLine.ijm](https://github.com/clij/clij-advanced-filters/blob/master/src/main/macro/drawLine.ijm)  
<a href="https://github.com/clij/clij-advanced-filters/blob/master/src/main/macro/"><img src="images/language_macro.png" height="20"/></a> [grid.ijm](https://github.com/clij/clij-advanced-filters/blob/master/src/main/macro/grid.ijm)  
<a href="https://github.com/clij/clij-advanced-filters/blob/master/src/main/macro/"><img src="images/language_macro.png" height="20"/></a> [meshTouchingNeighbors.ijm](https://github.com/clij/clij-advanced-filters/blob/master/src/main/macro/meshTouchingNeighbors.ijm)  
<a href="https://github.com/clij/clij-advanced-filters/blob/master/src/main/macro/"><img src="images/language_macro.png" height="20"/></a> [mesh_closest_points.ijm](https://github.com/clij/clij-advanced-filters/blob/master/src/main/macro/mesh_closest_points.ijm)  
<a href="https://github.com/clij/clatlab/blob/master/src/main/matlab/"><img src="images/language_matlab.png" height="20"/></a> [meshClosestPoints.m](https://github.com/clij/clatlab/blob/master/src/main/matlab/meshClosestPoints.m)  


[Back to CLIJ documentation](https://clij.github.io/)

[Imprint](https://clij.github.io/imprint)
