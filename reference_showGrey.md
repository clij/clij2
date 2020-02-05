## showGrey
![Image](images/mini_clij2_logo.png)![Image](images/mini_clijx_logo.png)![Image](images/mini_clijx_logo.png)

Visualises a single 2D image.

### Usage in ImageJ macro
```
Ext.CLIJx_showGrey(Image input, String title);
```


### Usage in Java
```
// init CLIJ and GPU
import net.haesleinhuepf.clij2.CLIJ;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
CLIJ2 clij2 = CLIJ2.getInstance();

// get input parameters
ClearCLBuffer arg1 = clij2.push(arg1ImagePlus);
```

```
// Execute operation on GPU
clij2.showGrey(clij, arg1, arg2);
```

```
//show result

// cleanup memory on GPU
clij2.release(arg1);
```




### Example scripts
<a href="https://github.com/clij/clij-advanced-filters/blob/master/src/main/macro/"><img src="images/language_macro.png" height="20"/></a> [showRGB.ijm](https://github.com/clij/clij-advanced-filters/blob/master/src/main/macro/showRGB.ijm)  
<a href="https://github.com/clij/clij-advanced-filters/blob/master/src/main/macro/"><img src="images/language_macro.png" height="20"/></a> [showRGB_x.ijm](https://github.com/clij/clij-advanced-filters/blob/master/src/main/macro/showRGB_x.ijm)  


[Back to CLIJ documentation](https://clij.github.io/)

[Imprint](https://clij.github.io/imprint)
