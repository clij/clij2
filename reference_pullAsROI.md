## pullAsROI
![Image](images/mini_clijx_logo.png)

Pulls a binary image from the GPU memory and puts it on the currently active ImageJ window.

### Usage in ImageJ macro
```
Ext.CLIJx_pullAsROI(Image binary_input);
```


### Usage in Java
```
// init CLIJ and GPU
import net.haesleinhuepf.clijx.CLIJ;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
CLIJx clijx = CLIJx.getInstance();

// get input parameters
ClearCLBuffer binary_input = clijx.push(binary_inputImagePlus);
```

```
// Execute operation on GPU
Roi resultPullAsROI = clijx.pullAsROI(clij, binary_input);
```

```
//show result
System.out.println(resultPullAsROI);

// cleanup memory on GPU
binary_input.close();
```




### Example scripts
<a href="https://github.com/clij/clij-advanced-filters/blob/master/src/main/macro/"><img src="images/language_macro.png" height="20"/></a> [pullAsROI.ijm](https://github.com/clij/clij-advanced-filters/blob/master/src/main/macro/pullAsROI.ijm)  


[Back to CLIJ documentation](https://clij.github.io/)

[Imprint](https://clij.github.io/imprint)
