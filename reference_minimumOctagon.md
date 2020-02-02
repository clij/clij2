## minimumOctagon
![Image](images/mini_clij2_logo.png)![Image](images/mini_clijx_logo.png)

Applies a minimum filter with kernel size 3x3 n times to an image iteratively. Odd iterations are done with box neighborhood, even iterations with a diamond. Thus, with n > 2, the filter shape is an octagon. The given number of iterations makes the filter result very similar to minimum sphere. Approximately:radius = iterations - 2

### Usage in ImageJ macro
```
Ext.CLIJx_minimumOctagon(Image input, Image destination, Number iterations);
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
int arg3 = 10;
```

```
// Execute operation on GPU
clij2.minimumOctagon(clij, arg1, arg2, arg3);
```

```
//show result

// cleanup memory on GPU
clij2.release(arg1);
clij2.release(arg2);
```




### Example scripts
<a href="https://github.com/clij/clij-advanced-filters/blob/master/src/main/macro/"><img src="images/language_macro.png" height="20"/></a> [iterative_minimum.ijm](https://github.com/clij/clij-advanced-filters/blob/master/src/main/macro/iterative_minimum.ijm)  
<a href="https://github.com/clij/clij-advanced-filters/blob/master/src/main/macro/"><img src="images/language_macro.png" height="20"/></a> [minimumOctagon.ijm](https://github.com/clij/clij-advanced-filters/blob/master/src/main/macro/minimumOctagon.ijm)  


[Back to CLIJ documentation](https://clij.github.io/)

[Imprint](https://clij.github.io/imprint)
