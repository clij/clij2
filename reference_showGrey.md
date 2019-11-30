## showGrey
![Image](images/mini_clijx_logo.png)

Visualises a single 2D image.

### Usage in ImageJ macro
```
Ext.CLIJx_showGrey(Image input, String title);
```


### Usage in Java
```
// init CLIJ and GPU
import net.haesleinhuepf.clijx.CLIJ;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
CLIJx clijx = CLIJx.getInstance();

// get input parameters
ClearCLBuffer arg1 = clijx.push(arg1ImagePlus);
```

```
// Execute operation on GPU
clijx.showGrey(clij, arg1, arg2);
```

```
//show result

// cleanup memory on GPU
arg1.close();
```




### Example scripts
<a href="https://github.com/clij/clij-advanced-filters/blob/master/src/main/macro/"><img src="images/language_macro.png" height="20"/></a> [showRGB.ijm](https://github.com/clij/clij-advanced-filters/blob/master/src/main/macro/showRGB.ijm)  


[Back to CLIJ documentation](https://clij.github.io/)

[Imprint](https://clij.github.io/imprint)
