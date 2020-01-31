## pullLabelsToROIManager
![Image](images/mini_clijx_logo.png)

Pulls all labels in a label map as ROIs to the ROI manager.

### Usage in ImageJ macro
```
Ext.CLIJx_pullLabelsToROIManager(Image binary_input);
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
clijx.pullLabelsToROIManager(clij, arg1, arg2);
```

```
//show result

// cleanup memory on GPU
arg1.close();
```




### Example scripts
<a href="https://github.com/clij/clij-advanced-filters/blob/master/src/main/macro/"><img src="images/language_macro.png" height="20"/></a> [pullLabelsToROIManager.ijm](https://github.com/clij/clij-advanced-filters/blob/master/src/main/macro/pullLabelsToROIManager.ijm)  


[Back to CLIJ documentation](https://clij.github.io/)

[Imprint](https://clij.github.io/imprint)
