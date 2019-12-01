## drawLine
![Image](images/mini_clijx_logo.png)

Draws a line between two points with a given thickness. All pixels other than on the line are untouched. Consider using clij.op.set(buffer, 0); in advance.

### Usage in ImageJ macro
```
Ext.CLIJx_drawLine(Image destination, Number x1, Number y1, Number z1, Number x2, Number y2, Number z2, Number thickness);
```


### Usage in Java
```
// init CLIJ and GPU
import net.haesleinhuepf.clijx.CLIJ;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
CLIJx clijx = CLIJx.getInstance();

// get input parameters
ClearCLBuffer arg1 = clijx.push(arg1ImagePlus);
float arg2 = 1.0;
float arg3 = 2.0;
float arg4 = 3.0;
float arg5 = 4.0;
float arg6 = 5.0;
float arg7 = 6.0;
float arg8 = 7.0;
```

```
// Execute operation on GPU
clijx.drawLine(clij, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8);
```

```
//show result

// cleanup memory on GPU
arg1.close();
```




### Example scripts
<a href="https://github.com/clij/clij-advanced-filters/blob/master/src/main/macro/"><img src="images/language_macro.png" height="20"/></a> [drawLine.ijm](https://github.com/clij/clij-advanced-filters/blob/master/src/main/macro/drawLine.ijm)  
<a href="https://github.com/clij/clij-advanced-filters/blob/master/src/main/macro/"><img src="images/language_macro.png" height="20"/></a> [grid.ijm](https://github.com/clij/clij-advanced-filters/blob/master/src/main/macro/grid.ijm)  
<a href="https://github.com/clij/clij-advanced-filters/blob/master/src/main/macro/"><img src="images/language_macro.png" height="20"/></a> [meshTouchingNeighbors.ijm](https://github.com/clij/clij-advanced-filters/blob/master/src/main/macro/meshTouchingNeighbors.ijm)  
<a href="https://github.com/clij/clij-advanced-filters/blob/master/src/main/macro/"><img src="images/language_macro.png" height="20"/></a> [mesh_closest_points.ijm](https://github.com/clij/clij-advanced-filters/blob/master/src/main/macro/mesh_closest_points.ijm)  


[Back to CLIJ documentation](https://clij.github.io/)

[Imprint](https://clij.github.io/imprint)
