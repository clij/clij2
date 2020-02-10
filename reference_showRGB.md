## showRGB
<img src="images/mini_empty_logo.png"/><img src="images/mini_empty_logo.png"/><img src="images/mini_clijx_logo.png"/>

Visualises three 2D images as one RGB image

### Usage in ImageJ macro
```
Ext.CLIJx_showRGB(Image red, Image green, Image blue, String title);
```


### Usage in Java
```
// init CLIJ and GPU
import net.haesleinhuepf.clij2.CLIJ;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
CLIJ2 clij2 = CLIJ2.getInstance();

// get input parameters
ClearCLBuffer red = clij2.push(redImagePlus);
ClearCLBuffer green = clij2.push(greenImagePlus);
ClearCLBuffer blue = clij2.push(blueImagePlus);
```

```
// Execute operation on GPU
clij2.showRGB(clij, red, green, blue, title);
```

```
//show result

// cleanup memory on GPU
clij2.release(red);
clij2.release(green);
clij2.release(blue);
```




### Example scripts
<a href="https://github.com/clij/clij-advanced-filters/blob/master/src/main/macro/"><img src="images/language_macro.png" height="20"/></a> [showRGB.ijm](https://github.com/clij/clij-advanced-filters/blob/master/src/main/macro/showRGB.ijm)  
<a href="https://github.com/clij/clij-advanced-filters/blob/master/src/main/macro/"><img src="images/language_macro.png" height="20"/></a> [showRGB_x.ijm](https://github.com/clij/clij-advanced-filters/blob/master/src/main/macro/showRGB_x.ijm)  
<a href="https://github.com/clij/clij-advanced-filters/blob/master/src/main/jython/"><img src="images/language_jython.png" height="20"/></a> [jaccardIndex.py](https://github.com/clij/clij-advanced-filters/blob/master/src/main/jython/jaccardIndex.py)  


[Back to CLIJ documentation](https://clij.github.io/)

[Imprint](https://clij.github.io/imprint)
