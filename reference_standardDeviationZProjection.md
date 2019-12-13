## standardDeviationZProjection
![Image](images/mini_clijx_logo.png)

Determines the standard deviation projection of an image along Z.

### Usage in ImageJ macro
```
Ext.CLIJx_standardDeviationZProjection(Image source, Image destination);
```


### Usage in Java
```
// init CLIJ and GPU
import net.haesleinhuepf.clijx.CLIJ;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
CLIJx clijx = CLIJx.getInstance();

// get input parameters
ClearCLBuffer source = clijx.push(sourceImagePlus);
destination = clij.create(source);
```

```
// Execute operation on GPU
clijx.standardDeviationZProjection(clij, source, destination);
```

```
//show result
destinationImagePlus = clij.pull(destination);
destinationImagePlus.show();

// cleanup memory on GPU
source.close();
destination.close();
```




### Example scripts
<a href="https://github.com/clij/clij-advanced-filters/blob/master/src/main/macro/"><img src="images/language_macro.png" height="20"/></a> [projections.ijm](https://github.com/clij/clij-advanced-filters/blob/master/src/main/macro/projections.ijm)  
<a href="https://github.com/clij/clij-advanced-filters/blob/master/src/main/macro/"><img src="images/language_macro.png" height="20"/></a> [standardDeviationZProjectionComparion.ijm](https://github.com/clij/clij-advanced-filters/blob/master/src/main/macro/standardDeviationZProjectionComparion.ijm)  
<a href="https://github.com/clij/clij-advanced-filters/blob/master/src/main/macro/"><img src="images/language_macro.png" height="20"/></a> [standardDeviationZProjectionComparion.ijm](https://github.com/clij/clij-advanced-filters/blob/master/src/main/macro/standardDeviationZProjectionComparion.ijm)  


[Back to CLIJ documentation](https://clij.github.io/)

[Imprint](https://clij.github.io/imprint)
