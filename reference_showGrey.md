## showGrey
<img src="images/mini_empty_logo.png"/><img src="images/mini_empty_logo.png"/><img src="images/mini_clijx_logo.png"/>

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
ClearCLBuffer input = clij2.push(inputImagePlus);
```

```
// Execute operation on GPU
clij2.showGrey(clij, input, title);
```

```
//show result

// cleanup memory on GPU
clij2.release(input);
```




### Example scripts
<a href="https://github.com/clij/clij-advanced-filters/blob/master/src/main/macro/"><img src="images/language_macro.png" height="20"/></a> [showRGB.ijm](https://github.com/clij/clij-advanced-filters/blob/master/src/main/macro/showRGB.ijm)  
<a href="https://github.com/clij/clij-advanced-filters/blob/master/src/main/macro/"><img src="images/language_macro.png" height="20"/></a> [showRGB_x.ijm](https://github.com/clij/clij-advanced-filters/blob/master/src/main/macro/showRGB_x.ijm)  


[Back to CLIJ documentation](https://clij.github.io/)

[Imprint](https://clij.github.io/imprint)
