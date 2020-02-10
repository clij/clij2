## pullAsROI
<img src="images/mini_empty_logo.png"/><img src="images/mini_clij2_logo.png"/><img src="images/mini_clijx_logo.png"/>

Pulls a binary image from the GPU memory and puts it on the currently active ImageJ window.

### Usage in ImageJ macro
```
Ext.CLIJx_pullAsROI(Image binary_input);
```


### Usage in Java
```
// init CLIJ and GPU
import net.haesleinhuepf.clij2.CLIJ;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
CLIJ2 clij2 = CLIJ2.getInstance();

// get input parameters
ClearCLBuffer binary_input = clij2.push(binary_inputImagePlus);
```

```
// Execute operation on GPU
Roi resultPullAsROI = clij2.pullAsROI(clij, binary_input);
```

```
//show result
System.out.println(resultPullAsROI);

// cleanup memory on GPU
clij2.release(binary_input);
```




### Example scripts
<a href="https://github.com/clij/clij-advanced-filters/blob/master/src/main/macro/"><img src="images/language_macro.png" height="20"/></a> [pullAsROI.ijm](https://github.com/clij/clij-advanced-filters/blob/master/src/main/macro/pullAsROI.ijm)  


[Back to CLIJ documentation](https://clij.github.io/)

[Imprint](https://clij.github.io/imprint)
