## drawBox
<img src="images/mini_empty_logo.png"/><img src="images/mini_clij2_logo.png"/><img src="images/mini_clijx_logo.png"/>

Draws a box at a given start point with given size. All pixels other than in the box are untouched. Consider using clij.op.set(buffer, 0); in advance.

### Usage in ImageJ macro
```
Ext.CLIJx_drawBox(Image destination, Number x, Number y, Number z, Number width, Number height, Number depth, Number value);
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
float width = 4.0;
float height = 5.0;
float depth = 6.0;
float value = 7.0;
```

```
// Execute operation on GPU
clij2.drawBox(clij, destination, x, y, z, width, height, depth, value);
```

```
//show result
destinationImagePlus = clij2.pull(destination);
destinationImagePlus.show();

// cleanup memory on GPU
clij2.release(destination);
```




### Example scripts
<a href="https://github.com/clij/clij-advanced-filters/blob/master/src/main/macro/"><img src="images/language_macro.png" height="20"/></a> [virtual_ablation.ijm](https://github.com/clij/clij-advanced-filters/blob/master/src/main/macro/virtual_ablation.ijm)  
<a href="https://github.com/clij/clij-advanced-filters/blob/master/src/main/macro/"><img src="images/language_macro.png" height="20"/></a> [rotating_sphere.ijm](https://github.com/clij/clij-advanced-filters/blob/master/src/main/macro/rotating_sphere.ijm)  
<a href="https://github.com/clij/clatlab/blob/master/src/main/matlab/"><img src="images/language_matlab.png" height="20"/></a> [drawBinaryRectangle.m](https://github.com/clij/clatlab/blob/master/src/main/matlab/drawBinaryRectangle.m)  


[Back to CLIJ documentation](https://clij.github.io/)

[Imprint](https://clij.github.io/imprint)
