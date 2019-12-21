## minimum3DBox
![Image](images/mini_clijx_logo.png)

Computes the local minimum of a pixels cube neighborhood. The cubes size is specified by 
its half-width, half-height and half-depth (radius).

### Usage in ImageJ macro
```
Ext.CLIJx_minimum3DBox(Image source, Image destination, Number radiusX, Number radiusY, Number radiusZ);
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
int arg3 = 10;
int arg4 = 20;
int arg5 = 30;
```

```
// Execute operation on GPU
clijx.minimum3DBox(clij, arg1, arg2, arg3, arg4, arg5);
```

```
//show result

// cleanup memory on GPU
arg1.close();
arg2.close();
```




### Example scripts
<a href="https://github.com/clij/clij-advanced-filters/blob/master/src/main/macro/"><img src="images/language_macro.png" height="20"/></a> [iterative_minimum.ijm](https://github.com/clij/clij-advanced-filters/blob/master/src/main/macro/iterative_minimum.ijm)  
<a href="https://github.com/clij/clij-advanced-filters/blob/master/src/main/macro/"><img src="images/language_macro.png" height="20"/></a> [minimumOctagon.ijm](https://github.com/clij/clij-advanced-filters/blob/master/src/main/macro/minimumOctagon.ijm)  
<a href="https://github.com/clij/clij-docs/blob/master/src/main/macro/"><img src="images/language_macro.png" height="20"/></a> [minimum.ijm](https://github.com/clij/clij-docs/blob/master/src/main/macro/minimum.ijm)  
<a href="https://github.com/clij/clij-docs/blob/master/src/main/macro/"><img src="images/language_macro.png" height="20"/></a> [topHat.ijm](https://github.com/clij/clij-docs/blob/master/src/main/macro/topHat.ijm)  


[Back to CLIJ documentation](https://clij.github.io/)

[Imprint](https://clij.github.io/imprint)
